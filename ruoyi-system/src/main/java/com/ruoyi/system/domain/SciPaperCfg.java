package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SciPaperCfg extends BaseEntity {
    private Integer id;
    private String order;
    private String user_order;
    private String points;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


    public String getUser_order() {
        return user_order;
    }

    public void setUser_order(String user_order) {
        this.user_order = user_order;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
