package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
import com.ruoyi.system.service.ISysApprovalProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysApprovalProcessServiceImpl implements ISysApprovalProcessService
{
    @Autowired
    private SysApprovalProcessMapper sysApprovalProcessMapper;

    @Override
    public SysApprovalProcess selectSysApprovalProcessById(Long id)
    {
        if (id == null) {
            return null;
        }
        return sysApprovalProcessMapper.selectSysApprovalProcessById(id);
    }

    @Override
    public List<SysApprovalProcess> selectSysApprovalProcessList(SysApprovalProcess sysApprovalProcess)
    {
        if (sysApprovalProcess == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalProcess> list = sysApprovalProcessMapper.selectSysApprovalProcessList(sysApprovalProcess);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public int insertSysApprovalProcess(SysApprovalProcess sysApprovalProcess)
    {
        if (sysApprovalProcess == null) {
            return 0;
        }
        return sysApprovalProcessMapper.insertSysApprovalProcess(sysApprovalProcess);
    }

    @Override
    public int updateSysApprovalProcess(SysApprovalProcess sysApprovalProcess)
    {
        if (sysApprovalProcess == null || sysApprovalProcess.getId() == null) {
            return 0;
        }
        return sysApprovalProcessMapper.updateSysApprovalProcess(sysApprovalProcess);
    }

    @Override
    public int deleteSysApprovalProcessByIds(Long[] ids)
    {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return sysApprovalProcessMapper.deleteSysApprovalProcessByIds(ids);
    }

    @Override
    public int deleteSysApprovalProcessById(Long id)
    {
        if (id == null) {
            return 0;
        }
        return sysApprovalProcessMapper.deleteSysApprovalProcessById(id);
    }
}
