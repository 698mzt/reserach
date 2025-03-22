package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.service.ISciProjectScoreCfgService;
import com.ruoyi.common.core.text.Convert;

/**
 * 横向课题得分配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-30
 */
@Service
public class SciProjectScoreCfgServiceImpl implements ISciProjectScoreCfgService 
{
    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    /**
     * 查询横向课题得分配置
     * 
     * @param id 横向课题得分配置主键
     * @return 横向课题得分配置
     */
    @Override
    public SciProjectScoreCfg selectSciProjectScoreCfgById(Long id)
    {
        return sciProjectScoreCfgMapper.selectSciProjectScoreCfgById(id);
    }

    /**
     * 查询横向课题得分配置列表
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 横向课题得分配置
     */
    @Override
    public List<SciProjectScoreCfg> selectSciProjectScoreCfgList(SciProjectScoreCfg sciProjectScoreCfg)
    {
        return sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg);
    }

    /**
     * 新增横向课题得分配置
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 结果
     */
    @Override
    public int insertSciProjectScoreCfg(SciProjectScoreCfg sciProjectScoreCfg)
    {
        return sciProjectScoreCfgMapper.insertSciProjectScoreCfg(sciProjectScoreCfg);
    }

    @Override
    public int insertVerticalScoreCfg(SciProjectScoreCfg sciProjectScoreCfg) {
        return sciProjectScoreCfgMapper.insertVerticalScoreCfg(sciProjectScoreCfg);
    }

    /**
     * 修改横向课题得分配置
     * 
     * @param sciProjectScoreCfg 横向课题得分配置
     * @return 结果
     */
    @Override
    public int updateSciProjectScoreCfg(SciProjectScoreCfg sciProjectScoreCfg)
    {
        sciProjectScoreCfg.setUpdateTime(DateUtils.getNowDate());
        return sciProjectScoreCfgMapper.updateSciProjectScoreCfg(sciProjectScoreCfg);
    }

    /**
     * 批量删除横向课题得分配置
     * 
     * @param ids 需要删除的横向课题得分配置主键
     * @return 结果
     */
    @Override
    public int deleteSciProjectScoreCfgByIds(String ids)
    {
        return sciProjectScoreCfgMapper.deleteSciProjectScoreCfgByIds(Convert.toStrArray(ids));
    }


    /**
     * 删除横向课题得分配置信息
     * 
     * @param id 横向课题得分配置主键
     * @return 结果
     */
    @Override
    public int deleteSciProjectScoreCfgById(Long id)
    {
        return sciProjectScoreCfgMapper.deleteSciProjectScoreCfgById(id);
    }

    @Override
    public Map<String, Object> getProjectScoreCfg() {
        Map<String, Object> returnMap = new HashMap<>();

        List<Map<String,Object>> fundsList = sciProjectScoreCfgMapper.getCfgFunds();
        for (int i = 0; i < fundsList.size(); i++) {
            Map<String,Object> fundsMap = fundsList.get(i);
            List<Map<String,Object>> userScoreList = sciProjectScoreCfgMapper.getUserScoreList(fundsMap);
            fundsMap.put("userScoreList",userScoreList);
        }

        returnMap.put("fundsList",fundsList);
        System.out.println("returnMap = " + returnMap);
        return returnMap;
    }
    @Override
    public Map<String, Object> getVerticalScoreCfg() {
        Map<String, Object> returnMap = new HashMap<>();

        List<Map<String,Object>> fundsList = sciProjectScoreCfgMapper.getVerticalCfgFunds();
        for (int i = 0; i < fundsList.size(); i++) {
            Map<String,Object> fundsMap = fundsList.get(i);
            List<Map<String,Object>> userScoreList = sciProjectScoreCfgMapper.getVerticalUserScoreList(fundsMap);
            fundsMap.put("userScoreList",userScoreList);
        }
        returnMap.put("fundsList",fundsList);
        System.out.println("returnMap = " + returnMap);
        return returnMap;
    }

    @Override
    public int deleteSciProjectScoreCfgByFunds(Map map) {
        return sciProjectScoreCfgMapper.deleteSciProjectScoreCfgByFunds(map);
    }
    @Override
    public int deleteVerticalScoreCfgByFunds(Map map) {
        return sciProjectScoreCfgMapper.deleteVerticalScoreCfgByFunds(map);
    }
}
