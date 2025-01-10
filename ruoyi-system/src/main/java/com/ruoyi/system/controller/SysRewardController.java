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
import com.ruoyi.system.domain.SysReward;
import com.ruoyi.system.service.ISysRewardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 奖励Controller
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
@Controller
@RequestMapping("/system/reward")
public class SysRewardController extends BaseController
{
    private String prefix = "system/reward";

    @Autowired
    private ISysRewardService sysRewardService;

    @RequiresPermissions("system:reward:view")
    @GetMapping()
    public String reward()
    {
        return prefix + "/reward";
    }

    /**
     * 查询奖励列表
     */
    @RequiresPermissions("system:reward:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysReward sysReward)
    {
        startPage();
        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);
        return getDataTable(list);
    }

    /**
     * 导出奖励列表
     */
    @RequiresPermissions("system:reward:export")
    @Log(title = "奖励", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysReward sysReward)
    {
        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);
        ExcelUtil<SysReward> util = new ExcelUtil<SysReward>(SysReward.class);
        return util.exportExcel(list, "奖励数据");
    }

    /**
     * 新增奖励
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存奖励
     */
    @RequiresPermissions("system:reward:add")
    @Log(title = "奖励", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysReward sysReward)
    {
        return toAjax(sysRewardService.insertSysReward(sysReward));
    }

    /**
     * 修改奖励
     */
    @RequiresPermissions("system:reward:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(id);
        mmap.put("sysReward", sysReward);
        return prefix + "/edit";
    }

    /**
     * 修改保存奖励
     */
    @RequiresPermissions("system:reward:edit")
    @Log(title = "奖励", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysReward sysReward)
    {
        return toAjax(sysRewardService.updateSysReward(sysReward));
    }

    /**
     * 删除奖励
     */
    @RequiresPermissions("system:reward:remove")
    @Log(title = "奖励", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysRewardService.deleteSysRewardByIds(ids));
    }
}
