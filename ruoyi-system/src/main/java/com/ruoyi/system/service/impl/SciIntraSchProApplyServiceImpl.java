package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SciIntraSchProApplyServiceImpl implements ISciIntraSchProApplyService {

    @Autowired
    private SciIntraSchProApplyMapper sciIntraSchProApplyMapper;
    @Autowired
    private SciIntraSchProPiyueMapper sciIntraSchProPiyueMapper;
    @Autowired
    SciIntraSchProApplyServiceImpl sciIntraSchProApplyService;
    @Autowired
    SciIntraSchProScoreMapper sciIntraSchProScoreService;
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
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
    @DataScope(deptAlias = "d", userAlias = "u")
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
    @DataScope(deptAlias = "d", userAlias = "u")
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



    /**
     * 开题通过
     * @param id
     * @param uid
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxPass(String id,Long uid,String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        if(urlFlag.equals("hecha")){
            state ="4";
            sciIntraSchProPiyue.setConcate("科研：开题同意");
        }else if(urlFlag.equals("pro")){
            state ="11";
            sciIntraSchProPiyue.setConcate("教研：开题同意");
        }else if(urlFlag.equals("dept_teacher")){
            state ="2";
            sciIntraSchProPiyue.setConcate("学院：开题同意");
        }
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));

        sciIntraSchProPiyue.setState("通过");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }

    /**
     * 结项通过
     * @param id
     * @param userId
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxover(String id, Long userId, String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        System.out.println("urlFlag = " + urlFlag);
        if(urlFlag.equals("JYSOVER")){
            state ="13";
            sciIntraSchProPiyue.setConcate("教研：结题同意");
        }else if(urlFlag.equals("KYCOVER")){
            state ="6";
            sciIntraSchProPiyue.setConcate("科研：结题同意");
        }else if(urlFlag.equals("dept_teacher")){
            state ="8";
            sciIntraSchProPiyue.setConcate("学院：结题同意");
        }
        //int b =  sciIntraSchProApplyMapper.sch_hxover(id,state);
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        //System.out.println(b);

        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));

        sciIntraSchProPiyue.setState("通过");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }
    /**
     * 开题驳回
     * @param id
     * @param uid
     * @param remark 驳回理由
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxBh(String id,Long uid, String remark,String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        //科研
        if(urlFlag.equals("hecha")){
            state ="5";
        }else if(urlFlag.equals("pro")){
            state ="3";
        }else if (urlFlag.equals("dept_teacher")){
            state ="12";
        }
        int a = sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
        sciIntraSchProPiyue.setState("驳回");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }
    /**
     * 结项驳回
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxoverBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        if(urlFlag.equals("JYSOVER")){
            state ="9";
        }else if(urlFlag.equals("KYCOVER")){
            state ="10";
        }else if(urlFlag.equals("dept_teacher")){
            state ="14";
        }
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
        sciIntraSchProPiyue.setState("驳回");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return a;
    }

    /**
     * 开题撤回
     * @param id
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxCH(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        if(urlFlag.equals("pro")){
            sciIntraSchProPiyue.setState("开题：教研驳回（撤回）");
            state ="3";

        }else if(urlFlag.equals("hecha")){
            sciIntraSchProPiyue.setState("开题：科研驳回（撤回）");
            state ="5";

        }else if(urlFlag.equals("dept_teacher")){
            sciIntraSchProPiyue.setState("开题：学院驳回（撤回）");
            state ="12";

        }
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);

        return a;
    }

    /**
     * 结题撤回
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    public int sch_hxOverCH(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        if(urlFlag.equals("JYSOVER")){
            sciIntraSchProPiyue.setState("结题：教研驳回（撤回）");
            state ="9";

        }else if(urlFlag.equals("KYCOVER")){
            sciIntraSchProPiyue.setState("结题：科研驳回（撤回）");
            state ="10";

        }else if(urlFlag.equals("dept_teacher")){
            sciIntraSchProPiyue.setState("结题：学院驳回（撤回）");
            state ="14";

        }
        //改状态
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);

        //插入记录
        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);

        return a;
    }
    @Override
    public int updateIntraSchoolApply(SciIntraSchoolPro sciIntraSchoolPro) {
        String NowState = sciIntraSchProApplyMapper.geStaticById(sciIntraSchoolPro.getId());

        String id = String.valueOf(sciIntraSchoolPro.getId());
        if (NowState.equals("3") || NowState.equals("5") ||NowState.equals("12")){
            sciIntraSchProApplyMapper.sch_hxPass(id,"1");
        }else if (NowState.equals("9") || NowState.equals("10") ||NowState.equals("14")){

            sciIntraSchProApplyMapper.sch_hxPass(id,"7");
        }
        return sciIntraSchProApplyMapper.updateIntraSchoolApply(sciIntraSchoolPro);
    }

    @Override
    public int overApply(String id, String state) {
        return sciIntraSchProApplyMapper.overApply(id,state);
    }

    @Override
    public int update_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.update_IntraSchPro_OverApply(sciIntraSchoolPro);
    }





    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER_admin(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_admin(sciIntraSchoolPro);
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_admin(sciIntraSchoolPro);
    }

    @Override
    public List<Long> getRoleid_list(Long userId) {
        return sciIntraSchProApplyMapper.getRoleid_list(userId);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_dept_teacher(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_closure_dept_teacher(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_dept_teacher(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER_dept_teacher(sciIntraSchoolPro);
    }


    @Override
    public int deleteSciSCHHorizontalApplyByIds(String ids)
    {
        return sciIntraSchProApplyMapper.deleteSciSCHHorizontalApplyByIds(Convert.toStrArray(ids));
    }

    @Override
    public String getuser_dnameById(Long userId) {
        return sciIntraSchProApplyMapper.getuser_dnameById(userId);
    }

    @Override
    public List<Map<String, Object>> getfilekey(Long userId) {
        return sciIntraSchProApplyMapper.getfilekey(userId);
    }

    /**
     * 更改自己的草稿状态，提交到教研室，加入操作记录
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int subDraft(String id, Long userid) {
        //1.更改草稿状态
        sciIntraSchProApplyMapper.sch_hxPass(id,"1");
        //2.插入操作记录
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        sciIntraSchProPiyue.setUid(userid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setState("草稿提交到教研室");
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return 1;
    }

}
