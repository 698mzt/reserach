package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ResearchWorkload;

import java.util.List;

public interface StatisticMapper {

    List<ResearchWorkload> selectAll(List<String> dictValues);

}
