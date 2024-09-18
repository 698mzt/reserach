package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.domain.EduTeaCourse;
import com.ruoyi.system.mapper.EduTeaCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EduCourseMapper;
import com.ruoyi.system.domain.EduCourse;
import com.ruoyi.system.service.IEduCourseService;
import com.ruoyi.common.core.text.Convert;

/**
 * 科目管理Service业务层处理
 * 
 * @author 张聪
 * @date 2024-08-14
 */
@Service
public class EduCourseServiceImpl implements IEduCourseService 
{
    @Autowired
    private EduCourseMapper eduCourseMapper;

    @Autowired
    private EduTeaCourseMapper eduTeaCourseMapper;

    /**
     * 查询科目管理
     * 
     * @param id 科目管理主键
     * @return 科目管理
     */
    @Override
    public EduCourse selectEduCourseById(Integer id)
    {
        return eduCourseMapper.selectEduCourseById(id);
    }

    /**
     * 查询科目管理列表
     * 
     * @param eduCourse 科目管理
     * @return 科目管理
     */
    @Override
    public List<EduCourse> selectEduCourseList(EduCourse eduCourse)
    {
        return eduCourseMapper.selectEduCourseList(eduCourse);
    }

    /**
     * 新增科目管理
     * 
     * @param eduCourse 科目管理
     * @return 结果
     */
    @Override
    public int insertEduCourse(EduCourse eduCourse)
    {
        return eduCourseMapper.insertEduCourse(eduCourse);
    }

    /**
     * 修改科目管理
     * 
     * @param eduCourse 科目管理
     * @return 结果
     */
    @Override
    public int updateEduCourse(EduCourse eduCourse)
    {
        return eduCourseMapper.updateEduCourse(eduCourse);
    }

    /**
     * 批量删除科目管理
     * 
     * @param ids 需要删除的科目管理主键
     * @return 结果
     */
    @Override
    public int deleteEduCourseByIds(String ids)
    {
        return eduCourseMapper.deleteEduCourseByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除科目管理信息
     * 
     * @param id 科目管理主键
     * @return 结果
     */
    @Override
    public int deleteEduCourseById(Integer id)
    {
        return eduCourseMapper.deleteEduCourseById(id);
    }

    @Override
    public List<EduCourse> selectCourseAll() {
        return eduCourseMapper.selectCourseAll();
    }

    @Override
    public List<EduCourse> selectCoursesByTeaId(Integer id) {
        //中间表的集合
        List<EduTeaCourse> teaCourses = eduTeaCourseMapper.selectEduTeaCourseByTid(id);//查询你选择的课程ids

        List<EduCourse> courses = selectCourseAll();//查询所有的课程
        for (EduCourse cr : courses)//所有的课程
        {
            for (EduTeaCourse tcr : teaCourses)//中间表你选择的课程
            {
                if (cr.getId() == tcr.getCid())
                {
                    cr.setFlag(true);
                    break;
                }
            }
        }
        return courses;
    }
}
