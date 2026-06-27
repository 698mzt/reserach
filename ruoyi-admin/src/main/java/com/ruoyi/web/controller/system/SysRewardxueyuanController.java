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

import com.ruoyi.common.core.domain.AjaxResult;
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
        model.addAttribute("searchMode", "deptName");
        return prefix + "/yjcgXY";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long collegeId = getSysUser().getParentId();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSXYByCollege(collegeId);
        return getDataTable(list);
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        Long collegeId = getSysUser().getParentId();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSXYByCollege(collegeId);
        String[] headers = {"学院", "专业", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)"};
        String[] fieldKeys = {"parentName", "deptName", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)"};
        return MapDataExcelUtil.exportExcel(list, headers, fieldKeys, "学院业绩成果数");
    }
}
