package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.act.ActProcessDefinition;
import com.ruoyi.activity.service.IActProcessDefinitionService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 流程定义服务实现
 */
@Service
public class ActProcessDefinitionServiceImpl implements IActProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ActProcessDefinition> selectProcessDefinitionList(ActProcessDefinition actProcessDefinition) {
        if (actProcessDefinition == null) {
            actProcessDefinition = new ActProcessDefinition();
        }

        org.activiti.engine.repository.ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();

        if (actProcessDefinition.getName() != null && !actProcessDefinition.getName().trim().isEmpty()) {
            query.processDefinitionNameLike(actProcessDefinition.getName());
        }

        if (actProcessDefinition.getKey() != null && !actProcessDefinition.getKey().trim().isEmpty()) {
            query.processDefinitionKey(actProcessDefinition.getKey());
        }

        List<ProcessDefinition> processDefinitions = query
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        List<ActProcessDefinition> result = new ArrayList<>();
        if (processDefinitions != null) {
            for (ProcessDefinition pd : processDefinitions) {
                ActProcessDefinition dto = new ActProcessDefinition();
                dto.setId(pd.getId());
                dto.setName(pd.getName());
                dto.setKey(pd.getKey());
                dto.setVersion(pd.getVersion());
                dto.setDeploymentId(pd.getDeploymentId());
                dto.setResourceName(pd.getResourceName());
                dto.setDiagramResourceName(pd.getDiagramResourceName());
                dto.setSuspensionState(pd.isSuspended() ? 2 : 1);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public ActProcessDefinition selectProcessDefinitionById(String processDefinitionId) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        if (pd == null) {
            return null;
        }

        ActProcessDefinition dto = new ActProcessDefinition();
        dto.setId(pd.getId());
        dto.setName(pd.getName());
        dto.setKey(pd.getKey());
        dto.setVersion(pd.getVersion());
        dto.setDeploymentId(pd.getDeploymentId());
        dto.setResourceName(pd.getResourceName());
        dto.setDiagramResourceName(pd.getDiagramResourceName());
        dto.setSuspensionState(pd.isSuspended() ? 2 : 1);
        return dto;
    }

    @Override
    public ActProcessDefinition selectProcessDefinitionByKey(String processDefinitionKey) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (pd == null) {
            return null;
        }

        ActProcessDefinition dto = new ActProcessDefinition();
        dto.setId(pd.getId());
        dto.setName(pd.getName());
        dto.setKey(pd.getKey());
        dto.setVersion(pd.getVersion());
        dto.setDeploymentId(pd.getDeploymentId());
        dto.setResourceName(pd.getResourceName());
        dto.setDiagramResourceName(pd.getDiagramResourceName());
        dto.setSuspensionState(pd.isSuspended() ? 2 : 1);
        return dto;
    }

    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Override
    public void deleteProcessDefinition(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    @Override
    public String convertToXml(String processDefinitionId) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        if (pd == null) {
            return "";
        }

        InputStream inputStream = repositoryService.getResourceAsStream(
                pd.getDeploymentId(),
                pd.getResourceName());

        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return new String(bytes, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("读取流程定义XML失败", e);
        }
    }
}