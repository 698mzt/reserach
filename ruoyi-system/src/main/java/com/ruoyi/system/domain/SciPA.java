package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;

public class SciPA {
    @Excel(name = "学院名称")
    private String collegeName;

    @Excel(name = "专业名称")
    private String majorName;

    @Excel(name = "纵向课题科研项目-校级以上")
    private String researchProjectAboveSchoolLevel;

    @Excel(name = "纵向课题科研项目-校级")
    private String researchProjectSchoolLevel;

    @Excel(name = "教材软著-积分")
    private String textbookCopyrightPoints;

    @Excel(name = "论文-积分")
    private String paperPoints;

    @Excel(name = "软著-积分")
    private String softwareCopyrightPoints;

    @Excel(name = "讲座报告-积分")
    private String lectureReportPoints;

    @Excel(name = "专利-积分")
    private String patentPoints;

    @Excel(name = "成果转化-积分")
    private String achievementConversionPoints;
    @Excel(name = "横向课题科研项目")
    private String avc;
    @Excel(name = "奖励-积分")
    private String ass;

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getResearchProjectAboveSchoolLevel() {
        return researchProjectAboveSchoolLevel;
    }

    public void setResearchProjectAboveSchoolLevel(String researchProjectAboveSchoolLevel) {
        this.researchProjectAboveSchoolLevel = researchProjectAboveSchoolLevel;
    }

    public String getResearchProjectSchoolLevel() {
        return researchProjectSchoolLevel;
    }

    public void setResearchProjectSchoolLevel(String researchProjectSchoolLevel) {
        this.researchProjectSchoolLevel = researchProjectSchoolLevel;
    }

    public String getTextbookCopyrightPoints() {
        return textbookCopyrightPoints;
    }

    public void setTextbookCopyrightPoints(String textbookCopyrightPoints) {
        this.textbookCopyrightPoints = textbookCopyrightPoints;
    }

    public String getPaperPoints() {
        return paperPoints;
    }

    public void setPaperPoints(String paperPoints) {
        this.paperPoints = paperPoints;
    }

    public String getSoftwareCopyrightPoints() {
        return softwareCopyrightPoints;
    }

    public void setSoftwareCopyrightPoints(String softwareCopyrightPoints) {
        this.softwareCopyrightPoints = softwareCopyrightPoints;
    }

    public String getLectureReportPoints() {
        return lectureReportPoints;
    }

    public void setLectureReportPoints(String lectureReportPoints) {
        this.lectureReportPoints = lectureReportPoints;
    }

    public String getPatentPoints() {
        return patentPoints;
    }

    public void setPatentPoints(String patentPoints) {
        this.patentPoints = patentPoints;
    }

    public String getAchievementConversionPoints() {
        return achievementConversionPoints;
    }

    public void setAchievementConversionPoints(String achievementConversionPoints) {
        this.achievementConversionPoints = achievementConversionPoints;
    }

    public String getAvc() {
        return avc;
    }

    public void setAvc(String avc) {
        this.avc = avc;
    }

    public String getAss() {
        return ass;
    }

    public void setAss(String as) {
        this.ass = as;
    }
}