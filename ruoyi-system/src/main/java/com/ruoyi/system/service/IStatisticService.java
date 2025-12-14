package com.ruoyi.system.service;

import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.domain.SciHorizontalApply;

import java.util.List;
import java.util.Map;

public interface IStatisticService {

    //    查询总计行
    List<ResearchWorkload> selectAllTeacher(List<String> dictValues,String pname, String dname);


    //    查询总计行
    List<ResearchWorkloadByJYS> selectAllDept(List<String> dictValues, String pname, String dname);


}
