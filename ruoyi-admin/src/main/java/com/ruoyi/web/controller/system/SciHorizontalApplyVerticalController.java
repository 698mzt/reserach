package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* 纵向课题Controller
* */
@Controller
@RequestMapping("/system/apply_vertical")
public class SciHorizontalApplyVerticalController extends BaseController {
    private String prefix = "system/apply_vertical";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciHorizontalApplyVerticalService sciHorizontalApplyVerticalService;

//    访问的首页
    @RequiresPermissions("system:apply_vertical:view")
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    /**
     * 查询横向课题列表
     */
//    todo: 未完成
    @RequiresPermissions("system:apply:list")
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId, SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setUid(getUserId());
        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        for (SysRole r :roles){
            if(r.getRoleKey().equals("sci_tesearch")){
                role ="sci_tesearch";
                break;
            }else if (r.getRoleKey().equals("research")){
                role="research";
                break;
            }
        }
        sciHorizontalApplyVertical.setRole(role);
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
        System.out.println(list);
        TableDataInfo data= getDataTable(list);

        return data;
    }

    /**
     * 新增立项申请课题
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
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
     * 保存新增立项申请
     */
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "立项申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        return toAjax(sciHorizontalApplyVerticalService.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 修改立项申请
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);

        List<SysUser> userList1 =  userService.selectAllUser();

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/edit";
    }

    /**
     *保存修改立项申请
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("1");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }


    /**  删除纵向课题*/
    @RequiresPermissions("system:apply_vertical:remove")
    @Log(title = "删除纵向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciHorizontalApplyVerticalService.deleteSciHorizontalApplyVerticalByIds(ids));
    }

//  todo:审批
    /**  查询要审批的纵向课题*/
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/detail";
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/applyPass")
    @ResponseBody
    public AjaxResult applyPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.applyPass(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/applyBh")
    @ResponseBody
    public AjaxResult applyBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.applyBh(id,getUserId(),remark,urlFlag));
    }

}
