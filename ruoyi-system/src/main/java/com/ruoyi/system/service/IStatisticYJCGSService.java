package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

/**
 * 教研室业绩成果数
 * */
public interface IStatisticYJCGSService {

    /**
     * 教研室
     * */
    List<Map<String, Object>> selectYJCGSJYS(String deptId);

    /**
     * 学院
     * */
    List<Map<String, Object>> selectYJCGSXY(Long parentId);

}
