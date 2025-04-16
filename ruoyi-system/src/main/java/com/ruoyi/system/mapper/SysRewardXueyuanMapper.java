package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

public interface SysRewardXueyuanMapper{

    List<Map<String, Object>> selectAll(String dept);

    List<Map<String, Object>> selectTotal(String dept);

    List<Map<String, Object>> selectXuexiao(String dept);

    List<Map<String, Object>> selectXuexiaoall(String dept);
}
