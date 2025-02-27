package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciPaperAr;
import com.ruoyi.system.service.*;
import org.apache.shiro.authz.annotation.Logical;
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
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 论文Controller
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
@Controller
@RequestMapping("/system/paper")
public class SciPaperAController extends BaseController
{
    private String prefix = "system/paper";

    @Autowired
    private ISciPaperAService sciPaperAService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:paper:view")
    @GetMapping()
    public String paper()
    {
        return prefix + "/paper";
    }

    /**
     * 查询论文列表
     */
    @RequiresPermissions("system:paper:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciPaperA sciPaperA,String year)
    {

        Long userId = getUserId();
        System.out.println("userId = " + userId);
        List<String> roleId = sciPaperAService.selectSciPaperAByroleId(userId);
        System.out.println("roleId = " + roleId);

        sciPaperA.setUid(userId);

        List<SciPaperA> list= new ArrayList<>();
            //教研室
            if (roleId.contains("102")) {
                list.addAll(sciPaperAService.selectSciPaperAList(sciPaperA));
            }
            //科研处
            if (roleId.contains("102")) {
                list.addAll(sciPaperAService.selectSciPaperAListKY(sciPaperA));
            }
            //学院
            if (roleId.contains("103")) {
                list.addAll(sciPaperAService.selectSciPaperAListXY(sciPaperA));
            }
            if (roleId.contains("100") && roleId.size()==1) {
                list.addAll(sciPaperAService.selectSciPaperAListCx(sciPaperA));
            }


        sciPaperA.setYear(year);
        startPage();

        return getDataTable(list);
    }

    /**
     * 导出论文列表
     */
    @RequiresPermissions("system:paper:export")
    @Log(title = "论文", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciPaperA sciPaperA)
    {
        List<SciPaperA> list = sciPaperAService.selectSciPaperAList(sciPaperA);
        ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
        return util.exportExcel(list, "论文数据");
    }

    /**
     * 新增论文
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();
        SysUser sysUser=null;
        Long user_id = getUserId();
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId() == user_id){
                sysUser=userList.get(a);
                break;
            }
        }
        mmap.put("user",sysUser);
        return prefix + "/add";
    }

    /**
     * 新增保存论文
     */
    @RequiresPermissions("system:paper:add")
    @Log(title = "论文", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciPaperA sciPaperA)
    {
        System.out.println("sciPaperA = " + sciPaperA);
        Long userId = getUserId();
        sciPaperA.setUserId(userId);
        System.out.println("userId = " + sciPaperA.getUserId());

        String user_name = userService.selectUserByLoginName(getLoginName()).getUserName();
        sciPaperA.setTeacherName(user_name);

        return toAjax(sciPaperAService.insertSciPaperA(sciPaperA));
    }

    /**
     * 修改论文
     */
    @RequiresPermissions("system:paper:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        mmap.put("sysUsers",currentUser);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/edit";
    }
    /**
     * 批阅
     * */
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy","system:paper:info"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Long id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        sciPaperA.setUrlFlag(urlFlag);
        System.out.println("sciPaperA = " + sciPaperA);

//        List<SysUser> userList =  userService.selectAllUser();
//        mmap.put("sysUsers",userList);

        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/detail";
    }

    /**
     * 修改保存论文
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciPaperA sciPaperA)
    {
        sciPaperA.setState("1");
        return toAjax(sciPaperAService.updateSciPaperA(sciPaperA));
    }

    /**
     * 删除论文
     */
    @RequiresPermissions("system:paper:remove")
    @Log(title = "论文", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciPaperAService.deleteSciPaperAByIds(ids));
    }

    /**
     * 论文通过
     */
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy"},logical= Logical.OR)
    @Log(title = "论文审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/pytg/{id}")
    @ResponseBody
    public AjaxResult pytg(@PathVariable("id") String id,String urlFlag,String paperCategory,String paperRanking) {
        String order = paperCategory;
        String user_order = paperRanking;
        return toAjax(sciPaperAService.pytg(id, getUserId(), urlFlag,order,user_order));
    }
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy","system:paper:xyrevoke","system:paper:kyrevoke"},logical= Logical.OR)
    @Log(title = "论文审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/pybh/{id}")
    @ResponseBody
    public AjaxResult pybh(@PathVariable("id")String id,String remark,String urlFlag)
    {
        System.out.println("remark = " + remark);
        return toAjax(sciPaperAService.pybh(id,getUserId(),remark,urlFlag));
    }


    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhxs/{kid}")
    @ResponseBody
    public TableDataInfo bhxs(@PathVariable("kid")Integer arid)
    {
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setAr_id(arid);
        List<SciPaperAr> list = sciPaperAService.selectSciPaperArList(sciPaperAr);
        return getDataTable(list);
    }

}
