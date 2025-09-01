package com.ruoyi.web.controller.system;

//学校业绩成果数

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SysRewardXueyuanMapper;
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

@Controller
@RequestMapping("/rewardxuexiao")
public class SysRewardxuexiaoController extends BaseController {

    @Autowired
    private SysRewardXueyuanMapper sysRewardXueyuanMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/yjcgXX";
    }

@PostMapping("/list")
@ResponseBody
public TableDataInfo list() {
    String dept = getSysUser().getDeptId().toString();
    List<LinkedHashMap<String, Object>> list = sysRewardXueyuanMapper.selectXuexiao(dept);
//    list.add(list.get(0));
    TableDataInfo data = getDataTable(list);
//    data.setRows(list); // 数据列表;
//    data.setTotal(list.size()); // 总记录数
    System.out.println("+++++++++++"+list);
    return data;

}

}
