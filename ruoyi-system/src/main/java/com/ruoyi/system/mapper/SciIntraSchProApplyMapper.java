package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SciIntraSchProApplyMapper {

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

    int sch_hxPass(@Param("id")String id, @Param("state")String state);
    int sch_hxover(@Param("id") String id,@Param("state") String state);

    public int insertIntraSchProPiyue(SciIntraSchProPiyue sciIntraSchProPiyue);

    int updateIntraSchoolApply(SciIntraSchoolPro sciIntraSchoolPro);

    int overApply(@Param("id") String id,@Param("state") String state);

    int insert_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro);


}
