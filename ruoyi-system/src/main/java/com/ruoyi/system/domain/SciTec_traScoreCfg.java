package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 成果转化得分配置对象 sci_tec_tra_score_cfg
 */
public class SciTec_traScoreCfg {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long tec_tra_id;

    /**
     * 经费小于
     */
    private String funds_max;

    /**
     * 经费大于
     */
    private String funds_min;

    /**
     * 负责人排名
     */
    private String user_order;

    /**
     * 总分
     */
    private String total_score;

    /**
     * 开题得分
     */
    private String start_score;

    /**
     * 结题得分
     */
    private String end_score;

    /**
     * 修改人
     */
    private String update_user;

    /**
     * 修改时间
     */
    private String update_time;

    public Long getTec_tra_id() {
        return tec_tra_id;
    }

    public void setTec_tra_id(Long tec_tra_id) {
        this.tec_tra_id = tec_tra_id;
    }

    public String getFunds_max() {
        return funds_max;
    }

    public void setFunds_max(String funds_max) {
        this.funds_max = funds_max;
    }

    public String getFunds_min() {
        return funds_min;
    }

    public void setFunds_min(String funds_min) {
        this.funds_min = funds_min;
    }

    public String getUser_order() {
        return user_order;
    }

    public void setUser_order(String user_order) {
        this.user_order = user_order;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getStart_score() {
        return start_score;
    }

    public void setStart_score(String start_score) {
        this.start_score = start_score;
    }

    public String getEnd_score() {
        return end_score;
    }

    public void setEnd_score(String end_score) {
        this.end_score = end_score;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("tec_tra_id", getTec_tra_id())
                .append("funds_max", getFunds_max())
                .append("funds_min", getFunds_min())
                .append("user_order", getUser_order())
                .append("total_score", getTotal_score())
                .append("start_score", getStart_score())
                .append("end_score", getEnd_score())
                .append("update_user", getUpdate_user())
                .append("update_time", getUpdate_time())
                .toString();
    }
}
