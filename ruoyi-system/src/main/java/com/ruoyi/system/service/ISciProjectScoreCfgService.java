package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciProjectScoreCfg;

/**
 * 横向课题得分配置Service接口
 * 
 * @author ruoyi
 * @date 2024-09-30
 */
public interface ISciProjectScoreCfgService 
{
    /**
     * 查询横向课题得分配置
     * 
     * @param id 横向课题得分配置主键
     * @return 横向课题得分配置
     */
    public SciProjectScoreCfg selectSciProjectScoreCfgById(Long id);

    /**
     * 查询横向课题得分配置列表
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 横向课题得分配置集合
     */
    public List<SciProjectScoreCfg> selectSciProjectScoreCfgList(SciProjectScoreCfg sciProjectScoreCfg);

    /**
     * 新增横向课题得分配置
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 结果
     */
    public int insertSciProjectScoreCfg(SciProjectScoreCfg sciProjectScoreCfg);

    /**
     * 修改横向课题得分配置
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 结果
     */
    public int updateSciProjectScoreCfg(SciProjectScoreCfg sciProjectScoreCfg);

    /**
     * 批量删除横向课题得分配置
     * 
     * @param ids 需要删除的横向课题得分配置主键集合
     * @return 结果
     */
    public int deleteSciProjectScoreCfgByIds(String ids);

    /**
     * 删除横向课题得分配置信息
     * 
     * @param id 横向课题得分配置主键
     * @return 结果
     */
    public int deleteSciProjectScoreCfgById(Long id);

    Map<String, Object> getProjectScoreCfg();
}
