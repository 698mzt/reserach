package com.ruoyi.system.domain;

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
    @Excel(name = "学院")
    private String  yname;
    @Excel(name = "专业")
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
    /** 课题名称 */
    @Excel(name = "项目名称")
    private String topName;
    /** 课题编号*/
    private String topNumber;

    /** 申请人 */
    @Excel(name = "主持人姓名")
    private String  userName;
    /** 甲方*/
    @Excel(name = "甲方")
    private String partyA;
    /** 乙方*/
    @Excel(name = "乙方")
    private String partyB;
    /** 项目金额 */
    @Excel(name = "项目金额")
    private String amount;
    /** 课题类型 */
    // @Excel(name = "课题类型")
    private String topType;

    /** 签订日期 */
    @Excel(name = "起始时间")
    private String signingData;
    /** 合同有效日期 */
    @Excel(name = "终止时间")
    private String validityDate;

    private String persionId;
    /** 排名 */
    @Excel(name = "排名")
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
    @Excel(name = "状态")
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
}
