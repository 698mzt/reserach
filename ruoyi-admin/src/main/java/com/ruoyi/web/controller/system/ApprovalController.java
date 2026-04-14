package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.service.IApprovalProcessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/approval")
public class ApprovalController extends BaseController {
    @Autowired
    private IApprovalProcessService approvalProcessService;

    @RequiresPermissions("approval:view")
    @PostMapping("/getCurrentNode")
    @ResponseBody
    public AjaxResult getCurrentNode(@RequestParam String processCode, @RequestParam String currentState) {
        Map<String, Object> result = approvalProcessService.getCurrentNode(processCode, currentState);
        return Boolean.TRUE.equals(result.get("success")) ? AjaxResult.success(result)
                : AjaxResult.error((String) result.get("message"));
    }

    @RequiresPermissions("approval:submit")
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult submit(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        Map<String, Object> result = approvalProcessService.submitApproval(
                processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        return Boolean.TRUE.equals(result.get("success")) ? AjaxResult.success(result)
                : AjaxResult.error((String) result.get("message"));
    }

    @RequiresPermissions("approval:approve")
    @PostMapping("/approve")
    @ResponseBody
    public AjaxResult approve(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        Map<String, Object> result = approvalProcessService.approve(
                processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        return Boolean.TRUE.equals(result.get("success")) ? AjaxResult.success(result)
                : AjaxResult.error((String) result.get("message"));
    }

    @RequiresPermissions("approval:reject")
    @PostMapping("/reject")
    @ResponseBody
    public AjaxResult reject(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        Map<String, Object> result = approvalProcessService.reject(
                processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        return Boolean.TRUE.equals(result.get("success")) ? AjaxResult.success(result)
                : AjaxResult.error((String) result.get("message"));
    }

    @RequiresPermissions("approval:history")
    @GetMapping("/history/{processCode}/{businessId}")
    @ResponseBody
    public AjaxResult getHistory(@PathVariable("processCode") String processCode,
            @PathVariable("businessId") Long businessId) {
        List<SysApprovalHistory> list = approvalProcessService.getApprovalHistory(processCode, businessId);
        return AjaxResult.success(list);
    }

    @RequiresPermissions("approval:nodes")
    @GetMapping("/nodes/{processCode}")
    @ResponseBody
    public AjaxResult getNodes(@PathVariable("processCode") String processCode) {
        List<SysApprovalNode> list = approvalProcessService.getProcessNodes(processCode);
        return AjaxResult.success(list);
    }
}
