package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

public interface StatisticJZMapper {

    List<Map<String, Object>> selectAllJZ(String deptId);

    List<Map<String, Object>> selectTotalJZ(String dept);
}
