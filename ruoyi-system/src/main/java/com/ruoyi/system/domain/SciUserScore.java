package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SciUserScore extends BaseEntity
{
    private Integer id;
    /**负责人*/
    private String userId;
    /**创建数据id*/
    private String applyId;
    /**创建时间*/
    private String Createtime;
    /**积分类型*/
    private String scoreType;
    /**立项或结项*/
    private String changeStatus;
    /**积分*/
    private String changeValue;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String createtime) {
        Createtime = createtime;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    @Override
    public String toString() {
        return "SciUserScore{" +
                "userId='" + userId + '\'' +
                ", Createtime='" + Createtime + '\'' +
                ", scoreType='" + scoreType + '\'' +
                ", changeStatus='" + changeStatus + '\'' +
                ", changeValue='" + changeValue + '\'' +
                '}';
    }
}
