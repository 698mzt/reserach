package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysApprovalState;
import com.ruoyi.system.service.ISysApprovalStateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/approval/state")
public class SysApprovalStateController extends BaseController {
    private String prefix = "system/approvalState";

    @Autowired
    private ISysApprovalStateService sysApprovalStateService;

    @RequiresPermissions("approval:state:view")
    @GetMapping()
    public String approvalState() {
        return prefix + "/approvalState";
    }

    @RequiresPermissions("approval:state:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysApprovalState sysApprovalState) {
        startPage();
        List<SysApprovalState> list = sysApprovalStateService.selectSysApprovalStateList(sysApprovalState);
        return getDataTable(list);
    }

    @RequiresPermissions("approval:state:listByProcess")
    @GetMapping("/listByProcess/{processCode}")
    @ResponseBody
    public List<SysApprovalState> listByProcess(@PathVariable("processCode") String processCode) {
        return sysApprovalStateService.selectSysApprovalStateByProcessCode(processCode);
    }

    @RequiresPermissions("approval:state:export")
    @Log(title = "审批状态管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysApprovalState sysApprovalState) {
        List<SysApprovalState> list = sysApprovalStateService.selectSysApprovalStateList(sysApprovalState);
        ExcelUtil<SysApprovalState> util = new ExcelUtil<SysApprovalState>(SysApprovalState.class);
        return util.exportExcel(list, "审批状态管理数据");
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @RequiresPermissions("approval:state:add")
    @Log(title = "审批状态管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysApprovalState sysApprovalState) {
        return toAjax(sysApprovalStateService.insertSysApprovalState(sysApprovalState));
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysApprovalState sysApprovalState = sysApprovalStateService.selectSysApprovalStateById(id);
        mmap.put("sysApprovalState", sysApprovalState != null ? sysApprovalState : new SysApprovalState());
        return prefix + "/edit";
    }

    @RequiresPermissions("approval:state:edit")
    @Log(title = "审批状态管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysApprovalState sysApprovalState) {
        return toAjax(sysApprovalStateService.updateSysApprovalState(sysApprovalState));
    }

    @RequiresPermissions("approval:state:remove")
    @Log(title = "审批状态管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysApprovalStateService
                .deleteSysApprovalStateByIds(com.ruoyi.common.core.text.Convert.toLongArray(ids)));
    }
}
