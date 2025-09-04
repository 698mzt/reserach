package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface StatisticJZMapper {

    @MapKey("deptId")
    List<Map<String, Object>> selectAllJZ(String deptId);

    @MapKey("parentId")
    List<Map<String, Object>> selectYJCGSXY(Long parentId);

}
