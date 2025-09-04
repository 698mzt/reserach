package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface StatisticJZMapper {

    @MapKey("userId")
    List<Map<String, Object>> selectAllJZ(String deptId);
}
