package com.ruoyi.system.domain;

/**
 * 教材著作成员管理实体类
 * 
 * @author zwh
 * @date 2026-01-19
 */
public class SciJiaocairuanzhuMember {
    private int id;
    private Integer jiaocaiId;
    private String memberId;
    private String memberName;
    private String ranking;
    private String researchScore;
    private Integer isChiefEditor;
    private Integer isAssociateEditor;
    private Integer isMember;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getJiaocaiId() {
        return jiaocaiId;
    }

    public void setJiaocaiId(Integer jiaocaiId) {
        this.jiaocaiId = jiaocaiId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getResearchScore() {
        return researchScore;
    }

    public void setResearchScore(String researchScore) {
        this.researchScore = researchScore;
    }

    public Integer getIsChiefEditor() {
        return isChiefEditor;
    }

    public void setIsChiefEditor(Integer isChiefEditor) {
        this.isChiefEditor = isChiefEditor;
    }

    public Integer getIsAssociateEditor() {
        return isAssociateEditor;
    }

    public void setIsAssociateEditor(Integer isAssociateEditor) {
        this.isAssociateEditor = isAssociateEditor;
    }

    public Integer getIsMember() {
        return isMember;
    }

    public void setIsMember(Integer isMember) {
        this.isMember = isMember;
    }
}