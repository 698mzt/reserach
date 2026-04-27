package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SysApprovalNode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "流程ID")
    private Long processId;

    private String processName;

    @Excel(name = "节点顺序")
    private Integer nodeOrder;

    @Excel(name = "节点编码")
    private String nodeCode;

    @Excel(name = "节点名称")
    private String nodeNm;

    @Excel(name = "关联部门ID")
    private String deptIds;

    @Excel(name = "关联角色ID")
    private String roleIds;

    @Excel(name = "关联角色Key")
    private String roleKeys;

    @Excel(name = "通过后的状态")
    private String passState;

    @Excel(name = "驳回后的状态")
    private String rejectState;

    @Excel(name = "是否可转办", readConverterExp = "0=否,1=是")
    private String canTransfer;

    @Excel(name = "是否可驳回", readConverterExp = "0=否,1=是")
    private String canBack;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "状态", readConverterExp = "0=启用,1=停用")
    private String status;
}
