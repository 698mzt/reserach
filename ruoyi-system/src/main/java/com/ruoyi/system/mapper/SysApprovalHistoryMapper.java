package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysApprovalHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysApprovalHistoryMapper {
    public SysApprovalHistory selectSysApprovalHistoryById(Long id);

    public List<SysApprovalHistory> selectSysApprovalHistoryList(SysApprovalHistory sysApprovalHistory);

    public List<SysApprovalHistory> selectSysApprovalHistoryByBusinessId(@Param("processCode") String processCode,
                                                                         @Param("businessId") Long businessId);

    public SysApprovalHistory selectLastRejectByBusinessId(@Param("processCode") String processCode,
                                                           @Param("businessId") Long businessId);

    public SysApprovalHistory selectLastRecallableByBusinessId(@Param("processCode") String processCode,
                                                               @Param("businessId") Long businessId,
                                                               @Param("currentState") String currentState);

    public int insertSysApprovalHistory(SysApprovalHistory sysApprovalHistory);

    public int deleteSysApprovalHistoryById(Long id);

    public int deleteSysApprovalHistoryByIds(Long[] ids);
}
