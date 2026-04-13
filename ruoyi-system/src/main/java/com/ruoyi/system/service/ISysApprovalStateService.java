package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysApprovalState;

import java.util.List;

public interface ISysApprovalStateService
{
    public SysApprovalState selectSysApprovalStateById(Long id);

    public List<SysApprovalState> selectSysApprovalStateList(SysApprovalState sysApprovalState);

    public List<SysApprovalState> selectSysApprovalStateByProcessCode(String processCode);

    public int insertSysApprovalState(SysApprovalState sysApprovalState);

    public int updateSysApprovalState(SysApprovalState sysApprovalState);

    public int deleteSysApprovalStateByIds(Long[] ids);

    public int deleteSysApprovalStateById(Long id);
}
