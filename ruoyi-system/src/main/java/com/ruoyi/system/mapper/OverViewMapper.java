package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;

import java.util.List;

public interface OverViewMapper {

    public List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApplyVertical> selectOtherListByUid2(SciHorizontalApplyVertical sciHorizontalApplyVertical);
}
