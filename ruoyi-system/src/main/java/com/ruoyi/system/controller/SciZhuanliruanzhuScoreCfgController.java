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
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.system.service.ISciZhuanliruanzhuScoreCfgService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 专利软著积分管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@Controller
@RequestMapping("/system/zhuanliruanzhuCfg")
public class SciZhuanliruanzhuScoreCfgController extends BaseController
{
    private String prefix = "system/zhuanliruanzhuCfg";

    @Autowired
    private ISciZhuanliruanzhuScoreCfgService sciZhuanliruanzhuScoreCfgService;

    @RequiresPermissions("system:zhuanliruanzhuCfg:view")
    @GetMapping()
    public String zhuanliruanzhuCfg()
    {
        return prefix + "/zhuanliruanzhuCfg";
    }

    /**
     * 查询专利软著积分管理列表
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        startPage();
        List<SciZhuanliruanzhuScoreCfg> list = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
        return getDataTable(list);
    }

    /**
     * 导出专利软著积分管理列表
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:export")
    @Log(title = "专利软著积分管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        List<SciZhuanliruanzhuScoreCfg> list = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
        ExcelUtil<SciZhuanliruanzhuScoreCfg> util = new ExcelUtil<SciZhuanliruanzhuScoreCfg>(SciZhuanliruanzhuScoreCfg.class);
        return util.exportExcel(list, "专利软著积分管理数据");
    }

    /**
     * 新增专利软著积分管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存专利软著积分管理
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:add")
    @Log(title = "专利软著积分管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        return toAjax(sciZhuanliruanzhuScoreCfgService.insertSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg));
    }

    /**
     * 修改专利软著积分管理
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgById(id);
        mmap.put("sciZhuanliruanzhuScoreCfg", sciZhuanliruanzhuScoreCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著积分管理
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:edit")
    @Log(title = "专利软著积分管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        return toAjax(sciZhuanliruanzhuScoreCfgService.updateSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg));
    }

    /**
     * 删除专利软著积分管理
     */
    @RequiresPermissions("system:zhuanliruanzhuCfg:remove")
    @Log(title = "专利软著积分管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciZhuanliruanzhuScoreCfgService.deleteSciZhuanliruanzhuScoreCfgByIds(ids));
    }
}
