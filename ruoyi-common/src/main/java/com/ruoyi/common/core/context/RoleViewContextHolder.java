package com.ruoyi.common.core.context;

/**
 * 角色视角上下文持有者
 * 使用ThreadLocal存储当前请求的角色视角标识
 *
 * @author ruoyi
 */
public class RoleViewContextHolder {

    private static final ThreadLocal<String> ROLE_VIEW_CONTEXT = new ThreadLocal<>();

    public static void setRoleView(String role) {
        ROLE_VIEW_CONTEXT.set(role);
    }

    public static String getRoleView() {
        return ROLE_VIEW_CONTEXT.get();
    }

    public static boolean hasRoleView() {
        String role = ROLE_VIEW_CONTEXT.get();
        return role != null && !"all".equals(role);
    }

    public static void clear() {
        ROLE_VIEW_CONTEXT.remove();
    }
}
