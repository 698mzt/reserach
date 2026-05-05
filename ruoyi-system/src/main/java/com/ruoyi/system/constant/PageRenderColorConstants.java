package com.ruoyi.system.constant;

/**
 * 页面渲染通用颜色常量
 * 定义状态颜色和按钮颜色的统一枚举值，确保前后端颜色协议一致
 *
 * @author ruoyi
 */
public class PageRenderColorConstants {

    /** 蓝色 - 用于审批中等进行中状态 */
    public static final String COLOR_PRIMARY = "primary";

    /** 绿色 - 用于审批通过等成功状态 */
    public static final String COLOR_SUCCESS = "success";

    /** 黄色 - 用于草稿、待提交等警告状态 */
    public static final String COLOR_WARNING = "warning";

    /** 红色 - 用于驳回、删除等危险操作 */
    public static final String COLOR_DANGER = "danger";

    /** 灰色 - 用于撤回、信息展示等中性状态 */
    public static final String COLOR_INFO = "info";

    /** 默认 - 用于普通状态 */
    public static final String COLOR_DEFAULT = "default";

    private PageRenderColorConstants() {
    }
}
