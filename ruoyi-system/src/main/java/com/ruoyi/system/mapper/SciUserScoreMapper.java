/*
package com.ruoyi.system.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface SciUserScoreMapper {
    @Select("select * from sci_horizontal_apply where id = #{}")
    Map<String, Object> getTopById(String dataId);
    @Select(" select * FROM sci_project_score_cfg t where funds_max>#{amout} and funds_min <= #{amout}")
    List<Map<String, Object>> queryCfgList(Map<String, Object> prmMap);
    @Insert(" INSERT INTO sci_user_score_history ( " +
            "user_id, create_time, score_type, " +
            "change_status, change_type, data_id, " +
            "change_value) " +
            "VALUES ( " +
            "#{user_id}, now(), #{score_type}," +
            " #{topStatus}, #{change_type}, #{data_id}," +
            " #{change_value}) ")
    void insertScoreHis(Map<String, Object> prmMap);
}

*/