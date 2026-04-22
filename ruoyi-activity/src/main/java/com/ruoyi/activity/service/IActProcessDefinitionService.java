package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.act.ActProcessDefinition;
import java.util.List;

/**
 * 流程定义服务接口
 */
public interface IActProcessDefinitionService {

    /**
     * 查询流程定义列表
     *
     * @param actProcessDefinition 流程定义
     * @return 流程定义列表
     */
    List<ActProcessDefinition> selectProcessDefinitionList(ActProcessDefinition actProcessDefinition);

    /**
     * 查询流程定义详情
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程定义
     */
    ActProcessDefinition selectProcessDefinitionById(String processDefinitionId);

    /**
     * 查询流程定义（根据Key）
     *
     * @param processDefinitionKey 流程定义Key
     * @return 流程定义
     */
    ActProcessDefinition selectProcessDefinitionByKey(String processDefinitionKey);

    /**
     * 挂起流程定义
     *
     * @param processDefinitionId 流程定义ID
     */
    void suspendProcessDefinition(String processDefinitionId);

    /**
     * 激活流程定义
     *
     * @param processDefinitionId 流程定义ID
     */
    void activateProcessDefinition(String processDefinitionId);

    /**
     * 删除流程定义
     *
     * @param deploymentId 部署ID
     * @param cascade 是否级联删除
     */
    void deleteProcessDefinition(String deploymentId, boolean cascade);

    /**
     * 转换流程定义为XML
     *
     * @param processDefinitionId 流程定义ID
     * @return XML字符串
     */
    String convertToXml(String processDefinitionId);
}