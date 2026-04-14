package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.act.ActProcessInstance;
import com.ruoyi.activity.service.IActProcessInstanceService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程实例控制器
 */
@Controller
@RequestMapping("/activity/processInstance")
public class ActProcessInstanceController extends BaseController {

    private String prefix = "activity/processInstance";

    @Autowired
    private IActProcessInstanceService processInstanceService;

    @RequiresPermissions("activity:processInstance:view")
    @GetMapping()
    public String processInstance() {
        return prefix + "/processInstance";
    }

    /**
     * 启动流程实例
     */
    @RequiresPermissions("activity:processInstance:start")
    @Log(title = "流程实例", businessType = BusinessType.INSERT)
    @PostMapping("/start")
    @ResponseBody
    public AjaxResult start(@RequestParam String processDefinitionKey,
            @RequestParam String businessKey,
            @RequestParam String startUserId,
            @RequestBody(required = false) Map<String, Object> variables) {
        String processInstanceId = processInstanceService.startProcess(
                processDefinitionKey,
                businessKey,
                variables,
                startUserId);
        return AjaxResult.success("流程启动成功", processInstanceId);
    }

    /**
     * 查询流程实例列表
     */
    @RequiresPermissions("activity:processInstance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActProcessInstance actProcessInstance) {
        try {
            List<ActProcessInstance> list = processInstanceService.selectProcessInstanceList(
                    actProcessInstance != null ? actProcessInstance : new ActProcessInstance());
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询流程实例列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 查询流程实例详情
     */
    @RequiresPermissions("activity:processInstance:query")
    @GetMapping("/detail/{processInstanceId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable String processInstanceId) {
        ActProcessInstance processInstance = processInstanceService.selectProcessInstanceById(processInstanceId);
        return AjaxResult.success(processInstance);
    }

    /**
     * 挂起流程实例
     */
    @RequiresPermissions("activity:processInstance:suspend")
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    @PutMapping("/suspend/{processInstanceId}")
    @ResponseBody
    public AjaxResult suspend(@PathVariable String processInstanceId) {
        processInstanceService.suspendProcessInstance(processInstanceId);
        return AjaxResult.success();
    }

    /**
     * 激活流程实例
     */
    @RequiresPermissions("activity:processInstance:activate")
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    @PutMapping("/activate/{processInstanceId}")
    @ResponseBody
    public AjaxResult activate(@PathVariable String processInstanceId) {
        processInstanceService.activateProcessInstance(processInstanceId);
        return AjaxResult.success();
    }

    /**
     * 删除流程实例
     */
    @RequiresPermissions("activity:processInstance:remove")
    @Log(title = "流程实例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{processInstanceId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable String processInstanceId, @RequestParam String deleteReason) {
        processInstanceService.deleteProcessInstance(processInstanceId, deleteReason);
        return AjaxResult.success();
    }

    /**
     * 查询流程历史
     */
    @RequiresPermissions("activity:processInstance:query")
    @GetMapping("/history/{processInstanceId}")
    @ResponseBody
    public AjaxResult history(@PathVariable String processInstanceId) {
        List<Map<String, Object>> history = processInstanceService.getProcessHistory(processInstanceId);
        return AjaxResult.success(history);
    }

    /**
     * 获取流程图（带高亮）
     */
    @RequiresPermissions("activity:processInstance:query")
    @GetMapping("/diagram/{processInstanceId}")
    public void getDiagram(@PathVariable String processInstanceId, HttpServletResponse response) throws IOException {
        InputStream inputStream = processInstanceService.getProcessDiagram(processInstanceId);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
}