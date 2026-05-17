package com.ruoyi.framework.aspectj;

import java.util.HashSet;
import java.util.Set;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.utils.ActiveRoleContextHolder;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;

/**
 * 授权信息拦截切面
 * 当用户切换了活动角色时，只返回该角色的角色标识和权限标识，
 * 使 @RequiresPermissions、shiro:hasPermission 等按活动角色判断。
 */
@Aspect
@Component
public class AuthInfoAspect {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Around("execution(* com.ruoyi.framework.shiro.realm.UserRealm.doGetAuthorizationInfo(..))")
    public Object aroundDoGetAuthorizationInfo(ProceedingJoinPoint pjp) throws Throwable {

        Long activeRoleId = ActiveRoleContextHolder.get();

        // 未切换角色 → 走原 Realm 逻辑（返回所有角色的权限）
        if (activeRoleId == null) {
            return pjp.proceed();
        }

        // 已切换 → 构造仅含活动角色的 AuthorizationInfo
        SysRole role = roleService.selectRoleById(activeRoleId);
        if (role == null) {
            // 角色已被管理员删除 → 回退到全部角色模式
            ActiveRoleContextHolder.clear();
            return pjp.proceed();
        }

        Set<String> roleKeys = new HashSet<>();
        roleKeys.add(role.getRoleKey());

        Set<String> permissions;
        // 系统管理员角色拥有所有权限
        if (role.isAdmin()) {
            permissions = new HashSet<>();
            permissions.add("*:*:*");
        } else {
            permissions = menuService.selectPermsByRoleId(activeRoleId);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roleKeys);
        info.setStringPermissions(permissions);
        return info;
    }
}
