package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.domain.SciTec_traScoreCfg;

import java.util.List;
import java.util.Map;

/**
 * 成果转化得分配置Service接口
 *
 * @date 2024-09-30
 */
public interface ISciTec_traCfgService {
    List<SciProjectScoreCfg> selectSciTec_traScoreCfgList(SciTec_traScoreCfg sciTec_traScoreCfg);

    Map<String, Object> getTec_traScoreCfg();

    int insertTec_traScoreCfg(SciTec_traScoreCfg sciTec_traScoreCfg);

    int deleteSciTec_traScoreCfgByFunds(Map map);
}
