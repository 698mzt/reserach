package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciZhuanliruanzhuMapper;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciZhuanliruanzhuPiyueService;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;

/**
 * 专利软著Controller
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
@Controller
@RequestMapping("/system/zhuanliruanzhu")
public class SciZhuanliruanzhuController extends BaseController
{
    private String prefix = "system/zhuanliruanzhu";

    @Autowired
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciZhuanliruanzhuPiyueService piyueService;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    @Autowired
    private SciZhuanliruanzhuMapper sciZhuanliruanzhuMapper;

    @RequiresPermissions("system:zhuanliruanzhu:view")
    @GetMapping()
    public String zhuanliruanzhu()
    {
        return prefix + "/zhuanliruanzhu";
    }

    /**
     * 查询专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhu sciZhuanliruanzhu,String year)
    {
        sciZhuanliruanzhu.setYear(year);
        sciZhuanliruanzhu.setUid(getUserId());


//        startPage();
//        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);

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
        sciZhuanliruanzhu.setRole(role);

        SysUser user = getSysUser();

        //设置部门id，传输过去用来为查询设置部门限制
        sciZhuanliruanzhu.setDeptId(getSysUser().getDeptId());

//          无用了//设置部门父id，传输过去用来为查询设置部门限制，这个是为查询部门负责人时，查询出部门负责人的部门，并设置查询条件，查询出部门负责人的部门下的所有子部门，
//        sciZhuanliruanzhu.setParentId(user.getDept().getParentId());
//        sciZhuanliruanzhu.setParentId(getSysUser().getAncestors());
        System.out.println(getSysUser());
        System.out.println(user.getDept().getParentId());




        System.out.println(sciZhuanliruanzhu);

        List<SciZhuanliruanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList4(sciZhuanliruanzhu);
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList3(sciZhuanliruanzhu);
                break;
//        教研室
            case "research":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList2(sciZhuanliruanzhu);
                break;

//        教师
            default:
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
                break;
        }




        return getDataTable(list);
    }

    /**
     * 导出专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:export")
    @Log(title = "专利软著", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
        ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
        return util.exportExcel(list, "专利软著数据");
    }

    /**
     * 新增专利软著
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
     * 新增保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:add")
    @Log(title = "专利软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.insertSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 修改专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);

        // 获取用户列表并添加到模型中
//        List<SysUser> sysUsers = userService.selectUserList(null);
//        mmap.put("sysUsers", sysUsers);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);

        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @Log(title = "专利软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.updateSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 删除专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:remove")
    @Log(title = "专利软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciZhuanliruanzhuService.deleteSciZhuanliruanzhuByIds(ids));
    }



//    @RequiresPermissions("system:zhuanliruanzhu:process","system:zhuanliruanzhu:info")
    @RequiresPermissions(value={"system:zhuanliruanzhu:process","system:zhuanliruanzhu:info"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciZhuanliruanzhu.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/detail";
    }





    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "专利软著审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag, String amount, SciProjectScoreCfg sciProjectScoreCfg)
    {

        return toAjax(sciZhuanliruanzhuService.hxPass(id,getUserId(),urlFlag));
    }


    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "专利软著被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciZhuanliruanzhuService.hxBh(id,getUserId(),remark,urlFlag));
    }





    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/recall";
    }


    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciZhuanliruanzhuService.recall(id,state,getUserId(),remark,urlFlag));
    }


    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:edit","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciZhuanliruanzhuPiyue ob = new SciZhuanliruanzhuPiyue();
        ob.setHxktId(kid);
        List<SciZhuanliruanzhuPiyue> list = piyueService.selectSciZhuanliruanzhuPiyueList(ob);
        return getDataTable(list);
    }
}

