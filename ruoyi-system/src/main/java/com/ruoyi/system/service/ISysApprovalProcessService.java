package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysApprovalProcess;

import java.util.List;

public interface ISysApprovalProcessService
{
    public SysApprovalProcess selectSysApprovalProcessById(Long id);

    public List<SysApprovalProcess> selectSysApprovalProcessList(SysApprovalProcess sysApprovalProcess);

    public int insertSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int updateSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int deleteSysApprovalProcessByIds(Long[] ids);

    public int deleteSysApprovalProcessById(Long id);
}
