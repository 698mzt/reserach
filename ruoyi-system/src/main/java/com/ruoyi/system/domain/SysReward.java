package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 奖励对象 sys_reward
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
public class SysReward extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long uid;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "申请人id")
    private Long userId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String  userName;

    /** 判断教研室还是科研处 */
    private String urlFlag;

    @Excel(name = "学院")
    private String  yname;
    @Excel(name = "专业")
    private String  dname;

    @Excel(name = "积分")
    private String  jifen;

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    /** 荣誉名称 */
    @Excel(name = "荣誉名称")
    private String rewardName;

    /** 颁发单位 */
    @Excel(name = "颁发单位")
    private String rewardDanwei;

    /** 奖励类型 */
    @Excel(name = "奖励类型")
    private String rewardLeixing;

    /** 证书文件 */
    @Excel(name = "证书文件")
    private String rewardWenjian;

    /** 颁发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "颁发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rewardTime;

    /** 分类 */
    @Excel(name = "分类")
    private String rewardFenlei;

    /** 等级 */
    @Excel(name = "等级")
    private String rewardDengji;

    /** 排名 */
    @Excel(name = "排名")
    private String rewardPaiming;

    /** 级别 */
    @Excel(name = "级别")
    private String rewardJibie;


    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 用户角色字段 */
    private String role;

    public String getYname() {
        return yname;
    }

    public void setYname(String yname) {
        this.yname = yname;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    public String getState() {return state;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setState(String state) {this.state = state;}
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setRewardName(String rewardName) 
    {
        this.rewardName = rewardName;
    }

    public String getRewardName() 
    {
        return rewardName;
    }
    public void setRewardDanwei(String rewardDanwei) 
    {
        this.rewardDanwei = rewardDanwei;
    }

    public String getRewardDanwei() 
    {
        return rewardDanwei;
    }
    public void setRewardLeixing(String rewardLeixing) 
    {
        this.rewardLeixing = rewardLeixing;
    }

    public String getRewardLeixing() 
    {
        return rewardLeixing;
    }
    public void setRewardWenjian(String rewardWenjian) 
    {
        this.rewardWenjian = rewardWenjian;
    }

    public String getRewardWenjian() 
    {
        return rewardWenjian;
    }
    public void setRewardTime(Date rewardTime) 
    {
        this.rewardTime = rewardTime;
    }

    public Date getRewardTime() 
    {
        return rewardTime;
    }
    public void setRewardFenlei(String rewardFenlei) 
    {
        this.rewardFenlei = rewardFenlei;
    }

    public String getRewardFenlei() 
    {
        return rewardFenlei;
    }
    public void setRewardDengji(String rewardDengji) 
    {
        this.rewardDengji = rewardDengji;
    }

    public String getRewardDengji() 
    {
        return rewardDengji;
    }
    public void setRewardPaiming(String rewardPaiming) 
    {
        this.rewardPaiming = rewardPaiming;
    }

    public String getRewardPaiming() 
    {
        return rewardPaiming;
    }
    public void setRewardJibie(String rewardJibie) 
    {
        this.rewardJibie = rewardJibie;
    }

    public String getRewardJibie() 
    {
        return rewardJibie;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("rewardName", getRewardName())
            .append("rewardDanwei", getRewardDanwei())
            .append("rewardLeixing", getRewardLeixing())
            .append("rewardWenjian", getRewardWenjian())
            .append("rewardTime", getRewardTime())
            .append("rewardFenlei", getRewardFenlei())
            .append("rewardDengji", getRewardDengji())
            .append("rewardPaiming", getRewardPaiming())
            .append("rewardJibie", getRewardJibie())
            .toString();
    }
}
