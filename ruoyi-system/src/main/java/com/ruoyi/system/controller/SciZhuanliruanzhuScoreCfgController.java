package com.ruoyi.system.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.system.service.ISciZhuanliruanzhuScoreCfgService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 专利软著得分配置Controller
 *
 * @author ruoyi
 * @date 2024-09-30
 */
@Controller
@RequestMapping("/system/zhuanliruanzhuScoreCfg")
public class SciZhuanliruanzhuScoreCfgController extends BaseController {
    private String prefix = "system/zhuanliruanzhuScoreCfg";

    @Autowired
    private ISciZhuanliruanzhuScoreCfgService sciZhuanliruanzhuScoreCfgService;

    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:view")
    @GetMapping()
    public String zhuanliruanzhuScoreCfg() {
        return prefix + "/zhuanliruanzhuScoreCfg";
    }

    /**
     * 查询专利软著得分配置列表
     */
    @PostMapping("/getCfgCard")
    @ResponseBody
    public Map<String, Object> getCfgCard(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0");
        resultMap.put("msg", "操作成功");
        Map<String, Object> cfgMap = sciZhuanliruanzhuScoreCfgService.getZhuanliruanzhuScoreCfg();
        resultMap.put("data", cfgMap);
        System.out.println("resultMap = " + resultMap);
        return resultMap;
    }

    /**
     * 查询专利软著得分配置列表
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg) {
        startPage();
        List<SciZhuanliruanzhuScoreCfg> list = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
        return getDataTable(list);
    }

    /**
     * 导出专利软著得分配置列表
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:export")
    @Log(title = "专利软著得分配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg) {
        List<SciZhuanliruanzhuScoreCfg> list = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
        ExcelUtil<SciZhuanliruanzhuScoreCfg> util = new ExcelUtil<SciZhuanliruanzhuScoreCfg>(SciZhuanliruanzhuScoreCfg.class);
        return util.exportExcel(list, "专利软著得分配置数据");
    }

    /**
     * 新增专利软著得分配置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存专利软著得分配置
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:add")
    @Log(title = "专利软著得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("userOrder") String userOrderStr
            , @RequestParam("totalScore") String totalStoreStr

            , @RequestParam String fundsMax
            , @RequestParam String fundsMin

    ) {
        String[] userOrder = userOrderStr.split(",");
        String[] totalScore = totalStoreStr.split(",");

        for (int i = 0; i < userOrder.length; i++) {
            SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg = new SciZhuanliruanzhuScoreCfg();

            sciZhuanliruanzhuScoreCfg.setFundsMax(fundsMax);
            sciZhuanliruanzhuScoreCfg.setFundsMin(fundsMin);
            sciZhuanliruanzhuScoreCfg.setUserOrder(userOrder[i] + "");
            sciZhuanliruanzhuScoreCfg.setTotalScore(totalScore[i] + "");

            sciZhuanliruanzhuScoreCfg.setUpdateUser(ShiroUtils.getUserId() + "");
            sciZhuanliruanzhuScoreCfgService.insertSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg);
        }

        return toAjax(1);
    }

    /**
     * 修改专利软著得分配置
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg = sciZhuanliruanzhuScoreCfgService.selectSciZhuanliruanzhuScoreCfgById(id);
        mmap.put("sciZhuanliruanzhuScoreCfg", sciZhuanliruanzhuScoreCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著得分配置
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:edit")
    @Log(title = "专利软著得分配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg) {
        return toAjax(sciZhuanliruanzhuScoreCfgService.updateSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg));
    }

    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:add")
    @Log(title = "专利软著得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/editCfg")
    @ResponseBody
    public Map<String, Object> editCfg(@RequestBody Map map) {
        System.out.println("map = " + map);

        int i = sciZhuanliruanzhuScoreCfgService.deleteSciZhuanliruanzhuScoreCfgByFunds(map);
        Map<String, Object> returnMap = new HashMap<>();

        if (i > 0) {
            returnMap.put("code", "0");
            returnMap.put("msg", "操作成功");
            return returnMap;
        } else {
            returnMap.put("code", "-1");
            returnMap.put("msg", "操作失败");
            return returnMap;
        }
    }

    /**
     * 删除专利软著得分配置
     */
    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:remove")
    @Log(title = "专利软著得分配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sciZhuanliruanzhuScoreCfgService.deleteSciZhuanliruanzhuScoreCfgByIds(ids));
    }

    @RequiresPermissions("system:zhuanliruanzhuScoreCfg:add")
    @Log(title = "专利软著得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/delCfg")
    @ResponseBody
    public Map<String, Object> delCfg(@RequestBody Map map) {
        System.out.println("map = " + map);

        int i = sciZhuanliruanzhuScoreCfgService.deleteSciZhuanliruanzhuScoreCfgByFunds(map);
        Map<String, Object> returnMap = new HashMap<>();

        if (i > 0) {
            returnMap.put("code", "0");
            returnMap.put("msg", "操作成功");
            return returnMap;
        } else {
            returnMap.put("code", "-1");
            returnMap.put("msg", "操作失败");
            return returnMap;
        }
    }
}
