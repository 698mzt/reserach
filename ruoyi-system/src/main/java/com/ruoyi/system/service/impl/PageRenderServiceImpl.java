package com.ruoyi.system.service.impl;

import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysApprovalNodeMapper;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
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
 * 实现统一的通用规则引擎，支持八大模块通用化
 * 状态映射和按钮规则从数据库审批流程配置中动态加载，避免硬编码
 *
 * 八大模块：论文、教材软著、专利软著、讲座报告、奖励、横向项目申报、横向项目结题、纵向项目申报、纵向项目结题、成果转化
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
        private final String moduleCode;
        private final String permPrefix;
        private final String processCode;
        private final String moduleName;

        ModuleConfig(String moduleCode, String permPrefix, String processCode, String moduleName) {
            this.moduleCode = moduleCode;
            this.permPrefix = permPrefix;
            this.processCode = processCode;
            this.moduleName = moduleName;
        }

        String getModuleCode() {
            return moduleCode;
        }

        String getPermPrefix() {
            return permPrefix;
        }

        String getProcessCode() {
            return processCode;
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
            return permPrefix + ":" + action;
        }
    }

    @Autowired
    private SysApprovalStateMapper approvalStateMapper;

    @Autowired
    private SysApprovalNodeMapper approvalNodeMapper;

    @Autowired
    private SysApprovalProcessMapper approvalProcessMapper;

    /** 八大模块配置注册表（moduleCode -> ModuleConfig） */
    private final Map<String, ModuleConfig> MODULE_REGISTRY = new HashMap<>();

    /** 各流程状态映射配置（processCode -> stateCode -> StatusMapping） */
    private final Map<String, Map<String, StatusMapping>> allStatusMappings = new HashMap<>();

    /** 各流程审批节点配置（processCode -> nodeCode -> SysApprovalNode） */
    private final Map<String, Map<String, SysApprovalNode>> allNodeMappings = new HashMap<>();

    /** 模块编码到流程编码的映射缓存（moduleCode -> SysApprovalProcess） */
    private final Map<String, SysApprovalProcess> moduleProcessCache = new HashMap<>();

    /**
     * 应用启动时从数据库加载审批流程配置，初始化所有模块的状态映射和节点映射
     */
    @PostConstruct
    public void init() {
        try {
            log.info("开始加载八大模块审批流程配置...");
            registerModuleConfigs();
            loadAllModuleConfigs();
            log.info("八大模块审批流程配置加载完成，注册模块数: {}, 总状态数: {}, 总节点数: {}",
                    MODULE_REGISTRY.size(), countAllStatusMappings(), countAllNodeMappings());
        } catch (Exception e) {
            log.error("加载八大模块审批流程配置失败，将使用空配置", e);
        }
    }

    /**
     * 注册八大模块配置
     * 每个模块注册 moduleCode、权限前缀、流程编码和模块名称
     */
    private void registerModuleConfigs() {
        MODULE_REGISTRY.put("PAPER", new ModuleConfig("PAPER", "system:paper", PAPER_PROCESS_CODE, "论文"));
        MODULE_REGISTRY.put("REWARD", new ModuleConfig("REWARD", "system:reward", REWARD_APPLY_PROCESS_CODE, "奖励"));
        MODULE_REGISTRY.put("TEXTBOOK", new ModuleConfig("TEXTBOOK", "system:textbook", TEXTBOOK_APPROVAL_PROCESS_CODE, "教材软著"));
        MODULE_REGISTRY.put("PATENT", new ModuleConfig("PATENT", "system:patent", PATENT_APPLY_PROCESS_CODE, "专利软著"));
        MODULE_REGISTRY.put("LECTURE", new ModuleConfig("LECTURE", "system:lecture", LECTURE_APPROVAL_PROCESS_CODE, "讲座报告"));
        MODULE_REGISTRY.put("HORIZONTAL_APPLY", new ModuleConfig("HORIZONTAL_APPLY", "system:apply", HORIZONTAL_APPLY_PROCESS_CODE, "横向项目申报"));
        MODULE_REGISTRY.put("HORIZONTAL_OVER", new ModuleConfig("HORIZONTAL_OVER", "system:apply", HORIZONTAL_OVER_PROCESS_CODE, "横向项目结题"));
        MODULE_REGISTRY.put("VERTICAL_APPLY", new ModuleConfig("VERTICAL_APPLY", "system:vertical", VERTICAL_APPLY_PROCESS_CODE, "纵向项目申报"));
        MODULE_REGISTRY.put("VERTICAL_OVER", new ModuleConfig("VERTICAL_OVER", "system:vertical", VERTICAL_OVER_PROCESS_CODE, "纵向项目结题"));
        MODULE_REGISTRY.put("TEC_TRA", new ModuleConfig("TEC_TRA", "system:teccar", INTRASCHPRO_APPLY_PROCESS_CODE, "成果转化"));
        log.info("已注册 {} 个模块配置", MODULE_REGISTRY.size());
    }

    /**
     * 从数据库加载所有启用流程的状态映射和节点配置
     * 查询 sys_approval_process 表中所有启用流程，为每个流程加载状态和节点
     */
    private void loadAllModuleConfigs() {
        allStatusMappings.clear();
        allNodeMappings.clear();
        moduleProcessCache.clear();

        // 从注册表中获取所有流程编码
        for (ModuleConfig config : MODULE_REGISTRY.values()) {
            String processCode = config.getProcessCode();
            String moduleCode = config.getModuleCode();
            String moduleName = config.getModuleName();

            // 查询流程信息
            SysApprovalProcess process = approvalProcessMapper.selectSysApprovalProcessByProcessCode(processCode);
            if (process == null) {
                log.warn("未找到流程编码 {} ({}) 的配置，请检查sys_approval_process表", processCode, moduleName);
                continue;
            }

            // 缓存流程信息
            moduleProcessCache.put(moduleCode, process);

            // 加载状态映射
            Map<String, StatusMapping> statusMapping = loadStatusMappingForProcess(processCode, moduleName);
            allStatusMappings.put(processCode, statusMapping);

            // 加载节点映射
            Map<String, SysApprovalNode> nodeMapping = loadNodeMappingForProcess(process.getId(), processCode, moduleName);
            allNodeMappings.put(processCode, nodeMapping);
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
     * 从数据库加载指定流程的审批节点配置
     *
     * @param processId 流程ID
     * @param processCode 流程编码
     * @param moduleName 模块名称（用于日志输出）
     * @return 节点编码到节点信息的映射表
     */
    private Map<String, SysApprovalNode> loadNodeMappingForProcess(Long processId, String processCode, String moduleName) {
        Map<String, SysApprovalNode> mapping = new HashMap<>();
        List<SysApprovalNode> nodes = approvalNodeMapper.selectSysApprovalNodeByProcessId(processId);
        if (nodes == null || nodes.isEmpty()) {
            log.warn("未找到流程 {} ({}) 的节点配置，请检查sys_approval_node表", processCode, moduleName);
            return mapping;
        }
        for (SysApprovalNode node : nodes) {
            if ("0".equals(node.getStatus())) {
                mapping.put(node.getNodeCode(), node);
            }
        }
        log.debug("流程 {} ({}) 加载了 {} 个节点", processCode, moduleName, mapping.size());
        return mapping;
    }

    /**
     * 统计所有流程的状态映射总数
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
     * 统计所有流程的节点映射总数
     *
     * @return 节点映射总数
     */
    private int countAllNodeMappings() {
        int count = 0;
        for (Map<String, SysApprovalNode> mapping : allNodeMappings.values()) {
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
     * 状态语义枚举
     */
    enum StateSemantic {
        DRAFT, JYS_AUDIT, KYC_AUDIT, PASSED, REJECTED, UNKNOWN
    }

    /**
     * 根据状态编码后缀推导状态语义
     *
     * @param stateCode 状态编码
     * @return 状态语义
     */
    private StateSemantic parseStateSemantic(String stateCode) {
        if (stateCode == null) {
            return StateSemantic.UNKNOWN;
        }
        if (stateCode.endsWith("_DRAFT")) {
            return StateSemantic.DRAFT;
        }
        if (stateCode.contains("_JYS_") && stateCode.endsWith("_AUDIT")) {
            return StateSemantic.JYS_AUDIT;
        }
        if (stateCode.contains("_KYC_") && stateCode.endsWith("_AUDIT")) {
            return StateSemantic.KYC_AUDIT;
        }
        if (stateCode.endsWith("_PASSED")) {
            return StateSemantic.PASSED;
        }
        if (stateCode.endsWith("_REJECTED")) {
            return StateSemantic.REJECTED;
        }
        return StateSemantic.UNKNOWN;
    }

    /**
     * 根据状态编码计算颜色类型
     *
     * @param stateCode 状态编码
     * @return 颜色类型
     */
    private String getColorTypeByStateCode(String stateCode) {
        StateSemantic semantic = parseStateSemantic(stateCode);
        switch (semantic) {
            case DRAFT:
                return PageRenderColorConstants.COLOR_DEFAULT;
            case JYS_AUDIT:
                return PageRenderColorConstants.COLOR_WARNING;
            case KYC_AUDIT:
                return PageRenderColorConstants.COLOR_INFO;
            case PASSED:
                return PageRenderColorConstants.COLOR_SUCCESS;
            case REJECTED:
                return PageRenderColorConstants.COLOR_DANGER;
            default:
                return PageRenderColorConstants.COLOR_PRIMARY;
        }
    }

    /**
     * 根据状态编码获取通用语义
     *
     * @param stateCode 状态编码
     * @return 语义描述
     */
    private String getSemanticByStateCode(String stateCode) {
        StateSemantic semantic = parseStateSemantic(stateCode);
        switch (semantic) {
            case DRAFT:
                return "待提交";
            case JYS_AUDIT:
            case KYC_AUDIT:
                return "审批中";
            case PASSED:
                return "通过";
            case REJECTED:
                return "驳回";
            default:
                return "未知";
        }
    }

    /**
     * 构建状态展示信息
     * 优先从统一缓存按当前流程的 processCode 查找，兜底使用状态编码后缀自动推导
     *
     * @param context 页面渲染上下文
     * @return 状态展示对象
     */
    @Override
    public PageRenderStatusMeta buildStatusMeta(PageRenderContext context) {
        if (context == null || context.getCurrentState() == null) {
            return PageRenderStatusMeta.of("", "未知", PageRenderColorConstants.COLOR_DEFAULT);
        }

        String stateCode = context.getCurrentState();
        String processCode = context.getProcessCode();

        // 优先从统一缓存查找
        if (processCode != null && allStatusMappings.containsKey(processCode)) {
            Map<String, StatusMapping> mapping = allStatusMappings.get(processCode);
            StatusMapping rule = mapping.get(stateCode);
            if (rule != null) {
                return PageRenderStatusMeta.of(stateCode, rule.getText(), rule.getColorType());
            }
        }

        // 兜底使用状态编码后缀自动推导
        String text = stateCode;
        String colorType = getColorTypeByStateCode(stateCode);
        return PageRenderStatusMeta.of(stateCode, text, colorType);
    }

    /**
     * 构建按钮动作列表
     * 统一入口：调用通用规则引擎 + 模块特有按钮扩展
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
        if (moduleCode == null) {
            return Collections.emptyList();
        }

        List<PageRenderActionItem> actions = new ArrayList<>();

        // 1. 调用通用规则引擎
        actions.addAll(buildCommonActions(context));

        // 2. 追加模块特有按钮
        actions.addAll(addModuleSpecificActions(context));

        // 3. 按排序号排序
        Collections.sort(actions);

        return actions;
    }

    /**
     * 构建通用按钮动作列表
     * 根据状态编码后缀语义 + 审批节点配置 + 权限前缀动态生成按钮列表
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> buildCommonActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = context.getCurrentState();
        if (state == null) {
            return actions;
        }

        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");
        StateSemantic semantic = parseStateSemantic(state);

        // 获取权限前缀（优先从context取，兜底从注册表取）
        String permPrefix = context.getPermPrefix();
        if (permPrefix == null) {
            ModuleConfig config = MODULE_REGISTRY.get(context.getModuleCode());
            if (config != null) {
                permPrefix = config.getPermPrefix();
            }
        }

        // 查看按钮：所有状态都显示
        if (permPrefix != null && context.hasPermission(permPrefix + ":info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看",
                    PageRenderColorConstants.COLOR_INFO, 100));
        }

        // 查看流程按钮：非草稿状态显示
        if (semantic != StateSemantic.DRAFT && permPrefix != null && context.hasPermission(permPrefix + ":info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程",
                    PageRenderColorConstants.COLOR_INFO, 101));
        }

        // 草稿状态按钮
        if (semantic == StateSemantic.DRAFT) {
            // 编辑按钮
            if ((isOwner || isAdmin) && permPrefix != null && context.hasPermission(permPrefix + ":edit")) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_EDIT, "编辑",
                        PageRenderColorConstants.COLOR_PRIMARY, 10));
            }
            // 删除按钮
            if ((isOwner || isAdmin) && permPrefix != null && context.hasPermission(permPrefix + ":remove")) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_REMOVE, "删除",
                        PageRenderColorConstants.COLOR_DANGER, 20, "确定要删除该记录吗？"));
            }
            // 提交按钮
            if ((isOwner || isAdmin) && permPrefix != null && context.hasPermission(permPrefix + ":edit")) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_SUBMIT, "提交",
                        PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要提交该记录吗？"));
            }
        }

        // 驳回状态按钮
        if (semantic == StateSemantic.REJECTED) {
            // 编辑按钮
            if ((isOwner || isAdmin) && permPrefix != null && context.hasPermission(permPrefix + ":edit")) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_EDIT, "编辑",
                        PageRenderColorConstants.COLOR_PRIMARY, 10));
            }
            // 重新提交按钮（编辑保存后状态会变回草稿，这里不额外提供重新提交）
        }

        // 审批中状态按钮
        if (semantic == StateSemantic.JYS_AUDIT || semantic == StateSemantic.KYC_AUDIT) {
            addAuditActions(context, actions, semantic, permPrefix, isOwner, isAdmin);
        }

        // 通过状态按钮
        if (semantic == StateSemantic.PASSED) {
            // 撤回按钮（需有撤回权限）
            if (permPrefix != null && context.hasPermission(permPrefix + ":kyrevoke")) {
                actions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL, "撤回",
                        PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
            }
        }

        return actions;
    }

    /**
     * 根据节点配置动态生成审批按钮
     * 不引用业务模块常量，通过节点配置和状态语义判断
     *
     * @param context 页面渲染上下文
     * @param actions 按钮列表（追加）
     * @param semantic 状态语义
     * @param permPrefix 权限前缀
     * @param isOwner 是否为创建人
     * @param isAdmin 是否为管理员
     */
    private void addAuditActions(PageRenderContext context, List<PageRenderActionItem> actions,
                                  StateSemantic semantic, String permPrefix, boolean isOwner, boolean isAdmin) {
        String state = context.getCurrentState();
        if (state == null || permPrefix == null) {
            return;
        }

        // 获取当前节点
        SysApprovalNode currentNode = getNodeByState(context.getProcessCode(), state);

        // 判断是否为最后一级节点（科研处审批）
        boolean isLastAuditNode = semantic == StateSemantic.KYC_AUDIT;
        // 判断是否为第一级节点（教研室审批）
        boolean isFirstAuditNode = semantic == StateSemantic.JYS_AUDIT;

        // 审批人按钮：有审批权限的用户可以看到批阅/通过/驳回按钮
        if (context.hasPermission(permPrefix + ":process")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REVIEW, "批阅",
                    PageRenderColorConstants.COLOR_PRIMARY, 30));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 31, "确定要通过该记录吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 32, "确定要驳回该记录吗？"));
        }

        // 撤回按钮规则：
        // 1. 教研室审批（第一级）：仅记录创建者可撤回
        // 2. 科研处审批（第二级）：教研室审批人可撤回，不限制作者本人
        if (isFirstAuditNode && isOwner) {
            // 教研室审批中，仅作者可撤回
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        } else if (isLastAuditNode && context.hasPermission(permPrefix + ":revoke")) {
            // 科研处审批中，有撤回权限的用户可撤回
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        }
    }

    /**
     * 根据状态编码获取当前审批节点
     *
     * @param processCode 流程编码
     * @param state 状态编码
     * @return 节点信息，未找到返回null
     */
    private SysApprovalNode getNodeByState(String processCode, String state) {
        if (processCode == null || state == null) {
            return null;
        }
        Map<String, SysApprovalNode> nodeMapping = allNodeMappings.get(processCode);
        if (nodeMapping == null) {
            return null;
        }
        // 节点编码和状态编码命名规则一致时可直接查找
        // 如 PAPER_JYS_AUDIT 对应 PAPER_JYS_AUDIT 节点
        return nodeMapping.get(state);
    }

    /**
     * 追加模块特有按钮
     * 在通用按钮规则之后，追加模块特有的按钮
     *
     * @param context 页面渲染上下文
     * @return 特有按钮列表
     */
    private List<PageRenderActionItem> addModuleSpecificActions(PageRenderContext context) {
        List<PageRenderActionItem> specificActions = new ArrayList<>();
        String moduleCode = context.getModuleCode();
        if (moduleCode == null) {
            return specificActions;
        }

        String permPrefix = context.getPermPrefix();
        if (permPrefix == null) {
            ModuleConfig config = MODULE_REGISTRY.get(moduleCode);
            if (config != null) {
                permPrefix = config.getPermPrefix();
            }
        }

        // 横向课题立项通过后显示"提交结项申请"和"追加金额"按钮
        if ("HORIZONTAL_APPLY".equals(moduleCode) && context.getCurrentState() != null
                && context.getCurrentState().endsWith("_PASSED")) {
            boolean isOwner = context.isOwner();
            boolean isAdmin = context.hasRole("admin");
            if ((isOwner || isAdmin) && permPrefix != null && context.hasPermission(permPrefix + ":add")) {
                specificActions.add(PageRenderActionItem.of(
                        "submitOver", "提交结项申请",
                        PageRenderColorConstants.COLOR_PRIMARY, 50));
                specificActions.add(PageRenderActionItem.of(
                        "addAmount", "追加金额",
                        PageRenderColorConstants.COLOR_SUCCESS, 51));
            }
        }

        return specificActions;
    }

    /**
     * 构建页面渲染汇总结果
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
