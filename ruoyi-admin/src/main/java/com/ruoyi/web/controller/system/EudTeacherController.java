package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.domain.EduCourse;
import com.ruoyi.system.service.IEduCourseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.EudTeacher;
import com.ruoyi.system.service.IEudTeacherService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教师管理Controller
 * 
 * @author 张聪
 * @date 2024-08-12
 */
@Controller
@RequestMapping("/system/teacher")
public class EudTeacherController extends BaseController
{
    private String prefix = "system/teacher";

    @Autowired
    private IEudTeacherService eudTeacherService;

    @Autowired
    private IEduCourseService eduCourseService;

    @RequiresPermissions("system:teacher:view")
    @GetMapping()
    public String teacher()
    {
        return prefix + "/teacher";
    }

    /**
     * 查询教师管理列表
     */
    @RequiresPermissions("system:teacher:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EudTeacher eudTeacher)
    {
        startPage();
        List<EudTeacher> list = eudTeacherService.selectEudTeacherList(eudTeacher);
        return getDataTable(list);
    }

    /**
     * 导出教师管理列表
     */
    @RequiresPermissions("system:teacher:export")
    @Log(title = "教师管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EudTeacher eudTeacher)
    {
        List<EudTeacher> list = eudTeacherService.selectEudTeacherList(eudTeacher);
        ExcelUtil<EudTeacher> util = new ExcelUtil<EudTeacher>(EudTeacher.class);
        return util.exportExcel(list, "教师管理数据");
    }

    /**
     * 新增教师管理
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        //传递课程的集合
        mmap.put("courses", eduCourseService.selectCourseAll());

        return prefix + "/add";
    }

    /**
     * 新增保存教师管理
     */
    @RequiresPermissions("system:teacher:add")
    @Log(title = "教师管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EudTeacher eudTeacher)
    {
        return toAjax(eudTeacherService.insertEudTeacher(eudTeacher));
    }

    /**
     * 修改教师管理
     */
    @RequiresPermissions("system:teacher:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        EudTeacher eudTeacher = eudTeacherService.selectEudTeacherById(id);
        //传递课程的集合
        List<EduCourse> courses = eduCourseService.selectCoursesByTeaId(id);
        mmap.put("courses", courses);
        mmap.put("eudTeacher", eudTeacher);
        return prefix + "/edit";
    }

    /**
     * 修改保存教师管理
     */
    @RequiresPermissions("system:teacher:edit")
    @Log(title = "教师管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EudTeacher eudTeacher)
    {
        return toAjax(eudTeacherService.updateEudTeacher(eudTeacher));
    }

    /**
     * 删除教师管理
     */
    @RequiresPermissions("system:teacher:remove")
    @Log(title = "教师管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(eudTeacherService.deleteEudTeacherByIds(ids));
    }
}
