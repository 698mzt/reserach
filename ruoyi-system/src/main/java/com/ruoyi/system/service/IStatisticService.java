package com.ruoyi.system.service;

import com.ruoyi.system.domain.ResearchWorkload;

import java.util.List;
import java.util.Map;

public interface IStatisticService {

    //    查询总计行
    List<ResearchWorkload> selectAll(List<String> dictValues);


}
