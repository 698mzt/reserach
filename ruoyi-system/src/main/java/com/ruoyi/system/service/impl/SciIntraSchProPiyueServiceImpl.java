package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.service.ISciIntraSchProPiyueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SciIntraSchProPiyueServiceImpl implements ISciIntraSchProPiyueService {
    @Autowired
    private SciIntraSchProPiyueMapper sciIntraSchProPiyueMapper;

    @Override
    public List<SciIntraSchProPiyue> selectIntraSchProPiyueList(SciIntraSchProPiyue sciIntraSchProPiyue) {
        return sciIntraSchProPiyueMapper.selectIntraSchProPiyueList(sciIntraSchProPiyue);
    }

    @Override
    public int insertIntraSchProPiyue(SciIntraSchProPiyue sciIntraSchProPiyue)
    {
        sciIntraSchProPiyue.setCreateTime(DateUtils.getNowDate());
        return sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
    }

}
