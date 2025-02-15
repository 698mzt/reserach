package com.ruoyi.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciZhuanliruanzhuScoreCfgMapper;
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.system.service.ISciZhuanliruanzhuScoreCfgService;
import com.ruoyi.common.core.text.Convert;

/**
 * 专利软著得分配置Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-30
 */
@Service
public class SciZhuanliruanzhuScoreCfgServiceImpl implements ISciZhuanliruanzhuScoreCfgService
{
    @Autowired
    private SciZhuanliruanzhuScoreCfgMapper sciZhuanliruanzhuScoreCfgMapper;

    /**
     * 查询专利软著得分配置
     *
     * @param id 专利软著得分配置主键
     * @return 专利软著得分配置
     */
    @Override
    public SciZhuanliruanzhuScoreCfg selectSciZhuanliruanzhuScoreCfgById(Long id)
    {
        return sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgById(id);
    }

    /**
     * 查询专利软著得分配置列表
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 专利软著得分配置
     */
    @Override
    public List<SciZhuanliruanzhuScoreCfg> selectSciZhuanliruanzhuScoreCfgList(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        return sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
    }

    /**
     * 新增专利软著得分配置
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 结果
     */
    @Override
    public int insertSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        return sciZhuanliruanzhuScoreCfgMapper.insertSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg);
    }

    /**
     * 修改专利软著得分配置
     *
     * @param sciZhuanliruanzhuScoreCfg 专利软著得分配置
     * @return 结果
     */
    @Override
    public int updateSciZhuanliruanzhuScoreCfg(SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg)
    {
        sciZhuanliruanzhuScoreCfg.setUpdateTime(DateUtils.getNowDate());
        return sciZhuanliruanzhuScoreCfgMapper.updateSciZhuanliruanzhuScoreCfg(sciZhuanliruanzhuScoreCfg);
    }

    /**
     * 批量删除专利软著得分配置
     *
     * @param ids 需要删除的专利软著得分配置主键
     * @return 结果
     */
    @Override
    public int deleteSciZhuanliruanzhuScoreCfgByIds(String ids)
    {
        return sciZhuanliruanzhuScoreCfgMapper.deleteSciZhuanliruanzhuScoreCfgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除专利软著得分配置信息
     *
     * @param id 专利软著得分配置主键
     * @return 结果
     */
    @Override
    public int deleteSciZhuanliruanzhuScoreCfgById(Long id)
    {
        return sciZhuanliruanzhuScoreCfgMapper.deleteSciZhuanliruanzhuScoreCfgById(id);
    }

    @Override
    public Map<String, Object> getZhuanliruanzhuScoreCfg() {
        Map<String, Object> returnMap = new HashMap<>();

        List<Map<String,Object>> fundsList = sciZhuanliruanzhuScoreCfgMapper.getCfgFunds();
        for (int i = 0; i < fundsList.size(); i++) {
            Map<String,Object> fundsMap = fundsList.get(i);
            List<Map<String,Object>> userScoreList = sciZhuanliruanzhuScoreCfgMapper.getUserScoreList(fundsMap);
            fundsMap.put("userScoreList",userScoreList);
        }

        returnMap.put("fundsList",fundsList);
        System.out.println("returnMap = " + returnMap);
        return returnMap;
    }

    @Override
    public int deleteSciZhuanliruanzhuScoreCfgByFunds(Map map) {
        return sciZhuanliruanzhuScoreCfgMapper.deleteSciZhuanliruanzhuScoreCfgByFunds(map);
    }
}
