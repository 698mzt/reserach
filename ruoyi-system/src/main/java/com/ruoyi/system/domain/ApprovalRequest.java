package com.ruoyi.system.domain;

import lombok.Data;
import java.util.List;

/**
 * 审批请求对象
 * 用于封装审批流程中的请求信息，包括流程编码、业务ID、当前状态、
 * 操作人信息以及相关的角色和部门信息
 */
@Data
public class ApprovalRequest {

    /** 流程编码，标识具体的审批流程类型 */
    private String processCode;

    /** 业务ID，关联的具体业务记录标识 */
    private Long businessId;

    /** 当前审批状态 */
    private String currentState;

    /** 审批意见或备注 */
    private String comment;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 操作人所属部门 */
    private String operatorDept;

    /** 操作人角色键列表，用于权限判断 */
    private List<String> operatorRoleKeys;

    /** 操作人部门ID列表，支持多部门场景 */
    private List<Long> operatorDeptIds;

    /**
     * 创建审批请求对象（基础版本）
     *
     * @param processCode 流程编码
     * @param businessId 业务ID
     * @param currentState 当前审批状态
     * @param comment 审批意见
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人所属部门
     * @return 封装好的审批请求对象
     */
    public static ApprovalRequest of(String processCode, Long businessId, String currentState,
                                     String comment, Long operatorId, String operatorName, String operatorDept) {
        ApprovalRequest req = new ApprovalRequest();
        req.setProcessCode(processCode);
        req.setBusinessId(businessId);
        req.setCurrentState(currentState);
        req.setComment(comment);
        req.setOperatorId(operatorId);
        req.setOperatorName(operatorName);
        req.setOperatorDept(operatorDept);
        return req;
    }

    /**
     * 创建审批请求对象（完整版本）
     * 包含操作人的角色和部门信息，用于复杂的权限控制场景
     *
     * @param processCode 流程编码
     * @param businessId 业务ID
     * @param currentState 当前审批状态
     * @param comment 审批意见
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人所属部门
     * @param operatorRoleKeys 操作人角色键列表
     * @param operatorDeptIds 操作人部门ID列表
     * @return 封装好的审批请求对象
     */
    public static ApprovalRequest of(String processCode, Long businessId, String currentState,
                                     String comment, Long operatorId, String operatorName, String operatorDept,
                                     List<String> operatorRoleKeys, List<Long> operatorDeptIds) {
        ApprovalRequest req = of(processCode, businessId, currentState, comment,
                operatorId, operatorName, operatorDept);
        req.setOperatorRoleKeys(operatorRoleKeys);
        req.setOperatorDeptIds(operatorDeptIds);
        return req;
    }
}
