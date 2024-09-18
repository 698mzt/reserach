package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * InnoDB free: 4096 kB对象 edu_tea_course
 * 
 * @author ruoyi
 * @date 2024-08-14
 */
public class EduTeaCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer tid;

    /**  */
    private Integer cid;

    public void setTid(Integer tid) 
    {
        this.tid = tid;
    }

    public Integer getTid() 
    {
        return tid;
    }
    public void setCid(Integer cid) 
    {
        this.cid = cid;
    }

    public Integer getCid() 
    {
        return cid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tid", getTid())
            .append("cid", getCid())
            .toString();
    }
}
