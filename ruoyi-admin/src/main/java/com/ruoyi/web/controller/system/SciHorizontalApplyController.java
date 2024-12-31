package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId,String year, SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApply.setYear(year);
        sciHorizontalApply.setUid(getUserId());
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
        sciHorizontalApply.setRole(role);
        sciHorizontalApply.setTableId(tableId);
        List<SciHorizontalApply> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
            switch (tableId){
                case "bootstrap-table0":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVERJYSKYC(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApplyKYC(sciHorizontalApply);
                    break;
            }
        }
//        教研室
        else if(role.equals("research")){
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVERJYSKYC(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByJYS(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApplyJYS(sciHorizontalApply);
                    break;
            }
        }
//        教师
        else{
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApply(sciHorizontalApply);
                    break;
            }
        }


        List<SciHorizontalApply> list1 = new ArrayList<>();
        list1 = sciHorizontalApplyService.selectOtherListByUid(sciHorizontalApply);
        list.addAll(list1);


        // 去重操作
        List<SciHorizontalApply> distinctList = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                SciHorizontalApply::getTopName,
                                Function.identity(),
                                (existing, replacement) -> existing
                        ),
                        map -> new ArrayList<>(map.values())
                ));
        TableDataInfo data= getDataTable(distinctList);

//        TableDataInfo data= getDataTable(list);
        return data;
    }

    /**
     * 导出横向课题列表
     */
    @RequiresPermissions("system:apply:export")
    @Log(title = "导出横向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApply sciHorizontalApply)
    {
        List<SciHorizontalApply> list = sciHorizontalApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
        for (SciHorizontalApply apply: list ) {
            if(apply.getState().equals("4")){
                apply.setState("结项");
            }else{
                apply.setState("在研");
            }
        }
        ExcelUtil<SciHorizontalApply> util = new ExcelUtil<SciHorizontalApply>(SciHorizontalApply.class);
        return util.exportExcel(list, "横向课题数据");
    }

    /**
     * 新增横向课题
     */
    @RequiresPermissions("system:apply:add")
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
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciHorizontalApply sciHorizontalApply)
    {
        return toAjax(sciHorizontalApplyService.insertSciHorizontalApply(sciHorizontalApply));
    }

    /**
     * 结项横向课题
     */
    @RequiresPermissions("system:apply:add")
    @GetMapping("/overadd")
    public String overadd( Integer id,ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/overadd";
    }

    /**
     * 保存结项横向课题
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请结项横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/overadd")
    @ResponseBody
    public AjaxResult overaddSave(SciHorizontalApply sciHorizontalApply)
    {
        String state = sciHorizontalApply.getState();
        String id = String.valueOf(sciHorizontalApply.getId());
//        sciHorizontalApplyService.overApply(id, state);
        return toAjax(sciHorizontalApplyService.updateSciHorizontalOverApply(sciHorizontalApply));
    }


    @RequiresPermissions("system:apply:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/detail";
    }

    @RequiresPermissions("system:apply:info")
    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/overdetail";
    }

    /**已结项查看 */
    @RequiresPermissions("system:apply:info")
    @GetMapping("/overView/{id}/{urlFlag}")
    public String overView(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/overView";
    }


    @RequiresPermissions(value={"system:apply:hecha","system:apply:process"},logical= Logical.OR)
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxPass(id,getUserId(),urlFlag));
    }
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process"},logical= Logical.OR)
    @Log(title = "结项横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxover")
    @ResponseBody
    public AjaxResult hxover(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxover(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply:hecha","system:apply:process"},logical= Logical.OR)
    @Log(title = "横向课题被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciHorizontalApplyService.hxBh(id,getUserId(),remark,urlFlag));
    }
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process"},logical= Logical.OR)
    @Log(title = "结项横向课题被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxoverBh")
    @ResponseBody
    public AjaxResult hxoverBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxoverBh(id,getUserId(),remark,urlFlag));
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
        return prefix + "/edit";
    }

    /**
     * 修改保存横向课题
     */
    @RequiresPermissions("system:apply:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApply.setState("1");
        return toAjax(sciHorizontalApplyService.updateSciHorizontalApply(sciHorizontalApply));
    }

    @RequiresPermissions("system:apply:edit")
    @GetMapping("/overedit/{id}")
    public String overedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        return prefix + "/overedit";
    }

    @RequiresPermissions("system:apply:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/overedit")
    @ResponseBody
    public AjaxResult overeditSave(SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApply.setState("1");
        return toAjax(sciHorizontalApplyService.updateSciHorizontalApply(sciHorizontalApply));
    }

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
    @Log(title = "删除横向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciHorizontalApplyService.deleteSciHorizontalApplyByIds(ids));
    }
}
