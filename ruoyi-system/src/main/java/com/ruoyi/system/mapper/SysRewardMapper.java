package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysReward;

/**
 * 奖励Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
public interface SysRewardMapper 
{
    /**
     * 查询奖励
     * 
     * @param id 奖励主键
     * @return 奖励
     */
    public SysReward selectSysRewardById(Long id);

    /**
     * 查询奖励列表
     * 
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardList(SysReward sysReward);

    /**
     * 新增奖励
     * 
     * @param sysReward 奖励
     * @return 结果
     */
    public int insertSysReward(SysReward sysReward);

    /**
     * 修改奖励
     * 
     * @param sysReward 奖励
     * @return 结果
     */
    public int updateSysReward(SysReward sysReward);

    /**
     * 删除奖励
     * 
     * @param id 奖励主键
     * @return 结果
     */
    public int deleteSysRewardById(Long id);

    /**
     * 批量删除奖励
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysRewardByIds(String[] ids);
}
