package com.ruoyi.system.service;

import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;

import java.util.List;

public interface IStatisticService {

    List<ResearchWorkload> selectAllTeacher(List<String> dictValues, String pname, String dname, String userName,
                                            Long scopeUserId, Long scopeDeptId, Long collegeParentId);

    List<ResearchWorkloadByJYS> selectAllDept(List<String> dictValues, String pname, String dname, String userName, Long collegeParentId);
}
