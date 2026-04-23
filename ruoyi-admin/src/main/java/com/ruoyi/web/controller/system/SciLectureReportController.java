package com.ruoyi.web.controller.system;

import java.util.*;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.domain.SciLectureReportOpinion;
import com.ruoyi.system.service.ISciLectureReportIntegralService;
import com.ruoyi.system.service.ISciLectureReportOpinionService;
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

    // 设置角色集合。若后期需要添加新的学院管理员角色，将其权限字符添加到集合中即可
    private static final Set<String> TEACHER_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher", // 软件学院管理员
            "discuss_college", // 商学院管理员
            "dzgc_college", // 电子工程学院管理员
            "yssj_college", // 艺术设计学院管理员
            "student_college", // 学生处学院管理员
            "marxism_college", // 马克思主义学院管理员
            "general" //综合院部管理员
    ));

    @RequiresPermissions("system:report:view")
    @GetMapping()
    public String report(ModelMap mmap)
    {
        // 将当前用户信息传递到模板，用于前端角色识别
        mmap.put("user", getSysUser());
        return prefix + "/report";
    }

    /**
     * 查询讲座报告列表
     * 功能：根据角色和表格ID查询讲座报告列表
     * SQL：根据角色不同，执行不同的查询语句
     */
    @RequiresPermissions("system:report:list")
    @Log(title = "查询讲座报告列表", businessType = BusinessType.OTHER)
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId, String year, SciLectureReport sciLectureReport)
    {
        sciLectureReport.setUid(getUserId());
        sciLectureReport.setYear(year);
        
        // 获取用户角色
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        for (SysRole r : roles) {
            if ("sci_tesearch".equals(r.getRoleKey())) {
                role = "sci_tesearch";
                break;
            } else if ("research".equals(r.getRoleKey())) {
                role = "research";
                break;
            } else if (TEACHER_ROLES.contains(r.getRoleKey())) {
                role = "dept_teacher";
                break;
            }
        }
        
        // 设置角色和表格ID
        sciLectureReport.setRole(role);
        sciLectureReport.setTab(tableId);
        
        // 基于角色的查询字段白名单校验
        validateQueryFieldsByRole(sciLectureReport, role);
        
        // 统一使用一个查询方法，通过@DataScope控制数据权限
        startPage();
        List<SciLectureReport> list = sciLectureReportService.selectSciLectureReportListAll(sciLectureReport);
        return getDataTable(list);
    }
    
    /**
     * 基于角色的查询字段白名单校验
     * 教师角色：仅允许通过"课题名称"检索
     * 教研室角色：允许通过"主持人（论文对应第一作者）"+"课题名称"检索
     * 学院角色：允许通过"专业"+"主持人（论文对应第一作者）"+"课题名称"检索
     * 科研处角色：允许通过"学院"+"专业"+"主持人（论文对应第一作者）"+"课题名称"全维度检索
     * 
     * @param sciLectureReport 查询条件对象
     * @param role 用户角色
     */
    private void validateQueryFieldsByRole(SciLectureReport sciLectureReport, String role) {
        if ("teacher".equals(role)) {
            // 教师：只保留课题名称，清空其他字段
            sciLectureReport.setTeacherName(null);
            sciLectureReport.setKeyanshi(null);
            sciLectureReport.setXueyuan(null);
        } else if ("research".equals(role)) {
            // 教研室：只保留主持人 + 课题名称，清空其他字段
            sciLectureReport.setKeyanshi(null);
            sciLectureReport.setXueyuan(null);
        } else if ("dept_teacher".equals(role)) {
            // 学院：保留专业 + 主持人 + 课题名称，清空学院字段
            sciLectureReport.setXueyuan(null);
        }
        // 科研处角色 (sci_tesearch) 可以使用所有字段，无需清空
    }

    /**
     * 导出讲座报告列表
     */
    @RequiresPermissions("system:report:export")
    @Log(title = "讲座报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciLectureReport sciLectureReport, String ids)
    {
        List<SciLectureReport> list = new ArrayList<SciLectureReport>();
        if (ids.equals("")){
            // 获取当前的用户信息
            SysUser currentUser = ShiroUtils.getSysUser();
            sciLectureReport.setUid(currentUser.getUserId());
            List<SysRole> roles = getSysUser().getRoles();
            Integer roleId = null;
            for (SysRole r : roles) {
                roleId = Math.toIntExact(r.getRoleId());
                break; // 只取第一个角色
            }
            
            // 根据角色ID设置状态列表
            if(roleId != null && roleId == 101){ // 科研处：角色ID 101
                sciLectureReport.setStatelist(Arrays.asList(2,5,4)); // 查询时状态设置
            }else if(roleId != null && roleId == 102){ // 教研室：角色ID 102
                sciLectureReport.setStatelist(Arrays.asList(1,6, 3, 4,5,7)); // 查询时状态设置
            }else if(roleId != null && (roleId == 103 || roleId == 104 || roleId == 105 || roleId == 106 || 
                                     roleId == 107 || roleId == 108 || roleId == 116 || roleId == 117 || 
                                     roleId == 118 || roleId == 119 || roleId == 120)){ // 学院
                sciLectureReport.setStatelist(Arrays.asList(2,4,6,7,5)); // 查询时状态设置
            }else if(roleId != null && roleId == 100){ // 教师：角色ID 100
                sciLectureReport.setStatelist(Arrays.asList(0,1, 2, 3, 5,4,6,7)); // 查询时状态设置
            }else if(roleId != null && roleId == 1){ // 管理员：角色ID 1
                sciLectureReport.setStatelist(Arrays.asList(0,1, 2, 3, 5,4,6,7)); // 查询时状态设置
            }else{ // 其他用户
                sciLectureReport.setStatelist(Arrays.asList(0,1, 2, 3, 5,4,6,7)); // 查询时状态设置
            }
            list = sciLectureReportService.selectSciLectureReportList(sciLectureReport);
        }else {
            list = sciLectureReportService.selectSciLectureReportListByIds(ids);
        }
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
     * 新增保存讲座报告保存时场地校验
     */
    @RequiresPermissions("system:report:add")
    @Log(title = "讲座报告场地校验", businessType = BusinessType.INSERT)
    @PostMapping("/checkConflict")
    @ResponseBody
    public AjaxResult checkConflict(SciLectureReport sciLectureReport)
    {
        Map <String, Object> map = sciLectureReportService.checkConflict(sciLectureReport);
        return success(map);
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
        sciLectureReport.setState("LECTURE_DRAFT");
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
    @RequiresPermissions("system:report:info")
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
        return toAjax(sciLectureReportService.reject(id,getUserId(),remark,urlFlag));
    }

    /**
     *讲座报告批阅意见表查询
     * opinion  ===> 意见
     */
    @RequiresPermissions("system:report:info")
    @PostMapping("/opinion/{rid}")
    @ResponseBody
    public TableDataInfo opinion(@PathVariable("rid")Integer rid)
    {
        SciLectureReportOpinion op = new SciLectureReportOpinion();
        op.setBaogaoId(rid);
        List<SciLectureReportOpinion> list = opinion.opinionlist(op);
        return getDataTable(list);
    }

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
