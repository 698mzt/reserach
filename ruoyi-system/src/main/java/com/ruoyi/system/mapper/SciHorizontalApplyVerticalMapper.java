package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalPersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 纵向课题Mapper接口
 *
 */
public interface SciHorizontalApplyVerticalMapper {

    /**
     * 查询纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 横向课题集合
     */
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalList(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 查询纵向课题列表（统一查询，支持所有状态）
     * 功能：统一查询纵向课题列表，支持多条件筛选和排序
     * 按照2026年度数据权限优化需求，所有管理员角色均可查看所有状态的课题
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 横向课题集合
     */
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListAll(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJYS(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListKYC(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListDept(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 保存立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    public  int insertSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * id查询立项申请
     *
     * @param id 纵向课题
     * @return 结果
     */
    public SciHorizontalApplyVertical selectSciHorizontalApplyVerticalById(Integer id);

    /**
     * 保存修改立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    public  int updateSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 审批页专用更新：只保存纵向课题立项审批允许编辑的业务字段。
     */
    int updateApplyApprovalEditableFields(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 审批页专用更新：只保存纵向课题结项审批允许编辑的业务字段。
     */
    int updateOverApprovalEditableFields(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 删除纵向课题信息
     *
     * @param ids 纵向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalApplyVerticalByIds(String[] ids);

    int applyPass(@Param("id") String id, @Param("state") String state, @Param("subjectSource") String subjectSource);

    int overPass(@Param("id") String id, @Param("state") String state,@Param("subjectSource") String subjectSource);


    /**
     * 查询结项申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJYSJX(SciHorizontalApplyVertical sciHorizontalApplyVertical);
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListKYCJX(SciHorizontalApplyVertical sciHorizontalApplyVertical);
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJX(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListDeptJX(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 查询已结项列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */

    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListOVER(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 导出纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalAllList(SciHorizontalApplyVertical sciHorizontalApplyVertical);


    int insertPersionVertical(SciHorizontalPersion sciHorizontalPersion);


    List<SciHorizontalApplyVertical> selectOtherListByUid(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 查询指定纵向申请的全部成员ID（来自 sci_persion_vertical，按排名升序）
     */
    java.util.List<String> selectPersionIdsByVerticalId(@Param("verticalId") Integer verticalId);

    int deletePersionVerticalByVerticalId(@Param("verticalId") Integer verticalId);

    /**
     * 统计查询纵向课题数据
     *
     * @param params 查询参数
     * @return 纵向课题集合
     */
    List<SciHorizontalApplyVertical> getStatsQuery(Map<String, String> params);

    List<SciHorizontalApplyVertical>
    getStatsQueryToCheck(Map<String, String> params);
}
