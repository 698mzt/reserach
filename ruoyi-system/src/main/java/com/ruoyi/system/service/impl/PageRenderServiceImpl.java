package com.ruoyi.system.service.impl;

import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.domain.*;
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
 * 实现统一的状态展示构造和按钮动作构造逻辑，支持八大模块通用化
 * 状态映射和按钮规则从数据库审批流程配置中动态加载，避免硬编码
 *
 * 八大模块：论文、教材软著、专利软著、讲座报告、奖励、横向项目、纵向项目、成果转化
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

    /** 教材专著审批 */
    private static final String TEXTBOOK_APPROVAL_PROCESS_CODE = "textbook_approval";

    /** 成果转化审批 */
    private static final String INTRASCHPRO_APPLY_PROCESS_CODE = "intraschpro_apply";

    /** 奖励审批 */
    private static final String REWARD_APPLY_PROCESS_CODE = "reward_apply";

    /** 讲座报告审批 */
    private static final String LECTURE_APPROVAL_PROCESS_CODE = "lecture_approval";

    /** 专利软著审批 */
    private static final String PATENT_APPLY_PROCESS_CODE = "patent_apply";

    /**
     * 模块配置内部类，封装单个模块的渲染配置信息
     * 包含流程编码、权限前缀、模块名称等，用于通用化按钮规则构造
     */
    private static class ModuleConfig {
        /** 流程编码（用于查询数据库状态/节点配置） */
        private final String processCode;
        /** 权限前缀（如 system:paper:、system:reward:） */
        private final String permissionPrefix;
        /** 模块名称（用于日志输出） */
        private final String moduleName;

        ModuleConfig(String processCode, String permissionPrefix, String moduleName) {
            this.processCode = processCode;
            this.permissionPrefix = permissionPrefix;
            this.moduleName = moduleName;
        }

        String getProcessCode() {
            return processCode;
        }

        String getPermissionPrefix() {
            return permissionPrefix;
        }

        String getModuleName() {
            return moduleName;
        }

        /**
         * 拼接完整权限标识
         *
         * @param action 权限动作（如 info、edit、remove）
         * @return 完整权限标识（如 system:paper:info）
         */
        String buildPermission(String action) {
            return permissionPrefix + action;
        }
    }

    @Autowired
    private SysApprovalStateMapper approvalStateMapper;

    /** 八大模块配置注册表（模块编码 -> 模块配置） */
    private final Map<String, ModuleConfig> moduleConfigs = new HashMap<>();

    /** 各模块状态映射配置（模块编码 -> 状态编码 -> 状态映射） */
    private final Map<String, Map<String, StatusMapping>> allStatusMappings = new HashMap<>();

    /**
     * 应用启动时从数据库加载审批流程配置，初始化所有模块的状态映射
     * 避免硬编码状态，支持通过数据库配置动态调整
     */
    @PostConstruct
    public void init() {
        try {
            log.info("开始加载八大模块审批流程配置...");
            registerModuleConfigs();
            loadAllStatusMappings();
            log.info("八大模块审批流程配置加载完成，模块数: {}, 总状态数: {}",
                    moduleConfigs.size(), countAllStatusMappings());
        } catch (Exception e) {
            log.error("加载八大模块审批流程配置失败，将使用空配置", e);
        }
    }

    /**
     * 注册八大模块配置
     * 每个模块注册流程编码和权限前缀，用于通用化按钮规则构造
     */
    private void registerModuleConfigs() {
        moduleConfigs.put("PAPER", new ModuleConfig(PAPER_PROCESS_CODE, "system:paper:", "论文"));
        moduleConfigs.put("REWARD", new ModuleConfig(REWARD_APPLY_PROCESS_CODE, "system:reward:", "奖励"));
        moduleConfigs.put("TEXTBOOK", new ModuleConfig(TEXTBOOK_APPROVAL_PROCESS_CODE, "system:textbook:", "教材软著"));
        moduleConfigs.put("PATENT", new ModuleConfig(PATENT_APPLY_PROCESS_CODE, "system:patent:", "专利软著"));
        moduleConfigs.put("LECTURE", new ModuleConfig(LECTURE_APPROVAL_PROCESS_CODE, "system:lecture:", "讲座报告"));
        moduleConfigs.put("HORIZONTAL_APPLY", new ModuleConfig(HORIZONTAL_APPLY_PROCESS_CODE, "system:horizontal:", "横向项目申报"));
        moduleConfigs.put("HORIZONTAL_OVER", new ModuleConfig(HORIZONTAL_OVER_PROCESS_CODE, "system:horizontal:", "横向项目结题"));
        moduleConfigs.put("VERTICAL_APPLY", new ModuleConfig(VERTICAL_APPLY_PROCESS_CODE, "system:vertical:", "纵向项目申报"));
        moduleConfigs.put("VERTICAL_OVER", new ModuleConfig(VERTICAL_OVER_PROCESS_CODE, "system:vertical:", "纵向项目结题"));
        moduleConfigs.put("TEC_TRA", new ModuleConfig(INTRASCHPRO_APPLY_PROCESS_CODE, "system:teccar:", "成果转化"));
        log.info("已注册 {} 个模块配置", moduleConfigs.size());
    }

    /**
     * 从数据库加载所有模块的状态映射配置
     * 遍历已注册的模块配置，查询每个流程下的状态，构建模块编码到状态映射的二级缓存
     */
    private void loadAllStatusMappings() {
        allStatusMappings.clear();
        for (Map.Entry<String, ModuleConfig> entry : moduleConfigs.entrySet()) {
            String moduleCode = entry.getKey();
            ModuleConfig config = entry.getValue();
            Map<String, StatusMapping> statusMapping = loadStatusMappingForProcess(config.getProcessCode(), config.getModuleName());
            allStatusMappings.put(moduleCode, statusMapping);
        }
    }

    /**
     * 从数据库加载指定流程的状态映射配置
     *
     * @param processCode 流程编码
     * @param moduleName 模块名称（用于日志输出）
     * @return 状态编码到状态映射的映射表
     */
    private Map<String, StatusMapping> loadStatusMappingForProcess(String processCode, String moduleName) {
        Map<String, StatusMapping> mapping = new HashMap<>();
        List<SysApprovalState> states = approvalStateMapper.selectSysApprovalStateByProcessCode(processCode);
        if (states == null || states.isEmpty()) {
            log.warn("未找到流程 {} ({}) 的状态配置，请检查sys_approval_state表", processCode, moduleName);
            return mapping;
        }
        for (SysApprovalState state : states) {
            // 只加载启用的状态（status=0）
            if ("0".equals(state.getStatus())) {
                String colorType = getColorTypeByStateCode(state.getStateCode());
                mapping.put(state.getStateCode(),
                        new StatusMapping(state.getStateName(), colorType, getSemanticByStateCode(state.getStateCode())));
            }
        }
        log.debug("流程 {} ({}) 加载了 {} 个状态", processCode, moduleName, mapping.size());
        return mapping;
    }

    /**
     * 统计所有模块的状态映射总数
     *
     * @return 状态映射总数
     */
    private int countAllStatusMappings() {
        int count = 0;
        for (Map<String, StatusMapping> mapping : allStatusMappings.values()) {
            count += mapping.size();
        }
        return count;
    }

    /**
     * 刷新配置缓存（用于管理员修改配置后手动刷新）
     */
    public void refreshCache() {
        log.info("手动刷新八大模块审批流程配置缓存...");
        init();
    }

    /**
     * 根据状态编码计算颜色类型
     * 基于状态编码后缀判断语义，统一颜色规则
     *
     * @param stateCode 状态编码
     * @return 颜色类型
     */
    private String getColorTypeByStateCode(String stateCode) {
        if (stateCode == null) {
            return PageRenderColorConstants.COLOR_DEFAULT;
        }
        if (stateCode.endsWith("_DRAFT")) {
            return PageRenderColorConstants.COLOR_DEFAULT;
        }
        if (stateCode.endsWith("_AUDIT")) {
            return PageRenderColorConstants.COLOR_WARNING;
        }
        if (stateCode.endsWith("_PASSED")) {
            return PageRenderColorConstants.COLOR_SUCCESS;
        }
        if (stateCode.endsWith("_REJECTED")) {
            return PageRenderColorConstants.COLOR_DANGER;
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
     * 通用化实现：基于状态编码后缀和节点配置，动态构造按钮规则，支持八大模块
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
        ModuleConfig config = moduleConfigs.get(moduleCode);
        if (config == null) {
            log.warn("未找到模块 {} 的配置，返回空按钮列表", moduleCode);
            return Collections.emptyList();
        }

        return buildGenericActions(context, config);
    }

    /**
     * 构建通用按钮动作列表
     * 基于状态编码后缀（_DRAFT、_AUDIT、_PASSED、_REJECTED）和节点配置，通用化构造按钮规则
     * 八大模块共用同一套按钮构造逻辑，仅权限前缀不同
     *
     * @param context 页面渲染上下文
     * @param config 模块配置
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> buildGenericActions(PageRenderContext context, ModuleConfig config) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = context.getCurrentState();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");

        // 基于状态编码后缀判断状态类型（不依赖节点Map，因为nodeCode和stateCode不同）
        // 状态编码格式示例：PAPER_JYS_AUDIT（教研室审批中）、PAPER_KYC_AUDIT（科研处审批中）
        boolean isInAudit = state != null && state.endsWith("_AUDIT");
        boolean isDraft = state != null && state.endsWith("_DRAFT");
        boolean isPassed = state != null && state.endsWith("_PASSED");
        boolean isRejected = state != null && state.endsWith("_REJECTED");
        // 判断是否为科研处审批（两级审批中的第二级）
        // 状态编码中包含 KYC 表示科研处审批，JYS 表示教研室审批
        boolean isKyAudit = state != null && state.contains("_KYC_");

        // 查看按钮：所有状态都显示，权限 {prefix}info
        if (context.hasPermission(config.buildPermission("info"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看",
                    PageRenderColorConstants.COLOR_INFO, 100));
        }

        // 查看流程按钮：非草稿状态显示，权限 {prefix}info
        if (!isDraft && context.hasPermission(config.buildPermission("info"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程",
                    PageRenderColorConstants.COLOR_INFO, 101));
        }

        // 编辑按钮：草稿或驳回状态，作者本人或管理员，权限 {prefix}edit
        if ((isDraft || isRejected)
                && (isOwner || isAdmin) && context.hasPermission(config.buildPermission("edit"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑",
                    PageRenderColorConstants.COLOR_PRIMARY, 10));
        }

        // 删除按钮：草稿或驳回状态，作者本人或管理员，权限 {prefix}remove
        if ((isDraft || isRejected) && (isOwner || isAdmin)
                && context.hasPermission(config.buildPermission("remove"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REMOVE, "删除",
                    PageRenderColorConstants.COLOR_DANGER, 20, "确定要删除该记录吗？"));
        }

        // 提交按钮：草稿状态，作者本人或管理员，权限 {prefix}edit
        if (isDraft && (isOwner || isAdmin)
                && context.hasPermission(config.buildPermission("edit"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要提交该记录吗？"));
        }

        // 审批节点按钮：处于审批中的节点，有审批权限的用户可以看到批阅/通过/驳回按钮
        if (isInAudit && context.hasPermission(config.buildPermission("process"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REVIEW, "批阅",
                    PageRenderColorConstants.COLOR_PRIMARY, 30));
            // 审批人进入批阅页面后需要"通过"和"驳回"按钮
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 31, "确定要通过该记录吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 32, "确定要驳回该记录吗？"));
        }

        // 审批中状态，仅科研处审批支持撤回（教研室审批不可撤回）
        // 所有模块均为两级审批（教研室→科研处），仅第二级科研处审批时可撤回
        if (isKyAudit && context.hasPermission(config.buildPermission("revoke"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        }

        // 通过状态，管理员可撤回（需有撤回权限）
        if (isPassed && context.hasPermission(config.buildPermission("kyrevoke"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        }

        // 按排序号排序
        Collections.sort(actions);

        return actions;
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
     * 获取模块状态映射配置
     * 从数据库加载的缓存中获取状态映射，避免硬编码
     *
     * @param moduleCode 模块编码
     * @return 状态映射配置
     */
    private Map<String, StatusMapping> getStatusMapping(String moduleCode) {
        Map<String, StatusMapping> mapping = allStatusMappings.get(moduleCode);
        if (mapping != null) {
            return mapping;
        }
        // 未找到模块配置时返回空映射
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
