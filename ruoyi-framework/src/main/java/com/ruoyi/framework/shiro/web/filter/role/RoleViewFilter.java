package com.ruoyi.framework.shiro.web.filter.role;

import com.ruoyi.common.core.context.RoleViewContextHolder;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 角色视角过滤器
 * 从请求头读取角色视角标识，设置到ThreadLocal中
 * 不修改Shiro缓存，不修改底层拦截器，仅在当前请求生效
 *
 * @author ruoyi
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RoleViewFilter extends OncePerRequestFilter {

    private static final String ROLE_VIEW_HEADER = "X-Role-View";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String roleView = request.getHeader(ROLE_VIEW_HEADER);

            if (StringUtils.isNotEmpty(roleView)) {
                RoleViewContextHolder.setRoleView(roleView);
            }

            filterChain.doFilter(request, response);
        } finally {
            RoleViewContextHolder.clear();
        }
    }
}
