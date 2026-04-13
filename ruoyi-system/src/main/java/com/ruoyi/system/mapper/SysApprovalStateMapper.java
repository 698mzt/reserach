package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysApprovalState;

import java.util.List;

public interface SysApprovalStateMapper
{
    public SysApprovalState selectSysApprovalStateById(Long id);

    public List<SysApprovalState> selectSysApprovalStateList(SysApprovalState sysApprovalState);

    public List<SysApprovalState> selectSysApprovalStateByProcessCode(String processCode);

    public int insertSysApprovalState(SysApprovalState sysApprovalState);

    public int updateSysApprovalState(SysApprovalState sysApprovalState);

    public int deleteSysApprovalStateById(Long id);

    public int deleteSysApprovalStateByIds(Long[] ids);
}
