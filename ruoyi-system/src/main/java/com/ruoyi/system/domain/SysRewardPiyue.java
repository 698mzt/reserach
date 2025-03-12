package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 横向课题的审核意见; InnoDB free: 11264 kB对象 sci_horizontal_piyue
 * 
 * @author 张聪
 * @date 2024-08-21
 */
public class SysRewardPiyue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 课题id */
    @Excel(name = "奖励id")
    private Integer rewardId;

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

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public Long getUid()
    {
        return uid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())

            .append("concate", getConcate())
            .append("uid", getUid())
            .append("createTime", getCreateTime())
            .toString();
    }
}
