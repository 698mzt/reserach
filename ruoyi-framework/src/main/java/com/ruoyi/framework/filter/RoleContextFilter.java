package com.ruoyi.framework.filter;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ActiveRoleContextHolder;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * 角色上下文过滤器（Shiro AccessControlFilter）
 * 若用户已切换活动角色，在请求生命周期内用 runAs 切换 Principal
 * 使 ShiroUtils.getSysUser().getRoles() 只返回活动角色
 *
 * 注意：继承 AccessControlFilter，由 ShiroConfig 注册到 Shiro 过滤器链
 */
public class RoleContextFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, 
            ServletResponse response, Object mappedValue) {
        
        Subject subject = getSubject(request, response);
        Session session = subject.getSession(false);

        if (session != null) {
            Long activeRoleId = (Long) session.getAttribute("activeRoleId");

            if (activeRoleId != null) {
                SysUser fullUser = ShiroUtils.getSysUser();
                if (fullUser == null) {
                    return true;
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
