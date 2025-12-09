package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 讲座报告对象 sci_lecture_report
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public class SciLectureReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * ====================================================================== 后加属性
     * 申请人id，用于关联查询姓名，学院，教研室 */
    private Integer userId;
    /** 年份 -- 查询*/
    private String year;
    /** 状态 */
    private String state;
    /** 当前登录用户id */
    private Long uid;
    /** 讲座报告批阅标识 */
    private String urlFlag;
    //    用户角色字段
    private String role;
    private String tab;

    /** 状态集合 */
    private List<Integer> statelist;
    // ====================================================================== 后加属性
    /** 序号 */
    private Integer id;

    /** 学院 */
    @Excel(name = "学院")
    private String xueyuan;

    /** 科研室 --- 专业*/
    @Excel(name = "教研室")
    private String keyanshi;

    /** 工号 */
    @Excel(name = "工号")
    private String jobId;

    /** 姓名 */
    @Excel(name = "老师名称")
    private String teacherName;

//    /** 行政职务 */
//    private String xingzhengPosition;
//
//    /** 社会职称 */
//    private String socialTitle;
//
//    /** 校内职称 */
//    private String schoolTitle;

    /** 讲座开始时间 */
    @Excel(name = "开始时间")
    private String reportTime;

    /** 讲座结束时间 */
    @Excel(name = "结束时间")
    private String reportEndTime;

    /** 讲座主题 */
    @Excel(name = "讲座主题")
    private String reportTheme;

    /** 主办单位 */
    @Excel(name = "主办单位")
    private String hostUnit;

    /** 讲座地点 */
    @Excel(name = "讲座地点")
    private String reportPlace;

    /** 讲座题目 */
    @Excel(name = "讲座题目")
    private String reportSubject;

    /** 参与人数 */
    @Excel(name = "参与人数")
    private Integer reportNumber;

    /** 报告时长 */
    @Excel(name = "报告时长")
    private Long reportDuration;


    /** 文件路径 */
//    @Excel(name = "文件路径")
    private String reportUrl;

    /** 分类 */
    @Excel(name = "分类")
    private String reportClassify;

    /** 科研分 */
    @Excel(name = "科研分")
    private String reportKeyanfen;

    /** 该条记录的报告对应的积分id */
    private int repIntId;

    /**
     * get---set方法
     * */
    public String getReportEndTime() {
        return reportEndTime;
    }

    public void setReportEndTime(String reportEndTime) {
        this.reportEndTime = reportEndTime;
    }
    public int getRepIntId() {
        return repIntId;
    }

    public void setRepIntId(int repIntId) {
        this.repIntId = repIntId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }
    public List<Integer> getStatelist() {
        return statelist;
    }

    public void setStatelist(List<Integer> statelist) {
        this.statelist = statelist;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setXueyuan(String xueyuan) 
    {
        this.xueyuan = xueyuan;
    }

    public String getXueyuan() 
    {
        return xueyuan;
    }
    public void setKeyanshi(String keyanshi) 
    {
        this.keyanshi = keyanshi;
    }

    public String getKeyanshi() 
    {
        return keyanshi;
    }
    public void setJobId(String jobId) 
    {
        this.jobId = jobId;
    }

    public String getJobId() 
    {
        return jobId;
    }
    public void setTeacherName(String teacherName) 
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName() 
    {
        return teacherName;
    }


    public void setReportTime(String reportTime) 
    {
        this.reportTime = reportTime;
    }

    public String getReportTime() 
    {
        return reportTime;
    }
    public void setReportTheme(String reportTheme) 
    {
        this.reportTheme = reportTheme;
    }

    public String getReportTheme() 
    {
        return reportTheme;
    }
    public void setHostUnit(String hostUnit) 
    {
        this.hostUnit = hostUnit;
    }

    public String getHostUnit() 
    {
        return hostUnit;
    }
    public void setReportPlace(String reportPlace) 
    {
        this.reportPlace = reportPlace;
    }

    public String getReportPlace() 
    {
        return reportPlace;
    }
    public void setReportNumber(Integer reportNumber) 
    {
        this.reportNumber = reportNumber;
    }

    public Integer getReportNumber() 
    {
        return reportNumber;
    }
    public void setReportSubject(String reportSubject) 
    {
        this.reportSubject = reportSubject;
    }

    public String getReportSubject() 
    {
        return reportSubject;
    }

    public Long getReportDuration() {
        return reportDuration;
    }

    public void setReportDuration(Long reportDuration) {
        this.reportDuration = reportDuration;
    }

    public void setReportClassify(String reportClassify)
    {
        this.reportClassify = reportClassify;
    }

    public String getReportClassify() 
    {
        return reportClassify;
    }
    public void setReportKeyanfen(String reportKeyanfen) 
    {
        this.reportKeyanfen = reportKeyanfen;
    }

    public String getReportKeyanfen() 
    {
        return reportKeyanfen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("year", getYear())
            .append("xueyuan", getXueyuan())
            .append("state", getState())
            .append("uid", getUid())
            .append("urlFlag", getUrlFlag())
            .append("keyanshi", getKeyanshi())
            .append("jobId", getJobId())
            .append("teacherName", getTeacherName())
            .append("reportTime", getReportTime())
            .append("reportTheme", getReportTheme())
            .append("hostUnit", getHostUnit())
            .append("reportPlace", getReportPlace())
            .append("reportNumber", getReportNumber())
            .append("reportSubject", getReportSubject())
            .append("reportDuration", getReportDuration())
            .append("reportClassify", getReportClassify())
            .append("reportKeyanfen", getReportKeyanfen())
            .toString();
    }
}
