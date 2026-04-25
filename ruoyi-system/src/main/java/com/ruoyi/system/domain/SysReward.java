package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private Long userId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String userName;

    /** 判断教研室还是科研处 */
    private String urlFlag;

    @Excel(name = "学院")
    private String yname;

    @Excel(name = "教研室")
    private String dname;

    /** 部门ID */
    private Long deptId;

    /** 父部门ID */
    private Long parentId;

    /** 积分 */
    @Excel(name = "积分")
    private String jifen;

    /** 预计科研分 */
    @Excel(name = "预计科研分")
    private String expectedJifen;

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

    /** 第一负责人 */
    private String firstPersonId;

    /** 第二负责人 */
    private String secondPersonId;

    /** 第三负责人 */
    private String thirdPersonId;

    /** 第四负责人 */
    private String fourthPersonId;

    /** 更多成员（第5位及以后），逗号分隔存储 */
    @Excel(name = "其他成员")
    private String extraMemberIds;


    // 用于前端展示的列表（不映射数据库）
    @Excel(name = "其他成员列表")
    private List<String> extraMembers;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 用户角色字段 */
    private String role;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getExpectedJifen() {
        return expectedJifen;
    }

    public void setExpectedJifen(String expectedJifen) {
        this.expectedJifen = expectedJifen;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardDanwei() {
        return rewardDanwei;
    }

    public void setRewardDanwei(String rewardDanwei) {
        this.rewardDanwei = rewardDanwei;
    }

    public String getRewardLeixing() {
        return rewardLeixing;
    }

    public void setRewardLeixing(String rewardLeixing) {
        this.rewardLeixing = rewardLeixing;
    }

    public String getRewardWenjian() {
        return rewardWenjian;
    }

    public void setRewardWenjian(String rewardWenjian) {
        this.rewardWenjian = rewardWenjian;
    }

    public Date getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(Date rewardTime) {
        this.rewardTime = rewardTime;
    }

    public String getRewardFenlei() {
        return rewardFenlei;
    }

    public void setRewardFenlei(String rewardFenlei) {
        this.rewardFenlei = rewardFenlei;
    }

    public String getRewardDengji() {
        return rewardDengji;
    }

    public void setRewardDengji(String rewardDengji) {
        this.rewardDengji = rewardDengji;
    }

    public String getRewardPaiming() {
        return rewardPaiming;
    }

    public void setRewardPaiming(String rewardPaiming) {
        this.rewardPaiming = rewardPaiming;
    }

    public String getRewardJibie() {
        return rewardJibie;
    }

    public void setRewardJibie(String rewardJibie) {
        this.rewardJibie = rewardJibie;
    }

    public String getFirstPersonId() {
        return firstPersonId;
    }

    public void setFirstPersonId(String firstPersonId) {
        this.firstPersonId = firstPersonId;
    }

    public String getSecondPersonId() {
        return secondPersonId;
    }

    public void setSecondPersonId(String secondPersonId) {
        this.secondPersonId = secondPersonId;
    }

    public String getThirdPersonId() {
        return thirdPersonId;
    }

    public void setThirdPersonId(String thirdPersonId) {
        this.thirdPersonId = thirdPersonId;
    }

    public String getFourthPersonId() {
        return fourthPersonId;
    }

    public void setFourthPersonId(String fourthPersonId) {
        this.fourthPersonId = fourthPersonId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExtraMemberIds() {
        return extraMemberIds;
    }

    public void setExtraMemberIds(String extraMemberIds) {
        this.extraMemberIds = extraMemberIds;
        // 自动解析为列表
        if (extraMemberIds != null && !extraMemberIds.isEmpty()) {
            this.extraMembers = Arrays.asList(extraMemberIds.split(","));
        } else {
            this.extraMembers = new ArrayList<>();
        }
    }
    public List<String> getExtraMembers() {
        return extraMembers;
    }

    public void setExtraMembers(List<String> extraMembers) {
        this.extraMembers = extraMembers;
        // 反向转换为字符串
        if (extraMembers != null && !extraMembers.isEmpty()) {
            this.extraMemberIds = String.join(",", extraMembers);
        }
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("uid", getUid())
                .append("id", getId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("urlFlag", getUrlFlag())
                .append("yname", getYname())
                .append("dname", getDname())
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("jifen", getJifen())
                .append("expectedJifen", getExpectedJifen())
                .append("rewardName", getRewardName())
                .append("rewardDanwei", getRewardDanwei())
                .append("rewardLeixing", getRewardLeixing())
                .append("rewardWenjian", getRewardWenjian())
                .append("rewardTime", getRewardTime())
                .append("rewardFenlei", getRewardFenlei())
                .append("rewardDengji", getRewardDengji())
                .append("rewardPaiming", getRewardPaiming())
                .append("rewardJibie", getRewardJibie())
                .append("firstPersonId", getFirstPersonId())
                .append("secondPersonId", getSecondPersonId())
                .append("thirdPersonId", getThirdPersonId())
                .append("fourthPersonId", getFourthPersonId())
                .append("extraMemberIds", getExtraMemberIds())
                .append("state", getState())
                .append("role", getRole())
                .toString();
    }

    public String getStateDes() {
        if (this.state == null) {
            return "";
        }
        switch (this.state) {
            case "REWARD_DRAFT": return "草稿箱";
            case "REWARD_JYS_AUDIT": return "奖励-教研室审批";
            case "REWARD_KYC_AUDIT": return "奖励-科研处审批";
            case "REWARD_PASSED": return "奖励-通过";
            case "REWARD_REJECTED": return "奖励-驳回";
            default: return this.state;
        }
    }
}