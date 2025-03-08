package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;

/**
 * 专利软著积分管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface SciZhuanliruanzhuScoreCfgMapper
{
    /**
     * 查询专利软著积分管理
     * 
     * @param id 专利软著积分管理主键
     * @return 专利软著积分管理
     */
    public SciZhuanliruanzhuScoreCfg selectSciZhuanliruanzhuScoreCfgById(Long id);

    /**
     * 查询专利软著积分管理列表
     * 
     * @param sciZhuanliruanzhuScoreCfg 专利软著积分管理
     * @return 专利软著积分管理集合
     */
    public List<SciZhuanliruanzhuScoreCfg> selectSciZhuanliruanzhuScoreCfgList(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 新增专利软著积分管理
     * 
     * @param sciZhuanliruanzhuScoreCfg 专利软著积分管理
     * @return 结果
     */
    public int insertSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 修改专利软著积分管理
     * 
     * @param sciZhuanliruanzhuScoreCfg 专利软著积分管理
     * @return 结果
     */
    public int updateSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg);

    /**
     * 删除专利软著积分管理
     * 
     * @param id 专利软著积分管理主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgById(Long id);

    /**
     * 批量删除专利软著积分管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgByIds(String[] ids);
}
