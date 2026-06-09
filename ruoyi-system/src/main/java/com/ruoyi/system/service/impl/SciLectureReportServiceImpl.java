package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.common.utils.DataScopeUtils;
import org.apache.shiro.SecurityUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciLectureReportIntegralMapper;
import com.ruoyi.system.mapper.SciLectureReportOpinionMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.system.mapper.SciLectureReportMapper;
import com.ruoyi.system.service.ISciLectureReportService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 讲座报告Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
@Service
public class SciLectureReportServiceImpl implements ISciLectureReportService {
    private static final Logger log = LoggerFactory.getLogger(SciLectureReportServiceImpl.class);

    @Autowired
    private SciLectureReportMapper sciLectureReportMapper;
    @Autowired
    private SciLectureReportOpinionMapper opinionMapper;
    // 讲座报告积分的Mapper接口
    @Autowired
    private SciLectureReportIntegralMapper reportIntegralMapper;
    // 流程管理服务
    @Autowired
    private IApprovalProcessService approvalProcessService;

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

    @Autowired
    private IPageRenderService pageRenderService;

    /**
     * 查询讲座报告
     * 
     * @param id 讲座报告主键
     * @return 讲座报告
     */
    @Override
    public SciLectureReport selectSciLectureReportById(Integer id) {
        SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
        fillPageRenderData(report);
        return report;
    }

    /**
     * 查询讲座报告列表
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportList(SciLectureReport sciLectureReport) {
        List<SciLectureReport> list = sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
        fillPageRenderData(list);
        return list;
    }

    /**
     * 查询讲座报告列表（导出用）
     *
     * @param ids 需要导出的讲座 报告id集合
     * @return 讲座报告
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListByIds(String ids) {
        return sciLectureReportMapper.selectSciLectureReportListByIds(Convert.toStrArray(ids));
    }

    // 教研室查询讲座报告列表
    // @Override
    // public List<SciLectureReport> selectSciLectureReportListJYS(SciLectureReport
    // sciLectureReport) {
    // return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    // }

    // 结项申请保存
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int SciLectureReportOverAdd(SciLectureReport sciLectureReport) {
        try {
            // 1. 获取当前用户信息
            SysUser currentUser = ShiroUtils.getSysUser();

            // 2. 使用流程管理服务提交结项审批
            ApprovalRequest submitRequest = ApprovalRequest.of("LECTURE_APPROVAL",
                    sciLectureReport.getId().longValue(), sciLectureReport.getState(),
                    "提交结项申请", currentUser.getUserId(), currentUser.getUserName(),
                    currentUser.getDept().getDeptName());

            ApprovalResult result = approvalProcessService.submitApproval(submitRequest);

            if (result != null && result.isSuccess()) {
                String newState = result.getNewState();
                // 5. 设置新状态
                sciLectureReport.setState(newState);
                // 6. 保存结项申请
                int updateResult = sciLectureReportMapper.SciLectureReportOverAdd(sciLectureReport);
                if (updateResult > 0) {
                    // 记录审批意见
                    SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                    sciLectureReportOpinion.setUid(currentUser.getUserId());
                    sciLectureReportOpinion.setBaogaoId(sciLectureReport.getId());
                    sciLectureReportOpinion.setConcate("提交");
                    sciLectureReportOpinion.setState("提交");
                    opinionMapper.opinionadd(sciLectureReportOpinion);
                    return updateResult;
                }
            }
        } catch (Exception e) {
            log.error("讲座报告结项申请异常", e);
            throw e; // 抛出异常，触发事务回滚
        }
        return 0;
    }

    // @Override
    // @DataScope(deptAlias = "d", userAlias = "u")
    // public List<SciLectureReport>
    // selectSciLectureReportListByKYS(SciLectureReport sciLectureReport) {
    // return sciLectureReportMapper.selectSciLectureReportListBy(sciLectureReport);
    // }

    /**
     * 新增讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSciLectureReport(SciLectureReport sciLectureReport) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 设置时间格式
            LocalDateTime endTime = LocalDateTime.parse(sciLectureReport.getReportTime(), formatter)
                    .plusMinutes(sciLectureReport.getReportDuration()); // 计算结束时间
            sciLectureReport.setReportEndTime(endTime.format(formatter));
        } catch (DateTimeParseException e) {
            System.err.println("时间格式错误！请使用 yyyy-MM-dd HH:mm 格式。");
        }

        // 设置默认状态为草稿
        if (sciLectureReport.getState() == null || sciLectureReport.getState().isEmpty()) {
            sciLectureReport.setState("LECTURE_DRAFT");
        }

        // 确保userId不为空
        if (sciLectureReport.getUserId() == null) {
            SysUser currentUser = ShiroUtils.getSysUser();
            if (currentUser != null) {
                sciLectureReport.setUserId(currentUser.getUserId().intValue());
            }
        }

        // 根据讲座报告分类同步积分
        syncLectureReportIntegral(sciLectureReport);

        int number = sciLectureReportMapper.insertSciLectureReport(sciLectureReport);

        // 检查是否成功插入并获取到ID
        if (number > 0 && sciLectureReport.getId() != null) {
            SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
            // 获取当前用户id并将数据类型从Integer转换为Long在给sciLectureReportOpinion.setUid()
            Integer userId = sciLectureReport.getUserId();
            sciLectureReportOpinion.setUid(userId != null ? userId.longValue() : null);
            // 新增报告的id
            sciLectureReportOpinion.setBaogaoId(sciLectureReport.getId());
            sciLectureReportOpinion.setConcate("新增");
            sciLectureReportOpinion.setState("新增");
            // 将批阅记录插入数据库
            opinionMapper.opinionadd(sciLectureReportOpinion);
        }
        return number;
    }

    /**
     * 新增讲座报告场地校验
     *
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    @Override
    @Transactional
    public HashMap<String, Object> checkConflict(SciLectureReport sciLectureReport) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 设置时间格式
            LocalDateTime endTime = LocalDateTime.parse(sciLectureReport.getReportTime(), formatter)
                    .plusMinutes(sciLectureReport.getReportDuration()); // 计算结束时间
            sciLectureReport.setReportEndTime(endTime.format(formatter));
            int number = sciLectureReportMapper.checkConflict(sciLectureReport); // 进行数据比对
            if (number > 0) {
                map.put("conflict", true);
                map.put("message", "有冲突");
            } else {
                map.put("conflict", false);
                map.put("message", "无冲突");
            }
        } catch (DateTimeParseException e) {
            map.put("conflict", true);
            map.put("message", "时间格式错误！请使用 yyyy-MM-dd HH:mm 格式。");
        }
        return map;
    }

    /**
     * 修改讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSciLectureReport(SciLectureReport sciLectureReport) {
        // 根据讲座报告分类同步积分
        syncLectureReportIntegral(sciLectureReport);

        int number = sciLectureReportMapper.updateSciLectureReport(sciLectureReport);
        if (sciLectureReport.getUrlFlag().equals("gengxin")) {
            SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
            // 操作用户id
            // 获取当前用户id并将数据类型从Integer转换为Long在给sciLectureReportOpinion.setUid()
            Integer userId = sciLectureReport.getUserId();
            sciLectureReportOpinion.setUid(userId != null ? userId.longValue() : null);
            // 被修改的报告的id
            sciLectureReportOpinion.setBaogaoId(sciLectureReport.getId());
            sciLectureReportOpinion.setConcate("修改");
            sciLectureReportOpinion.setState("修改");
            opinionMapper.opinionadd(sciLectureReportOpinion);
        }

        return number;
    }

    /**
     * 批量删除讲座报告
     * 
     * @param ids 需要删除的讲座报告主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSciLectureReportByIds(String ids) {
        sciLectureReportMapper.deleteSciLectureReportOpinionBy(Convert.toStrArray(ids));
        return sciLectureReportMapper.deleteSciLectureReportByIds(Convert.toStrArray(ids));
    }

    /**
     * 提交状态为草稿的讲座报告
     *
     * @param ids 需要提交的讲座报告主键
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSciLectureReportByIds(String ids) {
        if (ids == null || ids.trim().isEmpty()) {
            return 0;
        }

        String[] idArray = ids.split(",");

        if (idArray.length == 1) {
            try {
                Integer id = Integer.valueOf(idArray[0].trim());
                SysUser currentUser = ShiroUtils.getSysUser();

                SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
                if (report == null) {
                    return 0;
                }

                ApprovalRequest submitRequest = ApprovalRequest.of("LECTURE_APPROVAL",
                        id.longValue(), report.getState(),
                        "数据所有者提交", currentUser.getUserId(), currentUser.getUserName(),
                        currentUser.getDept().getDeptName());

                ApprovalResult result = approvalProcessService.submitApproval(submitRequest);

                if (result != null && result.isSuccess()) {
                    // 更新讲座报告状态
                    int updateResult = sciLectureReportMapper.criticism(id, result.getNewState());
                    if (updateResult > 0) {
                        SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                        sciLectureReportOpinion.setUid(currentUser.getUserId());
                        sciLectureReportOpinion.setBaogaoId(id);
                        sciLectureReportOpinion.setConcate("提交");
                        sciLectureReportOpinion.setState("提交");
                        opinionMapper.opinionadd(sciLectureReportOpinion);

                        return updateResult;
                    }
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 删除讲座报告信息
     * 
     * @param id 讲座报告主键
     * @return 结果
     */
    @Override
    public int deleteSciLectureReportById(Integer id) {
        return sciLectureReportMapper.deleteSciLectureReportById(id);
    }

    // 通过批阅修改讲座报告的状态，并将批阅意见插入数据库
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int criticism(Integer rid, Long userId, String remark, String urlFlag) {
        try {
            SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(rid);
            if (report == null) {
                return 0;
            }

            SysUser currentUser = ShiroUtils.getSysUser();

            // 调用封装好的approve方法处理审批通过
            ApprovalRequest approveRequest = ApprovalRequest.of("LECTURE_APPROVAL",
                    rid.longValue(), report.getState(),
                    remark != null && !remark.isEmpty() ? remark : "通过",
                    userId, currentUser.getUserName(),
                    currentUser.getDept().getDeptName());

            ApprovalResult result = approvalProcessService.approve(approveRequest);

            if (result != null && result.isSuccess()) {
                String newState = result.getNewState();

                // 更新讲座报告状态
                int updateResult = sciLectureReportMapper.criticism(rid, newState);
                if (updateResult > 0) {
                    // 记录审批意见
                    SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                    sciLectureReportOpinion.setUid(userId);
                    sciLectureReportOpinion.setBaogaoId(rid);
                    if (remark == null || remark.equals("")) {
                        remark = "通过";
                    }
                    sciLectureReportOpinion.setConcate(remark);
                    sciLectureReportOpinion.setState("通过");
                    opinionMapper.opinionadd(sciLectureReportOpinion);

                    // 计算科研分（如果需要）
                    if (newState != null && newState.equals("LECTURE_PASSED")) {
                        if (report.getReportClassify() != null && !report.getReportClassify().isEmpty()) {
                            Integer classifyId = Integer.parseInt(report.getReportClassify());
                            SciLectureReportIntegral sciLectureReportIntegral = reportIntegralMapper
                                    .selectSciLectureReportIntegralById(classifyId);
                            if (sciLectureReportIntegral != null && sciLectureReportIntegral.getIntegral() != null) {
                                sciLectureReportMapper.reportKeyanfen(rid,
                                        sciLectureReportIntegral.getIntegral());
                            }
                        }
                    }

                    return updateResult;
                }
            }
        } catch (Exception e) {
            log.error("讲座报告审批通过异常", e);
            throw e; // 抛出异常，触发事务回滚
        }
        return 0;
    }

    // 驳回业务处理方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Integer id, Long userId, String remark, String urlFlag) {
        // 校验驳回原因不能为空
        if (remark == null || remark.trim().isEmpty()) {
            throw new IllegalArgumentException("驳回原因不能为空");
        }

        SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
        if (report == null) {
            return 0;
        }

        SysUser currentUser = ShiroUtils.getSysUser();

        // 调用共有方法处理审批驳回（记录审批历史）
        ApprovalRequest rejectRequest = ApprovalRequest.of("LECTURE_APPROVAL",
                id.longValue(), report.getState(),
                remark, userId, currentUser.getUserName(),
                currentUser.getDept().getDeptName());

        approvalProcessService.reject(rejectRequest);

        // 强制驳回状态为 LECTURE_REJECTED，作者可编辑后重新提交
        return handleApprovalResult(id, userId, "LECTURE_REJECTED", remark, "驳回");
    }

    // 撤回审批业务处理方法 - 调用共有方法，通过状态撤回回到科研处审批
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recall(Integer id, Long userId, String remark) {
        SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
        if (report == null) {
            return 0;
        }

        String currentState = report.getState();
        SysUser currentUser = ShiroUtils.getSysUser();

        // 调用共有方法处理审批撤回（记录审批历史）
        ApprovalRequest recallRequest = ApprovalRequest.of("LECTURE_APPROVAL",
                id.longValue(), currentState,
                remark != null && !remark.isEmpty() ? remark : "撤回审批",
                userId, currentUser.getUserName(),
                currentUser.getDept().getDeptName());

        approvalProcessService.recall(recallRequest);

        // 撤回状态逻辑：根据当前状态决定撤回后的目标状态
        String newState = currentState;
        if ("LECTURE_JYS_AUDIT".equals(currentState)) {
            // 教研室审批中撤回回到草稿
            newState = "LECTURE_DRAFT";
        } else if ("LECTURE_KYC_AUDIT".equals(currentState)) {
            // 科研处审批中撤回回到教研室审批中
            newState = "LECTURE_JYS_AUDIT";
        } else if ("LECTURE_PASSED".equals(currentState)) {
            // 通过状态撤回回到科研处审批中
            newState = "LECTURE_KYC_AUDIT";
        } else if ("LECTURE_REJECTED".equals(currentState)) {
            // 驳回状态撤回：从审批历史中查询最近一次驳回记录，获取驳回前的状态
            SysApprovalHistory lastReject = sysApprovalHistoryService.selectLastRejectByBusinessId(
                    "LECTURE_APPROVAL", id.longValue());
            if (lastReject != null && StringUtils.isNotEmpty(lastReject.getOldState())) {
                // 回退到驳回前的状态
                newState = lastReject.getOldState();
            } else {
                // 无历史记录时回退到草稿
                newState = "LECTURE_DRAFT";
            }
        }

        // 记录审批意见
        SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
        sciLectureReportOpinion.setUid(userId);
        sciLectureReportOpinion.setBaogaoId(id);
        sciLectureReportOpinion.setConcate(remark != null && !remark.isEmpty() ? remark : "撤回");
        sciLectureReportOpinion.setState("撤回");
        opinionMapper.opinionadd(sciLectureReportOpinion);

        // 更新状态到科研处审批
        return sciLectureReportMapper.criticism(id, newState);
    }

    /**
     * 统一处理审批结果
     * 
     * @param id 业务ID
     * @param userId 用户ID
     * @param newState 新状态
     * @param remark 审批意见
     * @param operationType 操作类型（驳回/撤回）
     * @return 更新结果
     */
    private int handleApprovalResult(Integer id, Long userId, String newState, String remark, String operationType) {
        // 更新讲座报告状态
        int updateResult = sciLectureReportMapper.criticism(id, newState);
        if (updateResult > 0) {
            // 记录审批意见
            SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
            sciLectureReportOpinion.setUid(userId);
            sciLectureReportOpinion.setBaogaoId(id);
            sciLectureReportOpinion.setConcate(remark);
            sciLectureReportOpinion.setState(operationType);
            opinionMapper.opinionadd(sciLectureReportOpinion);

            // 处理科研分（驳回时清零）
            if ("驳回".equals(operationType) && "LECTURE_REJECTED".equals(newState)) {
                sciLectureReportMapper.reportKeyanfen(id, "0");
            }
        }
        return updateResult;
    }

    @Override
    public List<SciLectureReport> getStatsQuery(Map<String, String> params) {
        return sciLectureReportMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciLectureReportMapper.getStatsQueryToCheck(params);
    }

    /**
     * 同步讲座报告积分
     * 根据讲座报告的分类，从积分管理表中获取对应的积分值并设置到讲座报告中
     * 
     * @param sciLectureReport 讲座报告对象
     */
    private void syncLectureReportIntegral(SciLectureReport sciLectureReport) {
        if (sciLectureReport.getReportClassify() != null && !sciLectureReport.getReportClassify().isEmpty()) {
            try {
                // 将分类字符串转换为整数
                Integer classifyId = Integer.parseInt(sciLectureReport.getReportClassify());

                // 根据分类ID查询对应的积分
                SciLectureReportIntegral integral = reportIntegralMapper.selectSciLectureReportIntegralById(classifyId);

                if (integral != null && integral.getIntegral() != null) {
                    // 设置积分值到讲座报告
                    sciLectureReport.setReportKeyanfen(integral.getIntegral());
                    // 同时设置积分值字段
                    sciLectureReport.setIntegralValue(integral.getIntegral());
                    // 设置分类名称
                    sciLectureReport.setClassificationName(integral.getClassification());
                } else {
                    // 如果没有找到对应的积分，设置为0
                    sciLectureReport.setReportKeyanfen("0");
                    sciLectureReport.setIntegralValue("0");
                    sciLectureReport.setClassificationName("未知分类");
                }
            } catch (NumberFormatException e) {
                // 如果分类ID不是有效的数字，设置为0
                sciLectureReport.setReportKeyanfen("0");
                sciLectureReport.setIntegralValue("0");
                sciLectureReport.setClassificationName("无效分类");
            }
        } else {
            // 如果没有设置分类，设置为0
            sciLectureReport.setReportKeyanfen("0");
            sciLectureReport.setIntegralValue("0");
            sciLectureReport.setClassificationName("未选择分类");
        }
    }

    /**
     * 统一查询讲座报告列表
     * 通过@DataScope控制数据权限，移除状态过滤
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListAll(SciLectureReport sciLectureReport) {
        List<SciLectureReport> list = sciLectureReportMapper.selectSciLectureReportListAll(sciLectureReport);
        fillPageRenderData(list);
        return list;
    }
    
    /**
     * 统一填充页面渲染数据
     * 调用pageRenderService.fillPageRenderData方法实现操作列渲染
     * 
     * @param report 单个讲座报告对象
     */
    private void fillPageRenderData(SciLectureReport report) {
        PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                "LECTURE", "system:report", "LECTURE_APPROVAL",
                report.getState(),
                report.getId() != null ? report.getId().longValue() : null,
                report.getUserId() != null ? report.getUserId().longValue() : null,
                null);
        
        // 获取当前用户ID
        Long currentUserId = null;
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUserId();
        }
        
        // 调整按钮：处理驳回状态、撤回状态的按钮显示逻辑
        List<PageRenderActionItem> actions = adjustActionsForLecture(
                report.getState(), 
                result.getActions(), 
                report.getUserId(),
                currentUserId);
        
        // 设置个人排名：如果当前用户是申请人，则排名为"1"
        if (report.getUserId() != null && currentUserId != null && 
            report.getUserId().longValue() == currentUserId) {
            report.setPersonalRank("1");
        }
        
        report.setStatusMeta(result.getStatusMeta());
        report.setActions(actions);
    }
    
    /**
     * 统一填充页面渲染数据（批量）
     * 调用pageRenderService.fillPageRenderData方法实现操作列渲染
     * 
     * @param list 讲座报告列表
     */
    private void fillPageRenderData(List<SciLectureReport> list) {
        for (SciLectureReport report : list) {
            fillPageRenderData(report);
        }
    }
    
    /**
     * 构建当前用户的权限集合
     * 超级管理员权限处理：由于PageRenderServiceImpl的按钮渲染依赖权限检查，
     * 而selectPermsByUserId方法没有特殊处理超级管理员，导致管理员权限列表可能为空，
     * 因此需要手动添加讲座报告模块的所有权限，确保超级管理员能看到所有操作按钮
     * 
     * @param currentUser 当前用户
     * @return 权限集合
     */
    private Set<String> buildPermissions(SysUser currentUser) {
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        
        if (currentUser != null && currentUser.isAdmin()) {
            permissions.add("system:report:info");
            permissions.add("system:report:edit");
            permissions.add("system:report:add");
            permissions.add("system:report:remove");
            permissions.add("system:report:process");
            permissions.add("system:report:check");
            permissions.add("system:report:revoke");
            permissions.add("system:report:kyrevoke");
            permissions.add("system:report:xyprocess");
        }
        
        return permissions;
    }
    
    /**
     * 调整讲座报告按钮：确保正确显示撤回、驳回和提交按钮
     * 逻辑：
     * - 教研室审批中（LECTURE_JYS_AUDIT）：教研室角色可驳回，无撤回按钮
     * - 科研处审批中（LECTURE_KYC_AUDIT）：科研处可驳回，无撤回按钮
     * - 通过状态（LECTURE_PASSED）：科研处角色或管理员可撤回
     * - 驳回状态（LECTURE_REJECTED）：教研室和科研处角色都能撤回，作者可编辑
     * 
     * @param state 当前状态
     * @param actions 已有按钮列表
     * @param creatorId 业务数据创建者ID
     * @param currentUserId 当前登录用户ID
     * @return 调整后的按钮列表
     */
    private List<PageRenderActionItem> adjustActionsForLecture(String state, List<PageRenderActionItem> actions,
            Integer creatorId, Long currentUserId) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        
        // 判断当前用户是否为创建者
        boolean isCreator = creatorId != null && currentUserId != null && creatorId.longValue() == currentUserId;
        // 判断是否为管理员
        boolean isAdmin = SecurityUtils.getSubject().isPermitted("*:*:*");
        
        // 移除共有方法可能添加的撤回按钮，统一在本方法中处理
        actions.removeIf(a -> "recall".equals(a.getActionKey()));
        
        // 权限检查
        boolean hasProcess = SecurityUtils.getSubject().isPermitted("system:report:process"); // 教研室审批权限
        boolean hasCheck = SecurityUtils.getSubject().isPermitted("system:report:check");     // 科研处审批权限
        boolean hasKyRevoke = SecurityUtils.getSubject().isPermitted("system:report:kyrevoke"); // 科研处撤回权限
        
        // 教研室审批中：教研室角色可驳回
        if ("LECTURE_JYS_AUDIT".equals(state)) {
            // 确保显示驳回按钮（教研室）
            boolean hasReject = actions.stream().anyMatch(a -> "reject".equals(a.getActionKey()));
            if (!hasReject && hasProcess) {
                actions.add(PageRenderActionItem.of(
                        "reject", "驳回",
                        PageRenderColorConstants.COLOR_DANGER, 32, "确定要驳回该记录吗？"));
            }
            // 不显示撤回按钮
        }
        
        // 科研处审批中：科研处可驳回
        if ("LECTURE_KYC_AUDIT".equals(state)) {
            // 确保显示驳回按钮（科研处）
            boolean hasReject = actions.stream().anyMatch(a -> "reject".equals(a.getActionKey()));
            if (!hasReject && hasCheck) {
                actions.add(PageRenderActionItem.of(
                        "reject", "驳回",
                        PageRenderColorConstants.COLOR_DANGER, 32, "确定要驳回该记录吗？"));
            }
            // 不显示撤回按钮
        }
        
        // 通过状态：科研处角色或管理员可撤回
        if ("LECTURE_PASSED".equals(state)) {
            boolean hasRecall = actions.stream().anyMatch(a -> "recall".equals(a.getActionKey()));
            if (!hasRecall && (hasCheck || hasKyRevoke || isAdmin)) {
                actions.add(PageRenderActionItem.of(
                        "recall", "撤回",
                        PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
            }
        }
        
        // 驳回状态：教研室和科研处角色都能撤回，作者可编辑
        if ("LECTURE_REJECTED".equals(state)) {
            // 教研室和科研处角色都可撤回
            boolean hasRecall = actions.stream().anyMatch(a -> "recall".equals(a.getActionKey()));
            if (!hasRecall && (hasProcess || hasCheck || hasKyRevoke || isAdmin)) {
                actions.add(PageRenderActionItem.of(
                        "recall", "撤回",
                        PageRenderColorConstants.COLOR_WARNING, 40, "确定要撤回该记录吗？"));
            }
            // 确保显示编辑按钮（只有作者或管理员）
            boolean hasEdit = actions.stream().anyMatch(a -> "edit".equals(a.getActionKey()));
            if (!hasEdit && (isCreator || isAdmin) && SecurityUtils.getSubject().isPermitted("system:report:edit")) {
                actions.add(PageRenderActionItem.of(
                        "edit", "编辑",
                        PageRenderColorConstants.COLOR_PRIMARY, 10));
            }
            // 移除可能存在的提交按钮
            actions.removeIf(a -> "submit".equals(a.getActionKey()));
        }
        
        // 按排序号排序
        Collections.sort(actions);
        
        return actions;
    }
}
