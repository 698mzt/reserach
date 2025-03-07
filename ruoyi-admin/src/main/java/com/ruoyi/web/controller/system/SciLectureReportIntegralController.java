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
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.service.ISciLectureReportIntegralService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 讲座报告积分管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-16
 */
@Controller
@RequestMapping("/system/integral")
public class SciLectureReportIntegralController extends BaseController
{
    private String prefix = "system/integral";

    @Autowired
    private ISciLectureReportIntegralService sciLectureReportIntegralService;

    @RequiresPermissions("system:integral:view")
    @GetMapping()
    public String integral()
    {
        return prefix + "/integral";
    }

    /**
     * 查询讲座报告积分管理列表
     */
    @RequiresPermissions("system:integral:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciLectureReportIntegral sciLectureReportIntegral)
    {
        startPage();
        List<SciLectureReportIntegral> list = sciLectureReportIntegralService.selectSciLectureReportIntegralList(sciLectureReportIntegral);
        return getDataTable(list);
    }

    /**
     * 导出讲座报告积分管理列表
     */
    @RequiresPermissions("system:integral:export")
    @Log(title = "讲座报告积分管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciLectureReportIntegral sciLectureReportIntegral)
    {
        List<SciLectureReportIntegral> list = sciLectureReportIntegralService.selectSciLectureReportIntegralList(sciLectureReportIntegral);
        ExcelUtil<SciLectureReportIntegral> util = new ExcelUtil<SciLectureReportIntegral>(SciLectureReportIntegral.class);
        return util.exportExcel(list, "讲座报告积分管理数据");
    }

    /**
     * 新增讲座报告积分管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存讲座报告积分管理
     */
    @RequiresPermissions("system:integral:add")
    @Log(title = "讲座报告积分管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciLectureReportIntegral sciLectureReportIntegral)
    {
        return toAjax(sciLectureReportIntegralService.insertSciLectureReportIntegral(sciLectureReportIntegral));
    }

    /**
     * 修改讲座报告积分管理
     */
    @RequiresPermissions("system:integral:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciLectureReportIntegral sciLectureReportIntegral = sciLectureReportIntegralService.selectSciLectureReportIntegralById(id);
        mmap.put("sciLectureReportIntegral", sciLectureReportIntegral);
        return prefix + "/edit";
    }

    /**
     * 修改保存讲座报告积分管理
     */
    @RequiresPermissions("system:integral:edit")
    @Log(title = "讲座报告积分管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciLectureReportIntegral sciLectureReportIntegral)
    {
        return toAjax(sciLectureReportIntegralService.updateSciLectureReportIntegral(sciLectureReportIntegral));
    }

    /**
     * 删除讲座报告积分管理
     */
    @RequiresPermissions("system:integral:remove")
    @Log(title = "讲座报告积分管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciLectureReportIntegralService.deleteSciLectureReportIntegralByIds(ids));
    }
}
