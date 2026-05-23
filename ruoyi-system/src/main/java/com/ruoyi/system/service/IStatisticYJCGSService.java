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
    List<Map<String, Object>> selectYJCGSJYS(String deptId, Long userId);

    /**
     * 学院
     * */
    List<Map<String, Object>> selectYJCGSXY(Long parentId);

    List<Map<String, Object>> selectYJCGSXYByCollege(Long collegeId);

    /**
     * 学校/科研处
     * */
    List<Map<String, Object>> selectYJCGSXX();

    List<Map<String, Object>> selectYJCGSKYC(String userName);

    /**
     * 科研处业绩成果数-全量（管理员，不过滤学院）
     * */
    List<Map<String, Object>> selectAllYJCGSKYC(String userName);

}
