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
import com.ruoyi.system.domain.AlltotleScore;
import com.ruoyi.system.service.IAlltotleScoreService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 统计积分Controller
 *
 * @author ruoyi
 * @date 2025-09-03
 */
@Controller
@RequestMapping("/system/score")
public class AlltotleScoreController extends BaseController
{
  private String prefix = "system/score";

  @Autowired
  private IAlltotleScoreService alltotleScoreService;

//  @RequiresPermissions("system:score:view")
//  @GetMapping()
//  public String score()
//  {
//    return prefix + "/score";
//  }

  /**
   * 查询统计积分列表
   */
  @RequiresPermissions("system:score:list")
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(AlltotleScore alltotleScore)
  {
    startPage();
    List<AlltotleScore> list = alltotleScoreService.selectAlltotleScoreList(alltotleScore);
    return getDataTable(list);
  }

  /**
   * 导出统计积分列表
   */
  @RequiresPermissions("system:score:export")
  @Log(title = "统计积分", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(AlltotleScore alltotleScore)
  {
    List<AlltotleScore> list = alltotleScoreService.selectAlltotleScoreList(alltotleScore);
    ExcelUtil<AlltotleScore> util = new ExcelUtil<AlltotleScore>(AlltotleScore.class);
    return util.exportExcel(list, "统计积分数据");
  }

  /**
   * 新增统计积分
   */
//  @GetMapping("/add")
//  public String add()
//  {
//    return prefix + "/add";
//  }

  /**
   * 新增保存统计积分
   */
  @RequiresPermissions("system:score:add")
  @Log(title = "统计积分", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(AlltotleScore alltotleScore)
  {
    return toAjax(alltotleScoreService.insertAlltotleScore(alltotleScore));
  }

  /**
   * 修改统计积分
   */
//  @RequiresPermissions("system:score:edit")
//  @GetMapping("/edit/{userId}")
//  public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
//  {
//    AlltotleScore alltotleScore = alltotleScoreService.selectAlltotleScoreByUserId(userId);
//    mmap.put("alltotleScore", alltotleScore);
//    return prefix + "/edit";
//  }

  /**
   * 修改保存统计积分
   */
  @RequiresPermissions("system:score:edit")
  @Log(title = "统计积分", businessType = BusinessType.UPDATE)
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(AlltotleScore alltotleScore)
  {
    return toAjax(alltotleScoreService.updateAlltotleScore(alltotleScore));
  }

  /**
   * 删除统计积分
   */
  @RequiresPermissions("system:score:remove")
  @Log(title = "统计积分", businessType = BusinessType.DELETE)
  @PostMapping( "/remove")
  @ResponseBody
  public AjaxResult remove(String ids)
  {
    return toAjax(alltotleScoreService.deleteAlltotleScoreByUserIds(ids));
  }


  /**
   * 同步积分
   */
  @Log(title = "Alltotle", businessType = BusinessType.UPDATE)
  @PostMapping("/synchronousAlltotleScoreRes")
  @ResponseBody
  @Anonymous
  public AjaxResult synchronousAlltotleScoreRes()
  {
    return toAjax(synchronousAlltotleScore());
  }

  public int synchronousAlltotleScore(){
    return alltotleScoreService.synchronousAlltotleScore();
  }
}
