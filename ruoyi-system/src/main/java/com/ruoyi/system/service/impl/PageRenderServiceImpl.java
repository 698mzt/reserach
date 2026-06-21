package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysApprovalNodeMapper;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
import com.ruoyi.system.mapper.SysApprovalStateMapper;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 页面渲染公共服务实现类
 * 实现统一的通用规则引擎，支持八大模块通用化
 * 状态映射和按钮规则从数据库审批流程配置中动态加载，避免硬编码
 * 各模块权限通过 ModuleConfig 权限映射表动态解析，消除 if-else
 *
 * 八大模块：论文、教材软著、专利软著、讲座报告、奖励、横向项目申报、横向项目结题、纵向项目申报、纵向项目结题、成果转化
 *
 * @author ruoyi
 */
@Service
public class PageRenderServiceImpl implements IPageRenderService {

    private static final Logger log = LoggerFactory.getLogger(PageRenderServiceImpl.class);

    /** 横向课题立项审批 */
    private static final String HORIZONTAL_APPLY_PROCESS_CODE = "HORIZONTAL_APPLY";

    /** 横向课题结项审批 */
    private static final String HORIZONTAL_OVER_PROCESS_CODE = "HORIZONTAL_OVER";

    /** 追加金额审批 */
    private static final String HORIZONTAL_REAMOUNT_PROCESS_CODE = "HORIZONTAL_REAMOUNT";

    /** 纵向课题立项审批 */
    private static final String VERTICAL_APPLY_PROCESS_CODE = "VERTICAL_APPLY";

    /** 纵向课题结项审批 */
    private static final String VERTICAL_OVER_PROCESS_CODE = "VERTICAL_OVER";

    /** 论文审批流程 */
    private static final String PAPER_PROCESS_CODE = "PAPER_APPROVAL";

    /** 教材专著审批 */
    private static final String TEXTBOOK_APPROVAL_PROCESS_CODE = "TEXTBOOK_APPROVAL";

    /** 成果转化审批 */
    private static final String TEC_TRA_APPLY_PROCESS_CODE = "TEC_TRA_APPLY";

    /** 奖励审批 */
    private static final String REWARD_APPLY_PROCESS_CODE = "REWARD_APPLY";

    /** 讲座报告审批 */
    private static final String LECTURE_APPROVAL_PROCESS_CODE = "LECTURE_APPROVAL";

    /** 专利软著审批 */
    private static final String PATENT_APPLY_PROCESS_CODE = "PATENT_APPLY";

    // ========== 颜色常量（内联替代 PageRenderColorConstants） ==========
    private static final String COLOR_PRIMARY = "primary";
    private static final String COLOR_SUCCESS = "success";
    private static final String COLOR_WARNING = "warning";
    private static final String COLOR_DANGER = "danger";
    private static final String COLOR_INFO = "info";
    private static final String COLOR_DEFAULT = "default";

    /**
     * 模块配置内部类，封装单个模块的渲染配置信息
     * 包含流程编码、权限前缀、模块名称、权限映射表，用于通用化按钮规则构造
     */
    private static class ModuleConfig {
        private final String moduleCode;
        private final String permPrefix;
        private final String processCode;
        private final String moduleName;
        /** 权限映射表：通用动作标识 -> 实际权限后缀 */
        private final Map<String, String> permissionMap;

        ModuleConfig(String moduleCode, String permPrefix, String processCode,
                String moduleName, Map<String, String> permissionMap) {
            this.moduleCode = moduleCode;
            this.permPrefix = permPrefix;
            this.processCode = processCode;
            this.moduleName = moduleName;
            this.permissionMap = permissionMap != null ? permissionMap : new HashMap<>();
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
         * 根据通用动作标识获取完整权限标识
         * 自动从权限映射表中查找实际权限后缀，实现动态权限解析
         *
         * @param actionKey 通用动作标识（如 info、edit、approve、kypy、revoke 等）
         * @return 完整权限标识（如 system:paper:kypy、system:vertical:KYC）
         */
        String getPermission(String actionKey) {
            String suffix = permissionMap.getOrDefault(actionKey, actionKey);
            return permPrefix + ":" + suffix;
        }
    }

    @Autowired
    private SysApprovalStateMapper approvalStateMapper;

    @Autowired
    private SysApprovalNodeMapper approvalNodeMapper;

    @Autowired
    private SysApprovalProcessMapper approvalProcessMapper;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

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
            int statusCount = allStatusMappings.values().stream().mapToInt(Map::size).sum();
            int nodeCount = allNodeMappings.values().stream().mapToInt(Map::size).sum();
            log.info("八大模块审批流程配置加载完成，注册模块数: {}, 总状态数: {}, 总节点数: {}",
                    MODULE_REGISTRY.size(), statusCount, nodeCount);
        } catch (Exception e) {
            log.error("加载八大模块审批流程配置失败，将使用空配置", e);
        }
    }

    /**
     * 注册各模块配置及权限映射表
     * 权限映射表以数据库中实际存在的权限名称为准，不遵循统一命名规范
     */
    private void registerModuleConfigs() {
        // 论文模块：教研室批阅=process, 学院批阅=xypy, 科研批阅=kypy, 撤回=revoke, 科研退回=kyrevoke
        MODULE_REGISTRY.put("PAPER", new ModuleConfig("PAPER", "system:paper", PAPER_PROCESS_CODE, "论文",
                new HashMap<String, String>() {
                    {
                        put("info", "info");
                        put("edit", "edit");
                        put("add", "add");
                        put("remove", "remove");
                        put("approve", "process"); // 教研室批阅
                        put("kypy", "kypy"); // 科研处批阅
                        put("revoke", "revoke"); // 作者撤回
                        put("kyrevoke", "kyrevoke"); // 科研处退回
                        put("xyrevoke", "xyrevoke"); // 学院退回
                    }
                }));

        // 奖励模块：批阅=process, 核查=hecha, 查阅=chayue
        MODULE_REGISTRY.put("REWARD", new ModuleConfig("REWARD", "system:reward", REWARD_APPLY_PROCESS_CODE, "奖励",
                new HashMap<String, String>() {
                    {
                        put("info", "info");
                        put("edit", "edit");
                        put("add", "add");
                        put("remove", "remove");
                        put("approve", "process");
                        put("kypy", "hecha");
                        put("revoke", "revoke");
                        put("kyrevoke", "kyrevoke");
                    }
                }));

        // 教材软著模块：批阅=process, 核查=hecha, 查阅=chayue, 撤销=chexiao, 提交=tijiao
        MODULE_REGISTRY.put("TEXTBOOK",
                new ModuleConfig("TEXTBOOK", "system:jiaocairuanzhu", TEXTBOOK_APPROVAL_PROCESS_CODE, "教材软著",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "hecha");
                                put("revoke", "chexiao");
                                put("kyrevoke", "kyrevoke");
                                put("submit", "tijiao");
                            }
                        }));

        // 专利软著模块：批阅=process, 核查=hecha, 查阅=chayue, 撤销=chexiao
        MODULE_REGISTRY.put("PATENT",
                new ModuleConfig("PATENT", "system:zhuanliruanzhu", PATENT_APPLY_PROCESS_CODE, "专利软著",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "hecha");
                                put("revoke", "chexiao");
                                put("kyrevoke", "kyrevoke");
                            }
                        }));

        // 讲座报告模块：批阅=process, 核查=check, 学院批阅=xyprocess
        MODULE_REGISTRY.put("LECTURE",
                new ModuleConfig("LECTURE", "system:report", LECTURE_APPROVAL_PROCESS_CODE, "讲座报告",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "check");
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                                put("xyprocess", "xyprocess");
                            }
                        }));

        // 横向项目申报模块：批阅=process, 科研室批阅=reprocess, 核查=hecha, 学院审批=Dept, 结项=apply_over
        MODULE_REGISTRY.put("HORIZONTAL_APPLY",
                new ModuleConfig("HORIZONTAL_APPLY", "system:apply", HORIZONTAL_APPLY_PROCESS_CODE, "横向项目申报",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "hecha");
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                                put("reprocess", "reprocess");
                            }
                        }));

        // 横向项目结题模块：权限同立项
        MODULE_REGISTRY.put("HORIZONTAL_OVER",
                new ModuleConfig("HORIZONTAL_OVER", "system:apply", HORIZONTAL_OVER_PROCESS_CODE, "横向项目结题",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "hecha");
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                                put("reprocess", "reprocess");
                            }
                        }));

        // 追加金额审批模块：权限同立项（到账金额走独立审批流）
        MODULE_REGISTRY.put("HORIZONTAL_REAMOUNT",
                new ModuleConfig("HORIZONTAL_REAMOUNT", "system:apply", HORIZONTAL_REAMOUNT_PROCESS_CODE, "追加金额审批",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "process");
                                put("kypy", "hecha");
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                            }
                        }));

        // 纵向项目申报模块：JYS批阅=JYS, 学院审批=Dept, KYC批阅=KYC (大写！)
        MODULE_REGISTRY.put("VERTICAL_APPLY",
                new ModuleConfig("VERTICAL_APPLY", "system:apply_vertical", VERTICAL_APPLY_PROCESS_CODE, "纵向项目申报",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "JYS"); // 纵向教研室批阅
                                put("kypy", "KYC"); // 纵向科研处批阅（注意大写）
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                            }
                        }));

        // 纵向项目结题模块：权限同立项
        MODULE_REGISTRY.put("VERTICAL_OVER",
                new ModuleConfig("VERTICAL_OVER", "system:apply_vertical", VERTICAL_OVER_PROCESS_CODE, "纵向项目结题",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "JYS");
                                put("kypy", "KYC");
                                put("revoke", "revoke");
                                put("kyrevoke", "kyrevoke");
                            }
                        }));

        // 成果转化模块：教研室批阅=JYPY, 学院批阅=XYPY, 科研室批阅=KYPY, 教研室撤回=JYCH, 学院撤回=XYCH, 科研室撤回=KYCH
        MODULE_REGISTRY.put("TEC_TRA",
                new ModuleConfig("TEC_TRA", "system:intraSch", TEC_TRA_APPLY_PROCESS_CODE, "成果转化",
                        new HashMap<String, String>() {
                            {
                                put("info", "info");
                                put("edit", "edit");
                                put("add", "add");
                                put("remove", "remove");
                                put("approve", "JYPY"); // 教研室批阅
                                put("kypy", "KYPY"); // 科研室批阅
                                put("revoke", "JYCH"); // 教研室撤回
                                put("kyrevoke", "KYCH"); // 科研室撤回
                            }
                        }));

        log.info("已注册 {} 个模块配置，权限映射已就绪", MODULE_REGISTRY.size());
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
            Map<String, SysApprovalNode> nodeMapping = loadNodeMappingForProcess(process.getId(), processCode,
                    moduleName);
            allNodeMappings.put(processCode, nodeMapping);
        }
    }

    /**
     * 从数据库加载指定流程的状态映射配置
     *
     * @param processCode 流程编码
     * @param moduleName  模块名称（用于日志输出）
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
                        new StatusMapping(state.getStateName(), colorType,
                                getSemanticByStateCode(state.getStateCode())));
            }
        }
        log.debug("流程 {} ({}) 加载了 {} 个状态", processCode, moduleName, mapping.size());
        return mapping;
    }

    /**
     * 从数据库加载指定流程的审批节点配置
     *
     * @param processId   流程ID
     * @param processCode 流程编码
     * @param moduleName  模块名称（用于日志输出）
     * @return 节点编码到节点信息的映射表
     */
    private Map<String, SysApprovalNode> loadNodeMappingForProcess(Long processId, String processCode,
            String moduleName) {
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
     * 合理
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
                return COLOR_DEFAULT;
            case JYS_AUDIT:
                return COLOR_WARNING;
            case KYC_AUDIT:
                return COLOR_INFO;
            case PASSED:
                return COLOR_SUCCESS;
            case REJECTED:
                return COLOR_DANGER;
            default:
                return COLOR_PRIMARY;
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
            return PageRenderStatusMeta.of("", "未知", COLOR_DEFAULT);
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
     * 按状态语义分组生成按钮：通用按钮（全部状态）→ 状态特有按钮 → 模块特有扩展
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

        // 1. 通用按钮：查看、查看流程（所有状态）
        actions.addAll(addAlwaysVisibleActions(context));

        // 2. 按状态语义分组生成按钮
        StateSemantic semantic = parseStateSemantic(context.getCurrentState());
        switch (semantic) {
            case DRAFT:
                actions.addAll(addDraftStateActions(context));
                break;
            case REJECTED:
                actions.addAll(addRejectedStateActions(context));
                break;
            case JYS_AUDIT:
            case KYC_AUDIT:
                addAuditStateActions(context, actions);
                break;
            case PASSED:
                actions.addAll(addPassedStateActions(context));
                break;
            default:
                break;
        }

        // 3. 追加模块特有按钮
        actions.addAll(addModuleSpecificActions(context));

        // 4. 按排序号排序
        Collections.sort(actions);

        return actions;
    }

    /**
     * 添加通用按钮：查看、查看流程（所有状态都显示）
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> addAlwaysVisibleActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        ModuleConfig config = getModuleConfig(context.getModuleCode(), context.getPermPrefix());
        if (config == null) {
            return actions;
        }
        // 查看按钮
        if (context.hasPermission(config.getPermission("info"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看", COLOR_INFO, 100));
        }
        // 查看流程按钮
        if (context.hasPermission(config.getPermission("info"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程", COLOR_INFO, 101));
        }
        return actions;
    }

    /**
     * 添加草稿状态按钮：提交、编辑、删除
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> addDraftStateActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");
        boolean isTeacherActive = isTeacherRole(context.getActiveRoleKey());
        boolean canOwnerActAsTeacher = isAdmin || isTeacherActive;
        ModuleConfig config = getModuleConfig(context.getModuleCode(), context.getPermPrefix());
        if (config == null) {
            return actions;
        }
        // 提交：仅系统管理员 或 所有者且为教师角色，需edit权限
        if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("edit"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "提交", COLOR_SUCCESS, 5, "确定要提交该记录吗？"));
        }
        // 编辑：仅系统管理员 或 所有者且为教师角色，需edit权限
        if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("edit"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑", COLOR_PRIMARY, 10));
        }
        // 删除：仅系统管理员 或 所有者且为教师角色，需remove权限
        if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("remove"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REMOVE, "删除", COLOR_DANGER, 20, "确定要删除该记录吗？"));
        }
        return actions;
    }

    /**
     * 添加驳回状态按钮：编辑
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> addRejectedStateActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");
        boolean isTeacherActive = isTeacherRole(context.getActiveRoleKey());
        boolean canOwnerActAsTeacher = isAdmin || isTeacherActive;
        ModuleConfig config = getModuleConfig(context.getModuleCode(), context.getPermPrefix());
        if (config == null) {
            return actions;
        }
        // 编辑：仅系统管理员 或 所有者且为教师角色，需edit权限
        if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("edit"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑", COLOR_PRIMARY, 10));
        }
        return actions;
    }

    /**
     * 添加通过状态按钮：撤回
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> addPassedStateActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        ModuleConfig config = getModuleConfig(context.getModuleCode(), context.getPermPrefix());
        if (config == null) {
            return actions;
        }
        // 撤回：需有 kyrevoke 权限
        if (context.hasPermission(config.getPermission("kyrevoke"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        }
        return actions;
    }

    /**
     * 根据模块编码或权限前缀获取模块配置
     * 优先通过 moduleCode 查找，兜底通过 permPrefix 匹配
     *
     * @param moduleCode 模块编码
     * @param permPrefix 权限前缀（兜底匹配用）
     * @return 模块配置，未找到返回 null
     */
    private ModuleConfig getModuleConfig(String moduleCode, String permPrefix) {
        if (moduleCode != null && MODULE_REGISTRY.containsKey(moduleCode)) {
            return MODULE_REGISTRY.get(moduleCode);
        }
        // 兜底：通过权限前缀匹配
        if (permPrefix != null) {
            for (ModuleConfig config : MODULE_REGISTRY.values()) {
                if (config.getPermPrefix().equals(permPrefix)) {
                    return config;
                }
            }
        }
        return null;
    }

    /**
     * 添加审批中状态按钮：批阅、通过、驳回、撤回
     * 不引用业务模块常量，通过权限映射表动态获取权限标识
     *
     * 权限映射规则：
     * - 教研室审批（第一级）：使用 config.getPermission("approve") 动态解析
     * - 科研处审批（第二级）：使用 config.getPermission("kypy") 动态解析
     * - 撤回（科研处审批中）：教研室审批人使用 config.getPermission("approve") 权限撤回
     *
     * @param context 页面渲染上下文
     * @param actions 按钮列表（追加）
     */
    private void addAuditStateActions(PageRenderContext context, List<PageRenderActionItem> actions) {
        String state = context.getCurrentState();
        if (state == null) {
            return;
        }

        ModuleConfig config = getModuleConfig(context.getModuleCode(), context.getPermPrefix());
        if (config == null) {
            return;
        }

        StateSemantic semantic = parseStateSemantic(state);
        boolean isFirstAuditNode = semantic == StateSemantic.JYS_AUDIT;
        boolean isLastAuditNode = semantic == StateSemantic.KYC_AUDIT;

        // 教研室审批按钮
        if (isFirstAuditNode && context.hasPermission(config.getPermission("approve"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REVIEW, "批阅", COLOR_PRIMARY, 30));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过", COLOR_SUCCESS, 31, "确定要通过该记录吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回", COLOR_DANGER, 32, "确定要驳回该记录吗？"));
        }
        // 科研处审批按钮
        else if (isLastAuditNode && context.hasPermission(config.getPermission("kypy"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_KY_REVIEW, "批阅", COLOR_PRIMARY, 30));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过", COLOR_SUCCESS, 31, "确定要通过该记录吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回", COLOR_DANGER, 32, "确定要驳回该记录吗？"));
        }

        // 撤回按钮：科研处审批中，教研室审批人可撤回
        if (isLastAuditNode && context.hasPermission(config.getPermission("revoke"))) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该记录吗？"));
        }
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

        ModuleConfig config = MODULE_REGISTRY.get(moduleCode);
        if (config == null) {
            return specificActions;
        }

        // 横向课题：显示"申请结项"和"追加金额"按钮
        // 申请结项：仅已通过状态、申请人/管理员、需add权限
        // 追加金额：仅教师本人在立项流程全部状态可见，其他角色不显示
        if ("HORIZONTAL_APPLY".equals(moduleCode) && context.getCurrentState() != null) {
            boolean isOwner = context.isOwner();
            boolean isAdmin = context.hasRole("admin");
            boolean isTeacherActive = isTeacherRole(context.getActiveRoleKey());
            boolean canOwnerActAsTeacher = isAdmin || isTeacherActive;
            // 申请结项：仅系统管理员 或 所有者且当前为教师角色时显示，需add权限
            if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("add"))) {
                if (context.getCurrentState().endsWith("_PASSED")) {
                    specificActions.add(PageRenderActionItem.of(
                            "submitOver", "申请结项", COLOR_PRIMARY, 50));
                }
            }
            // 追加金额：仅申请人本人在立项流程各状态可见
            if (isOwner && context.hasPermission(config.getPermission("edit"))) {
                specificActions.add(PageRenderActionItem.of(
                        "reamount", "追加金额", COLOR_PRIMARY, 51));
            }
            // 驳回状态：所有可见该页面的角色均可撤回
            if (context.getCurrentState().endsWith("_REJECTED")) {
                specificActions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该驳回记录吗？"));
            }
        }

        // 横向课题结项：驳回状态下显示撤回按钮
        if ("HORIZONTAL_OVER".equals(moduleCode) && context.getCurrentState() != null
                && context.getCurrentState().endsWith("_REJECTED")) {
            specificActions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该驳回记录吗？"));
        }

        // 论文驳回状态：仅该条驳回操作的执行人可撤回，不能替他人撤回
        if ("PAPER".equals(moduleCode) && context.getCurrentState() != null
                && SciPaperA.PAPER_REJECTED.equals(context.getCurrentState())) {
            boolean isAdmin = context.hasRole("admin");
            boolean isOwner = context.isOwner();
            boolean hasRevokePerm = context.hasPermission(config.getPermission("revoke"));
            boolean hasKypyPerm = context.hasPermission(config.getPermission("kypy"));
            boolean hasApprovePerm = context.hasPermission(config.getPermission("approve"));
            // 查询最近一次驳回记录，检查是否为当前用户驳回
            boolean isRejectOperator = false;
            if (context.getBusinessId() != null && StringUtils.isNotEmpty(context.getProcessCode())) {
                SysApprovalHistory lastReject = sysApprovalHistoryService.selectLastRejectByBusinessId(
                        context.getProcessCode(), context.getBusinessId());
                if (lastReject != null && lastReject.getOperatorId() != null
                        && context.getCurrentUser() != null) {
                    isRejectOperator = lastReject.getOperatorId().equals(context.getCurrentUser().getUserId());
                }
            }
            // 系统管理员 或 驳回操作人（且拥有相应权限）才能撤回
            if (isAdmin || (isRejectOperator && (hasRevokePerm || hasKypyPerm || hasApprovePerm || isOwner))) {
                specificActions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该记录吗？"));
            }
        }

        // 纵向课题立项通过后显示"申请结项"按钮
        if ("VERTICAL_APPLY".equals(moduleCode) && context.getCurrentState() != null
                && (context.getCurrentState().endsWith("_PASS") || context.getCurrentState().endsWith("_PASSED"))) {
            boolean isOwner = context.isOwner();
            boolean isAdmin = context.hasRole("admin");
            boolean isTeacherActive = isTeacherRole(context.getActiveRoleKey());
            boolean canOwnerActAsTeacher = isAdmin || isTeacherActive;
            // 申请结项：仅系统管理员 或 所有者且当前为教师角色时显示，需add权限
            if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("add"))) {
                specificActions.add(PageRenderActionItem.of(
                        "overApply", "申请结项", COLOR_PRIMARY, 52));
            }
        }

        // 追加金额审批模块：驳回状态下显示"撤回"和"重新提交"按钮
        if ("HORIZONTAL_REAMOUNT".equals(moduleCode) && context.getCurrentState() != null
                && context.getCurrentState().endsWith("_REJECTED")) {
            boolean isOwner = context.isOwner();
            boolean isAdmin = context.hasRole("admin");
            boolean isTeacherActive = isTeacherRole(context.getActiveRoleKey());
            boolean canOwnerActAsTeacher = isAdmin || isTeacherActive;
            boolean canApprove = context.hasPermission(config.getPermission("approve"));
            boolean canKyrevoke = context.hasPermission("system:apply:kyrevoke");
            // 申请人/教研室/科研处均可撤回，目标状态不同
            if (isOwner || isAdmin || canApprove || canKyrevoke) {
                specificActions.add(PageRenderActionItem.of(
                        PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该记录吗？"));
            }
            // 重新提交：仅系统管理员 或 所有者且当前为教师角色时显示，需edit权限
            if ((isOwner && canOwnerActAsTeacher || isAdmin) && context.hasPermission(config.getPermission("edit"))) {
                specificActions.add(PageRenderActionItem.of(
                        "submit", "重新提交", COLOR_SUCCESS, 5, "确定要重新提交该记录吗？"));
            }
        }

        // 追加金额审批模块：JYS_AUDIT状态显示撤回按钮（仅录入者撤回→草稿）
        if ("HORIZONTAL_REAMOUNT".equals(moduleCode) && context.getCurrentState() != null
                && context.getCurrentState().endsWith("_JYS_AUDIT")
                && context.isOwner()) {
            specificActions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回", COLOR_WARNING, 40, "确定要撤回该记录吗？"));
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


    @Override
    public PageRenderResult<?> fillPageRenderData(String moduleCode, String permPrefix,
            String processCode, String currentState,
            Long businessId, Long creatorId, Set<String> permissions) {
        PageRenderStatusMeta fallbackMeta = PageRenderStatusMeta.of("", "未知", COLOR_DEFAULT);
        List<PageRenderActionItem> emptyActions = Collections.emptyList();

        if (currentState == null) {
            return PageRenderResult.of(fallbackMeta, emptyActions, null);
        }

        try {
            SysUser currentUser = ShiroUtils.getSysUser();
            if (currentUser == null) {
                return PageRenderResult.of(fallbackMeta, emptyActions, null);
            }

            Set<String> currentPermissions = permissions != null ? permissions : buildCurrentPermissions(currentUser);
            List<String> roleKeys = buildRoleKeys(currentUser);

            PageRenderContext context = new PageRenderContext();
            context.setModuleCode(moduleCode);
            context.setBusinessId(businessId);
            context.setCurrentState(currentState);
            context.setCreatorId(creatorId);
            context.setCurrentUser(currentUser);
            context.setPermissions(currentPermissions);
            context.setRoleKeys(roleKeys);
            context.setPermPrefix(permPrefix);
            context.setProcessCode(processCode);
            context.setActiveRoleKey(getActiveRoleKey(currentUser));

            PageRenderStatusMeta statusMeta = buildStatusMeta(context);
            List<PageRenderActionItem> actions = buildActions(context);

            return PageRenderResult.of(statusMeta, actions, null);
        } catch (Exception e) {
            log.error("填充页面渲染数据失败, moduleCode={}, currentState={}", moduleCode, currentState, e);
            return PageRenderResult.of(fallbackMeta, emptyActions, null);
        }
    }

    /**
     * 构建当前用户的权限列表（仅从数据库查询）
     * 使用 Shiro 自身的 isPermitted() 方法已有缓存机制，无需额外反射读缓存
     *
     * @param currentUser 当前登录用户对象，不能为 null
     * @return 用户权限标识列表，如果用户为空或无权限则返回空列表
     */
    @Override
    public Set<String> buildCurrentPermissions(SysUser currentUser) {
        if (currentUser == null) {
            return Collections.emptySet();
        }

        // 从数据库查询
        Long activeRoleId = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Session session = subject.getSession(false);
            if (session != null) {
                activeRoleId = (Long) session.getAttribute("activeRoleId");
            }
        }
        if (activeRoleId != null) {
            SysRole activeRole = roleService.selectRoleById(activeRoleId);
            if (activeRole != null) {
                if (activeRole.isAdmin()) {
                    Set<String> adminPerms = new LinkedHashSet<>();
                    adminPerms.add("*:*:*");
                    return adminPerms;
                }
                return sysMenuService.selectPermsByRoleId(activeRoleId);
            }
        }

        Set<String> permsSet;
        try {
            permsSet = sysMenuService.selectPermsByUserId(currentUser.getUserId());
        } catch (Exception e) {
            log.error("获取当前用户权限失败, userId={}", currentUser.getUserId(), e);
            return Collections.emptySet();
        }

        if (permsSet == null || permsSet.isEmpty()) {
            return Collections.emptySet();
        }

        return permsSet;
    }

    /**
     * 判断指定角色标识是否为教师角色
     * 教师角色指普通教师（如 roleKey=teacher 或无特殊角色标识的用户），
     * 不包括管理员角色（科研处、教研室、学院管理等）
     * 系统管理员（roleKey=admin）不属于教师角色，但通过 isAdmin 单独判断
     *
     * @param roleKey 角色标识
     * @return true 表示为教师角色
     */
    private boolean isTeacherRole(String roleKey) {
        if (roleKey == null) {
            // 无特定角色标识时默认为教师角色
            return true;
        }
        // 已知的非教师角色（管理员角色）列表
        Set<String> nonTeacherRoles = new HashSet<>(Arrays.asList(
                "admin", "sci_tesearch", "research", "dept_teacher",
                "discuss_college", "dzgc_college", "yssj_college",
                "student_college", "marxism_college", "general"));
        return !nonTeacherRoles.contains(roleKey);
    }

    /**
     * 获取当前用户的活跃角色标识（roleKey）
     * 从 Shiro Session 中获取 activeRoleId，再查询对应的角色标识
     *
     * @param currentUser 当前用户
     * @return 活跃角色标识，如果获取失败返回 null
     */
    private String getActiveRoleKey(SysUser currentUser) {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject == null) {
                return null;
            }
            Session session = subject.getSession(false);
            if (session == null) {
                return null;
            }
            Long activeRoleId = (Long) session.getAttribute("activeRoleId");
            if (activeRoleId == null) {
                return null;
            }
            SysRole activeRole = roleService.selectRoleById(activeRoleId);
            return activeRole != null ? activeRole.getRoleKey() : null;
        } catch (Exception e) {
            log.debug("获取活跃角色标识失败", e);
            return null;
        }
    }

    private List<String> buildRoleKeys(SysUser currentUser) {
        if (currentUser == null || currentUser.getRoles() == null) {
            return Collections.emptyList();
        }
        List<String> roleKeys = new ArrayList<>();
        for (SysRole role : currentUser.getRoles()) {
            if (role != null && role.getRoleKey() != null && !role.getRoleKey().isEmpty()) {
                roleKeys.add(role.getRoleKey());
            }
        }
        return roleKeys;
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
