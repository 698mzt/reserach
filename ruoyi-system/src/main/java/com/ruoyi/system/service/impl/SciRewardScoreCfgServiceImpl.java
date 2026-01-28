package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SciRewardScoreCfg;
import com.ruoyi.system.mapper.SciRewardScoreCfgMapper;
import com.ruoyi.system.service.ISciRewardScoreCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 奖励积分管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-24
 */
@Service
public class SciRewardScoreCfgServiceImpl implements ISciRewardScoreCfgService
{
    @Autowired
    private SciRewardScoreCfgMapper sciRewardScoreCfgMapper;

    /**
     * 查询奖励积分管理
     *
     * @param id 奖励积分管理主键
     * @return 奖励积分管理
     */
    @Override
    public SciRewardScoreCfg selectSciRewardScoreCfgById(Long id)
    {
        return sciRewardScoreCfgMapper.selectSciRewardScoreCfgById(id);
    }

    /**
     * 查询奖励积分管理列表
     *
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 奖励积分管理
     */
    @Override
    public List<SciRewardScoreCfg> selectSciRewardScoreCfgList(SciRewardScoreCfg sciRewardScoreCfg)
    {
        return sciRewardScoreCfgMapper.selectSciRewardScoreCfgList(sciRewardScoreCfg);
    }

    /**
     * 新增奖励积分管理
     *
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 结果
     */
    @Override
    public int insertSciRewardScoreCfg(SciRewardScoreCfg sciRewardScoreCfg)
    {
        return sciRewardScoreCfgMapper.insertSciRewardScoreCfg(sciRewardScoreCfg);
    }

    /**
     * 修改奖励积分管理
     *
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 结果
     */
    @Override
    public int updateSciRewardScoreCfg(SciRewardScoreCfg sciRewardScoreCfg)
    {
        return sciRewardScoreCfgMapper.updateSciRewardScoreCfg(sciRewardScoreCfg);
    }

    /**
     * 批量删除奖励积分管理
     *
     * @param ids 需要删除的奖励积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciRewardScoreCfgByIds(String ids)
    {
        return sciRewardScoreCfgMapper.deleteSciRewardScoreCfgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除奖励积分管理信息
     *
     * @param id 奖励积分管理主键
     * @return 结果
     */
    @Override
    public int deleteSciRewardScoreCfgById(Long id)
    {
        return sciRewardScoreCfgMapper.deleteSciRewardScoreCfgById(id);
    }

    @Override
    public String calculateScore(String fenLei, String dengJi, String paiMing) {
        SciRewardScoreCfg config = sciRewardScoreCfgMapper.selectScoreConfig(fenLei, dengJi, paiMing);
        return config != null ? config.getTotalScore() : "0";
    }
}
