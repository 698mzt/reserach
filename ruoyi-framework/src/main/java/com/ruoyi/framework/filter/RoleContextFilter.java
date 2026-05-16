package com.ruoyi.framework.filter;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ActiveRoleContextHolder;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 角色上下文过滤器（Shiro AccessControlFilter）
 * 若用户已切换活动角色，在请求生命周期内用 runAs 切换 Principal
 * 使 ShiroUtils.getSysUser().getRoles() 只返回活动角色
 *
 * 注意：继承 AccessControlFilter，由 ShiroConfig 注册到 Shiro 过滤器链
 *
 * 新增：
 * 1. 角色有效性校验 —— 每次请求检查 activeRoleId 是否仍属于当前用户
 *    如果管理员在后台解绑了该角色，自动回退到普通教师角色或第一个可用角色
 * 2. Principal 刷新 —— 检测管理员添加/删除角色后，自动从 DB 刷新 Principal
 */
public class RoleContextFilter extends AccessControlFilter {

    private static final Logger log = LoggerFactory.getLogger(RoleContextFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {

        Subject subject = getSubject(request, response);
        Session session = subject.getSession(false);

        if (session != null) {
            Long activeRoleId = (Long) session.getAttribute("activeRoleId");

            if (activeRoleId != null) {
                SysUser fullUser = ShiroUtils.getSysUser();
                if (fullUser == null) {
                    return true;
                }

                // 角色有效性校验 + 失效回退
            ValidationResult result = validateAndFallback(fullUser.getUserId(), activeRoleId);
            if (!result.isValid) {
                if (result.fallbackRoleId == null) {
                    // 无任何可用角色，清除 activeRoleId，不执行 runAs
                    ActiveRoleContextHolder.clear();
                    return true;
                }
                // 有回退角色，更新 activeRoleId 并刷新 Principal
                activeRoleId = result.fallbackRoleId;
                session.setAttribute("activeRoleId", activeRoleId);
                // 刷新 Principal 后重新获取用户，确保使用最新的角色信息
                fullUser = refreshPrincipal(subject, fullUser);
            }

                SysUser filteredUser = filterUserByRole(fullUser, activeRoleId);

                if (filteredUser.getRoles() == null || filteredUser.getRoles().isEmpty()) {
                    return true;
                }

                ActiveRoleContextHolder.set(activeRoleId);

                String realmName = subject.getPrincipals()
                        .getRealmNames().iterator().next();
                PrincipalCollection runAsPrincipals =
                        new SimplePrincipalCollection(filteredUser, realmName);

                subject.runAs(runAsPrincipals);
            }
        }

        return true;
    }

    /**
     * 校验结果封装
     */
    private static class ValidationResult {
        final boolean isValid;
        final Long fallbackRoleId;

        ValidationResult(boolean isValid, Long fallbackRoleId) {
            this.isValid = isValid;
            this.fallbackRoleId = fallbackRoleId;
        }
    }

    /**
     * 校验活动角色是否仍然属于当前用户
     * 绕过 DataScope 查询 sys_user_role，确保不受当前活动角色影响
     * 若角色已失效，自动计算回退角色（优先教师角色）
     */
    private ValidationResult validateAndFallback(Long userId, Long activeRoleId) {
        try {
            ISysRoleService roleService = SpringUtils.getBean(ISysRoleService.class);
            List<SysRole> roles = roleService.selectRolesByUserIdExcludingDataScope(userId);

            for (SysRole role : roles) {
                if (activeRoleId.equals(role.getRoleId()) && "0".equals(role.getStatus())) {
                    return new ValidationResult(true, null);
                }
            }

            // 角色失效，计算回退角色
            Long fallbackRoleId = findTeacherRole(roles);

            if (fallbackRoleId != null) {
                log.info("用户 {} 的活动角色 {} 已被解绑或禁用，回退到角色 roleId={}",
                        userId, activeRoleId, fallbackRoleId);
            } else {
                log.info("用户 {} 的活动角色 {} 已被解绑或禁用，且无任何可用角色",
                        userId, activeRoleId);
            }

            return new ValidationResult(false, fallbackRoleId);
        } catch (Exception e) {
            log.warn("校验角色有效性失败，回退到允许访问: userId={}, roleId={}", userId, activeRoleId, e);
            return new ValidationResult(true, null);
        }
    }

    /**
     * 从 DB 刷新用户 Principal，确保包含最新的角色信息
     * 用于处理管理员添加/删除角色后 Principal 不更新的问题
     * @return 刷新后的用户对象
     */
    private SysUser refreshPrincipal(Subject subject, SysUser currentUser) {
        try {
            ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
            SysUser freshUser = userService.selectUserById(currentUser.getUserId());
            if (freshUser != null && freshUser.getRoles() != null) {
                String realmName = subject.getPrincipals()
                        .getRealmNames().iterator().next();
                PrincipalCollection freshPrincipals =
                        new SimplePrincipalCollection(freshUser, realmName);
                subject.runAs(freshPrincipals);
                log.info("用户 {} Principal 已刷新，角色数: {}", currentUser.getUserId(), freshUser.getRoles().size());
                return freshUser;
            }
        } catch (Exception e) {
            log.warn("刷新 Principal 失败，继续使用缓存: userId={}", currentUser.getUserId(), e);
        }
        return currentUser;
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

    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(ServletRequest request,
            ServletResponse response, Exception exception) {
        try {
            Subject subject = getSubject(request, response);
            if (subject.isRunAs()) {
                subject.releaseRunAs();
            }
        } catch (Exception e) {
            // 仅日志，不吞异常传播
        }
        ActiveRoleContextHolder.clear();
    }

    /**
     * 复制用户对象，roles 列表中只保留活动角色
     */
    private SysUser filterUserByRole(SysUser source, Long activeRoleId) {
        SysUser target = new SysUser();
        BeanUtils.copyBeanProp(target, source);

        if (source.getRoles() != null) {
            List<SysRole> filtered = new ArrayList<>();
            for (SysRole role : source.getRoles()) {
                if (activeRoleId.equals(role.getRoleId())) {
                    filtered.add(role);
                    break;
                }
            }
            target.setRoles(filtered);
        }
        return target;
    }
}
