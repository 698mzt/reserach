package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.service.IStatisticService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/researchdept")
public class SciResearchDeptController extends BaseController {

    private String prefix = "system/statistic";

    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private ISysDeptService sysDeptService;

    @RequiresPermissions("statistic:kygzlKYG:view")
    @GetMapping
    public String index() {
        return prefix + "/kygzlKYG";
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

    @RequiresPermissions("statistic:kygzlKYG:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String pname, String dname, String userName) {
        List<String> dictValues = getCollegeDictValues();
        startPage();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues, pname, dname, userName,
                null, null, null);
        return getDataTable(list);
    }

    /**
     * 获取全量科研工作量数据（不分页），供饼图默认展示
     */
    @RequiresPermissions("statistic:kygzlKYG:view")
    @PostMapping("/collegeSummary")
    @ResponseBody
    public TableDataInfo collegeSummary() {
        List<String> dictValues = getCollegeDictValues();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues, null, null, null,
                null, null, null);
        return getDataTable(list);
    }

    @RequiresPermissions("statistic:kygzlKYG:view")
    @Log(title = "导出科研处科研工作量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String pname, String dname, String userName) {
        List<String> dictValues = getCollegeDictValues();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues, pname, dname, userName,
                null, null, null);
        ExcelUtil<ResearchWorkload> util = new ExcelUtil<ResearchWorkload>(ResearchWorkload.class);
        return util.exportExcel(list, "科研处科研工作量");
    }
}
