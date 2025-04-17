package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.domain.SciLectureReportOpinion;
import com.ruoyi.system.service.ISciLectureReportIntegralService;
import com.ruoyi.system.service.ISciLectureReportOpinionService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.models.auth.In;
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
import com.ruoyi.system.domain.SciLectureReport;
import com.ruoyi.system.service.ISciLectureReportService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 讲座报告Controller
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
@Controller
@RequestMapping("/system/report")
public class SciLectureReportController extends BaseController
{
    private String prefix = "system/report";

    // 讲座报告Service接口
    @Autowired
    private ISciLectureReportService sciLectureReportService;
    // 用户Service接口
    @Autowired
    private ISysUserService userService;
    // 讲座报告审核意见的Service接口
    @Autowired
    private ISciLectureReportOpinionService opinion;
    // 讲座报告积分的Service接口
    @Autowired
    private ISciLectureReportIntegralService sciLectureReportIntegralService;

    @RequiresPermissions("system:report:view")
    @GetMapping()
    public String report()
    {
        return prefix + "/report";
    }

    /**
     * 查询讲座报告列表
     */
    @RequiresPermissions("system:report:list")
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId,String year,SciLectureReport sciLectureReport)
    {
        System.out.println(tableId);
        sciLectureReport.setYear(year);
        sciLectureReport.setUid(getUserId());
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
            }else if (r.getRoleKey().equals("admin")){
                role="admin";
                break;
            }else if (r.getRoleKey().equals("dept_teacher")){
                role="dept_teacher";
                break;
            }
        }
        sciLectureReport.setRole(role);
        List<SciLectureReport> list = new ArrayList<>();
//        sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 5,4,6,7)); // 查询时状态设置
//        list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);

        // ====》 调整
        // 科研室
        if(role.equals("sci_tesearch")){
            sciLectureReport.setStatelist(Arrays.asList(2,5,4)); // 查询时状态设置
        }
        // 教研室
        else if(role.equals("research")){
            sciLectureReport.setStatelist(Arrays.asList(1,6, 3, 4,5,7)); // 查询时状态设置
//            sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 5,4,6,7)); // 查询时状态设置
        }
        // 学院
        else if(role.equals("dept_teacher")){
            sciLectureReport.setStatelist(Arrays.asList(2,4,6,7,5)); // 查询时状态设置
        }
        // 普通用户以及管理员
        else{
            sciLectureReport.setStatelist(Arrays.asList(0,1, 2, 3, 5,4,6,7)); // 查询时状态设置
        }
        list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
        // === 》 结束

//        // 科研室
//        if(role.equals("sci_tesearch")){
//            switch (tableId){
//                case "bootstrap-table0":  // 也完结
//
//                    break;
//                case "bootstrap-table1": // 项目申请
//                    sciLectureReport.setStatelist(Arrays.asList(1,2, 3,4,5));
//                    sciLectureReport.setState("4");  // 设置状态为4的并且是当前用户的项目不查询
//                    sciLectureReport.setTab("bootstrap-table1");
//                    list = sciLectureReportService.selectSciLectureReportListKYS_Tab1(sciLectureReport);
//                    break;
////                case "bootstrap-table2":  // 结项申请
////                    sciLectureReport.setStatelist(Arrays.asList(4,6,7,8,10));
////                    sciLectureReport.setState("4");
////                    sciLectureReport.setTab("bootstrap-table2");
////                    list = sciLectureReportService.selectSciLectureReportListKYS_Tab2(sciLectureReport);
////                    break;
//            }
//        }
//        // 教研室
//        else if(role.equals("research")){
//            switch (tableId){
//                case "bootstrap-table0":  // 也完结
////                    sciLectureReport.setStatelist(Arrays.asList(9));
////                    list = sciLectureReportService.selectSciLectureReportListJYS_Tab0(sciLectureReport);
//                    break;
//                case "bootstrap-table1": // 项目申请
//                    sciLectureReport.setStatelist(Arrays.asList(1,2,3,4,5));
//                    sciLectureReport.setState("4");  // 设置状态为4的并且是当前用户的项目不查询
//                    sciLectureReport.setTab("bootstrap-table1");
//                    list = sciLectureReportService.selectSciLectureReportListJYS_Tab1(sciLectureReport);
//                    break;
////                case "bootstrap-table2":  // 结项申请
////                    sciLectureReport.setStatelist(Arrays.asList(4,6,7,8,10));
////                    sciLectureReport.setState("4");
////                    sciLectureReport.setTab("bootstrap-table2");
////                    list = sciLectureReportService.selectSciLectureReportListJYS_Tab2(sciLectureReport);
////                    break;
//            }
//        }
//        // 管理员
//        else if(role.equals("admin")){
//            switch (tableId){
//                case "bootstrap-table0":  // 也完结
//
//                    break;
//                case "bootstrap-table1": // 项目申请
//                    sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 4, 5));
//                    list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
//                    break;
////                case "bootstrap-table2":  // 结项申请
////                    sciLectureReport.setStatelist(Arrays.asList(6,7,8,10)); // 讲座报告状态
////                    list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
////                    break;
//            }
//        }
//        // 普通用户
//        else{
//            switch (tableId){
//                case "bootstrap-table0":  // 也完结
//
//                    break;
//                case "bootstrap-table1": // 项目申请
////                    sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 5)); // 也完结和申请分开时的状态
//                    sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 5,4));
//                    list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
//                    break;
////                case "bootstrap-table2":  // 结项申请
////                    sciLectureReport.setStatelist(Arrays.asList(4,6,7,8,10)); // 讲座报告状态
////                    list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
////                    break;
//            }
//        }

        return getDataTable(list);
    }

    /**
     * 导出讲座报告列表
     */
    @RequiresPermissions("system:report:export")
    @Log(title = "讲座报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciLectureReport sciLectureReport)
    {
        sciLectureReport.setStatelist(Arrays.asList(1, 2, 3, 5,4,6,7));
        List<SciLectureReport> list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
        ExcelUtil<SciLectureReport> util = new ExcelUtil<SciLectureReport>(SciLectureReport.class);
        return util.exportExcel(list, "讲座报告数据");
    }

    /**
     * 新增讲座报告
     * 页面跳转
     */
    @GetMapping("/add")
    public String add(ModelMap mmap,SciLectureReportIntegral sciLectureReportIntegral)
    {
        // 获取当前的用户信息
        SysUser currentUser = ShiroUtils.getSysUser();

//        List<SysUser> userList =  userService.selectAllUser();
//        for (int a = 0; a<userList.size();a++) {
//            if(userList.get(a).getUserId() == getUserId()){
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a,user);
//                break;
//            }
//        }
//        mmap.put("sysUsers",userList);
        List<SciLectureReportIntegral> reportIntegralList = sciLectureReportIntegralService.selectSciLectureReportIntegralList(sciLectureReportIntegral);
        mmap.put("reportIntegralList",reportIntegralList);
        mmap.put("sysUsers",currentUser);
        return prefix + "/add";
    }

    /**
     * 新增保存讲座报告
     */
    @RequiresPermissions("system:report:add")
    @Log(title = "讲座报告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciLectureReport sciLectureReport)
    {
        return toAjax(sciLectureReportService.insertSciLectureReport(sciLectureReport));
    }

    /**
     * 提交讲座报告
     */
    @RequiresPermissions("system:report:submit")
    @Log(title = "提交讲座报告", businessType = BusinessType.DELETE)
    @PostMapping( "/tijiao")
    @ResponseBody
    public AjaxResult submit(String ids)
    {
//        System.out.println("ids = " + ids);
        return toAjax(sciLectureReportService.updateSciLectureReportByIds(ids));
    }

    /**
     * 修改讲座报告   查询对应id的数据
     */
    @RequiresPermissions("system:report:edit")
    @GetMapping("/edit/{id}/{urlFlag}")
    public String edit(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag,ModelMap mmap, SciLectureReportIntegral sciLectureReportIntegral)
    {
        // 获取当前的用户信息
        SysUser currentUser = ShiroUtils.getSysUser();
        SciLectureReport sciLectureReport = sciLectureReportService.selectSciLectureReportById(id);
        sciLectureReport.setUrlFlag(urlFlag);
//        List<SysUser> userList =  userService.selectAllUser();
//        mmap.put("sysUsers",userList);
        List<SciLectureReportIntegral> reportIntegralList = sciLectureReportIntegralService.selectSciLectureReportIntegralList(sciLectureReportIntegral);
        mmap.put("reportIntegralList",reportIntegralList);
        mmap.put("sysUsers",currentUser);
        mmap.put("sciLectureReport", sciLectureReport);
        return prefix + "/edit";
    }

    /**
     * 修改保存讲座报告
     */
    @RequiresPermissions("system:report:edit")
    @Log(title = "讲座报告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciLectureReport sciLectureReport)
    {
        sciLectureReport.setState("0");
        return toAjax(sciLectureReportService.updateSciLectureReport(sciLectureReport));
    }

    /**
     * 删除讲座报告
     */
    @RequiresPermissions("system:report:remove")
    @Log(title = "讲座报告", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciLectureReportService.deleteSciLectureReportByIds(ids));
    }

    /**
     * 讲座报告 （批阅  核查  查看 ）操作根据id查询对应的数据
     * detail ===> 详细页面
     */
    @RequiresPermissions(value = {"system:report:process","system:report:check","system:report:info","system:report:xyprocess"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciLectureReport sciLectureReport = sciLectureReportService.selectSciLectureReportById(id);
        sciLectureReport.setUrlFlag(urlFlag);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciLectureReport", sciLectureReport);
        return prefix + "/detail";
    }

    /**
     * 讲座报告  批阅通过保存
     * criticism  ===> 批阅
     */
    @RequiresPermissions(value = {"system:report:process","system:report:check","system:report:xyprocess"},logical= Logical.OR)
    @Log(title = "讲座报告审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/criticism")
    @ResponseBody
    public AjaxResult criticism(Integer id,String remark,String urlFlag)
    {
        System.out.println(remark);
        return toAjax(sciLectureReportService.criticism(id,getUserId(),remark,urlFlag));
    }

    /**
     * 讲座报告  批阅驳回保存
     * criticism  ===> 批阅
     */
    @RequiresPermissions(value = {"system:report:process","system:report:check","system:report:xyprocess","system:report:tuihui","system:report:chexiao"},logical= Logical.OR)
    @Log(title = "讲座报告审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping("/reject")
    @ResponseBody
    public AjaxResult reject(Integer id,String remark,String urlFlag)
    {
        System.out.println(remark);
        return toAjax(sciLectureReportService.reject(id,getUserId(),remark,urlFlag));
    }

    /**
     *讲座报告批阅意见表查询
     * opinion  ===> 意见
     */
    @RequiresPermissions("system:report:edit")
    @PostMapping("/opinion/{rid}")
    @ResponseBody
    public TableDataInfo opinion(@PathVariable("rid")Integer rid)
    {
        SciLectureReportOpinion op = new SciLectureReportOpinion();
        op.setBaogaoId(rid);
        List<SciLectureReportOpinion> list = opinion.opinionlist(op);
        return getDataTable(list);
    }

    @RequiresPermissions("system:report:edit")
    @GetMapping("/opinion/{rid}")
    @ResponseBody
    public TableDataInfo getopinion(@PathVariable("rid")Integer rid)
    {
        SciLectureReportOpinion op = new SciLectureReportOpinion();
        op.setBaogaoId(rid);
        List<SciLectureReportOpinion> list = opinion.opinionlist(op);
        return getDataTable(list);
    }

    /**
     * ==================================== over =================================
     * 讲座报告 （申请结项）操作根据id查询对应的数据
     * detail ===> 详细页面
     */
    @RequiresPermissions(value = {"system:report:jiexiang"},logical= Logical.OR)
    @GetMapping("/over/{id}")
    public String over(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciLectureReport sciLectureReport = sciLectureReportService.selectSciLectureReportById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciLectureReport", sciLectureReport);
        return prefix + "/over";
    }

    /**
     * 结项申请保存
     */
    @RequiresPermissions("system:report:add")
    @Log(title = "讲座报告结项申请", businessType = BusinessType.INSERT)
    @PostMapping("/over_add")
    @ResponseBody
    public AjaxResult overAddSave(SciLectureReport sciLectureReport)
    {
        return toAjax(sciLectureReportService.SciLectureReportOverAdd(sciLectureReport));
    }

    /**
     * 讲座报告结项审批 （批阅  核查  查看 ）操作根据id查询对应的数据
     * detail ===> 详细页面
     */
    @RequiresPermissions(value = {"system:report:process","system:report:check","system:report:info"},logical= Logical.OR)
    @GetMapping("/over_detail/{id}/{urlFlag}")
    public String overDetail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciLectureReport sciLectureReport = sciLectureReportService.selectSciLectureReportById(id);
        sciLectureReport.setUrlFlag(urlFlag);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciLectureReport", sciLectureReport);
        return prefix + "/over_detail";
    }
}
