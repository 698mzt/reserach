package com.ruoyi.activity.service;

import com.ruoyi.activity.domain.FormInstance;
import java.util.List;

/**
 * 表单实例服务接口
 */
public interface IFormInstanceService {

    /**
     * 查询表单实例列表
     *
     * @param formInstance 表单实例
     * @return 表单实例集合
     */
    List<FormInstance> selectFormInstanceList(FormInstance formInstance);

    /**
     * 查询表单实例
     *
     * @param instanceId 表单实例ID
     * @return 表单实例
     */
    FormInstance selectFormInstanceById(Long instanceId);

    /**
     * 新增表单实例
     *
     * @param formInstance 表单实例
     * @return 结果
     */
    int insertFormInstance(FormInstance formInstance);

    /**
     * 修改表单实例
     *
     * @param formInstance 表单实例
     * @return 结果
     */
    int updateFormInstance(FormInstance formInstance);

    /**
     * 删除表单实例
     *
     * @param instanceId 表单实例ID
     * @return 结果
     */
    int deleteFormInstanceById(Long instanceId);

    /**
     * 批量删除表单实例
     *
     * @param instanceIds 需要删除的数据ID
     * @return 结果
     */
    int deleteFormInstanceByIds(Long[] instanceIds);

    /**
     * 根据流程实例ID查询表单实例
     *
     * @param processInstanceId 流程实例ID
     * @return 表单实例
     */
    FormInstance selectFormInstanceByProcessInstanceId(String processInstanceId);
}