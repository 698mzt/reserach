package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface SciJYSKYMapper {
//    科研工作量教研室
    @MapKey("deptId")
    List<Map<String, Object>> selectKYGZLJYS(@Param("deptId") long deptId);

//    科研工作量学院
    @MapKey("parentId")
    List<Map<String, Object>> selectKYGZLXY(@Param("parentId") long parentId);
}