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
    int countIntraschproApplyAll();
    int countIntraschproApplyAllByStates(@Param("states") List<String> states);

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

    /**
     * 核算查询横向课题数据
     *
     * @param params 查询参数
     * @return 横向课题集合
     */
    List<SciHorizontalApply> getStatsQueryToCheck(Map<String, String> params);

    /**
     * 更新申请状态
     *
     * @param applyId 申请ID
     * @param state 新状态编码
     * @return 影响行数
     */
    int updateState(@Param("applyId") Integer applyId, @Param("state") String state);
}
