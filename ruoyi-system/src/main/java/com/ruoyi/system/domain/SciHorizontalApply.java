package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 横向课题对象 sci_horizontal_apply
 *
 * @author zhansan
 * @date 2024-08-16
 */
@Data
public class SciHorizontalApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long uid;
    private String tableId;
    private String newsql;

    private String agreeurl;
    private String  filingurl;
    /**  */
    private Integer id;
    @Excel(name = "学院", sort = 1)
    private String  yname;
    @Excel(name = "教研室", sort = 2)
    private String  dname;


    private String reAmount; //追加金额
    private String amountType; //类型
    private String accountData; //到账日期
    private String  reamountUrl;     //追加金额材料
    private Integer  dnameId; //登录用户专业id
    private Integer  userdnameId; //登录用户专业id
    private String  ynameId;  //学院id
    private Integer  userynameId; //登录用户专业id
    private Integer  reid;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Integer userId;
    
    /** 工号 */
    @Excel(name = "工号", sort = 3)
    private String loginName;
    
    /** 申请人 */
    @Excel(name = "姓名", sort = 4)
    private String  userName;
    
    /** 行政职务 */
    @Excel(name = "行政职务", sort = 5)
    private String adminstrativeTitle;
    
    /** 社会职称 */
    @Excel(name = "社会职称", sort = 6)
    private String socialTitle;
    
    /** 校内职称 */
    @Excel(name = "校内职称", sort = 7)
    private String teaZhicheng;
    
    /** 参与者用户ID（用于分组计算积分） */
    private Integer participantUserId;
    
    /** 积分值 */
    private String changeValue;

    /** 积分状态（立项/结项） */
    private String changeStatus;
    
    /** 积分总和（用于导出） */
    @Excel(name = "积分", sort = 10)
    private String totalScore;

    /** 老师获得的总分（用于导出，合计该老师所有项目的积分） */
    @Excel(name = "总分", sort = 11)
    private String totalTeacherScore;
    
    /** 课题名称 */
    @Excel(name = "课题名称", sort = 8)
    private String topName;
    /** 课题编号*/
    private String topNumber;

    /** 甲方*/
    @Excel(name = "甲方", sort = 13)
    private String partyA;
    /** 乙方*/
    @Excel(name = "乙方", sort = 14)
    private String partyB;
    /** 项目金额 */
    @Excel(name = "金额", sort = 15)
    private String amount;

    /** 到账金额 */
    private String creditedAmount;

    /** 课题类型 */
    // @Excel(name = "课题类型")
    private String topType;

    /** 签订日期 */
    @Excel(name = "立项日期", sort = 16)
    private String signingData;
    /** 合同有效日期 */
    @Excel(name = "结项日期", sort = 17)
    private String validityDate;

    private String persionId;
    /** 排名 */
    @Excel(name = "排名", sort = 9)
    private String ranking;



    private String createtime;

    private String overtime;
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





    /** 合同 */
    //@Excel(name = "合同")
    private String contract;

    /** 备案表 */
    //@Excel(name = "备案表")
    private String filing;

    /** 状态 */
    @Excel(name = "状态", sort = 18)
    private String state;

//    用户角色字段
    private String role;

//    查询年份
    private String year;

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public void setTopType(String topType)
    {
        this.topType = topType;
    }

    public String getTopType()
    {
        return topType;
    }



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

    public String getPersionId() {
        return persionId;
    }

    public void setPersionId(String persionId) {
        this.persionId = persionId;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
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

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getNewsql() {
        return newsql;
    }

    public void setNewsql(String newsql) {
        this.newsql = newsql;
    }



    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(String topNumber) {
        this.topNumber = topNumber;
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

    public String getReAmount() {
        return reAmount;
    }

    public void setReAmount(String reAmount) {
        this.reAmount = reAmount;
    }

    public String getReamountUrl() {
        return reamountUrl;
    }

    public void setReamountUrl(String reamountUrl) {
        this.reamountUrl = reamountUrl;
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public String getAccountData() {
        return accountData;
    }

    public void setAccountData(String accountData) {
        this.accountData = accountData;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getStateDes() {
        if (this.state == null) {
            return "";
        }
        switch (this.state){
            case "1":
                return "待处理";
            case "2":
                return "教研室通过";
            case "3":
                return "教研室退回";
            case "4":
                return "科研处通过";
            case "5":
                return "科研处退回";
            case "6":
                return "已完结";
            case "7":
                return "结项：待处理";
            case "8":
                return "结项：待学院审核";
            case "9":
                return "结项：教研室退回";
            case "10":
                return "结项：科研处退回";
            case "11":
                return "学院通过";
            case "22":
                return "学院退回";
            case "33":
                return "结项：待科研处审核";
            case "44":
                return "结项：学院退回";
            case "99":
                return "草稿箱";
        }
        return this.state;
    }
}
