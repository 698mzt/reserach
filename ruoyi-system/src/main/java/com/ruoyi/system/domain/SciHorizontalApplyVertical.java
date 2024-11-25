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
    @Excel(name = "学院")
    private String  yname;
    @Excel(name = "专业")
    private String  dname;


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
    /** 申请文件 */
    private String file;
    /** 开题文件 */
    private String openfile;
    /** 中期文件 */
    private String midfile;
    /** 结项文件 */
    private String overfile;
    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 角色 */
    private String role;

    /** 创建时间*/
    private String createtime;

    private String urlFlag;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
