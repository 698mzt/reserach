package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.SciHorizontalReamountService;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 横向课题Service业务层处理
 *
 * @author zhansan
 * @date 2024-08-16
 */
@Slf4j  // 添加此注解
@Service
public class SciHorizontalApplyServiceImpl implements ISciHorizontalApplyService
{
    @Autowired
    private SciHorizontalApplyMapper sciHorizontalApplyMapper;
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;
    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;
    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;
    @Autowired
    private SciHorizontalApplyMapper sciIntraschproApplyMapper;
    @Autowired
    private SciHorizontalApplyMapper sciPaperAMapper;
    @Autowired
    private SciHorizontalApplyMapper sciJiaocairuanzhuMapper;
    @Autowired
    private SciHorizontalApplyMapper sciZhuanliruanzhuMapper;
    @Autowired
    private SciHorizontalApplyMapper sysRewardMapper;
    @Autowired
    private SciHorizontalApplyMapper sciLectureReportMapper;
    @Autowired
    private SciHorizontalReamountMapper sciHorizontalReamountMapper;
    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IPageRenderService pageRenderService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 填充页面渲染数据（状态展示信息和按钮动作列表）
     * 优先直接调用 PageRender 公共服务构建状态与动作，横向课题特有规则仍在本模块兜底补齐。
     *
     * @param apply 横向课题对象
     */
    private void fillPageRenderData(SciHorizontalApply apply) {
        if (apply == null) {
            return;
        }
        try {
            SysUser currentUser = ShiroUtils.getSysUser();
            List<String> permissions = buildCurrentPermissions(currentUser);
            List<String> roleKeys = buildCurrentRoleKeys(currentUser);
            String currentState = apply.getState();
            String moduleCode = determineModuleCode(currentState);

            PageRenderContext context = new PageRenderContext();
            context.setModuleCode(moduleCode);
            context.setBusinessId(apply.getId() != null ? apply.getId().longValue() : null);
            context.setCurrentState(currentState);
            context.setCreatorId(apply.getUserId() != null ? apply.getUserId().longValue() : null);
            context.setCurrentUser(currentUser);
            context.setPermissions(permissions);
            context.setRoleKeys(roleKeys);

            PageRenderStatusMeta statusMeta = buildHorizontalStatusMeta(context);
            List<PageRenderActionItem> actions = pageRenderService.buildActions(context);
            if (actions == null || actions.isEmpty()) {
                actions = buildHorizontalActions(context);
            }

            apply.setStatusMeta(statusMeta);
            apply.setActions(actions);
        } catch (Exception e) {
            log.error("填充横向课题页面渲染数据失败, applyId={}", apply.getId(), e);
            apply.setStatusMeta(buildFallbackStatusMeta(apply.getState()));
            apply.setActions(new ArrayList<>());
        }
    }

    @Override
    public String getHorizontalStateText(String state) {
        return buildFallbackStatusMeta(state).getStatusText();
    }

    private List<String> buildCurrentPermissions(SysUser currentUser) {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        Set<String> permsSet = sysMenuService.selectPermsByUserId(currentUser.getUserId());
        if (permsSet == null || permsSet.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(permsSet);
    }

    /**
     * 构建当前登录用户角色列表。
     *
     * @param currentUser 当前登录用户
     * @return 角色Key列表
     */
    private List<String> buildCurrentRoleKeys(SysUser currentUser) {
        if (currentUser == null || currentUser.getRoles() == null) {
            return new ArrayList<>();
        }
        return currentUser.getRoles().stream()
                .filter(Objects::nonNull)
                .map(SysRole::getRoleKey)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    /**
     * 构建横向课题状态展示信息。
     *
     * @param context 页面渲染上下文
     * @return 状态展示对象
     */
    private PageRenderStatusMeta buildHorizontalStatusMeta(PageRenderContext context) {
        if (context == null || StringUtils.isEmpty(context.getCurrentState())) {
            return PageRenderStatusMeta.of("", "未知", PageRenderColorConstants.COLOR_DEFAULT);
        }

        PageRenderStatusMeta pageRenderStatusMeta = pageRenderService.buildStatusMeta(context);
        if (pageRenderStatusMeta != null
                && StringUtils.isNotEmpty(pageRenderStatusMeta.getStatusText())
                && !StringUtils.equals(pageRenderStatusMeta.getStatusText(), pageRenderStatusMeta.getStatusCode())
                && !StringUtils.equals(pageRenderStatusMeta.getStatusText(), context.getCurrentState())) {
            pageRenderStatusMeta.setStatusCode(context.getCurrentState());
            return pageRenderStatusMeta;
        }
        return buildFallbackStatusMeta(context.getCurrentState());
    }

    /**
     * 构建横向课题按钮动作列表。
     * 先直接调用 PageRender 公共服务；若公共服务未返回横向课题动作，再按横向课题规则补齐。
     * 规则说明：
     * 1. 草稿：view / edit / remove / submit
     * 2. 驳回：view / edit / submit
     * 3. 教研室审批：view / viewProcess / 作者recall / review
     * 4. 科研处审批：view / viewProcess / 教研室recall / kyReview
     * 5. 通过：view / viewProcess / 科研处recall
     * 6. 若存在学院审批，则按现有横向流程插入为：view / viewProcess / 教研室recall / review（学院审批入口）
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> buildHorizontalActions(PageRenderContext context) {
        if (context == null || StringUtils.isEmpty(context.getCurrentState())) {
            return new ArrayList<>();
        }

        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = context.getCurrentState();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");
        boolean isResearchRole = context.hasRole("research");
        boolean canView = context.hasPermission("system:apply:info");
        boolean canEdit = context.hasPermission("system:apply:edit");
        boolean canRemove = context.hasPermission("system:apply:remove");
        boolean canAdd = context.hasPermission("system:apply:add");
        boolean canJysReview = context.hasPermission("system:apply:process") && isResearchRole;
        boolean canCollegeReview = false;
        boolean canKycReview = context.hasPermission("system:apply:hecha") && context.hasRole("sci_tesearch");

        if (canView) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW,
                    "查看",
                    PageRenderColorConstants.COLOR_INFO,
                    100));
        }

        if (isHorizontalDraftState(state)) {
            if ((isOwner || isAdmin) && canEdit) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_EDIT,
                        "编辑",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        10));
            }
            if ((isOwner || isAdmin) && canRemove) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_REMOVE,
                        "删除",
                        PageRenderColorConstants.COLOR_DANGER,
                        20,
                        isApplyState(state) ? "确定要删除该立项申请吗？" : "确定要删除该结项申请吗？"));
            }
            if ((isOwner || isAdmin) && canEdit) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_SUBMIT,
                        "提交",
                        PageRenderColorConstants.COLOR_SUCCESS,
                        30,
                        isApplyState(state) ? "确定要提交该立项申请吗？" : "确定要提交该结项申请吗？"));
            }
        } else if (isHorizontalRejectedState(state)) {
            if ((isOwner || isAdmin) && canEdit) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_EDIT,
                        "编辑",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        10));
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_SUBMIT,
                        "提交",
                        PageRenderColorConstants.COLOR_SUCCESS,
                        30,
                        isApplyState(state) ? "确定要重新提交该立项申请吗？" : "确定要重新提交该结项申请吗？"));
            }
        } else if (isJysAuditState(state)) {
            addViewProcessAction(actions, canView);
            if (canJysReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL,
                        "撤回",
                        PageRenderColorConstants.COLOR_WARNING,
                        20,
                        isApplyState(state) ? "确定要撤回该立项申请吗？" : "确定要撤回该结项申请吗？"));
            }
            if (canJysReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_REVIEW,
                        "审批",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        30));
            }
        } else if (isXyAuditState(state)) {
            addViewProcessAction(actions, canView);
            if (canJysReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL,
                        "撤回",
                        PageRenderColorConstants.COLOR_WARNING,
                        20,
                        isApplyState(state) ? "确定要撤回该立项申请吗？" : "确定要撤回该结项申请吗？"));
            }
            if (canCollegeReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_REVIEW,
                        "审批",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        30));
            }
        } else if (isKycAuditState(state)) {
            addViewProcessAction(actions, canView);
            boolean canRecallFromPreviousNode = hasXyNode(state) ? canCollegeReview : canJysReview;
            if (canRecallFromPreviousNode) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL,
                        "撤回",
                        PageRenderColorConstants.COLOR_WARNING,
                        20,
                        isApplyState(state) ? "确定要撤回该立项申请吗？" : "确定要撤回该结项申请吗？"));
            }
            if (canKycReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_KY_REVIEW,
                        "审批",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        30));
            }
        } else if (isHorizontalPassedState(state)) {
            addViewProcessAction(actions, canView);
            if (isApplyState(state) && (isOwner || isAdmin) && canAdd) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_OVER_APPLY,
                        "提交结项申请",
                        PageRenderColorConstants.COLOR_SUCCESS,
                        15));
            }
            if (isApplyState(state) && (isOwner || isAdmin) && canAdd) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_REAMOUNT,
                        "追加金额",
                        PageRenderColorConstants.COLOR_PRIMARY,
                        16));
            }
            if (canKycReview) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL,
                        "撤回",
                        PageRenderColorConstants.COLOR_WARNING,
                        20,
                        isApplyState(state) ? "确定要撤回该立项申请吗？" : "确定要撤回该结项申请吗？"));
            }
        }

        Collections.sort(actions);
        return actions;
    }

    /**
     * 添加“查看流程”按钮。
     *
     * @param actions 按钮列表
     * @param canView 是否具备查看权限
     */
    private void addViewProcessAction(List<PageRenderActionItem> actions, boolean canView) {
        if (!canView) {
            return;
        }
        actions.add(PageRenderActionItem.of(
                PageRenderActionConstants.ACTION_VIEW_PROCESS,
                "查看流程",
                PageRenderColorConstants.COLOR_INFO,
                110));
    }

    /**
     * 判断是否为立项流程状态。
     *
     * @param state 状态编码
     * @return 是否为立项流程
     */
    private boolean isApplyState(String state) {
        return StringUtils.startsWith(state, "APPLY_");
    }

    /**
     * 判断是否为横向草稿状态。
     *
     * @param state 状态编码
     * @return 是否为草稿
     */
    private boolean isHorizontalDraftState(String state) {
        return StringUtils.equalsAny(state, "APPLY_DRAFT", "OVER_DRAFT");
    }

    /**
     * 判断是否为横向驳回状态。
     *
     * @param state 状态编码
     * @return 是否为驳回
     */
    private boolean isHorizontalRejectedState(String state) {
        return StringUtils.equalsAny(state, "APPLY_REJECTED", "OVER_REJECTED");
    }

    /**
     * 判断是否为教研室审批状态。
     *
     * @param state 状态编码
     * @return 是否为教研室审批
     */
    private boolean isJysAuditState(String state) {
        return StringUtils.equalsAny(state, "APPLY_JYS_AUDIT", "OVER_JYS_AUDIT");
    }

    /**
     * 判断是否为学院审批状态。
     *
     * @param state 状态编码
     * @return 是否为学院审批
     */
    private boolean isXyAuditState(String state) {
        return StringUtils.equalsAny(state, "APPLY_XY_AUDIT", "OVER_XY_AUDIT");
    }

    /**
     * 判断是否为科研处审批状态。
     *
     * @param state 状态编码
     * @return 是否为科研处审批
     */
    private boolean isKycAuditState(String state) {
        return StringUtils.equalsAny(state, "APPLY_KYC_AUDIT", "OVER_KYC_AUDIT");
    }

    /**
     * 判断是否为横向通过状态。
     *
     * @param state 状态编码
     * @return 是否为通过
     */
    private boolean isHorizontalPassedState(String state) {
        return StringUtils.equalsAny(state, "APPLY_PASSED", "OVER_PASSED");
    }

    /**
     * 判断当前流程是否存在学院审批节点。
     * 科研处审批前一节点可能是教研室，也可能是学院；当前模块已存在 XY 审批状态，
     * 因此按现有横向流程默认识别为存在学院审批节点，不修改公共流程实现。
     *
     * @param state 状态编码
     * @return 是否存在学院审批节点
     */
    private boolean hasXyNode(String state) {
        return StringUtils.equalsAny(state, "APPLY_KYC_AUDIT", "OVER_KYC_AUDIT");
    }


    /**
     * 构建横向课题状态兜底展示信息。
     *
     * @param state 状态编码
     * @return 状态展示对象
     */
    private PageRenderStatusMeta buildFallbackStatusMeta(String state) {
        if (StringUtils.isEmpty(state)) {
            return PageRenderStatusMeta.of("", "未知", PageRenderColorConstants.COLOR_DEFAULT);
        }
        if ("APPLY_DRAFT".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项草稿", PageRenderColorConstants.COLOR_DEFAULT);
        }
        if ("APPLY_JYS_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项教研室审批中", PageRenderColorConstants.COLOR_WARNING);
        }
        if ("APPLY_XY_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项学院审批中", PageRenderColorConstants.COLOR_WARNING);
        }
        if ("APPLY_KYC_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项科研处审批中", PageRenderColorConstants.COLOR_INFO);
        }
        if ("APPLY_PASSED".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项已通过", PageRenderColorConstants.COLOR_SUCCESS);
        }
        if ("APPLY_REJECTED".equals(state)) {
            return PageRenderStatusMeta.of(state, "立项已驳回", PageRenderColorConstants.COLOR_DANGER);
        }
        if ("OVER_DRAFT".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项草稿", PageRenderColorConstants.COLOR_DEFAULT);
        }
        if ("OVER_JYS_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项教研室审批中", PageRenderColorConstants.COLOR_WARNING);
        }
        if ("OVER_XY_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项学院审批中", PageRenderColorConstants.COLOR_WARNING);
        }
        if ("OVER_KYC_AUDIT".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项科研处审批中", PageRenderColorConstants.COLOR_INFO);
        }
        if ("OVER_PASSED".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项已通过", PageRenderColorConstants.COLOR_SUCCESS);
        }
        if ("OVER_REJECTED".equals(state)) {
            return PageRenderStatusMeta.of(state, "结项已驳回", PageRenderColorConstants.COLOR_DANGER);
        }
        return PageRenderStatusMeta.of(state, state, PageRenderColorConstants.COLOR_DEFAULT);
    }


    /**
     * 根据状态编码确定模块编码
     * 立项流程状态以 APPLY_ 开头，使用 HORIZONTAL_APPLY
     * 结项流程状态以 OVER_ 开头，使用 HORIZONTAL_OVER
     *
     * @param state 状态编码
     * @return 模块编码
     */
    private String determineModuleCode(String state) {
        if (state != null && state.startsWith("OVER_")) {
            return "HORIZONTAL_OVER";
        }
        return "HORIZONTAL_APPLY";
    }

    /**
     * 将业务状态编码转换为审批节点编码
     * 业务状态编码格式: {阶段}_{节点}_{状态} (如 APPLY_JYS_AUDIT)
     * 审批节点编码格式: {阶段}_{节点} (如 APPLY_JYS)
     * 两者概念不同：状态编码表示业务数据当前所处状态，节点编码标识审批流程中的节点
     *
     * @param stateCode 业务状态编码
     * @return 审批节点编码
     */
    private String stateToNodeCode(String stateCode) {
        if (stateCode == null || stateCode.isEmpty()) {
            return stateCode;
        }
        if (stateCode.endsWith("_AUDIT")) {
            return stateCode.substring(0, stateCode.length() - 6);
        }
        return stateCode;
    }

    /**
     * 将审批节点编码转换为业务状态编码
     * 仅对审批中状态的节点编码添加 _AUDIT 后缀
     * 终态（PASSED/REJECTED）和草稿态（DRAFT）不加后缀
     *
     * @param nodeCode 审批节点编码
     * @return 业务状态编码
     */
    private String nodeCodeToState(String nodeCode) {
        if (nodeCode == null || nodeCode.isEmpty()) {
            return nodeCode;
        }
        if (nodeCode.endsWith("_PASSED") || nodeCode.endsWith("_REJECTED") || nodeCode.endsWith("_DRAFT")) {
            return nodeCode;
        }
        if (!nodeCode.endsWith("_AUDIT")) {
            return nodeCode + "_AUDIT";
        }
        return nodeCode;
    }

    /**
     * 查询横向课题
     *
     * @param id 横向课题主键
     * @return 横向课题
     */
    @Override
    public SciHorizontalApply selectSciHorizontalApplyById(Integer id)
    {
        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
        if (apply != null) {
            fillPageRenderData(apply);
        }
        return apply;
    }

    /**
     * 查询横向课题列表
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply)
    {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyList(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    /**
     * 统一查询横向课题列表（基于数据权限控制）
     * 所有管理员角色均可查看所有状态的课题数据，不再根据状态过滤可见性
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListAll(SciHorizontalApply sciHorizontalApply)
    {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListAll(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply)
    {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByJYS(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByJYS(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    /**
     * 查询结项申请列表（基于数据权限控制）
     * 所有管理员角色均可查看所有状态的结项申请数据，不再根据状态过滤可见性
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApply(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApply(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyJYS(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApplyJYS(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyKYC(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApplyKYC(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOVER(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOVERKYC(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOVERKYC(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }


    @Override
    public int overApply(String id, String state) {
        return sciHorizontalApplyMapper.overApply(id,state);
    }



    /**
     * 新增横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply)
    {
        // 新增：插入前查重
        SciHorizontalApply query = new SciHorizontalApply();
        query.setTopName(sciHorizontalApply.getTopName());
        query.setTopNumber(sciHorizontalApply.getTopNumber());
        List<SciHorizontalApply> existList = sciHorizontalApplyMapper.selectSciHorizontalApplyList(query);
        if (existList != null && !existList.isEmpty()) {
            // 课题名称或编号已存在，返回-1
            return -1;
        }
        sciHorizontalApplyMapper.insertSciHorizontalApply(sciHorizontalApply);

        //插入人员的排名
        Integer id = sciHorizontalApply.getId();
        SciHorizontalPersion sciHorizontalPersion = new SciHorizontalPersion();
        sciHorizontalPersion.setApplyid(id);
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFirstPersonId())) {
            sciHorizontalPersion.setRanking("1");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFirstPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getSecondPersonId())) {
            sciHorizontalPersion.setRanking("2");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getSecondPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getThirdPersonId())) {
            sciHorizontalPersion.setRanking("3");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getThirdPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFourthPersonId())) {
            sciHorizontalPersion.setRanking("4");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFourthPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        Integer applyid = sciHorizontalApply.getId() ;
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(Integer.valueOf(applyid));
        sciHorizontalPiyue.setConcate("新增数据");
        sciHorizontalPiyue.setState("新增");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return id;
    }




    /**
     * 修改横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSciHorizontalApply(SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);

        Integer id = sciHorizontalApply.getId();
        SciHorizontalPersion sciHorizontalPersion = new SciHorizontalPersion();
        sciHorizontalPersion.setApplyid(id);
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFirstPersonId()) ) {
            sciHorizontalApplyMapper.deletePersion(sciHorizontalPersion);
            sciHorizontalPersion.setRanking("1");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFirstPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getSecondPersonId())) {
            sciHorizontalPersion.setRanking("2");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getSecondPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getThirdPersonId())) {
            sciHorizontalPersion.setRanking("3");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getThirdPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFourthPersonId())) {
            sciHorizontalPersion.setRanking("4");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFourthPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        if (sciHorizontalApply.getNewsql().equals("99")) {
            sciHorizontalPiyue.setConcate("提交申请");
            sciHorizontalPiyue.setState("提交");
        } else if (sciHorizontalApply.getNewsql().equals("999")) {
            sciHorizontalPiyue.setConcate("提交申请");
            sciHorizontalPiyue.setState("提交");
        }
        else {
            sciHorizontalPiyue.setConcate("修改");
            sciHorizontalPiyue.setState("修改");
        }
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return '1';
    }

    /**
     * 批量删除横向课题
     * @param ids 需要删除的横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyByIds(String ids)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalApplyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除横向课题信息
     *
     * @param ids 横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyById(Integer ids)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalApplyById(ids);
    }

    /**
     * 删除横向课题另一个表信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalOverApplyById(Integer id)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalOverApplyById(id);
    }

    @Override
    public int hxPass(String id,Long uid,String urlFlag) {
        try {
            Integer applyId = Integer.valueOf(id);
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
            if (apply == null) {
                return 0;
            }
            SysUser user = userService.selectUserById(uid);
            if (user == null) {
                return 0;
            }
            List<String> roleKeys = user.getRoles() == null ? new ArrayList<>() : user.getRoles().stream()
                    .map(SysRole::getRoleKey)
                    .collect(Collectors.toList());
            List<Long> deptIds = buildOperatorDeptIds(user);
            ApprovalResult result = approveApply(
                    applyId,
                    apply.getState(),
                    uid,
                    user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "",
                    roleKeys,
                    deptIds,
                    null
            );
            return result.isSuccess() ? 1 : 0;
        } catch (Exception e) {
            log.error("横向课题立项审批通过失败: id={}, operatorId={}", id, uid, e);
            return 0;
        }
    }

    @Override
    public int amountPass(String id,String reid, Long uid, String urlFlag, List score, List persion, Integer applyId,String amountType) {
        String state = "0";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setApplyId(applyId.toString());
        if(urlFlag.equals("hecha")){
            state ="6";
//            以负责人列表大小为准，顺序匹配每个负责人所对应的分数，记录到sciUserScore中。
//            科研处
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                if (amountType.equals("1"))
                    sciUserScore.setChangeStatus("立项");
                if (amountType.equals("2"))
                    sciUserScore.setChangeStatus("结项");
                sciUserScoreMapper.insertScoreHistory(sciUserScore);
            }
//            教研室
        }else if(urlFlag.equals("pro")){
            state ="2";
//            学院通过
        }else if (urlFlag.equals("Dept")){
            state ="11";
        }
        int a =  sciHorizontalReamountService.amountpass(reid,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int hxover(String id,Long uid,String urlFlag) {
        try {
            Integer applyId = Integer.valueOf(id);
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
            if (apply == null) {
                return 0;
            }
            SysUser user = userService.selectUserById(uid);
            if (user == null) {
                return 0;
            }
            List<String> roleKeys = user.getRoles() == null ? new ArrayList<>() : user.getRoles().stream()
                    .map(SysRole::getRoleKey)
                    .collect(Collectors.toList());
            List<Long> deptIds = buildOperatorDeptIds(user);
            ApprovalResult result = approveOver(
                    applyId,
                    apply.getState(),
                    uid,
                    user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "",
                    roleKeys,
                    deptIds,
                    null
            );
            return result.isSuccess() ? 1 : 0;
        } catch (Exception e) {
            log.error("横向课题结项审批通过失败: id={}, operatorId={}", id, uid, e);
            return 0;
        }
    }

    @Override
    public int hxBh(String id,Long uid, String remark,String urlFlag) {
        try {
            Integer applyId = Integer.valueOf(id);
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
            if (apply == null) {
                return 0;
            }
            SysUser user = userService.selectUserById(uid);
            if (user == null) {
                return 0;
            }
            List<String> roleKeys = user.getRoles() == null ? new ArrayList<>() : user.getRoles().stream()
                    .map(SysRole::getRoleKey)
                    .collect(Collectors.toList());
            List<Long> deptIds = buildOperatorDeptIds(user);
            ApprovalResult result = rejectApply(
                    applyId,
                    apply.getState(),
                    uid,
                    user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "",
                    roleKeys,
                    deptIds,
                    remark
            );
            return result.isSuccess() ? 1 : 0;
        } catch (Exception e) {
            log.error("横向课题立项审批驳回失败: id={}, operatorId={}", id, uid, e);
            return 0;
        }
    }

    @Override
    public int hxoverBh(String id, Long uid, String remark, String urlFlag) {
        try {
            Integer applyId = Integer.valueOf(id);
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
            if (apply == null) {
                return 0;
            }
            SysUser user = userService.selectUserById(uid);
            if (user == null) {
                return 0;
            }
            List<String> roleKeys = user.getRoles() == null ? new ArrayList<>() : user.getRoles().stream()
                    .map(SysRole::getRoleKey)
                    .collect(Collectors.toList());
            List<Long> deptIds = buildOperatorDeptIds(user);
            ApprovalResult result = rejectOver(
                    applyId,
                    apply.getState(),
                    uid,
                    user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "",
                    roleKeys,
                    deptIds,
                    remark
            );
            return result.isSuccess() ? 1 : 0;
        } catch (Exception e) {
            log.error("横向课题结项审批驳回失败: id={}, operatorId={}", id, uid, e);
            return 0;
        }
    }



    @Override
//    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply) {
        String role = sciHorizontalApply.getRole();
        String tableId = sciHorizontalApply.getTableId();
        List<SciHorizontalApply> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
//        教研室
        else if(role.equals("research")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
//        教师
        else {
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
        return list;
    }

    //     申请结项流程
    @Override
    public int overSaveSciHorizontalApply(SciHorizontalApply sciHorizontalApply) {
        // 校验结项日期必须在立项日期后
        String signingData = sciHorizontalApply.getSigningData();
        String validityDate = sciHorizontalApply.getValidityDate();
        if (signingData != null && validityDate != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(signingData);
                Date end = sdf.parse(validityDate);
                if (!end.after(start)) {
                    return -1;
                }
            } catch (Exception e) {
                return -2;
            }
        }
//            获取到账金额
        String creditedAmountValue = sciHorizontalApplyMapper.selectCreditedAmountById(sciHorizontalApply.getId());
        if (creditedAmountValue != null && StringUtils.isNotEmpty(creditedAmountValue)){
            try {
//                新增总金额
                BigDecimal credited = new BigDecimal(creditedAmountValue);
                if (sciHorizontalApply.getReAmount() != null && StringUtils.isNotEmpty(sciHorizontalApply.getReAmount())){
                    credited = credited.add(new BigDecimal(sciHorizontalApply.getReAmount()));
                }
//                项目金额
                BigDecimal applied = new BigDecimal(sciHorizontalApply.getAmount());
            } catch (NumberFormatException e) {
                return -2; // 解析失败
            }
        }
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(sciHorizontalApply.getId());
        sciHorizontalPiyue.setConcate("提交结项");
        sciHorizontalPiyue.setState("提交");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.exportSciHorizontalApplyList(sciHorizontalApply);
    }

    @Override
    public int deletePersionByid(String ids) {
        return sciHorizontalApplyMapper.deletePersionByid(Convert.toStrArray(ids));
    }

    @Override
    @Transactional
    public void saveApplyPersons(Integer applyId, List<String> personIds) {
        if (applyId == null) {
            return;
        }

        // 先清空原有成员
        SciHorizontalPersion del = new SciHorizontalPersion();
        del.setApplyid(applyId);
        sciHorizontalApplyMapper.deletePersion(del);

        // 按顺序插入，ranking 从 1 开始
        SciHorizontalPersion rec = new SciHorizontalPersion();
        rec.setApplyid(applyId);
        for (int i = 0; i < personIds.size(); i++) {
            rec.setRanking(String.valueOf(i + 1));
            rec.setPersionid(personIds.get(i));
            sciHorizontalApplyMapper.insertPersion(rec);
        }
    }

    @Override
    public java.util.List<String> selectPersionIdsByApplyId(Integer applyId) {
        return sciHorizontalApplyMapper.selectPersionIdsByApplyId(applyId);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByDept(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByDept(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverDept(SciHorizontalApply sciHorizontalApply) {
        List<SciHorizontalApply> list = sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverDept(sciHorizontalApply);
        list.forEach(this::fillPageRenderData);
        return list;
    }

    @Override
    public int sci_horizontal_piyue(Integer id) {
        return sciHorizontalApplyMapper.sci_horizontal_piyue(id);
    }

    @Override
    public int recall(Integer id, String state,Long uid, String remark, String urlFlag) {
        try {
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
            if (apply == null) {
                return 0;
            }
            SysUser user = userService.selectUserById(uid);
            if (user == null) {
                return 0;
            }
            String currentState = apply.getState();
            ApprovalResult result;
            if (StringUtils.startsWith(currentState, "APPLY_")) {
                result = recallApply(
                        id,
                        currentState,
                        uid,
                        user.getUserName(),
                        user.getDept() != null ? user.getDept().getDeptName() : "",
                        remark
                );
            } else if (StringUtils.startsWith(currentState, "OVER_")) {
                result = recallOver(
                        id,
                        currentState,
                        uid,
                        user.getUserName(),
                        user.getDept() != null ? user.getDept().getDeptName() : "",
                        remark
                );
            } else {
                return 0;
            }
            return result.isSuccess() ? 1 : 0;
        } catch (Exception e) {
            log.error("横向课题撤回失败: id={}, operatorId={}", id, uid, e);
            return 0;
        }
    }

    @Override
    public int amountBh(String id, String reid, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("pro")){
            state ="3";
        }else if(urlFlag.equals("hecha")){
            state ="5";
        }else if (urlFlag.equals("Dept")){
            state ="22";
        }
        int a =  sciHorizontalReamountService.amountpass(reid,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
        sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public List<creditedAmount> selectCreditedAmount() {
        return sciHorizontalApplyMapper.selectCreditedAmount();
    }

    private List<Long> buildOperatorDeptIds(SysUser user) {
        List<Long> deptIds = new ArrayList<>();
        if (user == null) {
            return deptIds;
        }
        if (user.getDeptId() != null) {
            deptIds.add(user.getDeptId());
        }
        if (user.getDept() != null && user.getDept().getParentId() != null) {
            deptIds.add(user.getDept().getParentId());
        }
        return deptIds;
    }

    /**
     * 提交课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */


    private static final Set<String> COLLEGE_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher", // 软件学院管理员
            "discuss_college", // 商学院管理员
            "dzgc_college", // 电子工程学院管理员
            "yssj_college", // 艺术设计学院管理员
            "student_college", // 学生处学院管理员
            "marxism_college", // 马克思主义学院管理员
            "general" //综合院部管理员
    ));
    @Override
    public Map<String, Integer> getTodoCount(SysUser user) {
        Map<String, Integer> counts = new HashMap<>();
        String mainRole = determineMainRole(user.getRoles());

        int horizontalCount = calculateHorizontalTodoCount(user, mainRole);
        int reamountCount = calculateReamountTodoCount(user, mainRole);

        counts.put("horizontal", horizontalCount + reamountCount);
        counts.put("vertical", calculateVerticalTodoCount(user, mainRole));
        counts.put("achievement", calculateAchievementTodoCount(user, mainRole));
        counts.put("paper", calculatePaperTodoCount(user, mainRole)); // 统计论文待办数量
        counts.put("textbook", calculateTextbookTodoCount(user, mainRole)); // 统计教材软著待办数量
        counts.put("patent", calculatePatentTodoCount(user, mainRole)); // 统计专利软著待办数量
        counts.put("reward", calculateRewardTodoCount(user, mainRole)); // 统计奖励待办数量
        counts.put("lecture", calculateLectureTodoCount(user, mainRole)); // 统计讲座报告待办数量
        return counts;
    }
    /**
     * 计算金额审批待办数量
     */
    private int calculateReamountTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount2 = countReamountByStates(Arrays.asList("11", "33"));
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount1 = countReamountByDeptAndStates(collegeDeptId, Arrays.asList("2", "8"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount = countReamountByDeptAndStates(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
        }
        return count;
    }

    // 金额审批的个人待办统计
    private int countPersonalReamountTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countReamountByUserAndStates(userId, states, getCurrentYear());
    }

    // 金额审批的部门统计（包含子部门）
    private int countReamountByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countReamountByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countReamountByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    // 金额审批的状态统计
    private int countReamountByStates(List<String> states) {
        return sciHorizontalApplyMapper.countReamountByStates(states, getCurrentYear());
    }

    /**
     * 确定用户的主要角色
     */
    private String determineMainRole(List<SysRole> roles) {
        // 按角色优先级顺序判断
        if (hasRole(roles, "sci_tesearch")) return "sci_tesearch"; //科研处
//        if (hasRole(roles, "dept_teacher")) return "dept_teacher"; // 学院
        if (roles.stream().anyMatch(role -> COLLEGE_ROLES.contains(role.getRoleKey()))) { return "dept_teacher";}
        if (hasRole(roles, "research")) return "research";  //  教研室
        return "teacher";
    }

    private boolean hasRole(List<SysRole> roles, String roleKey) {
        return roles.stream().anyMatch(r -> roleKey.equals(r.getRoleKey()));
    }

    /**
     * 计算横向课题待办数量
     */
    private int countApprovalItems(List<String> states) {
        return sciHorizontalApplyMapper.countByStates(states, getCurrentYear());
    }

    //    private int countDeptApprovals(Long deptId, List<String> states) {
//        return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
//    }
// 修改后的部门统计方法
//    private int countDeptApprovals(Long deptId, List<String> states, String roleKey) {
//        return isCollegeLevelRole(roleKey) ?
//                sciHorizontalApplyMapper.countByDeptAndStatesWithChildren(deptId, states, getCurrentYear()) :
//                sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
//    }

    // 判断学院级角色
//    private boolean isCollegeLevelRole(String roleKey) {
//        return Arrays.asList("dept_teacher", "sci_tesearch").contains(roleKey);
//    }

    private int countResearchApprovals(Long deptId, List<String> states) {
        return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
    }

    private int countPersonalTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countByUserAndStates(userId, states, getCurrentYear());
    }

    private String getCurrentYear() {
        return String.valueOf(Year.now().getValue());
    }
    //    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
//        int count;
//        switch (roleKey) {
//            case "sci_tesearch":
////                count = countApprovalItems(Arrays.asList("4", "11"));
////                break;
//                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"));//部门
//                count = personalCount2 + deptCount2;
//                break;
//            case "dept_teacher":
//                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount1 = countDeptApprovals(user.getDeptId(), Arrays.asList("2", "1451"));//部门
//                count = personalCount1 + deptCount1;
//                break;
//            case "research":
//                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "4656"));//部门
//                count = personalCount + deptCount;
//                break;
//            default:
//                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                break;
//        }
//        return count;
//    }
//    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
//        List<String> states;
//        int count = 0;
//
//        // 根据角色获取状态列表
//        switch (roleKey) {
//            case "sci_tesearch":
//                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"), roleKey);
//                count = personalCount2 + deptCount2;
//                break;
//            case "dept_teacher":
//                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount1 = countDeptApprovals(user.getDeptId(), Arrays.asList("2", "1451"), roleKey);
//                count = personalCount1 + deptCount1;
//                break;
//            case "research":
//                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "4656"), roleKey);
//                count = personalCount + deptCount;
//                break;
//            default:
//                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//        }
//        return count;
//    }
    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"), roleKey);
                // 部门待办：所有状态为11的项目（学院通过后提交到科研处）
//                int deptCount2 = sciHorizontalApplyMapper.countByState("11", getCurrentYear());
                int deptCount2 = sciHorizontalApplyMapper.countByStates(Arrays.asList("11", "33"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                // 个人待办：教师自己提交的项目
                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
                // 部门待办：包含所有子教研室的状态为2的项目
                int deptCount1 = countDeptApprovals(collegeDeptId, Arrays.asList("2", "8"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
        }
        return count;
    }

    // 优化后的部门审批统计方法
    private int countDeptApprovals(Long deptId, List<String> states, String roleKey) {
        // 学院和科研处角色需要递归查询子部门
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        }
        // 教研室只需查询本部门
        else {
            return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    // 判断学院级角色
    private boolean isCollegeLevelRole(String roleKey) {
        return "dept_teacher".equals(roleKey) || "sci_tesearch".equals(roleKey);
    }



    //8.31纵向课题待办
    // 添加纵向课题统计方法
    private int calculateVerticalTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount2 = sciHorizontalApplyMapper.countVerticalByStates(Arrays.asList("4", "44"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount1 = countVerticalByDeptAndStates(collegeDeptId, Arrays.asList("2", "22"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount = countVerticalByDeptAndStates(user.getDeptId(), Arrays.asList("1", "11"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
        }
        return count;
    }

    // 添加纵向课题的个人待办统计
    private int countPersonalVerticalTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countVerticalByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加纵向课题的部门统计
    private int countVerticalByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countVerticalByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countVerticalByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //8.31论文待办
    // 添加论文待办统计方法
    private int calculatePaperTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount2 = sciHorizontalApplyMapper.countPaperByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount1 = countPaperByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount = countPaperByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
        }
        return count;
    }

    // 添加论文的个人待办统计
    private int countPersonalPaperTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countPaperByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加论文的部门统计
    private int countPaperByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countPaperByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countPaperByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.6成果转化待办
    // 添加成果转化待办统计方法
    private int calculateAchievementTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount2 = sciHorizontalApplyMapper.countAchievementByStates(Arrays.asList("2", "8"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount1 = countAchievementByDeptAndStates(collegeDeptId, Arrays.asList("11", "13"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount = countAchievementByDeptAndStates(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
        }
        return count;
    }

    // 添加成果转化的个人待办统计
    private int countPersonalAchievementTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countAchievementByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加成果转化的部门统计
    private int countAchievementByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countAchievementByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countAchievementByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15教材软著
    // 添加教材软著待办统计方法
    private int calculateTextbookTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countTextbookByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount1 = countTextbookByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount = countTextbookByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
        }
        return count;
    }

    // 添加教材软著的个人待办统计
    private int countPersonalTextbookTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countTextbookByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加教材软著的部门统计
    private int countTextbookByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countTextbookByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countTextbookByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15专利软著
    // 添加专利软著待办统计方法
    private int calculatePatentTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countPatentByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount1 = countPatentByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount = countPatentByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
        }
        return count;
    }

    // 添加专利软著的个人待办统计
    private int countPersonalPatentTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countPatentByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加专利软著的部门统计
    private int countPatentByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countPatentByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countPatentByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15 奖励
    // 添加奖励待办统计方法
    private int calculateRewardTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countRewardByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount1 = countRewardByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount = countRewardByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
        }
        return count;
    }

    // 添加奖励的个人待办统计
    private int countPersonalRewardTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countRewardByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加奖励的部门统计
    private int countRewardByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countRewardByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countRewardByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.14 讲座报告
    // 添加讲座报告待办统计方法
    private int calculateLectureTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount2 = sciHorizontalApplyMapper.countLectureByStates(Arrays.asList("2"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount1 = countLectureByDeptAndStates(collegeDeptId, Arrays.asList("6"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount = countLectureByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
        }
        return count;
    }

    // 添加讲座报告的个人待办统计
    private int countPersonalLectureTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countLectureByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加讲座报告的部门统计
    private int countLectureByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countLectureByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countLectureByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    @Override
    public int countVerticalApply(SysUser user) {
        String mainRole = determineMainRole(user.getRoles());
        int count = calculateVerticalTodoCount(user, mainRole);
        return count;
    }

    @Override
    public int countVerticalAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4", "6", "11", "22", "44");
        int count = countPersonalVerticalTodos(user.getUserId(), auditStates);
        return count;
    }

    @Override
    public int countVerticalComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("66");
        int count = countPersonalVerticalTodos(user.getUserId(), completeStates);
        return count;
    }

    //****************************************************************************************
    // 横向课题统计实现
    @Override
    public int countHorizontalApply(SysUser user) {
        return sciHorizontalApplyMapper.countHorizontalApplyByUser(user.getUserId());
    }

    @Override
    public int countHorizontalAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "11", "4", "7", "8", "33");
        return sciHorizontalApplyMapper.countHorizontalApplyByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countHorizontalComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciHorizontalApplyMapper.countHorizontalApplyByUserAndStates1(user.getUserId(), completeStates);
    }

    // 成果转化统计实现
    @Override
    public int countAchievementApply(SysUser user) {
        return sciIntraschproApplyMapper.countIntraschproApplyByUser(user.getUserId());
    }

    @Override
    public int countAchievementAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "11", "2", "4", "7", "13", "8");
        return sciIntraschproApplyMapper.countIntraschproApplyByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countAchievementComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciIntraschproApplyMapper.countIntraschproApplyByUserAndStates1(user.getUserId(), completeStates);
    }

    // 论文统计实现
    @Override
    public int countPaperApply(SysUser user) {
        return sciPaperAMapper.countPaperAByUser(user.getUserId());
    }

    @Override
    public int countPaperAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciPaperAMapper.countPaperAByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countPaperComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("8");
        return sciPaperAMapper.countPaperAByUserAndStates1(user.getUserId(), completeStates);
    }

    // 教材软著统计实现
    @Override
    public int countTextbookApply(SysUser user) {
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUser(user.getUserId());
    }

    @Override
    public int countTextbookAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countTextbookComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUserAndStates1(user.getUserId(), completeStates);
    }

    // 专利软著统计实现
    @Override
    public int countPatentApply(SysUser user) {
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUser(user.getUserId());
    }

    @Override
    public int countPatentAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countPatentComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUserAndStates1(user.getUserId(), completeStates);
    }

    // 奖励统计实现
    @Override
    public int countRewardApply(SysUser user) {
        return sysRewardMapper.countRewardByUser(user.getUserId());
    }

    @Override
    public int countRewardAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sysRewardMapper.countRewardByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countRewardComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sysRewardMapper.countRewardByUserAndStates1(user.getUserId(), completeStates);
    }

    // 讲座报告统计实现
    @Override
    public int countLectureApply(SysUser user) {
        return sciLectureReportMapper.countLectureReportByUser(user.getUserId());
    }

    @Override
    public int countLectureAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "6", "2");
        return sciLectureReportMapper.countLectureReportByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countLectureComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("4");
        return sciLectureReportMapper.countLectureReportByUserAndStates1(user.getUserId(), completeStates);
    }
    /**
     * 提交课题
     *
     * @return 结果
     */

    @Override
    public List<SciHorizontalApply> getStatsQuery(Map<String, String> params) {
        return sciHorizontalApplyMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciHorizontalApplyMapper.getStatsQueryToCheck(params);
    }

    /**
     * 删除被驳回的到账金额
     * */
    @Override
    public int removeAmount(String id, String reid, Long userId, String remark, String urlFlag) {
        return sciHorizontalReamountMapper.reamountremove(Integer.valueOf(reid));
    }

    /**
     * 功能描述：重新计算科研分
     * 涉及SQL：SELECT FROM sci_horizontal_apply / sci_project_score_cfg / sci_user_score
     * @param ids 课题ID列表，多个ID用逗号分隔
     * @param operatorId 操作人ID
     * @return 成功重新计算的课题数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recalculateScore(String ids, Long operatorId) {
        String[] idArray = ids.split(",");
        int successCount = 0;
        
        for (String idStr : idArray) {
            try {
                Integer id = Integer.valueOf(idStr.trim());
                SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
                if (apply == null) {
                    continue;
                }
                
                String amountStr = apply.getCreditedAmount();
                if (amountStr == null || amountStr.isEmpty()) {
                    amountStr = apply.getAmount();
                }
                if (amountStr == null || amountStr.isEmpty()) {
                    continue;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    continue;
                }
                
                List<String> personIds = selectPersionIdsByApplyId(id);
                if (personIds == null || personIds.isEmpty()) {
                    continue;
                }
                
                List<Integer> scores = calculateScoresByAmount(amount, personIds.size());
                
                sciUserScoreMapper.deleteScoreById(String.valueOf(id), "立项");
                
                for (int i = 0; i < personIds.size() && i < scores.size(); i++) {
                    SciUserScore userScore = new SciUserScore();
                    userScore.setApplyId(String.valueOf(id));
                    userScore.setUserId(personIds.get(i));
                    userScore.setChangeValue(String.valueOf(scores.get(i)));
                    userScore.setChangeStatus("重新计算");
                    sciUserScoreMapper.insertScoreHistory(userScore);
                }
                
                successCount++;
            } catch (Exception e) {
                log.error("重新计算课题{}科研分失败: {}", idStr, e.getMessage());
            }
        }
        
        return successCount;
    }

    /**
     * 功能描述：获取科研分计算预览
     * 涉及SQL：SELECT FROM sci_horizontal_apply / sci_project_score_cfg
     * @param id 课题ID
     * @return 预览结果Map
     */
    @Override
    public Map<String, Object> previewScore(Integer id) {
        Map<String, Object> result = new HashMap<>();
        
        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
        if (apply == null) {
            result.put("error", "课题不存在");
            return result;
        }
        
        String amountStr = apply.getCreditedAmount();
        if (amountStr == null || amountStr.isEmpty()) {
            amountStr = apply.getAmount();
        }
        
        result.put("topName", apply.getTopName());
        result.put("amount", apply.getAmount());
        result.put("creditedAmount", apply.getCreditedAmount());
        
        if (amountStr == null || amountStr.isEmpty()) {
            result.put("error", "未填写项目金额");
            return result;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            result.put("error", "金额格式错误");
            return result;
        }
        
        List<String> personIds = selectPersionIdsByApplyId(id);
        if (personIds == null || personIds.isEmpty()) {
            result.put("error", "未设置课题成员");
            return result;
        }
        
        List<Integer> scores = calculateScoresByAmount(amount, personIds.size());
        
        List<Map<String, Object>> memberScores = new ArrayList<>();
        for (int i = 0; i < personIds.size() && i < scores.size(); i++) {
            Map<String, Object> memberScore = new HashMap<>();
            memberScore.put("ranking", i + 1);
            memberScore.put("userId", personIds.get(i));
            memberScore.put("score", scores.get(i));
            memberScores.add(memberScore);
        }
        
        result.put("memberScores", memberScores);
        result.put("totalScore", scores.stream().mapToInt(Integer::intValue).sum());
        
        return result;
    }

    /**
     * 功能描述：根据金额计算科研分
     * 从数据库sci_project_score_cfg表读取配置数据
     * 
     * @param amount 项目金额（万元）
     * @param memberCount 成员数量
     * @return 各成员科研分列表
     */
    private List<Integer> calculateScoresByAmount(double amount, int memberCount) {
        List<Integer> scores = new ArrayList<>();
        
        // 从数据库查询配置数据
        SciProjectScoreCfg query = new SciProjectScoreCfg();
        query.setProjectType("H"); // H-横向课题
        List<SciProjectScoreCfg> allConfigs = sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(query);
        
        if (allConfigs != null && !allConfigs.isEmpty()) {
            // 使用数据库配置计算
            for (int rank = 1; rank <= Math.min(memberCount, 4); rank++) {
                scores.add(calculateHorizontalScoreByAmount(amount, rank, allConfigs));
            }
        } else {
            // 如果数据库没有配置，使用默认值
            int[] defaultScores = getDefaultScores(amount);
            int count = Math.min(memberCount, 4);
            for (int i = 0; i < count; i++) {
                scores.add(defaultScores[i]);
            }
        }
        
        return scores;
    }

    /**
     * 根据金额和排名计算横向课题积分
     * 从数据库sci_project_score_cfg表读取配置数据
     * 
     * @param amount 项目金额（万元）
     * @param rank 排名（1-4）
     * @param allConfigs 所有配置数据
     * @return 科研分
     */
    private Integer calculateHorizontalScoreByAmount(double amount, int rank, List<SciProjectScoreCfg> allConfigs) {
        if (amount < 0) {
            return 0;
        }

        // 查找匹配的金额区间配置
        SciProjectScoreCfg matchedConfig = null;
        for (SciProjectScoreCfg config : allConfigs) {
            Double min = Double.valueOf(config.getFundsMin());
            Double max = config.getFundsMax() != null && !config.getFundsMax().isEmpty() 
                         ? Double.valueOf(config.getFundsMax()) : null;
            
            // 判断金额是否在当前区间
            if (amount >= min && (max == null || amount <= max)) {
                matchedConfig = config;
                break;
            }
        }

        // 如果没有匹配的配置，使用2万元以下按比例计算
        if (matchedConfig == null) {
            // 2万对应的基准分：排名第一80分，排名第二30分，排名第三20分，排名第四10分
            double ratio = amount / 2.0;
            switch (rank) {
                case 1: return (int) Math.round(80 * ratio);
                case 2: return (int) Math.round(30 * ratio);
                case 3: return (int) Math.round(20 * ratio);
                case 4: return (int) Math.round(10 * ratio);
                default: return 0;
            }
        }

        // 提取匹配配置的字段值为final变量，以便在lambda中使用
        final String matchedFundsMin = matchedConfig.getFundsMin();
        final String matchedFundsMax = matchedConfig.getFundsMax();

        // 获取该排名对应的配置
        List<SciProjectScoreCfg> rankConfigs = allConfigs.stream()
            .filter(c -> c.getFundsMin().equals(matchedFundsMin) && 
                        (matchedFundsMax == null || c.getFundsMax() == null || c.getFundsMax().equals(matchedFundsMax)) &&
                        c.getUserOrder() != null && Integer.valueOf(c.getUserOrder()) == rank)
            .collect(Collectors.toList());

        if (rankConfigs.isEmpty()) {
            return 0;
        }

        SciProjectScoreCfg rankConfig = rankConfigs.get(0);
        
        // 优先使用总分
        if (rankConfig.getTotalScore() != null && !rankConfig.getTotalScore().isEmpty()) {
            return Integer.valueOf(rankConfig.getTotalScore());
        }
        
        // 如果没有总分，使用开题得分和结题得分的平均值
        if (rankConfig.getStartScore() != null && !rankConfig.getStartScore().isEmpty() &&
            rankConfig.getEndScore() != null && !rankConfig.getEndScore().isEmpty()) {
            int startScore = Integer.valueOf(rankConfig.getStartScore());
            int endScore = Integer.valueOf(rankConfig.getEndScore());
            return (startScore + endScore) / 2;
        }

        return 0;
    }

    /**
     * 功能描述：根据金额获取默认科研分配置
     * @param amount 项目金额（万元）
     * @return 四个排名的科研分数组
     */
    private int[] getDefaultScores(double amount) {
        if (amount >= 100) {
            return new int[]{9880, 4460, 1780, 880};
        } else if (amount >= 75) {
            return new int[]{7400, 3360, 1340, 650};
        } else if (amount >= 50) {
            return new int[]{4360, 1980, 780, 380};
        } else if (amount >= 35) {
            return new int[]{2860, 1280, 520, 240};
        } else if (amount >= 20) {
            return new int[]{1520, 680, 280, 120};
        } else if (amount >= 10) {
            return new int[]{440, 200, 80, 40};
        } else if (amount >= 5) {
            return new int[]{220, 100, 40, 20};
        } else if (amount >= 2) {
            return new int[]{80, 30, 20, 10};
        } else if (amount > 0) {
            double ratio = amount / 2.0;
            return new int[]{
                (int) Math.round(80 * ratio),
                (int) Math.round(30 * ratio),
                (int) Math.round(20 * ratio),
                (int) Math.round(10 * ratio)
            };
        } else {
            return new int[]{0, 0, 0, 0};
        }
    }

    //*******************************************************************************************************
    // 审批流程相关方法实现（集成IApprovalProcessService）

    @Autowired
    private IApprovalProcessService approvalProcessService;

    /**
     * 提交立项申请审批
     * <p>
     * 通过调用IApprovalProcessService.submitApproval()实现，
     * 确保状态流转的规范性和审批历史的完整记录。
     * </p>
     *
     * @param applyId 申请ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 提交说明（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult submitApplyApproval(Integer applyId, Long operatorId,
            String operatorName, String operatorDept, String comment) {
        if (applyId == null || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("立项申请不存在");
        }

        if (!"APPLY_DRAFT".equals(apply.getState())) {
            return ApprovalResult.fail("只有草稿状态的申请才能提交");
        }

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_APPLY",
                applyId.longValue(),
                apply.getState(),
                comment,
                operatorId,
                operatorName,
                operatorDept
        );

        ApprovalResult result = approvalProcessService.submitApproval(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("立项申请提交成功: applyId={}, newState={}, operator={}", 
                    applyId, correctedState, operatorName);
        } else {
            log.error("立项申请提交失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 立项申请审批通过
     * <p>
     * 通过调用IApprovalProcessService.approve()实现，
     * 自动根据节点配置流转到下一状态。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 审批意见（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult approveApply(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            List<String> operatorRoleKeys, List<Long> operatorDeptIds, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("立项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_APPLY",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept,
                operatorRoleKeys,
                operatorDeptIds
        );

        ApprovalResult result = approvalProcessService.approve(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("立项申请审批通过: applyId={}, currentState={}, newState={}, operator={}", 
                    applyId, currentState, correctedState, operatorName);
        } else {
            log.error("立项申请审批通过失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 立项申请审批驳回
     * <p>
     * 通过调用IApprovalProcessService.reject()实现，
     * 状态回退到草稿或上一节点。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 驳回原因
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult rejectApply(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            List<String> operatorRoleKeys, List<Long> operatorDeptIds, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("立项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_APPLY",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept,
                operatorRoleKeys,
                operatorDeptIds
        );

        ApprovalResult result = approvalProcessService.reject(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("立项申请审批驳回: applyId={}, currentState={}, newState={}, operator={}, reason={}", 
                    applyId, currentState, correctedState, operatorName, comment);
        } else {
            log.error("立项申请审批驳回失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 立项申请撤回
     * <p>
     * 通过调用IApprovalProcessService.recall()实现，
     * 状态回退到草稿。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 撤回原因（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult recallApply(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("立项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_APPLY",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept
        );

        ApprovalResult result = approvalProcessService.recall(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("立项申请撤回成功: applyId={}, currentState={}, newState={}, operator={}", 
                    applyId, currentState, correctedState, operatorName);
        } else {
            log.error("立项申请撤回失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 提交结项申请审批
     * <p>
     * 通过调用IApprovalProcessService.submitApproval()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 提交说明（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult submitOverApproval(Integer applyId, Long operatorId,
            String operatorName, String operatorDept, String comment) {
        if (applyId == null || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("结项申请不存在");
        }

        if (!"OVER_DRAFT".equals(apply.getState())) {
            return ApprovalResult.fail("只有草稿状态的申请才能提交");
        }

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_OVER",
                applyId.longValue(),
                apply.getState(),
                comment,
                operatorId,
                operatorName,
                operatorDept
        );

        ApprovalResult result = approvalProcessService.submitApproval(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("结项申请提交成功: applyId={}, newState={}, operator={}", 
                    applyId, correctedState, operatorName);
        } else {
            log.error("结项申请提交失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 结项申请审批通过
     * <p>
     * 通过调用IApprovalProcessService.approve()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 审批意见（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult approveOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            List<String> operatorRoleKeys, List<Long> operatorDeptIds, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("结项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_OVER",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept,
                operatorRoleKeys,
                operatorDeptIds
        );

        ApprovalResult result = approvalProcessService.approve(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("结项申请审批通过: applyId={}, currentState={}, newState={}, operator={}", 
                    applyId, currentState, correctedState, operatorName);
        } else {
            log.error("结项申请审批通过失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 结项申请审批驳回
     * <p>
     * 通过调用IApprovalProcessService.reject()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param operatorRoleKeys 操作人角色列表
     * @param operatorDeptIds 操作人部门ID列表
     * @param comment 驳回原因
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult rejectOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept,
            List<String> operatorRoleKeys, List<Long> operatorDeptIds, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("结项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_OVER",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept,
                operatorRoleKeys,
                operatorDeptIds
        );

        ApprovalResult result = approvalProcessService.reject(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("结项申请审批驳回: applyId={}, currentState={}, newState={}, operator={}, reason={}", 
                    applyId, currentState, correctedState, operatorName, comment);
        } else {
            log.error("结项申请审批驳回失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 结项申请撤回
     * <p>
     * 通过调用IApprovalProcessService.recall()实现。
     * </p>
     *
     * @param applyId 申请ID
     * @param currentState 当前状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param operatorDept 操作人部门
     * @param comment 撤回原因（可选）
     * @return 审批结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalResult recallOver(Integer applyId, String currentState,
            Long operatorId, String operatorName, String operatorDept, String comment) {
        if (applyId == null || StringUtils.isEmpty(currentState) || operatorId == null) {
            return ApprovalResult.fail("参数不完整");
        }

        SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(applyId);
        if (apply == null) {
            return ApprovalResult.fail("结项申请不存在");
        }

        if (!currentState.equals(apply.getState())) {
            return ApprovalResult.fail("申请状态已变更，请刷新后重试");
        }

        String nodeCode = stateToNodeCode(currentState);

        ApprovalRequest request = ApprovalRequest.of(
                "HORIZONTAL_OVER",
                applyId.longValue(),
                nodeCode,
                comment,
                operatorId,
                operatorName,
                operatorDept
        );

        ApprovalResult result = approvalProcessService.recall(request);
        
        if (result.isSuccess()) {
            String newState = result.getNewState();
            String correctedState = nodeCodeToState(newState);
            if (!correctedState.equals(newState)) {
                sciHorizontalApplyMapper.updateState(applyId, correctedState);
                result = ApprovalResult.ok(result.getMessage(), correctedState);
            }
            log.info("结项申请撤回成功: applyId={}, currentState={}, newState={}, operator={}", 
                    applyId, currentState, correctedState, operatorName);
        } else {
            log.error("结项申请撤回失败: applyId={}, reason={}", applyId, result.getMessage());
        }
        
        return result;
    }

    /**
     * 查询立项申请审批历史
     *
     * @param applyId 申请ID
     * @return 审批历史列表
     */
    @Override
    public List<SysApprovalHistory> getApplyApprovalHistory(Integer applyId) {
        if (applyId == null) {
            return new ArrayList<>();
        }
        return approvalProcessService.getApprovalHistory(
                "HORIZONTAL_APPLY", 
                applyId.longValue()
        );
    }

    /**
     * 查询结项申请审批历史
     *
     * @param applyId 申请ID
     * @return 审批历史列表
     */
    @Override
    public List<SysApprovalHistory> getOverApprovalHistory(Integer applyId) {
        if (applyId == null) {
            return new ArrayList<>();
        }
        return approvalProcessService.getApprovalHistory(
                "HORIZONTAL_OVER", 
                applyId.longValue()
        );
    }
}
