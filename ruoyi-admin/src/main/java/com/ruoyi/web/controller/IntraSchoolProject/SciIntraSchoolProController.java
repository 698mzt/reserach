package com.ruoyi.web.controller.IntraSchoolProject;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
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
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

//http://localhost:8081/IntraSchPro
@Controller
@RequestMapping("/IntraSchPro")
public class SciIntraSchoolProController extends BaseController{
    private String prefix="system/IntraSchPro" ;



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
    public TableDataInfo list(@PathVariable("tableId") String tableId, SciHorizontalApply sciHorizontalApply)
    {
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
        List<SciHorizontalApply> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
            switch (tableId){
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciHorizontalApply);
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("list.get(i) = " + list.get(i));
                    }
                    break;
               /* case "bootstrap-table1":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyListByOverApplyKYC(sciHorizontalApply);
                    break;*/
            }
        }
//        教研室
        else if(role.equals("research")){
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciHorizontalApply);
                    break;
               /* case "bootstrap-table1":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyListByJYS(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyListByOverApplyJYS(sciHorizontalApply);
                    break;*/
            }
        }
        else{
            switch (tableId) {
                case "bootstrap-table0":
                    list = sciIntraSchProApplyService.sel_my_IntraSchPro_isOVER(sciHorizontalApply);
                    break;
                /*case "bootstrap-table1":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    list = sciIntraSchProApplyService.selectSciHorizontalApplyListByOverApply(sciHorizontalApply);
                    break;*/
            }
        }
        TableDataInfo data= getDataTable(list);

        return data;
    }


}
