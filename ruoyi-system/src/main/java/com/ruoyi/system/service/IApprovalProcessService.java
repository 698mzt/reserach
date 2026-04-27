package com.ruoyi.system.service;

import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalNode;

import java.util.List;

/**
 * 审批流程核心服务接口
 * <p>
 * 基于数据库状态机驱动的审批流引擎，通过 sys_approval_process（流程配置）、
 * sys_approval_node（节点配置）、sys_approval_state（状态字典）、
 * sys_approval_history（审批历史）四表联动实现。
 * </p>
 * <p>
 * 持通过、驳回、撤回操作。
 * 状态流转由节点配置的 passState/rejectState 驱动，无需硬编码状态值。
 * </p>
 *
 * @see ApprovalRequest 审批请求入参对象
 * @see ApprovalResult 审批结果返回对象
 */
public interface IApprovalProcessService {

    /**
     * 获取当前审批节点信息
     * <p>
     * 根据流程编码和当前状态，定位当前所处的审批节点及下一节点。
     * 采用双重定位策略：优先用 nodeCode 匹配，兜底用 enterState 匹配。
     * </p>
     *
     * @param processCode  流程编码，如 "PAPER_APPROVAL"、"LECTURE_APPROVAL"
     * @param currentState 当前业务状态编码，如 "PAPER_JYS_AUDIT"
     * @return 审批结果，包含 currentNode（当前节点）、nextNode（下一节点）、nodeList（节点列表）、process（流程配置）
     */
    ApprovalResult getCurrentNode(String processCode, String currentState);

    /**
     * 提交审批
     * <p>
     * 将业务数据从草稿/驳回状态提交到第一个审批节点。
     * 新状态取当前节点的 passState，若存在下一节点则取下一节点的 rejectState
     * （表示"等待该节点审批"的中间态）。
     * </p>
     *
     * @param request 审批请求，必须包含 processCode、businessId、currentState、操作人信息
     * @return 审批结果，newState 为提交后的新状态编码
     */
    ApprovalResult submitApproval(ApprovalRequest request);

    /**
     * 审批通过
     * <p>
     * 当前节点审批通过，状态流转到 passState 指定的下一状态。
     * 若下一节点为 null（isLast=true），表示流程已走完，业务进入终态。
     * </p>
     *
     * @param request 审批请求，必须包含 processCode、businessId、currentState、操作人信息
     * @return 审批结果，newState 为通过后的新状态编码，isLast 标识是否为最后一个节点
     */
    ApprovalResult approve(ApprovalRequest request);

    /**
     * 审批驳回
     * <p>
     * 当前节点审批驳回，状态回退到 rejectState 指定的状态。
     * 会检查节点的 canBack 配置，若不允许驳回则返回失败。
     * </p>
     *
     * @param request 审批请求，必须包含 processCode、businessId、currentState、操作人信息
     * @return 审批结果，newState 为驳回后的回退状态编码
     */
    ApprovalResult reject(ApprovalRequest request);

    /**
     * 判断用户是否有审批权限
     * <p>
     * 根据用户ID、所属部门ID列表、角色key列表判断是否具备审批资格。
     * 当前实现为基础校验（参数非空），后续需与 sys_approval_node 的 roleIds/deptIds 联动。
     * </p>
     *
     * @param userId   操作人ID
     * @param deptIds  操作人所属部门ID列表
     * @param roleKeys 操作人角色key列表
     * @return true=有审批权限，false=无审批权限
     */
    boolean canApprove(Long userId, List<Long> deptIds, List<String> roleKeys);

    /**
     * 查询审批历史记录
     *
     * @param processCode 流程编码
     * @param businessId  业务数据ID
     * @return 审批历史列表，按时间排序
     */
    List<SysApprovalHistory> getApprovalHistory(String processCode, Long businessId);

    /**
     * 查询流程的所有审批节点
     *
     * @param processCode 流程编码
     * @return 审批节点列表，按 nodeOrder 排序
     */
    List<SysApprovalNode> getProcessNodes(String processCode);

    /**
     * 撤回审批
     * <p>
     * 审批人撤回已通过的审批，状态回退到上一节点：
     * - 若当前为第一个节点，回退到该节点的 rejectState（通常为草稿）
     * - 若当前为中间节点，回退到当前节点的 rejectState；
     * 若 rejectState 为空，则回退到前一节点的 passState
     * </p>
     *
     * @param request 审批请求，必须包含 processCode、businessId、currentState、操作人信息
     * @return 审批结果，newState 为撤回后的回退状态编码
     */
    ApprovalResult recall(ApprovalRequest request);

    /**
     * 更新业务表状态
     * <p>
     * 根据 sys_approval_process 中配置的 businessTable 和 statusField，
     * 自动将指定业务表的状态字段更新为新值。
     * 此方法供 submitApproval/approve/reject/recall 内部调用，
     * 也可供业务 Service 在需要手动更新状态时调用。
     * </p>
     *
     * @param processCode 流程编码，用于查找 businessTable 和 statusField 配置
     * @param businessId  业务数据ID
     * @param newState    新状态值
     * @return 影响行数，0表示未更新
     */
    int updateBusinessState(String processCode, Long businessId, String newState);
}
