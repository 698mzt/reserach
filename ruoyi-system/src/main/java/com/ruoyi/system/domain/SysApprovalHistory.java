package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class SysApprovalHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "流程编码")
    private String processCode;

    @Excel(name = "业务数据ID")
    private Long businessId;

    @Excel(name = "审批节点ID")
    private Long nodeId;

    @Excel(name = "审批节点名称")
    private String nodeName;

    @Excel(name = "操作类型")
    private String action;

    @Excel(name = "操作人ID")
    private Long operatorId;

    @Excel(name = "操作人姓名")
    private String operatorName;

    @Excel(name = "操作人部门")
    private String operatorDept;

    @Excel(name = "变更前状态")
    private String oldState;

    @Excel(name = "变更后状态")
    private String newState;

    @Excel(name = "审批意见")
    private String comment;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    // 以下为关联字段，不存储在数据库中
    @Excel(name = "流程名称")
    private String processName;
    
    @Excel(name = "原状态名称")
    private String oldStateName;
    
    @Excel(name = "新状态名称")
    private String newStateName;
    
    @Excel(name = "业务名称")
    private String businessName;
}
