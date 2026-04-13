package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.FormDefinition;
import java.util.List;

/**
 * 表单定义服务接口
 */
public interface IFormDefinitionService {

    /**
     * 查询表单定义列表
     *
     * @param formDefinition 表单定义
     * @return 表单定义列表
     */
    List<FormDefinition> selectFormDefinitionList(FormDefinition formDefinition);

    /**
     * 查询表单定义详情
     *
     * @param formId 表单定义ID
     * @return 表单定义
     */
    FormDefinition selectFormDefinitionById(Long formId);

    /**
     * 新增表单定义
     *
     * @param formDefinition 表单定义
     * @return 结果
     */
    int insertFormDefinition(FormDefinition formDefinition);

    /**
     * 修改表单定义
     *
     * @param formDefinition 表单定义
     * @return 结果
     */
    int updateFormDefinition(FormDefinition formDefinition);

    /**
     * 删除表单定义
     *
     * @param formId 表单定义ID
     * @return 结果
     */
    int deleteFormDefinitionById(Long formId);

    /**
     * 批量删除表单定义
     *
     * @param formIds 需要删除的数据ID
     * @return 结果
     */
    int deleteFormDefinitionByIds(Long[] formIds);

    /**
     * 根据表单Key查询表单定义
     *
     * @param formKey 表单标识
     * @return 表单定义
     */
    FormDefinition selectFormDefinitionByKey(String formKey);
}