package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.mapper.SciHorizontalApplyMapper;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SciIntraSchProApplyServiceImpl implements ISciIntraSchProApplyService {

    @Autowired
    private SciIntraSchProApplyMapper sciIntraSchProApplyMapper;
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;
    @Override
    public List<SciHorizontalApply> sel_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> sel_my_IntraSchPro_isOVER(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_my_IntraSchPro_isOVER(sciHorizontalApply);
    }


}
