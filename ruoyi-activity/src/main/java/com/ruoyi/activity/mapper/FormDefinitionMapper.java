package com.ruoyi.activity.mapper;

import com.ruoyi.activity.domain.FormDefinition;
import java.util.List;

/**
 * 表单定义Mapper接口
 */
public interface FormDefinitionMapper {

    /**
     * 查询表单定义
     *
     * @param formId 表单定义ID
     * @return 表单定义
     */
    public FormDefinition selectFormDefinitionById(Long formId);

    /**
     * 查询表单定义列表
     *
     * @param formDefinition 表单定义
     * @return 表单定义集合
     */
    public List<FormDefinition> selectFormDefinitionList(FormDefinition formDefinition);

    /**
     * 新增表单定义
     *
     * @param formDefinition 表单定义
     * @return 结果
     */
    public int insertFormDefinition(FormDefinition formDefinition);

    /**
     * 修改表单定义
     *
     * @param formDefinition 表单定义
     * @return 结果
     */
    public int updateFormDefinition(FormDefinition formDefinition);

    /**
     * 删除表单定义
     *
     * @param formId 表单定义ID
     * @return 结果
     */
    public int deleteFormDefinitionById(Long formId);

    /**
     * 批量删除表单定义
     *
     * @param formIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFormDefinitionByIds(Long[] formIds);

    /**
     * 根据表单Key查询表单定义
     *
     * @param formKey 表单标识
     * @return 表单定义
     */
    public FormDefinition selectFormDefinitionByKey(String formKey);
}