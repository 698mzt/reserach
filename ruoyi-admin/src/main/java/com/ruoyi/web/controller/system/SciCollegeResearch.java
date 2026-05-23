package com.ruoyi.web.controller.system;

//学院科研工作量

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.service.IStatisticService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collegeresearch")
public class SciCollegeResearch extends BaseController {

    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private com.ruoyi.system.service.ISysUserService userService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:kygzlXY:view")
    @GetMapping
    public String index() { return prefix + "/kygzlXY"; }

    /**
     * 获取学院parentId用于数据过滤
     * 从数据库重新查询用户角色，确保获取完整的角色信息
     */
    private Long getCollegeParentId() {
        SysUser currentUser = getSysUser();
        if (currentUser == null) return null;
        
        // 从数据库重新查询用户及其角色
        SysUser dbUser = userService.selectUserById(currentUser.getUserId());
        List<SysRole> roles = (dbUser != null) ? dbUser.getRoles() : currentUser.getRoles();
        
        if (roles != null) {
            for (SysRole role : roles) {
                long rid = role.getRoleId();
                if ((rid >= 103L && rid <= 108L) || (rid >= 116L && rid <= 120L)) {
                    return (dbUser != null) ? dbUser.getDeptId() : currentUser.getDeptId();
                }
            }
        }
        return null;
    }

    @RequiresPermissions("statistic:kygzlXY:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String pname, String dname) {
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        List<String> dictValues = dictList.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        startPage();
        Long collegeParentId = getCollegeParentId();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, pname, dname, null, collegeParentId);
        TableDataInfo data = getDataTable(list);
        return data;
    }

    @RequiresPermissions("statistic:kygzlXY:view")
    @Log(title = "导出学院科研工作量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(String pname, String dname) {
        List<SysDictData> dictList = DictUtils.getDictCache("sys_acade_dept");
        List<String> dictValues = dictList.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        Long collegeParentId = getCollegeParentId();
        List<ResearchWorkloadByJYS> list = statisticService.selectAllDept(dictValues, pname, dname, null, collegeParentId);
        ExcelUtil<ResearchWorkloadByJYS> util = new ExcelUtil<ResearchWorkloadByJYS>(ResearchWorkloadByJYS.class);
        return util.exportExcel(list, "学院科研工作量");
    }
}
