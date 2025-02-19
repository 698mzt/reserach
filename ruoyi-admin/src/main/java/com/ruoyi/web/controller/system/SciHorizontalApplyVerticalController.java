package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciHorizontalReamount;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.SciHorizontalReamountService;
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

    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;

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
    @RequiresPermissions("system:apply_vertical:list")
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId,String year ,SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setUid(getUserId());
        sciHorizontalApplyVertical.setYear(year);
        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        label:
        for (SysRole r :roles){
            switch (r.getRoleKey()) {
                case "sci_tesearch":
                    role = "sci_tesearch";
                    break label;
                case "research":
                    role = "research";
                    break label;
                case "dept_teacher":
                    role = "dept_teacher";
                    break label;
            }
        }
        sciHorizontalApplyVertical.setRole(role);
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//        教研室
            case "research":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//      学院负责人
            case "dept_teacher":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//        教师
            default:
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
        }

        TableDataInfo data= getDataTable(list);

        return data;
    }

    /**
     * 导出横向课题列表
     */
    @RequiresPermissions("system:apply_vertical:export")
    @Log(title = "导出纵向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        List<SciHorizontalApplyVertical> list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalAllList(sciHorizontalApplyVertical);
        for (SciHorizontalApplyVertical applyVeryical: list ) {
            if(applyVeryical.getState().equals("4444")){
                applyVeryical.setState("结项");
            }else{
                applyVeryical.setState("在研");
            }
        }
        ExcelUtil<SciHorizontalApplyVertical> util = new ExcelUtil<SciHorizontalApplyVertical>(SciHorizontalApplyVertical.class);
        return util.exportExcel(list, "纵向课题数据");
    }

    /**
     * 新增立项申请课题
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();
        System.out.println(getUserId());
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId().equals(getUserId())){
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
    public AjaxResult addSave(SciHorizontalApplyVertical sciHorizontalApplyVertical,SciHorizontalReamount sciHorizontalReamount)
    {
        Integer id = sciHorizontalApplyVerticalService.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        sciHorizontalReamount.setApplyVertivalId(id.toString());
        return toAjax(sciHorizontalReamountService.insertAmount(sciHorizontalReamount));
    }

    /**
     * 结项
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/overadd")
    public String overadd( Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overadd";
    }
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "结项", businessType = BusinessType.INSERT)
    @PostMapping("/overadd")
    @ResponseBody
    public AjaxResult overaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical,SciHorizontalReamount sciHorizontalReamount)
    {
        sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        return toAjax(sciHorizontalReamountService.insertAmount(sciHorizontalReamount));
    }

    /**
     * 修改申请
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
     *保存修改申请
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

    /**
     * 修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/overedit/{id}")
    public String overedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);

        List<SysUser> userList1 =  userService.selectAllUser();

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overedit";
    }
    /**
     *保存修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/overedit")
    @ResponseBody
    public AjaxResult overeditSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("1111");
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


    //  结项批阅
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overdetail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/overPass")
    @ResponseBody
    public AjaxResult overPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.overPass(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/overBh")
    @ResponseBody
    public AjaxResult overBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.overBh(id,getUserId(),remark,urlFlag));
    }

    /** 查看 */
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/overView/{id}/{urlFlag}")
    public String overView(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag,ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overView";
    }

    /**
     * 添加到账金额
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/Reamount/{id}")
    public String Reamount(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/reamount";
    }
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "添加到账金额", businessType = BusinessType.INSERT)
    @PostMapping("/Reamount")
    @ResponseBody
    public AjaxResult Reamount(SciHorizontalReamount sciHorizontalReamount)
    {
        return toAjax(sciHorizontalReamountService.insertAmount(sciHorizontalReamount));
    }
}
