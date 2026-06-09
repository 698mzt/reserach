package com.ruoyi.web.controller.system;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CookieUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.IResearchDashboardService;

/**
 * 首页 业务处理
 *
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IResearchDashboardService researchDashboardService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // 取身份信息
        SysUser user = getSysUser();

        // 先获取用户全角色列表，复用于活跃角色判定和角色切换下拉框
        List<SysRole> allRoles = roleService.selectRolesByUserIdExcludingDataScope(user.getUserId());

        // 根据活动角色获取菜单（角色感知菜单查询）
        List<SysMenu> menus;
        Object activeRoleId = ShiroUtils.getSubject().getSession(false).getAttribute("activeRoleId");
        if (activeRoleId != null) {
            // 已切换角色 → 从 allRoles 列表中提取活跃角色，避免单独查询数据库
            SysRole activeRole = allRoles.stream()
                    .filter(r -> activeRoleId.equals(r.getRoleId()))
                    .findFirst()
                    .orElse(null);
            if (activeRole != null && activeRole.isAdmin()) {
                // 管理员角色 → 返回全部菜单，再过滤统计子页面
                menus = menuService.selectMenuNormalAll();
                filterAdminStatisticMenus(menus);
            } else {
                // 普通角色 → 查询该角色的菜单
                menus = menuService.selectMenusByRoleId(Long.valueOf(activeRoleId.toString()));
            }
        } else {
            // 未切换 → 使用原逻辑（用户ID查询）
            menus = menuService.selectMenusByUser(user);
            if (user.isAdmin()) {
                filterAdminStatisticMenus(menus);
            }
        }

        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        Boolean footer = Convert.toBool(configService.selectConfigByKey("sys.index.footer"), true);
        Boolean tagsView = Convert.toBool(configService.selectConfigByKey("sys.index.tagsView"), true);
        mmap.put("footer", footer);
        mmap.put("tagsView", tagsView);
        mmap.put("mainClass", contentMainClass(footer, tagsView));
        mmap.put("copyrightYear", RuoYiConfig.getCopyrightYear());
        mmap.put("demoEnabled", RuoYiConfig.isDemoEnabled());
        mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        mmap.put("isMobile", ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")));

        // 内嵌角色列表 JSON，避免前端异步请求竞态
        // 单角色用户不显示切换下拉，传空数组；多角色才传角色列表
        List<SysRole> activeRoles = allRoles.stream()
                .filter(r -> "0".equals(r.getStatus()))
                .collect(Collectors.toList());
        if (activeRoles.size() <= 1) {
            // 单角色或无角色 → 不显示切换下拉
            mmap.put("roleListJson", "[]");
        } else {
            // 多角色 → 显示切换下拉
            mmap.put("roleListJson", JSON.toJSONString(activeRoles));
        }

        // 当前活动角色 ID
        mmap.put("activeRoleId", activeRoleId);

        // 菜单导航显示风格
        String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
        // 移动端，默认使左侧导航菜单，否则取默认配置
        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

        // 优先Cookie配置导航菜单
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        for (Cookie cookie : cookies)
        {
            if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName()))
            {
                indexStyle = cookie.getValue();
                break;
            }
        }
        String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
        return webIndex;
    }

    // 锁定屏幕
    @GetMapping("/lockscreen")
    public String lockscreen(ModelMap mmap)
    {
        mmap.put("user", getSysUser());
        ServletUtils.getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
        return "lock";
    }

    // 解锁屏幕
    @PostMapping("/unlockscreen")
    @ResponseBody
    public AjaxResult unlockscreen(String password)
    {
        SysUser user = getSysUser();
        if (StringUtils.isNull(user))
        {
            return AjaxResult.error("服务器超时，请重新登录");
        }
        if (passwordService.matches(user, password))
        {
            ServletUtils.getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
            return AjaxResult.success();
        }
        return AjaxResult.error("密码不正确，请重新输入。");
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin()
    {
        return "skin";
    }

    // 切换菜单
    @GetMapping("/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response)
    {
        CookieUtils.setCookie(response, "nav-style", style);
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", RuoYiConfig.getVersion());
        // 取身份信息
        SysUser user = getSysUser();
        mmap.put("user", user);
        //首页设置
        putMainRoleSwitchData(mmap, user);
        return "main_research";
        //return "main_v2";
    }

    @GetMapping("/system/main/dashboard")
    @ResponseBody
    public AjaxResult dashboard(@RequestParam(value = "year", required = false) String year)
    {
        return AjaxResult.success(researchDashboardService.getDashboard(ShiroUtils.getSysUser(), year));
    }

    // content-main class
    public String contentMainClass(Boolean footer, Boolean tagsView)
    {
        if (!footer && !tagsView)
        {
            return "tagsview-footer-hide";
        }
        else if (!footer)
        {
            return "footer-hide";
        }
        else if (!tagsView)
        {
            return "tagsview-hide";
        }
        return StringUtils.EMPTY;
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate)
    {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate)
    {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0)
        {
            if (StringUtils.isNull(pwdUpdateDate))
            {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 管理员角色过滤：统计菜单下只保留科研处相关页面
     */
    private void filterAdminStatisticMenus(List<SysMenu> menus)
    {
        if (menus == null) return;
        for (SysMenu menu : menus)
        {
            if (menu.getMenuId() == 2250L && menu.getChildren() != null)
            {
                // 统计父菜单下，只保留5个页面
                Set<Long> keepIds = new HashSet<>(Arrays.asList(
                    2315L, 2320L, 2376L, 2381L, 2392L
                ));
                menu.getChildren().removeIf(child -> !keepIds.contains(child.getMenuId()));
            }
            if (menu.getChildren() != null && !menu.getChildren().isEmpty())
            {
                filterAdminStatisticMenus(menu.getChildren());
            }
        }
    }

    private void putMainRoleSwitchData(ModelMap mmap, SysUser user)
    {
        List<SysRole> allRoles = roleService.selectRolesByUserIdExcludingDataScope(user.getUserId());
        List<SysRole> activeRoles = allRoles.stream()
                .filter(r -> "0".equals(r.getStatus()))
                .collect(Collectors.toList());
        Object activeRoleId = ShiroUtils.getSubject().getSession(false).getAttribute("activeRoleId");
        mmap.put("roleListJson", activeRoles.size() <= 1 ? "[]" : JSON.toJSONString(activeRoles));
        mmap.put("activeRoleId", activeRoleId);
        mmap.put("activeRoleName", getActiveRoleName(activeRoles, activeRoleId));
    }

    private String getActiveRoleName(List<SysRole> activeRoles, Object activeRoleId)
    {
        if (activeRoles == null || activeRoles.isEmpty())
        {
            return "";
        }
        if (activeRoleId != null)
        {
            for (SysRole role : activeRoles)
            {
                if (role.getRoleId() != null && role.getRoleId().toString().equals(activeRoleId.toString()))
                {
                    return role.getRoleName();
                }
            }
        }
        return activeRoles.get(0).getRoleName();
    }
}
