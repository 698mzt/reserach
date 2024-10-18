package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISciUserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/system/userScore")
public class SciUserScoreController  extends BaseController {
    @Autowired
    ISciUserScoreService sciUserScoreService;

    public AjaxResult insertHistory(@RequestBody Map<String, Object> paramMap) {
        AjaxResult ajaxResult = sciUserScoreService.insertHistory(paramMap);
        return ajaxResult;
    }

    public AjaxResult computeUserScore(@RequestBody Map<String, Object> paramMap) {
        AjaxResult ajaxResult = sciUserScoreService.computeUserScore(paramMap);
        return ajaxResult;
    }
}
