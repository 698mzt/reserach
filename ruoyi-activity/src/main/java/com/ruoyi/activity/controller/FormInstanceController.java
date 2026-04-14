package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.FormInstance;
import com.ruoyi.activity.service.IFormInstanceService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 表单实例控制器
 */
@Controller
@RequestMapping("/activity/formInstance")
public class FormInstanceController extends BaseController {

    private String prefix = "activity/formInstance";

    @Autowired
    private IFormInstanceService formInstanceService;

    @RequiresPermissions("activity:formInstance:view")
    @GetMapping()
    public String formInstance() {
        return prefix + "/formInstance";
    }

    /**
     * 查询表单实例列表
     */
    @RequiresPermissions("activity:formInstance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FormInstance formInstance) {
        startPage();
        List<FormInstance> list = formInstanceService.selectFormInstanceList(formInstance);
        return getDataTable(list);
    }

    /**
     * 导出表单实例列表
     */
    @RequiresPermissions("activity:formInstance:export")
    @Log(title = "表单实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormInstance formInstance) {
        List<FormInstance> list = formInstanceService.selectFormInstanceList(formInstance);
        ExcelUtil<FormInstance> util = new ExcelUtil<FormInstance>(FormInstance.class);
        util.exportExcel(response, list, "表单实例数据");
    }

    /**
     * 新增表单实例
     */
    @RequiresPermissions("activity:formInstance:add")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存表单实例
     */
    @RequiresPermissions("activity:formInstance:add")
    @Log(title = "表单实例", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FormInstance formInstance) {
        return toAjax(formInstanceService.insertFormInstance(formInstance));
    }

    /**
     * 修改表单实例
     */
    @RequiresPermissions("activity:formInstance:edit")
    @GetMapping("/edit/{instanceId}")
    public String edit(@PathVariable("instanceId") Long instanceId, ModelMap mmap) {
        FormInstance formInstance = formInstanceService.selectFormInstanceById(instanceId);
        mmap.put("formInstance", formInstance);
        return prefix + "/edit";
    }

    /**
     * 修改保存表单实例
     */
    @RequiresPermissions("activity:formInstance:edit")
    @Log(title = "表单实例", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FormInstance formInstance) {
        return toAjax(formInstanceService.updateFormInstance(formInstance));
    }

    /**
     * 删除表单实例
     */
    @RequiresPermissions("activity:formInstance:remove")
    @Log(title = "表单实例", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        Long[] instanceIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toArray(Long[]::new);
        return toAjax(formInstanceService.deleteFormInstanceByIds(instanceIds));
    }

    /**
     * 查询表单实例详情
     */
    @RequiresPermissions("activity:formInstance:query")
    @GetMapping("/detail/{instanceId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("instanceId") Long instanceId) {
        FormInstance formInstance = formInstanceService.selectFormInstanceById(instanceId);
        return AjaxResult.success(formInstance);
    }

    /**
     * 根据流程实例ID查询表单实例
     */
    @RequiresPermissions("activity:formInstance:query")
    @GetMapping("/processInstance/{processInstanceId}")
    @ResponseBody
    public AjaxResult getByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        FormInstance formInstance = formInstanceService.selectFormInstanceByProcessInstanceId(processInstanceId);
        return AjaxResult.success(formInstance);
    }
}