package com.ruoyi.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 审批页面渲染汇总对象，统一承载状态展示信息与按钮动作列表
 * 作为Controller返回的标准格式，供前端统一解析渲染
 *
 * @param <T> 业务数据泛型
 * @author ruoyi
 */
public class PageRenderResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态展示信息 */
    private PageRenderStatusMeta statusMeta;

    /** 按钮动作列表 */
    private List<PageRenderActionItem> actions;

    /** 业务数据 */
    private T data;

    public PageRenderResult() {
    }

    public PageRenderResult(PageRenderStatusMeta statusMeta, List<PageRenderActionItem> actions, T data) {
        this.statusMeta = statusMeta;
        this.actions = actions;
        this.data = data;
    }

    /**
     * 静态工厂方法，构造页面渲染汇总对象
     *
     * @param statusMeta 状态展示信息
     * @param actions    按钮动作列表
     * @param data       业务数据
     * @param <T>        业务数据泛型
     * @return 页面渲染汇总对象
     */
    public static <T> PageRenderResult<T> of(PageRenderStatusMeta statusMeta, List<PageRenderActionItem> actions, T data) {
        return new PageRenderResult<>(statusMeta, actions, data);
    }

    public PageRenderStatusMeta getStatusMeta() {
        return statusMeta;
    }

    public void setStatusMeta(PageRenderStatusMeta statusMeta) {
        this.statusMeta = statusMeta;
    }

    public List<PageRenderActionItem> getActions() {
        return actions;
    }

    public void setActions(List<PageRenderActionItem> actions) {
        this.actions = actions;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageRenderResult{" +
                "statusMeta=" + statusMeta +
                ", actions=" + actions +
                ", data=" + data +
                '}';
    }
}
