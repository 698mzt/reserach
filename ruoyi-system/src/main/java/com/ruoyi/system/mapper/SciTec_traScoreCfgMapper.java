package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.domain.SciTec_traScoreCfg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 成果转化得分配置Mapper接口
 *
 * @date 2024-09-30
 */

public interface SciTec_traScoreCfgMapper {
    /**
     * 查询成果转化得分配置列表
     *
     * @param sciTec_traScoreCfg 成果转化得分配置
     * @return 横向课题得分配置
     */
    List<SciProjectScoreCfg> selectSciTec_traScoreCfgList(SciTec_traScoreCfg sciTec_traScoreCfg);
    /**
     * 新增成果转化得分配置
     * @param sciTec_traScoreCfg
     * @return
     */
    int insertTec_traScoreCfg(SciTec_traScoreCfg sciTec_traScoreCfg);
    @Select(" SELECT DISTINCT " +
            " funds_max, " +
            " funds_min " +
            "FROM " +
            " sci_technology_transfer_cfg t  " +
            "ORDER BY " +
            " FUNDS_MIN + 0 DESC ")
    List<Map<String, Object>> getCfgFunds();

    @Select(" SELECT " +
            " t.user_order, " +
            " t.total_score, " +
            " t.start_score, " +
            " t.end_score  " +
            "FROM " +
            " sci_technology_transfer_cfg t  " +
            "WHERE " +
            " t.funds_max = #{funds_max}  " +
            " AND t.funds_min = #{funds_min}  " +
            "ORDER BY " +
            " t.total_score + 0 DESC ")
    List<Map<String, Object>> getUserScoreList(Map<String, Object> fundsMap);

    @Delete("  delete from sci_technology_transfer_cfg   " +
            "where funds_max = #{funds_max} and funds_min = #{funds_min}")
    int deleteSciTec_traScoreCfgByFunds(Map map);

    @Select("SELECT user_order, total_score, start_score, end_score " +
            "FROM sci_technology_transfer_cfg " +
            "WHERE CAST(funds_min AS DECIMAL) <= #{amount} " +
            "AND CAST(funds_max AS DECIMAL) > #{amount} " +
            "ORDER BY user_order + 0 ASC")
    List<Map<String, Object>> selectScoreByAmount(@Param("amount") double amount);

    @Select("SELECT " +
            " funds_max AS fundsMax, " +
            " funds_min AS fundsMin, " +
            " user_order AS userOrder, " +
            " total_score AS totalScore, " +
            " start_score AS startScore, " +
            " end_score AS endScore " +
            "FROM sci_technology_transfer_cfg " +
            "ORDER BY CAST(funds_min AS DECIMAL(10,2)) ASC, " +
            " CASE WHEN funds_max IS NULL OR funds_max = '' OR CAST(funds_max AS DECIMAL(10,2)) = 0 THEN 999999999 " +
            "      ELSE CAST(funds_max AS DECIMAL(10,2)) END ASC, " +
            " CAST(user_order AS UNSIGNED) ASC")
    List<SciProjectScoreCfg> selectScoreConfigsForCalculation();
}
