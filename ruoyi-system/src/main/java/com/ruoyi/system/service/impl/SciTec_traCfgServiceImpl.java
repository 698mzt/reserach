package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.domain.SciTec_traScoreCfg;
import com.ruoyi.system.mapper.SciTectraScoreCfgMapper;
import com.ruoyi.system.service.ISciTec_traCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成果转化得分配置Service业务层处理
 *
 * @date 2024-09-30
 */
@Service
public class SciTec_traCfgServiceImpl implements ISciTec_traCfgService {
    @Autowired
    private SciTectraScoreCfgMapper sciTec_traScoreCfgMapper;

    @Override
    public List<SciProjectScoreCfg> selectSciTec_traScoreCfgList(SciTec_traScoreCfg sciTec_traScoreCfg) {
        return sciTec_traScoreCfgMapper.selectSciTec_traScoreCfgList(sciTec_traScoreCfg);
    }

    @Override
    public Map<String, Object> getTec_traScoreCfg() {
        Map<String, Object> returnMap = new HashMap<>();

        List<Map<String,Object>> fundsList = sciTec_traScoreCfgMapper.getCfgFunds();
        for (int i = 0; i < fundsList.size(); i++) {
            Map<String,Object> fundsMap = fundsList.get(i);
            List<Map<String,Object>> userScoreList = sciTec_traScoreCfgMapper.getUserScoreList(fundsMap);
            fundsMap.put("userScoreList",userScoreList);
        }

        returnMap.put("fundsList",fundsList);
        System.out.println("returnMap = " + returnMap);
        return returnMap;
    }

    @Override
    public int insertTec_traScoreCfg(SciTec_traScoreCfg sciTec_traScoreCfg) {
        return sciTec_traScoreCfgMapper.insertTec_traScoreCfg(sciTec_traScoreCfg);
    }

    @Override
    public int deleteSciTec_traScoreCfgByFunds(Map map) {
        return sciTec_traScoreCfgMapper.deleteSciTec_traScoreCfgByFunds(map);
    }
}
