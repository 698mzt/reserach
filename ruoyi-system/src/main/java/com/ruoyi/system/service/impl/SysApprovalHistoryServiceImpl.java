package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.mapper.SysApprovalHistoryMapper;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysApprovalHistoryServiceImpl implements ISysApprovalHistoryService {
    @Autowired
    private SysApprovalHistoryMapper sysApprovalHistoryMapper;

    @Override
    public SysApprovalHistory selectSysApprovalHistoryById(Long id) {
        if (id == null) {
            return null;
        }
        return sysApprovalHistoryMapper.selectSysApprovalHistoryById(id);
    }

    @Override
    public List<SysApprovalHistory> selectSysApprovalHistoryList(SysApprovalHistory sysApprovalHistory) {
        if (sysApprovalHistory == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalHistory> list = sysApprovalHistoryMapper.selectSysApprovalHistoryList(sysApprovalHistory);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public List<SysApprovalHistory> selectSysApprovalHistoryByBusinessId(String processCode, Long businessId) {
        if (processCode == null || processCode.trim().isEmpty() || businessId == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalHistory> list = sysApprovalHistoryMapper.selectSysApprovalHistoryByBusinessId(processCode,
                businessId);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public SysApprovalHistory selectLastRejectByBusinessId(String processCode, Long businessId) {
        if (processCode == null || processCode.trim().isEmpty() || businessId == null) {
            return null;
        }
        return sysApprovalHistoryMapper.selectLastRejectByBusinessId(processCode, businessId);
    }

    @Override
    public int insertSysApprovalHistory(SysApprovalHistory sysApprovalHistory) {
        if (sysApprovalHistory == null) {
            return 0;
        }
        return sysApprovalHistoryMapper.insertSysApprovalHistory(sysApprovalHistory);
    }

    @Override
    public int deleteSysApprovalHistoryByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return sysApprovalHistoryMapper.deleteSysApprovalHistoryByIds(ids);
    }

    @Override
    public int deleteSysApprovalHistoryById(Long id) {
        if (id == null) {
            return 0;
        }
        return sysApprovalHistoryMapper.deleteSysApprovalHistoryById(id);
    }
}
