package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.service.ISysApprovalProcessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/approval/process")
public class SysApprovalProcessController extends BaseController {
    private String prefix = "system/approvalProcess";

    @Autowired
    private ISysApprovalProcessService sysApprovalProcessService;

    @RequiresPermissions("approval:process:view")
    @GetMapping()
    public String approvalProcess() {
        return prefix + "/approvalProcess";
    }

    @RequiresPermissions("approval:process:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysApprovalProcess sysApprovalProcess) {
        startPage();
        List<SysApprovalProcess> list = sysApprovalProcessService.selectSysApprovalProcessList(sysApprovalProcess);
        return getDataTable(list);
    }

    @RequiresPermissions("approval:process:export")
    @Log(title = "审批流程配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysApprovalProcess sysApprovalProcess) {
        List<SysApprovalProcess> list = sysApprovalProcessService.selectSysApprovalProcessList(sysApprovalProcess);
        ExcelUtil<SysApprovalProcess> util = new ExcelUtil<SysApprovalProcess>(SysApprovalProcess.class);
        return util.exportExcel(list, "审批流程配置数据");
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @RequiresPermissions("approval:process:add")
    @Log(title = "审批流程配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysApprovalProcess sysApprovalProcess) {
        return toAjax(sysApprovalProcessService.insertSysApprovalProcess(sysApprovalProcess));
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysApprovalProcess sysApprovalProcess = sysApprovalProcessService.selectSysApprovalProcessById(id);
        mmap.put("sysApprovalProcess", sysApprovalProcess != null ? sysApprovalProcess : new SysApprovalProcess());
        return prefix + "/edit";
    }

    @RequiresPermissions("approval:process:edit")
    @Log(title = "审批流程配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysApprovalProcess sysApprovalProcess) {
        return toAjax(sysApprovalProcessService.updateSysApprovalProcess(sysApprovalProcess));
    }

    @RequiresPermissions("approval:process:remove")
    @Log(title = "审批流程配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysApprovalProcessService
                .deleteSysApprovalProcessByIds(com.ruoyi.common.core.text.Convert.toLongArray(ids)));
    }
}
