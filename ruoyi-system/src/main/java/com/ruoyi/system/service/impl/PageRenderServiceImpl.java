package com.ruoyi.system.service.impl;

import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.domain.PageRenderActionItem;
import com.ruoyi.system.domain.PageRenderContext;
import com.ruoyi.system.domain.PageRenderResult;
import com.ruoyi.system.domain.PageRenderStatusMeta;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.domain.SysApprovalState;
import com.ruoyi.system.mapper.SysApprovalNodeMapper;
import com.ruoyi.system.mapper.SysApprovalStateMapper;
import com.ruoyi.system.service.IPageRenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面渲染公共服务实现类
 * 实现统一的状态展示构造和按钮动作构造逻辑，论文模块作为首个试点
 * 状态映射和按钮规则从数据库审批流程配置中动态加载，避免硬编码
 *
 * @author ruoyi
 */
@Service
public class PageRenderServiceImpl implements IPageRenderService {

    private static final Logger log = LoggerFactory.getLogger(PageRenderServiceImpl.class);

    /** 横向课题立项审批 */
    private static final String HORIZONTAL_APPLY_PROCESS_CODE = "horizontal_apply";

    /** 横向课题结项审批 */
    private static final String HORIZONTAL_OVER_PROCESS_CODE = "horizontal_over";

    /** 纵向课题立项审批 */
    private static final String VERTICAL_APPLY_PROCESS_CODE = "vertical_apply";

    /** 纵向课题结项审批 */
    private static final String VERTICAL_OVER_PROCESS_CODE = "vertical_over";

    /** 论文审批流程 */
    private static final String PAPER_PROCESS_CODE = "paper_approval";

    /** 教材专著审批流程 */
    private static final String TEXTBOOK_APPROVAL_PROCESS_CODE = "textbook_approval";

    /** 成果转化审批 */
    private static final String INTRASCHPRO_APPLY_PROCESS_CODE = "intraschpro_apply";

    /** 奖励审批 */
    private static final String REWARD_APPLY_PROCESS_CODE = "reward_apply";

    /** 讲座报告审批 */
    private static final String LECTURE_APPROVAL_PROCESS_CODE = "lecture_approval";

    /** 专利软著审批 */
    private static final String PATENT_APPLY_PROCESS_CODE = "patent_apply";

    @Autowired
    private SysApprovalStateMapper approvalStateMapper;

    @Autowired
    private SysApprovalNodeMapper approvalNodeMapper;

    @Autowired
    private com.ruoyi.system.mapper.SysApprovalProcessMapper approvalProcessMapper;

    /** 论文模块状态映射配置（从数据库动态加载） */
    private final Map<String, StatusMapping> paperStatusMapping = new HashMap<>();

    /** 论文模块审批节点配置（从数据库动态加载） */
    private final Map<String, SysApprovalNode> paperNodeMapping = new HashMap<>();

    /**
     * 应用启动时从数据库加载审批流程配置，初始化状态映射和节点映射
     * 避免硬编码状态，支持通过数据库配置动态调整
     */
    @PostConstruct
    public void init() {
        try {
            log.info("开始加载审批流程配置...");
            loadStatusMapping();
            loadNodeMapping();
            log.info("审批流程配置加载完成，状态数: {}, 节点数: {}", 
                    paperStatusMapping.size(), paperNodeMapping.size());
        } catch (Exception e) {
            log.error("加载审批流程配置失败，将使用空配置", e);
        }
    }

    /**
     * 从数据库加载状态映射配置
     * 查询paper_approval流程下所有启用的状态，构建状态编码到展示信息的映射
     */
    private void loadStatusMapping() {
        paperStatusMapping.clear();
        
        // 查询论文流程的所有状态
        List<SysApprovalState> states = approvalStateMapper.selectSysApprovalStateByProcessCode(PAPER_PROCESS_CODE);
        if (states == null || states.isEmpty()) {
            log.warn("未找到流程 {} 的状态配置，请检查sys_approval_state表", PAPER_PROCESS_CODE);
            return;
        }
        
        for (SysApprovalState state : states) {
            // 只加载启用的状态（status=0）
            if ("0".equals(state.getStatus())) {
                String colorType = getColorTypeBySort(state.getSort());
                paperStatusMapping.put(state.getStateCode(), 
                        new StatusMapping(state.getStateName(), colorType, getSemanticByStateCode(state.getStateCode())));
            }
        }
    }

    /**
     * 从数据库加载审批节点配置
     * 查询paper_approval流程下所有启用的节点，构建节点编码到节点信息的映射
     */
    private void loadNodeMapping() {
        paperNodeMapping.clear();
        
        // 先根据流程编码查询流程ID，避免硬编码
        com.ruoyi.system.domain.SysApprovalProcess process = approvalProcessMapper.selectSysApprovalProcessByProcessCode(PAPER_PROCESS_CODE);
        if (process == null) {
            log.warn("未找到流程编码 {} 的配置，请检查sys_approval_process表", PAPER_PROCESS_CODE);
            return;
        }
        
        // 查询论文流程的所有节点
        List<SysApprovalNode> nodes = approvalNodeMapper.selectSysApprovalNodeByProcessId(process.getId());
        if (nodes == null || nodes.isEmpty()) {
            log.warn("未找到流程 {} 的节点配置，请检查sys_approval_node表", PAPER_PROCESS_CODE);
            return;
        }
        
        for (SysApprovalNode node : nodes) {
            // 只加载启用的节点（status=0）
            if ("0".equals(node.getStatus())) {
                paperNodeMapping.put(node.getNodeCode(), node);
            }
        }
    }

    /**
     * 刷新配置缓存（用于管理员修改配置后手动刷新）
     */
    public void refreshCache() {
        log.info("手动刷新审批流程配置缓存...");
        init();
    }

    /**
     * 根据排序号计算颜色类型
     * 排序号越小（越早的节点），颜色越浅；终态使用特定颜色
     *
     * @param sort 排序号
     * @return 颜色类型
     */
    private String getColorTypeBySort(Integer sort) {
        if (sort == null) {
            return PageRenderColorConstants.COLOR_DEFAULT;
        }
        if (sort <= 1) {
            return PageRenderColorConstants.COLOR_WARNING;
        }
        return PageRenderColorConstants.COLOR_PRIMARY;
    }

    /**
     * 根据状态编码获取通用语义
     * 从状态编码中提取语义信息，用于前端按钮展示
     *
     * @param stateCode 状态编码
     * @return 语义描述
     */
    private String getSemanticByStateCode(String stateCode) {
        if (stateCode == null) {
            return "未知";
        }
        if (stateCode.endsWith("_DRAFT")) {
            return "待提交";
        }
        if (stateCode.endsWith("_AUDIT")) {
            return "审批中";
        }
        if (stateCode.endsWith("_PASSED")) {
            return "通过";
        }
        if (stateCode.endsWith("_REJECTED")) {
            return "驳回";
        }
        return "未知";
    }

    /**
     * 构建状态展示信息
     * 根据模块编码与当前状态生成状态文案和颜色信息
     * 状态信息从数据库配置缓存中读取，避免硬编码
     *
     * @param context 页面渲染上下文
     * @return 状态展示对象
     */
    @Override
    public PageRenderStatusMeta buildStatusMeta(PageRenderContext context) {
        if (context == null || context.getCurrentState() == null) {
            return PageRenderStatusMeta.of("", "未知", PageRenderColorConstants.COLOR_DEFAULT);
        }

        Map<String, StatusMapping> mapping = getStatusMapping(context.getModuleCode());
        StatusMapping rule = mapping.get(context.getCurrentState());

        if (rule == null) {
            return PageRenderStatusMeta.of(context.getCurrentState(), context.getCurrentState(), PageRenderColorConstants.COLOR_DEFAULT);
        }

        return PageRenderStatusMeta.of(context.getCurrentState(), rule.getText(), rule.getColorType());
    }

    /**
     * 构建按钮动作列表
     * 根据状态、权限、当前用户身份生成按钮列表
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    @Override
    public List<PageRenderActionItem> buildActions(PageRenderContext context) {
        if (context == null) {
            return Collections.emptyList();
        }

        String moduleCode = context.getModuleCode();
        if ("PAPER".equals(moduleCode)) {
            return buildPaperActions(context);
        }

        // 其它模块暂返回空列表，后续按需扩展
        return Collections.emptyList();
    }

    /**
     * 构建页面渲染汇总结果
     * 统一入口方法，将状态展示信息与按钮列表组装为完整返回对象
     *
     * @param context      页面渲染上下文
     * @param businessData 业务数据
     * @param <T>          业务数据泛型
     * @return 页面渲染汇总结果
     */
    @Override
    public <T> PageRenderResult<T> buildRenderResult(PageRenderContext context, T businessData) {
        PageRenderStatusMeta statusMeta = buildStatusMeta(context);
        List<PageRenderActionItem> actions = buildActions(context);
        return PageRenderResult.of(statusMeta, actions, businessData);
    }

    /**
     * 构建论文模块按钮动作列表
     * 根据论文状态、用户角色、权限生成对应的按钮列表
     * 状态判断基于数据库加载的节点配置，避免硬编码状态值
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> buildPaperActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = context.getCurrentState();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");
        
        // 从数据库节点配置获取当前节点信息
        SysApprovalNode currentNode = paperNodeMapping.get(state);
        boolean isInAudit = currentNode != null && state.endsWith("_AUDIT");
        boolean isDraft = state.endsWith("_DRAFT");
        boolean isPassed = state.endsWith("_PASSED");
        boolean isRejected = state.endsWith("_REJECTED");

        // 查看按钮：所有状态都显示，权限 system:paper:info
        if (context.hasPermission("system:paper:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看",
                    PageRenderColorConstants.COLOR_INFO, 100));
        }

        // 查看流程按钮：非草稿状态显示，权限 system:paper:info
        if (!isDraft && context.hasPermission("system:paper:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程",
                    PageRenderColorConstants.COLOR_INFO, 101));
        }

        // 编辑按钮：草稿或驳回状态，作者本人或管理员，权限 system:paper:edit
        if ((isDraft || isRejected)
                && (isOwner || isAdmin) && context.hasPermission("system:paper:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑",
                    PageRenderColorConstants.COLOR_PRIMARY, 10));
        }

        // 删除按钮：草稿或驳回状态，作者本人或管理员，权限 system:paper:remove
        if ((isDraft || isRejected) && (isOwner || isAdmin)
                && context.hasPermission("system:paper:remove")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REMOVE, "删除",
                    PageRenderColorConstants.COLOR_DANGER, 20, "确定要删除该论文吗？"));
        }

        // 提交按钮：草稿状态，作者本人或管理员，权限 system:paper:edit
        if (isDraft && (isOwner || isAdmin)
                && context.hasPermission("system:paper:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要提交该论文吗？"));
        }

        // 驳回状态只显示编辑按钮，不显示重新提交按钮
        // 用户编辑保存后，状态会自动变为草稿，然后显示提交按钮

        // 审批节点按钮：处于审批中的节点，有审批权限的用户可以看到批阅/通过/驳回按钮
        // 注意：科研处审批节点(PAPER_KYC_AUDIT)由专门的科研核查按钮处理，此处排除
        boolean isKyAudit = SciPaperA.PAPER_KYC_AUDIT.equals(state);
        if (isInAudit && !isKyAudit && context.hasPermission("system:paper:process")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REVIEW, "批阅",
                    PageRenderColorConstants.COLOR_PRIMARY, 30));
            // 审批人进入批阅页面后需要"通过"和"驳回"按钮
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 31, "确定要通过该论文吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 32, "确定要驳回该论文吗？"));
        }

        // 审批中状态，作者可撤回（仅科研处审批状态，教研室审批状态不允许撤回）
        // isInAudit 包含教研室和科研处审批状态，需要排除教研室审批状态
        boolean isJysAudit = SciPaperA.PAPER_JYS_AUDIT.equals(state);
        if (isInAudit && !isJysAudit && isOwner
                && context.hasPermission("system:paper:revoke")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该论文吗？"));
        }

        // 科研核查按钮：科研处审批状态，有科研核查权限 system:paper:kypy
        if (SciPaperA.PAPER_KYC_AUDIT.equals(state) && context.hasPermission("system:paper:kypy")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_KY_REVIEW, "科研核查",
                    PageRenderColorConstants.COLOR_PRIMARY, 35));
            // 科研处审批人需要"通过"和"驳回"按钮
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 36, "确定要通过该论文吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 37, "确定要驳回该论文吗？"));
        }

        // 科研处审批状态，教研室审批人可撤回，权限 system:paper:revoke
        if (SciPaperA.PAPER_KYC_AUDIT.equals(state)
                && context.hasPermission("system:paper:revoke")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该论文吗？"));
        }

        // 通过状态，科研处可撤回，权限 system:paper:kyrevoke
        if (isPassed
                && context.hasPermission("system:paper:kyrevoke")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该论文吗？"));
        }

        // 按排序号排序
        Collections.sort(actions);

        return actions;
    }

    /**
     * 获取模块状态映射配置
     * 从数据库加载的缓存中获取状态映射，避免硬编码
     *
     * @param moduleCode 模块编码
     * @return 状态映射配置
     */
    private Map<String, StatusMapping> getStatusMapping(String moduleCode) {
        if ("PAPER".equals(moduleCode)) {
            return paperStatusMapping;
        }
        // 其它模块暂返回空映射，后续按需扩展
        return Collections.emptyMap();
    }

    /**
     * 状态映射内部类，封装状态文案、颜色类型、通用语义
     */
    private static class StatusMapping {
        private final String text;
        private final String colorType;
        private final String semantic;

        StatusMapping(String text, String colorType, String semantic) {
            this.text = text;
            this.colorType = colorType;
            this.semantic = semantic;
        }

        String getText() {
            return text;
        }

        String getColorType() {
            return colorType;
        }

        String getSemantic() {
            return semantic;
        }
    }
}
