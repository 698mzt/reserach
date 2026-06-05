package com.ruoyi.system.domain;

/**
 * 奖励成员关联实体类
 * 
 * 用于存储奖励与成员的关联关系，支持成员账号登录后查看自己参与的奖励项目
 * 参考横向课题的 SciHorizontalPersion 设计模式
 * 
 * @author wh
 * @date 2024-12-23
 */
public class SciRewardPersion {

    /** 主键ID */
    private Integer id;

    /** 奖励ID（关联sys_reward表） */
    private Integer rewardId;

    /** 成员用户ID（关联sys_user表的user_id） */
    private String persionId;

    /** 排名（1-主持人，2-成员1，3-成员2，...） */
    private String ranking;

    /** 预计积分 */
    private String expectedScore;

    /** 实际积分 */
    private String actualScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public String getPersionId() {
        return persionId;
    }

    public void setPersionId(String persionId) {
        this.persionId = persionId;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getExpectedScore() {
        return expectedScore;
    }

    public void setExpectedScore(String expectedScore) {
        this.expectedScore = expectedScore;
    }

    public String getActualScore() {
        return actualScore;
    }

    public void setActualScore(String actualScore) {
        this.actualScore = actualScore;
    }
}