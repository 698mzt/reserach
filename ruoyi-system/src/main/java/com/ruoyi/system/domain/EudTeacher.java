package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教师管理对象 eud_teacher
 * 
 * @author 张聪
 * @date 2024-08-12
 */
public class EudTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 教师 */
    @Excel(name = "教师")
    private String tname;

    /** 工号 */
    @Excel(name = "工号")
    private String num;

    /** 职称 */

    private String zhicheng;

    @Excel(name = "职称")
    private String zcName;

    @Excel(name = "科目")
    private String teacourses;

    public String getTeacourses() {
        return teacourses;
    }

    public void setTeacourses(String teacourses) {
        this.teacourses = teacourses;
    }

    private Integer[] courses;

    public Integer[] getCourses() {
        return courses;
    }

    public void setCourses(Integer[] courses) {
        this.courses = courses;
    }

    public String getZcName() {
        return zcName;
    }

    public void setZcName(String zcName) {
        this.zcName = zcName;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setTname(String tname) 
    {
        this.tname = tname;
    }

    public String getTname() 
    {
        return tname;
    }
    public void setNum(String num) 
    {
        this.num = num;
    }

    public String getNum() 
    {
        return num;
    }
    public void setZhicheng(String zhicheng) 
    {
        this.zhicheng = zhicheng;
    }

    public String getZhicheng() 
    {
        return zhicheng;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tname", getTname())
            .append("num", getNum())
            .append("zhicheng", getZhicheng())
            .toString();
    }
}
