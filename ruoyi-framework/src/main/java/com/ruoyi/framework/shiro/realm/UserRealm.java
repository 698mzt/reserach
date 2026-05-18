package com.ruoyi.framework.shiro.realm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.RoleBlockedException;
import com.ruoyi.common.exception.user.UserBlockedException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.framework.shiro.service.SysLoginService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;

/**
 * 自定义Realm 处理登录 权限
 * 
 * @author ruoyi
 */
public class UserRealm extends AuthorizingRealm
{
    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysLoginService loginService;

    /**
     * 授权
     * 从 Session 读取活动角色 ID，校验有效性后只返回该角色的权限。
     * 若角色已失效则自动回退（优先普通教师角色）。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        Long activeRoleId = getActiveRoleIdFromSession();
        if (activeRoleId != null) {
            Long userId = ShiroUtils.getUserId();
            List<SysRole> userRoles = roleService.selectRolesByUserIdExcludingDataScope(userId);
            boolean valid = false;
            for (SysRole role : userRoles) {
                if (activeRoleId.equals(role.getRoleId()) && "0".equals(role.getStatus())) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                activeRoleId = findTeacherRole(userRoles);
                if (activeRoleId != null) {
                    SecurityUtils.getSubject().getSession().setAttribute("activeRoleId", activeRoleId);
                }
            }
        }

        if (activeRoleId != null) {
            SysRole role = roleService.selectRoleById(activeRoleId);
            if (role != null) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                info.setRoles(new HashSet<>(Arrays.asList(role.getRoleKey())));
                if (role.isAdmin()) {
                    info.addStringPermission("*:*:*");
                } else {
                    info.setStringPermissions(menuService.selectPermsByRoleId(activeRoleId));
                }
                return info;
            }
        }

        SysUser user = ShiroUtils.getSysUser();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user.isAdmin())
        {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        }
        else
        {
            info.setRoles(roleService.selectRoleKeys(user.getUserId()));
            info.setStringPermissions(menuService.selectPermsByUserId(user.getUserId()));
        }
        return info;
    }

    /**
     * 从当前 Session 中读取活动角色 ID
     */
    private Long getActiveRoleIdFromSession() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Session session = subject.getSession(false);
            if (session != null) {
                return (Long) session.getAttribute("activeRoleId");
            }
        }
        return null;
    }

    /**
     * 查找普通教师角色，若无则返回第一个启用的角色
     */
    private Long findTeacherRole(List<SysRole> roles) {
        Long firstAvailable = null;
        for (SysRole role : roles) {
            if (!"0".equals(role.getStatus())) {
                continue;
            }
            if (firstAvailable == null) {
                firstAvailable = role.getRoleId();
            }
            if ("teacher".equals(role.getRoleKey())) {
                return role.getRoleId();
            }
        }
        return firstAvailable;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";
        if (upToken.getPassword() != null)
        {
            password = new String(upToken.getPassword());
        }

        SysUser user = null;
        try
        {
            user = loginService.login(username, password);
        }
        catch (CaptchaException e)
        {
            throw new AuthenticationException(e.getMessage(), e);
        }
        catch (UserNotExistsException e)
        {
            throw new UnknownAccountException(e.getMessage(), e);
        }
        catch (UserPasswordNotMatchException e)
        {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
        catch (UserPasswordRetryLimitExceedException e)
        {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        }
        catch (UserBlockedException e)
        {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (RoleBlockedException e)
        {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (Exception e)
        {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

    /**
     * 重写授权缓存 key 生成逻辑，将活动角色 ID 纳入 key。
     * 不同活动角色的权限独立缓存，互不冲突。
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Long activeRoleId = getActiveRoleIdFromSession();
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        return new CacheKey(user.getUserId(), activeRoleId);
    }

    /**
     * 清理指定用户授权信息缓存
     */
    public void clearCachedAuthorizationInfo(Object principal)
    {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        this.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清理所有用户授权信息缓存
     */
    public void clearAllCachedAuthorizationInfo()
    {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null)
        {
            for (Object key : cache.keys())
            {
                cache.remove(key);
            }
        }
    }

    /**
     * 授权缓存 key 包装类。
     * 基于 userId + activeRoleId 计算 equals/hashCode，
     * 不依赖 SysUser 或 PrincipalCollection 的 equals 实现。
     */
    private static class CacheKey {
        private final Long userId;
        private final Long activeRoleId;

        CacheKey(Long userId, Long activeRoleId) {
            this.userId = userId;
            this.activeRoleId = activeRoleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheKey)) return false;
            CacheKey key = (CacheKey) o;
            return Objects.equals(userId, key.userId) && Objects.equals(activeRoleId, key.activeRoleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, activeRoleId);
        }
    }
}
