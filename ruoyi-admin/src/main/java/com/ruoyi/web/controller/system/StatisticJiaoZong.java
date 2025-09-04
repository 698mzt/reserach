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
import com.ruoyi.system.service.IStatisticYJCGSService;
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
    private IStatisticYJCGSService statisticYJCGSService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/yjcgJYS";
    }

    @PostMapping("/listJZ")
    @ResponseBody
    public TableDataInfo listJZ() {
        String dept = getSysUser().getDeptId().toString();
        startPage();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSJYS(dept);

        TableDataInfo data = getDataTable(list);
        return data;
    }
}