package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SysApprovalProcess extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "流程编码")
    private String processCode;

    @Excel(name = "流程名称")
    private String processName;

    @Excel(name = "关联业务表名")
    private String businessTable;

    @Excel(name = "状态字段名")
    private String statusField;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "状态", readConverterExp = "0=启用,1=停用")
    private String status;
}
