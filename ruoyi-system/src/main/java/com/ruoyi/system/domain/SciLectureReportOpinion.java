package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 讲座报告审核意见表对象 sci_lecture_report_opinion
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public class SciLectureReportOpinion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 课题id */
    @Excel(name = "课题id")
    private Integer baogaoId;

    /** 驳回意见 */
    @Excel(name = "驳回意见")
    private String concate;

    /** 提交人 */
    @Excel(name = "提交人")
    private Long uid;

    /** 提交人姓名 */
    private String uname;
    /** 状态 */
    @Excel(name = "状态")
    private String state;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getBaogaoId() {
        return baogaoId;
    }

    public void setBaogaoId(Integer baogaoId) {
        this.baogaoId = baogaoId;
    }

    public String getConcate() {
        return concate;
    }

    public void setConcate(String concate) {
        this.concate = concate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "SciLectureReportOpinion{" +
                "id=" + id +
                ", baogaoId=" + baogaoId +
                ", concate='" + concate + '\'' +
                ", uid=" + uid +
                ", uname='" + uname + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
    public String getStateDes() {
        if (this.state == null) {
            return "";
        }
        switch (this.state) {

            case "1":
                return "待教研室处理";
            case "2":
                return "待科研处处理";
            case "3":
                return "教研室驳回";
            case "4":
                return "已通过";
            case "5":
                return "科研处驳回";
            case "6":
                return "待学院处理";
            case "7":
                return "学院驳回";
            case "0":
                return "草稿箱";

        }
        return this.state;
    }
}
