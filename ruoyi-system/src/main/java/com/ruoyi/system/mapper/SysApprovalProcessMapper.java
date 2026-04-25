package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysApprovalProcess;

import java.util.List;

public interface SysApprovalProcessMapper
{
    public SysApprovalProcess selectSysApprovalProcessById(Long id);

    public List<SysApprovalProcess> selectSysApprovalProcessList(SysApprovalProcess sysApprovalProcess);

    public int insertSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int updateSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int deleteSysApprovalProcessById(Long id);

    public int deleteSysApprovalProcessByIds(Long[] ids);

    public SysApprovalProcess selectSysApprovalProcessByProcessCode(String processCode);
}
