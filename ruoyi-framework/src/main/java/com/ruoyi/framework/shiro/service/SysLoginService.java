package com.ruoyi.framework.shiro.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.UserBlockedException;
import com.ruoyi.common.exception.user.UserDeleteException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 登录
     */
    public SysUser login(String username, String password)
    {
        // 验证码校验
        if (ShiroConstants.CAPTCHA_ERROR.equals(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, ShiroUtils.getIp()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }

        // 查询用户信息
        SysUser user = userService.selectUserByLoginName(username);

        /**
        if (user == null && maybeMobilePhoneNumber(username))
        {
            user = userService.selectUserByPhoneNumber(username);
        }

        if (user == null && maybeEmail(username))
        {
            user = userService.selectUserByEmail(username);
        }
        */

        if (user == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }
        
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        setRolePermission(user);
        setDefaultActiveRole(user);
        recordLoginInfo(user.getUserId());
        return user;
    }

    /**
    private boolean maybeEmail(String username)
    {
        if (!username.matches(UserConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username)
    {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
        {
            return false;
        }
        return true;
    }
    */

    /**
     * 设置角色权限
     *
     * @param user 用户信息
     */
    public void setRolePermission(SysUser user)
    {
        List<SysRole> roles = user.getRoles();
        if (!roles.isEmpty())
        {
            // 多角色设置permissions属性，以便数据权限匹配权限
            for (SysRole role : roles)
            {
                // 系统管理员角色拥有所有权限
                Set<String> rolePerms;
                if (role.isAdmin()) {
                    rolePerms = new HashSet<>();
                    rolePerms.add("*:*:*");
                } else {
                    rolePerms = menuService.selectPermsByRoleId(role.getRoleId());
                }
                role.setPermissions(rolePerms);
            }
        }
    }

    /**
     * 设置登录默认活动角色
     * 单角色用户默认进入其唯一角色，多角色用户按"最高权限优先"原则判定
     * 优先级：超级管理员(1) > 科研处(101) > 学院负责人(103-120) > 教研室(102) > 普通教师(100)
     *
     * @param user 用户信息
     */
    public void setDefaultActiveRole(SysUser user)
    {
        List<SysRole> roles = roleService.selectRolesByUserIdExcludingDataScope(user.getUserId());
        List<SysRole> activeRoles = roles.stream()
                .filter(r -> "0".equals(r.getStatus()))
                .collect(Collectors.toList());

        if (activeRoles.isEmpty()) {
            return;
        }

        if (activeRoles.size() > 1) {
            // 多角色用户 → 按"最高权限优先"原则判定身份
            setHighestRole(activeRoles);
        } else {
            // 单角色用户 → 默认进入其唯一角色
            SecurityUtils.getSubject().getSession().setAttribute("activeRoleId", activeRoles.get(0).getRoleId());
        }
    }

    /**
     * 多角色用户按优先级设置最高身份角色
     * 优先级：超级管理员(1) > 科研处(101) > 学院负责人(103-120) > 教研室(102) > 普通教师(100)
     */
    private void setHighestRole(List<SysRole> activeRoles)
    {
        Set<Long> ids = activeRoles.stream()
                .map(SysRole::getRoleId).collect(Collectors.toSet());

        Long selected = null;

        // 超级管理员
        if (ids.contains(1L)) {
            selected = 1L;
        }
        // 科研处
        if (selected == null && ids.contains(101L)) {
            selected = 101L;
        }
        // 学院负责人 (role_id: 103-108学院管理员, 116-120学院负责人)
        if (selected == null) {
            for (long id = 103L; id <= 120L && selected == null; id++) {
                if (ids.contains(id)) {
                    selected = id;
                }
            }
        }
        // 教研室
        if (selected == null && ids.contains(102L)) {
            selected = 102L;
        }
        // 普通教师
        if (selected == null && ids.contains(100L)) {
            selected = 100L;
        }

        final Long finalSelected = selected != null ? selected : activeRoles.get(0).getRoleId();
        SysRole targetRole = activeRoles.stream()
                .filter(r -> finalSelected.equals(r.getRoleId()))
                .findFirst()
                .orElse(activeRoles.get(0));

        SecurityUtils.getSubject().getSession().setAttribute("activeRoleId", targetRole.getRoleId());
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }
}
