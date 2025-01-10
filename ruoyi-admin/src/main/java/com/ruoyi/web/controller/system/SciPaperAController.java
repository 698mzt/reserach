package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 论文Controller
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
@Controller
@RequestMapping("/system/paper")
public class SciPaperAController extends BaseController
{
    private String prefix = "system/paper";

    @Autowired
    private ISciPaperAService sciPaperAService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:paper:view")
    @GetMapping()
    public String paper()
    {
        return prefix + "/paper";
    }

    /**
     * 查询论文列表
     */
    @RequiresPermissions("system:paper:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciPaperA sciPaperA,String year)
    {
        sciPaperA.setYear(year);
        startPage();
        List<SciPaperA> list = sciPaperAService.selectSciPaperAList(sciPaperA);
        return getDataTable(list);
    }

    /**
     * 导出论文列表
     */
    @RequiresPermissions("system:paper:export")
    @Log(title = "论文", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciPaperA sciPaperA)
    {
        List<SciPaperA> list = sciPaperAService.selectSciPaperAList(sciPaperA);
        ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
        return util.exportExcel(list, "论文数据");
    }

    /**
     * 新增论文
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();

        SysUser sysUser=null;
        Long user_id = getUserId();

        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId() == user_id){
                sysUser=userList.get(a);
                break;
            }
        }

        mmap.put("user",sysUser);

        return prefix + "/add";
    }

    /**
     * 新增保存论文
     */
    @RequiresPermissions("system:paper:add")
    @Log(title = "论文", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciPaperA sciPaperA)
    {
        System.out.println("sciPaperA = " + sciPaperA);
        return toAjax(sciPaperAService.insertSciPaperA(sciPaperA));
    }

    /**
     * 修改论文
     */
    @RequiresPermissions("system:paper:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {

        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/edit";
    }

    /**
     * 修改保存论文
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciPaperA sciPaperA)
    {
        return toAjax(sciPaperAService.updateSciPaperA(sciPaperA));
    }

    /**
     * 删除论文
     */
    @RequiresPermissions("system:paper:remove")
    @Log(title = "论文", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciPaperAService.deleteSciPaperAByIds(ids));
    }
}
