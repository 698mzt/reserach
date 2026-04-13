package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/approval/history")
public class SysApprovalHistoryController extends BaseController {
    private String prefix = "system/approvalHistory";

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

    @RequiresPermissions("approval:history:view")
    @GetMapping()
    public String approvalHistory() {
        return prefix + "/approvalHistory";
    }

    @RequiresPermissions("approval:history:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysApprovalHistory sysApprovalHistory) {
        startPage();
        List<SysApprovalHistory> list = sysApprovalHistoryService.selectSysApprovalHistoryList(sysApprovalHistory);
        return getDataTable(list);
    }

    @RequiresPermissions("approval:history:listByBusiness")
    @GetMapping("/listByBusiness/{processCode}/{businessId}")
    @ResponseBody
    public List<SysApprovalHistory> listByBusiness(@PathVariable("processCode") String processCode,
            @PathVariable("businessId") Long businessId) {
        return sysApprovalHistoryService.selectSysApprovalHistoryByBusinessId(processCode, businessId);
    }

    @RequiresPermissions("approval:history:export")
    @Log(title = "审批历史查询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysApprovalHistory sysApprovalHistory) {
        List<SysApprovalHistory> list = sysApprovalHistoryService.selectSysApprovalHistoryList(sysApprovalHistory);
        ExcelUtil<SysApprovalHistory> util = new ExcelUtil<SysApprovalHistory>(SysApprovalHistory.class);
        return util.exportExcel(list, "审批历史查询数据");
    }

    @RequiresPermissions("approval:history:remove")
    @Log(title = "审批历史查询", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysApprovalHistoryService
                .deleteSysApprovalHistoryByIds(com.ruoyi.common.core.text.Convert.toLongArray(ids)));
    }
}
