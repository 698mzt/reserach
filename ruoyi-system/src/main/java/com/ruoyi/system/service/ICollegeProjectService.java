package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

public interface ICollegeProjectService {
    Integer judge(Long deptId, String loginName);

    List<Map<String, Object>> selAll( Long deptId);
}
