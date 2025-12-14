package com.ruoyi.web.controller.IntraSchoolProject;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/StatsQuery")
public class StatsQueryController extends BaseController {
    private String prefix = "system/StatsQuery";
    @Autowired // 纵向课题 1
    private ISysUserService sysUserService;
    @Autowired // 纵向课题 1
    private ISciHorizontalApplyVerticalService sciHorizontalApplyVerticalService;
    @Autowired // 横向课题 2
    private ISciHorizontalApplyService sciHorizontalApplyService;
    @Autowired // 成果转化 3
    private ISciIntraSchProApplyService sciIntraSchProApplyService;
    @Autowired // 论文 4
    private ISciPaperAService sciPaperAService;
    @Autowired // 教材专著 5 赵威翰
    private ISciJiaocairuanzhuService sciJiaocairuanzhuService;
    @Autowired // 专利软著 6 雷
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;
    @Autowired // 奖励 7 15204
    private ISysRewardService sysRewardService;
    @Autowired // 讲座报告Service接口 8
    private ISciLectureReportService sciLectureReportService;

    @Autowired
    private ISysUserService userService;
    @GetMapping("")
    String view(ModelMap mmap) {

        return prefix + "/view";
    }

    @GetMapping("/ToCheck")
    String viewToCheck(ModelMap mmap) {
        List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
        // todo: 不知道为啥要这样处理 很迷 !!!
//        for (int a = 0; a < userList.size(); a++) {
//            if (userList.get(a).getUserId() == getUserId()) {
//                SysUser user = userList.get(a);
//                user.setFlag(true);
//                userList.set(a, user);
//                break;
//            }
//        }
        mmap.put("sysUsers", userList);
        return prefix + "/viewToCheck";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestParam Map<String, String> params) {
        // 如果前端传递了noData标志，说明是初始加载，返回空数据
        if ("true".equals(params.get("noData"))) {
            return getDataTable(new ArrayList<>());
        }
        params.put("uid",getUserId().toString());
        //判断身份
        String role_str = panRole_str();
        SysUser sysUser = getSysUser();
        //学院
        if (role_str.equals("dept_teacher")) {
            params.put("Pcollege",sysUser.getParentId().toString());
        }
        //科研处
        else if (role_str.equals("sci_tesearch")) {

        }
        //        教研室
        else if (role_str.equals("research")) {
            params.put("major",sysUser.getDeptId().toString());
        }
        //admin
        else if (role_str.equals("admin")) {

        }
        else if (role_str.equals("teacher")) {
            params.put("userId",getUserId().toString());

        }
        // 获取分页参数，添加默认值避免 null
        String offsetStr = params.getOrDefault("offset", "0");
        String limitStr = params.getOrDefault("limit", "10");
        // 解析为整数
        int offset = Integer.parseInt(offsetStr);
        int limit = Integer.parseInt(limitStr);
        // 计算分页参数
        int pageNum = offset / limit + 1;
        int pageSize = limit;
        // 使用 PageHelper 进行分页
        PageHelper.startPage(pageNum, pageSize);


        // 获取项目类别参数
        String remark = params.get("remark");
        //System.out.println("项目类别: " + remark);

        List<Object> list = new ArrayList<>();
        // 根据项目类别返回不同的数据
        if (remark != null && !remark.isEmpty()) {
            if ("1".equals(remark)) {
                // 获取项目类别1的数据（纵向课题）
                List<SciHorizontalApplyVertical> statsQuery = sciHorizontalApplyVerticalService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("2".equals(remark)) {
                // 获取项目类别2的数据（横向课题）
                List<SciHorizontalApply> statsQuery = sciHorizontalApplyService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("3".equals(remark)) {
                // 查询项目类别3的数据（成果转化）
                List<SciIntraSchoolPro> statsQuery = sciIntraSchProApplyService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("4".equals(remark)) {
                // 获取项目类别4的数据（论文）
                List<SciPaperA> statsQuery = sciPaperAService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("5".equals(remark)) {
                // 获取项目类别5的数据（教材专著）
                List<SciJiaocairuanzhu> statsQuery = sciJiaocairuanzhuService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("6".equals(remark)) {
                // 获取项目类别6的数据（专利软著）
                List<SciZhuanliruanzhu> statsQuery = sciZhuanliruanzhuService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("7".equals(remark)) {
                // 获取项目类别7的数据（奖励）
                List<SysReward> statsQuery = sysRewardService.getStatsQuery(params);
                return getDataTable(statsQuery);
            } else if ("8".equals(remark)) {
                // 获取项目类别8的数据（讲座报告）
                List<SciLectureReportOpinion> statsQuery = sciLectureReportService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }
        } else {
            // 如果没有选择项目类别，返回空数据
            list = new ArrayList<>();
        }

        return getDataTable(list);
    }

    /**
     * 导出列表
     */

    //@RequiresPermissions("system:statsquery:export")
    @Log(title = "统计导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(@RequestParam Map<String, String> params) {
        params.put("uid",getUserId().toString());
        //判断身份
        String role_str = panRole_str();
        SysUser sysUser = getSysUser();
        //学院
        if (role_str.equals("dept_teacher")) {
            params.put("Pcollege",sysUser.getParentId().toString());
        }
        //科研处
        else if (role_str.equals("sci_tesearch")) {

        }
        //        教研室
        else if (role_str.equals("research")) {
            params.put("major",sysUser.getDeptId().toString());
        }
        //admin
        else if (role_str.equals("admin")) {

        }
        else if (role_str.equals("teacher")) {
            params.put("userId",getUserId().toString());

        }

        try {
            // 获取项目类别参数
            String remark = params.get("remark");
            // 根据项目类别返回不同的数据
            if (remark != null && !remark.isEmpty()) {
                if ("1".equals(remark)) {
                    // 获取项目类别1的数据（纵向课题）导出
                    List<SciHorizontalApplyVertical> exportList = sciHorizontalApplyVerticalService.getStatsQuery(params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciHorizontalApplyVertical> util = new ExcelUtil<SciHorizontalApplyVertical>(SciHorizontalApplyVertical.class);
                    return util.exportExcel(exportList, "纵向课题数据");
                } else if ("2".equals(remark)) {
                    // 获取项目类别2的数据（横向课题）导出
                    List<SciHorizontalApply> exportList = sciHorizontalApplyService.getStatsQuery(params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciHorizontalApply> util = new ExcelUtil<SciHorizontalApply>(SciHorizontalApply.class);
                    return util.exportExcel(exportList, "横向课题数据");
                } else if ("3".equals(remark)) {
                    // 查询项目类别3的数据（成果转化）导出
                    List<SciIntraSchoolPro> exportList = sciIntraSchProApplyService.getStatsQuery(params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciIntraSchoolPro> util = new ExcelUtil<SciIntraSchoolPro>(SciIntraSchoolPro.class);
                    return util.exportExcel(exportList, "成果转化数据");
                } else if ("4".equals(remark)) {
                    // 获取项目类别4的数据（论文）导出
                    List<SciPaperA> exportList = sciPaperAService.getStatsQuery( params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
                    return util.exportExcel(exportList, "论文数据");
                } else if ("5".equals(remark)) {
                    // 获取项目类别5的数据（教材专著）导出
                    List<SciJiaocairuanzhu> exportList = sciJiaocairuanzhuService.getStatsQuery( params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciJiaocairuanzhu> util = new ExcelUtil<SciJiaocairuanzhu>(SciJiaocairuanzhu.class);
                    return util.exportExcel(exportList, "教材专著数据");
                } else if ("6".equals(remark)) {
                    // 获取项目类别6的数据（专利软著）导出
                    List<SciZhuanliruanzhu> exportList = new ArrayList<>();
                    if (params.get("userId")!=null && !params.get("userId").isEmpty()){
                          exportList = sciZhuanliruanzhuService.getStatsQueryToExcil( params);
                    }else{
                          exportList = sciZhuanliruanzhuService.getStatsQuery( params);

                    }
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
                    return util.exportExcel(exportList, "专利软著数据");
                } else if ("7".equals(remark)) {
                    // 获取项目类别7的数据（奖励）导出
                    List<SysReward> exportList = sysRewardService.getStatsQuery( params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SysReward> util = new ExcelUtil<SysReward>(SysReward.class);
                    return util.exportExcel(exportList, "奖励数据");
                } else if ("8".equals(remark)) {
                    // 获取项目类别8的数据（讲座报告）导出
                    List<SciLectureReportOpinion> exportList = sciLectureReportService.getStatsQuery( params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciLectureReportOpinion> util = new ExcelUtil<SciLectureReportOpinion>(SciLectureReportOpinion.class);
                    return util.exportExcel(exportList, "讲座报告数据");
                }
            }
            return error("未指定导出模块或模块不存在");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/listToCheck")
    @ResponseBody
    public TableDataInfo listToCheck(@RequestParam Map<String, String> params) {
        // 如果前端传递了noData标志，说明是初始加载，返回空数据
        if ("true".equals(params.get("noData"))) {
            return getDataTable(new ArrayList<>());
        }
        params.put("uid",getUserId().toString());
        //判断身份
        String role_str = panRole_str();
        SysUser sysUser = getSysUser();
        //学院
        if (role_str.equals("dept_teacher")) {
            params.put("Pcollege",sysUser.getParentId().toString());
        }
        //科研处
        else if (role_str.equals("sci_tesearch")) {

        }
        //        教研室
        else if (role_str.equals("research")) {
            params.put("major",sysUser.getDeptId().toString());
        }
        //admin
        else if (role_str.equals("admin")) {

        }
        else if (role_str.equals("teacher")) {
            params.put("userId",getUserId().toString());

        }
        // 获取分页参数，添加默认值避免 null
        String offsetStr = params.getOrDefault("offset", "0");
        String limitStr = params.getOrDefault("limit", "10");
        // 解析为整数
        int offset = Integer.parseInt(offsetStr);
        int limit = Integer.parseInt(limitStr);
        // 计算分页参数
        int pageNum = offset / limit + 1;
        int pageSize = limit;
        // 使用 PageHelper 进行分页
        PageHelper.startPage(pageNum, pageSize);


        // 获取项目类别参数
        String remark = params.get("remark");
        //System.out.println("项目类别: " + remark);

        List<Object> list = new ArrayList<>();
        // 根据项目类别返回不同的数据
        if (remark != null && !remark.isEmpty()) {
            if ("1".equals(remark)) {
                // 获取项目类别1的数据（纵向课题）
                List<SciHorizontalApplyVertical> statsQuery = sciHorizontalApplyVerticalService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("2".equals(remark)) {
                // 获取项目类别2的数据（横向课题）
                List<SciHorizontalApply> statsQuery = sciHorizontalApplyService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("3".equals(remark)) {
                // 查询项目类别3的数据（成果转化）
                List<SciIntraSchoolPro> statsQuery = sciIntraSchProApplyService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("4".equals(remark)) {
                // 获取项目类别4的数据（论文）
                List<SciPaperA> statsQuery = sciPaperAService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("5".equals(remark)) {
                // 获取项目类别5的数据（教材专著）
                List<SciJiaocairuanzhu> statsQuery = sciJiaocairuanzhuService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("6".equals(remark)) {
                // 获取项目类别6的数据（专利软著）
                List<SciZhuanliruanzhu> statsQuery = sciZhuanliruanzhuService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("7".equals(remark)) {
                // 获取项目类别7的数据（奖励）
                List<SysReward> statsQuery = sysRewardService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            } else if ("8".equals(remark)) {
                // 获取项目类别8的数据（讲座报告）
                List<SciLectureReport> statsQuery = sciLectureReportService.getStatsQueryToCheck(params);
                return getDataTable(statsQuery);
            }
        } else {
            // 如果没有选择项目类别，返回空数据
            list = new ArrayList<>();
        }

        return getDataTable(list);
    }

    /**
     * 导出核算列表
     * 使用与listToCheck相同的查询逻辑
     * 导出的Excel包含查询到的所有列，并计算总分
     */
    //@RequiresPermissions("system:statsquery:export")
    @Log(title = "导出核算", businessType = BusinessType.EXPORT)
    @PostMapping("/exportToCheck")
    @ResponseBody
    public AjaxResult exportToCheck(@RequestParam Map<String, String> params) {
        String remark = params.get("remark");

        try {
            // 使用与listToCheck相同的参数处理逻辑
            params.put("uid", getUserId().toString());
            //判断身份
            String role_str = panRole_str();
            SysUser sysUser = getSysUser();
            //学院
            if (role_str.equals("dept_teacher")) {
                params.put("Pcollege", sysUser.getParentId().toString());
            }
            //科研处
            else if (role_str.equals("sci_tesearch")) {

            }
            //        教研室
            else if (role_str.equals("research")) {
                params.put("major", sysUser.getDeptId().toString());
            }
            //admin
            else if (role_str.equals("admin")) {

            }
            else if (role_str.equals("teacher")) {
                params.put("userId", getUserId().toString());
            }
            
            // 根据项目类别返回不同的数据
            if (remark != null && !remark.isEmpty()) {
                if ("1".equals(remark)) {
                    // 获取项目类别1的数据（纵向课题）导出
                    List<SciHorizontalApplyVertical> exportList = sciHorizontalApplyVerticalService.getStatsQueryToCheck(params);

                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算积分：按课题ID和参与者用户ID分组，计算每个课题的立项和结项积分之和
                    // 使用 Map 存储 (id, participantUserId) -> 积分总和
                    Map<String, Double> scoreMap = new java.util.HashMap<>();
                    
                    // 第一遍遍历：计算每个(课题ID, 参与者用户ID)组合的积分总和
                    for (SciHorizontalApplyVertical item : exportList) {
                        // 使用 s.id 和 u.user_id（参与者的ID）作为分组键
                        // participantUserId 是参与者的ID，userId 是申请人的ID
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double currentScore = 0.0;
                        
                        // 解析积分值
                        if (item.getChangeValue() != null && !item.getChangeValue().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getChangeValue());
                            } catch (NumberFormatException e) {
                                // 如果解析失败，使用0
                                currentScore = 0.0;
                            }
                        }
                        
                        // 累加积分（同一个课题的立项和结项积分相加）
                        scoreMap.put(key, scoreMap.getOrDefault(key, 0.0) + currentScore);
                    }
                    
                    // 计算每个老师获得的总分（按参与者用户ID分组，累加所有项目的积分）
                    // 使用 Set 记录已经计算过的项目，避免重复累加
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    java.util.Set<String> processedProjects = new java.util.HashSet<>();
                    
                    for (SciHorizontalApplyVertical item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        
                        // 如果这个项目还没有被计算过，则累加到老师总分中
                        if (!processedProjects.contains(key)) {
                            Double projectScore = scoreMap.getOrDefault(key, 0.0);
                            teacherTotalScoreMap.put(participantId, teacherTotalScoreMap.getOrDefault(participantId, 0.0) + projectScore);
                            processedProjects.add(key);
                        }
                    }
                    
                    // 为每个记录设置积分总和和老师总分（不去重，导出所有记录）
                    for (SciHorizontalApplyVertical item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double totalScore = scoreMap.getOrDefault(key, 0.0);
                        item.setTotalScore(String.valueOf(totalScore));
                        
                        // 设置该老师获得的总分
                        Double teacherTotalScore = teacherTotalScoreMap.getOrDefault(participantId, 0.0);
                        item.setTotalTeacherScore(String.valueOf(teacherTotalScore));
                    }
                    
                    // 直接使用所有记录（不去重）
                    List<SciHorizontalApplyVertical> finalList = exportList;
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciHorizontalApplyVertical> util = new ExcelUtil<SciHorizontalApplyVertical>(SciHorizontalApplyVertical.class);
                    return util.exportExcel(finalList, "纵向课题核算数据");
                }
                else if ("2".equals(remark)) {

                    // 获取项目类别2的数据（横向课题）导出
                    List<SciHorizontalApply> exportList = sciHorizontalApplyService.getStatsQueryToCheck(params);

                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算积分：按课题ID和参与者用户ID分组，计算每个课题的立项和结项积分之和
                    // 使用 Map 存储 (id, participantUserId) -> 积分总和
                    Map<String, Double> scoreMap = new java.util.HashMap<>();
                    
                    // 第一遍遍历：计算每个(课题ID, 参与者用户ID)组合的积分总和
                    for (SciHorizontalApply item : exportList) {
                        // 使用 s.id 和 u.user_id（参与者的ID）作为分组键
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double currentScore = 0.0;
                        
                        // 解析积分值
                        if (item.getChangeValue() != null && !item.getChangeValue().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getChangeValue());
                            } catch (NumberFormatException e) {
                                // 如果解析失败，使用0
                                currentScore = 0.0;
                            }
                        }
                        
                        // 累加积分（同一个课题的立项和结项积分相加）
                        scoreMap.put(key, scoreMap.getOrDefault(key, 0.0) + currentScore);
                    }
                    
                    // 计算每个老师获得的总分（按参与者用户ID分组，累加所有项目的积分）
                    // 使用 Set 记录已经计算过的项目，避免重复累加
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    java.util.Set<String> processedProjects = new java.util.HashSet<>();
                    
                    for (SciHorizontalApply item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        
                        // 如果这个项目还没有被计算过，则累加到老师总分中
                        if (!processedProjects.contains(key)) {
                            Double projectScore = scoreMap.getOrDefault(key, 0.0);
                            teacherTotalScoreMap.put(participantId, teacherTotalScoreMap.getOrDefault(participantId, 0.0) + projectScore);
                            processedProjects.add(key);
                        }
                    }
                    
                    // 为每个记录设置积分总和和老师总分（不去重，导出所有记录）
                    for (SciHorizontalApply item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double totalScore = scoreMap.getOrDefault(key, 0.0);
                        item.setTotalScore(String.valueOf(totalScore));
                        
                        // 设置该老师获得的总分
                        Double teacherTotalScore = teacherTotalScoreMap.getOrDefault(participantId, 0.0);
                        item.setTotalTeacherScore(String.valueOf(teacherTotalScore));
                    }
                    
                    // 直接使用所有记录（不去重）
                    List<SciHorizontalApply> finalList = exportList;
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciHorizontalApply> util = new ExcelUtil<SciHorizontalApply>(SciHorizontalApply.class);
                    return util.exportExcel(finalList, "横向课题核算数据");
                }
                else if ("3".equals(remark)) {
                    // 查询项目类别3的数据（成果转化）导出
                    List<SciIntraSchoolPro> exportList = sciIntraSchProApplyService.getStatsQueryToCheck(params);

                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算积分：按课题ID和参与者用户ID分组，计算每个课题的积分
                    // 使用 Map 存储 (id, participantUserId) -> 积分总和
                    Map<String, Double> scoreMap = new java.util.HashMap<>();
                    
                    // 第一遍遍历：计算每个(课题ID, 参与者用户ID)组合的积分总和
                    for (SciIntraSchoolPro item : exportList) {
                        // 使用 s.id 和 u.user_id（参与者的ID）作为分组键
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double currentScore = 0.0;
                        
                        // 解析积分值
                        if (item.getChangeValue() != null && !item.getChangeValue().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getChangeValue());
                            } catch (NumberFormatException e) {
                                // 如果解析失败，使用0
                                currentScore = 0.0;
                            }
                        }
                        
                        // 累加积分（同一个课题的积分相加，如果有多个记录）
                        scoreMap.put(key, scoreMap.getOrDefault(key, 0.0) + currentScore);
                    }
                    
                    // 计算每个老师获得的总分（按参与者用户ID分组，累加所有项目的积分）
                    // 使用 Set 记录已经计算过的项目，避免重复累加
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    java.util.Set<String> processedProjects = new java.util.HashSet<>();
                    
                    for (SciIntraSchoolPro item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        
                        // 如果这个项目还没有被计算过，则累加到老师总分中
                        if (!processedProjects.contains(key)) {
                            Double projectScore = scoreMap.getOrDefault(key, 0.0);
                            teacherTotalScoreMap.put(participantId, teacherTotalScoreMap.getOrDefault(participantId, 0.0) + projectScore);
                            processedProjects.add(key);
                        }
                    }
                    
                    // 为每个记录设置积分总和和老师总分（不去重，导出所有记录）
                    for (SciIntraSchoolPro item : exportList) {
                        Integer participantId = item.getParticipantUserId() != null ? item.getParticipantUserId() : item.getUserId();
                        String key = item.getId() + "_" + participantId;
                        Double totalScore = scoreMap.getOrDefault(key, 0.0);
                        item.setTotalScore(String.valueOf(totalScore));
                        
                        // 设置该老师获得的总分
                        Double teacherTotalScore = teacherTotalScoreMap.getOrDefault(participantId, 0.0);
                        item.setTotalTeacherScore(String.valueOf(teacherTotalScore));
                    }
                    
                    // 直接使用所有记录（不去重）
                    List<SciIntraSchoolPro> finalList = exportList;
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciIntraSchoolPro> util = new ExcelUtil<SciIntraSchoolPro>(SciIntraSchoolPro.class);
                    return util.exportExcel(finalList, "成果转化核算数据");
                }
                else if ("4".equals(remark)) {
                    // 获取项目类别4的数据（论文）导出
                    List<SciPaperA> exportList = sciPaperAService.getStatsQueryToCheck(params);
                    
                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算总分：按用户ID分组，累加所有论文的积分
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    for (SciPaperA item : exportList) {
                        Integer userId = item.getParticipantUserId() != null ? item.getParticipantUserId() : (item.getUserId() != null ? item.getUserId().intValue() : null);
                        if (userId == null) continue;
                        
                        Double currentScore = 0.0;
                        if (item.getChangeValue() != null && !item.getChangeValue().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getChangeValue());
                            } catch (NumberFormatException e) {
                                currentScore = 0.0;
                            }
                        }
                        teacherTotalScoreMap.put(userId, teacherTotalScoreMap.getOrDefault(userId, 0.0) + currentScore);
                    }
                    
                    // 设置每个记录的总分
                    for (SciPaperA item : exportList) {
                        Integer userId = item.getParticipantUserId() != null ? item.getParticipantUserId() : (item.getUserId() != null ? item.getUserId().intValue() : null);
                        if (userId != null) {
                            Double totalScore = teacherTotalScoreMap.getOrDefault(userId, 0.0);
                            item.setTotalTeacherScore(String.valueOf(totalScore));
                        }
                    }
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
                    return util.exportExcel(exportList, "论文核算数据");
                }
                else if ("5".equals(remark)) {
                    // 获取项目类别5的数据（教材专著）导出
                    List<SciJiaocairuanzhu> exportList = sciJiaocairuanzhuService.getStatsQueryToCheck(params);
                    
                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算总分：按用户ID分组，累加所有教材专著的积分
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    for (SciJiaocairuanzhu item : exportList) {
                        Integer userId = item.getUserId();
                        if (userId == null) continue;
                        
                        Double currentScore = 0.0;
                        if (item.getJifen() != null && !item.getJifen().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getJifen());
                            } catch (NumberFormatException e) {
                                currentScore = 0.0;
                            }
                        }
                        teacherTotalScoreMap.put(userId, teacherTotalScoreMap.getOrDefault(userId, 0.0) + currentScore);
                    }
                    
                    // 设置每个记录的总分（需要添加totalTeacherScore字段到实体类，这里先注释）
                    // for (SciJiaocairuanzhu item : exportList) {
                    //     Integer userId = item.getUserId();
                    //     if (userId != null) {
                    //         Double totalScore = teacherTotalScoreMap.getOrDefault(userId, 0.0);
                    //         item.setTotalTeacherScore(String.valueOf(totalScore));
                    //     }
                    // }
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciJiaocairuanzhu> util = new ExcelUtil<SciJiaocairuanzhu>(SciJiaocairuanzhu.class);
                    return util.exportExcel(exportList, "教材专著核算数据");
                }
                else if ("6".equals(remark)) {
                    // 获取项目类别6的数据（专利软著）导出
                    List<SciZhuanliruanzhu> exportList = sciZhuanliruanzhuService.getStatsQueryToCheck(params);
                    
                    // 处理状态描述
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    
                    // 计算总分：按用户ID分组，累加所有专利软著的积分
                    Map<Integer, Double> teacherTotalScoreMap = new java.util.HashMap<>();
                    for (SciZhuanliruanzhu item : exportList) {
                        Integer userId = item.getUserId();
                        if (userId == null) continue;
                        
                        Double currentScore = 0.0;
                        if (item.getJifen() != null && !item.getJifen().isEmpty()) {
                            try {
                                currentScore = Double.parseDouble(item.getJifen());
                            } catch (NumberFormatException e) {
                                currentScore = 0.0;
                            }
                        }
                        teacherTotalScoreMap.put(userId, teacherTotalScoreMap.getOrDefault(userId, 0.0) + currentScore);
                    }
                    
                    // 设置每个记录的总分（需要添加totalTeacherScore字段到实体类，这里先注释）
                    // for (SciZhuanliruanzhu item : exportList) {
                    //     Integer userId = item.getUserId();
                    //     if (userId != null) {
                    //         Double totalScore = teacherTotalScoreMap.getOrDefault(userId, 0.0);
                    //         item.setTotalTeacherScore(String.valueOf(totalScore));
                    //     }
                    // }
                    
                    // 使用普通导出方法（不合并单元格）
                    ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
                    return util.exportExcel(exportList, "专利软著核算数据");
                }
                else if ("7".equals(remark)) {
                    // 获取项目类别7的数据（奖励）导出
                    List<SysReward> exportList = sysRewardService.getStatsQueryToCheck(params);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SysReward> util = new ExcelUtil<SysReward>(SysReward.class);
                    return util.exportExcel(exportList, "奖励核算数据");
                }
                else if ("8".equals(remark)) {
                    // 获取项目类别8的数据（讲座报告）导出
                    List<SciLectureReport> exportList = sciLectureReportService.getStatsQueryToCheck(params);
                    ExcelUtil<SciLectureReport> util = new ExcelUtil<SciLectureReport>(SciLectureReport.class);
                    return util.exportExcel(exportList, "讲座报告核算数据");
                }
            }
            return error("未指定导出模块或模块不存在");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
    /**
     * 判断当前登陆用户的身份
     * dept_teacher，sci_tesearch，research，admin，teacher
     *
     * @return
     */
    private String panRole_str() {
        String role_str = "";
        //当前登陆角色列表，如果一个人有多个角色，列表就多一项
        List<SysRole> roles = getSysUser().getRoles();
        //代表六个身份
        List<Integer> roles_list = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

        for (SysRole r : roles) {
            System.out.println("r.getRoleKey()=" + r.getRoleKey());
            //普通教师
            if (r.getRoleId() == 100) {
                roles_list.set(2, 1);
            }
            //科研处
            if (r.getRoleId() == 101) {
                roles_list.set(3, 1);
            }
            //教研室
            if (r.getRoleId() == 102) {
                roles_list.set(4, 1);
            }
            //学院负责人
            if (r.getRoleId() == 103 || r.getRoleId() == 104 || r.getRoleId() == 105 || r.getRoleId() == 106 || r.getRoleId() == 107 || r.getRoleId() == 108) {
                roles_list.set(5, 1);
            }
            //超级管理员
            if (r.getRoleId() == 1) {
                roles_list.set(1, 1);
            }
        }
        //学院
        if (roles_list.get(5) == 1) {
            role_str = "dept_teacher";
        }
//        科研处
        else if (roles_list.get(3) == 1) {
            role_str = "sci_tesearch";

        }
//        教研室
        else if (roles_list.get(4) == 1) {
            System.out.println("this is 教研");
            role_str = "research";

        }
        //admin
        else if (roles_list.get(1) == 1) {
            role_str = "admin";

        }

        String rolesString = roles_list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(""));
        //只是普通教师
        if (rolesString.equals("001000")) {
            role_str = "teacher";
        }
        return role_str;


    }



}
