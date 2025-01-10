package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.mapper.OverViewMapper;
import com.ruoyi.system.service.OverViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课题概览Service业务层处理
 *
 * @author 14740
 * @date 2025-01-06
 */
@Service
public class OverViewServiceImpl implements OverViewService {

    @Autowired
    private OverViewMapper OverViewMapper;


    @Override
    public List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply) {
        return OverViewMapper.selectOtherListByUid(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApplyVertical> selectOtherListByUid2(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return OverViewMapper.selectOtherListByUid2(sciHorizontalApplyVertical);
    }
}
