package com.ruoyi.web.controller.system;

//教研室科研工作量

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.IStatisticKYGZLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jysky")
public class SciJYSKY extends BaseController {

    private String prefix = "system/statistic";

    @Autowired
    private IStatisticKYGZLService statisticKYGZLService;


    @GetMapping
    public String index()
    {
        return prefix + "/kygzlJYS";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long deptId =getSysUser().getDeptId();
        startPage();
        List<Map<String, Object>> list = statisticKYGZLService.selectKYGZLJYS(deptId);
        return getDataTable(list);
    }
}
