package com.ruoyi.web.controller.IntraSchoolProject;
import java.util.List;

import com.ruoyi.common.annotation.Anonymous;
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
import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.service.IAlltotleService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AlltotleController
 *
 * @author ruoyi
 * @date 2025-08-26
 */

@Controller
@RequestMapping("/system/Alltotle")
public class AlltotleController extends BaseController
{
  private String prefix = "system/Alltotle";

  @Autowired
  private IAlltotleService alltotleService;

//  @RequiresPermissions("system:Alltotle:view")
//  @GetMapping()
//  public String Alltotle()
//  {
//    return prefix + "/Alltotle";
//  }

  /**
   * 查询Alltotle列表
   */
  @RequiresPermissions("system:Alltotle:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(Alltotle alltotle)
  {
    startPage();
    List<Alltotle> list = alltotleService.selectAlltotleList(alltotle);
    return getDataTable(list);
  }

  /**
   * 导出Alltotle列表
   */
//  @RequiresPermissions("system:Alltotle:export")
//  @Log(title = "Alltotle", businessType = BusinessType.EXPORT)
//  @PostMapping("/export")
//  @ResponseBody
//  public AjaxResult export(Alltotle alltotle)
//  {
//    List<Alltotle> list = alltotleService.selectAlltotleList(alltotle);
//    ExcelUtil<Alltotle> util = new ExcelUtil<Alltotle>(Alltotle.class);
//    return util.exportExcel(list, "Alltotle数据");
//  }

  /**
   * 新增Alltotle
   */
//  @GetMapping("/add")
//  public String add()
//  {
//    return prefix + "/add";
//  }

  /**
   * 新增保存Alltotle
   */
  @RequiresPermissions("system:Alltotle:add")
  @Log(title = "Alltotle", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(Alltotle alltotle)
  {
    return toAjax(alltotleService.insertAlltotle(alltotle));
  }

  /**
   * 修改Alltotle
   */
//  @RequiresPermissions("system:Alltotle:edit")
//  @GetMapping("/edit/{userId}")
//  public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
//  {
//    Alltotle alltotle = alltotleService.selectAlltotleByUserId(userId);
//    mmap.put("alltotle", alltotle);
//    return prefix + "/edit";
//  }

  /**
   * 修改保存Alltotle
   */
  @RequiresPermissions("system:Alltotle:edit")
  @Log(title = "Alltotle", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(Alltotle alltotle)
  {
    return toAjax(alltotleService.updateAlltotle(alltotle));
  }

  /**
   * 删除Alltotle
   */
  @RequiresPermissions("system:Alltotle:remove")
  @Log(title = "Alltotle", businessType = BusinessType.DELETE)
  @PostMapping( "/remove")
  @ResponseBody
  public AjaxResult remove(String ids)
  {
    return toAjax(alltotleService.deleteAlltotleByUserIds(ids));
  }

  //@RequiresPermissions("system:Alltotle:edit")
  @Log(title = "Alltotle", businessType = BusinessType.UPDATE)
  @PostMapping("/synchronousAlltotleRes")
  @ResponseBody
  @Anonymous
  public AjaxResult synchronousAlltotleRes()
  {
    return toAjax(synchronousAlltotle());
  }
  // 同步修改
  public int synchronousAlltotle (){
    return alltotleService.synchronousAlltotle();
  }

}
