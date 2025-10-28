package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.time.LocalDateTime;

public class Paper_user_score extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long pusId; //主键
    private Long paperId; // 论文id
    private Long userId; // 用户id
    private String authorLevel; // 作者等级  (暂定为得分等级)
    private String score; // 得分
    private String authorOrder; // 作者排序
    private String isDeleted; // 逻辑删除

    public String getAuthorOrder() {
        return authorOrder;
    }

    public void setAuthorOrder(String authorOrder) {
        this.authorOrder = authorOrder;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getPusId() {
        return pusId;
    }

    public void setPusId(Long pusId) {
        this.pusId = pusId;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorLevel() {
        return authorLevel;
    }

    public void setAuthorLevel(String authorLevel) {
        this.authorLevel = authorLevel;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
