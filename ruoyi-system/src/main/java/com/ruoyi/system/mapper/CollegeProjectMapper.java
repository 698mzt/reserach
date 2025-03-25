package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CollegeProjectMapper {
    String getDeptName(Long deptId);

    List<Map<String, Object>> selAll(@Param("deptId")Long deptId);
}
