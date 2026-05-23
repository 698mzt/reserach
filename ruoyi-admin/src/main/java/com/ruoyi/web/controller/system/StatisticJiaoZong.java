package com.ruoyi.web.controller.system;

//<!--教研室业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.IStatisticYJCGSService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/statisticJZ")
public class StatisticJiaoZong extends BaseController {

    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;
    @Autowired
    private ISysUserService userService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:achievement:view")
    @GetMapping()
    public String apply(Model model) {
        SysUser currentUser = getSysUser();
        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();

        boolean hasAdmin = false;
        boolean hasSciResearch = false;
        boolean hasCollege = false;
        boolean hasResearchDept = false;
        boolean hasTeacher = false;

        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long rid = role.getRoleId();
                if (rid == 1L) hasAdmin = true;
                if (rid == 101L) hasSciResearch = true;
                if ((rid >= 103L && rid <= 108L) || (rid >= 116L && rid <= 120L)) hasCollege = true;
                if (rid == 102L) hasResearchDept = true;
                if (rid == 100L) hasTeacher = true;
            }
        }

        if (hasAdmin) return "redirect:/rewardxuexiao";
        if (hasSciResearch) return "redirect:/rewardkeyanchu";
        if (hasCollege) return "redirect:/rewardxueyuan";

        boolean isPureTeacher = hasTeacher && !hasResearchDept;
        model.addAttribute("modalName", isPureTeacher ? "我的业绩成果数" : "教研室业绩成果数");
        return prefix + "/yjcgJYS";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/listJZ")
    @ResponseBody
    public TableDataInfo listJZ() {
        SysUser user = getSysUser();
        // 重新查询用户角色
        SysUser dbUser = userService.selectUserById(user.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : user.getRoles();

        boolean isAdminOrResearch = false;
        boolean hasTeacher = false;
        boolean hasResearchDept = false;
        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long rid = role.getRoleId();
                if (rid == 1L || rid == 101L) { isAdminOrResearch = true; }
                if (rid == 100L) { hasTeacher = true; }
                if (rid == 102L) { hasResearchDept = true; }
            }
        }

        String deptId = isAdminOrResearch ? null : user.getDeptId().toString();
        Long userId = (hasTeacher && !hasResearchDept) ? user.getUserId() : null;
        startPage();
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSJYS(deptId, userId);
        return getDataTable(list);
    }
}
