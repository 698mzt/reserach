package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.domain.PageRenderResult;
import java.util.List;
import java.util.Map;

@Service
public class SciIntraSchProApplyServiceImpl implements ISciIntraSchProApplyService {

    private static final String TEC_TRA_PROCESS_CODE = "TEC_TRA_APPLY";
    private static final String TEC_TRA_DRAFT = "TEC_TRA_DRAFT";
    private static final String TEC_TRA_JYS_AUDIT = "TEC_TRA_JYS_AUDIT";
    private static final String TEC_TRA_KYC_AUDIT = "TEC_TRA_KYC_AUDIT";
    private static final String TEC_TRA_PASSED = "TEC_TRA_PASSED";
    private static final String TEC_TRA_REJECTED = "TEC_TRA_REJECTED";
    private static final String MODULE_CODE = "TEC_TRA";
    private static final String PERM_PREFIX = "system:intraSch";
    @Autowired
    private SciIntraSchProApplyMapper sciIntraSchProApplyMapper;
    @Autowired
    private SciIntraSchProPiyueMapper sciIntraSchProPiyueMapper;
    @Autowired
    SciIntraSchProApplyServiceImpl sciIntraSchProApplyService;
    @Autowired
    SciIntraSchProScoreMapper sciIntraSchProScoreService;
    @Autowired
    private IApprovalProcessService approvalProcessService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IPageRenderService pageRenderService;

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_my_IntraSchPro_isOVER(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_my_IntraSchPro_isOVER(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_ky(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_approval_ky(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_jy(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_approval_jy(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_my(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_approval_my(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }


    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_ky(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_closure_ky(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_jy(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_closure_jy(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }


    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_my(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_closure_my(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
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
        SciIntraSchoolPro pro = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(id);
        fillPageRenderData(pro);
        return pro;
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
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return 0;
        }

        String currentState = normalizeTecTraState(apply.getState());
        if (isTecTraState(apply.getState())) {
            ApprovalResult result = approvalProcessService.approve(buildTecTraApprovalRequest(id, uid, currentState, "通过"));
            if (!result.isSuccess()) {
                return 0;
            }
            insertTecTraPiyue(id, uid, getApproveText(result.getNewState()), "通过");
            return 1;
        }

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
        if ("0".equals(state)) {
            return 0;
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
        if ("0".equals(state)) {
            return 0;
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
     * 0:成功 1:失败
     */
    @Override
    public int sch_hxBh(String id,Long uid, String remark,String urlFlag) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return 0;
        }

        String currentState = normalizeTecTraState(apply.getState());
        if (isTecTraState(apply.getState())) {
            ApprovalResult result = approvalProcessService.reject(buildTecTraApprovalRequest(id, uid, currentState, remark));
            if (!result.isSuccess()) {
                return 0;
            }
            insertTecTraPiyue(id, uid, "成果转化-驳回", remark);
            return 1;
        }

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
        if ("0".equals(state)) {
            return 0;
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
        if ("0".equals(state)) {
            return 0;
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
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return 0;
        }
        String currentState = normalizeTecTraState(apply.getState());
        if (isTecTraState(apply.getState())) {
            ApprovalResult result = approvalProcessService.recall(buildTecTraApprovalRequest(id, userId, currentState, remark));
            if (!result.isSuccess()) {
                return 0;
            }
            insertTecTraPiyue(id, userId, "成果转化-撤回", remark);
            return 1;
        }

        String state = "0";
        SciIntraSchProPiyue sciIntraSchProPiyue = new SciIntraSchProPiyue();
        if(urlFlag.equals("pro")){
            sciIntraSchProPiyue.setState("开题：教研驳回（撤回）");
            sciIntraSchProPiyue.setConcate("开题：教研驳回（撤回）");
            state = "1";

        }else if(urlFlag.equals("hecha")){
            sciIntraSchProPiyue.setState("开题：科研驳回（撤回）");
            sciIntraSchProPiyue.setConcate("开题：科研驳回（撤回）");
            state = "2";

        }else if(urlFlag.equals("dept_teacher")){
            sciIntraSchProPiyue.setState("开题：学院驳回（撤回）");
            sciIntraSchProPiyue.setConcate("开题：学院驳回（撤回）");
            state = "11";

        }
        if ("0".equals(state)) {
            return 0;
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
        if ("0".equals(state)) {
            return 0;
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
        if (NowState.equals("3") || NowState.equals("5") || NowState.equals("12") || TEC_TRA_REJECTED.equals(NowState)){
            sciIntraSchoolPro.setState(TEC_TRA_DRAFT);
        }else if (NowState.equals("9") || NowState.equals("10") ||NowState.equals("14")){
            sciIntraSchoolPro.setState("16");
        }
        int rows = sciIntraSchProApplyMapper.updateIntraSchoolApply(sciIntraSchoolPro);
        return rows;
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
    public int subDraft(String id, Long userid,String state) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply != null && isTecTraState(apply.getState())) {
            String currentState = normalizeTecTraState(apply.getState());
            ApprovalResult result = approvalProcessService.submitApproval(buildTecTraApprovalRequest(id, userid, currentState, "提交审批"));
            if (!result.isSuccess()) {
                return 0;
            }
            insertTecTraPiyue(id, userid, "成果转化-提交", "提交审批");
            return 1;
        }

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

    /**
     * 判断是否为旧状态码（数字格式）
     */
    private boolean isTecTraState(String state) {
        return "TEC_TRA_DRAFT".equals(state)
                || "TEC_TRA_JYS_AUDIT".equals(state)
                || "TEC_TRA_KYC_AUDIT".equals(state)
                || "TEC_TRA_PASSED".equals(state)
                || "TEC_TRA_REJECTED".equals(state);
    }

    /**
     * 将旧状态码映射为新状态编码
     */
    private String normalizeTecTraState(String state) {
        if (isTecTraState(state)) {
            return state; // 已经是新状态码
        }
        // 旧状态码映射（根据实际业务调整）
        switch (state) {
            case "15": return "TEC_TRA_DRAFT";
            case "1": return "TEC_TRA_JYS_AUDIT";
            case "2": return "TEC_TRA_KYC_AUDIT";
            case "3": return "TEC_TRA_REJECTED";
            case "4": return "TEC_TRA_PASSED";
            case "5": return "TEC_TRA_REJECTED";
            case "12": return "TEC_TRA_REJECTED";
            default: return state;
        }
    }

    private String getApproveText(String newState) {
        if (TEC_TRA_KYC_AUDIT.equals(newState)) {
            return "成果转化-教研室审批通过";
        }
        if (TEC_TRA_PASSED.equals(newState)) {
            return "成果转化-科研处审批通过";
        }
        return "成果转化-审批通过";
    }

    private void insertTecTraPiyue(String id, Long uid, String state, String concate) {
        SciIntraSchProPiyue piyue = new SciIntraSchProPiyue();
        piyue.setUid(uid);
        piyue.setSchxktId(Integer.valueOf(id));
        piyue.setState(state);
        piyue.setConcate(concate == null || concate.trim().isEmpty() ? state : concate);
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(piyue);
    }

    private ApprovalRequest buildTecTraApprovalRequest(String id, Long uid, String currentState, String comment) {
        SysUser user = uid != null ? sysUserService.selectUserById(uid) : null;
        return ApprovalRequest.of(
                TEC_TRA_PROCESS_CODE,
                Long.valueOf(id),
                currentState,
                comment,
                uid,
                user != null ? user.getUserName() : "",
                user != null && user.getDept() != null ? user.getDept().getDeptName() : ""
        );
    }
    /**
     * 填充页面渲染数据（statusMeta和actions）
     * 调用标准公有方法 fillPageRenderData() 实现页面渲染数据填充
     *
     * @param pro 成果转化对象
     */
    private void fillPageRenderData(SciIntraSchoolPro pro) {
        if (pro == null) {
            return;
        }
        SysUser currentUser = ShiroUtils.getSysUser();
        String normalizedState = normalizeTecTraState(pro.getState());
        PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                MODULE_CODE,
                PERM_PREFIX,
                TEC_TRA_PROCESS_CODE,
                normalizedState,
                pro.getId() != null ? pro.getId().longValue() : null,
                pro.getUid() != null ? pro.getUid() : null,
                null  // 传 null，由公共服务内部处理缓存
        );
        pro.setStatusMeta(result.getStatusMeta());
        pro.setActions(result.getActions());
    }
}



