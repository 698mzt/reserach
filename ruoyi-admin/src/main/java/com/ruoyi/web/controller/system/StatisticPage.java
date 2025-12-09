package com.ruoyi.web.controller.system;
//<!--教研室科研工作任务计划表-->
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.mapper.StatisticMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.IStatisticService;
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
    @Autowired
    private IStatisticService statisticService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/kygzrwJYS";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        String dept = getSysUser().getDeptId().toString();
        startPage();
//        List<Map<String, Object>> list = statisticService.selectAll(dept);
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        // 使用字典数据的dictValue作为筛选条件
        List<String> dictValues = dictList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        startPage();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues);
        TableDataInfo data = getDataTable(list);
        System.out.println(data);
        return data;
    }
}