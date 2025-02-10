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
import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.service.ISciProjectScoreCfgService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 横向课题得分配置Controller
 *
 * @author ruoyi
 * @date 2024-09-30
 */
@Controller
@RequestMapping("/system/projectScoreCfg")
public class SciProjectScoreCfgController extends BaseController {
    private String prefix = "system/projectScoreCfg";

    @Autowired
    private ISciProjectScoreCfgService sciProjectScoreCfgService;

    @RequiresPermissions("system:projectScoreCfg:view")
    @GetMapping()
    public String projectScoreCfg() {
        return prefix + "/projectScoreCfg";
    }


    /**
     * 查询横向课题得分配置列表
     */
    @PostMapping("/getCfgCard")
    @ResponseBody
    public Map<String, Object> getCfgCard(SciProjectScoreCfg sciProjectScoreCfg) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0");
        resultMap.put("msg", "操作成功");
        Map<String, Object> cfgMap = sciProjectScoreCfgService.getProjectScoreCfg();
        resultMap.put("data", cfgMap);
        System.out.println("resultMap = " + resultMap);
        return resultMap;
    }

    /**
     * 查询横向课题得分配置列表
     */
    @RequiresPermissions("system:projectScoreCfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciProjectScoreCfg sciProjectScoreCfg) {
        startPage();
        List<SciProjectScoreCfg> list = sciProjectScoreCfgService.selectSciProjectScoreCfgList(sciProjectScoreCfg);
        return getDataTable(list);
    }

    /**
     * 导出横向课题得分配置列表
     */
    @RequiresPermissions("system:projectScoreCfg:export")
    @Log(title = "横向课题得分配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciProjectScoreCfg sciProjectScoreCfg) {
        List<SciProjectScoreCfg> list = sciProjectScoreCfgService.selectSciProjectScoreCfgList(sciProjectScoreCfg);
        ExcelUtil<SciProjectScoreCfg> util = new ExcelUtil<SciProjectScoreCfg>(SciProjectScoreCfg.class);
        return util.exportExcel(list, "横向课题得分配置数据");
    }

    /**
     * 新增横向课题得分配置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存横向课题得分配置
     */
    @RequiresPermissions("system:projectScoreCfg:add")
    @Log(title = "横向课题得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("userOrder") String userOrderStr
            , @RequestParam("totalScore") String totalStoreStr
            , @RequestParam("startScore") String startScoreStr
            , @RequestParam("endScore") String endScoreStr
            , @RequestParam String fundsMax
            , @RequestParam String fundsMin
            , @RequestParam String projectType
    ) {
        String[] userOrder = userOrderStr.split(",");
        String[] totalScore = totalStoreStr.split(",");
        String[] startScore = startScoreStr.split(",");
        String[] endScore = endScoreStr.split(",");
//        String fundsMax = map.get("fundsMax") + "";
//        String fundsMin = map.get("fundsMin") + "";
//        String projectType = map.get("projectType") + "";
        for (int i = 0; i < userOrder.length; i++) {
            SciProjectScoreCfg sciProjectScoreCfg = new SciProjectScoreCfg();
            sciProjectScoreCfg.setProjectType(projectType);
            sciProjectScoreCfg.setFundsMax(fundsMax);
            sciProjectScoreCfg.setFundsMin(fundsMin);
            sciProjectScoreCfg.setUserOrder(userOrder[i] + "");
            sciProjectScoreCfg.setTotalScore(totalScore[i] + "");
            sciProjectScoreCfg.setStartScore(startScore[i] + "");
            sciProjectScoreCfg.setEndScore(endScore[i] + "");
            sciProjectScoreCfg.setUpdateUser(ShiroUtils.getUserId() + "");
            sciProjectScoreCfgService.insertSciProjectScoreCfg(sciProjectScoreCfg);
        }

        return toAjax(1);
    }

    /**
     * 修改横向课题得分配置
     */
    @RequiresPermissions("system:projectScoreCfg:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SciProjectScoreCfg sciProjectScoreCfg = sciProjectScoreCfgService.selectSciProjectScoreCfgById(id);
        mmap.put("sciProjectScoreCfg", sciProjectScoreCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存横向课题得分配置
     */
    @RequiresPermissions("system:projectScoreCfg:edit")
    @Log(title = "横向课题得分配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciProjectScoreCfg sciProjectScoreCfg) {
        return toAjax(sciProjectScoreCfgService.updateSciProjectScoreCfg(sciProjectScoreCfg));
    }

    @RequiresPermissions("system:projectScoreCfg:add")
    @Log(title = "横向课题得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/editCfg")
    @ResponseBody
    public Map<String, Object> editCfg(@RequestBody  Map map) {
        System.out.println("map = " + map);

        int i = sciProjectScoreCfgService.deleteSciProjectScoreCfgByFunds(map);
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
     * 删除横向课题得分配置
     */
    @RequiresPermissions("system:projectScoreCfg:remove")
    @Log(title = "横向课题得分配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sciProjectScoreCfgService.deleteSciProjectScoreCfgByIds(ids));
    }

    @RequiresPermissions("system:projectScoreCfg:add")
    @Log(title = "横向课题得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/delCfg")
    @ResponseBody
    public Map<String, Object> delCfg(@RequestBody  Map map) {
        System.out.println("map = " + map);

        int i = sciProjectScoreCfgService.deleteSciProjectScoreCfgByFunds(map);
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
