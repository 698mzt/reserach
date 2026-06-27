package com.ruoyi.web.controller.system;

//<!--学院科研任务计划-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.service.IStatisticService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collegeplan")
public class SciCollegePlanController extends BaseController {

    @Autowired
    private IStatisticService statisticService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:plan:view")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("modalName", "学院科研任务计划");
        model.addAttribute("searchMode", "deptName");
        return prefix + "/kygzrwXY";
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        List<String> dictValues = DictUtils.getDictCache("sys_acade_dept").stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        startPage();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, null, null, null, getSysUser().getParentId());
        return getDataTable(list);
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        List<String> dictValues = DictUtils.getDictCache("sys_acade_dept").stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, null, null, null, getSysUser().getParentId());
        ExcelUtil<ResearchWorkloadByJYS> util = new ExcelUtil<ResearchWorkloadByJYS>(ResearchWorkloadByJYS.class);
        return util.exportExcel(list, "学院科研任务计划");
    }
}
