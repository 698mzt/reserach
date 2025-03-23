package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.mapper.StatisticMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/statistic")
public class StatisticPage extends BaseController {

    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    private String prefix = "/system/statistic";
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        startPage();
        String dept = getSysUser().getDeptId().toString();
        List<Map<String, Object>> list = statisticMapper.selectAll(dept);
        List<Map<String, Object>> list1 = statisticMapper.selectTotal(dept);

        List<SysUser> users = userService.selectAllUser();
//        todo:获取部门信息错误：list


        // 新增: 将用户和部门信息添加到list中
        for (Map<String, Object> map : list) {
            Long userId = (Long) map.get("教师");
            Long deptId = (Long) map.get("专业");

            if (userId != null) {
                Optional<SysUser> userOptional = users.stream()
                        .filter(user -> user.getUserId().equals(userId))
                        .findFirst();
                userOptional.ifPresent(user -> map.put("userName", user.getUserName()));
            }

            if (deptId != null){
                SysDept dept1 = deptService.selectDeptById(deptId);
                map.put("deptName", dept1.getDeptName());
            }

        }

        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof byte[]) {
                    String decodedValue = new String((byte[]) value, StandardCharsets.UTF_8);
                    entry.setValue(decodedValue);
                } else if (value instanceof String) {
                    entry.setValue(((String) value).trim());
                }
            }
        }

        list.add(list1.get(0));

        TableDataInfo data = getDataTable(list);
        System.out.println(data);
        return data;
    }
}