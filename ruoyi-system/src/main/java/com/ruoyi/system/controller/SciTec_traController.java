package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.domain.SciTec_traScoreCfg;

import com.ruoyi.system.service.ISciTec_traCfgService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成果转化 得分配置Controller
 *
 * @author tqj
 * @date 2025-03-06
 */
@Controller
@RequestMapping("/system/tec_tra")
public class SciTec_traController extends BaseController {
    private String prefix = "system/tec_tra";

    @Autowired
    private ISciTec_traCfgService sciTec_traCfgService;

    @RequiresPermissions("system:tec_tra:view")
    @GetMapping("")
    public String view() {
        System.out.println("SciTec_traController.view");
        return prefix + "/view";
    }

    /**
     * 查询成果转化得分配置列表
     */
    @RequiresPermissions("system:tec_tra:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciTec_traScoreCfg sciTec_traScoreCfg) {
        startPage();
        List<SciProjectScoreCfg> list = sciTec_traCfgService.selectSciTec_traScoreCfgList(sciTec_traScoreCfg);
        return getDataTable(list);
    }

    /**
     * 查询 成果转化 得分配置列表
     */
    @PostMapping("/getCfgCard")
    @ResponseBody
    public Map<String, Object> getCfgCard(SciTec_traScoreCfg sciTec_traScoreCfg) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0");
        resultMap.put("msg", "操作成功");
        Map<String, Object> cfgMap = sciTec_traCfgService.getTec_traScoreCfg();
        resultMap.put("data", cfgMap);
        System.out.println("resultMap = " + resultMap);
        return resultMap;
    }


    /**
     * 新增 成果转化 得分配置
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    @RequiresPermissions("system:tec_tra:add")
    @Log(title = "横向课题得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("userOrder") String userOrderStr
            , @RequestParam("totalScore") String totalStoreStr
            , @RequestParam("startScore") String startScoreStr
            , @RequestParam("endScore") String endScoreStr
            , @RequestParam String fundsMax
            , @RequestParam String fundsMin
            //, @RequestParam String projectType
    ) {
        System.out.println("SciTec_traController.addSave");
        String[] userOrder = userOrderStr.split(",");
        String[] totalScore = totalStoreStr.split(",");
        String[] startScore = startScoreStr.split(",");
        String[] endScore = endScoreStr.split(",");
//        String fundsMax = map.get("fundsMax") + "";
//        String fundsMin = map.get("fundsMin") + "";
//        String projectType = map.get("projectType") + "";
        for (int i = 0; i < userOrder.length; i++) {
            SciTec_traScoreCfg sciTec_traScoreCfg =new SciTec_traScoreCfg();
            sciTec_traScoreCfg.setFunds_max(fundsMax);
            sciTec_traScoreCfg.setFunds_min(fundsMin);
            sciTec_traScoreCfg.setUser_order(userOrder[i] + "");
            sciTec_traScoreCfg.setTotal_score(totalScore[i] + "");
            sciTec_traScoreCfg.setStart_score(startScore[i] + "");
            sciTec_traScoreCfg.setEnd_score(endScore[i] + "");
            sciTec_traScoreCfg.setUpdate_user(ShiroUtils.getUserId() + "");
            sciTec_traCfgService.insertTec_traScoreCfg(sciTec_traScoreCfg);
        }

        return toAjax(1);
    }

    @RequiresPermissions("system:tec_tra:add")
    @Log(title = "成果转化得分配置", businessType = BusinessType.INSERT)
    @PostMapping("/delCfg")
    @ResponseBody
    public Map<String, Object> delCfg(@RequestBody  Map map) {
        System.out.println("map = " + map);

        int i = sciTec_traCfgService.deleteSciTec_traScoreCfgByFunds(map);
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
