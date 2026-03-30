package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolScore;
import com.ruoyi.system.domain.SciProjectScoreCfg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SciIntraSchProScoreMapper {

    // 获取积分
    List<SciProjectScoreCfg> getUserScoreList(Integer amount);

    Integer getScoreById(@Param("id") Object id, @Param("uid")Long uid);

    int update_SchPro_score(@Param("sciIntraSch_Id")Integer sciIntraSch_Id, @Param("userid")int userid);

    int set_SchPro_score_noScore(SciIntraSchoolScore sciIntraSchoolScore);

    int set_SchPro_score(@Param("score")int score, @Param("id")Integer id, @Param("useridd")int useridd);

    int set_SchPro_JT_score(@Param("score")int score, @Param("id")Integer id, @Param("useridd")int useridd);

    /**
     * 根据用户id获取这个用户的总积分
     * @param uid
     * @return
     */
    Long getScoreByUId( @Param("uid")Long uid);

    int update_SchPro_score_jt(@Param("sciIntraSch_Id")Integer sciIntraSch_Id, @Param("userid") int userid);
}
