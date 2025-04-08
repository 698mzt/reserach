package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SciIntraSchProPiyue extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**  */
    private Integer id;

    /** 课题id */
    @Excel(name = "课题id")
    private Integer schxktId;

    /** 驳回意见 */
    //@Excel(name = "驳回意见")
    @Excel(name = "操作")
    private String concate;

    /** 提交人 */
    @Excel(name = "提交人")
    private Long uid;

    private String uname;

    private String state;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchxktId() {
        return schxktId;
    }

    public void setSchxktId(Integer schxktId) {
        this.schxktId = schxktId;
    }

    public String getConcate() {
        return concate;
    }

    public void setConcate(String concate) {
        this.concate = concate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "SciIntraSchProPiyue{" +
                "id=" + id +
                ", schxktId=" + schxktId +
                ", concate='" + concate + '\'' +
                ", uid=" + uid +
                ", uname='" + uname + '\'' +
                '}';
    }
}
