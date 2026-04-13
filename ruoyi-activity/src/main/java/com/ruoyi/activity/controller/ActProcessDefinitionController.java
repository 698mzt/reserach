package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.act.ActProcessDefinition;
import com.ruoyi.activity.service.IActProcessDefinitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义控制器
 */
@Controller
@RequestMapping("/activity/processDefinition")
public class ActProcessDefinitionController extends BaseController {

    private String prefix = "activity/processDefinition";

    @Autowired
    private IActProcessDefinitionService processDefinitionService;

    @RequiresPermissions("activity:processDefinition:view")
    @GetMapping()
    public String process() {
        return prefix + "/processDefinition";
    }

    /**
     * 查询流程定义列表
     */
    @RequiresPermissions("activity:processDefinition:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActProcessDefinition actProcessDefinition) {
        try {
            List<ActProcessDefinition> list = processDefinitionService.selectProcessDefinitionList(
                    actProcessDefinition != null ? actProcessDefinition : new ActProcessDefinition());
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询流程定义列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 查询流程定义详情
     */
    @RequiresPermissions("activity:processDefinition:query")
    @GetMapping("/detail/{processDefinitionId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable String processDefinitionId) {
        ActProcessDefinition processDefinition = processDefinitionService
                .selectProcessDefinitionById(processDefinitionId);
        return AjaxResult.success(processDefinition);
    }

    /**
     * 挂起流程定义
     */
    @RequiresPermissions("activity:processDefinition:suspend")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PutMapping("/suspend/{processDefinitionId}")
    @ResponseBody
    public AjaxResult suspend(@PathVariable String processDefinitionId) {
        processDefinitionService.suspendProcessDefinition(processDefinitionId);
        return AjaxResult.success();
    }

    /**
     * 激活流程定义
     */
    @RequiresPermissions("activity:processDefinition:activate")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PutMapping("/activate/{processDefinitionId}")
    @ResponseBody
    public AjaxResult activate(@PathVariable String processDefinitionId) {
        processDefinitionService.activateProcessDefinition(processDefinitionId);
        return AjaxResult.success();
    }

    /**
     * 删除流程定义
     */
    @RequiresPermissions("activity:processDefinition:remove")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deploymentId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable String deploymentId, @RequestParam(defaultValue = "false") boolean cascade) {
        processDefinitionService.deleteProcessDefinition(deploymentId, cascade);
        return AjaxResult.success();
    }

    /**
     * 转换流程定义为XML
     */
    @RequiresPermissions("activity:processDefinition:query")
    @GetMapping("/xml/{processDefinitionId}")
    public void convertToXml(@PathVariable String processDefinitionId, HttpServletResponse response)
            throws IOException {
        String xml = processDefinitionService.convertToXml(processDefinitionId);
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=process.xml");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(xml.getBytes("UTF-8"));
        outputStream.close();
    }
}