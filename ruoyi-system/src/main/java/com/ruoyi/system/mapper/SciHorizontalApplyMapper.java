package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciHorizontalApply;
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
     * 批量删除横向课题
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciHorizontalApplyByIds(String[] ids);

    int hxPass(@Param("id") String id,@Param("state") String state);
}
