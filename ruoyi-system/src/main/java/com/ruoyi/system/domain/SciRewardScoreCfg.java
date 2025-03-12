package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 奖励积分管理对象 sci_reward_score_cfg
 * 
 * @author ruoyi
 * @date 2025-02-24
 */
public class SciRewardScoreCfg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 分类 */
    @Excel(name = "分类")
    private String fenLei;

    /** 排名 */
    @Excel(name = "排名")
    private String paiMing;

    /** 等级 */
    @Excel(name = "等级")
    private String dengJi;

    /** 总分 */
    @Excel(name = "总分")
    private String totalScore;

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
    public void setDengJi(String dengJi) 
    {
        this.dengJi = dengJi;
    }

    public String getDengJi() 
    {
        return dengJi;
    }
    public void setTotalScore(String totalScore) 
    {
        this.totalScore = totalScore;
    }

    public String getTotalScore() 
    {
        return totalScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fenLei", getFenLei())
            .append("paiMing", getPaiMing())
            .append("dengJi", getDengJi())
            .append("totalScore", getTotalScore())
            .toString();
    }
}
