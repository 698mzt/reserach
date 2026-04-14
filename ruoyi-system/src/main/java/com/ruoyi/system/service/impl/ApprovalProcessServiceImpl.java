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
            processQuery.setStatus("1");
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

            if (!"1".equals(currentNode.getCanBack())) {
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
            processQuery.setStatus("1");
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
            int currentNodeIndex = -1;

            for (int i = 0; i < nodeList.size(); i++) {
                SysApprovalNode node = nodeList.get(i);
                if (currentState.equals(node.getPassState()) || currentState.equals(node.getRejectState())) {
                    currentNode = node;
                    currentNodeIndex = i;
                    break;
                }
            }

            if (currentNode == null) {
                result.put("message", "未找到当前状态对应的节点");
                return result;
            }

            String recallState = null;

            if (currentNodeIndex == 0) {
                recallState = currentNode.getRejectState();
            } else {
                SysApprovalNode prevNode = nodeList.get(currentNodeIndex - 1);
                recallState = prevNode.getPassState();
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
}
