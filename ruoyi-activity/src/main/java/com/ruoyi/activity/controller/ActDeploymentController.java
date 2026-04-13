package com.ruoyi.activity.controller;

import com.ruoyi.activity.domain.act.ActDeployment;
import com.ruoyi.activity.service.IActDeploymentService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程部署控制器
 */
@Controller
@RequestMapping("/activity/deployment")
public class ActDeploymentController extends BaseController {

    private String prefix = "activity/deployment";

    @Autowired
    private IActDeploymentService deploymentService;

    @RequiresPermissions("activity:deployment:view")
    @GetMapping()
    public String deployment() {
        return prefix + "/deployment";
    }

    /**
     * 查询流程部署列表
     */
    @RequiresPermissions("activity:deployment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActDeployment actDeployment) {
        try {
            List<ActDeployment> list = deploymentService.selectDeploymentList(actDeployment != null ? actDeployment : new ActDeployment());
            return getDataTable(list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("查询部署列表失败", e);
            return getDataTable(new ArrayList<>());
        }
    }


    /**
     * 部署流程
     */
    @RequiresPermissions("activity:deployment:add")
    @Log(title = "流程部署", businessType = BusinessType.INSERT)
    @PostMapping("/deploy")
    @ResponseBody
    public AjaxResult deploy(@RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("file") MultipartFile file) {
        try {
            String deploymentId = deploymentService.deploy(name, category, file);
            return AjaxResult.success("部署成功", deploymentId);
        } catch (Exception e) {
            return AjaxResult.error("部署失败：" + e.getMessage());
        }
    }

    /**
     * 删除部署
     */
    @RequiresPermissions("activity:deployment:remove")
    @Log(title = "流程部署", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deploymentId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable String deploymentId) {
        deploymentService.deleteDeployment(deploymentId);
        return AjaxResult.success();
    }

    /**
     * 获取流程资源
     */
    @GetMapping("/resource/{deploymentId}/{resourceName}")
    public void getResource(@PathVariable String deploymentId,
            @PathVariable String resourceName,
            HttpServletResponse response) throws IOException {
        InputStream inputStream = deploymentService.getResource(deploymentId, resourceName);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 获取流程图片
     */
    @GetMapping("/image/{deploymentId}")
    public void getProcessImage(@PathVariable String deploymentId,
            HttpServletResponse response) throws IOException {
        InputStream inputStream = deploymentService.getProcessImage(deploymentId);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 部署详情
     */
    @RequiresPermissions("activity:deployment:query")
    @GetMapping("/detail/{deploymentId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable String deploymentId) {
        Map<String, Object> deploymentInfo = deploymentService.getDeploymentInfo(deploymentId);
        return AjaxResult.success(deploymentInfo);
    }
}