package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 专利软著的审核意见; InnoDB free: 11264 kB对象 sci_horizontal_piyue
 *
 * @author 张聪
 * @date 2024-08-21
 */
public class SciJiaocairuanzhuPiyue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 课题id */
    @Excel(name = "课题id")
    private Integer jiaocai_id;

    /** 驳回意见 */
    @Excel(name = "驳回意见")
    private String concate;

    /** 提交人 */
    @Excel(name = "提交人")
    private Long uid;

    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setJiaocai_id(Integer jiaocai_id)
    {
        this.jiaocai_id = jiaocai_id;
    }

    public Integer getJiaocai_id()
    {
        return jiaocai_id;
    }
    public void setConcate(String concate)
    {
        this.concate = concate;
    }

    public String getConcate()
    {
        return concate;
    }
    public void setUid(Long uid)
    {
        this.uid = uid;
    }

    public Long getUid()
    {
        return uid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("jiaocai_id", getJiaocai_id())
                .append("concate", getConcate())
                .append("uid", getUid())
                .append("createTime", getCreateTime())
                .toString();
    }
}
