package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciProjectScoreCfg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * 横向课题得分配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-30
 */
public interface SciProjectScoreCfgMapper {
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
     * 删除横向课题得分配置
     *
     * @param id 横向课题得分配置主键
     * @return 结果
     */
    public int deleteSciProjectScoreCfgById(Long id);

    /**
     * 批量删除横向课题得分配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciProjectScoreCfgByIds(String[] ids);

    @Select(" SELECT DISTINCT " +
            " funds_max, " +
            " funds_min, " +
            " project_type  " +
            "FROM " +
            " sci_project_score_cfg t  " +
            "ORDER BY " +
            " FUNDS_MIN + 0 DESC ")
    List<Map<String, Object>> getCfgFunds();

    @Select(" SELECT " +
            " t.user_order, " +
            " t.total_score, " +
            " t.start_score, " +
            " t.end_score  " +
            "FROM " +
            " sci_project_score_cfg t  " +
            "WHERE " +
            " t.funds_max = #{funds_max}  " +
            " AND t.funds_min = #{funds_min}  " +
            " AND t.project_type = #{project_type}  " +
            "ORDER BY " +
            " t.total_score + 0 DESC ")
    List<Map<String, Object>> getUserScoreList(Map<String, Object> fundsMap);

    @Delete("  delete from sci_project_score_cfg   " +
            "where funds_max = #{funds_max} and funds_min = #{funds_min}")
    int deleteSciProjectScoreCfgByFunds(Map map);
}
