package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolScore;

public interface SciIntraSchProScoreService {

    int set_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro, int key);

    Integer getScoreById(Object id, Long uid);

    /**
     * 开题撤回时积分
     * @param sciIntraSchoolPro1
     * @return
     */
    Integer update_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro1);

    /**
     * 结题撤回时积分
     * @param sciIntraSchoolPro1
     * @return
     */
    Integer update_SchPro_score_jt(SciIntraSchoolPro sciIntraSchoolPro1);

    /**
     * 申请开题的时候就就设置积分为0
     * @param sciIntraSchoolPro
     * @return
     */
    int set_SchPro_score_noScore(SciIntraSchoolPro sciIntraSchoolPro);
}