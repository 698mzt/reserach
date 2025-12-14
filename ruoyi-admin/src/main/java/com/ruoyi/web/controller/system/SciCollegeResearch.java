package com.ruoyi.web.controller.system;

//学院科研工作量

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.mapper.SciCollegeResearchMapper;
import com.ruoyi.system.service.IStatisticKYGZLService;
import com.ruoyi.system.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collegeresearch")
public class SciCollegeResearch extends BaseController {

    @Autowired
    private IStatisticService statisticService;
    private String prefix = "system/statistic";

    @GetMapping
    public String index()
    {
        return prefix + "/kygzlXY";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String pname, String dname) {
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        // 使用字典数据的dictValue作为筛选条件
        List<String> dictValues = dictList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        startPage();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues,pname, dname);
        TableDataInfo data = getDataTable(list);
        return data;
    }

    @Log(title = "导出教研室科研工作量（教研室版）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String pname, String dname)
    {
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        // 使用字典数据的dictValue作为筛选条件
        List<String> dictValues = dictList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, pname, dname);
        ExcelUtil<ResearchWorkloadByJYS> util = new ExcelUtil<ResearchWorkloadByJYS>(ResearchWorkloadByJYS.class);
        return util.exportExcel(list, "教研室科研工作量");
    }

}
