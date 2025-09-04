package com.ruoyi.web.controller.system;

//<!--学院业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SysRewardXueyuanMapper;
import com.ruoyi.system.service.IStatisticYJCGSService;
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
    private IStatisticYJCGSService statisticYJCGSService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/yjcgXY";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long parentId = getSysUser().getParentId();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSXY(parentId);
        TableDataInfo data = getDataTable(list);
        return data;
    }
}