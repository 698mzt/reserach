package com.ruoyi.web.controller.IntraSchoolProject;


import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/StatsQuery")
public class StatsQueryController extends BaseController {
    private final String prefix = "system/StatsQuery";

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
    @Autowired
    private ISysDeptService deptService;
    
    /**
     * 跳转统计查询页面
     *
     * @param mmap 页面模型
     * @return 页面路径
     */
    @Log(title = "统计查询页面", businessType = BusinessType.OTHER)
    @GetMapping("")
    public String view(ModelMap mmap) {
        preloadQueryData(mmap);
        return prefix + "/view";
    }

    /**
     * 跳转核算查询页面
     *
     * @param mmap 页面模型
     * @return 页面路径
     */
    @Log(title = "核算查询页面", businessType = BusinessType.OTHER)
    @GetMapping("/ToCheck")
    public String viewToCheck(ModelMap mmap) {
        preloadQueryData(mmap);
        return prefix + "/viewToCheck";
    }

    private void preloadQueryData(ModelMap mmap) {
        List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
        mmap.put("sysUsers", userList);
        mmap.put("collegeMajorData", buildCollegeMajorData());
        String role = resolveQueryRole();
        mmap.put("queryRole", role);
        if ("dept_teacher".equals(role)) {
            Long collegeId = resolveCollegeId(getSysUser());
            if (collegeId != null) {
                mmap.put("fixedCollegeId", String.valueOf(collegeId));
            }
        } else if ("research".equals(role)) {
            SysUser sysUser = getSysUser();
            if (sysUser != null && sysUser.getDeptId() != null) {
                mmap.put("fixedMajorId", String.valueOf(sysUser.getDeptId()));
            }
        }
    }
    
    /**
     * 构建学院专业数据，用于前端 cxSelect 组件
     * 格式：[{v: null, n: '学院', s: [{v: null, n: '专业'}]}, {v: '学院ID', n: '学院名称', s: [{v: '专业ID', n: '专业名称'}]}]
     */
    private List<Map<String, Object>> buildCollegeMajorData() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 添加默认选项
        Map<String, Object> defaultOption = new HashMap<>();
        defaultOption.put("v", null);
        defaultOption.put("n", "学院");
        List<Map<String, Object>> defaultSubOptions = new ArrayList<>();
        Map<String, Object> defaultSubOption = new HashMap<>();
        defaultSubOption.put("v", null);
        defaultSubOption.put("n", "专业");
        defaultSubOptions.add(defaultSubOption);
        defaultOption.put("s", defaultSubOptions);
        result.add(defaultOption);
        
        // 获取所有部门
        SysDept deptQuery = new SysDept();
        deptQuery.setStatus("0"); // 只获取正常状态的部门
        List<SysDept> allDepts = deptService.selectDeptList(deptQuery);
        
        // 找出所有学院（parent_id = 100 的部门）
        List<SysDept> colleges = allDepts.stream()
                .filter(dept -> dept.getParentId() != null && dept.getParentId().equals(100L))
                .sorted((d1, d2) -> {
                    if (d1.getOrderNum() != null && d2.getOrderNum() != null) {
                        return d1.getOrderNum().compareTo(d2.getOrderNum());
                    }
                    return d1.getDeptName().compareTo(d2.getDeptName());
                })
                .collect(Collectors.toList());
        
        // 为每个学院构建数据
        for (SysDept college : colleges) {
            Map<String, Object> collegeMap = new HashMap<>();
            collegeMap.put("v", college.getDeptId().toString());
            collegeMap.put("n", college.getDeptName());
            
            // 找出该学院下的所有专业（parent_id = 学院ID 的部门）
            List<Map<String, Object>> majors = new ArrayList<>();
            
            // 添加"空"选项
            Map<String, Object> emptyOption = new HashMap<>();
            emptyOption.put("v", "null");
            emptyOption.put("n", "空");
            majors.add(emptyOption);
            
            // 添加专业
            List<SysDept> collegeMajors = allDepts.stream()
                    .filter(dept -> dept.getParentId() != null && dept.getParentId().equals(college.getDeptId()))
                    .sorted((d1, d2) -> {
                        if (d1.getOrderNum() != null && d2.getOrderNum() != null) {
                            return d1.getOrderNum().compareTo(d2.getOrderNum());
                        }
                        return d1.getDeptName().compareTo(d2.getDeptName());
                    })
                    .collect(Collectors.toList());
            
            for (SysDept major : collegeMajors) {
                Map<String, Object> majorMap = new HashMap<>();
                majorMap.put("v", major.getDeptId().toString());
                majorMap.put("n", major.getDeptName());
                majors.add(majorMap);
            }
            
            collegeMap.put("s", majors);
            result.add(collegeMap);
        }
        
        return result;
    }

    /**
     * 查询统计列表（跨模块统一查询入口）
     *
     * @param params 查询参数
     * @return 分页结果
     */
    @Log(title = "统计查询", businessType = BusinessType.OTHER)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestParam Map<String, String> params) {
        return queryList(params, false);
    }

    /**
     * 导出统计列表
     *
     * @param params 查询参数
     * @return 导出结果
     */
    @Log(title = "统计导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(@RequestParam Map<String, String> params) {
        try {
            Map<String, String> queryParams = new HashMap<>(params);
            normalizeParams(queryParams);
            applyRoleScope(queryParams, false);

            String remark = queryParams.get("remark");
            if (remark == null || remark.isEmpty()) {
                return error("未指定导出模块或模块不存在");
            }

            if ("6".equals(remark) && queryParams.get("userId") != null && !queryParams.get("userId").isEmpty()) {
                List<SciZhuanliruanzhu> exportList = sciZhuanliruanzhuService.getStatsQueryToExcil(queryParams);
                exportList = exportList.stream().map(item -> {
                    item.setState(item.getStateDes());
                    return item;
                }).collect(Collectors.toList());
                ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<>(SciZhuanliruanzhu.class);
                return util.exportExcel(exportList, "专利软著数据");
            }

            List<?> exportList = queryByRemark(remark, queryParams, false);
            exportList = ((List<?>) exportList).stream().map(item -> {
                if (item instanceof SciHorizontalApplyVertical) {
                    ((SciHorizontalApplyVertical) item).setState(((SciHorizontalApplyVertical) item).getStateDes());
                } else if (item instanceof SciHorizontalApply) {
                    ((SciHorizontalApply) item).setState(((SciHorizontalApply) item).getStateDes());
                } else if (item instanceof SciIntraSchoolPro) {
                    ((SciIntraSchoolPro) item).setState(((SciIntraSchoolPro) item).getStateDes());
                } else if (item instanceof SciPaperA) {
                    ((SciPaperA) item).setState(((SciPaperA) item).getStateDes());
                } else if (item instanceof SciJiaocairuanzhu) {
                    ((SciJiaocairuanzhu) item).setState(((SciJiaocairuanzhu) item).getStateDes());
                } else if (item instanceof SciZhuanliruanzhu) {
                    ((SciZhuanliruanzhu) item).setState(((SciZhuanliruanzhu) item).getStateDes());
                } else if (item instanceof SysReward) {
                    ((SysReward) item).setState(((SysReward) item).getStateDes());
                } else if (item instanceof SciLectureReport) {
                    ((SciLectureReport) item).setState(((SciLectureReport) item).getStateDes());
                }
                return item;
            }).collect(Collectors.toList());

            if ("1".equals(remark)) {
                ExcelUtil<SciHorizontalApplyVertical> util = new ExcelUtil<>(SciHorizontalApplyVertical.class);
                return util.exportExcel((List<SciHorizontalApplyVertical>) exportList, "纵向课题数据");
            }
            if ("2".equals(remark)) {
                ExcelUtil<SciHorizontalApply> util = new ExcelUtil<>(SciHorizontalApply.class);
                return util.exportExcel((List<SciHorizontalApply>) exportList, "横向课题数据");
            }
            if ("3".equals(remark)) {
                ExcelUtil<SciIntraSchoolPro> util = new ExcelUtil<>(SciIntraSchoolPro.class);
                return util.exportExcel((List<SciIntraSchoolPro>) exportList, "成果转化数据");
            }
            if ("4".equals(remark)) {
                ExcelUtil<SciPaperA> util = new ExcelUtil<>(SciPaperA.class);
                return util.exportExcel((List<SciPaperA>) exportList, "论文数据");
            }
            if ("5".equals(remark)) {
                ExcelUtil<SciJiaocairuanzhu> util = new ExcelUtil<>(SciJiaocairuanzhu.class);
                return util.exportExcel((List<SciJiaocairuanzhu>) exportList, "教材专著数据");
            }
            if ("6".equals(remark)) {
                ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<>(SciZhuanliruanzhu.class);
                return util.exportExcel((List<SciZhuanliruanzhu>) exportList, "专利软著数据");
            }
            if ("7".equals(remark)) {
                ExcelUtil<SysReward> util = new ExcelUtil<>(SysReward.class);
                return util.exportExcel((List<SysReward>) exportList, "奖励数据");
            }
            if ("8".equals(remark)) {
                ExcelUtil<SciLectureReport> util = new ExcelUtil<>(SciLectureReport.class);
                return util.exportExcel((List<SciLectureReport>) exportList, "讲座报告数据");
            }
            return error("未指定导出模块或模块不存在");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 查询核算列表（跨模块统一查询入口）
     *
     * @param params 查询参数
     * @param year 年度
     * @return 分页结果
     */
    @Log(title = "核算查询", businessType = BusinessType.OTHER)
    @PostMapping("/listToCheck")
    @ResponseBody
    public TableDataInfo listToCheck(@RequestParam Map<String, String> params, String year) {
        Map<String, String> queryParams = new HashMap<>(params);
        if (year != null && !year.isEmpty()) {
            queryParams.put("year", year);
        }
        return queryList(queryParams, true);
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
            Map<String, String> queryParams = new HashMap<>(params);
            normalizeParams(queryParams);
            applyRoleScope(queryParams, true);
            
            // 根据项目类别返回不同的数据
            if (remark != null && !remark.isEmpty()) {
                if ("1".equals(remark)) {
                    // 获取项目类别1的数据（纵向课题）导出
                    List<SciHorizontalApplyVertical> exportList = sciHorizontalApplyVerticalService.getStatsQueryToCheck(queryParams);

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
                    List<SciHorizontalApply> exportList = sciHorizontalApplyService.getStatsQueryToCheck(queryParams);

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
                    List<SciIntraSchoolPro> exportList = sciIntraSchProApplyService.getStatsQueryToCheck(queryParams);

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
                    List<SciPaperA> exportList = sciPaperAService.getStatsQueryToCheck(queryParams);
                    
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
                    List<SciJiaocairuanzhu> exportList = sciJiaocairuanzhuService.getStatsQueryToCheck(queryParams);
                    
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
                    List<SciZhuanliruanzhu> exportList = sciZhuanliruanzhuService.getStatsQueryToCheck(queryParams);
                    
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
                    List<SysReward> exportList = sysRewardService.getStatsQueryToCheck(queryParams);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SysReward> util = new ExcelUtil<SysReward>(SysReward.class);
                    return util.exportExcel(exportList, "奖励核算数据");
                }
                else if ("8".equals(remark)) {
                    // 获取项目类别8的数据（讲座报告）导出 SciLectureReport
                    List<SciLectureReport> exportList = sciLectureReportService.getStatsQueryToCheck(queryParams);
                    exportList = exportList.stream().map(item -> {
                        item.setState(item.getStateDes());
                        return item;
                    }).collect(Collectors.toList());
                    ExcelUtil<SciLectureReport> util = new ExcelUtil<SciLectureReport>(SciLectureReport.class);
                    return util.exportExcel(exportList, "讲座报告核算数据");
                }
            }
            return error("未指定导出模块或模块不存在");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private TableDataInfo queryList(Map<String, String> params, boolean checkMode) {
        if ("true".equals(params.get("noData"))) {
            return getDataTable(new ArrayList<>());
        }

        Map<String, String> queryParams = new HashMap<>(params);
        normalizeParams(queryParams);
        applyRoleScope(queryParams, checkMode);
        startPageFromParams(queryParams);

        String remark = queryParams.get("remark");
        if (remark == null || remark.isEmpty()) {
            return getDataTable(new ArrayList<>());
        }

        return getDataTable(queryByRemark(remark, queryParams, checkMode));
    }

    private List<?> queryByRemark(String remark, Map<String, String> params, boolean checkMode) {
        if ("1".equals(remark)) {
            return checkMode ? sciHorizontalApplyVerticalService.getStatsQueryToCheck(params) : sciHorizontalApplyVerticalService.getStatsQuery(params);
        }
        if ("2".equals(remark)) {
            return checkMode ? sciHorizontalApplyService.getStatsQueryToCheck(params) : sciHorizontalApplyService.getStatsQuery(params);
        }
        if ("3".equals(remark)) {
            return checkMode ? sciIntraSchProApplyService.getStatsQueryToCheck(params) : sciIntraSchProApplyService.getStatsQuery(params);
        }
        if ("4".equals(remark)) {
            return checkMode ? sciPaperAService.getStatsQueryToCheck(params) : sciPaperAService.getStatsQuery(params);
        }
        if ("5".equals(remark)) {
            return checkMode ? sciJiaocairuanzhuService.getStatsQueryToCheck(params) : sciJiaocairuanzhuService.getStatsQuery(params);
        }
        if ("6".equals(remark)) {
            return checkMode ? sciZhuanliruanzhuService.getStatsQueryToCheck(params) : sciZhuanliruanzhuService.getStatsQuery(params);
        }
        if ("7".equals(remark)) {
            return checkMode ? sysRewardService.getStatsQueryToCheck(params) : sysRewardService.getStatsQuery(params);
        }
        if ("8".equals(remark)) {
            return checkMode ? sciLectureReportService.getStatsQueryToCheck(params) : sciLectureReportService.getStatsQuery(params);
        }
        return new ArrayList<>();
    }

    private void normalizeParams(Map<String, String> params) {
        params.put("uid", String.valueOf(getUserId()));
        if (params.get("jobTitle") == null && params.get("job_title") != null) {
            params.put("jobTitle", params.get("job_title"));
        }
    }

    private void applyRoleScope(Map<String, String> params, boolean checkMode) {
        String role = resolveQueryRole();
        SysUser sysUser = getSysUser();

        if ("teacher".equals(role)) {
            params.put("userId", String.valueOf(getUserId()));
            return;
        }

        if ("research".equals(role)) {
            if (sysUser.getDeptId() != null) {
                params.put("major", String.valueOf(sysUser.getDeptId()));
            }
            return;
        }

        if ("dept_teacher".equals(role)) {
            Long collegeId = resolveCollegeId(sysUser);
            if (collegeId != null) {
                params.put("college", String.valueOf(collegeId));
                params.put("Pcollege", String.valueOf(collegeId));
            }
        }
    }

    private Long resolveCollegeId(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        if (sysUser.getParentId() != null && !Objects.equals(sysUser.getParentId(), 100L)) {
            return sysUser.getParentId();
        }
        if (sysUser.getDeptId() != null) {
            return sysUser.getDeptId();
        }
        return null;
    }

    private void startPageFromParams(Map<String, String> params) {
        String offsetStr = params.get("offset");
        String limitStr = params.get("limit");
        if (offsetStr != null && limitStr != null) {
            int offset = Integer.parseInt(offsetStr);
            int limit = Integer.parseInt(limitStr);
            int pageNum = offset / limit + 1;
            PageHelper.startPage(pageNum, limit);
            return;
        }
        startPage();
    }

    /**
     * 判断当前登陆用户的身份
     * dept_teacher，sci_tesearch，research，admin，teacher
     *
     * @return
     */
    private String resolveQueryRole() {
        List<SysRole> roles = getSysUser().getRoles();
        if (roles == null || roles.isEmpty()) {
            return "teacher";
        }

        boolean isAdmin = false;
        boolean isTeacher = false;
        boolean isSciResearch = false;
        boolean isResearchRoom = false;
        boolean isCollegeLeader = false;

        for (SysRole r : roles) {
            if (r == null || r.getRoleId() == null) {
                continue;
            }
            if (Objects.equals(r.getRoleId(), 1L)) {
                isAdmin = true;
            }
            if (Objects.equals(r.getRoleId(), 100L)) {
                isTeacher = true;
            }
            if (Objects.equals(r.getRoleId(), 101L)) {
                isSciResearch = true;
            }
            if (Objects.equals(r.getRoleId(), 102L)) {
                isResearchRoom = true;
            }
            if (Objects.equals(r.getRoleId(), 103L)
                    || Objects.equals(r.getRoleId(), 104L)
                    || Objects.equals(r.getRoleId(), 105L)
                    || Objects.equals(r.getRoleId(), 106L)
                    || Objects.equals(r.getRoleId(), 107L)
                    || Objects.equals(r.getRoleId(), 108L)) {
                isCollegeLeader = true;
            }
        }

        if (isCollegeLeader) {
            return "dept_teacher";
        }
        if (isSciResearch) {
            return "sci_tesearch";
        }
        if (isResearchRoom) {
            return "research";
        }
        if (isAdmin) {
            return "admin";
        }
        if (isTeacher && roles.size() == 1) {
            return "teacher";
        }
        return "teacher";
    }



}
