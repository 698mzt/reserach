package com.ruoyi.activity.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表单实例实体
 */
public class FormInstance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 表单实例ID */
    private Long instanceId;

    /** 表单定义ID */
    private Long formDefinitionId;

    /** 表单标识 */
    private String formKey;

    /** 表单实例标题 */
    private String instanceTitle;

    /** 表单数据 */
    private String formData;

    /** 提交人ID */
    private Long submitterId;

    /** 提交人姓名 */
    private String submitterName;

    /** 流程实例ID */
    private String processInstanceId;

    /** 当前流程状态 */
    private String processStatus;

    /** 状态（0草稿 1已提交 2已完成 3已拒绝） */
    private String status;

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(Long formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getInstanceTitle() {
        return instanceTitle;
    }

    public void setInstanceTitle(String instanceTitle) {
        this.instanceTitle = instanceTitle;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}