package com.ruoyi.system.controller;

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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rewardxuexiao")
public class SysRewardxuexiaoController extends BaseController {

    @Autowired
    private SysRewardXueyuanMapper sysRewardXueyuanMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "/system/rewardxuexiao";
    @GetMapping()
    public String apply()
    {
        return prefix + "/applyxuexiao";
    }

@PostMapping("/list")
@ResponseBody
public TableDataInfo list() {
    String dept = getSysUser().getDeptId().toString();
    List<Map<String, Object>> list = sysRewardXueyuanMapper.selectXuexiao(dept);
    list.add(list.get(0));
    TableDataInfo data = getDataTable(list);
//    data.setRows(list); // 数据列表;
//    data.setTotal(list.size()); // 总记录数
    System.out.println("+++++++++++"+list);
    return data;

}

}
