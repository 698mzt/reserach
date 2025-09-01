package com.ruoyi.web.controller.system;

//教研室科研工作量

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciJYSKYMapper;
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
    @Autowired
    private SciJYSKYMapper sciJYSKYMapper;
    private String prefix = "system/statistic";
    @GetMapping
    public String index()
    {
        return prefix + "/kygzlJYS";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        Long deptId =getSysUser().getDeptId();
        System.out.println("deptId = " + deptId);
        List<Map<String, Object>> list = sciJYSKYMapper.selectJYSKY(deptId);
        System.out.println("list = " + list);
        TableDataInfo data = getDataTable(list);

        data.setRows(list); // 数据列表
        data.setTotal(list.size()); // 总记录数

        System.out.println("TableDataInfo: " + data);

        return data;
    }
}
