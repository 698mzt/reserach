package com.ruoyi.web.controller.system;

//教研室科研工作量（大类积分）

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.service.IStatisticKYGZLService;
import com.ruoyi.system.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jysky")
public class SciJYSKY extends BaseController {

    private String prefix = "system/statistic";

    @Autowired
    private IStatisticService statisticService;


    @GetMapping
    public String index()
    {
        return prefix + "/kygzlJYS";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        // 使用字典数据的dictValue作为筛选条件
        List<String> dictValues = dictList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        startPage();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues);
        TableDataInfo data = getDataTable(list);
        return data;
    }
}
