package com.ruoyi.web.controller.system;

//<!--教研室业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.system.service.IStatisticYJCGSService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.domain.AjaxResult;
import java.util.*;
import java.util.Map;

@Controller
@RequestMapping("/statisticJZ")
public class StatisticJiaoZong extends BaseController {

    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:achievement:view")
    @GetMapping()
    public String apply(Model model) {
        return applyWithUser(getSysUser(), model);
    }

    String applyWithUser(SysUser user, Model model) {
        List<SysRole> roles = user.getRoles();

        if (roles != null && !roles.isEmpty()) {
            Long roleId = roles.get(0).getRoleId();
            if (roleId == 1L)
                return "redirect:/rewardxuexiao";
            if (roleId == 101L)
                return "redirect:/rewardkeyanchu";
            if ((roleId >= 103L && roleId <= 108L) || (roleId >= 116L && roleId <= 120L))
                return "redirect:/rewardxueyuan";
            if (roleId == 102L) {
                model.addAttribute("modalName", "教研室业绩成果数");
                model.addAttribute("searchMode", "teacherName");
                // 传递当前用户信息到模板，用于空数据占位行显示
                model.addAttribute("user", user);
                return prefix + "/yjcgJYS";
            }
        }

        model.addAttribute("modalName", "我的业绩成果数");
        model.addAttribute("searchMode", "none");
        // 传递当前用户信息到模板，用于空数据占位行显示用户名和部门
        model.addAttribute("user", user);
        return prefix + "/yjcgJYS";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/listJZ")
    @ResponseBody
    public TableDataInfo listJZ(String userName) {
        SysUser user = getSysUser();
        ScopeResult scope = resolveScope(user);

        List<Map<String, Object>> allData = statisticYJCGSService.selectYJCGSJYS(scope.getDeptId(), scope.getUserId());

        if (userName != null && !userName.trim().isEmpty()) {
            List<Map<String, Object>> filteredData = new ArrayList<>();
            for (Map<String, Object> row : allData) {
                if ("总计".equals(row.get("userName")))
                    continue;
                String rowName = (String) row.get("userName");
                if (rowName != null && rowName.contains(userName.trim())) {
                    filteredData.add(row);
                }
            }
            if (!allData.isEmpty() && "总计".equals(allData.get(allData.size() - 1).get("userName"))) {
                filteredData.add(allData.get(allData.size() - 1));
            }
            allData = filteredData;
        }

        int totalCount = allData.size() > 0 && "总计".equals(allData.get(allData.size() - 1).get("userName"))
                ? allData.size() - 1
                : allData.size();

        PageDomain pageDomain = TableSupport.buildPageRequest();
        int pageNum = pageDomain.getPageNum() > 0 ? pageDomain.getPageNum() : 1;
        int pageSize = pageDomain.getPageSize() > 0 ? pageDomain.getPageSize() : 10;

        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);

        List<Map<String, Object>> pageData = new ArrayList<>();
        if (startIndex < totalCount) {
            pageData = allData.subList(startIndex, endIndex);
        }

        if (!pageData.isEmpty() && allData.size() > 0 && "总计".equals(allData.get(allData.size() - 1).get("userName"))) {
            pageData = new ArrayList<>(pageData);
            pageData.add(allData.get(allData.size() - 1));
        }

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(pageData);
        rspData.setTotal(totalCount);

        return rspData;
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String userName) {
        SysUser user = getSysUser();
        ScopeResult scope = resolveScope(user);

        List<Map<String, Object>> allData = statisticYJCGSService.selectYJCGSJYS(scope.getDeptId(), scope.getUserId());

        String[] headers = { "专业", "教师", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作", "专利", "软著",
                "奖励", "学术报告(讲座类)" };
        String[] fieldKeys = { "deptName", "userName", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作",
                "专利", "软著", "奖励", "学术报告(讲座类)" };

        return MapDataExcelUtil.exportExcel(allData, headers, fieldKeys, "教研室业绩成果数");
    }

    ScopeResult resolveScope(SysUser user) {
        List<SysRole> roles = user.getRoles();

        if (roles != null && !roles.isEmpty()) {
            Long roleId = roles.get(0).getRoleId();
            if (roleId == 1L || roleId == 101L) {
                return new ScopeResult(null, null);
            }
            if (roleId == 100L) {
                return new ScopeResult(user.getDeptId() != null ? user.getDeptId().toString() : null, user.getUserId());
            }
        }

        return new ScopeResult(user.getDeptId() != null ? user.getDeptId().toString() : null, null);
    }

    static class ScopeResult {
        private final String deptId;
        private final Long userId;

        ScopeResult(String deptId, Long userId) {
            this.deptId = deptId;
            this.userId = userId;
        }

        String getDeptId() {
            return deptId;
        }

        Long getUserId() {
            return userId;
        }
    }
}
