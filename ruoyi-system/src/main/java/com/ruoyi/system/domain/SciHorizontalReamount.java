package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SciHorizontalReamount extends BaseEntity {
    private Integer reid;
    private String applyId;
    private String verticalId;
    private String reAmount;
    private String accountData;
    private String reamountUrl;
    private String amountType;
    private String state;

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getReAmount() {
        return reAmount;
    }

    public void setReAmount(String reAmount) {
        this.reAmount = reAmount;
    }

    public String getAccountData() {
        return accountData;
    }

    public void setAccountData(String accountData) {
        this.accountData = accountData;
    }

    public String getReamountUrl() {
        return reamountUrl;
    }

    public void setReamountUrl(String reamountUrl) {
        this.reamountUrl = reamountUrl;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getVerticalId() {
        return verticalId;
    }

    public void setVerticalId(String verticalId) {
        this.verticalId = verticalId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
