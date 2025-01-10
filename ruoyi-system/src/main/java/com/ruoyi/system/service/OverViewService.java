package com.ruoyi.system.service;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OverViewService {
    List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApplyVertical> selectOtherListByUid2(SciHorizontalApplyVertical sciHorizontalApplyVertical);
}
