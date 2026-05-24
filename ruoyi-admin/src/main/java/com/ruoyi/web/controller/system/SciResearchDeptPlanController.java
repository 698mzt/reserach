package com.ruoyi.web.controller.system;

//<!--科研处科研任务计划-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.service.IStatisticService;
import com.ruoyi.system.service.ISysDeptService;
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
@RequestMapping("/researchdeptplan")
public class SciResearchDeptPlanController extends BaseController {

    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private ISysDeptService sysDeptService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:plan:view")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("modalName", "科研处科研任务计划");
        model.addAttribute("searchMode", "teacherName");
        return prefix + "/kygzrwKYG";
    }

    private List<String> getCollegeDictValues() {
        if (getSysUser().isAdmin()) {
            return null;
        }
        SysDept deptParam = new SysDept();
        deptParam.setParentId(100L);
        deptParam.setDelFlag("0");
        List<SysDept> deptList = sysDeptService.selectDeptList(deptParam);
        return deptList.stream()
                .filter(d -> d.getDeptName() != null && d.getDeptName().contains("学院"))
                .map(d -> String.valueOf(d.getDeptId()))
                .collect(Collectors.toList());
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String pname, String dname) {
        List<String> dictValues = getCollegeDictValues();
        startPage();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, pname, dname, null, null);
        return getDataTable(list);
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String pname, String dname) {
        List<String> dictValues = getCollegeDictValues();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, pname, dname, null, null);
        ExcelUtil<ResearchWorkloadByJYS> util = new ExcelUtil<ResearchWorkloadByJYS>(ResearchWorkloadByJYS.class);
        return util.exportExcel(list, "科研处科研任务计划");
    }
}
