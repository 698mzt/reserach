package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciIntraSchoolPro;

import java.util.List;

public interface ISciIntraSchProApplyService {
    List<SciIntraSchoolPro> sel_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_my_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_ky(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_jy(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_my(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_ky(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_jy(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_my(SciIntraSchoolPro sciIntraSchoolPro);

    int insert_SchPro_Apply(SciIntraSchoolPro sciIntraSchoolPro);

    SciIntraSchoolPro sel_IntraSchPro_by_id(Integer id);

    int sch_hxPass(String id, Long userId, String urlFlag);

    int sch_hxBh(String id, Long userId, String remark, String urlFlag);

    int updateIntraSchoolApply(SciIntraSchoolPro sciIntraSchoolPro);

    int overApply(String id, String state);

    int insert_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro);

    int sch_hxover(String id, Long userId, String urlFlag);

    int sch_hxoverBh(String id, Long userId, String remark, String urlFlag);
}
