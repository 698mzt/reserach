package com.ruoyi.web.controller.system;

//<!--教研室业绩成果数-->

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.system.service.IStatisticYJCGSService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/statisticJZ")
public class StatisticJiaoZong extends BaseController {

    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;
    @Autowired
    private ISysUserService userService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:achievement:view")
    @GetMapping()
    public String apply(Model model) {
        SysUser currentUser = getSysUser();
        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();

        boolean hasAdmin = false;
        boolean hasSciResearch = false;
        boolean hasCollege = false;
        boolean hasResearchDept = false;
        boolean hasTeacher = false;

        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long rid = role.getRoleId();
                if (rid == 1L) hasAdmin = true;
                if (rid == 101L) hasSciResearch = true;
                if ((rid >= 103L && rid <= 108L) || (rid >= 116L && rid <= 120L)) hasCollege = true;
                if (rid == 102L) hasResearchDept = true;
                if (rid == 100L) hasTeacher = true;
            }
        }

        if (hasAdmin) return "redirect:/rewardxuexiao";
        if (hasSciResearch) return "redirect:/rewardkeyanchu";
        if (hasCollege) return "redirect:/rewardxueyuan";

        boolean isPureTeacher = hasTeacher && !hasResearchDept;
        model.addAttribute("modalName", isPureTeacher ? "我的业绩成果数" : "教研室业绩成果数");
        return prefix + "/yjcgJYS";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/listJZ")
    @ResponseBody
    public TableDataInfo listJZ() {
        SysUser user = getSysUser();
        // 重新查询用户角色
        SysUser dbUser = userService.selectUserById(user.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : user.getRoles();

        boolean isAdminOrResearch = false;
        boolean hasTeacher = false;
        boolean hasResearchDept = false;
        if (roles != null) {
            for (SysRole role : roles) {
                if (role == null || role.getRoleId() == null) continue;
                long rid = role.getRoleId();
                if (rid == 1L || rid == 101L) { isAdminOrResearch = true; }
                if (rid == 100L) { hasTeacher = true; }
                if (rid == 102L) { hasResearchDept = true; }
            }
        }

        String deptId = isAdminOrResearch ? null : user.getDeptId().toString();
        Long userId = (hasTeacher && !hasResearchDept) ? user.getUserId() : null;
        
        // 先查询所有数据（不分页），用于计算正确的总数和获取完整数据
        List<Map<String, Object>> allData = statisticYJCGSService.selectYJCGSJYS(deptId, userId);
        
        // 获取实际数据行数（不含总计行）
        int totalCount = allData.size() > 0 && "总计".equals(allData.get(allData.size() - 1).get("userName")) 
                ? allData.size() - 1 
                : allData.size();
        
        // 获取分页参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        int pageNum = pageDomain.getPageNum() > 0 ? pageDomain.getPageNum() : 1;
        int pageSize = pageDomain.getPageSize() > 0 ? pageDomain.getPageSize() : 10;
        
        // 手动计算分页
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);
        
        // 提取当前页数据（不含总计行）
        List<Map<String, Object>> pageData = new ArrayList<>();
        if (startIndex < totalCount) {
            pageData = allData.subList(startIndex, endIndex);
        }
        
        // 如果当前页有数据，并且存在总计行，将总计行添加到当前页末尾
        if (!pageData.isEmpty() && allData.size() > 0 && "总计".equals(allData.get(allData.size() - 1).get("userName"))) {
            pageData = new ArrayList<>(pageData); // 转换为新列表
            pageData.add(allData.get(allData.size() - 1)); // 添加总计行
        }
        
        // 构建分页后的结果
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(pageData);
        rspData.setTotal(totalCount);
        
        return rspData;
    }
}
