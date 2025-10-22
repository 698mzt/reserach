package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciLectureReportIntegralMapper;
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.service.ISciLectureReportIntegralService;
import com.ruoyi.common.core.text.Convert;

/**
 * 讲座报告积分管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-16
 */
@Service
public class SciLectureReportIntegralServiceImpl implements ISciLectureReportIntegralService 
{
    @Autowired
    private SciLectureReportIntegralMapper sciLectureReportIntegralMapper;

    /**
     * 查询讲座报告积分管理
     * 
     * @param id 讲座报告积分管理主键
     * @return 讲座报告积分管理
     */
    @Override
    public SciLectureReportIntegral selectSciLectureReportIntegralById(Integer id)
    {
        return sciLectureReportIntegralMapper.selectSciLectureReportIntegralById(id);
    }

    /**
     * 查询讲座报告积分管理列表
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 讲座报告积分管理
     */
    @Override
    public List<SciLectureReportIntegral> selectSciLectureReportIntegralList(SciLectureReportIntegral sciLectureReportIntegral)
    {
        return sciLectureReportIntegralMapper.selectSciLectureReportIntegralList(sciLectureReportIntegral);
    }

    /**
     * 新增讲座报告积分管理
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 结果
     */
    @Override
    public int insertSciLectureReportIntegral(SciLectureReportIntegral sciLectureReportIntegral)
    {
        return sciLectureReportIntegralMapper.insertSciLectureReportIntegral(sciLectureReportIntegral);
    }

    /**
     * 修改讲座报告积分管理
     * 
     * @param sciLectureReportIntegral 讲座报告积分管理
     * @return 结果
     */
    @Override
    public int updateSciLectureReportIntegral(SciLectureReportIntegral sciLectureReportIntegral)
    {
        return sciLectureReportIntegralMapper.updateSciLectureReportIntegral(sciLectureReportIntegral);
    }

    /**
     * 批量删除讲座报告积分管理
     * 
     * @param ids 需要删除的讲座报告积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciLectureReportIntegralByIds(String ids)
    {
        return sciLectureReportIntegralMapper.deleteSciLectureReportIntegralByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除讲座报告积分管理信息
     * 
     * @param id 讲座报告积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciLectureReportIntegralById(Integer id)
    {
        return sciLectureReportIntegralMapper.deleteSciLectureReportIntegralById(id);
    }

    @Override
    public List<SciLectureReportIntegral> getStatsQuery(Map<String, String> params) {
        return sciLectureReportIntegralMapper.getStatsQuery(params);
    }
}
