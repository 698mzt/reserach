package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 横向课题Controller
 * 
 * @author zhansan
 * @date 2024-08-16
 */
@Controller
@RequestMapping("/system/apply")
public class SciHorizontalApplyController extends BaseController
{
    private String prefix = "system/apply";

    @Autowired
    private ISciHorizontalApplyService sciHorizontalApplyService;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISciHorizontalPiyueService piyueService;

    @RequiresPermissions("system:apply:view")
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    /**
     * 查询横向课题列表
     */
    @RequiresPermissions("system:apply:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciHorizontalApply sciHorizontalApply)
    {
        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        boolean falg = false;
        for (SysRole r :roles){
            if(r.getRoleKey().equals("sci_tesearch")){
                falg =true;
                break;
            }
        }
        List<SciHorizontalApply> list = new ArrayList<>();
        if(falg){
            list = sciHorizontalApplyService.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
        }else{
            list = sciHorizontalApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
        }


        return getDataTable(list);
    }

    /**
     * 导出横向课题列表
     */
    @RequiresPermissions("system:apply:export")
    @Log(title = "横向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApply sciHorizontalApply)
    {
        List<SciHorizontalApply> list = sciHorizontalApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
        ExcelUtil<SciHorizontalApply> util = new ExcelUtil<SciHorizontalApply>(SciHorizontalApply.class);
        return util.exportExcel(list, "横向课题数据");
    }

    /**
     * 新增横向课题
     */
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
       // SysUser user=getSysUser();
       List<SysUser> userList =  userService.selectAllUser();
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId() == getUserId()){
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a,user);
                break;
            }
        }
        mmap.put("sysUsers",userList);
        return prefix + "/add";
    }

    /**
     * 新增保存横向课题
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciHorizontalApply sciHorizontalApply)
    {
        return toAjax(sciHorizontalApplyService.insertSciHorizontalApply(sciHorizontalApply));
    }

    @RequiresPermissions("system:apply:info")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/detail";
    }


    @RequiresPermissions("system:apply:process")
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id)
    {
        return toAjax(sciHorizontalApplyService.hxPass(id,getUserId()));
    }

    @RequiresPermissions("system:apply:process")
    @Log(title = "横向课题被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark)
    {

        return toAjax(sciHorizontalApplyService.hxBh(id,getUserId(),remark));
    }
    /**
     * 修改横向课题
     */
    @RequiresPermissions("system:apply:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);

        List<SysUser> userList1 =  userService.selectAllUser();

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
//        for (int a = 0; a<userList.size();a++) {
//            if(userList.get(a).getUserId() ==Long.valueOf(sciHorizontalApply.getFirstPersonId()) ){
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a,user);
//                mmap.put("sysUsers3",userList);
//                user.setFlag(false);
//                userList.set(a,user);
//                break;
//            }
//        }

//        for (int a = 0; a<userList.size();a++) {
//            if(userList.get(a).getUserId() ==Long.valueOf(sciHorizontalApply.getSecondPersonId()) ){
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a,user);
//                mmap.put("sysUsers4",userList);
//                user.setFlag(false);
//                userList.set(a,user);
//                break;
//            }
//        }

//        for (int a = 0; a<userList.size();a++) {
//            if(userList.get(a).getUserId() ==Long.valueOf(sciHorizontalApply.getThirdPersonId()) ){
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a,user);
//                mmap.put("sysUsers5",userList);
//                user.setFlag(false);
//                userList.set(a,user);
//                break;
//            }
//        }
//
//        for (int a = 0; a<userList.size();a++) {
//            if(userList.get(a).getUserId() ==Long.valueOf(sciHorizontalApply.getFourthPersonId()) ){
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a,user);
//                mmap.put("sysUsers6",userList);
//                user.setFlag(false);
//                userList.set(a,user);
//
//                break;
//            }
//        }
        return prefix + "/edit";
    }

    /**
     * 修改保存横向课题
     */
    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciHorizontalPiyue ob = new SciHorizontalPiyue();
        ob.setHxktId(kid);
        List<SciHorizontalPiyue> list = piyueService.selectSciHorizontalPiyueList(ob);
        return getDataTable(list);
    }

    /**
     * 删除横向课题
     */
    @RequiresPermissions("system:apply:remove")
    @Log(title = "横向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciHorizontalApplyService.deleteSciHorizontalApplyByIds(ids));
    }
}
