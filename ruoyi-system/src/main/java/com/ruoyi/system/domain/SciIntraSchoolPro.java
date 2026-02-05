package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class SciIntraSchoolPro extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long uid;

    private Integer appid;
    private String agreeurl;
    private String  filingurl;
    /**  */
    private Integer id;
    @Excel(name = "学院", sort = 1)
    private String  yname;
    @Excel(name = "教研室", sort = 2)
    private String  dname;

    /** 工号 */
    @Excel(name = "工号", sort = 3)
    private String loginName;
    
    /** 行政职务 */
    @Excel(name = "行政职务", sort = 4)
    private String adminstrativeTitle;
    
    /** 社会职称 */
    @Excel(name = "社会职称", sort = 5)
    private String socialTitle;
    
    /** 校内职称 */
    @Excel(name = "校内职称", sort = 6)
    private String teaZhicheng;
    
    /** 参与者用户ID（用于分组计算积分） */
    private Integer participantUserId;
    
    /** 积分值 */
    private String changeValue;
    
    /** 排名 */
    @Excel(name = "排名", sort = 14)
    private String ranking;
    
    /** 积分总和（用于导出） */
    @Excel(name = "积分", sort = 15)
    private String totalScore;

    /** 老师获得的总分（用于导出，合计该老师所有项目的积分） */
    @Excel(name = "总分", sort = 16)
    private String totalTeacherScore;

    @Excel(name = "甲方", sort = 8)
    private String pa;
    @Excel(name = "乙方", sort = 9)
    private String pb;

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }
    
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAdminstrativeTitle() {
        return adminstrativeTitle;
    }

    public void setAdminstrativeTitle(String adminstrativeTitle) {
        this.adminstrativeTitle = adminstrativeTitle;
    }

    public String getSocialTitle() {
        return socialTitle;
    }

    public void setSocialTitle(String socialTitle) {
        this.socialTitle = socialTitle;
    }

    public String getTeaZhicheng() {
        return teaZhicheng;
    }

    public void setTeaZhicheng(String teaZhicheng) {
        this.teaZhicheng = teaZhicheng;
    }

    public Integer getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(Integer participantUserId) {
        this.participantUserId = participantUserId;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getTotalTeacherScore() {
        return totalTeacherScore;
    }

    public void setTotalTeacherScore(String totalTeacherScore) {
        this.totalTeacherScore = totalTeacherScore;
    }

    /** 申请人 */
    @Excel(name = "姓名", sort = 7)
    private String  userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Integer userId;
    /** 成果名称 */
    @Excel(name = "成果名称", sort = 7)
    private String topName;

    // 删除课题类型字段
    // @Excel(name = "分类", sort = 13)
    // private String topType;

    /** 转化日期 */
    @Excel(name = "转化日期", sort = 11)
    private String signingData;

    /** 合同有效日期 - 不再使用 */
    @Excel(name = "合同有效日期", sort = 12)
    private String validityDate;

    private String createtime;

    /** 第一负责人 */
    //@Excel(name = "第一负责人")
    private String firstPersonId;



    /** 第二负责人 */
    // @Excel(name = "第二负责人")
    private String secondPersonId;



    /** 第三负责人 */
    // @Excel(name = "第三负责人")
    private String thirdPersonId;



    /** 第四负责人 */
    //  @Excel(name = "第四负责人")
    private String fourthPersonId;

    /** 动态添加的成员IDs - 用于存储第五及以后的负责人ID */
    private List<String> members;

    /** 项目金额 */
    @Excel(name = "项目金额", sort = 10)
    private String amount;

    /** 合同 */
    //@Excel(name = "合同")
    private String contract;

    /** 备案表 */
    //@Excel(name = "备案表")
    private String filing;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    private String year;

    //角色
    private String role;

    //判定登陆人的 专业/学院 是否和当前课题负责人的 专业/学院  是否相等
    private String deptNamekey;

    //结项成果扫描件
    private String overContract;
    //结项成果登记表
    private String overFiling;

    //第一负责人获得积分  这几个字段没有用到
    private String firstPoints;
    private String secondPoints;
    private String thirdPoints;
    private String forthPoints;

  public String getFirstPoints() {
        return firstPoints;
    }

    public void setFirstPoints(String firstPoints) {
        this.firstPoints = firstPoints;
    }

    public String getSecondPoints() {
        return secondPoints;
    }

    public void setSecondPoints(String secondPoints) {
        this.secondPoints = secondPoints;
    }

    public String getThirdPoints() {
        return thirdPoints;
    }

    public void setThirdPoints(String thirdPoints) {
        this.thirdPoints = thirdPoints;
    }

    public String getForthPoints() {
        return forthPoints;
    }

    public void setForthPoints(String forthPoints) {
        this.forthPoints = forthPoints;
    }

    public String getOverContract() {
        return overContract;
    }

    public void setOverContract(String overContract) {
        this.overContract = overContract;
    }

    public String getOverFiling() {
        return overFiling;
    }

    public void setOverFiling(String overFiling) {
        this.overFiling = overFiling;
    }

    public String getDeptNamekey() {
        return deptNamekey;
    }

    public void setDeptNamekey(String deptNamekey) {
        this.deptNamekey = deptNamekey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String urlFlag;

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setTopName(String topName)
    {
        this.topName = topName;
    }

    public String getTopName()
    {
        return topName;
    }

    // 删除课题类型相关方法
    /*
    public void setTopType(String topType)
    {
        this.topType = topType;
    }

    public String getTopType()
    {
        return topType;
    }
    */



    public void setSigningData(String signingData)
    {
        this.signingData = signingData;
    }

    public String getSigningData()
    {
        return signingData;
    }

    public void setValidityDate(String validityDate)
    {
        this.validityDate = validityDate;
    }

    public String getValidityDate()
    {
        return validityDate;
    }

    public void setFirstPersonId(String firstPersonId)
    {
        this.firstPersonId = firstPersonId;
    }

    public String getFirstPersonId()
    {
        return firstPersonId;
    }

    public void setSecondPersonId(String secondPersonId)
    {
        this.secondPersonId = secondPersonId;
    }

    public String getSecondPersonId()
    {
        return secondPersonId;
    }

    public void setThirdPersonId(String thirdPersonId)
    {
        this.thirdPersonId = thirdPersonId;
    }

    public String getThirdPersonId()
    {
        return thirdPersonId;
    }



    public void setFourthPersonId(String fourthPersonId)
    {
        this.fourthPersonId = fourthPersonId;
    }

    public String getFourthPersonId()
    {
        return fourthPersonId;
    }

    /**
     * 获取动态添加的成员IDs
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * 设置动态添加的成员IDs
     */
    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setContract(String contract)
    {
        this.contract = contract;
    }

    public String getContract()
    {
        return contract;
    }

    public void setFiling(String filing)
    {
        this.filing = filing;
    }

    public String getFiling()
    {
        return filing;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getAgreeurl() {
        return agreeurl;
    }

    public void setAgreeurl(String agreeurl) {
        this.agreeurl = agreeurl;
    }

    public String getFilingurl() {
        return filingurl;
    }

    public void setFilingurl(String filingurl) {
        this.filingurl = filingurl;
    }

    @Override
    public String toString() {
        return "SciIntraSchoolPro{" +
                "uid=" + uid +
                ", appid=" + appid +
                ", agreeurl='" + agreeurl + '\'' +
                ", filingurl='" + filingurl + '\'' +
                ", id=" + id +
                ", yname='" + yname + '\'' +
                ", dname='" + dname + '\'' +
                ", pa='" + pa + '\'' +
                ", pb='" + pb + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", topName='" + topName + '\'' +
                ", signingData='" + signingData + '\'' +
                ", validityDate='" + validityDate + '\'' +
                ", createtime='" + createtime + '\'' +
                ", firstPersonId='" + firstPersonId + '\'' +
                ", secondPersonId='" + secondPersonId + '\'' +
                ", thirdPersonId='" + thirdPersonId + '\'' +
                ", fourthPersonId='" + fourthPersonId + '\'' +
                ", amount='" + amount + '\'' +
                ", contract='" + contract + '\'' +
                ", filing='" + filing + '\'' +
                ", state='" + state + '\'' +
                ", year='" + year + '\'' +
                ", urlFlag='" + urlFlag + '\'' +
                '}';
    }

    public String getStateDes() {
        if (this.state == null) {
            return "";
        }
        switch (this.state){
            case "1":
                return "开题：待教研室处理";
            case "2":
                return "开题：待科研室处理";
            case "3":
                return "开题：教研室退回";
            case "4":
                return "待申请结题";
            case "5":
                return "开题：科研室退回";
            case "6":
                return "完结啦！！";
            case "7":
                return "结题：待教研室处理";
            case "8":
                return "结题：待科研室处理";
            case "9":
                return "结题：教研室退回";
            case "10":
                return "结题：科研室退回";
            case "11":
                return "开题：待学院处理";
            case "12":
                return "开题：学院退回";
            case "13":
                return "结题：待学院处理";
            case "14":
                return "结题：学院退回";
            case "15":
                return "草稿箱";
            case "16":
                return "结项草稿";
        }
        return this.state;
    }
}