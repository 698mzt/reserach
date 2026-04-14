package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.mapper.SysApprovalNodeMapper;
import com.ruoyi.system.service.ISysApprovalNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysApprovalNodeServiceImpl implements ISysApprovalNodeService
{
    @Autowired
    private SysApprovalNodeMapper sysApprovalNodeMapper;

    @Override
    public SysApprovalNode selectSysApprovalNodeById(Long id)
    {
        if (id == null) {
            return null;
        }
        return sysApprovalNodeMapper.selectSysApprovalNodeById(id);
    }

    @Override
    public List<SysApprovalNode> selectSysApprovalNodeList(SysApprovalNode sysApprovalNode)
    {
        if (sysApprovalNode == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalNode> list = sysApprovalNodeMapper.selectSysApprovalNodeList(sysApprovalNode);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public List<SysApprovalNode> selectSysApprovalNodeByProcessId(Long processId)
    {
        if (processId == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalNode> list = sysApprovalNodeMapper.selectSysApprovalNodeByProcessId(processId);
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public int insertSysApprovalNode(SysApprovalNode sysApprovalNode)
    {
        if (sysApprovalNode == null) {
            return 0;
        }
        return sysApprovalNodeMapper.insertSysApprovalNode(sysApprovalNode);
    }

    @Override
    public int updateSysApprovalNode(SysApprovalNode sysApprovalNode)
    {
        if (sysApprovalNode == null || sysApprovalNode.getId() == null) {
            return 0;
        }
        return sysApprovalNodeMapper.updateSysApprovalNode(sysApprovalNode);
    }

    @Override
    public int deleteSysApprovalNodeByIds(Long[] ids)
    {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return sysApprovalNodeMapper.deleteSysApprovalNodeByIds(ids);
    }

    @Override
    public int deleteSysApprovalNodeById(Long id)
    {
        if (id == null) {
            return 0;
        }
        return sysApprovalNodeMapper.deleteSysApprovalNodeById(id);
    }
}
