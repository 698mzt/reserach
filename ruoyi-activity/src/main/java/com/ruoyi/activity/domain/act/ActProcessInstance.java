package com.ruoyi.activity.domain.act;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 流程实例实体
 */
public class ActProcessInstance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 流程实例ID */
    private String id;

    /** 流程定义ID */
    private String processDefinitionId;

    /** 流程定义Key */
    private String processDefinitionKey;

    /** 业务Key */
    private String businessKey;

    /** 流程发起人 */
    private String startUserId;

    /** 开始时间 */
    private Date startTime;

    /** 流程状态 */
    private String suspended;

    /** 结束时间 */
    private Date endTime;

    /** 删除原因 */
    private String deleteReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getSuspended() {
        return suspended;
    }

    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }
}