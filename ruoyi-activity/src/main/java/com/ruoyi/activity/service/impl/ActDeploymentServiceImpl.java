package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.act.ActDeployment;
import com.ruoyi.activity.service.IActDeploymentService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * 流程部署服务实现
 */
@Service
public class ActDeploymentServiceImpl implements IActDeploymentService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ActDeployment> selectDeploymentList(ActDeployment actDeployment) {
        DeploymentQuery query = repositoryService.createDeploymentQuery();

        if (actDeployment != null && actDeployment.getName() != null && !actDeployment.getName().isEmpty()) {
            query.deploymentNameLike(actDeployment.getName());
        }

        if (actDeployment != null && actDeployment.getCategory() != null
                && !actDeployment.getCategory().trim().isEmpty() == false) {
            query.deploymentCategory(actDeployment.getCategory());
        }

        List<Deployment> deployments = query
                .orderByDeploymenTime()
                .desc()
                .list();

        List<ActDeployment> result = new ArrayList<>();
        for (Deployment deployment : deployments) {
            ActDeployment dto = new ActDeployment();
            dto.setId(deployment.getId());
            dto.setName(deployment.getName());
            dto.setCategory(deployment.getCategory());
            dto.setDeploymentTime(deployment.getDeploymentTime());
            result.add(dto);
        }
        return result;
    }

    @Override
    public String deploy(String name, String category, MultipartFile file) {
        try {
            Deployment deployment;
            if (file.getOriginalFilename().endsWith(".zip")) {
                ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
                deployment = repositoryService.createDeployment()
                        .name(name)
                        .category(category)
                        .addZipInputStream(zipInputStream)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()
                        .name(name)
                        .category(category)
                        .addInputStream(file.getOriginalFilename(), file.getInputStream())
                        .deploy();
            }
            return deployment.getId();
        } catch (IOException e) {
            throw new RuntimeException("流程部署失败", e);
        }
    }

    @Override
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public InputStream getResource(String deploymentId, String resourceName) {
        return repositoryService.getResourceAsStream(deploymentId, resourceName);
    }

    @Override
    public InputStream getProcessImage(String deploymentId) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .list();

        if (processDefinitions.isEmpty()) {
            throw new RuntimeException("未找到流程定义");
        }

        ProcessDefinition processDefinition = processDefinitions.get(0);
        String diagramResourceName = processDefinition.getDiagramResourceName();

        return repositoryService.getResourceAsStream(
                deploymentId,
                diagramResourceName);
    }

    @Override
    public Map<String, Object> getDeploymentInfo(String deploymentId) {
        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .list();

        Map<String, Object> result = new HashMap<>();
        result.put("deployment", deployment);
        result.put("processDefinitions", processDefinitions);

        return result;
    }
}