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
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("statistic:kygzlJYS:view")
    @GetMapping
    public String index(org.springframework.ui.Model model) {
        SysUser currentUser = getSysUser();
        model.addAttribute("modalName", isPureTeacher(currentUser) ? "我的科研工作量" : "科研工作量");
        model.addAttribute("searchMode", isPureTeacher(currentUser) ? "none" : "teacherName");
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
        SysUser currentUser = getSysUser();
        String exportTitle = isPureTeacher(currentUser) ? "我的科研工作量" : "教研室科研工作量";
        return util.exportExcel(list, exportTitle);
    }

    /**
     * 判断当前用户是否为纯教师身份（仅 roleId=100，无其他业务角色）
     * 允许同时拥有roleId=1（超级管理员），但不允许有其他业务角色
     */
    private boolean isPureTeacher(SysUser user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        boolean hasTeacher = false;
        for (SysRole role : user.getRoles()) {
            if (role == null || role.getRoleId() == null) {
                continue;
            }
            long rid = role.getRoleId();
            if (rid == 100L) {
                hasTeacher = true;
            } else if (rid != 1L) {
                // 如果有非教师、非超管的其它角色，则不是纯教师
                return false;
            }
        }
        return hasTeacher;
    }

    private ScopeParam buildScopeParam() {
        SysUser currentUser = getSysUser();
        if (currentUser == null) {
            return new ScopeParam(null, null, null);
        }
        
        // 从数据库重新查询用户及其角色，确保获取完整的角色信息
        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();
        
        boolean hasAdmin = false;
        boolean hasCollege = false;
        boolean hasResearchDept = false;
        boolean hasTeacher = false;
        boolean hasSciResearch = false;
        
        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) {
                    continue;
                }
                long roleId = role.getRoleId();
                if (roleId == 1L) {
                    hasAdmin = true;
                }
                if (roleId == 101L) {
                    hasSciResearch = true;
                }
                if (roleId == 102L) {
                    hasResearchDept = true;
                }
                if ((roleId >= 103L && roleId <= 108L) || (roleId >= 116L && roleId <= 120L)) {
                    hasCollege = true;
                }
                if (roleId == 100L) {
                    hasTeacher = true;
                }
            }
        }
        
        if (hasAdmin || hasSciResearch) {
            return new ScopeParam(null, null, null);
        }
        if (hasCollege) {
            return new ScopeParam(null, null, currentUser.getDeptId());
        }
        if (hasResearchDept) {
            // 使用从数据库查询到的deptId
            Long deptId = (dbUser != null) ? dbUser.getDeptId() : currentUser.getDeptId();
            // 如果deptId不为空，按教研室范围查询；否则不限制
            if (deptId != null) {
                return new ScopeParam(null, deptId, null);
            }
            return new ScopeParam(null, null, null);
        }
        // 纯教师用户只能看到自己的数据
        if (hasTeacher) {
            return new ScopeParam(currentUser.getUserId(), null, null);
        }
        Long userDeptId = currentUser.getDeptId();
        if (userDeptId != null) {
            return new ScopeParam(null, userDeptId, null);
        }
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

        private Long getScopeUserId() {
            return scopeUserId;
        }

        private Long getScopeDeptId() {
            return scopeDeptId;
        }

        private Long getCollegeParentId() {
            return collegeParentId;
        }
    }
}
