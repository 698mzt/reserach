package com.ruoyi.system.service;

import com.ruoyi.system.domain.AlltotleScore;
import com.ruoyi.system.domain.SciIntraSchoolPro;

import java.util.List;

public interface SciIntraSchProScoreService {
    int set_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro,int key);
    // 查询某个课题这个老师的得分
    Integer getScoreById(Object id,Long uid);

    Integer update_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro1);

    int set_SchPro_score_noScore(SciIntraSchoolPro sciIntraSchoolPro);

    Integer update_SchPro_score_jt(SciIntraSchoolPro sciIntraSchoolPro1);
}
