package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticMapper {

    List<ResearchWorkload> selectAllTeacher(@Param("dictValue") List<String> dictValues,
                                            @Param("pname") String pname,
                                            @Param("dname") String dname,
                                            @Param("userName") String userName,
                                            @Param("scopeUserId") Long scopeUserId,
                                            @Param("scopeDeptId") Long scopeDeptId,
                                            @Param("collegeParentId") Long collegeParentId);

    List<ResearchWorkloadByJYS> selectAllDept(@Param("dictValue") List<String> dictValues, @Param("pname")String pname, @Param("dname")String dname, @Param("userName")String userName, @Param("collegeParentId")Long collegeParentId);
}
