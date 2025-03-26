package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SysRewardXueyuanMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rewardxueyuan")
public class SysRewardxueyuanController extends BaseController {

    @Autowired
    private SysRewardXueyuanMapper sysRewardXueyuanMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "/system/rewardxueyuan";
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        startPage();
        String dept = getSysUser().getDeptId().toString();
        List<Map<String, Object>> list = sysRewardXueyuanMapper.selectAll(dept);
        List<Map<String, Object>> list1 = sysRewardXueyuanMapper.selectTotal(dept);
        list.add(list1.get(0));
        TableDataInfo data = getDataTable(list);
        System.out.println(list1);
        return data;
    }
}