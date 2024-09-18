package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EudTeacher;

/**
 * 教师管理Mapper接口
 * 
 * @author 张聪
 * @date 2024-08-12
 */
public interface EudTeacherMapper 
{
    /**
     * 查询教师管理
     * 
     * @param id 教师管理主键
     * @return 教师管理
     */
    public EudTeacher selectEudTeacherById(Integer id);

    /**
     * 查询教师管理列表
     * 
     * @param eudTeacher 教师管理
     * @return 教师管理集合
     */
    public List<EudTeacher> selectEudTeacherList(EudTeacher eudTeacher);

    /**
     * 新增教师管理
     * 
     * @param eudTeacher 教师管理
     * @return 结果
     */
    public int insertEudTeacher(EudTeacher eudTeacher);

    /**
     * 修改教师管理
     * 
     * @param eudTeacher 教师管理
     * @return 结果
     */
    public int updateEudTeacher(EudTeacher eudTeacher);

    /**
     * 删除教师管理
     * 
     * @param id 教师管理主键
     * @return 结果
     */
    public int deleteEudTeacherById(Integer id);

    /**
     * 批量删除教师管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEudTeacherByIds(String[] ids);
}
