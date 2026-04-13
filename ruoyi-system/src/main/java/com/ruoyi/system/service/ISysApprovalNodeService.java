package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysApprovalNode;

import java.util.List;

public interface ISysApprovalNodeService
{
    public SysApprovalNode selectSysApprovalNodeById(Long id);

    public List<SysApprovalNode> selectSysApprovalNodeList(SysApprovalNode sysApprovalNode);

    public List<SysApprovalNode> selectSysApprovalNodeByProcessId(Long processId);

    public int insertSysApprovalNode(SysApprovalNode sysApprovalNode);

    public int updateSysApprovalNode(SysApprovalNode sysApprovalNode);

    public int deleteSysApprovalNodeByIds(Long[] ids);

    public int deleteSysApprovalNodeById(Long id);
}
