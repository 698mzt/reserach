package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciHorizontalReamount;
import com.ruoyi.system.mapper.SciHorizontalReamountMapper;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SciHorizontalReamountServiceImpl implements SciHorizontalReamountService {

    @Autowired
    private SciHorizontalReamountMapper sciHorizontalReamountMapper;
    @Override
    public int insertAmount(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.insertAmount(sciHorizontalReamount);
    }
}
