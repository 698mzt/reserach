package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysApprovalHistory;

import java.util.List;

public interface ISysApprovalHistoryService {
    public SysApprovalHistory selectSysApprovalHistoryById(Long id);

    public List<SysApprovalHistory> selectSysApprovalHistoryList(SysApprovalHistory sysApprovalHistory);

    public List<SysApprovalHistory> selectSysApprovalHistoryByBusinessId(String processCode, Long businessId);

    public SysApprovalHistory selectLastRejectByBusinessId(String processCode, Long businessId);

    public SysApprovalHistory selectLastRecallableByBusinessId(String processCode, Long businessId, String currentState);

    public int insertSysApprovalHistory(SysApprovalHistory sysApprovalHistory);

    public int deleteSysApprovalHistoryByIds(Long[] ids);

    public int deleteSysApprovalHistoryById(Long id);
}
