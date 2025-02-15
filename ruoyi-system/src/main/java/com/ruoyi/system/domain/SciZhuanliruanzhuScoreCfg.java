package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 横向课题得分配置对象 sci_project_score_cfg
 *
 * @author ruoyi
 * @date 2024-09-30
 */
public class SciZhuanliruanzhuScoreCfg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 经费小于 */
    @Excel(name = "经费小于")
    private String fundsMax;

    /** 经费大于 */
    @Excel(name = "经费大于")
    private String fundsMin;

    /** 负责人排名 */
    @Excel(name = "负责人排名")
    private String userOrder;

    /** 总分 */
    @Excel(name = "总分")
    private String totalScore;

    /** 开题得分 */
    @Excel(name = "开题得分")
    private String startScore;

    /** 结题得分 */
    @Excel(name = "结题得分")
    private String endScore;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updateUser;

    /** 课题类型，H-横向，V-纵向 */
    @Excel(name = "课题类型，H-横向，V-纵向")
    private String projectType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFundsMax(String fundsMax)
    {
        this.fundsMax = fundsMax;
    }

    public String getFundsMax()
    {
        return fundsMax;
    }
    public void setFundsMin(String fundsMin)
    {
        this.fundsMin = fundsMin;
    }

    public String getFundsMin()
    {
        return fundsMin;
    }
    public void setUserOrder(String userOrder)
    {
        this.userOrder = userOrder;
    }

    public String getUserOrder()
    {
        return userOrder;
    }
    public void setTotalScore(String totalScore)
    {
        this.totalScore = totalScore;
    }

    public String getTotalScore()
    {
        return totalScore;
    }
    public void setStartScore(String startScore)
    {
        this.startScore = startScore;
    }

    public String getStartScore()
    {
        return startScore;
    }
    public void setEndScore(String endScore)
    {
        this.endScore = endScore;
    }

    public String getEndScore()
    {
        return endScore;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }
    public void setProjectType(String projectType)
    {
        this.projectType = projectType;
    }

    public String getProjectType()
    {
        return projectType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fundsMax", getFundsMax())
                .append("fundsMin", getFundsMin())
                .append("userOrder", getUserOrder())
                .append("totalScore", getTotalScore())
                .append("startScore", getStartScore())
                .append("endScore", getEndScore())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("projectType", getProjectType())
                .toString();
    }
}
