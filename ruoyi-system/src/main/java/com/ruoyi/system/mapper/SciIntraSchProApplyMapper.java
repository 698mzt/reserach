package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;

import java.util.List;

public interface SciIntraSchProApplyMapper {

    List<SciHorizontalApply> sel_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_my_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_approval_ky(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_approval_jy(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_approval_my(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_closure_ky(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_closure_jy(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_IntraSchPro_closure_my(SciHorizontalApply sciHorizontalApply);
}
