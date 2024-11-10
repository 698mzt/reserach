package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApply;

import java.util.List;

public interface ISciIntraSchProApplyService {
    List<SciHorizontalApply> sel_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> sel_my_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply);
}
