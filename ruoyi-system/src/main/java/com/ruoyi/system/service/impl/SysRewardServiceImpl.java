package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysRewardMapper;
import com.ruoyi.system.domain.SysReward;
import com.ruoyi.system.service.ISysRewardService;
import com.ruoyi.common.core.text.Convert;

/**
 * 奖励Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
@Service
public class SysRewardServiceImpl implements ISysRewardService 
{
    @Autowired
    private SysRewardMapper sysRewardMapper;

    /**
     * 查询奖励
     * 
     * @param id 奖励主键
     * @return 奖励
     */
    @Override
    public SysReward selectSysRewardById(Long id)
    {
        return sysRewardMapper.selectSysRewardById(id);
    }

    /**
     * 查询奖励列表
     * 
     * @param sysReward 奖励
     * @return 奖励
     */
    @Override
    public List<SysReward> selectSysRewardList(SysReward sysReward)
    {
        return sysRewardMapper.selectSysRewardList(sysReward);
    }

    /**
     * 新增奖励
     * 
     * @param sysReward 奖励
     * @return 结果
     */
    @Override
    public int insertSysReward(SysReward sysReward)
    {
        return sysRewardMapper.insertSysReward(sysReward);
    }

    /**
     * 修改奖励
     * 
     * @param sysReward 奖励
     * @return 结果
     */
    @Override
    public int updateSysReward(SysReward sysReward)
    {
        return sysRewardMapper.updateSysReward(sysReward);
    }

    /**
     * 批量删除奖励
     * 
     * @param ids 需要删除的奖励主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardByIds(String ids)
    {
        return sysRewardMapper.deleteSysRewardByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除奖励信息
     * 
     * @param id 奖励主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardById(Long id)
    {
        return sysRewardMapper.deleteSysRewardById(id);
    }
}
