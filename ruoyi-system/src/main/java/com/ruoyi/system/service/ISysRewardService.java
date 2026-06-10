package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysReward;
import org.apache.ibatis.annotations.Options;

import java.util.List;
import java.util.Map;

/**
 * 奖励Service接口
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
public interface ISysRewardService 
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
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertSysReward(SysReward sysReward);

    /**
     * 修改奖励
     * 
     * @param sysReward 奖励
     * @return 结果
     */
    public int updateSysReward(SysReward sysReward);

    /**
     * 批量删除奖励
     * 
     * @param ids 需要删除的奖励主键集合
     * @return 结果
     */
    public int deleteSysRewardByIds(String ids);

    /**
     * 删除奖励信息
     * 
     * @param id 奖励主键
     * @return 结果
     */
    public int deleteSysRewardById(Long id);


    int hxPass(String id,Long uid,String urlFlag);
    public int hxBh(String id, Long uid, String remark, String urlFlag);

//    int hxBh(String id,Long uid,String urlFlag);
//    int hxBh(String id,Long uid, String remark,String urlFlag);
//    int hxoverBh(String id, Long userId, String remark, String urlFlag);

    List<SysReward> selectSysRewardListByKYC(SysReward sysReward);

    List<SysReward> selectSysRewardListByJYS(SysReward sysReward);
    List<SysReward> selectSysRewardListByXUE(SysReward sysReward);
//
//    List<SysReward> selectSysRewardListByOverReward(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardJYS(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardKYC(SysReward sysReward);
//
//    List<SysReward> selectSysRewardListByOVER(SysReward sysReward);

    int overReward(String id, String state);

    int recall(Integer id, String state, Long userId, String remark, String urlFlag);

    /**
     * 统一审批操作（通过/驳回/撤回）
     * 
     * @param id 奖励ID
     * @param userId 用户ID
     * @param comment 审批意见
     * @param operationType 操作类型：approve(通过)、reject(驳回)、recall(撤回)
     * @return 操作结果，成功返回1，失败返回-1
     */
    int approve(String id, Long userId, String comment, String operationType);


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

    /**
     * 保存奖励成员关联信息
     * 
     * 先删除原有成员记录，再批量插入新成员，实现奖励成员的更新
     * 
     * @param rewardId 奖励ID
     * @param personIds 成员ID列表
     */
    void saveRewardPersons(Integer rewardId, List<String> personIds);

    /**
     * 根据奖励ID查询成员ID列表
     * 
     * @param rewardId 奖励ID
     * @return 成员ID列表
     */
    List<String> selectPersionIdsByRewardId(Integer rewardId);

    /**
     * 根据成员ID查询参与的奖励列表
     * 
     * 核心方法：用于成员账号登录后查看自己参与的所有奖励项目
     * 通过关联表 sci_reward_persion 获取成员信息
     * 
     * @param persionId 成员用户ID
     * @param year 年份过滤条件
     * @return 奖励列表
     */
    List<SysReward> selectRewardsByPersionId(String persionId, String year);
    //
    //    @Override
    //    public int hxover(String id, Long uid, String urlFlag) {
    //        String state = "0";
    //        if(urlFlag.equals("JYSOVER")){
    //            state ="8";
    //        }else if(urlFlag.equals("KYCOVER")){
    //            state ="6";
    //        }
    //        int a =  sysRewardMapper.hxPass(id,state);
    //        System.out.println(a);
    //        return a;
    //    }
    //

}
