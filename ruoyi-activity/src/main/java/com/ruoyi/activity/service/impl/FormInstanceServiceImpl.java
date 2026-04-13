package com.ruoyi.activity.service.impl;

import com.ruoyi.activity.domain.FormInstance;
import com.ruoyi.activity.mapper.FormInstanceMapper;
import com.ruoyi.activity.service.IFormInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 表单实例服务实现
 */
@Service
public class FormInstanceServiceImpl implements IFormInstanceService {

    @Autowired
    private FormInstanceMapper formInstanceMapper;

    @Override
    public List<FormInstance> selectFormInstanceList(FormInstance formInstance) {
        return formInstanceMapper.selectFormInstanceList(formInstance);
    }

    @Override
    public FormInstance selectFormInstanceById(Long instanceId) {
        return formInstanceMapper.selectFormInstanceById(instanceId);
    }

    @Override
    public int insertFormInstance(FormInstance formInstance) {
        return formInstanceMapper.insertFormInstance(formInstance);
    }

    @Override
    public int updateFormInstance(FormInstance formInstance) {
        return formInstanceMapper.updateFormInstance(formInstance);
    }

    @Override
    public int deleteFormInstanceById(Long instanceId) {
        return formInstanceMapper.deleteFormInstanceById(instanceId);
    }

    @Override
    public int deleteFormInstanceByIds(Long[] instanceIds) {
        return formInstanceMapper.deleteFormInstanceByIds(instanceIds);
    }

    @Override
    public FormInstance selectFormInstanceByProcessInstanceId(String processInstanceId) {
        return formInstanceMapper.selectFormInstanceByProcessInstanceId(processInstanceId);
    }
}