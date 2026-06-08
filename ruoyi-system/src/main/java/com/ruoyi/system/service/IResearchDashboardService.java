package com.ruoyi.system.service;

import java.util.Map;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 科研首页数据聚合服务.
 */
public interface IResearchDashboardService
{
    Map<String, Object> getDashboard(SysUser user, String year);
}
