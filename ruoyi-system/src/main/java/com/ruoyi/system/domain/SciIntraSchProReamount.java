package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SciIntraSchProReamount extends BaseEntity {
    private Integer id;
    private String applyId;
    private String reAmount;
    private String accountData;
    private String reamountUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
