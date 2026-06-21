package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.PageRenderActionItem;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.mapper.SciIntraSchProPiyueMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.domain.PageRenderResult;
import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private IPageRenderService pageRenderService;
    @Autowired
    private IApprovalProcessService approvalProcessService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

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


    @Override
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
     * 立项审批通过
     * 参考论文模块实现，调用公有方法 approvalProcessService.approve()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param uid
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxPass(String id, Long uid, String urlFlag) {
        return sch_hxPass(id, uid, urlFlag, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxPass(String id, Long uid, String urlFlag, SciIntraSchoolPro approvalEdit) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(uid);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行审批通过
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, "审批通过",
                uid, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.approve(request);
        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0 && approvalEdit != null) {
            approvalEdit.setId(Integer.valueOf(id));
            sciIntraSchProApplyMapper.updateApprovalEditableFields(approvalEdit);
        }
        if (rows > 0) {
            boolean isLast = result.isLast();
            String piyueText = isLast ? "成果转化-科研处审批通过" : "成果转化-教研室审批通过";
            insertTecTraPiyue(id, uid, piyueText, "通过");
        }
        return rows;
    }

    /**
     * 结项审批通过
     * 参考论文模块实现，调用公有方法 approvalProcessService.approve()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param userId
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxover(String id, Long userId, String urlFlag) {
        return sch_hxover(id, userId, urlFlag, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxover(String id, Long userId, String urlFlag, SciIntraSchoolPro approvalEdit) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行结项审批通过
        String approveComment;
        if ("JYSOVER".equals(urlFlag)) {
            approveComment = "教研：结题同意";
        } else if ("KYCOVER".equals(urlFlag)) {
            approveComment = "科研：结题同意";
        } else if ("dept_teacher".equals(urlFlag)) {
            approveComment = "学院：结题同意";
        } else {
            return 0;
        }

        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, approveComment,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.approve(request);
        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0 && approvalEdit != null) {
            approvalEdit.setId(Integer.valueOf(id));
            sciIntraSchProApplyMapper.updateOverApprovalEditableFields(approvalEdit);
        }
        if (rows > 0) {
            insertTecTraPiyue(id, userId, approveComment, approveComment);
        }
        return rows;
    }
    /**
     * 立项审批驳回
     * 参考论文模块实现，调用公有方法 approvalProcessService.reject()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param uid
     * @param remark 驳回理由
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxBh(String id, Long uid, String remark, String urlFlag) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(uid);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行驳回
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, remark,
                uid, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.reject(request);
        if (!result.isSuccess()) {
            return -1;
        }

        // 驳回后设置为已驳回状态
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, TEC_TRA_REJECTED);
        if (rows > 0) {
            insertTecTraPiyue(id, uid, "成果转化-驳回", remark);
        }
        return rows;
    }
    /**
     * 结项审批驳回
     * 参考论文模块实现，调用公有方法 approvalProcessService.reject()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxoverBh(String id, Long userId, String remark, String urlFlag) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行结项驳回
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, remark,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.reject(request);
        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0) {
            String piyueState;
            if ("JYSOVER".equals(urlFlag)) {
                piyueState = "结题：教研驳回";
            } else if ("KYCOVER".equals(urlFlag)) {
                piyueState = "结题：科研驳回";
            } else if ("dept_teacher".equals(urlFlag)) {
                piyueState = "结题：学院驳回";
            } else {
                piyueState = "结题：驳回";
            }
            insertTecTraPiyue(id, userId, piyueState, remark);
        }
        return rows;
    }

    /**
     * 立项审批撤回
     * 参考论文模块实现，调用公有方法 approvalProcessService.recall()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxCH(String id, Long userId, String remark, String urlFlag) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行撤回
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, remark,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.recall(request);
        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0) {
            insertTecTraPiyue(id, userId, "成果转化-撤回", remark);
        }
        return rows;
    }

    /**
     * 结项审批撤回
     * 参考论文模块实现，调用公有方法 approvalProcessService.recall()
     * 公有方法中已包含后端身份校验、节点权限判断和状态流转逻辑
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sch_hxOverCH(String id, Long userId, String remark, String urlFlag) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return -1;
        }

        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        // 调用公有审批服务执行结项撤回
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, remark,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.recall(request);
        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0) {
            String piyueState;
            if ("JYSOVER".equals(urlFlag)) {
                piyueState = "结题：教研撤回";
            } else if ("KYCOVER".equals(urlFlag)) {
                piyueState = "结题：科研撤回";
            } else if ("dept_teacher".equals(urlFlag)) {
                piyueState = "结题：学院撤回";
            } else {
                piyueState = "结题：撤回";
            }
            insertTecTraPiyue(id, userId, piyueState, remark);
        }
        return rows;
    }
    @Override
    public int updateIntraSchoolApply(SciIntraSchoolPro sciIntraSchoolPro, Long userId) {
        String NowState = sciIntraSchProApplyMapper.geStaticById(sciIntraSchoolPro.getId());

        String id = String.valueOf(sciIntraSchoolPro.getId());
        if ("3".equals(NowState) || "5".equals(NowState) || "12".equals(NowState) || TEC_TRA_REJECTED.equals(NowState)){
            sciIntraSchoolPro.setState(TEC_TRA_DRAFT);
        }else if ("9".equals(NowState) || "10".equals(NowState) || "14".equals(NowState)){
            sciIntraSchoolPro.setState("16");
        }
        int rows = sciIntraSchProApplyMapper.updateIntraSchoolApply(sciIntraSchoolPro);
        if (rows > 0) {
            SciIntraSchProPiyue piyue = new SciIntraSchProPiyue();
            piyue.setUid(userId);
            piyue.setSchxktId(sciIntraSchoolPro.getId());
            piyue.setState("修改");
            piyue.setConcate("编辑");
            sciIntraSchProPiyueMapper.insertIntraSchProPiyue(piyue);
        }
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
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_isOVER_admin(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_approval_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_approval_admin(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
    }

    @Override
    public List<SciIntraSchoolPro> sel_IntraSchPro_closure_admin(SciIntraSchoolPro sciIntraSchoolPro) {
        List<SciIntraSchoolPro> list = sciIntraSchProApplyMapper.sel_IntraSchPro_closure_admin(sciIntraSchoolPro);
        for (SciIntraSchoolPro pro : list) {
            fillPageRenderData(pro);
        }
        return list;
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

    /**
     * 提交草稿到教研室审批
     * 参考论文模块实现，调用公有方法 approvalProcessService.submitApproval()
     * 公有方法中已包含后端身份校验和所有审批逻辑
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int subDraft(String id, Long userid, String state) {
        SciIntraSchoolPro apply = sciIntraSchProApplyMapper.sel_IntraSchPro_by_id(Integer.valueOf(id));
        if (apply == null) {
            return 0;
        }
        String currentState = normalizeTecTraState(apply.getState());
        SysUser user = sysUserService.selectUserById(userid);
        if (user == null) {
            return 0;
        }

        // 调用公有审批服务提交申请
        ApprovalRequest request = ApprovalRequest.of(TEC_TRA_PROCESS_CODE,
                Long.valueOf(id), currentState, "提交申请",
                userid, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.submitApproval(request);
        if (!result.isSuccess()) {
            return 0;
        }

        String newState = result.getNewState();
        int rows = sciIntraSchProApplyMapper.sch_hxPass(id, newState);
        if (rows > 0) {
            insertTecTraPiyue(id, userid, "成果转化-提交", "提交审批");
        }
        return rows;
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
     * 判断状态是否为新格式的TEC_TRA状态码
     */
    private boolean isTecTraState(String state) {
        if (state == null) return false;
        return state.startsWith("TEC_TRA_");
    }

    /**
     * 将旧数字状态码映射为新语义化状态编码（TEC_TRA_*格式）
     * 涵盖立项和结项两类流程的所有状态码
     */
    private String normalizeTecTraState(String state) {
        if (state == null || state.trim().isEmpty()) {
            return TEC_TRA_DRAFT;
        }
        // 已经是新状态码，直接返回
        if (isTecTraState(state)) {
            return state;
        }
        // 旧数字状态码映射（含立项和结项）
        switch (state) {
            // 立项状态码
            case "15": return TEC_TRA_DRAFT;
            case "1":  return TEC_TRA_JYS_AUDIT;
            case "11": return TEC_TRA_JYS_AUDIT;
            case "2":  return TEC_TRA_KYC_AUDIT;
            case "3":  return TEC_TRA_REJECTED;
            case "4":  return TEC_TRA_PASSED;
            case "5":  return TEC_TRA_REJECTED;
            case "12": return TEC_TRA_REJECTED;
            // 结项状态码 — 复用对应的立项状态语义
            case "16": return TEC_TRA_DRAFT;
            case "7":  return TEC_TRA_JYS_AUDIT;   // 结项教研室审批中
            case "13": return TEC_TRA_JYS_AUDIT;   // 结项教研室审批中
            case "8":  return TEC_TRA_KYC_AUDIT;   // 结项科研处审批中
            case "6":  return TEC_TRA_PASSED;       // 结项通过
            case "9":  return TEC_TRA_REJECTED;     // 结项驳回
            case "10": return TEC_TRA_REJECTED;     // 结项驳回
            case "14": return TEC_TRA_REJECTED;     // 结项驳回
            default: return state;
        }
    }

    private void insertTecTraPiyue(String id, Long uid, String state, String concate) {
        SciIntraSchProPiyue piyue = new SciIntraSchProPiyue();
        piyue.setUid(uid);
        piyue.setSchxktId(Integer.valueOf(id));
        piyue.setState(state);
        piyue.setConcate(concate == null || concate.trim().isEmpty() ? state : concate);
        sciIntraSchProPiyueMapper.insertIntraSchProPiyue(piyue);
    }

    /**
     * 填充页面渲染数据（statusMeta和actions）
     * 参考论文模块实现，提前计算当前用户权限并传入 fillPageRenderData()
     * 确保角色切换后按钮显隐正确。
     * 驳回态撤回按钮参照论文模块 addModuleSpecificActions()：
     * 调用公有方法 sysApprovalHistoryService.selectLastRejectByBusinessId()
     * 查询最近一次驳回记录，仅驳回操作人（或 admin）可见撤回按钮。
     *
     * @param pro 成果转化对象
     */
    private void fillPageRenderData(SciIntraSchoolPro pro) {
        if (pro == null) {
            return;
        }
        SysUser currentUser = ShiroUtils.getSysUser();
        String normalizedState = normalizeTecTraState(pro.getState());
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                MODULE_CODE,
                PERM_PREFIX,
                TEC_TRA_PROCESS_CODE,
                normalizedState,
                pro.getId() != null ? pro.getId().longValue() : null,
                pro.getUid() != null ? pro.getUid() : null,
                permissions
        );
        pro.setStatusMeta(result.getStatusMeta());

        // 驳回状态撤回按钮：参考论文模块，仅驳回操作人（或admin）可撤回
        // 调用公有方法 sysApprovalHistoryService 查审批记录，不修改公有方法
        List<PageRenderActionItem> actions = result.getActions() != null
                ? new ArrayList<>(result.getActions()) : new ArrayList<>();
        if (normalizedState != null && normalizedState.endsWith("_REJECTED")
                && pro.getId() != null) {
            boolean isAdmin = currentUser != null && currentUser.isAdmin();
            boolean isOwner = pro.getUid() != null
                    && currentUser != null
                    && pro.getUid().equals(currentUser.getUserId());
            boolean hasRevokePerm = permissions.contains("system:intraSch:JYCH");
            boolean hasKypyPerm = permissions.contains("system:intraSch:KYPY");
            boolean hasApprovePerm = permissions.contains("system:intraSch:JYPY");
            boolean isRejectOperator = false;
            try {
                SysApprovalHistory lastReject = sysApprovalHistoryService
                        .selectLastRejectByBusinessId(TEC_TRA_PROCESS_CODE,
                                pro.getId().longValue());
                if (lastReject != null && lastReject.getOperatorId() != null
                        && currentUser != null) {
                    isRejectOperator = lastReject.getOperatorId()
                            .equals(currentUser.getUserId());
                }
            } catch (Exception e) {
                // 查询失败时不追加撤回按钮
            }
            if (isAdmin || (isRejectOperator
                    && (hasRevokePerm || hasKypyPerm || hasApprovePerm || isOwner))) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL, "撤回",
                        PageRenderColorConstants.COLOR_WARNING, 40,
                        "确定要撤回该记录吗？"));
            }
        }
        pro.setActions(actions);
    }
}
