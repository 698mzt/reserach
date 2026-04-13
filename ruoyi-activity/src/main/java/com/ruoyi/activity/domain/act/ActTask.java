package com.ruoyi.activity.domain.act;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 流程任务实体
 */
public class ActTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private String id;

    /** 任务名称 */
    private String name;

    /** 任务Key */
    private String taskDefinitionKey;

    /** 流程实例ID */
    private String processInstanceId;

    /** 流程定义ID */
    private String processDefinitionId;

    /** 任务执行人 */
    private String assignee;

    /** 任务候选组 */
    private String candidateGroup;

    /** 任务创建时间 */
    private Date createTime;

    /** 任务到期时间 */
    private Date dueDate;

    /** 任务优先级 */
    private Integer priority;

    /** 任务状态 */
    private String suspended;

    /** 流程名称 */
    private String processName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCandidateGroup() {
        return candidateGroup;
    }

    public void setCandidateGroup(String candidateGroup) {
        this.candidateGroup = candidateGroup;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSuspended() {
        return suspended;
    }

    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}