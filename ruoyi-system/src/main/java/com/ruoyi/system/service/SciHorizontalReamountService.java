package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalReamount;

import java.util.List;

public interface SciHorizontalReamountService {
    public int insertAmount(SciHorizontalReamount sciHorizontalReamount);

    public int insertVerticalAmount(SciHorizontalReamount sciHorizontalReamount);

    List<SciHorizontalApply> selectAmountList(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectAmountListJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectAmountListDept(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectAmountListKYC(SciHorizontalApply sciHorizontalApply);

    SciHorizontalApply selectAmountById(Integer id);

    int amountpass(String id, String state);


    int amountedit(SciHorizontalReamount sciHorizontalReamount);

    List<SciHorizontalReamount> selectAmountListById(Integer id);

    List<SciHorizontalApplyVertical> selectVerticalAmountListKYC(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountListJYS(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountListDept(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountList(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    SciHorizontalApplyVertical selectVerticalAmountById(Integer id);


    int verticalamountpass(String reid, String state);
}
