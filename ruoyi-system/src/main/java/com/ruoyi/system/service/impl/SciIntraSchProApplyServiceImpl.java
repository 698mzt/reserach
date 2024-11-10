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

    @Override
    public List<SciHorizontalApply> sel_IntraSchPro_approval_ky(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_ky(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> sel_IntraSchPro_approval_jy(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_jy(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> sel_IntraSchPro_approval_my(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_my(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> sel_IntraSchPro_closure_ky(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_ky(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> sel_IntraSchPro_closure_jy(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_jy(sciHorizontalApply);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> sel_IntraSchPro_closure_my(SciHorizontalApply sciHorizontalApply) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_my(sciHorizontalApply);
    }


}
