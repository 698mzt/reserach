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
//        教研室
        if (role.equals("research")){
            switch (tableId){
//                已结项
                case "bootstrap-table0":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                    break;
//                申请
                case "bootstrap-table1":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                    break;
//                结项
                case "bootstrap-table2":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                    break;
//                中期
                case "bootstrap-table3":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListZQ(sciHorizontalApplyVertical);
                    break;
//                开题
                case "bootstrap-table4":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListKT(sciHorizontalApplyVertical);
                    break;
            }
        }else if (role.equals("sci_tesearch")){
            switch (tableId){
//                已结项
                case "bootstrap-table0":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                    break;
//                申请
                case "bootstrap-table1":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                    break;
//                结项
                case "bootstrap-table2":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                    break;
//                中期
                case "bootstrap-table3":
                   list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListZQ(sciHorizontalApplyVertical);
                    break;
//                开题
                case "bootstrap-table4":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListKT(sciHorizontalApplyVertical);
                    break;
            }
        }else{
            switch (tableId){
//                已结项
                case "bootstrap-table0":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                    break;
//                申请
                case "bootstrap-table1":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                    break;
//                结项
                case "bootstrap-table2":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                    break;
//                中期
                case "bootstrap-table3":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListZQ(sciHorizontalApplyVertical);
                    break;
//                开题
               case "bootstrap-table4":
                    list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListKT(sciHorizontalApplyVertical);
                    break;
            }
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
     * 开题
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/openadd")
    public String openadd( Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/openadd";
    }
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "开题", businessType = BusinessType.INSERT)
    @PostMapping("/openadd")
    @ResponseBody
    public AjaxResult openaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("11");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 中期
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/midadd")
    public String midadd( Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/midadd";
    }
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "中期", businessType = BusinessType.INSERT)
    @PostMapping("/midadd")
    @ResponseBody
    public AjaxResult midaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("111");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
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
    @Log(title = "中期", businessType = BusinessType.INSERT)
    @PostMapping("/overadd")
    @ResponseBody
    public AjaxResult overaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("1111");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
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
     * 修改开题
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/openedit/{id}")
    public String openedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);

        List<SysUser> userList1 =  userService.selectAllUser();

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/openedit";
    }

    /**
     *保存修改开题
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/openedit")
    @ResponseBody
    public AjaxResult openeditSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("11");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 修改中期
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/midedit/{id}")
    public String midedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);

        List<SysUser> userList1 =  userService.selectAllUser();

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/midedit";
    }

    /**
     *保存修改中期
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/midedit")
    @ResponseBody
    public AjaxResult mideditSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("111");
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

//    开题批阅
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/opendetail/{id}/{urlFlag}")
    public String opendetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/opendetail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/openPass")
    @ResponseBody
    public AjaxResult openPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.openPass(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/openBh")
    @ResponseBody
    public AjaxResult openBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.openBh(id,getUserId(),remark,urlFlag));
    }

//    中期批阅
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/middetail/{id}/{urlFlag}")
    public String middetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/middetail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/midPass")
    @ResponseBody
    public AjaxResult midPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.midPass(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/midBh")
    @ResponseBody
    public AjaxResult midBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.midBh(id,getUserId(),remark,urlFlag));
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



}
