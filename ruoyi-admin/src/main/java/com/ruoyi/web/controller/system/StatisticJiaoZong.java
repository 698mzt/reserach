package com.ruoyi.web.controller.system;

//<!--教研室业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.mapper.StatisticJZMapper;
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

@Controller
@RequestMapping("/statisticJZ")
public class StatisticJiaoZong extends BaseController {

    @Autowired
    private StatisticJZMapper statisticJZMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/yjcgJYS.html";
    }

    @PostMapping("/listJZ")
    @ResponseBody
    public TableDataInfo listJZ() {
        startPage();
        String dept = getSysUser().getDeptId().toString();
//        String ParentId = getSysUser().getParentId().toString();
//        System.out.println(ParentId);


        List<Map<String, Object>> list = statisticJZMapper.selectAllJZ(dept);
        List<Map<String, Object>> list1 = statisticJZMapper.selectTotalJZ(dept);

        List<SysUser> users = userService.selectAllUser();
//        todo:获取部门信息错误：list




        list.add(list1.get(0));

        TableDataInfo data = getDataTable(list);
        System.out.println(list);
        System.out.println(list1);
        return data;
    }
}