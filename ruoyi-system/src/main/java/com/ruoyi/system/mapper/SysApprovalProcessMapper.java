package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysApprovalProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysApprovalProcessMapper {
    public SysApprovalProcess selectSysApprovalProcessById(Long id);

    public List<SysApprovalProcess> selectSysApprovalProcessList(SysApprovalProcess sysApprovalProcess);

    public int insertSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int updateSysApprovalProcess(SysApprovalProcess sysApprovalProcess);

    public int deleteSysApprovalProcessById(Long id);

    public int deleteSysApprovalProcessByIds(Long[] ids);

    public SysApprovalProcess selectSysApprovalProcessByProcessCode(String processCode);

    /**
     * 通用业务表状态更新
     * <p>
     * 根据 sys_approval_process 中配置的 businessTable 和 statusField，
     * 动态更新任意业务表的状态字段，无需为每个业务表编写独立的 Mapper 方法。
     * </p>
     *
     * @param tableName   业务表名，如 "sci_horizontal_apply"
     * @param statusField 状态字段名，如 "state"
     * @param newState    新状态值
     * @param businessId  业务数据ID
     * @return 影响行数
     */
    int updateBusinessState(@Param("tableName") String tableName,
            @Param("statusField") String statusField,
            @Param("newState") String newState,
            @Param("businessId") Long businessId);
}
