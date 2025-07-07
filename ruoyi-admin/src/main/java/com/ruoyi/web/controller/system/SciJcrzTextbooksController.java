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
import com.ruoyi.system.domain.SciJcrzTextbooks;
import com.ruoyi.system.service.ISciJcrzTextbooksService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教材软著Controller
 *
 * @author zwh
 * @date 2024-11-14
 */
@Controller
@RequestMapping("/system/textbooks")
public class SciJcrzTextbooksController extends BaseController
{
    private String prefix = "system/textbooks";

    @Autowired
    private ISciJcrzTextbooksService sciJcrzTextbooksService;

    @RequiresPermissions("system:textbooks:view")
    @GetMapping()
    public String textbooks()
    {
        return prefix + "/textbooks";
    }

    /**
     * 查询教材软著列表
     */
    @RequiresPermissions("system:textbooks:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciJcrzTextbooks sciJcrzTextbooks)
    {
        startPage();
        List<SciJcrzTextbooks> list = sciJcrzTextbooksService.selectSciJcrzTextbooksList(sciJcrzTextbooks);
        return getDataTable(list);
    }

    /**
     * 导出教材软著列表
     */
    @RequiresPermissions("system:textbooks:export")
    @Log(title = "教材软著", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciJcrzTextbooks sciJcrzTextbooks)
    {
        List<SciJcrzTextbooks> list = sciJcrzTextbooksService.selectSciJcrzTextbooksList(sciJcrzTextbooks);
        ExcelUtil<SciJcrzTextbooks> util = new ExcelUtil<SciJcrzTextbooks>(SciJcrzTextbooks.class);
        return util.exportExcel(list, "教材软著数据");
    }

    /**
     * 新增教材软著
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存教材软著
     */
    @RequiresPermissions("system:textbooks:add")
    @Log(title = "教材软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciJcrzTextbooks sciJcrzTextbooks)
    {
        return toAjax(sciJcrzTextbooksService.insertSciJcrzTextbooks(sciJcrzTextbooks));
    }

    /**
     * 修改教材软著
     */
    @RequiresPermissions("system:textbooks:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SciJcrzTextbooks sciJcrzTextbooks = sciJcrzTextbooksService.selectSciJcrzTextbooksById(id);
        mmap.put("sciJcrzTextbooks", sciJcrzTextbooks);
        return prefix + "/edit";
    }

    /**
     * 修改保存教材软著
     */
    @RequiresPermissions("system:textbooks:edit")
    @Log(title = "教材软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciJcrzTextbooks sciJcrzTextbooks)
    {
        return toAjax(sciJcrzTextbooksService.updateSciJcrzTextbooks(sciJcrzTextbooks));
    }

    /**
     * 删除教材软著
     */
    @RequiresPermissions("system:textbooks:remove")
    @Log(title = "教材软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciJcrzTextbooksService.deleteSciJcrzTextbooksByIds(ids));
    }
}
