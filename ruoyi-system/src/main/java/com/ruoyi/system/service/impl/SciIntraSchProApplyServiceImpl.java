package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.system.domain.SciIntraSchProPiyue;

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
    @DataScope(deptAlias = "d", userAlias = "u")
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
    @DataScope(deptAlias = "d", userAlias = "u")
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
        sciIntraSchProApplyMapper.insert_SchPro_Apply(sciIntraSchoolPro);
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        sciIntraSchProPiyue.setConcate("新建草稿");
        sciIntraSchProPiyue.setUid(sciIntraSchoolPro.getUid());
        sciIntraSchProPiyue.setSchxktId(sciIntraSchoolPro.getId());
        sciIntraSchProPiyue.setState("新建草稿");
        return sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
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
            sciIntraSchProPiyue.setState("科研：开题同意");
        }else if(urlFlag.equals("pro")){
            state ="11";
            sciIntraSchProPiyue.setConcate("教研：开题同意");
            sciIntraSchProPiyue.setState("教研：开题同意");
        }else if(urlFlag.equals("dept_teacher")){
            state ="2";
            sciIntraSchProPiyue.setConcate("学院：开题同意");
            sciIntraSchProPiyue.setState("学院：开题同意");
        }
        // 状态修改
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        // 补全批阅记录
        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));

        // 插入批阅记录
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
          sciIntraSchProPiyue.setState("教研：结题同意");
        }else if(urlFlag.equals("KYCOVER")){
            state ="6";
            sciIntraSchProPiyue.setConcate("科研：结题同意");
          sciIntraSchProPiyue.setState("科研：结题同意");

        }else if(urlFlag.equals("dept_teacher")){
            state ="8";
            sciIntraSchProPiyue.setConcate("学院：结题同意");
          sciIntraSchProPiyue.setState("学院：结题同意");

        }
        //int b =  sciIntraSchProApplyMapper.sch_hxover(id,state);
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);
        //System.out.println(b);

        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));


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
          sciIntraSchProPiyue.setState("科研：开题驳回");
            state ="5";
        }else if(urlFlag.equals("pro")){
          sciIntraSchProPiyue.setState("教研：开题驳回");
            state ="3";
        }else if (urlFlag.equals("dept_teacher")){
          sciIntraSchProPiyue.setState("学院：开题驳回");
            state ="12";
        }
        int a = sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(uid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);

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
          sciIntraSchProPiyue.setState("结题：教研驳回");
            state ="9";
        }else if(urlFlag.equals("KYCOVER")){
          sciIntraSchProPiyue.setState("结题：科研驳回");
            state ="10";
        }else if(urlFlag.equals("dept_teacher")){
          sciIntraSchProPiyue.setState("结题：学院驳回");
            state ="14";
        }
        int a =  sciIntraSchProApplyMapper.sch_hxPass(id,state);

        sciIntraSchProPiyue.setUid(userId);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        sciIntraSchProPiyue.setConcate(remark);
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
            sciIntraSchProPiyue.setConcate("开题：教研驳回（撤回）");
            state ="1";

        }else if(urlFlag.equals("hecha")){
            sciIntraSchProPiyue.setState("开题：科研驳回（撤回）");
            sciIntraSchProPiyue.setConcate("开题：科研驳回（撤回）");
            state ="2";

        }else if(urlFlag.equals("dept_teacher")){
            sciIntraSchProPiyue.setState("开题：学院驳回（撤回）");
            sciIntraSchProPiyue.setConcate("开题：学院驳回（撤回）");
            state ="11";

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
            state ="7";

        }else if(urlFlag.equals("KYCOVER")){
            sciIntraSchProPiyue.setState("结题：科研驳回（撤回）");
            state ="8";

        }else if(urlFlag.equals("dept_teacher")){
            sciIntraSchProPiyue.setState("结题：学院驳回（撤回）");
            state ="13";

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
            sciIntraSchProApplyMapper.sch_hxPass(id,"15");
        }else if (NowState.equals("9") || NowState.equals("10") ||NowState.equals("14")){

            sciIntraSchProApplyMapper.sch_hxPass(id,"16");
        }
        return sciIntraSchProApplyMapper.updateIntraSchoolApply(sciIntraSchoolPro);
    }

    @Override
    public int overApply(String id, String state, Long userId) {
      SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
      sciIntraSchProPiyue.setState("申请结项");
      sciIntraSchProPiyue.setConcate("申请结项");
      sciIntraSchProPiyue.setId(Integer.valueOf(id));
      sciIntraSchProPiyue.setUid(userId);
      sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
      return sciIntraSchProApplyMapper.overApply(id,state);
    }

    @Override
    public int update_IntraSchPro_OverApply(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.update_IntraSchPro_OverApply(sciIntraSchoolPro);
    }





    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_isOVER_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER_admin(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        return sciIntraSchProApplyMapper.sel_IntraSchPro_approval_admin(sciIntraSchoolPro);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
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
    public int subDraft(String id, Long userid,String state) {
        //1.更改草稿状态
        sciIntraSchProApplyMapper.sch_hxPass(id,state);
        //2.插入操作记录
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        sciIntraSchProPiyue.setUid(userid);
        sciIntraSchProPiyue.setSchxktId(Integer.valueOf(id));
        if(state.equals("1")){
          sciIntraSchProPiyue.setState("开题：提交草稿");
          sciIntraSchProPiyue.setConcate("开题：提交草稿");
        }else if(state.equals("7")){
          sciIntraSchProPiyue.setState("结题：提交草稿");
          sciIntraSchProPiyue.setConcate("结题：提交草稿");
        }
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(sciIntraSchProPiyue);
        return 1;
    }
    @Override
    public List<SciIntraSchoolPro> getStatsQuery(Map<String, String> params) {
        return sciIntraSchProApplyMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciIntraSchoolPro> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciIntraSchProApplyMapper.getStatsQueryToCheck(params);
    }
    
    @Override
    public List<String> selectPersionIdsByIntraSchId(Integer id) {
        // 根据成果转化ID查询所有成员ID，包括前四个固定成员和动态成员
        SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(id);
        List<String> personIds = new ArrayList<>();
        
        if (sciIntraSchoolPro != null) {
            // 添加前四个固定成员
            if (sciIntraSchoolPro.getFirstPersonId() != null && !sciIntraSchoolPro.getFirstPersonId().isEmpty() 
                && !sciIntraSchoolPro.getFirstPersonId().equals("-1") && !sciIntraSchoolPro.getFirstPersonId().equals("null")) {
                personIds.add(sciIntraSchoolPro.getFirstPersonId());
            }
            
            if (sciIntraSchoolPro.getSecondPersonId() != null && !sciIntraSchoolPro.getSecondPersonId().isEmpty() 
                && !sciIntraSchoolPro.getSecondPersonId().equals("-1") && !sciIntraSchoolPro.getSecondPersonId().equals("null")) {
                personIds.add(sciIntraSchoolPro.getSecondPersonId());
            }
            
            if (sciIntraSchoolPro.getThirdPersonId() != null && !sciIntraSchoolPro.getThirdPersonId().isEmpty() 
                && !sciIntraSchoolPro.getThirdPersonId().equals("-1") && !sciIntraSchoolPro.getThirdPersonId().equals("null")) {
                personIds.add(sciIntraSchoolPro.getThirdPersonId());
            }
            
            if (sciIntraSchoolPro.getFourthPersonId() != null && !sciIntraSchoolPro.getFourthPersonId().isEmpty() 
                && !sciIntraSchoolPro.getFourthPersonId().equals("-1") && !sciIntraSchoolPro.getFourthPersonId().equals("null")) {
                personIds.add(sciIntraSchoolPro.getFourthPersonId());
            }
            
            // 添加动态成员（如果存在）
            if (sciIntraSchoolPro.getMembers() != null && !sciIntraSchoolPro.getMembers().isEmpty()) {
                for (String member : sciIntraSchoolPro.getMembers()) {
                    if (member != null && !member.isEmpty() 
                        && !member.equals("-1") && !member.equals("null")) {
                        personIds.add(member);
                    }
                }
            }
        }
        
        return personIds;
    }
}


