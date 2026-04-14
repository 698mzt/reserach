package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SysApprovalState extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "状态编码")
    private String stateCode;

    @Excel(name = "状态名称")
    private String stateName;

    @Excel(name = "所属流程")
    private String processCode;

    @Excel(name = "排序")
    private Integer sort;

    @Excel(name = "状态", readConverterExp = "0=启用,1=停用")
    private String status;
}
