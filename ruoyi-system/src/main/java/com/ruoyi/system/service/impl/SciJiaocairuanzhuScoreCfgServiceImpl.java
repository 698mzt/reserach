package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SciJiaocairuanzhuScoreCfg;
import com.ruoyi.system.mapper.SciJiaocairuanzhuScoreCfgMapper;
import com.ruoyi.system.service.ISciJiaocairuanzhuScoreCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 教材软著积分管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@Service
public class SciJiaocairuanzhuScoreCfgServiceImpl implements ISciJiaocairuanzhuScoreCfgService
{
    @Autowired
    private SciJiaocairuanzhuScoreCfgMapper sciJiaocairuanzhuScoreCfgMapper;

    /**
     * 查询教材软著积分管理
     * 
     * @param id 教材软著积分管理主键
     * @return 教材软著积分管理
     */
    @Override
    public SciJiaocairuanzhuScoreCfg selectSciJiaocairuanzhuScoreCfgById(Long id)
    {
        return sciJiaocairuanzhuScoreCfgMapper.selectSciJiaocairuanzhuScoreCfgById(id);
    }

    /**
     * 查询教材软著积分管理列表
     * 
     * @param sciJiaocairuanzhuScoreCfg 教材软著积分管理
     * @return 教材软著积分管理
     */
    @Override
    public List<SciJiaocairuanzhuScoreCfg> selectSciJiaocairuanzhuScoreCfgList(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        return sciJiaocairuanzhuScoreCfgMapper.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);
    }

    /**
     * 新增教材软著积分管理
     * 
     * @param sciJiaocairuanzhuScoreCfg 教材软著积分管理
     * @return 结果
     */
    @Override
    public int insertSciJiaocairuanzhuScoreCfg(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        return sciJiaocairuanzhuScoreCfgMapper.insertSciJiaocairuanzhuScoreCfg(sciJiaocairuanzhuScoreCfg);
    }

    /**
     * 修改教材软著积分管理
     * 
     * @param sciJiaocairuanzhuScoreCfg 教材软著积分管理
     * @return 结果
     */
    @Override
    public int updateSciJiaocairuanzhuScoreCfg(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        sciJiaocairuanzhuScoreCfg.setUpdateTime(DateUtils.getNowDate());
        return sciJiaocairuanzhuScoreCfgMapper.updateSciJiaocairuanzhuScoreCfg(sciJiaocairuanzhuScoreCfg);
    }

    /**
     * 批量删除教材软著积分管理
     * 
     * @param ids 需要删除的教材软著积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuScoreCfgByIds(String ids)
    {
        return sciJiaocairuanzhuScoreCfgMapper.deleteSciJiaocairuanzhuScoreCfgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除教材软著积分管理信息
     * 
     * @param id 教材软著积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuScoreCfgById(Long id)
    {
        return sciJiaocairuanzhuScoreCfgMapper.deleteSciJiaocairuanzhuScoreCfgById(id);
    }
}
