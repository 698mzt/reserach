package com.ruoyi.system.domain;

import java.io.Serializable;

/**
 * 通用状态展示对象，封装状态编码、状态文案、颜色标识
 * 用于后端统一返回状态展示信息，供前端直接渲染状态颜色与文案
 *
 * @author ruoyi
 */
public class PageRenderStatusMeta implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态编码 */
    private String statusCode;

    /** 状态文案 */
    private String statusText;

    /** 颜色类型：primary/success/warning/danger/info/default */
    private String colorType;

    public PageRenderStatusMeta() {
    }

    public PageRenderStatusMeta(String statusCode, String statusText, String colorType) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.colorType = colorType;
    }

    /**
     * 静态工厂方法，快速构造状态展示对象
     *
     * @param statusCode 状态编码
     * @param statusText 状态文案
     * @param colorType  颜色类型
     * @return 状态展示对象
     */
    public static PageRenderStatusMeta of(String statusCode, String statusText, String colorType) {
        return new PageRenderStatusMeta(statusCode, statusText, colorType);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getColorType() {
        return colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    @Override
    public String toString() {
        return "PageRenderStatusMeta{" +
                "statusCode='" + statusCode + '\'' +
                ", statusText='" + statusText + '\'' +
                ", colorType='" + colorType + '\'' +
                '}';
    }
}
