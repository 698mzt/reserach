package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysReward;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    int hxPass(@Param("id") String id, @Param("state") String state);
    int hxBh(@Param("id") String id,@Param("state") String state);

    List<SysReward> selectSysRewardListByKYC(SysReward sysReward);

    List<SysReward> selectSysRewardListByJYS(SysReward sysReward);

    List<SysReward> selectSysRewardListByXUE(SysReward sysReward);

//    List<SysReward> selectSysRewardListByOverReward(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardJYS(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardKYC(SysReward sysReward);


//    List<SysReward> selectSysRewardListByOVER(SysReward sysReward);

    int overReward(@Param("id") String id,@Param("state") String state);


    void updateJifen(@Param("id")  Long id,@Param("jifen") int jifen);
    void resetJifenById(@Param("id") Long id);

    /**
     * 统计查询奖励数据
     *
     * @param params 查询参数
     * @return 奖励集合
     */
    List<SysReward> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询奖励数据
     *
     * @param params 查询参数
     * @return 奖励集合
     */
    List<SysReward> getStatsQueryToCheck(Map<String, String> params);
}
