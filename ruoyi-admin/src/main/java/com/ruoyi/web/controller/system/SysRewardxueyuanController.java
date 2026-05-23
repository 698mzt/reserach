package com.ruoyi.web.controller.system;

//<!--学院业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.IStatisticYJCGSService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/rewardxueyuan")
public class SysRewardxueyuanController extends BaseController {

    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:achievement:view")
    @GetMapping()
    public String apply(Model model) {
        model.addAttribute("modalName", "学院业绩成果数");
        return prefix + "/yjcgXY";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long collegeId = getSysUser().getDeptId();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSXYByCollege(collegeId);
        return getDataTable(list);
    }
}
