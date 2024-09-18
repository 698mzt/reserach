package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EduCourse;

/**
 * 科目管理Mapper接口
 * 
 * @author 张聪
 * @date 2024-08-14
 */
public interface EduCourseMapper 
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
     * 删除科目管理
     * 
     * @param id 科目管理主键
     * @return 结果
     */
    public int deleteEduCourseById(Integer id);

    /**
     * 批量删除科目管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduCourseByIds(String[] ids);

    public List<EduCourse>  selectCourseAll();
}
