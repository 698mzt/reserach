package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciPA;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 科研工作量
 * */
public interface SciCollegeResearchMapper {
    /*
    * 查询学院数据
    * */
    List<Map<String, Object>> selectKYGZLXY(@Param("parentId") Long parentId);

}