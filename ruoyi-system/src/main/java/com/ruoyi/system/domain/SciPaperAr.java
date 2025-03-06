package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SciPaperAr extends BaseEntity {
    private Integer id;
    private Integer ar_id;
    @Excel(name = "批阅意见")
    private String concate;
    private Long uid;
    private String create_time;
    private String state;

    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAr_id() {
        return ar_id;
    }

    public void setAr_id(Integer ar_id) {
        this.ar_id = ar_id;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SciPaperAr{" +
                "id=" + id +
                ", ar_id=" + ar_id +
                ", concate='" + concate + '\'' +
                ", uid=" + uid +
                ", create_time='" + create_time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
