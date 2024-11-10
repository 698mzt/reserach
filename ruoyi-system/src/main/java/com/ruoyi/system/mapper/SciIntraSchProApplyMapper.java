package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;

import java.util.List;

public interface SciIntraSchProApplyMapper {

    List<SciHorizontalApply> sel_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_my_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);
}
