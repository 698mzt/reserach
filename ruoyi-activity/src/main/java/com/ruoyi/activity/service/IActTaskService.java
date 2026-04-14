package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.act.ActTask;
import java.util.List;
import java.util.Map;

/**
 * 任务服务接口
 */
public interface IActTaskService {

    /**
     * 查询待办任务列表
     *
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<ActTask> selectTodoTaskList(String userId);

    /**
     * 查询已办任务列表
     *
     * @param userId 用户ID
     * @return 已办任务列表
     */
    List<ActTask> selectDoneTaskList(String userId);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 流程变量
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * 转办任务
     *
     * @param taskId 任务ID
     * @param userId 目标用户ID
     */
    void transferTask(String taskId, String userId);

    /**
     * 委派任务
     *
     * @param taskId 任务ID
     * @param userId 目标用户ID
     */
    void delegateTask(String taskId, String userId);

    /**
     * 撤回任务
     *
     * @param taskId 任务ID
     */
    void withdrawTask(String taskId);

    /**
     * 查询任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    Map<String, Object> getTaskDetail(String taskId);

    /**
     * 获取任务表单数据
     *
     * @param taskId 任务ID
     * @return 表单数据
     */
    Map<String, Object> getTaskFormData(String taskId);
}