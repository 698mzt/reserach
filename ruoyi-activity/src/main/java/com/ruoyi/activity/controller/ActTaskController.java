package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.act.ActTask;
import com.ruoyi.activity.service.IActTaskService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@Controller
@RequestMapping("/activity/task")
public class ActTaskController extends BaseController {

    private String prefix = "activity/task";

    @Autowired
    private IActTaskService taskService;

    @RequiresPermissions("activity:task:view")
    @GetMapping()
    public String task() {
        return prefix + "/task";
    }

    /**
     * 查询待办任务列表
     */
    @RequiresPermissions("activity:task:list")
    @PostMapping("/todoList")
    @ResponseBody
    public TableDataInfo todoList() {
        try {
            String userId = ShiroUtils.getUserId().toString();
            List<ActTask> list = taskService.selectTodoTaskList(userId);
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询待办任务列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 查询已办任务列表
     */
    @RequiresPermissions("activity:task:list")
    @PostMapping("/doneList")
    @ResponseBody
    public TableDataInfo doneList() {
        try {
            String userId = ShiroUtils.getUserId().toString();
            List<ActTask> list = taskService.selectDoneTaskList(userId);
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询已办任务列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 完成任务
     */
    @RequiresPermissions("activity:task:complete")
    @Log(title = "任务处理", businessType = BusinessType.UPDATE)
    @PostMapping("/complete/{taskId}")
    @ResponseBody
    public AjaxResult complete(@PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        taskService.completeTask(taskId, variables);
        return AjaxResult.success();
    }

    /**
     * 转办任务
     */
    @RequiresPermissions("activity:task:transfer")
    @Log(title = "任务转办", businessType = BusinessType.UPDATE)
    @PostMapping("/transfer/{taskId}")
    @ResponseBody
    public AjaxResult transfer(@PathVariable String taskId, @RequestParam String userId) {
        taskService.transferTask(taskId, userId);
        return AjaxResult.success();
    }

    /**
     * 委派任务
     */
    @RequiresPermissions("activity:task:delegate")
    @Log(title = "任务委派", businessType = BusinessType.UPDATE)
    @PostMapping("/delegate/{taskId}")
    @ResponseBody
    public AjaxResult delegate(@PathVariable String taskId, @RequestParam String userId) {
        taskService.delegateTask(taskId, userId);
        return AjaxResult.success();
    }

    /**
     * 撤回任务
     */
    @RequiresPermissions("activity:task:withdraw")
    @Log(title = "任务撤回", businessType = BusinessType.UPDATE)
    @PostMapping("/withdraw/{taskId}")
    @ResponseBody
    public AjaxResult withdraw(@PathVariable String taskId) {
        taskService.withdrawTask(taskId);
        return AjaxResult.success();
    }

    /**
     * 查询任务详情
     */
    @RequiresPermissions("activity:task:query")
    @GetMapping("/detail/{taskId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable String taskId) {
        Map<String, Object> taskDetail = taskService.getTaskDetail(taskId);
        return AjaxResult.success(taskDetail);
    }

    /**
     * 获取任务表单数据
     */
    @RequiresPermissions("activity:task:query")
    @GetMapping("/formData/{taskId}")
    @ResponseBody
    public AjaxResult formData(@PathVariable String taskId) {
        Map<String, Object> formData = taskService.getTaskFormData(taskId);
        return AjaxResult.success(formData);
    }
}