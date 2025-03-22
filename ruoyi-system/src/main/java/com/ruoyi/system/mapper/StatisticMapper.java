package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

public interface StatisticMapper {

    List<Map<String, Object>> selectAll(String deptId);

    List<Map<String, Object>> selectTotal(String dept);
}
