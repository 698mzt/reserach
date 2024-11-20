package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SciIntraSchProApplyServiceImpl implements ISciIntraSchProApplyService {

    @Autowired
    private SciIntraSchProApplyMapper sciIntraSchProApplyMapper;
    @Autowired
    private SciIntraSchProPiyueMapper sciIntraSchProPiyueMapper;
    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_my_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_my_IntraSchPro_isOVER(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_ky(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_ky(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_jy(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_jy(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_my(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_my(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_ky(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_ky(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_jy(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_jy(sciIntraSchoolPro);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_my(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_my(sciIntraSchoolPro);
    }

    @Override
    public int insert_SchPro_Apply(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.insert_SchPro_Apply(sciIntraSchoolPro);
    }

    @Override
    public SciIntraSchoolPro sel_IntraSchPro_by_id(Integer id) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(id);
    }

    @Override
    public int sch_hxBh(String id,Long uid, String remark,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="5";
        }else if(urlFlag.equals("pro")){
            state ="3";
        }
        System.out.println("state = " + state);
        int a = sciIntraSchProApplyMapper.sch_hxPass(id,state);
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
        sciIntraSchProPiyue.setState("教研室驳回");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }

    @Override
    public int updateIntraSchoolApply(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.updateIntraSchoolApply(sciIntraSchoolPro);
    }

    @Override
    public int overApply(String id, String state) {
        return sciIntraSchProApplyMapper.overApply(id,state);
    }

    @Override
    public int insert_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.insert_IntraSchPro_OverApply(sciIntraSchoolPro);
    }

    @Override
    public int sch_hxover(String id, Long userId, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYSOVER")){
            state ="8";
        }else if(urlFlag.equals("KYCOVER")){
            state ="6";
        }
        int b =  sciIntraSchProApplyMapper.sch_hxover(id,state);
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        System.out.println(b);
        return a;
    }

    @Override
    public int sch_hxoverBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYSOVER")){
            state ="9";
        }else if(urlFlag.equals("KYCOVER")){
            state ="10";
        }
        int b =  sciIntraSchProApplyMapper.sch_hxover(id,state);
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        System.out.println(b);
        return a;
    }

    @Override
    public int sch_hxPass(String id,Long uid,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="4";
        }else if(urlFlag.equals("pro")){
            state ="2";
        }
        System.out.println("sch_hxPass:state = " + state);
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate("同意");
        sciIntraSchProPiyue.setState("通过");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }


}
