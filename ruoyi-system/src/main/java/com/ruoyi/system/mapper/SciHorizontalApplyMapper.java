package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPersion;
import com.ruoyi.system.domain.creditedAmount;
import org.apache.ibatis.annotations.Param;

/**
 * 横向课题Mapper接口
 *
 * @author zhansan
 * @date 2024-08-16
 */
public interface SciHorizontalApplyMapper
{
    /**
     * 根据申请ID按排名查询成员ID列表
     */
    List<String> selectPersionIdsByApplyId(@Param("applyId") Integer applyId);
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
     * 新增横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply);



    /**
     * 修改横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    public int updateSciHorizontalApply(SciHorizontalApply sciHorizontalApply);

    /**
     * 删除横向课题
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalApplyById(Integer id);

    /**
     * 删除横向课题另一个表信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalOverApplyById(Integer id);

    /**
     * 批量删除横向课题
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciHorizontalApplyByIds(String[] ids);

    int hxPass(@Param("id") String id,@Param("state") String state);

    List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApply(SciHorizontalApply sciHorizontalApply);
    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyJYS(SciHorizontalApply sciHorizontalApply);
    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyKYC(SciHorizontalApply sciHorizontalApply);


    List<SciHorizontalApply> selectSciHorizontalApplyListByOVER(SciHorizontalApply sciHorizontalApply);
    List<SciHorizontalApply> selectSciHorizontalApplyListByOVERKYC(SciHorizontalApply sciHorizontalApply);

    int overApply(@Param("id") String id,@Param("state") String state);

    int insertPersion(SciHorizontalPersion sciHorizontalPersion);


    List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply);

    public int deletePersion(SciHorizontalPersion sciHorizontalPersion);

    List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply);

    public int deletePersionByid(String[] ids);

    List<SciHorizontalApply> selectSciHorizontalApplyListByDept(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverDept(SciHorizontalApply sciHorizontalApply);

    int sci_horizontal_piyue(Integer id);

    /**
     * 查询到账金额
     * */
    List<creditedAmount> selectCreditedAmount();
    String selectCreditedAmountById(Integer  id);

    // 科研处审批统计
    int countByStates(@Param("states") List<String> states,
                      @Param("year") String year);

    // 部门审批统计
    // 新增递归查询方法
    int countByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                         @Param("states") List<String> states,
                                         @Param("year") String year);
    int countByDeptAndStates(@Param("deptId") Long deptId,
                             @Param("states") List<String> states,
                             @Param("year") String year);

    // 个人待办统计
    int countByUserAndStates(@Param("userId") Long userId,
                             @Param("states") List<String> states,
                             @Param("year") String year);

    int countByState(@Param("state") String state,
                     @Param("year") String year);

    //金额统计
    // 金额审批统计方法
    int countReamountByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countReamountByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countReamountByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countReamountByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);



    //8.31纵向课题待办
    // 添加纵向课题统计方法
    int countVerticalByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countVerticalByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countVerticalByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countVerticalByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    //8.31论文待办
    // 添加论文统计方法
    int countPaperByStates(@Param("states") List<String> states,
                           @Param("year") String year);

    int countPaperByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                              @Param("states") List<String> states,
                                              @Param("year") String year);

    int countPaperByDeptAndStates(@Param("deptId") Long deptId,
                                  @Param("states") List<String> states,
                                  @Param("year") String year);

    int countPaperByUserAndStates(@Param("userId") Long userId,
                                  @Param("states") List<String> states,
                                  @Param("year") String year);

    //9.6成果转化待办
    // 添加成果转化统计方法
    int countAchievementByStates(@Param("states") List<String> states,
                                 @Param("year") String year);

    int countAchievementByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                    @Param("states") List<String> states,
                                                    @Param("year") String year);

    int countAchievementByDeptAndStates(@Param("deptId") Long deptId,
                                        @Param("states") List<String> states,
                                        @Param("year") String year);

    int countAchievementByUserAndStates(@Param("userId") Long userId,
                                        @Param("states") List<String> states,
                                        @Param("year") String year);

    //9.14 教材专著待办
    // 教材软著统计方法
    int countTextbookByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countTextbookByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countTextbookByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countTextbookByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    //9.14 专利软著待办
    // 专利软著统计方法
    int countPatentByStates(@Param("states") List<String> states,
                            @Param("year") String year);

    int countPatentByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                               @Param("states") List<String> states,
                                               @Param("year") String year);

    int countPatentByDeptAndStates(@Param("deptId") Long deptId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countPatentByUserAndStates(@Param("userId") Long userId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    //9.14 奖励待办
    // 奖励统计方法
    int countRewardByStates(@Param("states") List<String> states,
                            @Param("year") String year);

    int countRewardByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                               @Param("states") List<String> states,
                                               @Param("year") String year);

    int countRewardByDeptAndStates(@Param("deptId") Long deptId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countRewardByUserAndStates(@Param("userId") Long userId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    //9.14 讲座报告待办
    // 讲座报告统计方法
    int countLectureByStates(@Param("states") List<String> states,
                             @Param("year") String year);

    int countLectureByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                @Param("states") List<String> states,
                                                @Param("year") String year);

    int countLectureByDeptAndStates(@Param("deptId") Long deptId,
                                    @Param("states") List<String> states,
                                    @Param("year") String year);

    int countLectureByUserAndStates(@Param("userId") Long userId,
                                    @Param("states") List<String> states,
                                    @Param("year") String year);



    // 纵向课题统计方法（添加vertical前缀避免冲突）
    int countVerticalApplyByUser(@Param("userId") Long userId);
    int countVerticalApplyByUserAndStates(@Param("userId") Long userId,
                                          @Param("states") List<String> states);
    int countVerticalApplyByUserAndState(@Param("userId") Long userId,
                                         @Param("state") String state);

    //************************************************************************************************
    int countHorizontalApplyByUser(@Param("userId") Long userId);
    int countHorizontalApplyByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countIntraschproApplyByUser(@Param("userId") Long userId);
    int countIntraschproApplyByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countPaperAByUser(@Param("userId") Long userId);
    int countPaperAByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countJiaocairuanzhuByUser(@Param("userId") Long userId);
    int countJiaocairuanzhuByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countZhuanliruanzhuByUser(@Param("userId") Long userId);
    int countZhuanliruanzhuByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countRewardByUser(@Param("userId") Long userId);
    int countRewardByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    int countLectureReportByUser(@Param("userId") Long userId);
    int countLectureReportByUserAndStates1(@Param("userId") Long userId, @Param("states") List<String> states);

    /**
     * 统计查询横向课题数据
     *
     * @param params 查询参数
     * @return 横向课题集合
     */
    List<SciHorizontalApply> getStatsQuery(Map<String, String> params);
}
