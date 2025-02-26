package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciIntraSchProReamount;
import com.ruoyi.system.mapper.SciHorizontalReamountMapper;
import com.ruoyi.system.mapper.SciIntraSchProReamountMapper;
import com.ruoyi.system.service.SciIntraSchProReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SciIntraSchProReamountServiceImpl implements SciIntraSchProReamountService {
    @Autowired
    private SciIntraSchProReamountMapper sciIntraSchProReamountMapper;
    @Override
    public int insertAmount(SciIntraSchProReamount sciIntraSchProReamount) {
        return sciIntraSchProReamountMapper.insertAmount(sciIntraSchProReamount);
    }
}
