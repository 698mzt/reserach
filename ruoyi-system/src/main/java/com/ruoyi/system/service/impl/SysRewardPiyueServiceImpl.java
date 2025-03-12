package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysRewardPiyue;
import com.ruoyi.system.mapper.SysRewardPiyueMapper;
import com.ruoyi.system.service.ISysRewardPiyueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 奖励的审核意见; InnoDB free: 11264 kBService业务层处理
 * 
 * @author 张聪
 * @date 2024-08-21
 */
@Service
public class SysRewardPiyueServiceImpl implements ISysRewardPiyueService
{
    @Autowired
    private SysRewardPiyueMapper sysRewardPiyueMapper;

    /**
     * 查询奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 奖励的审核意见; InnoDB free: 11264 kB主键
     * @return 奖励的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public SysRewardPiyue selectSysRewardPiyueById(Integer id)
    {
        return sysRewardPiyueMapper.selectSysRewardPiyueById(id);
    }

    /**
     * 查询奖励的审核意见; InnoDB free: 11264 kB列表
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 奖励的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public List<SysRewardPiyue> selectSysRewardPiyueList(SysRewardPiyue sysRewardPiyue)
    {
        return sysRewardPiyueMapper.selectSysRewardPiyueList(sysRewardPiyue);
    }

    /**
     * 新增奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    @Override
    public int insertSysRewardPiyue(SysRewardPiyue sysRewardPiyue)
    {
        sysRewardPiyue.setCreateTime(DateUtils.getNowDate());
        return sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
    }

    /**
     * 修改奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    @Override
    public int updateSysRewardPiyue(SysRewardPiyue sysRewardPiyue)
    {
        return sysRewardPiyueMapper.updateSysRewardPiyue(sysRewardPiyue);
    }

    /**
     * 批量删除奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param ids 需要删除的奖励的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardPiyueByIds(String ids)
    {
        return sysRewardPiyueMapper.deleteSysRewardPiyueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除奖励的审核意见; InnoDB free: 11264 kB信息
     * 
     * @param id 奖励的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardPiyueById(Integer id)
    {
        return sysRewardPiyueMapper.deleteSysRewardPiyueById(id);
    }
}
