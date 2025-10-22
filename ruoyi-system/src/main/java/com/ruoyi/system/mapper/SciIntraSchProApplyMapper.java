package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    int update_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro);


    List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_admin(SciIntraSchoolPro sciIntraSchoolPro);

    List<Long> getRoleid_list(Long userId);

    List<SciIntraSchoolPro> sel_IntraSchPro_approval_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_closure_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro);

    int deleteSciSCHHorizontalApplyByIds(String[] toStrArray);

    String geStaticById(Integer id);

    String getuser_dnameById(Long userId);

    List<Map<String, Object>> getfilekey(@Param("userId")Long userId);

    List<Map<String, Object>> getAllOverSchProToAlltotle();
}
