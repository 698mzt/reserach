package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文对象 sci_paper_a
 *
 * @author ruoyi
 * @date 2024-11-07
 */
public class SciPaperA extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Long id;
    private String urlFlag;
    private Long userId;

    private String roleId;
    private String roleName;
    private String rolekey;



    //user表
    private Long uid;

    private String state;
    /**
     * 年份
     */
    private String year;

    /**
     * 学院
     */
    @Excel(name = "学院")
    private String college;

    /**
     * 教研室
     */
    @Excel(name = "教研室")
    private String researchRoom;

    /**
     * 工号
     */
    @Excel(name = "工号")
    private String employeeId;

    /**
     * 教师姓名
     */
    @Excel(name = "教师姓名")
    private String teacherName;

    /**
     * 行政职务
     */
    @Excel(name = "行政职务")
    private String administrativePosition;

    /**
     * 社会职称
     */
    @Excel(name = "社会职称")
    private String socialTitle;

    /**
     * 校内职称
     */
    @Excel(name = "校内职称")
    private String onCampusTitle;

    /**
     * 作者类型
     */
    @Excel(name = "作者类型")
    private String authorType;

    /**
     * 论文名称
     */
    @Excel(name = "论文名称")
    private String paperTitle;

    /**
     * 论文类别
     */
    @Excel(name = "论文类别")
    private String paperCategory;

    /**
     * 发表期刊
     */
    @Excel(name = "发表期刊")
    private String publishedJournal;

    /**
     * 收录情况
     */
    @Excel(name = "收录情况")
    private String inclusionStatus;

    /**
     * 发表时间
     */
    @Excel(name = "发表时间")
    private String publicationTime;

    @Excel(name = "论文文件")
    private String text_paper;
    /**
     * 是否与行业联合发表
     */
    @Excel(name = "是否与行业联合发表")
    private String isIndustryCollaborative;

    /**
     * 是否与地方联合发表
     */
    @Excel(name = "是否与地方联合发表")
    private String isLocalCollaborative;

    /**
     * 是否与国际联合发表
     */
    @Excel(name = "是否与国际联合发表")
    private String isInternationalCollaborative;

    /**
     * 是否是跨学科论文
     */
    @Excel(name = "是否是跨学科论文")
    private String isCrossDiscipline;

    /**
     * 排名
     */
    @Excel(name = "排名")
    private String paperRanking;

    /**
     * 科研分
     */
    @Excel(name = "科研分")
    private String researchScore;



    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remarks;
//    private String dataScope;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRolekey() {
        return rolekey;
    }

    public void setRolekey(String rolekey) {
        this.rolekey = rolekey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCollege() {
        return college;
    }

    public void setResearchRoom(String researchRoom) {
        this.researchRoom = researchRoom;
    }

    public String getResearchRoom() {
        return researchRoom;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setAdministrativePosition(String administrativePosition) {
        this.administrativePosition = administrativePosition;
    }

    public String getAdministrativePosition() {
        return administrativePosition;
    }

    public void setSocialTitle(String socialTitle) {
        this.socialTitle = socialTitle;
    }

    public String getSocialTitle() {
        return socialTitle;
    }

    public void setOnCampusTitle(String onCampusTitle) {
        this.onCampusTitle = onCampusTitle;
    }

    public String getOnCampusTitle() {
        return onCampusTitle;
    }

    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    public String getAuthorType() {
        return authorType;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperCategory(String paperCategory) {
        this.paperCategory = paperCategory;
    }

    public String getPaperCategory() {
        return paperCategory;
    }

    public void setPublishedJournal(String publishedJournal) {
        this.publishedJournal = publishedJournal;
    }

    public String getPublishedJournal() {
        return publishedJournal;
    }

    public void setInclusionStatus(String inclusionStatus) {
        this.inclusionStatus = inclusionStatus;
    }

    public String getInclusionStatus() {
        return inclusionStatus;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setIsIndustryCollaborative(String isIndustryCollaborative) {
        this.isIndustryCollaborative = isIndustryCollaborative;
    }

    public String getIsIndustryCollaborative() {
        return isIndustryCollaborative;
    }

    public void setIsLocalCollaborative(String isLocalCollaborative) {
        this.isLocalCollaborative = isLocalCollaborative;
    }

    public String getIsLocalCollaborative() {
        return isLocalCollaborative;
    }

    public void setIsInternationalCollaborative(String isInternationalCollaborative) {
        this.isInternationalCollaborative = isInternationalCollaborative;
    }

    public String getIsInternationalCollaborative() {
        return isInternationalCollaborative;
    }

    public void setIsCrossDiscipline(String isCrossDiscipline) {
        this.isCrossDiscipline = isCrossDiscipline;
    }

    public String getIsCrossDiscipline() {
        return isCrossDiscipline;
    }

    public void setPaperRanking(String paperRanking) {
        this.paperRanking = paperRanking;
    }

    public String getPaperRanking() {
        return paperRanking;
    }

    public void setResearchScore(String researchScore) {
        this.researchScore = researchScore;
    }

    public String getResearchScore() {
        return researchScore;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getText_paper() {
        return text_paper;
    }

    public void setText_paper(String text_paper) {
        this.text_paper = text_paper;
    }

    //    public String getDataScope() {
//        return dataScope;
//    }
//
//    public void setDataScope(String dataScope) {
//        this.dataScope = dataScope;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("college", getCollege())
                .append("researchRoom", getResearchRoom())
                .append("employeeId", getEmployeeId())
                .append("teacherName", getTeacherName())
                .append("administrativePosition", getAdministrativePosition())
                .append("socialTitle", getSocialTitle())
                .append("onCampusTitle", getOnCampusTitle())
                .append("authorType", getAuthorType())
                .append("paperTitle", getPaperTitle())
                .append("paperCategory", getPaperCategory())
                .append("publishedJournal", getPublishedJournal())
                .append("inclusionStatus", getInclusionStatus())
                .append("publicationTime", getPublicationTime())
                .append("isIndustryCollaborative", getIsIndustryCollaborative())
                .append("isLocalCollaborative", getIsLocalCollaborative())
                .append("isInternationalCollaborative", getIsInternationalCollaborative())
                .append("isCrossDiscipline", getIsCrossDiscipline())
                .append("paperRanking", getPaperRanking())
                .append("researchScore", getResearchScore())
                .append("remarks", getRemarks())
                .toString();
    }
}
