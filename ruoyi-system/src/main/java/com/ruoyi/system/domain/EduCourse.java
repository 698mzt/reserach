package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 科目管理对象 edu_course
 * 
 * @author 张聪
 * @date 2024-08-14
 */
public class EduCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 名称 */
    @Excel(name = "名称")
    private String cname;

    /** 学分 */
    @Excel(name = "学分")
    private String score;

    /** 性质 */
    @Excel(name = "性质")
    private String xingzhi;

    /** 考核形式 */
    @Excel(name = "考核形式")
    private String kaoshi;

    /** 教师是否存在此课程标识 默认不存在 */
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setCname(String cname) 
    {
        this.cname = cname;
    }

    public String getCname() 
    {
        return cname;
    }
    public void setScore(String score) 
    {
        this.score = score;
    }

    public String getScore() 
    {
        return score;
    }
    public void setXingzhi(String xingzhi) 
    {
        this.xingzhi = xingzhi;
    }

    public String getXingzhi() 
    {
        return xingzhi;
    }
    public void setKaoshi(String kaoshi) 
    {
        this.kaoshi = kaoshi;
    }

    public String getKaoshi() 
    {
        return kaoshi;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cname", getCname())
            .append("score", getScore())
            .append("xingzhi", getXingzhi())
            .append("kaoshi", getKaoshi())
            .toString();
    }
}
