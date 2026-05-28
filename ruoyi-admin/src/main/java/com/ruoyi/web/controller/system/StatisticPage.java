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
    private IStatisticService statisticService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:plan:view")
    @GetMapping()
    public String apply(Model model) {
        return applyWithUser(getSysUser(), model);
    }

    String applyWithUser(SysUser user, Model model) {
        List<SysRole> roles = user.getRoles();

        if (roles != null && !roles.isEmpty()) {
            Long roleId = roles.get(0).getRoleId();
            if (roleId == 1L || roleId == 101L) {
                return "redirect:/researchdeptplan";
            }
            if ((roleId >= 103L && roleId <= 108L) || (roleId >= 116L && roleId <= 120L)) {
                return "redirect:/collegeplan";
            }
            if (roleId == 102L) {
                model.addAttribute("modalName", "教研室科研任务计划");
                model.addAttribute("searchMode", "teacherName");
                // 传递当前用户信息到模板，用于空数据占位行显示
                model.addAttribute("user", user);
                return prefix + "/kygzrwJYS";
            }
        }

        model.addAttribute("modalName", "我的科研任务计划");
        model.addAttribute("searchMode", "none");
        // 传递当前用户信息到模板，用于空数据占位行显示用户名和部门
        model.addAttribute("user", user);
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
        if (currentUser == null)
            return new ScopeParam(null, null, null);
        return buildScopeParamWithUser(currentUser);
    }

    ScopeParam buildScopeParamWithUser(SysUser user) {
        List<SysRole> roles = user.getRoles();

        if (roles != null && !roles.isEmpty()) {
            Long roleId = roles.get(0).getRoleId();
            if (roleId == 1L || roleId == 101L) {
                return new ScopeParam(null, null, null);
            }
            if ((roleId >= 103L && roleId <= 108L) || (roleId >= 116L && roleId <= 120L)) {
                return new ScopeParam(null, null, user.getDeptId());
            }
            if (roleId == 102L) {
                Long deptId = user.getDeptId();
                if (deptId != null)
                    return new ScopeParam(null, deptId, null);
                return new ScopeParam(null, null, null);
            }
            if (roleId == 100L) {
                return new ScopeParam(user.getUserId(), null, null);
            }
        }

        Long userDeptId = user.getDeptId();
        if (userDeptId != null)
            return new ScopeParam(null, userDeptId, null);
        return new ScopeParam(null, null, null);
    }

    static class ScopeParam {
        private final Long scopeUserId;
        private final Long scopeDeptId;
        private final Long collegeParentId;

        ScopeParam(Long scopeUserId, Long scopeDeptId, Long collegeParentId) {
            this.scopeUserId = scopeUserId;
            this.scopeDeptId = scopeDeptId;
            this.collegeParentId = collegeParentId;
        }

        Long getScopeUserId() {
            return scopeUserId;
        }

        Long getScopeDeptId() {
            return scopeDeptId;
        }

        Long getCollegeParentId() {
            return collegeParentId;
        }
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
