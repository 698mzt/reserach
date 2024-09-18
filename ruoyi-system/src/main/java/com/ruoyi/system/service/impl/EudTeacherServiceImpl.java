package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.EduTeaCourse;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.EduTeaCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EudTeacherMapper;
import com.ruoyi.system.domain.EudTeacher;
import com.ruoyi.system.service.IEudTeacherService;
import com.ruoyi.common.core.text.Convert;

/**
 * 教师管理Service业务层处理
 * 
 * @author 张聪
 * @date 2024-08-12
 */
@Service
public class EudTeacherServiceImpl implements IEudTeacherService 
{
    @Autowired
    private EudTeacherMapper eudTeacherMapper;
    @Autowired
    private  EduTeaCourseMapper eduTeaCourseMapper;

    /**
     * 查询教师管理
     * 
     * @param id 教师管理主键
     * @return 教师管理
     */
    @Override
    public EudTeacher selectEudTeacherById(Integer id)
    {
        return eudTeacherMapper.selectEudTeacherById(id);
    }

    /**
     * 查询教师管理列表
     * 
     * @param eudTeacher 教师管理
     * @return 教师管理
     */
    @Override
    public List<EudTeacher> selectEudTeacherList(EudTeacher eudTeacher)
    {
        return eudTeacherMapper.selectEudTeacherList(eudTeacher);
    }

    /**
     * 新增教师管理
     * 
     * @param eudTeacher 教师管理
     * @return 结果
     */
    @Override
    public int insertEudTeacher(EudTeacher eudTeacher)
    {
        int rows =eudTeacherMapper.insertEudTeacher(eudTeacher);
        // 新增教师与课程管理
        insertTeaCourse(eudTeacher.getId(), eudTeacher.getCourses());
        return  rows;
    }

    /**
     * 修改教师管理
     * 
     * @param tea 教师管理
     * @return 结果
     */
    @Override
    public int updateEudTeacher(EudTeacher tea)
    {
       int row = eudTeacherMapper.updateEudTeacher(tea);
        //1删除中间表中的数据
        eduTeaCourseMapper.deleteEduTeaCourseByTid(tea.getId());
        // 2新增教师与课程管理
        insertTeaCourse(tea.getId(), tea.getCourses());
        return row;
    }

    /**
     * 批量删除教师管理
     * 
     * @param ids 需要删除的教师管理主键
     * @return 结果
     */
    @Override
    public int deleteEudTeacherByIds(String ids)
    {
        String[] st = Convert.toStrArray(ids);
        for (String s : st){
            eduTeaCourseMapper.deleteEduTeaCourseByTid(Integer.valueOf(s));
        }


        return eudTeacherMapper.deleteEudTeacherByIds(st);
    }

    /**
     * 删除教师管理信息
     * 
     * @param id 教师管理主键
     * @return 结果
     */
    @Override
    public int deleteEudTeacherById(Integer id)
    {

        return eudTeacherMapper.deleteEudTeacherById(id);
    }

    /**
     * 新增教师课程信息
     *
     * @param tid 教师id
     * @param courses 课程数据组
     */
    public void insertTeaCourse(Integer tid, Integer[] courses)
    {
        if (StringUtils.isNotNull(courses))
        {
            // 新增教师与课程管理
            List<EduTeaCourse> list = new ArrayList<EduTeaCourse>();
            for (Integer course : courses)
            {
                EduTeaCourse ur = new EduTeaCourse();
                ur.setTid(tid);
                ur.setCid(course);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                eduTeaCourseMapper.batchTeaCoures(list);
            }
        }
    }
}
