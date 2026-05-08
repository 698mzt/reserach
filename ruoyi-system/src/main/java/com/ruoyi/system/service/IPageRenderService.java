package com.ruoyi.system.service;

import com.ruoyi.system.domain.PageRenderActionItem;
import com.ruoyi.system.domain.PageRenderContext;
import com.ruoyi.system.domain.PageRenderResult;
import com.ruoyi.system.domain.PageRenderStatusMeta;

import java.util.List;

/**
 * 页面渲染公共服务接口
 * 提供统一的状态展示构造和按钮动作构造能力，供八大模块共用
 *
 * @author ruoyi
 */
public interface IPageRenderService {

    /**
     * 构建状态展示信息
     * 根据模块编码与当前状态生成状态文案和颜色信息
     *
     * @param context 页面渲染上下文
     * @return 状态展示对象
     */
    PageRenderStatusMeta buildStatusMeta(PageRenderContext context);

    /**
     * 构建按钮动作列表
     * 根据状态、权限、当前用户身份、审批节点能力生成按钮列表
     *
     * @param context 页面渲染上下文
     * @return 按钮动作列表
     */
    List<PageRenderActionItem> buildActions(PageRenderContext context);

    /**
     * 构建页面渲染汇总结果
     * 统一入口方法，将状态展示信息与按钮列表组装为完整返回对象
     *
     * @param context      页面渲染上下文
     * @param businessData 业务数据
     * @param <T>          业务数据泛型
     * @return 页面渲染汇总结果
     */
    <T> PageRenderResult<T> buildRenderResult(PageRenderContext context, T businessData);

    /**
     * 统一填充页面渲染数据（公有方法）
     * 自动从 Shiro 缓存获取当前用户权限和角色，构建 PageRenderContext 并调用 buildStatusMeta + buildActions
     *
     * @param moduleCode   模块编码（如 PAPER、REWARD、TEXTBOOK 等）
     * @param permPrefix   权限前缀（如 system:paper、system:reward）
     * @param processCode  流程编码（如 PAPER_APPROVAL、REWARD_APPLY）
     * @param currentState 当前状态编码（如 PAPER_DRAFT、REWARD_JYS_AUDIT）
     * @param businessId   业务ID
     * @param creatorId    创建者ID
     * @return 页面渲染汇总结果（包含 statusMeta 和 actions）
     */
    PageRenderResult<?> fillPageRenderData(String moduleCode, String permPrefix,
                                            String processCode, String currentState,
                                            Long businessId, Long creatorId);
}
