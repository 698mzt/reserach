package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.FormDefinition;
import com.ruoyi.activity.service.IFormDefinitionService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 表单定义控制器
 */
@Controller
@RequestMapping("/activity/form")
public class FormDefinitionController extends BaseController {

    private String prefix = "activity/form";

    @Autowired
    private IFormDefinitionService formDefinitionService;

    @RequiresPermissions("activity:form:view")
    @GetMapping()
    public String form() {
        return prefix + "/form";
    }

    /**
     * 查询表单定义列表
     */
    @RequiresPermissions("activity:form:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FormDefinition formDefinition) {
        try {
            startPage();
            List<FormDefinition> list = formDefinitionService.selectFormDefinitionList(
                    formDefinition != null ? formDefinition : new FormDefinition());
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询表单定义列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }

    /**
     * 导出表单定义列表
     */
    @RequiresPermissions("activity:form:export")
    @Log(title = "表单定义", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormDefinition formDefinition) {
        List<FormDefinition> list = formDefinitionService.selectFormDefinitionList(formDefinition);
        ExcelUtil<FormDefinition> util = new ExcelUtil<FormDefinition>(FormDefinition.class);
        util.exportExcel(response, list, "表单定义数据");
    }

    /**
     * 新增表单定义
     */
    @RequiresPermissions("activity:form:add")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存表单定义
     */
    @RequiresPermissions("activity:form:add")
    @Log(title = "表单定义", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FormDefinition formDefinition) {
        return toAjax(formDefinitionService.insertFormDefinition(formDefinition));
    }

    /**
     * 修改表单定义
     */
    @RequiresPermissions("activity:form:edit")
    @GetMapping("/edit/{formId}")
    public String edit(@PathVariable("formId") Long formId, ModelMap mmap) {
        FormDefinition formDefinition = formDefinitionService.selectFormDefinitionById(formId);
        mmap.put("formDefinition", formDefinition);
        return prefix + "/edit";
    }

    /**
     * 修改保存表单定义
     */
    @RequiresPermissions("activity:form:edit")
    @Log(title = "表单定义", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FormDefinition formDefinition) {
        return toAjax(formDefinitionService.updateFormDefinition(formDefinition));
    }

    /**
     * 删除表单定义
     */
    @RequiresPermissions("activity:form:remove")
    @Log(title = "表单定义", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        Long[] formIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toArray(Long[]::new);
        return toAjax(formDefinitionService.deleteFormDefinitionByIds(formIds));
    }

    /**
     * 查询表单定义详情
     */
    @RequiresPermissions("activity:form:query")
    @GetMapping("/detail/{formId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("formId") Long formId) {
        FormDefinition formDefinition = formDefinitionService.selectFormDefinitionById(formId);
        return AjaxResult.success(formDefinition);
    }
}