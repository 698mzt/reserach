package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciPA;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SciCollegeResearchMapper {
    List<Map<String, Object>> selectCollegeResearch(@Param("deptId") Long deptId);

    List<Map<String, Object>>  selectCollegeResearchID(@Param("deptId") Long deptId);

    List<SciPA> selectCollegeResearchList(Long deptId);
}