package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolScore;
import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.service.SciIntraSchProScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SciIntraSchProScoreServiceImpl implements SciIntraSchProScoreService {

    @Autowired
    private SciIntraSchProScoreMapper sciIntraSchProScoreMapper;

    @Override
    public int set_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro,int key) {
        int re=0;
        String amount=sciIntraSchoolPro.getAmount();
        int amountt = 0;
        try {
            amountt = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            System.out.println("set_SchPro_score:无法转换为整数：" + amount);
        }
        //查询积分配置表拿到积分配置
        List<SciProjectScoreCfg>List = sciIntraSchProScoreMapper.getUserScoreList(amountt);
        System.out.println("List = " + List);

        for (int i=1;i<=4;i++) {
            String userid = i == 1 ? sciIntraSchoolPro.getFirstPersonId() : i == 2 ? sciIntraSchoolPro.getSecondPersonId() : i == 3 ? sciIntraSchoolPro.getThirdPersonId() : sciIntraSchoolPro.getFourthPersonId();
            int useridd = 0;
            try {
                useridd = Integer.parseInt(userid);
            } catch (NumberFormatException e) {
                System.out.println("set_SchPro_score:无法转换为整数：" + userid);
            }
            //SciIntraSchoolScore sciIntraSchoolScore = new SciIntraSchoolScore(sciIntraSchoolPro.getId(), i, getScore(i,List), useridd,0);
            if (key==0){
                re=sciIntraSchProScoreMapper.set_SchPro_score(getScore(i,List,key),sciIntraSchoolPro.getId(),useridd);
            } else if (key==1) {
                //设置结题积分
                re=sciIntraSchProScoreMapper.set_SchPro_JT_score(getScore(i,List,key),sciIntraSchoolPro.getId(),useridd);
            }

        }
        return re;
    }

    @Override
    public Integer getScoreById(Object id ,Long uid) {
        return sciIntraSchProScoreMapper.getScoreById(id,uid);
    }

    /**
     * 撤回时积分
     * @param sciIntraSchoolPro1
     * @return
     */
    @Override
    public Integer update_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro1) {
        int re=0;
        for (int i=1;i<=4;i++) {
            String userid = i == 1 ? sciIntraSchoolPro1.getFirstPersonId() : i == 2 ? sciIntraSchoolPro1.getSecondPersonId() : i == 3 ? sciIntraSchoolPro1.getThirdPersonId() : sciIntraSchoolPro1.getFourthPersonId();
            int useridd = 0;
            try {
                useridd = Integer.parseInt(userid);
            } catch (NumberFormatException e) {
                System.out.println("update_SchPro_score:无法转换为整数：" + userid);
            }
            re=sciIntraSchProScoreMapper.update_SchPro_score(sciIntraSchoolPro1.getId(),useridd);
        }
        return re;
    }

    /**
     * 申请开题的时候就就设置积分为0
     * @param sciIntraSchoolPro
     * @return
     */
    @Override
    public int set_SchPro_score_noScore(SciIntraSchoolPro sciIntraSchoolPro) {
        int re=0;
        for (int i=1;i<=4;i++) {
            String userid = i == 1 ? sciIntraSchoolPro.getFirstPersonId() : i == 2 ? sciIntraSchoolPro.getSecondPersonId() : i == 3 ? sciIntraSchoolPro.getThirdPersonId() : sciIntraSchoolPro.getFourthPersonId();
            int useridd = 0;
            try {
                useridd = Integer.parseInt(userid);
            } catch (NumberFormatException e) {
                System.out.println("set_SchPro_score:无法转换为整数：" + userid);
            }
            SciIntraSchoolScore sciIntraSchoolScore = new SciIntraSchoolScore(sciIntraSchoolPro.getId(), i, 0,useridd,0);
            re=sciIntraSchProScoreMapper.set_SchPro_score_noScore(sciIntraSchoolScore);
        }
        return re;
    }

    private int getScore(int sciIntraSchResponTierId,List<SciProjectScoreCfg>List,int key){
        int return_score=0;
        if (key==0){
            //从与金额对应的四条数据中拿出与负责人层级相同的开题金额
             return_score= Integer.parseInt(List.get(sciIntraSchResponTierId-1).getStartScore());
        } else if (key==1) {
            //从与金额对应的四条数据中拿出与负责人层级相同的结题金额
            return_score= Integer.parseInt(List.get(sciIntraSchResponTierId-1).getEndScore());
        }

        return return_score;
    }
}
