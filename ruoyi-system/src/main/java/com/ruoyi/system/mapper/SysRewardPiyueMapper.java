package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysRewardPiyue;

import java.util.List;

/**
 * 奖励的审核意见; InnoDB free: 11264 kBMapper接口
 * 
 * @author 张聪
 * @date 2024-08-21
 */
public interface SysRewardPiyueMapper
{
    /**
     * 查询奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 奖励的审核意见; InnoDB free: 11264 kB主键
     * @return 奖励的审核意见; InnoDB free: 11264 kB
     */
    public SysRewardPiyue selectSysRewardPiyueById(Integer id);

    /**
     * 查询奖励的审核意见; InnoDB free: 11264 kB列表
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 奖励的审核意见; InnoDB free: 11264 kB集合
     */
    public List<SysRewardPiyue> selectSysRewardPiyueList(SysRewardPiyue sysRewardPiyue);

    /**
     * 新增奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    public int insertSysRewardPiyue(SysRewardPiyue sysRewardPiyue);

    /**
     * 修改奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param sysRewardPiyue 奖励的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    public int updateSysRewardPiyue(SysRewardPiyue sysRewardPiyue);

    /**
     * 删除奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 奖励的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    public int deleteSysRewardPiyueById(Integer id);

    /**
     * 批量删除奖励的审核意见; InnoDB free: 11264 kB
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysRewardPiyueByIds(String[] ids);
}
