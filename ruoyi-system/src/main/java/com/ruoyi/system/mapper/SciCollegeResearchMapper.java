package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciPA;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SciCollegeResearchMapper {
    //先查个列表吧
    List<Map<String, Object>> selectCollegeResearch(@Param("deptId") Long deptId);

    //查个ID
    List<Map<String, Object>> selectCollegeResearchID(@Param("deptId") Long deptId);


    //这个是单纯为了查所有学院的数据，
    List<Map<String, Object>> GeneralCollegeSearcherList();


    //这个方法是为了导出，导出需要一个实体类，目前来看不好说，所以先用这个，后面再看
    List<SciPA> selectCollegeResearchList(Long deptId);
}