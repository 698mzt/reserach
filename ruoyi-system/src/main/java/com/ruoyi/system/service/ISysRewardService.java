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
     * 统计查询奖励数据
     *
     * @param params 查询参数
     * @return 奖励集合
     */
    List<SysReward> getStatsQuery(Map<String, String> params);
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
