package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SciCollegeResearchMapper {
    List<Map<String, Object>> selectCollegeResearch(@Param("deptId") Long deptId);
}