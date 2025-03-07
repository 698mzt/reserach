package com.ruoyi.system.domain;

public class SciHorizontalPersion {
    private int id;
    private Integer applyid;
    private Integer verticalid;
    private String persionid;
    private String ranking;



    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public Integer getVerticalid() {
        return verticalid;
    }

    public void setVerticalid(Integer verticalid) {
        this.verticalid = verticalid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersionid() {
        return persionid;
    }

    public void setPersionid(String persionid) {
        this.persionid = persionid;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
