package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EduCourse;

/**
 * 科目管理Service接口
 * 
 * @author 张聪
 * @date 2024-08-14
 */
public interface IEduCourseService 
{
    /**
     * 查询科目管理
     * 
     * @param id 科目管理主键
     * @return 科目管理
     */
    public EduCourse selectEduCourseById(Integer id);

    /**
     * 查询科目管理列表
     * 
     * @param eduCourse 科目管理
     * @return 科目管理集合
     */
    public List<EduCourse> selectEduCourseList(EduCourse eduCourse);

    /**
     * 新增科目管理
     * 
     * @param eduCourse 科目管理
     * @return 结果
     */
    public int insertEduCourse(EduCourse eduCourse);

    /**
     * 修改科目管理
     * 
     * @param eduCourse 科目管理
     * @return 结果
     */
    public int updateEduCourse(EduCourse eduCourse);

    /**
     * 批量删除科目管理
     * 
     * @param ids 需要删除的科目管理主键集合
     * @return 结果
     */
    public int deleteEduCourseByIds(String ids);

    /**
     * 删除科目管理信息
     * 
     * @param id 科目管理主键
     * @return 结果
     */
    public int deleteEduCourseById(Integer id);

    public List<EduCourse> selectCourseAll();

    List<EduCourse> selectCoursesByTeaId(Integer id);
}
