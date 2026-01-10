package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据权限工具类
 * 用于为 Map 类型参数手动应用数据权限过滤
 * 
 * @author ruoyi
 */
public class DataScopeUtils
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE_KEY = "_dataScope";

    /**
     * 手动为 Map 类型参数应用数据权限
     * 
     * @param params 参数 Map
     * @param deptAlias 部门表别名
     * @param userAlias 用户表别名
     * @param permission 权限字符
     */
    public static void applyDataScopeToMap(Map<String, String> params, String deptAlias, String userAlias, String permission)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser == null || currentUser.isAdmin())
        {
            return; // 超级管理员不过滤数据
        }

        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        for (SysRole role : currentUser.getRoles())
        {
            String dataScope = role.getDataScope();
            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope))
            {
                continue;
            }
            if (StringUtils.isNotEmpty(permission) && StringUtils.isNotEmpty(role.getPermissions())
                    && !StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission)))
            {
                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                // 全部数据权限，清空条件
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
                // 自定数据权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            }
            else if (DATA_SCOPE_DEPT.equals(dataScope))
            {
                // 部门数据权限
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, currentUser.getDeptId()));
            }
            else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
            {
                // 部门及以下数据权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, currentUser.getDeptId(), currentUser.getDeptId()));
            }
            else if (DATA_SCOPE_SELF.equals(dataScope))
            {
                // 仅本人数据权限
                if (StringUtils.isNotBlank(userAlias))
                {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, currentUser.getUserId()));
                }
                else
                {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }

        // 多角色情况下，所有角色都不包含传递过来的权限字符，这个时候sqlString也会为空，所以要限制一下,不查询任何数据
        if (conditions.isEmpty())
        {
            sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            // 由于参数类型是 Map<String, String>，无法直接存储嵌套的 Map 结构来支持 params.dataScope
            // 我们使用 _dataScope 键来存储权限 SQL，XML 中会检查这个键
            // 注意：sqlString 以 " OR " 开头，所以从第4个字符开始截取（去掉前4个字符 " OR "）
            String dataScopeSql = " AND (" + sqlString.substring(4) + ")";
            params.put(DATA_SCOPE_KEY, dataScopeSql);
        }
    }
}

