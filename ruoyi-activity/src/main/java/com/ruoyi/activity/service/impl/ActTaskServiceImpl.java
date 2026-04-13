package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.act.ActTask;
import com.ruoyi.activity.service.IActTaskService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 任务服务实现
 */
@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ActTask> selectTodoTaskList(String userId) {
        List<ActTask> result = new ArrayList<>();

        if (userId == null || userId.trim().isEmpty()) {
            return result;
        }

        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        if (tasks != null) {
            for (Task task : tasks) {
                ActTask dto = new ActTask();
                dto.setId(task.getId());
                dto.setName(task.getName());
                dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
                dto.setProcessInstanceId(task.getProcessInstanceId());
                dto.setProcessDefinitionId(task.getProcessDefinitionId());
                dto.setAssignee(task.getAssignee());
                dto.setCreateTime(task.getCreateTime());
                dto.setDueDate(task.getDueDate());
                dto.setPriority(task.getPriority());

                // 获取流程名称
                if (task.getProcessDefinitionId() != null) {
                    ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                            .processDefinitionId(task.getProcessDefinitionId())
                            .singleResult();
                    if (pd != null) {
                        dto.setProcessName(pd.getName());
                    }
                }

                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<ActTask> selectDoneTaskList(String userId) {
        List<ActTask> result = new ArrayList<>();

        if (userId == null || userId.trim().isEmpty()) {
            return result;
        }

        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        if (historicTasks != null) {
            for (HistoricTaskInstance historicTask : historicTasks) {
                ActTask dto = new ActTask();
                dto.setId(historicTask.getId());
                dto.setName(historicTask.getName());
                dto.setTaskDefinitionKey(historicTask.getTaskDefinitionKey());
                dto.setProcessInstanceId(historicTask.getProcessInstanceId());
                dto.setProcessDefinitionId(historicTask.getProcessDefinitionId());
                dto.setAssignee(historicTask.getAssignee());
                dto.setCreateTime(historicTask.getCreateTime());
                dto.setDueDate(historicTask.getDueDate());
                dto.setPriority(historicTask.getPriority());

                // 获取流程名称
                if (historicTask.getProcessDefinitionId() != null) {
                    ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                            .processDefinitionId(historicTask.getProcessDefinitionId())
                            .singleResult();
                    if (pd != null) {
                        dto.setProcessName(pd.getName());
                    }
                }

                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public void transferTask(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void delegateTask(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }

    @Override
    public void withdrawTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在或已完成");
        }
        taskService.setAssignee(taskId, task.getOwner());
    }

    @Override
    public Map<String, Object> getTaskDetail(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", task.getId());
        result.put("taskName", task.getName());
        result.put("taskDefinitionKey", task.getTaskDefinitionKey());
        result.put("processInstanceId", task.getProcessInstanceId());
        result.put("processDefinitionId", task.getProcessDefinitionId());
        result.put("assignee", task.getAssignee());
        result.put("createTime", task.getCreateTime());
        result.put("dueDate", task.getDueDate());
        result.put("priority", task.getPriority());

        Map<String, Object> variables = taskService.getVariables(taskId);
        result.put("variables", variables);

        return result;
    }

    @Override
    public Map<String, Object> getTaskFormData(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        Map<String, Object> formData = taskService.getVariables(taskId);
        return formData;
    }
}