package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SciJiaocairuanzhuScoreCfg;
import com.ruoyi.system.service.ISciJiaocairuanzhuScoreCfgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专利软著积分管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@Controller
@RequestMapping("/system/jiaocairuanzhuCfg")
public class SciJiaocairuanzhuScoreCfgController extends BaseController
{
    private String prefix = "system/jiaocairuanzhuCfg";

    @Autowired
    private ISciJiaocairuanzhuScoreCfgService sciJiaocairuanzhuScoreCfgService;

    @RequiresPermissions("system:jiaocairuanzhuCfg:view")
    @GetMapping()
    public String jiaocairuanzhuCfg()
    {
        return prefix + "/jiaocairuanzhuCfg";
    }

    /**
     * 查询专利软著积分管理列表
     */
    @RequiresPermissions("system:jiaocairuanzhuCfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        startPage();
        List<SciJiaocairuanzhuScoreCfg> list = sciJiaocairuanzhuScoreCfgService.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);
        return getDataTable(list);
    }

    /**
     * 导出专利软著积分管理列表
     */
    @RequiresPermissions("system:jiaocairuanzhuCfg:export")
    @Log(title = "专利软著积分管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        List<SciJiaocairuanzhuScoreCfg> list = sciJiaocairuanzhuScoreCfgService.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);
        ExcelUtil<SciJiaocairuanzhuScoreCfg> util = new ExcelUtil<SciJiaocairuanzhuScoreCfg>(SciJiaocairuanzhuScoreCfg.class);
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
    @RequiresPermissions("system:jiaocairuanzhuCfg:add")
    @Log(title = "专利软著积分管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        return toAjax(sciJiaocairuanzhuScoreCfgService.insertSciJiaocairuanzhuScoreCfg(sciJiaocairuanzhuScoreCfg));
    }

    /**
     * 修改专利软著积分管理
     */
    @RequiresPermissions("system:jiaocairuanzhuCfg:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg = sciJiaocairuanzhuScoreCfgService.selectSciJiaocairuanzhuScoreCfgById(id);
        mmap.put("sciJiaocairuanzhuScoreCfg", sciJiaocairuanzhuScoreCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著积分管理
     */
    @RequiresPermissions("system:jiaocairuanzhuCfg:edit")
    @Log(title = "专利软著积分管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg)
    {
        return toAjax(sciJiaocairuanzhuScoreCfgService.updateSciJiaocairuanzhuScoreCfg(sciJiaocairuanzhuScoreCfg));
    }

    /**
     * 删除专利软著积分管理
     */
    @RequiresPermissions("system:jiaocairuanzhuCfg:remove")
    @Log(title = "专利软著积分管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciJiaocairuanzhuScoreCfgService.deleteSciJiaocairuanzhuScoreCfgByIds(ids));
    }
}
