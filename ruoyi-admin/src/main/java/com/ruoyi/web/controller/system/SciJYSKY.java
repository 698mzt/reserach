package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.service.IStatisticService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jysky")
public class SciJYSKY extends BaseController {

    private String prefix = "system/statistic";

    @Autowired
    private IStatisticService statisticService;

    @RequiresPermissions("statistic:kygzlJYS:view")
    @GetMapping
    public String index(Model model) {
        return indexWithUser(getSysUser(), model);
    }

    String indexWithUser(SysUser user, Model model) {
        boolean isTeacher = isTeacherRole(user);
        model.addAttribute("modalName", isTeacher ? "我的科研工作量" : "科研工作量");
        model.addAttribute("searchMode", isTeacher ? "none" : "teacherName");
        // 传递当前用户信息到模板，用于空数据占位行显示用户名和部门
        model.addAttribute("user", user);
        return prefix + "/kygzlJYS";
    }

    @RequiresPermissions("statistic:kygzlJYS:view")
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

    @RequiresPermissions("statistic:kygzlJYS:view")
    @Log(title = "导出科研工作量", businessType = BusinessType.EXPORT)
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
        return util.exportExcel(list, resolveExportTitle(getSysUser()));
    }

    String resolveExportTitle(SysUser user) {
        return isTeacherRole(user) ? "我的科研工作量" : "教研室科研工作量";
    }

    private boolean isTeacherRole(SysUser user) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return true;
        }
        Long roleId = user.getRoles().get(0).getRoleId();
        return roleId == 100L;
    }

    private ScopeParam buildScopeParam() {
        SysUser currentUser = getSysUser();
        if (currentUser == null) return new ScopeParam(null, null, null);
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
                if (deptId != null) return new ScopeParam(null, deptId, null);
                return new ScopeParam(null, null, null);
            }
            if (roleId == 100L) {
                return new ScopeParam(user.getUserId(), null, null);
            }
        }

        Long userDeptId = user.getDeptId();
        if (userDeptId != null) return new ScopeParam(null, userDeptId, null);
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

        Long getScopeUserId() { return scopeUserId; }
        Long getScopeDeptId() { return scopeDeptId; }
        Long getCollegeParentId() { return collegeParentId; }
    }
}
