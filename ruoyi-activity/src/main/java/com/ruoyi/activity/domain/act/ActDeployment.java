package com.ruoyi.activity.domain.act;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 流程部署实体
 */
public class ActDeployment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 部署ID */
    private String id;

    /** 部署名称 */
    private String name;

    /** 部署时间 */
    private Date deploymentTime;

    /** 分类 */
    private String category;

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

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}