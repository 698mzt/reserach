package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciRewardScoreCfg;

import java.util.List;

/**
 * 奖励积分管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-24
 */
public interface SciRewardScoreCfgMapper 
{
    /**
     * 查询奖励积分管理
     * 
     * @param id 奖励积分管理主键
     * @return 奖励积分管理
     */
    public SciRewardScoreCfg selectSciRewardScoreCfgById(Long id);

    /**
     * 查询奖励积分管理列表
     * 
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 奖励积分管理集合
     */
    public List<SciRewardScoreCfg> selectSciRewardScoreCfgList(SciRewardScoreCfg sciRewardScoreCfg);

    /**
     * 新增奖励积分管理
     * 
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 结果
     */
    public int insertSciRewardScoreCfg(SciRewardScoreCfg sciRewardScoreCfg);

    /**
     * 修改奖励积分管理
     * 
     * @param sciRewardScoreCfg 奖励积分管理
     * @return 结果
     */
    public int updateSciRewardScoreCfg(SciRewardScoreCfg sciRewardScoreCfg);

    /**
     * 删除奖励积分管理
     * 
     * @param id 奖励积分管理主键
     * @return 结果
     */
    public int deleteSciRewardScoreCfgById(Long id);

    /**
     * 批量删除奖励积分管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciRewardScoreCfgByIds(String[] ids);
}
