package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.service.ISysApprovalNodeService;
import com.ruoyi.system.service.ISysApprovalProcessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/approval/node")
public class SysApprovalNodeController extends BaseController {
    private String prefix = "system/approvalNode";

    @Autowired
    private ISysApprovalNodeService sysApprovalNodeService;

    @Autowired
    private ISysApprovalProcessService sysApprovalProcessService;

    @RequiresPermissions("approval:node:view")
    @GetMapping()
    public String approvalNode() {
        return prefix + "/approvalNode";
    }

    @RequiresPermissions("approval:node:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysApprovalNode sysApprovalNode) {
        startPage();
        List<SysApprovalNode> list = sysApprovalNodeService.selectSysApprovalNodeList(sysApprovalNode);
        return getDataTable(list);
    }

    @RequiresPermissions("approval:node:listByProcess")
    @GetMapping("/listByProcess/{processId}")
    @ResponseBody
    public List<SysApprovalNode> listByProcess(@PathVariable("processId") Long processId) {
        return sysApprovalNodeService.selectSysApprovalNodeByProcessId(processId);
    }

    @RequiresPermissions("approval:node:export")
    @Log(title = "审批节点配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysApprovalNode sysApprovalNode) {
        List<SysApprovalNode> list = sysApprovalNodeService.selectSysApprovalNodeList(sysApprovalNode);
        ExcelUtil<SysApprovalNode> util = new ExcelUtil<SysApprovalNode>(SysApprovalNode.class);
        return util.exportExcel(list, "审批节点配置数据");
    }

    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("processes", sysApprovalProcessService.selectSysApprovalProcessList(new SysApprovalProcess()));
        return prefix + "/add";
    }

    @RequiresPermissions("approval:node:add")
    @Log(title = "审批节点配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysApprovalNode sysApprovalNode) {
        return toAjax(sysApprovalNodeService.insertSysApprovalNode(sysApprovalNode));
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysApprovalNode sysApprovalNode = sysApprovalNodeService.selectSysApprovalNodeById(id);
        mmap.put("sysApprovalNode", sysApprovalNode != null ? sysApprovalNode : new SysApprovalNode());
        mmap.put("processes", sysApprovalProcessService.selectSysApprovalProcessList(new SysApprovalProcess()));
        return prefix + "/edit";
    }

    @RequiresPermissions("approval:node:edit")
    @Log(title = "审批节点配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysApprovalNode sysApprovalNode) {
        return toAjax(sysApprovalNodeService.updateSysApprovalNode(sysApprovalNode));
    }

    @RequiresPermissions("approval:node:remove")
    @Log(title = "审批节点配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(
                sysApprovalNodeService.deleteSysApprovalNodeByIds(com.ruoyi.common.core.text.Convert.toLongArray(ids)));
    }
}
