package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SciHorizontalApplyVertical extends BaseEntity {
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


    /** 申请人 */
    @Excel(name = "老师名称", sort = 3)
    private String  userName;

    /** 工号 */
    private String loginName;

    /** 积分值 */
    private String changeValue;

    /** 积分状态（立项/结项） */
    private String changeStatus;

    /** 参与者用户ID（用于分组计算积分） */
    private Integer participantUserId;

    /** 积分总和（用于导出） */
    @Excel(name = "积分", sort = 7)
    private String totalScore;

    /** 老师获得的总分（用于导出，合计该老师所有项目的积分） */
    @Excel(name = "总分", sort = 8)
    private String totalTeacherScore;

    /** 角色类型（用于数据权限控制） */
    private String roleType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Integer getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(Integer participantUserId) {
        this.participantUserId = participantUserId;
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

    private Integer userId;
    /** 课题名称 */
    @Excel(name = "课题名称", sort = 5)
    private String topName;
    /** 课题编号 */
    private String topNumber;
    /** 课题类型 */
    // @Excel(name = "课题类型")
    private String topType;
    /** 签订日期 */
    @Excel(name = "立项日期", sort = 9)
    private String signingData;
    @Excel(name = "结项日期", sort = 10)
    private String validityData;
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
    /** 项目金额 */
    @Excel(name = "项目金额", sort = 11)
    private String amount;
    /** 申请文件 */
    private String file;
    /** 开题文件 */
    private String openfile;
    /** 中期文件 */
    private String midfile;
    /** 结项文件 */
    private String overfile;
    /** 状态 */
    @Excel(name = "状态", sort = 12)
    private String state;

    /** 课题来源 */
    @Excel(name = "课题来源", sort = 4)
    private String subjectSource;

    /** 排名 */
    @Excel(name = "排名", sort = 6)
    private String ranking;

    public String getSubjectSource() {
        return subjectSource;
    }

    public void setSubjectSource(String subjectSource) {
        this.subjectSource = subjectSource;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    /** 角色 */
    private String role;

    /** 创建时间*/
    private String createtime;

    private String urlFlag;

    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOpenfile() {
        return openfile;
    }

    public void setOpenfile(String openfile) {
        this.openfile = openfile;
    }

    public String getMidfile() {
        return midfile;
    }

    public void setMidfile(String midfile) {
        this.midfile = midfile;
    }

    public String getOverfile() {
        return overfile;
    }

    public void setOverfile(String overfile) {
        this.overfile = overfile;
    }

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getTopType() {
        return topType;
    }

    public void setTopType(String topType) {
        this.topType = topType;
    }

    public String getSigningData() {
        return signingData;
    }

    public void setSigningData(String signingData) {
        this.signingData = signingData;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getValidityData() {
        return validityData;
    }

    public void setValidityData(String validityData) {
        this.validityData = validityData;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(String topNumber) {
        this.topNumber = topNumber;
    }

    private String reAmount; //追加金额
    private String amountType; //类型
    private String accountData; //到账日期
    private String  reamountUrl;     //追加金额材料
    private Integer  dnameId; //登录用户专业id
    private Integer  userdnameId; //登录用户专业id
    private String  ynameId;  //学院id
    private Integer  userynameId; //登录用户专业id
    private Integer  reid;
    private String newsql;
    private String tableId;
    private String score;
    private String expectedScore;
    private String expectedScore1;
    private String expectedScore2;
    private String expectedScore3;
    private String expectedScore4;

    public String getReAmount() {
        return reAmount;
    }

    public void setReAmount(String reAmount) {
        this.reAmount = reAmount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAccountData() {
        return accountData;
    }

    public void setAccountData(String accountData) {
        this.accountData = accountData;
    }

    public String getReamountUrl() {
        return reamountUrl;
    }

    public void setReamountUrl(String reamountUrl) {
        this.reamountUrl = reamountUrl;
    }

    public Integer getDnameId() {
        return dnameId;
    }

    public void setDnameId(Integer dnameId) {
        this.dnameId = dnameId;
    }

    public Integer getUserdnameId() {
        return userdnameId;
    }

    public void setUserdnameId(Integer userdnameId) {
        this.userdnameId = userdnameId;
    }

    public String getYnameId() {
        return ynameId;
    }

    public void setYnameId(String ynameId) {
        this.ynameId = ynameId;
    }

    public Integer getUserynameId() {
        return userynameId;
    }

    public void setUserynameId(Integer userynameId) {
        this.userynameId = userynameId;
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public String getNewsql() {
        return newsql;
    }

    public void setNewsql(String newsql) {
        this.newsql = newsql;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getExpectedScore() {
        return expectedScore;
    }

    public void setExpectedScore(String expectedScore) {
        this.expectedScore = expectedScore;
    }

    public String getExpectedScore1() {
        return expectedScore1;
    }

    public void setExpectedScore1(String expectedScore1) {
        this.expectedScore1 = expectedScore1;
    }

    public String getExpectedScore2() {
        return expectedScore2;
    }

    public void setExpectedScore2(String expectedScore2) {
        this.expectedScore2 = expectedScore2;
    }

    public String getExpectedScore3() {
        return expectedScore3;
    }

    public void setExpectedScore3(String expectedScore3) {
        this.expectedScore3 = expectedScore3;
    }

    public String getExpectedScore4() {
        return expectedScore4;
    }

    public void setExpectedScore4(String expectedScore4) {
        this.expectedScore4 = expectedScore4;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getStateDes(){
        // 添加空值检查
        if (this.state == null) {
            return "";
        }
        switch (this.state){
            case "1":
                return "待处理";
            case "2":
                return "待学院审核";
            case "3":
                return "教研室退回";
            case "4":
                return "待科研处审核";
            case "5":
                return "学院退回";
            case "6":
                return "待结项";
            case "7":
                return "科研处退回";
            case "11":
                return "结项：待处理";
            case "22":
                return "结项：待学院审核";
            case "33":
                return "结项：教研室退回";
            case "44":
                return "结项：待科研处审核";
            case "55":
                return "结项：学院退回";
            case "66":
                return "已结项";
            case "77":
                return "结项：科研处退回";
            case "99":
                return "立项草稿箱";
            case "999":
                return "结项草稿箱";
        }
        return this.state;
    }
}
