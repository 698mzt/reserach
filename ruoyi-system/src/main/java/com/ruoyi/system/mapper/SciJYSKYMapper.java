package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface SciJYSKYMapper {
    List<Map<String, Object>> selectJYSKY(@Param("deptId") long deptId);
}