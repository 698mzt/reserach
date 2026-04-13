package com.ruoyi.activity.domain.act;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 流程定义实体
 */
public class ActProcessDefinition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 流程定义ID */
    private String id;

    /** 流程定义名称 */
    private String name;

    /** 流程定义Key */
    private String key;

    /** 版本 */
    private Integer version;

    /** 部署ID */
    private String deploymentId;

    /** 流程资源名称 */
    private String resourceName;

    /** 流程图片名称 */
    private String diagramResourceName;

    /** 挂起状态 */
    private Integer suspensionState;

    /** 描述 */
    private String description;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public Integer getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(Integer suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}