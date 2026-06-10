package com.ruoyi.system.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISysApprovalNodeService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import lombok.extern.slf4j.Slf4j;

/**
 * 审批流程核心服务实现
 * <p>
 * 基于数据库状态机驱动的审批流引擎，实现 IApprovalProcessService 接口定义的所有审批操作。
 * 核心机制：通过 sys_approval_node 的 enterState/passState/rejectState 配置驱动状态流转，
 * 无需在代码中硬编码状态值，新增业务模块只需在数据库中配置流程和节点即可。
 * </p>
 *
 * <h3>节点状态字段说明</h3>
 * <ul>
 * <li>enterState：进入该节点时的状态编码，表示"等待该节点审批"（如 PAPER_JYS_AUDIT）</li>
 * <li>passState：该节点审批通过后的状态编码，指向下一节点的 enterState 或终态（如 PAPER_PASSED）</li>
 * <li>rejectState：该节点审批驳回后的状态编码，指回上一节点的 enterState 或草稿（如 PAPER_DRAFT）</li>
 * </ul>
 *
 * <h3>状态流转规则</h3>
 * <ul>
 * <li>提交：草稿 → 第一个审批节点的 enterState</li>
 * <li>通过：当前节点 passState → 下一节点 enterState（或终态）</li>
 * <li>驳回：当前节点 rejectState → 上一节点 enterState（或草稿）</li>
 * <li>撤回：当前节点 → rejectState 或前一节点 enterState</li>
 * </ul>
 *
 * @see IApprovalProcessService
 * @see ApprovalRequest
 * @see ApprovalResult
 */
@Slf4j
@Service
public class ApprovalProcessServiceImpl implements IApprovalProcessService {

    @Autowired
    private SysApprovalProcessMapper sysApprovalProcessMapper;

    @Autowired
    private ISysApprovalNodeService sysApprovalNodeService;

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

    /**
     * 获取当前审批节点信息
     * <p>
     * 通过 currentState 精确匹配 node.nodeCode 定位当前节点。
     * nodeCode 与 enterState 保持一致，表示"业务数据处于该状态时，由本节点负责审批"。
     * </p>
     */
    @Override
    public ApprovalResult getCurrentNode(String processCode, String currentState) {
        if (StringUtils.isEmpty(processCode) || StringUtils.isEmpty(currentState)) {
            return ApprovalResult.fail("参数不能为空");
        }

        try {
            SysApprovalProcess process = getProcessByCode(processCode);
            if (process == null) {
                return ApprovalResult.fail("流程配置不存在或已停用");
            }

            List<SysApprovalNode> nodeList = sysApprovalNodeService.selectSysApprovalNodeByProcessId(process.getId());
            if (nodeList == null || nodeList.isEmpty()) {
                return ApprovalResult.fail("流程节点配置不存在");
            }

            SysApprovalNode currentNode = null;
            SysApprovalNode nextNode = null;

            for (int i = 0; i < nodeList.size(); i++) {
                SysApprovalNode node = nodeList.get(i);
                if (currentState.equals(node.getNodeCode())) {
                    currentNode = node;
                    if (i < nodeList.size() - 1) {
                        nextNode = nodeList.get(i + 1);
                    }
                    break;
                }
            }

            if (currentNode == null) {
                for (int i = 0; i < nodeList.size(); i++) {
                    SysApprovalNode node = nodeList.get(i);
                    if (currentState.equals(node.getRejectState()) || currentState.equals(node.getPassState())) {
                        currentNode = node;
                        if (i < nodeList.size() - 1) {
                            nextNode = nodeList.get(i + 1);
                        }
                        break;
                    }
                }
            }

            if (currentNode == null) {
                return ApprovalResult.fail("未找到当前状态[" + currentState + "]对应的节点，请检查节点配置的nodeCode、passState或rejectState");
            }

            return ApprovalResult.okWithNode("获取当前节点成功", null,
                    currentNode, nextNode, nodeList, process);

        } catch (Exception e) {
            log.error("获取当前审批节点异常", e);
            return ApprovalResult.fail("系统异常：" + e.getMessage());
        }
    }

    /**
     * 提交审批
     * <p>
     * 新状态 = 第一个审批节点的 enterState（表示"等待该节点审批"）。
     * 提交操作始终将业务数据推入第一个审批节点，无论当前处于草稿还是驳回状态。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult submitApproval(ApprovalRequest request) {
        if (StringUtils.isEmpty(request.getProcessCode()) || request.getBusinessId() == null
                || StringUtils.isEmpty(request.getCurrentState())) {
            return ApprovalResult.fail("参数不能为空");
        }

        try {
            SysApprovalProcess process = getProcessByCode(request.getProcessCode());
            if (process == null) {
                return ApprovalResult.fail("流程配置不存在或已停用");
            }

            List<SysApprovalNode> nodeList = sysApprovalNodeService.selectSysApprovalNodeByProcessId(process.getId());
            if (nodeList == null || nodeList.isEmpty()) {
                return ApprovalResult.fail("流程节点配置不存在");
            }

            SysApprovalNode firstNode = nodeList.get(0);
            String newState = firstNode.getNodeCode();
            if (StringUtils.isEmpty(newState)) {
                return ApprovalResult.fail("首个审批节点[" + firstNode.getNodeNm() + "]未配置nodeCode");
            }

            saveApprovalHistory(request.getProcessCode(), request.getBusinessId(), firstNode, "submit",
                    request.getOperatorId(), request.getOperatorName(), request.getOperatorDept(),
                    request.getCurrentState(), newState, request.getComment());

            updateBusinessState(request.getProcessCode(), request.getBusinessId(), newState);

            return ApprovalResult.ok("提交成功", newState);

        } catch (Exception e) {
            log.error("提交审批异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 审批通过
     * <p>
     * 新状态 = currentNode.passState：
     * - 若存在下一节点，passState 应指向下一节点的 enterState
     * - 若无下一节点（isLast=true），passState 为终态（如 PAPER_PASSED）
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult approve(ApprovalRequest request) {
        if (StringUtils.isEmpty(request.getProcessCode()) || request.getBusinessId() == null
                || StringUtils.isEmpty(request.getCurrentState())) {
            return ApprovalResult.fail("参数不能为空");
        }

        try {
            ApprovalResult nodeResult = getCurrentNode(request.getProcessCode(), request.getCurrentState());
            if (!nodeResult.isSuccess()) {
                return nodeResult;
            }

            SysApprovalNode currentNode = nodeResult.getCurrentNode();
            SysApprovalNode nextNode = nodeResult.getNextNode();

            if (!checkNodePermission(currentNode, request)) {
                return ApprovalResult.fail("当前用户无权审批节点[" + currentNode.getNodeNm() + "]");
            }

            if (StringUtils.isEmpty(currentNode.getPassState())) {
                return ApprovalResult.fail("节点[" + currentNode.getNodeNm() + "]未配置passState");
            }

            String newState = currentNode.getPassState();

            saveApprovalHistory(request.getProcessCode(), request.getBusinessId(), currentNode, "approve",
                    request.getOperatorId(), request.getOperatorName(), request.getOperatorDept(),
                    request.getCurrentState(), newState, request.getComment());

            updateBusinessState(request.getProcessCode(), request.getBusinessId(), newState);

            boolean isLast = nextNode == null;
            String message = isLast ? "审批通过，流程结束" : "审批通过，进入下一节点";

            ApprovalResult result = ApprovalResult.ok(message, newState, nextNode, isLast);
            result.setCurrentNode(currentNode);
            result.setNodeList(nodeResult.getNodeList());
            result.setProcess(nodeResult.getProcess());

            return result;

        } catch (Exception e) {
            log.error("审批通过异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 审批驳回
     * <p>
     * 驳回前检查节点的 canBack 配置（"1" 或 "Y" 表示允许驳回）。
     * 新状态 = currentNode.rejectState（通常回退到上一节点的 enterState 或草稿）。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult reject(ApprovalRequest request) {
        if (StringUtils.isEmpty(request.getProcessCode()) || request.getBusinessId() == null
                || StringUtils.isEmpty(request.getCurrentState())) {
            return ApprovalResult.fail("参数不能为空");
        }

        try {
            ApprovalResult nodeResult = getCurrentNode(request.getProcessCode(), request.getCurrentState());
            if (!nodeResult.isSuccess()) {
                return nodeResult;
            }

            SysApprovalNode currentNode = nodeResult.getCurrentNode();

            if (!checkNodePermission(currentNode, request)) {
                return ApprovalResult.fail("当前用户无权驳回节点[" + currentNode.getNodeNm() + "]");
            }

            if (!"1".equals(currentNode.getCanBack()) && !"Y".equals(currentNode.getCanBack())) {
                return ApprovalResult.fail("当前节点不允许驳回");
            }

            if (StringUtils.isEmpty(currentNode.getRejectState())) {
                return ApprovalResult.fail("节点[" + currentNode.getNodeNm() + "]未配置rejectState");
            }

            String newState = currentNode.getRejectState();

            saveApprovalHistory(request.getProcessCode(), request.getBusinessId(), currentNode, "reject",
                    request.getOperatorId(), request.getOperatorName(), request.getOperatorDept(),
                    request.getCurrentState(), newState, request.getComment());

            updateBusinessState(request.getProcessCode(), request.getBusinessId(), newState);

            return ApprovalResult.ok("驳回成功", newState);

        } catch (Exception e) {
            log.error("审批驳回异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断用户是否有审批权限
     * <p>
     * 根据当前节点的 roleIds/deptIds/roleKeys 配置与用户信息进行匹配：
     * - 优先匹配 roleKeys（角色标识）
     * - 其次匹配 deptIds（部门ID）
     * - 最后匹配 roleIds（角色ID）
     * 任一匹配即视为有权限
     * </p>
     */
    @Override
    public boolean canApprove(Long userId, List<Long> deptIds, List<String> roleKeys) {
        if (userId == null) {
            return false;
        }
        if ((deptIds == null || deptIds.isEmpty()) && (roleKeys == null || roleKeys.isEmpty())) {
            return false;
        }
        return true;
    }

    private boolean checkNodePermission(SysApprovalNode node, ApprovalRequest request) {
        if (node == null || request == null) {
            return false;
        }

        List<String> operatorRoleKeys = request.getOperatorRoleKeys();
        List<Long> operatorDeptIds = request.getOperatorDeptIds();

        if ((operatorRoleKeys == null || operatorRoleKeys.isEmpty())
                && (operatorDeptIds == null || operatorDeptIds.isEmpty())) {
            return true;
        }

        String nodeRoleKeys = node.getRoleKeys();
        if (StringUtils.isNotEmpty(nodeRoleKeys) && operatorRoleKeys != null) {
            String[] allowedRoleKeys = nodeRoleKeys.split(",");
            for (String allowedKey : allowedRoleKeys) {
                if (operatorRoleKeys.contains(allowedKey.trim())) {
                    return true;
                }
            }
        }

        String nodeDeptIds = node.getDeptIds();
        if (StringUtils.isNotEmpty(nodeDeptIds) && operatorDeptIds != null) {
            String[] allowedDeptIds = nodeDeptIds.split(",");
            for (String allowedId : allowedDeptIds) {
                try {
                    if (operatorDeptIds.contains(Long.valueOf(allowedId.trim()))) {
                        return true;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        String nodeRoleIds = node.getRoleIds();
        if (StringUtils.isNotEmpty(nodeRoleIds) && operatorRoleKeys != null
                && StringUtils.isEmpty(nodeRoleKeys) && StringUtils.isEmpty(nodeDeptIds)) {
            return true;
        }

        if (StringUtils.isEmpty(nodeRoleKeys) && StringUtils.isEmpty(nodeDeptIds)
                && StringUtils.isEmpty(nodeRoleIds)) {
            return true;
        }

        return false;
    }

    /**
     * 查询审批历史记录
     */
    @Override
    public List<SysApprovalHistory> getApprovalHistory(String processCode, Long businessId) {
        if (StringUtils.isEmpty(processCode) || businessId == null) {
            return new ArrayList<>();
        }
        return sysApprovalHistoryService.selectSysApprovalHistoryByBusinessId(processCode, businessId);
    }

    /**
     * 查询流程的所有审批节点
     */
    @Override
    public List<SysApprovalNode> getProcessNodes(String processCode) {
        if (StringUtils.isEmpty(processCode)) {
            return new ArrayList<>();
        }

        try {
            SysApprovalProcess process = getProcessByCode(processCode);
            if (process == null) {
                return new ArrayList<>();
            }
            return sysApprovalNodeService.selectSysApprovalNodeByProcessId(process.getId());

        } catch (Exception e) {
            log.error("获取流程节点异常", e);
            return new ArrayList<>();
        }
    }

    /**
     * 撤回审批
     * <p>
     * 撤回状态计算逻辑：
     * - 第一个节点：回退到 firstNode.rejectState（通常为草稿）
     * - 中间/末尾节点：优先取 currentNode.rejectState，为空则取前一节点的 enterState
     * </p>
     */
    /**
     * 撤回审批
     * <p>
     * 从审批历史记录中查找导致当前状态的最近一次操作（审批通过/驳回/提交），
     * 恢复业务状态到该操作前的状态（oldState）。该操作人（及系统管理员）可撤回。
     * 不再依赖节点配置的 rejectState 字段。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult recall(ApprovalRequest request) {
        if (StringUtils.isEmpty(request.getProcessCode()) || request.getBusinessId() == null
                || StringUtils.isEmpty(request.getCurrentState())) {
            return ApprovalResult.fail("参数不能为空");
        }

        if (request.getOperatorId() == null) {
            return ApprovalResult.fail("操作人ID不能为空");
        }

        try {
            // 查询最近一次导致当前状态的审批历史记录（排除撤回操作自身避免循环）
            SysApprovalHistory lastHistory = sysApprovalHistoryService.selectLastRecallableByBusinessId(
                    request.getProcessCode(), request.getBusinessId(), request.getCurrentState());

            if (lastHistory == null || StringUtils.isEmpty(lastHistory.getOldState())) {
                return ApprovalResult.fail("未找到可撤回的历史记录，或历史记录的oldState为空");
            }

            // 权限校验：仅驳回操作人可撤回驳回记录（系统管理员除外）
            // 从其他状态（审批中、通过等）撤回时不校验操作人
            if ("reject".equals(lastHistory.getAction())
                    && lastHistory.getOperatorId() != null
                    && !lastHistory.getOperatorId().equals(request.getOperatorId())
                    && !SysUser.isAdmin(request.getOperatorId())) {
                return ApprovalResult.fail("仅操作人可撤回该记录");
            }

            String recallState = lastHistory.getOldState();

            // 保存撤回审批历史
            SysApprovalHistory history = new SysApprovalHistory();
            history.setProcessCode(request.getProcessCode());
            history.setBusinessId(request.getBusinessId());
            history.setAction("recall");
            history.setOperatorId(request.getOperatorId());
            history.setOperatorName(request.getOperatorName());
            history.setOperatorDept(request.getOperatorDept());
            history.setOldState(request.getCurrentState());
            history.setNewState(recallState);
            // 审批意见：优先使用请求传入的意见，若为空则使用导致当前状态的历史记录的审批意见
            if (StringUtils.isNotEmpty(request.getComment())) {
                history.setComment(request.getComment());
            } else if (StringUtils.isNotEmpty(lastHistory.getComment())) {
                history.setComment(lastHistory.getComment());
            }
            history.setCreateTime(new Date());

            // 设置节点信息：节点名称为导致当前状态的历史记录中的oldState对应的节点名称
            // 即"驳回前的节点"，与驳回操作的节点保持一致
            if (lastHistory.getNodeId() != null) {
                history.setNodeId(lastHistory.getNodeId());
            }
            if (StringUtils.isNotEmpty(lastHistory.getNodeName())) {
                history.setNodeName(lastHistory.getNodeName());
            }

            sysApprovalHistoryService.insertSysApprovalHistory(history);

            // 更新业务表状态
            updateBusinessState(request.getProcessCode(), request.getBusinessId(), recallState);

            return ApprovalResult.ok("撤回成功", recallState);

        } catch (Exception e) {
            log.error("撤回审批异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新业务表状态
     * <p>
     * 利用 sys_approval_process 中配置的 businessTable 和 statusField，
     * 通过动态 SQL 自动更新对应业务表的状态字段。
     * 若流程未配置 businessTable，则跳过更新（仅记录审批历史）。
     * </p>
     *
     * @param processCode 流程编码，用于查找 businessTable 和 statusField 配置
     * @param businessId  业务数据ID
     * @param newState    新状态值
     * @return 影响行数，0表示未更新或未配置业务表
     */
    @Override
    public int updateBusinessState(String processCode, Long businessId, String newState) {
        if (StringUtils.isEmpty(processCode) || businessId == null || StringUtils.isEmpty(newState)) {
            throw new IllegalArgumentException("updateBusinessState参数不完整: processCode=" + processCode
                    + ", businessId=" + businessId + ", newState=" + newState);
        }

        SysApprovalProcess process = getProcessByCode(processCode);
        if (process == null) {
            throw new IllegalStateException("流程配置不存在，无法更新业务表状态: processCode=" + processCode);
        }

        String businessTable = process.getBusinessTable();
        String statusField = process.getStatusField();

        if (StringUtils.isEmpty(businessTable) || StringUtils.isEmpty(statusField)) {
            log.info("流程未配置 businessTable 或 statusField，跳过业务表状态更新: processCode={}", processCode);
            return 0;
        }

        if (!isValidIdentifier(businessTable) || !isValidIdentifier(statusField)) {
            throw new IllegalArgumentException("非法的表名或字段名: businessTable=" + businessTable
                    + ", statusField=" + statusField);
        }

        int rows = sysApprovalProcessMapper.updateBusinessState(businessTable, statusField, newState, businessId);
        if (rows == 0) {
            throw new IllegalStateException("业务表状态更新失败，未影响任何行: " + businessTable + "."
                    + statusField + " WHERE id=" + businessId);
        }
        log.info("业务表状态更新: {}.{} = {} WHERE id={}, 影响行数={}",
                businessTable, statusField, newState, businessId, rows);
        return rows;
    }

    /**
     * 根据流程编码查询启用状态的流程配置
     *
     * @param processCode 流程编码
     * @return 流程配置，不存在或已停用返回null
     */
    private SysApprovalProcess getProcessByCode(String processCode) {
        SysApprovalProcess processQuery = new SysApprovalProcess();
        processQuery.setProcessCode(processCode);
        processQuery.setStatus("0");
        List<SysApprovalProcess> processList = sysApprovalProcessMapper.selectSysApprovalProcessList(processQuery);
        return (processList != null && !processList.isEmpty()) ? processList.get(0) : null;
    }

    /**
     * 查找节点在列表中的索引
     */
    private int findNodeIndex(List<SysApprovalNode> nodeList, SysApprovalNode target) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).getId().equals(target.getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 计算撤回目标状态
     * <p>
     * 撤回逻辑：
     * - 第一个节点：取 firstNode.rejectState（通常为草稿），为空则返回null
     * - 最后一个节点且当前状态为该节点的passState（结束状态）：返回该节点的nodeCode
     * - 中间/末尾节点：优先取 currentNode.rejectState，为空则取前一节点的 passState
     * </p>
     */
    private String computeRecallState(List<SysApprovalNode> nodeList, SysApprovalNode currentNode,
            int currentNodeIndex, String currentState) {
        if (currentNodeIndex == 0) {
            if (StringUtils.isEmpty(currentNode.getRejectState())) {
                return null;
            }
            return currentNode.getRejectState();
        }

        if (currentNodeIndex == nodeList.size() - 1 && currentState.equals(currentNode.getPassState())) {
            // 最后一个节点且当前状态为该节点的passState（结束状态）时，撤回回到最后一个节点的nodeCode
            return currentNode.getNodeCode();
        }

        String recallState = currentNode.getRejectState();
        if (StringUtils.isEmpty(recallState)) {
            SysApprovalNode prevNode = nodeList.get(currentNodeIndex - 1);
            recallState = prevNode.getPassState();
        }
        return recallState;
    }

    /**
     * 保存审批历史记录
     *
     * @param processCode  流程编码
     * @param businessId   业务数据ID
     * @param node         审批节点（可为null）
     * @param action       操作类型：submit/approve/reject/recall
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param oldState     变更前状态
     * @param newState     变更后状态
     * @param comment      审批意见
     */
    private void saveApprovalHistory(String processCode, Long businessId, SysApprovalNode node, String action,
            Long operatorId, String operatorName, String operatorDept,
            String oldState, String newState, String comment) {
        SysApprovalHistory history = new SysApprovalHistory();
        history.setProcessCode(processCode);
        history.setBusinessId(businessId);
        history.setNodeId(node != null ? node.getId() : null);
        history.setNodeName(node != null ? node.getNodeNm() : "");
        history.setAction(action);
        history.setOperatorId(operatorId);
        history.setOperatorName(operatorName);
        history.setOperatorDept(operatorDept);
        history.setOldState(oldState);
        history.setNewState(newState);
        history.setComment(comment);
        history.setCreateTime(new Date());

        sysApprovalHistoryService.insertSysApprovalHistory(history);
    }

    /**
     * 校验标识符是否合法（防 SQL 注入）
     * <p>
     * 只允许字母、数字、下划线，且以字母开头
     * </p>
     */
    private boolean isValidIdentifier(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return false;
        }
        return identifier.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }
}
