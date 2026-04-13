package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalNode;

import java.util.List;
import java.util.Map;

public interface IApprovalProcessService {
    Map<String, Object> getCurrentNode(String processCode, String currentState);

    Map<String, Object> submitApproval(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept);

    Map<String, Object> approve(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept);

    Map<String, Object> reject(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept);

    boolean canApprove(Long userId, List<Long> deptIds, List<String> roleKeys);

    List<SysApprovalHistory> getApprovalHistory(String processCode, Long businessId);

    List<SysApprovalNode> getProcessNodes(String processCode);

    Map<String, Object> recall(String processCode, Long businessId, String currentState,
            String comment, Long operatorId, String operatorName, String operatorDept);
}
