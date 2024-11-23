package com.ruoyi.system.controller;

import java.util.List;
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
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 专利软著Controller
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
@Controller
@RequestMapping("/system/zhuanliruanzhu")
public class SciZhuanliruanzhuController extends BaseController
{
    private String prefix = "system/zhuanliruanzhu";

    @Autowired
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;

    @RequiresPermissions("system:zhuanliruanzhu:view")
    @GetMapping()
    public String zhuanliruanzhu()
    {
        return prefix + "/zhuanliruanzhu";
    }

    /**
     * 查询专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        startPage();
        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
        return getDataTable(list);
    }

    /**
     * 导出专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:export")
    @Log(title = "专利软著", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
        ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
        return util.exportExcel(list, "专利软著数据");
    }

    /**
     * 新增专利软著
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:add")
    @Log(title = "专利软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.insertSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 修改专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @Log(title = "专利软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.updateSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 删除专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:remove")
    @Log(title = "专利软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciZhuanliruanzhuService.deleteSciZhuanliruanzhuByIds(ids));
    }
}
