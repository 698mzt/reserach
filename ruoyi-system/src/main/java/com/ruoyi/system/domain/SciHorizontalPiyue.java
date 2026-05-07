package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 横向课题的审核意见; InnoDB free: 11264 kB对象 sci_horizontal_piyue
 * 
 * @author 张聪
 * @date 2024-08-21
 */
public class SciHorizontalPiyue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 课题id */
    @Excel(name = "课题id")
    private Integer hxktId;
    private Integer verticalId;

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

    private String stateText;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setHxktId(Integer hxktId) 
    {
        this.hxktId = hxktId;
    }

    public Integer getHxktId() 
    {
        return hxktId;
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

    public Integer getVerticalId() {
        return verticalId;
    }

    public void setVerticalId(Integer verticalId) {
        this.verticalId = verticalId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hxktId", getHxktId())
            .append("concate", getConcate())
            .append("uid", getUid())
            .append("createTime", getCreateTime())
            .toString();
    }
}
