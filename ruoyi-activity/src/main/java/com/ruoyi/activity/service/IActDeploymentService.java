package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.act.ActDeployment;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程部署服务接口
 */
public interface IActDeploymentService {

    /**
     * 查询流程部署列表
     *
     * @param actDeployment 流程部署
     * @return 流程部署列表
     */
    List<ActDeployment> selectDeploymentList(ActDeployment actDeployment);

    /**
     * 部署流程
     *
     * @param name 流程名称
     * @param category 分类
     * @param file 流程文件
     * @return 部署ID
     */
    String deploy(String name, String category, MultipartFile file);

    /**
     * 删除部署
     *
     * @param deploymentId 部署ID
     */
    void deleteDeployment(String deploymentId);

    /**
     * 获取流程资源文件
     *
     * @param deploymentId 部署ID
     * @param resourceName 资源名称
     * @return 输入流
     */
    InputStream getResource(String deploymentId, String resourceName);

    /**
     * 获取流程图片
     *
     * @param deploymentId 部署ID
     * @return 输入流
     */
    InputStream getProcessImage(String deploymentId);

    /**
     * 查询部署详情
     *
     * @param deploymentId 部署ID
     * @return 部署详情
     */
    Map<String, Object> getDeploymentInfo(String deploymentId);
}