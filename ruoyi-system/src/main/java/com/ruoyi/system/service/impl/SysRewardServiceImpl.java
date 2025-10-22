package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciRewardScoreCfg;
import com.ruoyi.system.domain.SysReward;
import com.ruoyi.system.domain.SysRewardPiyue;
import com.ruoyi.system.mapper.SciRewardScoreCfgMapper;
import com.ruoyi.system.mapper.SysRewardMapper;
import com.ruoyi.system.mapper.SysRewardPiyueMapper;
import com.ruoyi.system.service.ISysRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    @Autowired
    private SysRewardPiyueMapper sysRewardPiyueMapper;
    @Autowired
    private SciRewardScoreCfgMapper sciRewardScoreCfgMapper;

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
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysReward> selectSysRewardList(SysReward sysReward)
    {
        return sysRewardMapper.selectSysRewardList(sysReward);
    }

    @Override
    public List<SysReward> selectSysRewardListByKYC(SysReward sysReward) {
        return sysRewardMapper.selectSysRewardListByKYC(sysReward);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysReward> selectSysRewardListByJYS(SysReward sysReward) {
        return sysRewardMapper.selectSysRewardListByJYS(sysReward);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysReward> selectSysRewardListByXUE(SysReward sysReward) {
        return sysRewardMapper.selectSysRewardListByXUE(sysReward);
    }

//    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
//    public List<SysReward> selectSysRewardListByOverReward(SysReward sysReward) {
//        return sysRewardMapper.selectSysRewardList(sysReward);
//    }
//
//    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
//    public List<SysReward> selectSysRewardListByOverRewardJYS(SysReward sysReward) {
//        return sysRewardMapper.selectSysRewardList(sysReward);
//    }
//
//    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
//    public List<SysReward> selectSysRewardListByOverRewardKYC(SysReward sysReward) {
//        return sysRewardMapper.selectSysRewardList(sysReward);
//    }
//
//    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
//    public List<SysReward> selectSysRewardListByOVER(SysReward sysReward) {
//        return sysRewardMapper.selectSysRewardList(sysReward);
//    }

    @Override
    public int overReward(String id, String state) {
        return sysRewardMapper.overReward(id,state);
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
        int a = sysRewardMapper.insertSysReward(sysReward);
        int id = Integer.parseInt(sysReward.getId().toString());
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(sysReward.getUserId());
        sysRewardPiyue.setRewardId(id);
        sysRewardPiyue.setConcate("新增");
        sysRewardPiyue.setState("新增");
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
        return a;
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
        sysRewardMapper.updateSysReward(sysReward);
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(sysReward.getUserId());
        sysRewardPiyue.setRewardId(Integer.valueOf(sysReward.getId().toString()));
        if (sysReward.getState().equals("1")) {
            sysRewardPiyue.setConcate("提交");
            sysRewardPiyue.setState("提交");
        }else {
            sysRewardPiyue.setConcate("修改");
            sysRewardPiyue.setState("修改");
        }
        return sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
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
//
    @Override
    public int hxPass(String id, Long uid, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="4";
        }else if(urlFlag.equals("pro")) {
            state = "2";
        }
        else if(urlFlag.equals("chayue")) {
            state = "6";
            SysReward sysReward = sysRewardMapper.selectSysRewardById(Long.valueOf(id));
            String a = sysReward.getRewardFenlei();
            String b = sysReward.getRewardDengji();
            String c = sysReward.getRewardPaiming();
            SciRewardScoreCfg sciRewardScoreCfg = new SciRewardScoreCfg();
            sciRewardScoreCfg.setFenLei(a);
            sciRewardScoreCfg.setDengJi(b);
            sciRewardScoreCfg.setPaiMing(c);
            List<SciRewardScoreCfg> d = sciRewardScoreCfgMapper.selectSciRewardScoreCfgList(sciRewardScoreCfg);

            int jifen = 0;
            for (SciRewardScoreCfg cfg : d) {
                jifen = Integer.parseInt(cfg.getTotalScore());
                System.out.println("Jifen: " + jifen);
            }
           sysRewardMapper.updateJifen(Long.valueOf(id), jifen);

        }
        int a =  sysRewardMapper.hxPass(id,state);
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(uid);
        sysRewardPiyue.setRewardId(Integer.valueOf(id));
        sysRewardPiyue.setConcate("同意");
        sysRewardPiyue.setState("通过");
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
        return a;
    }


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
    @Override
    public int hxBh(String id, Long uid, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="5";
        }else if(urlFlag.equals("pro")){
            state ="3";
        }else if(urlFlag.equals("chayue")){
            state ="7";
        }
        int a = sysRewardMapper.hxPass(id,state);
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(uid);
        sysRewardPiyue.setRewardId(Integer.valueOf(id));
        sysRewardPiyue.setConcate(remark);
        sysRewardPiyue.setState("驳回");
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
        return a;
    }
//撤销
    @Override
    public int recall(Integer id, String state,Long uid, String remark, String urlFlag) {
        String newState = state;
        switch (state) {
//            教研室
            case "2":
            case "3":
                newState = "1";
                break;
            //            学院
            case "4":
            case "5":
                newState = "2";
                break;
            //            科研处
            case "6":
            case "7":
                newState = "4";
                break;
        }
        if (state.equals("6")) {
            sysRewardMapper.resetJifenById(Long.valueOf(id));
        }


//        设置状态
    int a =sysRewardMapper.hxPass(id.toString(),newState);
    //        插入日志
    SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(uid);
        sysRewardPiyue.setRewardId(Integer.valueOf(id));
        sysRewardPiyue.setConcate(remark);
        sysRewardPiyue.setState("撤回");
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
        return a;

    }
    @Override
    public List<SysReward> getStatsQuery(Map<String, String> params) {
        return sysRewardMapper.getStatsQuery(params);
    }
}
