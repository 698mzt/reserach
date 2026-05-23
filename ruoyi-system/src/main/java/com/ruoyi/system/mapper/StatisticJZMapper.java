package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StatisticJZMapper {

    @MapKey("deptId")
    List<Map<String, Object>> selectAllJZ(@Param("deptId") String deptId, @Param("userId") Long userId);

    @MapKey("parentId")
    List<Map<String, Object>> selectYJCGSXY(Long parentId);

    List<Map<String, Object>> selectYJCGSXYByDeptId(Long deptId);

    String selectDeptNameById(Long deptId);

    List<Map<String, Object>> selectYJCGSKYC(@Param("userName") String userName);

    List<Map<String, Object>> selectAllYJCGSKYC(@Param("userName") String userName);

    List<Map<String, Object>> selectYJCGSXX();

}
