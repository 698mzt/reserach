package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciIntraSchoolPro;

public interface SciIntraSchProScoreService {
    int set_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro,int key);

    Integer getScoreById(Object id,Long uid);

    Integer update_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro1);

    int set_SchPro_score_noScore(SciIntraSchoolPro sciIntraSchoolPro);
}
