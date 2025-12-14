package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.domain.SciHorizontalApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StatisticMapper {

    List<ResearchWorkload> selectAllTeacher(@Param("dictValue") List<String> dictValues, @Param("pname")String pname, @Param("dname")String dname);


    List<ResearchWorkloadByJYS> selectAllDept(@Param("dictValue") List<String> dictValues, @Param("pname")String pname, @Param("dname")String dname);


}
