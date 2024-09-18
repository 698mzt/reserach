package com.ruoyi.web.controller.system;

import java.util.List;
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
import com.ruoyi.system.domain.EduCourse;
import com.ruoyi.system.service.IEduCourseService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 科目管理Controller
 * 
 * @author 张聪
 * @date 2024-08-14
 */
@Controller
@RequestMapping("/system/course")
public class EduCourseController extends BaseController
{
    private String prefix = "system/course";

    @Autowired
    private IEduCourseService eduCourseService;

    @RequiresPermissions("system:course:view")
    @GetMapping()
    public String course()
    {
        return prefix + "/course";
    }

    /**
     * 查询科目管理列表
     */
    @RequiresPermissions("system:course:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EduCourse eduCourse)
    {

        startPage();
        List<EduCourse> list = eduCourseService.selectEduCourseList(eduCourse);
        return getDataTable(list);
    }

    /**
     * 导出科目管理列表
     */
    @RequiresPermissions("system:course:export")
    @Log(title = "科目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EduCourse eduCourse)
    {
        List<EduCourse> list = eduCourseService.selectEduCourseList(eduCourse);
        ExcelUtil<EduCourse> util = new ExcelUtil<EduCourse>(EduCourse.class);
        return util.exportExcel(list, "科目管理数据");
    }

    /**
     * 新增科目管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存科目管理
     */
    @RequiresPermissions("system:course:add")
    @Log(title = "科目管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EduCourse eduCourse)
    {
        return toAjax(eduCourseService.insertEduCourse(eduCourse));
    }

    /**
     * 修改科目管理
     */
    @RequiresPermissions("system:course:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        EduCourse eduCourse = eduCourseService.selectEduCourseById(id);
        mmap.put("eduCourse", eduCourse);
        return prefix + "/edit";
    }

    /**
     * 修改保存科目管理
     */
    @RequiresPermissions("system:course:edit")
    @Log(title = "科目管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EduCourse eduCourse)
    {
        return toAjax(eduCourseService.updateEduCourse(eduCourse));
    }

    /**
     * 删除科目管理
     */
    @RequiresPermissions("system:course:remove")
    @Log(title = "科目管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(eduCourseService.deleteEduCourseByIds(ids));
    }
}
