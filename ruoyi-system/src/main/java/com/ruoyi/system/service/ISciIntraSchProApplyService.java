package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalReamount;
import com.ruoyi.system.domain.SciIntraSchoolPro;

import java.util.List;
import java.util.Map;

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

    int overApply(String id, String state,Long userId);

    int update_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro);

    int sch_hxover(String id, Long userId, String urlFlag);

    int sch_hxoverBh(String id, Long userId, String remark, String urlFlag);

    int sch_hxCH(String id,Long userId, String remark, String urlFlag);
    int sch_hxOverCH(String id,Long userId, String remark, String urlFlag);
    List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    public int deleteSciSCHHorizontalApplyByIds(String ids);


    String getuser_dnameById(Long userId);

    int subDraft(String id,Long userid, String state);

    List<SciIntraSchoolPro> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询成果转化数据
     *
     * @param params 查询参数
     * @return 成果转化集合
     * @author
     * @date 2020/11/09
     */
    List<SciIntraSchoolPro> getStatsQueryToCheck(Map<String, String> params);

}
