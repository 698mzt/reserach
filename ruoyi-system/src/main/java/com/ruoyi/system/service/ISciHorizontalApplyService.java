package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.creditedAmount;
import org.apache.ibatis.annotations.Options;

/**
 * 横向课题Service接口
 *
 * @author zhansan
 * @date 2024-08-16
 */
public interface ISciHorizontalApplyService
{
    /**
     * 查询横向课题
     *
     * @param id 横向课题主键
     * @return 横向课题
     */
    public SciHorizontalApply selectSciHorizontalApplyById(Integer id);

    /**
     * 查询横向课题列表
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题集合
     */
    public List<SciHorizontalApply> selectSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply);

    /**
     * 统一查询横向课题列表（基于数据权限控制）
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题集合
     */
    public List<SciHorizontalApply> selectSciHorizontalApplyListAll(SciHorizontalApply sciHorizontalApply);

    /**
     * 新增横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply);




    /**
     * 修改横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    public int updateSciHorizontalApply(SciHorizontalApply sciHorizontalApply);

    /**
     * 批量删除横向课题
     *
     * @param ids 需要删除的横向课题主键集合
     * @return 结果
     */
    public int deleteSciHorizontalApplyByIds(String ids);

    /**
     * 删除横向课题信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalApplyById(Integer id);

    /**
     * 删除横向课题另一个信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalOverApplyById(Integer id);

    int hxPass(String id,Long uid,String urlFlag);
    int amountPass(String id,String reid,Long uid,String urlFlag,List score,List persion,Integer applyId,String amountType);
    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);
    int hxoverBh(String id, Long userId, String remark, String urlFlag);

    List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApply(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyKYC(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOVERKYC(SciHorizontalApply sciHorizontalApply);

    int overApply(String id, String state);


    List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply);
    //申请结项流程
    int overSaveSciHorizontalApply(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply);

    int deletePersionByid(String ids);

    List<SciHorizontalApply> selectSciHorizontalApplyListByDept(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverDept(SciHorizontalApply sciHorizontalApply);

    public int sci_horizontal_piyue(Integer id);

//    int rercallState(Integer id);

    int recall(Integer id, String newState,Long uid, String remark, String urlFlag);


    int amountBh(String id, String reid, Long userId, String remark, String urlFlag);



    /**
     * 查询到账金额
     * */
    List<creditedAmount> selectCreditedAmount();

    Map<String, Integer> getTodoCount(SysUser user);

    /**
     * 按顺序保存成员到 sci_persion（先清空再插入，ranking 从1开始）
     */
    void saveApplyPersons(Integer applyId, java.util.List<String> personIds);
    /**
     * 查询指定申请的全部成员ID（来自 sci_persion，按排名升序）
     */
    java.util.List<String> selectPersionIdsByApplyId(Integer applyId);
    //*******************************************************************************************************
    // 纵向课题统计方法
    int countVerticalApply(SysUser user);
    int countVerticalAudit(SysUser user);
    int countVerticalComplete(SysUser user);
    // 横向课题统计方法
    int countHorizontalApply(SysUser user);
    int countHorizontalAudit(SysUser user);
    int countHorizontalComplete(SysUser user);

    // 成果转化统计方法
    int countAchievementApply(SysUser user);
    int countAchievementAudit(SysUser user);
    int countAchievementComplete(SysUser user);

    // 论文统计方法
    int countPaperApply(SysUser user);
    int countPaperAudit(SysUser user);
    int countPaperComplete(SysUser user);

    // 教材软著统计方法
    int countTextbookApply(SysUser user);
    int countTextbookAudit(SysUser user);
    int countTextbookComplete(SysUser user);

    // 专利软著统计方法
    int countPatentApply(SysUser user);
    int countPatentAudit(SysUser user);
    int countPatentComplete(SysUser user);

    // 奖励统计方法
    int countRewardApply(SysUser user);
    int countRewardAudit(SysUser user);
    int countRewardComplete(SysUser user);

    // 讲座报告统计方法
    int countLectureApply(SysUser user);
    int countLectureAudit(SysUser user);
    int countLectureComplete(SysUser user);

    /**
     * 统计查询横向课题数据
     *
     * @param params 查询参数
     * @return 横向课题集合
     */
    List<SciHorizontalApply> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询横向课题数据
     *
     * @param params 查询参数
     * @return 横向课题集合
     */
    List<SciHorizontalApply> getStatsQueryToCheck(Map<String, String> params);

    /**
     * 删除被驳回的到账金额
     * */
    int removeAmount(String id, String reid, Long userId, String remark, String urlFlag);

    /**
     * 功能描述：重新计算科研分
     * @param ids 课题ID列表，多个ID用逗号分隔
     * @param operatorId 操作人ID
     * @return 成功重新计算的课题数量
     */
    int recalculateScore(String ids, Long operatorId);

    /**
     * 功能描述：获取科研分计算预览
     * @param id 课题ID
     * @return 预览结果Map
     */
    Map<String, Object> previewScore(Integer id);

    //*******************************************************************************************************
    // 审批流程相关方法（集成IApprovalProcessService）
    
    /**
     * 提交立项申请审批
     * <p>
     * 将立项申请从草稿状态提交到第一个审批节点（教研室审批）。
     * 通过调用IApprovalProcessService.submitApproval()实现，确保状态流转的规范性。
     * </p>
     *
     * @param applyId 申请ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 提交说明（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult submitApplyApproval(Integer applyId, Long operatorId, 
            String operatorName, String operatorDept, String comment);

    /**
     * 立项申请审批通过
     * <p>
     * 当前审批节点通过，状态流转到下一节点或终态。
     * 通过调用IApprovalProcessService.approve()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 审批意见（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult approveApply(Integer applyId, String currentState, 
            Long operatorId, String operatorName, String operatorDept,
            java.util.List<String> operatorRoleKeys, java.util.List<Long> operatorDeptIds, String comment);

    /**
     * 立项申请审批驳回
     * <p>
     * 当前审批节点驳回，状态回退到草稿或上一节点。
     * 通过调用IApprovalProcessService.reject()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 驳回原因
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult rejectApply(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            java.util.List<String> operatorRoleKeys, java.util.List<Long> operatorDeptIds, String comment);

    /**
     * 立项申请撤回
     * <p>
     * 提交者撤回已提交的立项申请，状态回退到草稿。
     * 通过调用IApprovalProcessService.recall()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 撤回原因（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult recallApply(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept, String comment);

    /**
     * 提交结项申请审批
     * <p>
     * 将结项申请从草稿状态提交到第一个审批节点（教研室审批）。
     * 通过调用IApprovalProcessService.submitApproval()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 提交说明（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult submitOverApproval(Integer applyId, Long operatorId,
            String operatorName, String operatorDept, String comment);

    /**
     * 结项申请审批通过
     * <p>
     * 当前审批节点通过，状态流转到下一节点或终态。
     * 通过调用IApprovalProcessService.approve()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 审批意见（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult approveOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            java.util.List<String> operatorRoleKeys, java.util.List<Long> operatorDeptIds, String comment);

    /**
     * 结项申请审批驳回
     * <p>
     * 当前审批节点驳回，状态回退到草稿或上一节点。
     * 通过调用IApprovalProcessService.reject()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 驳回原因
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult rejectOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            java.util.List<String> operatorRoleKeys, java.util.List<Long> operatorDeptIds, String comment);

    /**
     * 结项申请撤回
     * <p>
     * 提交者撤回已提交的结项申请，状态回退到草稿。
     * 通过调用IApprovalProcessService.recall()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 撤回原因（可选）
     * @return 审批结果
     */
    com.ruoyi.system.domain.ApprovalResult recallOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept, String comment);

    /**
     * 查询立项申请审批历史
     *
     * @param applyId 申请ID
     * @return 审批历史列表
     */
    java.util.List<com.ruoyi.system.domain.SysApprovalHistory> getApplyApprovalHistory(Integer applyId);

    /**
     * 查询结项申请审批历史
     *
     * @param applyId 申请ID
     * @return 审批历史列表
     */
    java.util.List<com.ruoyi.system.domain.SysApprovalHistory> getOverApprovalHistory(Integer applyId);
}
