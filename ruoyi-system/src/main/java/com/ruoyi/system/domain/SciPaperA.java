package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 论文对象 sci_paper_a
 *
 * @author ruoyi
 * @date 2024-11-07
 */
@Data
public class SciPaperA extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static final List<String> TWO_AUTHORS = List.of("11", "12", "10");
    public  final List<String> ONE_AUTHORS = List.of("8", "9");

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
     * 论文类型
     */
    @Excel(name = "论文类型")
    private String paperType;

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

    @Excel(name = "论文收录通知")
    private String text_paper;
    //论文pdf文件
    @Excel(name = "论文原文")
    private String word_paper;
    //论文word文件

    @Excel(name = "搜索网址")
    private String  search_web;
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
    /** 一作 */
    private String firstPersonId;

    /** 二作 */
    private String secondPersonId;

    /**  三作 */
    private String thirdPersonId;

    /** 四作 */
    private String fourthPersonId;

    /**通讯作者 */
    private String communicationAuthorId;
    
    /** 一作姓名 */
    private String firstAuthorName;
    
    /** 二作姓名 */
    private String secondAuthorName;
    
    /** 三作姓名 */
    private String thirdAuthorName;
    
    /** 四作姓名 */
    private String fourthAuthorName;
    
    /** 通讯作者姓名 */
    private String correspondingAuthorName;

    /** 一作分数 */
    private String firstAuthorScore;

    /** 二作分数 */
    private String secondAuthorScore;

    /** 三作分数 */
    private String thirdAuthorScore;

    /** 四作分数 */
    private String fourthAuthorScore;

    /** 通讯作者分数 */
    private String correspondingAuthorScore;



    public List<String> getONE_AUTHORS() {
        return ONE_AUTHORS;
    }

    public String getFirstAuthorName() {
        return firstAuthorName;
    }

    public void setFirstAuthorName(String firstAuthorName) {
        this.firstAuthorName = firstAuthorName;
    }

    public String getSecondAuthorName() {
        return secondAuthorName;
    }

    public void setSecondAuthorName(String secondAuthorName) {
        this.secondAuthorName = secondAuthorName;
    }

    public String getThirdAuthorName() {
        return thirdAuthorName;
    }

    public void setThirdAuthorName(String thirdAuthorName) {
        this.thirdAuthorName = thirdAuthorName;
    }

    public String getFourthAuthorName() {
        return fourthAuthorName;
    }

    public void setFourthAuthorName(String fourthAuthorName) {
        this.fourthAuthorName = fourthAuthorName;
    }

    public String getCorrespondingAuthorName() {
        return correspondingAuthorName;
    }

    public void setCorrespondingAuthorName(String correspondingAuthorName) {
        this.correspondingAuthorName = correspondingAuthorName;
    }

    public String getFirstAuthorScore() {
        return firstAuthorScore;
    }

    public void setFirstAuthorScore(String firstAuthorScore) {
        this.firstAuthorScore = firstAuthorScore;
    }

    public String getSecondAuthorScore() {
        return secondAuthorScore;
    }

    public void setSecondAuthorScore(String secondAuthorScore) {
        this.secondAuthorScore = secondAuthorScore;
    }

    public String getThirdAuthorScore() {
        return thirdAuthorScore;
    }

    public void setThirdAuthorScore(String thirdAuthorScore) {
        this.thirdAuthorScore = thirdAuthorScore;
    }

    public String getFourthAuthorScore() {
        return fourthAuthorScore;
    }

    public void setFourthAuthorScore(String fourthAuthorScore) {
        this.fourthAuthorScore = fourthAuthorScore;
    }

    public String getCorrespondingAuthorScore() {
        return correspondingAuthorScore;
    }

    public void setCorrespondingAuthorScore(String correspondingAuthorScore) {
        this.correspondingAuthorScore = correspondingAuthorScore;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getCommunicationAuthorId() {
        return communicationAuthorId;
    }

    public void setCommunicationAuthorId(String communicationAuthorId) {
        this.communicationAuthorId = communicationAuthorId;
    }

    public String getFourthPersonId() {
        return fourthPersonId;
    }

    public void setFourthPersonId(String fourthPersonId) {
        this.fourthPersonId = fourthPersonId;
    }

    public String getThirdPersonId() {
        return thirdPersonId;
    }

    public void setThirdPersonId(String thirdPersonId) {
        this.thirdPersonId = thirdPersonId;
    }

    public String getSecondPersonId() {
        return secondPersonId;
    }

    public void setSecondPersonId(String secondPersonId) {
        this.secondPersonId = secondPersonId;
    }

    public String getFirstPersonId() {
        return firstPersonId;
    }

    public void setFirstPersonId(String firstPersonId) {
        this.firstPersonId = firstPersonId;
    }

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

    public String getWord_paper() {
        return word_paper;
    }

    public void setWord_paper(String word_paper) {
        this.word_paper = word_paper;
    }

    public String getSearch_web() {
        return search_web;
    }

    public void setSearch_web(String search_web) {
        this.search_web = search_web;
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
        return "SciPaperA{" +
                "id=" + id +
                ", urlFlag='" + urlFlag + '\'' +
                ", userId=" + userId +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", rolekey='" + rolekey + '\'' +
                ", uid=" + uid +
                ", state='" + state + '\'' +
                ", year='" + year + '\'' +
                ", college='" + college + '\'' +
                ", researchRoom='" + researchRoom + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", administrativePosition='" + administrativePosition + '\'' +
                ", socialTitle='" + socialTitle + '\'' +
                ", onCampusTitle='" + onCampusTitle + '\'' +
                ", authorType='" + authorType + '\'' +
                ", paperTitle='" + paperTitle + '\'' +
                ", paperCategory='" + paperCategory + '\'' +
                ", publishedJournal='" + publishedJournal + '\'' +
                ", inclusionStatus='" + inclusionStatus + '\'' +
                ", publicationTime='" + publicationTime + '\'' +
                ", text_paper='" + text_paper + '\'' +
                ", word_paper='" + word_paper + '\'' +
                ", isIndustryCollaborative='" + isIndustryCollaborative + '\'' +
                ", isLocalCollaborative='" + isLocalCollaborative + '\'' +
                ", isInternationalCollaborative='" + isInternationalCollaborative + '\'' +
                ", isCrossDiscipline='" + isCrossDiscipline + '\'' +
                ", paperRanking='" + paperRanking + '\'' +
                ", researchScore='" + researchScore + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public String getAuthorIds() {
        return this.getFirstPersonId() + "," + this.getSecondPersonId() + "," + this.getThirdPersonId() + "," + this.getFourthPersonId()+ ","+ this.getCommunicationAuthorId();
    }


}
