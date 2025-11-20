package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 教材软著积分管理对象 sci_jiaocairuanzhu_score_cfg
 * 
 * @author ruoyi
 * @date 2025-02-26
 */
public class SciJiaocairuanzhuScoreCfg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 分类 */
    @Excel(name = "分类")
    private String fenLei;

    /** 个人排名 */
    @Excel(name = "个人排名")
    private String paiMing;

    /** 总分 */
    @Excel(name = "总分")
    private String totalScore;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updateUser;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFenLei(String fenLei) 
    {
        this.fenLei = fenLei;
    }

    public String getFenLei() 
    {
        return fenLei;
    }
    public void setPaiMing(String paiMing) 
    {
        this.paiMing = paiMing;
    }

    public String getPaiMing() 
    {
        return paiMing;
    }
    public void setTotalScore(String totalScore) 
    {
        this.totalScore = totalScore;
    }

    public String getTotalScore() 
    {
        return totalScore;
    }
    public void setUpdateUser(String updateUser) 
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser() 
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fenLei", getFenLei())
            .append("paiMing", getPaiMing())
            .append("totalScore", getTotalScore())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
