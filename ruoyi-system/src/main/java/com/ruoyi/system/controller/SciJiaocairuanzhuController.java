package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMapper;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciJiaocairuanzhuPiyueService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISciJiaocairuanzhuService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;

/**
 * 教材软著Controller
 *
 * @author ruoyi
 *  2024-11-21
 */
@Controller
@RequestMapping("/system/jiaocairuanzhu")
public class SciJiaocairuanzhuController extends BaseController
{
    private String prefix = "system/jiaocairuanzhu";

    @Autowired
    private ISciJiaocairuanzhuService sciJiaocairuanzhuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciJiaocairuanzhuPiyueService piyueService;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    @Autowired
    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @RequiresPermissions("system:jiaocairuanzhu:view")
    @GetMapping()
    public String jiaocairuanzhu()
    {
        return prefix + "/jiaocairuanzhu";
    }

    /**f
     * 查询教材软著列表
     */

    @RequiresPermissions("system:jiaocairuanzhu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciJiaocairuanzhu sciJiaocairuanzhu,String year)

    {
//        System.out.println("Received tableId: " + tableId);
        System.out.println("Received year: " + year);
        sciJiaocairuanzhu.setYear(year);
        sciJiaocairuanzhu.setUid(getUserId());
        System.out.println("SciJiaocairuanzhu object: " + sciJiaocairuanzhu);


//        startPage();
//        List<SciJiaocairuanzhu> list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);

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
                case "discuss_college":
                case "art_design_college":
                case "cxcy_college":
                case "marxism_college":
                case "dzgc_college":
                    role = "dept_teacher";
                    break label;
                case "admin":
                    role = "admin";
                    break label;
            }
        }
        sciJiaocairuanzhu.setRole(role);

        SysUser user = getSysUser();

        //设置部门id，传输过去用来为查询设置部门限制
        sciJiaocairuanzhu.setDeptId(getSysUser().getDeptId());

//          无用了//设置部门父id，传输过去用来为查询设置部门限制，这个是为查询部门负责人时，查询出部门负责人的部门，并设置查询条件，查询出部门负责人的部门下的所有子部门，
        sciJiaocairuanzhu.setParentId(user.getDept().getParentId());
//        sciJiaocairuanzhu.setParentId(getSysUser().getAncestors());
//        System.out.println(getSysUser());
//        System.out.println(user.getDept().getParentId());




//        System.out.println(sciJiaocairuanzhu);

        List<SciJiaocairuanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList4(sciJiaocairuanzhu);
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList3(sciJiaocairuanzhu);
                break;
//        教研室
            case "research":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList2(sciJiaocairuanzhu);
                break;


            case "admin":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
                break;
            //        教师
            default:
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList1(sciJiaocairuanzhu);
                break;
        }




        return getDataTable(list);
    }

    /**
     * 导出教材软著列表
     */
    @RequiresPermissions("system:jiaocairuanzhu:export")
    @Log(title = "教材软著", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciJiaocairuanzhu sciJiaocairuanzhu)
    {

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
                case "admin":
                    role = "admin";
                    break label;
            }
        }
        sciJiaocairuanzhu.setRole(role);

        //设置部门id，传输过去用来为查询设置部门限制
        sciJiaocairuanzhu.setDeptId(getSysUser().getDeptId());

        SysUser user = getSysUser();
        sciJiaocairuanzhu.setParentId(user.getDept().getParentId());

        List<SciJiaocairuanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);   //导出科研处的下的所有数据
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList31(sciJiaocairuanzhu);  //导出此学院下的所有数据
                break;
//        教研室
            case "research":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList21(sciJiaocairuanzhu);   //导出此教研室下的所有数据
                break;

        }

        // 处理状态显示：状态为6显示"已完结"，其他显示"审批中"
        for (SciJiaocairuanzhu item : list) {
            if (item.getState() != null && "6".equals(item.getState())){
                item.setState("已完结");
            } else {
                item.setState("审批中");
            }
        }

//        List<SciJiaocairuanzhu> list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
        ExcelUtil<SciJiaocairuanzhu> util = new ExcelUtil<SciJiaocairuanzhu>(SciJiaocairuanzhu.class);
        return util.exportExcel(list, "教材软著数据");
    }



    /**
     * 检查专利名称与负责人级别是否重复
     */
    @RequiresPermissions("system:jiaocairuanzhu:add")
    @PostMapping("/checkDuplicate")
    @ResponseBody
    public AjaxResult checkDuplicate(@RequestParam String mingcheng, @RequestParam String paiming) {
        boolean exists = sciJiaocairuanzhuService.checkExist(mingcheng, paiming);
        if (exists) {
            return AjaxResult.error("该专利名称的该负责人级别已存在，不可重复添加");
        } else {
            return AjaxResult.success(); // code == 0
        }
    }

    /**
     * 新增教材软著
     */

    //get请求一般是加载表单页面，而不是处理表单提交
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {

        List<SysUser> userList =  userService.selectAllUser();
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
     * 新增保存教材软著
     */
    @RequiresPermissions("system:jiaocairuanzhu:add")
    @Log(title = "教材软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return toAjax(sciJiaocairuanzhuService.insertSciJiaocairuanzhu(sciJiaocairuanzhu));
    }

    /**
     * 修改教材软著
     */
    @RequiresPermissions("system:jiaocairuanzhu:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);

        // 获取用户列表并添加到模型中
//        List<SysUser> sysUsers = userService.selectUserList(null);
//        mmap.put("sysUsers", sysUsers);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);

        return prefix + "/edit";
    }

    /**
     * 修改保存教材软著
     */
    @RequiresPermissions("system:jiaocairuanzhu:edit")
    @Log(title = "教材软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return toAjax(sciJiaocairuanzhuService.updateSciJiaocairuanzhu(sciJiaocairuanzhu));
    }

    /**
     * 删除教材软著
     */
    @RequiresPermissions("system:jiaocairuanzhu:remove")
    @Log(title = "教材软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciJiaocairuanzhuService.deleteSciJiaocairuanzhuByIds(ids));
    }



//    @RequiresPermissions("system:jiaocairuanzhu:process","system:jiaocairuanzhu:info")

    //批阅
    @RequiresPermissions(value={"system:jiaocairuanzhu:process","system:jiaocairuanzhu:info"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciJiaocairuanzhu.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);
        return prefix + "/detail";
    }





    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue","system:jiaocairuanzhu:info"},logical= Logical.OR)
    @Log(title = "教材软著审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag, String amount, SciProjectScoreCfg sciProjectScoreCfg)
    {

        return toAjax(sciJiaocairuanzhuService.hxPass(id,getUserId(),urlFlag));
    }


    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "教材软著被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciJiaocairuanzhuService.hxBh(id,getUserId(),remark,urlFlag));
    }





    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);
        return prefix + "/recall";
    }


    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciJiaocairuanzhuService.recall(id,state,getUserId(),remark,urlFlag));
    }


    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:edit","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciJiaocairuanzhuPiyue ob = new SciJiaocairuanzhuPiyue();
        ob.setJiaocai_id(kid);
        List<SciJiaocairuanzhuPiyue> list = piyueService.selectSciJiaocairuanzhuPiyueList(ob);
        return getDataTable(list);
    }




}

