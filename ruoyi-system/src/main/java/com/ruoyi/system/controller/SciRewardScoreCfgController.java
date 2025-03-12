package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SciRewardScoreCfg;
import com.ruoyi.system.service.ISciRewardScoreCfgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 奖励积分管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-24
 */
@Controller
@RequestMapping("/system/rewardcfg")
public class SciRewardScoreCfgController extends BaseController
{
    private String prefix = "system/rewardcfg";

    @Autowired
    private ISciRewardScoreCfgService sciRewardScoreCfgService;

    @RequiresPermissions("system:rewardcfg:view")
    @GetMapping()
    public String rewardcfg()
    {
        return prefix + "/rewardcfg";
    }

    /**
     * 查询奖励积分管理列表
     */
    @RequiresPermissions("system:rewardcfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciRewardScoreCfg sciRewardScoreCfg)
    {
        startPage();
        List<SciRewardScoreCfg> list = sciRewardScoreCfgService.selectSciRewardScoreCfgList(sciRewardScoreCfg);
        return getDataTable(list);
    }

    /**
     * 导出奖励积分管理列表
     */
    @RequiresPermissions("system:rewardcfg:export")
    @Log(title = "奖励积分管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciRewardScoreCfg sciRewardScoreCfg)
    {
        List<SciRewardScoreCfg> list = sciRewardScoreCfgService.selectSciRewardScoreCfgList(sciRewardScoreCfg);
        ExcelUtil<SciRewardScoreCfg> util = new ExcelUtil<SciRewardScoreCfg>(SciRewardScoreCfg.class);
        return util.exportExcel(list, "奖励积分管理数据");
    }

    /**
     * 新增奖励积分管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存奖励积分管理
     */
    @RequiresPermissions("system:rewardcfg:add")
    @Log(title = "奖励积分管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciRewardScoreCfg sciRewardScoreCfg)
    {
        return toAjax(sciRewardScoreCfgService.insertSciRewardScoreCfg(sciRewardScoreCfg));
    }

    /**
     * 修改奖励积分管理
     */
    @RequiresPermissions("system:rewardcfg:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SciRewardScoreCfg sciRewardScoreCfg = sciRewardScoreCfgService.selectSciRewardScoreCfgById(id);
        mmap.put("sciRewardScoreCfg", sciRewardScoreCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存奖励积分管理
     */
    @RequiresPermissions("system:rewardcfg:edit")
    @Log(title = "奖励积分管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciRewardScoreCfg sciRewardScoreCfg)
    {
        return toAjax(sciRewardScoreCfgService.updateSciRewardScoreCfg(sciRewardScoreCfg));
    }

    /**
     * 删除奖励积分管理
     */
    @RequiresPermissions("system:rewardcfg:remove")
    @Log(title = "奖励积分管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciRewardScoreCfgService.deleteSciRewardScoreCfgByIds(ids));
    }
}
