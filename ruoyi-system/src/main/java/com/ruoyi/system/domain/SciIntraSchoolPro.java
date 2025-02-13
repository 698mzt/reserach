package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SciIntraSchoolPro extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long uid;

    private Integer appid;
    private String agreeurl;
    private String  filingurl;
    /**  */
    private Integer id;
    @Excel(name = "学院")
    private String  yname;
    @Excel(name = "专业")
    private String  dname;

    private String pa;
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

    /** 申请人 */
    @Excel(name = "申请人")
    private String  userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Integer userId;
    /** 课题名称 */
    @Excel(name = "课题名称")
    private String topName;

    /** 课题类型 */
    // @Excel(name = "课题类型")
    private String topType;

    /** 签订日期 */
    @Excel(name = "申请日期")
    private String signingData;

    /** 合同有效日期 */
    @Excel(name = "结项日期")
    private String validityDate;

    @Excel(name = "申请结项日期")
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



    /** 项目金额 */
    @Excel(name = "项目金额")
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
                ", topType='" + topType + '\'' +
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
}
