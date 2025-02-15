package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * 专利软著得分配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-30
 */
public interface SciZhuanliruanzhuScoreCfgMapper {
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
     * 删除专利软著得分配置
     *
     * @param id 专利软著得分配置主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgById(Long id);

    /**
     * 批量删除专利软著得分配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuScoreCfgByIds(String[] ids);

    @Select(" SELECT DISTINCT " +
            " funds_max, " +
            " funds_min " +

            "FROM " +
            " sci_zhuanliruanzhu_score_cfg t  " +
            "ORDER BY " +
            " FUNDS_MIN + 0 DESC ")
    List<Map<String, Object>> getCfgFunds();

    @Select(" SELECT " +
            " t.user_order, " +
            " t.total_score " +

            "FROM " +
            " sci_zhuanliruanzhu_score_cfg t  " +
            "WHERE " +
            " t.funds_max = #{funds_max}  " +
            " AND t.funds_min = #{funds_min}  " +

            "ORDER BY " +
            " t.total_score + 0 DESC ")
    List<Map<String, Object>> getUserScoreList(Map<String, Object> fundsMap);

    @Delete("  delete from sci_zhuanliruanzhu_score_cfg   " +
            "where funds_max = #{funds_max} and funds_min = #{funds_min}")
    int deleteSciZhuanliruanzhuScoreCfgByFunds(Map map);
}
