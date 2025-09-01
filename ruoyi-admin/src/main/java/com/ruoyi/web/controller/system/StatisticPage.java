package com.ruoyi.web.controller.system;
//<!--教研室科研工作任务计划表-->
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.mapper.StatisticMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistic")
public class StatisticPage extends BaseController {

    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/kygzrwJYS.html";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        startPage();
        String dept = getSysUser().getDeptId().toString();
        List<Map<String, Object>> list = statisticMapper.selectAll(dept);
        List<Map<String, Object>> list1 = statisticMapper.selectTotal(dept);
        if (list == null || list1 == null || list1.isEmpty()) {
            throw new IllegalStateException("查询结果为空");
        }
        List<SysUser> users = userService.selectAllUser();
        Map<Long, SysUser> userMap = users.stream()
                .collect(Collectors.toMap(SysUser::getUserId, user -> user));
        // 新增: 将用户和部门信息添加到list中
        for (Map<String, Object> map : list) {
            Long userId = (Long) map.get("教师");
            Long deptId = (Long) map.get("专业");

            if (userId != null && userMap.containsKey(userId)) {
                SysUser user = userMap.get(userId);
                map.put("userName", user.getUserName());
            }

            if (deptId != null) {
                SysDept dept1 = deptService.selectDeptById(deptId);
                if (dept1 != null) {
                    map.put("deptName", dept1.getDeptName());
                } else {
                    map.put("deptName", "未知部门"); // 防止空值
                }
            }
        }
        for (Map<String, Object> map : list) {
            map.replaceAll((key, value) -> {
                if (value instanceof byte[]) {
                    return new String((byte[]) value, StandardCharsets.UTF_8);
                } else if (value instanceof String) {
                    return ((String) value).trim();
                }
                return value;
            });
        }
        list.add(list1.get(0));
        TableDataInfo data = getDataTable(list);
        System.out.println(data);
        return data;
    }
}