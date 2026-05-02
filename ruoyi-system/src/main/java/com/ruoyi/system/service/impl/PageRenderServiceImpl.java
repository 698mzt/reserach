package com.ruoyi.system.service.impl;

import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.domain.PageRenderActionItem;
import com.ruoyi.system.domain.PageRenderContext;
import com.ruoyi.system.domain.PageRenderResult;
import com.ruoyi.system.domain.PageRenderStatusMeta;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.service.IPageRenderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面渲染公共服务实现类
 * 实现统一的状态展示构造和按钮动作构造逻辑，论文模块作为首个试点
 *
 * @author ruoyi
 */
@Service
public class PageRenderServiceImpl implements IPageRenderService {

    /** 论文模块状态映射配置 */
    private static final Map<String, StatusMapping> PAPER_STATUS_MAPPING = new HashMap<>();

    static {
        PAPER_STATUS_MAPPING.put(SciPaperA.PAPER_DRAFT,
                new StatusMapping("草稿", PageRenderColorConstants.COLOR_WARNING, "待提交"));
        PAPER_STATUS_MAPPING.put(SciPaperA.PAPER_JYS_AUDIT,
                new StatusMapping("教研室审批", PageRenderColorConstants.COLOR_PRIMARY, "审批中"));
        PAPER_STATUS_MAPPING.put(SciPaperA.PAPER_KYC_AUDIT,
                new StatusMapping("科研处审批", PageRenderColorConstants.COLOR_PRIMARY, "审批中"));
        PAPER_STATUS_MAPPING.put(SciPaperA.PAPER_PASSED,
                new StatusMapping("审批通过", PageRenderColorConstants.COLOR_SUCCESS, "通过"));
        PAPER_STATUS_MAPPING.put(SciPaperA.PAPER_REJECTED,
                new StatusMapping("审批驳回", PageRenderColorConstants.COLOR_DANGER, "驳回"));
    }

    /**
     * 构建状态展示信息
     * 根据模块编码与当前状态生成状态文案和颜色信息
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
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    private List<PageRenderActionItem> buildPaperActions(PageRenderContext context) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = context.getCurrentState();
        boolean isOwner = context.isOwner();
        boolean isAdmin = context.hasRole("admin");

        // 查看按钮：所有状态都显示，权限 system:paper:info
        if (context.hasPermission("system:paper:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看",
                    PageRenderColorConstants.COLOR_INFO, 100));
        }

        // 查看流程按钮：非草稿状态显示，权限 system:paper:info
        if (!SciPaperA.PAPER_DRAFT.equals(state) && context.hasPermission("system:paper:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程",
                    PageRenderColorConstants.COLOR_INFO, 101));
        }

        // 编辑按钮：草稿或驳回状态，作者本人或管理员，权限 system:paper:edit
        if ((SciPaperA.PAPER_DRAFT.equals(state) || SciPaperA.PAPER_REJECTED.equals(state))
                && (isOwner || isAdmin) && context.hasPermission("system:paper:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑",
                    PageRenderColorConstants.COLOR_PRIMARY, 10));
        }

        // 删除按钮：草稿状态，作者本人或管理员，权限 system:paper:remove
        if (SciPaperA.PAPER_DRAFT.equals(state) && (isOwner || isAdmin)
                && context.hasPermission("system:paper:remove")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REMOVE, "删除",
                    PageRenderColorConstants.COLOR_DANGER, 20, "确定要删除该论文吗？"));
        }

        // 提交按钮：草稿状态，作者本人，权限 system:paper:edit
        if (SciPaperA.PAPER_DRAFT.equals(state) && isOwner
                && context.hasPermission("system:paper:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要提交该论文吗？"));
        }

        // 驳回后重新提交：驳回状态，作者本人，权限 system:paper:edit
        if (SciPaperA.PAPER_REJECTED.equals(state) && isOwner
                && context.hasPermission("system:paper:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "重新提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要重新提交该论文吗？"));
        }

        // 批阅按钮：教研室审批状态，有批阅权限 system:paper:process
        if (SciPaperA.PAPER_JYS_AUDIT.equals(state) && context.hasPermission("system:paper:process")) {
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

        // 教研室审批状态，作者可撤回，权限 system:paper:revoke
        if (SciPaperA.PAPER_JYS_AUDIT.equals(state) && isOwner
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
        if (SciPaperA.PAPER_PASSED.equals(state)
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
     *
     * @param moduleCode 模块编码
     * @return 状态映射配置
     */
    private Map<String, StatusMapping> getStatusMapping(String moduleCode) {
        if ("PAPER".equals(moduleCode)) {
            return PAPER_STATUS_MAPPING;
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
