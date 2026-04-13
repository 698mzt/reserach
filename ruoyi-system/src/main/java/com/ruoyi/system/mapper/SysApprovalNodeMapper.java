package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysApprovalNode;

import java.util.List;

public interface SysApprovalNodeMapper
{
    public SysApprovalNode selectSysApprovalNodeById(Long id);

    public List<SysApprovalNode> selectSysApprovalNodeList(SysApprovalNode sysApprovalNode);

    public List<SysApprovalNode> selectSysApprovalNodeByProcessId(Long processId);

    public int insertSysApprovalNode(SysApprovalNode sysApprovalNode);

    public int updateSysApprovalNode(SysApprovalNode sysApprovalNode);

    public int deleteSysApprovalNodeById(Long id);

    public int deleteSysApprovalNodeByIds(Long[] ids);
}
