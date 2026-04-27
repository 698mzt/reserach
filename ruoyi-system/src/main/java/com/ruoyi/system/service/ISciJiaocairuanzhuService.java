package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.domain.SciJiaocairuanzhuMember;

/**
 * 教材软著Service接口
 *
 * @author ruoyi
 * @date 2024-11-21
 */
public interface ISciJiaocairuanzhuService
{
    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    public SciJiaocairuanzhu selectSciJiaocairuanzhuById(Integer id);

    /**
     * 查询教材软著列表
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著集合
     */
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 新增教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    public int insertSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 修改教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    public int updateSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的教材软著主键集合
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuByIds(String ids);

    /**
     * 删除教材软著信息
     *
     * @param id 教材软著主键
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuById(Integer id);



    int hxPass(String id,Long uid,String urlFlag);

    int updateJifen(Long id, int jifen);



//    int hxPass(String id,Long uid,String urlFlag,List score,List persion,Integer applyId);
//    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList4(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList3(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList2(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList1(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList31(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList21(SciJiaocairuanzhu sciJiaocairuanzhu);

    // 新方法：教师查询
    List<SciJiaocairuanzhu> selectSciPaperAListCx(SciJiaocairuanzhu sciJiaocairuanzhu);

    // 新方法：教研室查询
    List<SciJiaocairuanzhu> selectSciPaperAListCxList(SciJiaocairuanzhu sciJiaocairuanzhu);

    // 新方法：学院查询
    List<SciJiaocairuanzhu> selectSciPaperAListXY(SciJiaocairuanzhu sciJiaocairuanzhu);

    // 新方法：科研处查询
    List<SciJiaocairuanzhu> selectSciPaperAListKY(SciJiaocairuanzhu sciJiaocairuanzhu);

    // 新方法：管理员查询
    List<SciJiaocairuanzhu> selectSciPaperAList(SciJiaocairuanzhu sciJiaocairuanzhu);

    int recall(Integer id, String state, Long userId, String remark, String urlFlag);
//    int hxoverBh(String id, Long userId, String remark, String urlFlag);

//    int collegeAudit(String id, Long userId, String urlFlag);
    /**
     * 检查是否已存在相同专利名称和负责人级别
     * @param mingcheng 专利名称
     * @param paiming 负责人级别
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean checkExist(String mingcheng, String paiming, Long userId);
    
    /**
     * 保存教材著作成员信息
     * @param jiaocaiId 教材著作ID
     * @param membersJson 成员信息JSON字符串
     * @return 结果
     */
    int saveJiaocairuanzhuMembers(Integer jiaocaiId, String membersJson);

    /**
     * 统计查询教材专著数据
     *
     * @param params 查询参数
     * @return 教材专著集合
     */
    List<SciJiaocairuanzhu> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询教材专著数据
     *
     * @param params 查询参数
     * @return 教材专著集合
     */
    List<SciJiaocairuanzhu> getStatsQueryToCheck(Map<String, String> params);

    /**
     * 获取教材著作成员列表
     *
     * @param jiaocaiId 教材著作ID
     * @return 教材著作成员列表
     */
    List<SciJiaocairuanzhuMember> getJiaocairuanzhuMembers(Integer jiaocaiId);

    /**
     * 统一查询教材软著列表（所有角色均可查看所有状态）
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著集合
     */
    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuListAll(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 查询下一级状态方法
     * 获取当前审批节点信息及下一步节点信息
     * @param currentState 当前业务数据的状态
     * @return 包含当前节点、下一节点等信息的ApprovalResult
     */
    ApprovalResult getNextState(String currentState);

    /**
     * 操作记录方法
     * 记录审批操作的历史信息
     * @param businessId 业务ID
     * @param oldState 原状态
     * @param newState 新状态
     * @param operatorId 操作人ID
     * @param action 操作类型
     * @param comment 审批意见
     * @return 操作结果
     */
    Map<String, Object> recordApprovalAction(Integer businessId, String oldState, String newState, Long operatorId, String action, String comment);


}
