package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 讲座报告积分管理对象 sci_lecture_report_integral
 * 
 * @author ruoyi
 * @date 2025-02-16
 */
public class SciLectureReportIntegral extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 分类 */
    @Excel(name = "分类")
    private String classification;

    /** 积分 */
    @Excel(name = "积分")
    private String integral;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date overCreatetime;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setClassification(String classification) 
    {
        this.classification = classification;
    }

    public String getClassification() 
    {
        return classification;
    }
    public void setIntegral(String integral) 
    {
        this.integral = integral;
    }

    public String getIntegral() 
    {
        return integral;
    }
    public void setOverCreatetime(Date overCreatetime) 
    {
        this.overCreatetime = overCreatetime;
    }

    public Date getOverCreatetime() 
    {
        return overCreatetime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("classification", getClassification())
            .append("integral", getIntegral())
            .append("overCreatetime", getOverCreatetime())
            .toString();
    }
}
