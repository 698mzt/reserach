package com.ruoyi.system.domain;

import java.io.Serializable;

/**
 * 通用按钮动作对象，封装按钮的完整配置信息
 * 用于后端统一返回按钮动作列表，供前端按配置渲染
 *
 * @author ruoyi
 */
public class PageRenderActionItem implements Serializable, Comparable<PageRenderActionItem> {
    private static final long serialVersionUID = 1L;

    /** 动作标识：view/viewProcess/edit/remove/submit/approve/reject/recall */
    private String actionKey;

    /** 按钮文案 */
    private String actionText;

    /** 颜色类型：primary/success/warning/danger/info/default */
    private String colorType;

    /** 展示方式：button/link/icon */
    private String displayType;

    /** 是否显示 */
    private Boolean visible;

    /** 排序号，数值越小越靠前 */
    private Integer sortOrder;

    /** 确认消息（可选，点击后弹出确认框） */
    private String confirmMsg;

    public PageRenderActionItem() {
        this.visible = true;
        this.displayType = "button";
        this.sortOrder = 0;
    }

    /**
     * 静态工厂方法，快速构造按钮动作对象
     *
     * @param actionKey  动作标识
     * @param actionText 按钮文案
     * @param colorType  颜色类型
     * @param sortOrder  排序号
     * @return 按钮动作对象
     */
    public static PageRenderActionItem of(String actionKey, String actionText, String colorType, int sortOrder) {
        PageRenderActionItem item = new PageRenderActionItem();
        item.setActionKey(actionKey);
        item.setActionText(actionText);
        item.setColorType(colorType);
        item.setSortOrder(sortOrder);
        return item;
    }

    /**
     * 静态工厂方法，构造带确认消息的按钮动作对象
     *
     * @param actionKey  动作标识
     * @param actionText 按钮文案
     * @param colorType  颜色类型
     * @param sortOrder  排序号
     * @param confirmMsg 确认消息
     * @return 按钮动作对象
     */
    public static PageRenderActionItem of(String actionKey, String actionText, String colorType, int sortOrder, String confirmMsg) {
        PageRenderActionItem item = of(actionKey, actionText, colorType, sortOrder);
        item.setConfirmMsg(confirmMsg);
        return item;
    }

    @Override
    public int compareTo(PageRenderActionItem other) {
        if (other == null || other.sortOrder == null) {
            return -1;
        }
        if (this.sortOrder == null) {
            return 1;
        }
        return this.sortOrder.compareTo(other.sortOrder);
    }

    public String getActionKey() {
        return actionKey;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getColorType() {
        return colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getConfirmMsg() {
        return confirmMsg;
    }

    public void setConfirmMsg(String confirmMsg) {
        this.confirmMsg = confirmMsg;
    }

    @Override
    public String toString() {
        return "PageRenderActionItem{" +
                "actionKey='" + actionKey + '\'' +
                ", actionText='" + actionText + '\'' +
                ", colorType='" + colorType + '\'' +
                ", displayType='" + displayType + '\'' +
                ", visible=" + visible +
                ", sortOrder=" + sortOrder +
                ", confirmMsg='" + confirmMsg + '\'' +
                '}';
    }
}
