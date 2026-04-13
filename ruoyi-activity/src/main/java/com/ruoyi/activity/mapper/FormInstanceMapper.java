package com.ruoyi.activity.mapper;

import com.ruoyi.activity.domain.FormInstance;
import java.util.List;

/**
 * 表单实例Mapper接口
 */
public interface FormInstanceMapper {

    /**
     * 查询表单实例
     *
     * @param instanceId 表单实例ID
     * @return 表单实例
     */
    public FormInstance selectFormInstanceById(Long instanceId);

    /**
     * 查询表单实例列表
     *
     * @param formInstance 表单实例
     * @return 表单实例集合
     */
    public List<FormInstance> selectFormInstanceList(FormInstance formInstance);

    /**
     * 新增表单实例
     *
     * @param formInstance 表单实例
     * @return 结果
     */
    public int insertFormInstance(FormInstance formInstance);

    /**
     * 修改表单实例
     *
     * @param formInstance 表单实例
     * @return 结果
     */
    public int updateFormInstance(FormInstance formInstance);

    /**
     * 删除表单实例
     *
     * @param instanceId 表单实例ID
     * @return 结果
     */
    public int deleteFormInstanceById(Long instanceId);

    /**
     * 批量删除表单实例
     *
     * @param instanceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFormInstanceByIds(Long[] instanceIds);

    /**
     * 根据流程实例ID查询表单实例
     *
     * @param processInstanceId 流程实例ID
     * @return 表单实例
     */
    public FormInstance selectFormInstanceByProcessInstanceId(String processInstanceId);
}