package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;

/**
 * 专利软著得分配置Service接口
 *
 * @author ruoyi
 * @date 2024-09-30
 */
public interface ISciZhuanliruanzhuScoreCfgService
{
    /**
     * 查询专利软著得分配置
     *
     * @param id 专利软著得分配置主键
     * @return 专利软著得分配置
     */
    public SciZhuanliruanzhuScoreCfg selectSciZhuanliruanzhuScoreCfgById(Long id);

    /**
     * 查询专利软著得分配置列表
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 专利软著得分配置集合
     */
    public List<SciZhuanliruanzhuScoreCfg> selectSciZhuanliruanzhuScoreCfgList(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 新增专利软著得分配置
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 结果
     */
    public int insertSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 修改专利软著得分配置
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 结果
     */
    public int updateSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 批量删除专利软著得分配置
     *
     * @param ids 需要删除的专利软著得分配置主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgByIds(String ids);

    /**
     * 删除专利软著得分配置信息
     *
     * @param id 专利软著得分配置主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgById(Long id);

    Map<String, Object> getZhuanliruanzhuScoreCfg();

    int deleteSciZhuanliruanzhuScoreCfgByFunds(Map map);
}
