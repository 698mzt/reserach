package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.act.ActProcessInstance;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程实例服务接口
 */
public interface IActProcessInstanceService {

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @param startUserId 发起人ID
     * @return 流程实例ID
     */
    String startProcess(String processDefinitionKey, String businessKey,
                        Map<String, Object> variables, String startUserId);

    /**
     * 查询流程实例列表
     *
     * @param actProcessInstance 流程实例
     * @return 流程实例列表
     */
    List<ActProcessInstance> selectProcessInstanceList(ActProcessInstance actProcessInstance);

    /**
     * 查询流程实例详情
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    ActProcessInstance selectProcessInstanceById(String processInstanceId);

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void suspendProcessInstance(String processInstanceId);

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstance(String processInstanceId);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 查询流程历史
     *
     * @param processInstanceId 流程实例ID
     * @return 流程历史
     */
    List<Map<String, Object>> getProcessHistory(String processInstanceId);

    /**
     * 获取流程图（带高亮）
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图
     */
    InputStream getProcessDiagram(String processInstanceId);
}