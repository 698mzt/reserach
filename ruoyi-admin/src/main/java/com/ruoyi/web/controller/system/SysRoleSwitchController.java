package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.ISysRoleService;

/**
 * 角色切换控制器
 * 提供角色切换、切换回全部角色、获取角色列表等接口
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleSwitchController extends BaseController {

    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取当前用户可切换的角色列表
     * 使用排除 DataScope 的方法，避免切换角色后 runAs 导致数据权限过滤角色列表
     */
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
        List<SysRole> roles = roleService.selectRolesByUserIdExcludingDataScope(ShiroUtils.getUserId());
        List<SysRole> activeRoles = roles.stream()
                .filter(r -> "0".equals(r.getStatus()))
                .collect(Collectors.toList());
        return success(activeRoles);
    }

    /**
     * 切换到指定角色
     * 校验该角色是否属于当前用户且已启用，然后存入 Session
     */
    @Log(title = "角色切换", businessType = BusinessType.OTHER)
    @PostMapping("/switch")
    @ResponseBody
    public AjaxResult switchRole(Long roleId) {
        SysUser user = ShiroUtils.getSysUser();

        List<SysRole> userRoles = roleService.selectRolesByUserIdExcludingDataScope(user.getUserId());
        boolean valid = userRoles.stream()
                .anyMatch(r -> roleId.equals(r.getRoleId()) && "0".equals(r.getStatus()));
        if (!valid) {
            return error("无权切换到该角色");
        }

        SecurityUtils.getSubject().getSession().setAttribute("activeRoleId", roleId);
        return success();
    }

    /**
     * 切回全部角色模式
     * 移除 Session 中的 activeRoleId 属性
     */
    @Log(title = "切换回全部角色", businessType = BusinessType.OTHER)
    @PostMapping("/switchBack")
    @ResponseBody
    public AjaxResult switchBack() {
        SecurityUtils.getSubject().getSession().removeAttribute("activeRoleId");
        return success();
    }
}
