package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.act.ActProcessInstance;
import com.ruoyi.activity.service.IActProcessInstanceService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;

/**
 * 流程实例服务实现
 */
@Service
public class ActProcessInstanceServiceImpl implements IActProcessInstanceService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public String startProcess(String processDefinitionKey, String businessKey,
            Map<String, Object> variables, String startUserId) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                processDefinitionKey,
                businessKey,
                variables);
        return processInstance.getId();
    }

    @Override
    public List<ActProcessInstance> selectProcessInstanceList(ActProcessInstance actProcessInstance) {
        if (actProcessInstance == null) {
            actProcessInstance = new ActProcessInstance();
        }

        org.activiti.engine.runtime.ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();

        if (actProcessInstance.getProcessDefinitionKey() != null
                && !actProcessInstance.getProcessDefinitionKey().trim().isEmpty()) {
            query.processDefinitionKey(actProcessInstance.getProcessDefinitionKey());
        }

        if (actProcessInstance.getBusinessKey() != null
                && !actProcessInstance.getBusinessKey().trim().isEmpty()) {
            query.processInstanceBusinessKey(actProcessInstance.getBusinessKey());
        }

        List<ProcessInstance> processInstances = query.list();

        List<ActProcessInstance> result = new ArrayList<>();
        if (processInstances != null) {
            for (ProcessInstance pi : processInstances) {
                ActProcessInstance dto = new ActProcessInstance();
                dto.setId(pi.getId());
                dto.setProcessDefinitionId(pi.getProcessDefinitionId());
                dto.setProcessDefinitionKey(pi.getProcessDefinitionKey());
                dto.setBusinessKey(pi.getBusinessKey());

                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(pi.getId())
                        .singleResult();
                if (historicProcessInstance != null) {
                    dto.setStartUserId(historicProcessInstance.getStartUserId());
                    dto.setStartTime(historicProcessInstance.getStartTime());
                }

                dto.setSuspended(pi.isSuspended() ? "1" : "0");
                result.add(dto);
            }
        }

        // 按开始时间降序排序
        result.sort((a, b) -> {
            Date aTime = a.getStartTime();
            Date bTime = b.getStartTime();
            if (aTime == null && bTime == null)
                return 0;
            if (aTime == null)
                return 1;
            if (bTime == null)
                return -1;
            return bTime.compareTo(aTime);
        });

        return result;
    }

    @Override
    public ActProcessInstance selectProcessInstanceById(String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (pi == null) {
            return null;
        }

        ActProcessInstance dto = new ActProcessInstance();
        dto.setId(pi.getId());
        dto.setProcessDefinitionId(pi.getProcessDefinitionId());
        dto.setProcessDefinitionKey(pi.getProcessDefinitionKey());
        dto.setBusinessKey(pi.getBusinessKey());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicProcessInstance != null) {
            dto.setStartUserId(historicProcessInstance.getStartUserId());
            dto.setStartTime(historicProcessInstance.getStartTime());
        }

        dto.setSuspended(pi.isSuspended() ? "1" : "0");
        return dto;
    }

    @Override
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    public List<Map<String, Object>> getProcessHistory(String processInstanceId) {
        List<Map<String, Object>> historyList = new ArrayList<>();

        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        for (HistoricActivityInstance activity : activities) {
            Map<String, Object> history = new HashMap<>();
            history.put("activityId", activity.getActivityId());
            history.put("activityName", activity.getActivityName());
            history.put("activityType", activity.getActivityType());
            history.put("startTime", activity.getStartTime());
            history.put("endTime", activity.getEndTime());
            history.put("assignee", activity.getAssignee());
            historyList.add(history);
        }

        return historyList;
    }

    @Override
    public InputStream getProcessDiagram(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (historicProcessInstance == null) {
                throw new RuntimeException("未找到流程实例");
            }
        }

        String processDefinitionId = processInstance != null ? processInstance.getProcessDefinitionId()
                : historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult()
                        .getProcessDefinitionId();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        List<String> activeActivityIds = new ArrayList<>();
        if (processInstance != null) {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            for (Task task : tasks) {
                activeActivityIds.add(task.getTaskDefinitionKey());
            }
        }

        try {
            ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration()
                    .getProcessDiagramGenerator();

            if (diagramGenerator == null) {
                throw new RuntimeException("流程图生成器未配置");
            }

            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,
                    Collections.<String>emptyList(),
                    "宋体",
                    "宋体",
                    "宋体",
                    null,
                    1.0);
        } catch (Exception e) {
            throw new RuntimeException("生成流程图失败：" + e.getMessage(), e);
        }
    }
}