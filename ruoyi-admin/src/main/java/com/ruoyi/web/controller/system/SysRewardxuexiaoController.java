package com.ruoyi.web.controller.system;

//学校业绩成果数

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SysRewardXueyuanMapper;
import com.ruoyi.system.service.IStatisticYJCGSService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rewardxuexiao")
public class SysRewardxuexiaoController extends BaseController {

    @Autowired
    private SysRewardXueyuanMapper sysRewardXueyuanMapper;
    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/yjcgXX";
    }

@PostMapping("/list")
@ResponseBody
public TableDataInfo list() {
    Long parentId = 100L;
    List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSXY(parentId);
    TableDataInfo data = getDataTable(list);
    return data;

}

}
