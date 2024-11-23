package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApplyVertical;
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

}
