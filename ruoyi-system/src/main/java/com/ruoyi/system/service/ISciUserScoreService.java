package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;

import java.util.Map;

public interface ISciUserScoreService {
    AjaxResult computeUserScore(Map<String, Object> paramMap);

    AjaxResult insertHistory(Map<String, Object> paramMap);
}
