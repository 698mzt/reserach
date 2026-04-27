package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.service.IApprovalProcessService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批操作控制器
 * <p>
 * 提供审批流的统一 REST 入口，包括提交、通过、驳回、撤回、
 * 查询当前节点、审批历史、流程节点等操作。
 * 所有操作均通过 IApprovalProcessService 驱动，状态流转由数据库配置决定。
 * </p>
 *
 * @see IApprovalProcessService
 * @see ApprovalRequest
 * @see ApprovalResult
 */
@Controller
@RequestMapping("/approval")
public class ApprovalController extends BaseController {

    @Autowired
    private IApprovalProcessService approvalProcessService;

    /**
     * 获取当前审批节点信息
     *
     * @param processCode  流程编码
     * @param currentState 当前业务状态编码
     * @return 当前节点、下一节点、节点列表、流程配置
     */
    @RequiresPermissions("approval:view")
    @PostMapping("/getCurrentNode")
    @ResponseBody
    public AjaxResult getCurrentNode(@RequestParam String processCode, @RequestParam String currentState) {
        ApprovalResult result = approvalProcessService.getCurrentNode(processCode, currentState);
        return result.isSuccess() ? AjaxResult.success(result) : AjaxResult.error(result.getMessage());
    }

    /**
     * 提交审批
     * <p>
     * 将业务数据从草稿状态提交到第一个审批节点，
     * 操作人信息从当前登录用户（Shiro Session）中获取。
     * </p>
     *
     * @param processCode  流程编码
     * @param businessId   业务数据ID
     * @param currentState 当前业务状态编码
     * @param comment      审批意见（可选）
     * @return 提交结果，包含新状态编码
     */
    @RequiresPermissions("approval:submit")
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult submit(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        ApprovalRequest request = ApprovalRequest.of(processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        ApprovalResult result = approvalProcessService.submitApproval(request);
        return result.isSuccess() ? AjaxResult.success(result) : AjaxResult.error(result.getMessage());
    }

    /**
     * 审批通过
     * <p>
     * 当前节点审批通过，状态流转到 passState 指定的下一状态。
     * 若 isLast=true，表示流程已走完，调用方可执行终态逻辑。
     * </p>
     *
     * @param processCode  流程编码
     * @param businessId   业务数据ID
     * @param currentState 当前业务状态编码
     * @param comment      审批意见（可选）
     * @return 审批结果，包含新状态编码和 isLast 标识
     */
    @RequiresPermissions("approval:approve")
    @PostMapping("/approve")
    @ResponseBody
    public AjaxResult approve(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        ApprovalRequest request = ApprovalRequest.of(processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        ApprovalResult result = approvalProcessService.approve(request);
        return result.isSuccess() ? AjaxResult.success(result) : AjaxResult.error(result.getMessage());
    }

    /**
     * 审批驳回
     * <p>
     * 当前节点审批驳回，状态回退到 rejectState 指定的状态。
     * 会检查节点的 canBack 配置，若不允许驳回则返回失败。
     * </p>
     *
     * @param processCode  流程编码
     * @param businessId   业务数据ID
     * @param currentState 当前业务状态编码
     * @param comment      驳回原因（可选）
     * @return 驳回结果，包含回退后的新状态编码
     */
    @RequiresPermissions("approval:reject")
    @PostMapping("/reject")
    @ResponseBody
    public AjaxResult reject(@RequestParam String processCode,
            @RequestParam Long businessId,
            @RequestParam String currentState,
            @RequestParam(required = false) String comment) {
        SysUser user = getSysUser();
        ApprovalRequest request = ApprovalRequest.of(processCode, businessId, currentState, comment,
                user.getUserId(), user.getUserName(), user.getDept() != null ? user.getDept().getDeptName() : "");
        ApprovalResult result = approvalProcessService.reject(request);
        return result.isSuccess() ? AjaxResult.success(result) : AjaxResult.error(result.getMessage());
    }

    /**
     * 查询审批历史记录
     *
     * @param processCode 流程编码
     * @param businessId  业务数据ID
     * @return 审批历史列表
     */
    @RequiresPermissions("approval:history")
    @GetMapping("/history/{processCode}/{businessId}")
    @ResponseBody
    public AjaxResult getHistory(@PathVariable("processCode") String processCode,
            @PathVariable("businessId") Long businessId) {
        List<SysApprovalHistory> list = approvalProcessService.getApprovalHistory(processCode, businessId);
        return AjaxResult.success(list);
    }

    /**
     * 查询流程的所有审批节点
     *
     * @param processCode 流程编码
     * @return 审批节点列表，按 nodeOrder 排序
     */
    @RequiresPermissions("approval:nodes")
    @GetMapping("/nodes/{processCode}")
    @ResponseBody
    public AjaxResult getNodes(@PathVariable("processCode") String processCode) {
        List<SysApprovalNode> list = approvalProcessService.getProcessNodes(processCode);
        return AjaxResult.success(list);
    }
}
