package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysApprovalState;
import com.ruoyi.system.mapper.SysApprovalStateMapper;
import com.ruoyi.system.service.ISysApprovalStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysApprovalStateServiceImpl implements ISysApprovalStateService
{
    @Autowired
    private SysApprovalStateMapper sysApprovalStateMapper;

    @Override
    public SysApprovalState selectSysApprovalStateById(Long id)
    {
        if (id == null) {
            return null;
        }
        return sysApprovalStateMapper.selectSysApprovalStateById(id);
    }

    @Override
    public List<SysApprovalState> selectSysApprovalStateList(SysApprovalState sysApprovalState)
    {
        if (sysApprovalState == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalState> list = sysApprovalStateMapper.selectSysApprovalStateList(sysApprovalState);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public List<SysApprovalState> selectSysApprovalStateByProcessCode(String processCode)
    {
        if (processCode == null || processCode.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalState> list = sysApprovalStateMapper.selectSysApprovalStateByProcessCode(processCode);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public int insertSysApprovalState(SysApprovalState sysApprovalState)
    {
        if (sysApprovalState == null) {
            return 0;
        }
        return sysApprovalStateMapper.insertSysApprovalState(sysApprovalState);
    }

    @Override
    public int updateSysApprovalState(SysApprovalState sysApprovalState)
    {
        if (sysApprovalState == null || sysApprovalState.getId() == null) {
            return 0;
        }
        return sysApprovalStateMapper.updateSysApprovalState(sysApprovalState);
    }

    @Override
    public int deleteSysApprovalStateByIds(Long[] ids)
    {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return sysApprovalStateMapper.deleteSysApprovalStateByIds(ids);
    }

    @Override
    public int deleteSysApprovalStateById(Long id)
    {
        if (id == null) {
            return 0;
        }
        return sysApprovalStateMapper.deleteSysApprovalStateById(id);
    }
}
