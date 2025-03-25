package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciCollegeResearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collegeresearch")
public class SciCollegeResearch extends BaseController {
    @Autowired
    private SciCollegeResearchMapper sciCollegeResearchMapper;
    private String prefix = "/system/collegeresearch";
    @GetMapping
    public String index()
    {
        return prefix + "/index";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        List<Map<String, Object>> list = sciCollegeResearchMapper.selectCollegeResearch();
        System.out.println("list = " + list);
        TableDataInfo data = getDataTable(list);

        data.setRows(list); // 数据列表
        data.setTotal(list.size()); // 总记录数

        System.out.println("TableDataInfo: " + data);

        return data;
    }
}
