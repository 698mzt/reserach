package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalReamount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SciHorizontalReamountMapper {

    public int insertAmount(SciHorizontalReamount sciHorizontalReamount);

    public int insertVerticalAmount(SciHorizontalReamount sciHorizontalReamount);

    public List<SciHorizontalApply> selectAmountList(SciHorizontalApply sciHorizontalApply);

    public List<SciHorizontalApply> selectAmountListJYS(SciHorizontalApply sciHorizontalApply);

    public  List<SciHorizontalApply> selectAmountListDept(SciHorizontalApply sciHorizontalApply);

    public  List<SciHorizontalApply> selectAmountListKYC(SciHorizontalApply sciHorizontalApply);

    public  SciHorizontalApply selectAmountById(Integer id);

    public  int amountpass(@Param("id")String id,@Param("state") String state);


    int amountedit(SciHorizontalReamount sciHorizontalReamount);

    List<SciHorizontalReamount> selectAmountListById(Integer id);

    List<SciHorizontalApplyVertical> selectVerticalAmountListKYC(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountListJYS(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountListDept(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    List<SciHorizontalApplyVertical> selectVerticalAmountList(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    SciHorizontalApplyVertical selectVerticalAmountById(Integer id);

    int verticalamountpass(@Param("id")String id,@Param("state") String state);
}
