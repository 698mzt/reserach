package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalPersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 删除纵向课题信息
     *
     * @param ids 纵向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalApplyVerticalByIds(String[] ids);

    int applyPass(@Param("id") String id, @Param("state") String state);

    int overPass(@Param("id") String id, @Param("state") String state);


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
}
