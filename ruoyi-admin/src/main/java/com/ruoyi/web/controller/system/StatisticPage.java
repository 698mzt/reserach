package com.ruoyi.web.controller.system;
//<!--教研室科研工作任务计划表-->
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.service.IStatisticService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistic")
public class StatisticPage extends BaseController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private IStatisticService statisticService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:plan:view")
    @GetMapping()
    public String apply(Model model) {
        SysUser currentUser = getSysUser();
        // 从数据库重新查询用户及其角色
        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();

        boolean hasAdmin = false;
        boolean hasResearchDept = false;
        boolean hasTeacher = false;
        boolean hasSciResearch = false;

        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long roleId = role.getRoleId();
                if (roleId == 1L) hasAdmin = true;
                if (roleId == 102L) hasResearchDept = true;
                if (roleId == 101L) hasSciResearch = true;
                if (roleId == 100L) hasTeacher = true;
            }
        }

        // admin/科研处角色重定向到科研处页面（全量数据）
        if (hasAdmin || hasSciResearch) {
            return "redirect:/researchdeptplan";
        }
        // 学院角色重定向到学院页面
        if ((dbUser != null ? dbUser.getRoles() : currentUser.getRoles()) != null) {
            for (SysRole role : (dbUser != null ? dbUser.getRoles() : currentUser.getRoles())) {
                if (role == null || role.getRoleId() == null) continue;
                long rid = role.getRoleId();
                if ((rid >= 103L && rid <= 108L) || (rid >= 116L && rid <= 120L)) {
                    return "redirect:/collegeplan";
                }
            }
        }

        boolean isPureTeacher = hasTeacher && !hasResearchDept;
        model.addAttribute("modalName", isPureTeacher ? "我的科研任务计划" : "教研室科研任务计划");
        model.addAttribute("searchMode", isPureTeacher ? "none" : "teacherName");
        return prefix + "/kygzrwJYS";
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String pname, String dname, String userName) {
        List<String> dictValues = DictUtils.getDictCache("sys_acade_dept").stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        ScopeParam scopeParam = buildScopeParam();
        startPage();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues, pname, dname, userName,
                scopeParam.getScopeUserId(), scopeParam.getScopeDeptId(), scopeParam.getCollegeParentId());
        return getDataTable(list);
    }

    private ScopeParam buildScopeParam() {
        SysUser currentUser = getSysUser();
        if (currentUser == null) return new ScopeParam(null, null, null);

        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();

        boolean hasAdmin = false, hasCollege = false, hasResearchDept = false, hasTeacher = false, hasSciResearch = false;
        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long roleId = role.getRoleId();
                if (roleId == 1L) hasAdmin = true;
                if (roleId == 101L) hasSciResearch = true;
                if (roleId == 102L) hasResearchDept = true;
                if ((roleId >= 103L && roleId <= 108L) || (roleId >= 116L && roleId <= 120L)) hasCollege = true;
                if (roleId == 100L) hasTeacher = true;
            }
        }

        if (hasAdmin || hasSciResearch) return new ScopeParam(null, null, null);
        if (hasCollege) return new ScopeParam(null, null, currentUser.getDeptId());
        if (hasResearchDept) {
            Long deptId = (dbUser != null) ? dbUser.getDeptId() : currentUser.getDeptId();
            if (deptId != null) return new ScopeParam(null, deptId, null);
            return new ScopeParam(null, null, null);
        }
        if (hasTeacher) return new ScopeParam(currentUser.getUserId(), null, null);

        Long userDeptId = currentUser.getDeptId();
        if (userDeptId != null) return new ScopeParam(null, userDeptId, null);
        return new ScopeParam(null, null, null);
    }

    private static class ScopeParam {
        private final Long scopeUserId;
        private final Long scopeDeptId;
        private final Long collegeParentId;
        private ScopeParam(Long scopeUserId, Long scopeDeptId, Long collegeParentId) {
            this.scopeUserId = scopeUserId;
            this.scopeDeptId = scopeDeptId;
            this.collegeParentId = collegeParentId;
        }
        private Long getScopeUserId() { return scopeUserId; }
        private Long getScopeDeptId() { return scopeDeptId; }
        private Long getCollegeParentId() { return collegeParentId; }
    }

    @RequiresPermissions("statistic:plan:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String pname, String dname, String userName) {
        List<String> dictValues = DictUtils.getDictCache("sys_acade_dept").stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());
        ScopeParam scopeParam = buildScopeParam();
        List<ResearchWorkload> list = statisticService.selectAllTeacher(dictValues, pname, dname, userName,
                scopeParam.getScopeUserId(), scopeParam.getScopeDeptId(), scopeParam.getCollegeParentId());
        ExcelUtil<ResearchWorkload> util = new ExcelUtil<ResearchWorkload>(ResearchWorkload.class);
        return util.exportExcel(list, "科研任务计划");
    }
}
