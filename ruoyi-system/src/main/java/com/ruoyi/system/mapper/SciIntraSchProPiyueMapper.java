package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;


import java.util.List;

public interface SciIntraSchProPiyueMapper {
    List<SciIntraSchProPiyue> selectIntraSchProPiyueList(SciIntraSchProPiyue sciIntraSchProPiyue);

    int insertIntraSchProPiyue(SciIntraSchProPiyue sciIntraSchProPiyue);
}
