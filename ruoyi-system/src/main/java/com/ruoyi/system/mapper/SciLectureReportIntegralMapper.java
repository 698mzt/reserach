package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.domain.SciLectureReportOpinion;

/**
 * 讲座报告积分管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-16
 */
public interface SciLectureReportIntegralMapper 
{
    /**
     * 查询讲座报告积分管理
     * 
     * @param id 讲座报告积分管理主键
     * @return 讲座报告积分管理
     */
    public SciLectureReportIntegral selectSciLectureReportIntegralById(Integer id);

    /**
     * 查询讲座报告积分管理列表
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 讲座报告积分管理集合
     */
    public List<SciLectureReportIntegral> selectSciLectureReportIntegralList(SciLectureReportIntegral sciLectureReportIntegral);

    /**
     * 新增讲座报告积分管理
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 结果
     */
    public int insertSciLectureReportIntegral(SciLectureReportIntegral sciLectureReportIntegral);

    /**
     * 修改讲座报告积分管理
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 结果
     */
    public int updateSciLectureReportIntegral(SciLectureReportIntegral sciLectureReportIntegral);

    /**
     * 删除讲座报告积分管理
     * 
     * @param id 讲座报告积分管理主键
     * @return 结果
     */
    public int deleteSciLectureReportIntegralById(Integer id);

    /**
     * 批量删除讲座报告积分管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciLectureReportIntegralByIds(String[] ids);

}
