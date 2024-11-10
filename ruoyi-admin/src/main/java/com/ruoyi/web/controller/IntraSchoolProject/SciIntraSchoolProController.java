package com.ruoyi.web.controller.IntraSchoolProject;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.system.domain.SciHorizontalApply;
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
    private ISciHorizontalPiyueService piyueService;

    @GetMapping("")
    String view() {
        return prefix + "/view";
    }

    /**
     * 查询横向课题列表
     */


    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId, SciHorizontalApply sciHorizontalApply) {
        sciHorizontalApply.setUid(getUserId());
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
        List<SciHorizontalApply> list = new ArrayList<>();
//        科研处
        if (role.equals("sci_tesearch")) {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciHorizontalApply);
                    System.out.println("list = " + list);
                    break;
                case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_ky(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_ky(sciHorizontalApply);
                    break;
            }
        }
//        教研室
        else if (role.equals("research")) {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciHorizontalApply);
                    System.out.println("list = " + list);
                    break;
                case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_jy(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_jy(sciHorizontalApply);
                    break;
            }
        } else {
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_my_IntraSchPro_isOVER(sciHorizontalApply);
                    System.out.println("list = " + list);
                    break;
               case "bootstrap-table1":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_approval_my(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_closure_my(sciHorizontalApply);
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


}
