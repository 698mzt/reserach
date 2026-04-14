package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.FormDefinition;
import com.ruoyi.activity.mapper.FormDefinitionMapper;
import com.ruoyi.activity.service.IFormDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单定义服务实现
 */
@Service
public class FormDefinitionServiceImpl implements IFormDefinitionService {

    @Autowired
    private FormDefinitionMapper formDefinitionMapper;

    @Override
    public List<FormDefinition> selectFormDefinitionList(FormDefinition formDefinition) {
        List<FormDefinition> list = formDefinitionMapper.selectFormDefinitionList(formDefinition);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public FormDefinition selectFormDefinitionById(Long formId) {
        if (formId == null) {
            return null;
        }
        return formDefinitionMapper.selectFormDefinitionById(formId);
    }

    @Override
    public int insertFormDefinition(FormDefinition formDefinition) {
        if (formDefinition == null) {
            return 0;
        }
        return formDefinitionMapper.insertFormDefinition(formDefinition);
    }

    @Override
    public int updateFormDefinition(FormDefinition formDefinition) {
        if (formDefinition == null || formDefinition.getFormId() == null) {
            return 0;
        }
        return formDefinitionMapper.updateFormDefinition(formDefinition);
    }

    @Override
    public int deleteFormDefinitionById(Long formId) {
        if (formId == null) {
            return 0;
        }
        return formDefinitionMapper.deleteFormDefinitionById(formId);
    }

    @Override
    public int deleteFormDefinitionByIds(Long[] formIds) {
        if (formIds == null || formIds.length == 0) {
            return 0;
        }
        return formDefinitionMapper.deleteFormDefinitionByIds(formIds);
    }

    @Override
    public FormDefinition selectFormDefinitionByKey(String formKey) {
        if (formKey == null || formKey.trim().isEmpty()) {
            return null;
        }
        return formDefinitionMapper.selectFormDefinitionByKey(formKey);
    }
}
