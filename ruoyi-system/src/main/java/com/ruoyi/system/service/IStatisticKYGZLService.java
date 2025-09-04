package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
/**
* 科研工作量
* */
public interface IStatisticKYGZLService {

    /**
    * 教研室
    * */
    List<Map<String, Object>> selectKYGZLJYS(Long deptId);

    /**
    * 学院
    * */
    List<Map<String, Object>> selectKYGZLXY(Long parentId);

//    /**
//    * 学校
//    * */
//    List<Map<String, Object>> selectKYGZLXX(Long deptId);
}
