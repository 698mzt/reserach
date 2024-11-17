package com.ruoyi.web.controller.IntraSchoolProject;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciIntraSchProPiyue;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import com.ruoyi.system.service.ISciIntraSchProPiyueService;
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
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;

//http://localhost:8081/IntraSchPro
@Controller
@RequestMapping("/IntraSchPro")
public class SciIntraSchoolProController extends BaseController {
    private String prefix = "system/IntraSchPro";


    @Autowired
    private ISciIntraSchProApplyService sciIntraSchProApplyService;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISciIntraSchProPiyueService piyueService;

    @GetMapping("")
    String view() {
        return prefix + "/view";
    }

    /**
     * 查询横向课题列表
     */


    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId, SciIntraSchoolPro sciIntraSchoolPro) {
        sciIntraSchoolPro.setUid(getUserId());
        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        for (SysRole r : roles) {
            if (r.getRoleKey().equals("sci_tesearch")) {
                role = "sci_tesearch";
                break;
            } else if (r.getRoleKey().equals("research")) {
                role = "research";
                break;
            }
        }
        List<SciIntraSchoolPro> list = new ArrayList<>();
//        科研处
        if (role.equals("sci_tesearch")) {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
                    System.out.println("list = " + list);
                    break;
                case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_ky(sciIntraSchoolPro);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_ky(sciIntraSchoolPro);
                    break;
            }
        }
//        教研室
        else if (role.equals("research")) {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
                    System.out.println("list = " + list);
                    break;
                case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_jy(sciIntraSchoolPro);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_jy(sciIntraSchoolPro);
                    break;
            }
        } else {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_my_IntraSchPro_isOVER(sciIntraSchoolPro);
                    System.out.println("list = " + list);
                    break;
               case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_my(sciIntraSchoolPro);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_my(sciIntraSchoolPro);
                    break;
            }
        }
        TableDataInfo data = getDataTable(list);
        System.out.println("data = " + data);
        return data;
    }

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

    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciIntraSchoolPro sciIntraSchoolPro)
    {
        return toAjax(sciIntraSchProApplyService.insert_SchPro_Apply(sciIntraSchoolPro));
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        System.out.println("id = " + id);
        SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);

        List<SysUser> userList1 =  userService.selectAllUser();
       
        mmap.put("sysUsers1",userList1);
        mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
        System.out.println("SciIntraSchoolProController.edit");
        if (sciIntraSchoolPro.getState().equals("3")||sciIntraSchoolPro.getState().equals("5")){
            System.out.println("1");
            return prefix + "/edit";
        }else if(sciIntraSchoolPro.getState().equals("6")){
            System.out.println("1");
            return prefix + "/is_Over";
        } else {
            System.out.println("1");
            return prefix + "/edit_Over";
        }
        //return prefix + "/edit";
    }

    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {

        SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciIntraSchoolPro.setUrlFlag(urlFlag);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
        //mmap.put("urlFlag",urlFlag);
        System.out.println("SciIntraSchoolProController.detail");
        return prefix + "/detail";
    }

    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciIntraSchoolPro.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
        //mmap.put("urlFlag",urlFlag);
        return prefix + "/overdetail";
    }

    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciIntraSchProPiyue ob = new SciIntraSchProPiyue();
        ob.setSchxktId(kid);
        List<SciIntraSchProPiyue> list = piyueService.selectIntraSchProPiyueList(ob);
        return getDataTable(list);
    }

    @PostMapping( "/sch_hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciIntraSchProApplyService.sch_hxBh(id,getUserId(),remark,urlFlag));
    }

    @PostMapping( "/sch_hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id,String urlFlag)
    {
        return toAjax(sciIntraSchProApplyService.sch_hxPass(id,getUserId(),urlFlag));
    }

    /**
     * 更新校内横向课题
     * @param
     * @return
     */
    @PostMapping("/sc_edit")
    @ResponseBody
    public AjaxResult sc_editSave(SciIntraSchoolPro sciIntraSchoolPro)
    {
        sciIntraSchoolPro.setState("1");
        return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro));
    }

    @PostMapping("/edit_Over")
    @ResponseBody
    //todo:这里的sciHorizontalApply里面getState()是个null
    public AjaxResult editSave_Over(SciIntraSchoolPro sciIntraSchoolPro)
    {
        sciIntraSchoolPro.setState("7");
        return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro));
    }

    /**
     * 结项校内横向课题
     */
    @GetMapping("/overadd")
    public String overadd( Integer id,ModelMap mmap)
    {
        SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
        return prefix + "/overadd";
    }

    @PostMapping("/sch_overadd")
    @ResponseBody
    public AjaxResult sch_overaddSave(SciIntraSchoolPro sciIntraSchoolPro)
    {
        String state = sciIntraSchoolPro.getState();
        String id = String.valueOf(sciIntraSchoolPro.getId());
        sciIntraSchProApplyService.overApply(id, state);
        return toAjax(sciIntraSchProApplyService.insert_IntraSchPro_OverApply(sciIntraSchoolPro));
    }

    @PostMapping( "/sch_hxover")
    @ResponseBody
    public AjaxResult hxover(String id,String urlFlag)
    {
        return toAjax(sciIntraSchProApplyService.sch_hxover(id,getUserId(),urlFlag));
    }

    @PostMapping( "/sch_hxoverBh")
    @ResponseBody
    public AjaxResult hxoverBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciIntraSchProApplyService.sch_hxoverBh(id,getUserId(),remark,urlFlag));
    }



}
