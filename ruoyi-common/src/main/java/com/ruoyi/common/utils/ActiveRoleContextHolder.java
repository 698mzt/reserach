package com.ruoyi.common.utils;

/**
 * 活动角色上下文持有者（请求级别，ThreadLocal）
 * 由 RoleContextFilter 设置，由 AuthInfoAspect 读取
 */
public class ActiveRoleContextHolder {

    private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();

    public static void set(Long roleId) {
        CONTEXT.set(roleId);
    }

    public static Long get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
