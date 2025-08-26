package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciPA;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SciCollegeResearchMapper {
    //先查个列表
    List<Map<String, Object>> selectCollegeResearch(@Param("deptId") Long deptId);

    //查个ID
    List<Map<String, Object>> selectCollegeResearchID(@Param("deptId") Long deptId);


    //这个是查所有学院的数据，
    List<Map<String, Object>> GeneralCollegeSearcherList();


    List<SciPA> selectCollegeResearchList(Long deptId);
}