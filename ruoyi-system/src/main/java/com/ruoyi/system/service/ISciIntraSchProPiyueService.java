package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;

import java.util.List;

public interface ISciIntraSchProPiyueService {
    List<SciIntraSchProPiyue> selectIntraSchProPiyueList(SciIntraSchProPiyue sciIntraSchProPiyue);

    int insertIntraSchProPiyue(SciIntraSchProPiyue sciIntraSchProPiyue);
}
