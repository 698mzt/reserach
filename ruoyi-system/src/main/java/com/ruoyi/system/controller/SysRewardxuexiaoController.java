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
    startPage();
    List<Map<String, Object>> list = sysRewardXueyuanMapper.selectXuexiao(dept);

    // 确保所有行都有学院字段
    list.forEach(row -> {
        if (!row.containsKey("学院")) {
            row.put("学院", "N/A");
        }
        if (row.get("专业") == null) {
            row.put("isCollegeTotal", true);
        }
    });

    // 添加总合计行前检查空数据
    List<Map<String, Object>> grandList = sysRewardXueyuanMapper.selectXuexiaoall(dept);
    if (!grandList.isEmpty()) {
        Map<String, Object> grandTotal = grandList.get(0);
        grandTotal.put("isGrandTotal", true);
        list.add(grandTotal);
    }

    return getDataTable(list);
}

}
