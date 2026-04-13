package com.ruoyi.activity.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表单定义实体
 */
public class FormDefinition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 表单定义ID */
    private Long formId;

    /** 表单标识 */
    private String formKey;

    /** 表单名称 */
    private String formName;

    /** 表单标题 */
    private String formTitle;

    /** 表单Schema定义 */
    private String formSchema;

    /** 表单UI Schema定义 */
    private String formUiSchema;

    /** 关联的流程定义键 */
    private String processDefinitionKey;

    /** 表单分类 */
    private String category;

    /** 状态（0正常 1停用） */
    private String status;

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormSchema() {
        return formSchema;
    }

    public void setFormSchema(String formSchema) {
        this.formSchema = formSchema;
    }

    public String getFormUiSchema() {
        return formUiSchema;
    }

    public void setFormUiSchema(String formUiSchema) {
        this.formUiSchema = formUiSchema;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}