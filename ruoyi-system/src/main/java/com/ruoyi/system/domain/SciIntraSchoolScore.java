package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SciIntraSchoolScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public SciIntraSchoolScore() {
    }

    public SciIntraSchoolScore( Integer sciIntraSchid, Integer sciIntraSchResponTier, Integer earnedPoints, Integer userid, Integer earnedJTPoints) {
        this.sciIntraSchid = sciIntraSchid;
        this.sciIntraSchResponTier = sciIntraSchResponTier;
        this.earnedPoints = earnedPoints;
        this.userid = userid;
        this.earnedJTPoints = earnedJTPoints;

    }

    //校内横向课题积分id
    private Integer intraschid;

    //校内横向课题id
    private Integer sciIntraSchid;

    //责任层级
    private Integer sciIntraSchResponTier;

    //已获开题积分
    private Integer earnedPoints;

    //该层级负责人id
    private Integer userid;

    //已获结题积分
    private Integer earnedJTPoints;

    //已获总积分
    private Integer earnedALLPoints;

    public Integer getEarnedJTPoints() {
        return earnedJTPoints;
    }

    public void setEarnedJTPoints(Integer earnedJTPoints) {
        this.earnedJTPoints = earnedJTPoints;
    }

    public Integer getEarnedALLPoints() {
        return earnedALLPoints;
    }

    public void setEarnedALLPoints(Integer earnedALLPoints) {
        this.earnedALLPoints = earnedALLPoints;
    }

    public Integer getIntraschid() {
        return intraschid;
    }

    public void setIntraschid(Integer intraschid) {
        this.intraschid = intraschid;
    }

    public Integer getSciIntraSchid() {
        return sciIntraSchid;
    }

    public void setSciIntraSchid(Integer sciIntraSchid) {
        this.sciIntraSchid = sciIntraSchid;
    }

    public Integer getSciIntraSchResponTier() {
        return sciIntraSchResponTier;
    }

    public void setSciIntraSchResponTier(Integer sciIntraSchResponTier) {
        this.sciIntraSchResponTier = sciIntraSchResponTier;
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "SciIntraSchoolScore{" +
                "intraschid=" + intraschid +
                ", sciIntraSchid=" + sciIntraSchid +
                ", sciIntraSchResponTier='" + sciIntraSchResponTier + '\'' +
                ", earnedPoints=" + earnedPoints +
                ", userid=" + userid +
                '}';
    }
}
