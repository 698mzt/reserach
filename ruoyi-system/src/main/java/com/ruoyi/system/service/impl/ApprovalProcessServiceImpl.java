package com.ruoyi.system.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISysApprovalNodeService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalProcessServiceImpl implements IApprovalProcessService {
    @Autowired
    private SysApprovalProcessMapper sysApprovalProcessMapper;

    @Autowired
    private ISysApprovalNodeService sysApprovalNodeService;

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

    @Override
    public Map<String, Object> getCurrentNode(String processCode, String currentState) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        if (StringUtils.isEmpty(processCode) || StringUtils.isEmpty(currentState)) {
            result.put("message", "参数不能为空");
            return result;
        }

        try {
            SysApprovalProcess processQuery = new SysApprovalProcess();
            processQuery.setProcessCode(processCode);
            processQuery.setStatus("0");
            List<SysApprovalProcess> processList = sysApprovalProcessMapper.selectSysApprovalProcessList(processQuery);

            if (processList == null || processList.isEmpty()) {
                result.put("message", "流程配置不存在或已停用");
                return result;
            }

            SysApprovalProcess process = processList.get(0);
            List<SysApprovalNode> nodeList = sysApprovalNodeService.selectSysApprovalNodeByProcessId(process.getId());

            if (nodeList == null || nodeList.isEmpty()) {
                result.put("message", "流程节点配置不存在");
                return result;
            }

            SysApprovalNode currentNode = null;
            SysApprovalNode nextNode = null;

            // 优先根据 nodeCode 字段找节点
            for (SysApprovalNode node : nodeList) {
                if (currentState.equals(node.getNodeCode())) {
                    currentNode = node;
                    int currentIndex = nodeList.indexOf(node);
                    if (currentIndex < nodeList.size() - 1) {
                        nextNode = nodeList.get(currentIndex + 1);
                    }
                    break;
                }
            }

            // 如果根据 nodeCode 没找到，再使用原来的逻辑
            if (currentNode == null) {
                for (SysApprovalNode node : nodeList) {
                    if (currentState.equals(node.getRejectState())) {
                        currentNode = node;
                        break;
                    }
                    if (currentState.equals(node.getPassState())) {
                        int currentIndex = nodeList.indexOf(node);
                        if (currentIndex < nodeList.size() - 1) {
                            nextNode = nodeList.get(currentIndex + 1);
                        }
                        currentNode = node;
                        break;
                    }
                
                }
            }

            if (currentNode == null) {
                result.put("message", "未找到当前状态对应的节点");
                return result;
            }

            result.put("success", true);
            result.put("currentNode", currentNode);
            result.put("nextNode", nextNode);
            result.put("nodeList", nodeList);
            result.put("process", process);

        } catch (Exception e) {
            log.error("获取当前审批节点异常", e);
            result.put("message", "系统异常：" + e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitApproval(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        if (StringUtils.isEmpty(processCode) || businessId == null || StringUtils.isEmpty(currentState)) {
            result.put("message", "参数不能为空");
            return result;
        }

        try {
            Map<String, Object> nodeResult = getCurrentNode(processCode, currentState);
            if (!(Boolean) nodeResult.get("success")) {
                return nodeResult;
            }

            SysApprovalNode currentNode = (SysApprovalNode) nodeResult.get("currentNode");
            SysApprovalNode nextNode = (SysApprovalNode) nodeResult.get("nextNode");

            String newState = currentNode.getPassState();
            if (nextNode != null) {
                newState = nextNode.getRejectState();
            }

            saveApprovalHistory(processCode, businessId, currentNode, "submit",
                    operatorId, operatorName, operatorDept, currentState, newState, comment);

            result.put("success", true);
            result.put("newState", newState);
            result.put("message", "提交成功");

        } catch (Exception e) {
            log.error("提交审批异常", e);
            result.put("message", "系统异常：" + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> approve(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        if (StringUtils.isEmpty(processCode) || businessId == null || StringUtils.isEmpty(currentState)) {
            result.put("message", "参数不能为空");
            return result;
        }

        try {
            Map<String, Object> nodeResult = getCurrentNode(processCode, currentState);
            if (!(Boolean) nodeResult.get("success")) {
                return nodeResult;
            }

            SysApprovalNode currentNode = (SysApprovalNode) nodeResult.get("currentNode");
            SysApprovalNode nextNode = (SysApprovalNode) nodeResult.get("nextNode");

            String newState = currentNode.getPassState();

            saveApprovalHistory(processCode, businessId, currentNode, "approve",
                    operatorId, operatorName, operatorDept, currentState, newState, comment);

            result.put("success", true);
            result.put("newState", newState);
            result.put("nextNode", nextNode);
            result.put("isLast", nextNode == null);
            result.put("message", nextNode == null ? "审批通过，流程结束" : "审批通过，进入下一节点");

        } catch (Exception e) {
            log.error("审批通过异常", e);
            result.put("message", "系统异常：" + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> reject(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        if (StringUtils.isEmpty(processCode) || businessId == null || StringUtils.isEmpty(currentState)) {
            result.put("message", "参数不能为空");
            return result;
        }

        try {
            Map<String, Object> nodeResult = getCurrentNode(processCode, currentState);
            if (!(Boolean) nodeResult.get("success")) {
                return nodeResult;
            }

            SysApprovalNode currentNode = (SysApprovalNode) nodeResult.get("currentNode");

            if (!"1".equals(currentNode.getCanBack()) && !"Y".equals(currentNode.getCanBack())) {
                result.put("message", "当前节点不允许驳回");
                return result;
            }

            String newState = currentNode.getRejectState();

            saveApprovalHistory(processCode, businessId, currentNode, "reject",
                    operatorId, operatorName, operatorDept, currentState, newState, comment);

            result.put("success", true);
            result.put("newState", newState);
            result.put("message", "驳回成功");

        } catch (Exception e) {
            log.error("审批驳回异常", e);
            result.put("message", "系统异常：" + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public boolean canApprove(Long userId, List<Long> deptIds, List<String> roleKeys) {
        if (userId == null) {
            return false;
        }
        if (deptIds == null && roleKeys == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<SysApprovalHistory> getApprovalHistory(String processCode, Long businessId) {
        if (StringUtils.isEmpty(processCode) || businessId == null) {
            return new ArrayList<>();
        }
        return sysApprovalHistoryService.selectSysApprovalHistoryByBusinessId(processCode, businessId);
    }

    @Override
    public List<SysApprovalNode> getProcessNodes(String processCode) {
        if (StringUtils.isEmpty(processCode)) {
            return new ArrayList<>();
        }

        try {
            SysApprovalProcess processQuery = new SysApprovalProcess();
            processQuery.setProcessCode(processCode);
            processQuery.setStatus("0");
            List<SysApprovalProcess> processList = sysApprovalProcessMapper.selectSysApprovalProcessList(processQuery);

            if (processList == null || processList.isEmpty()) {
                return new ArrayList<>();
            }

            return sysApprovalNodeService.selectSysApprovalNodeByProcessId(processList.get(0).getId());

        } catch (Exception e) {
            log.error("获取流程节点异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recall(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        if (StringUtils.isEmpty(processCode) || businessId == null || StringUtils.isEmpty(currentState)) {
            result.put("message", "参数不能为空");
            return result;
        }

        try {
            Map<String, Object> nodeResult = getCurrentNode(processCode, currentState);
            if (!(Boolean) nodeResult.get("success")) {
                return nodeResult;
            }

            SysApprovalProcess process = (SysApprovalProcess) nodeResult.get("process");
            List<SysApprovalNode> nodeList = (List<SysApprovalNode>) nodeResult.get("nodeList");
            SysApprovalNode currentNode = (SysApprovalNode) nodeResult.get("currentNode");

            int currentNodeIndex = -1;
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).getId().equals(currentNode.getId())) {
                    currentNodeIndex = i;
                    break;
                }
            }

            if (currentNodeIndex == -1) {
                result.put("message", "未找到当前节点在流程中的位置");
                return result;
            }

            String recallState = null;

            if (currentNodeIndex == 0) {
                // 第一节点撤回时，回到初始状态
                // 从节点列表中获取第一个节点的 rejectState 作为初始状态
                SysApprovalNode firstNode = nodeList.get(0);
                recallState = firstNode.getRejectState();
                if (StringUtils.isEmpty(recallState)) {
                    recallState = "PAPER_DRAFT";
                }
            } else {
                // 使用当前节点的 rejectState 作为撤回状态
                recallState = currentNode.getRejectState();
                if (StringUtils.isEmpty(recallState)) {
                    // 如果当前节点没有配置 rejectState，使用前一个节点的 passState
                    SysApprovalNode prevNode = nodeList.get(currentNodeIndex - 1);
                    recallState = prevNode.getPassState();
                }
            }

            if (StringUtils.isEmpty(recallState)) {
                result.put("message", "无法确定撤回目标状态");
                return result;
            }

            saveApprovalHistory(processCode, businessId, currentNode, "recall",
                    operatorId, operatorName, operatorDept, currentState, recallState, comment);

            result.put("success", true);
            result.put("newState", recallState);
            result.put("message", "撤回成功");

        } catch (Exception e) {
            log.error("撤回审批异常", e);
            result.put("message", "系统异常：" + e.getMessage());
            throw new RuntimeException(e);
        }

        return result;
    }

/**
 * 保存审批历史记录方法
 * @param processCode 流程编码
 * @param businessId 业务ID
 * @param node 审批节点信息
 * @param action 操作类型
 * @param operatorId 操作人ID
 * @param operatorName 操作人姓名
 * @param operatorDept 操作人部门
 * @param oldState 原状态
 * @param newState 新状态
 * @param comment 备注信息
 */
    private void saveApprovalHistory(String processCode, Long businessId, SysApprovalNode node, String action,
            Long operatorId, String operatorName, String operatorDept,
            String oldState, String newState, String comment) {
        // 创建审批历史记录对象
        SysApprovalHistory history = new SysApprovalHistory();
        // 设置流程编码
        history.setProcessCode(processCode);
        // 设置业务ID
        history.setBusinessId(businessId);
        // 设置节点ID（如果节点不为空）
        history.setNodeId(node != null ? node.getId() : null);
        // 设置节点名称（如果节点不为空，否则为空字符串）
        history.setNodeName(node != null ? node.getNodeNm() : "");
        // 设置操作类型
        history.setAction(action);
        // 设置操作人ID
        history.setOperatorId(operatorId);
        // 设置操作人姓名
        history.setOperatorName(operatorName);
        // 设置操作人部门
        history.setOperatorDept(operatorDept);
        // 设置原状态
        history.setOldState(oldState);
        // 设置新状态
        history.setNewState(newState);
        // 设置备注信息
        history.setComment(comment);
        // 创建当前时间
        history.setCreateTime(new Date());

        // 调用服务层方法插入审批历史记录
        sysApprovalHistoryService.insertSysApprovalHistory(history);
    }
}
