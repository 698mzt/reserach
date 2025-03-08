package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciJiaocairuanzhuScoreCfg;

import java.util.List;

/**
 * 专利软著积分管理Service接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface ISciJiaocairuanzhuScoreCfgService
{
    /**
     * 查询专利软著积分管理
     * 
     * @param id 专利软著积分管理主键
     * @return 专利软著积分管理
     */
    public SciJiaocairuanzhuScoreCfg selectSciJiaocairuanzhuScoreCfgById(Long id);

    /**
     * 查询专利软著积分管理列表
     * 
     * @param sciJiaocairuanzhuScoreCfg 专利软著积分管理
     * @return 专利软著积分管理集合
     */
    public List<SciJiaocairuanzhuScoreCfg> selectSciJiaocairuanzhuScoreCfgList(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg);

    /**
     * 新增专利软著积分管理
     * 
     * @param sciJiaocairuanzhuScoreCfg 专利软著积分管理
     * @return 结果
     */
    public int insertSciJiaocairuanzhuScoreCfg(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg);

    /**
     * 修改专利软著积分管理
     * 
     * @param sciJiaocairuanzhuScoreCfg 专利软著积分管理
     * @return 结果
     */
    public int updateSciJiaocairuanzhuScoreCfg(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg);

    /**
     * 批量删除专利软著积分管理
     * 
     * @param ids 需要删除的专利软著积分管理主键集合
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuScoreCfgByIds(String ids);

    /**
     * 删除专利软著积分管理信息
     * 
     * @param id 专利软著积分管理主键
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuScoreCfgById(Long id);
}
