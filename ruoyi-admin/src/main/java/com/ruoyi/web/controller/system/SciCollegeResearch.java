package com.ruoyi.web.controller.system;

//学院科研工作量

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciCollegeResearchMapper;
import com.ruoyi.system.service.IStatisticKYGZLService;
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
@Controller
@RequestMapping("/collegeresearch")
public class SciCollegeResearch extends BaseController {
    @Autowired
    private SciCollegeResearchMapper sciCollegeResearchMapper;
    @Autowired
    private IStatisticKYGZLService IStatisticKYGZLService;
    private String prefix = "system/statistic";

    @GetMapping
    public String index()
    {
        return prefix + "/kygzlXY";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long parentId = getSysUser().getParentId();
        startPage();
        List<Map<String, Object>> list = IStatisticKYGZLService.selectKYGZLXY(parentId);
        TableDataInfo data = getDataTable(list);
        return data;
    }

}
