package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 审批页面渲染上下文对象，封装统一渲染方法所需的全部上下文信息
 * 避免方法参数过多且分散，作为状态和动作构造方法的统一输入
 *
 * @author ruoyi
 */
public class PageRenderContext implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 模块编码：PAPER/REWARD/SOFTWARE/HORIZONTAL/VERTICAL/INTRASCH/JIAOCAI/REPORT */
    private String moduleCode;

    /** 业务ID */
    private Long businessId;

    /** 当前状态编码 */
    private String currentState;

    /** 创建人ID */
    private Long creatorId;

    /** 当前登录用户 */
    private SysUser currentUser;

    /** 当前用户权限列表 */
    private Set<String> permissions;

    /** 当前用户角色key列表 */
    private List<String> roleKeys;

    /** 扩展业务信息 */
    private Map<String, Object> extInfo;

    /** 权限前缀（如 system:paper、system:apply），由调用方设置，用于动态拼接权限标识 */
    private String permPrefix;

    /** 流程编码（如 PAPER_APPROVAL、HORIZONTAL_APPLY），由调用方设置，用于查询审批流程配置 */
    private String processCode;

    public PageRenderContext() {
    }

    /**
     * 判断当前用户是否为业务创建人
     *
     * @return 是否为创建人
     */
    public boolean isOwner() {
        if (currentUser == null || creatorId == null) {
            return false;
        }
        return creatorId.equals(currentUser.getUserId());
    }

    /**
     * 判断当前用户是否拥有指定权限
     * 优先使用权限列表判断，若权限列表为空则使用Shiro实时判断
     * 支持通配符*:*:*匹配所有权限
     *
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    public boolean hasPermission(String permission) {
        // 如果有权限列表，优先使用列表判断
        if (permissions != null && !permissions.isEmpty()) {
            // 支持通配符*:*:*匹配所有权限
            if (permissions.contains("*:*:*")) {
                return true;
            }
            return permissions.contains(permission);
        }
        // 权限列表为空时，使用Shiro实时判断
        try {
            return org.apache.shiro.SecurityUtils.getSubject().isPermitted(permission);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断当前用户是否拥有指定角色
     *
     * @param roleKey 角色标识
     * @return 是否拥有角色
     */
    public boolean hasRole(String roleKey) {
        if (roleKeys == null || roleKey == null) {
            return false;
        }
        return roleKeys.contains(roleKey);
    }

    /**
     * 判断当前状态是否为指定状态之一
     *
     * @param states 状态编码数组
     * @return 是否匹配
     */
    public boolean isInState(String... states) {
        if (currentState == null || states == null) {
            return false;
        }
        for (String state : states) {
            if (currentState.equals(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取扩展信息中的值
     *
     * @param key 扩展信息键
     * @return 扩展信息值
     */
    public Object getExtValue(String key) {
        if (extInfo == null || key == null) {
            return null;
        }
        return extInfo.get(key);
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public SysUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SysUser currentUser) {
        this.currentUser = currentUser;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoleKeys() {
        return roleKeys;
    }

    public void setRoleKeys(List<String> roleKeys) {
        this.roleKeys = roleKeys;
    }

    public Map<String, Object> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, Object> extInfo) {
        this.extInfo = extInfo;
    }

    public String getPermPrefix() {
        return permPrefix;
    }

    public void setPermPrefix(String permPrefix) {
        this.permPrefix = permPrefix;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    @Override
    public String toString() {
        return "PageRenderContext{" +
                "moduleCode='" + moduleCode + '\'' +
                ", businessId=" + businessId +
                ", currentState='" + currentState + '\'' +
                ", creatorId=" + creatorId +
                ", currentUser=" + (currentUser != null ? currentUser.getUserId() : null) +
                ", permissions=" + permissions +
                ", roleKeys=" + roleKeys +
                ", permPrefix='" + permPrefix + '\'' +
                ", processCode='" + processCode + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
