package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalReamount;
import com.ruoyi.system.mapper.SciHorizontalReamountMapper;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SciHorizontalReamountServiceImpl implements SciHorizontalReamountService {

    @Autowired
    private SciHorizontalReamountMapper sciHorizontalReamountMapper;
    @Override
    public int insertAmount(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.insertAmount(sciHorizontalReamount);
    }

    @Override
    public int insertVerticalAmount(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.insertVerticalAmount(sciHorizontalReamount);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectAmountList(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountList(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectAmountListJYS(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListJYS(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> selectAmountListDept(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListDept(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> selectAmountListKYC(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListKYC(sciHorizontalApply);
    }

    @Override
    public SciHorizontalApply selectAmountById(Integer id) {
        return sciHorizontalReamountMapper.selectAmountById(id);
    }

    @Override
    public int amountpass(String id, String state) {
        return sciHorizontalReamountMapper.amountpass(id,state);
    }

    @Override
    public int amountedit(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.amountedit(sciHorizontalReamount);
    }

    @Override
    public List<SciHorizontalReamount> selectAmountListById(Integer id) {
        return sciHorizontalReamountMapper.selectAmountListById(id);
    }

    @Override
    public List<SciHorizontalApplyVertical> selectVerticalAmountListKYC(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalReamountMapper.selectVerticalAmountListKYC(sciHorizontalApplyVertical);
    }

    @Override
    public List<SciHorizontalApplyVertical> selectVerticalAmountListJYS(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalReamountMapper.selectVerticalAmountListJYS(sciHorizontalApplyVertical);
    }

    @Override
    public List<SciHorizontalApplyVertical> selectVerticalAmountListDept(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalReamountMapper.selectVerticalAmountListDept(sciHorizontalApplyVertical);
    }

    @Override
    public List<SciHorizontalApplyVertical> selectVerticalAmountList(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalReamountMapper.selectVerticalAmountList(sciHorizontalApplyVertical);
    }

    @Override
    public SciHorizontalApplyVertical selectVerticalAmountById(Integer id) {
        return sciHorizontalReamountMapper.selectVerticalAmountById(id);
    }

    @Override
    public int verticalamountpass(String reid, String state) {
        return sciHorizontalReamountMapper.verticalamountpass(reid,state);
    }

}
