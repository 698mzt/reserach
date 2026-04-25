package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.domain.SciLectureReportOpinion;
import com.ruoyi.system.mapper.SciLectureReportIntegralMapper;
import com.ruoyi.system.mapper.SciLectureReportOpinionMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciLectureReportMapper;
import com.ruoyi.system.domain.SciLectureReport;
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
public class SciLectureReportServiceImpl implements ISciLectureReportService 
{
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

    /**
     * 查询讲座报告
     * 
     * @param id 讲座报告主键
     * @return 讲座报告
     */
    @Override
    public SciLectureReport selectSciLectureReportById(Integer id)
    {
        return sciLectureReportMapper.selectSciLectureReportById(id);
    }

    /**
     * 查询讲座报告列表
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportList(SciLectureReport sciLectureReport)
    {
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }

    /**
     * 查询讲座报告列表（导出用）
     *
     * @param ids 需要导出的讲座 报告id集合
     * @return 讲座报告
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListByIds(String ids)
    {
        return sciLectureReportMapper.selectSciLectureReportListByIds(Convert.toStrArray(ids));
    }


    // 教研室查询讲座报告列表
//    @Override
//    public List<SciLectureReport> selectSciLectureReportListJYS(SciLectureReport sciLectureReport) {
//        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
//    }




    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListJYS_Tab0(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }
    // 教研室管理员的项目申请Tab页查询讲座报告列表
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListJYS_Tab1(SciLectureReport sciLectureReport) {
//        List<SciLectureReport> sciLectureReportList = sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
//        List<SciLectureReport> list = new ArrayList<>();
//        for (SciLectureReport sciLectureReport1 : sciLectureReportList) {
////            sciLectureReport1.getUserId()!=(sciLectureReport.getUid())
//            // 当前状态为2，并且不是当前用户的项目，则不显示
//            if (sciLectureReport1.getState().equals("2") && !Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
//                continue;
//            }
//            // 当前状态为4，并且是当前用户的项目，则不显示
//            if (sciLectureReport1.getState().equals("4") && Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
//                continue;
//            }
//            list.add(sciLectureReport1);
//        }
//        return list;
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListJYS_Tab2(SciLectureReport sciLectureReport) {
//        List<SciLectureReport> sciLectureReportList = sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
//        List<SciLectureReport> list = new ArrayList<>();
//        for (SciLectureReport sciLectureReport1 : sciLectureReportList) {
////            sciLectureReport1.getUserId()!=(sciLectureReport.getUid())
//            // 当前状态为4，并且不是当前用户的项目，则不显示
//            if (sciLectureReport1.getState().equals("4") && !Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
//                continue;
//            }
//            list.add(sciLectureReport1);
//        }
//        return list;
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }



    // 科研室项目申请tab页讲座报告数据查询
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListKYS_Tab1(SciLectureReport sciLectureReport) {
//        List<SciLectureReport> sciLectureReportList = sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
//        List<SciLectureReport> list = new ArrayList<>();
//        for (SciLectureReport sciLectureReport1 : sciLectureReportList) {
////            sciLectureReport1.getUserId()!=(sciLectureReport.getUid())
//            // 当前状态为2，并且不是当前用户的项目，则不显示
////            if (sciLectureReport1.getState().equals("2") && !Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
////                continue;
////            }
//            // 当前状态为4，并且是当前用户的项目，则不显示
//            if (sciLectureReport1.getState().equals("4") && Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
//                continue;
//            }
//            list.add(sciLectureReport1);
//        }
//        return sciLectureReportList;
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }


    // 结项申请tab页
    @Override
    public List<SciLectureReport> selectSciLectureReportListKYS_Tab2(SciLectureReport sciLectureReport) {
//        List<SciLectureReport> sciLectureReportList = sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
//        List<SciLectureReport> list = new ArrayList<>();
//        for (SciLectureReport sciLectureReport1 : sciLectureReportList) {
////            sciLectureReport1.getUserId()!=(sciLectureReport.getUid())
//            // 当前状态为4，并且不是当前用户的项目，则不显示
//            if (sciLectureReport1.getState().equals("4") && !Objects.equals(sciLectureReport1.getUserId() != null ? sciLectureReport1.getUserId().longValue() : null, sciLectureReport.getUid())){
//                continue;
//            }
//            list.add(sciLectureReport1);
//        }
//        return list;
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }

    // 结项申请保存
    @Override
    public int SciLectureReportOverAdd(SciLectureReport sciLectureReport) {
        try {
            // 1. 获取当前用户信息
            SysUser currentUser = ShiroUtils.getSysUser();
            
            // 2. 使用流程管理服务提交结项审批
            java.util.Map<String, Object> result = approvalProcessService.submitApproval(
                    "LECTURE_APPROVAL", // 讲座报告审批流程编码
                    sciLectureReport.getId().longValue(), // 业务ID
                    sciLectureReport.getState(), // 当前状态
                    "提交结项申请", // 审批意见
                    currentUser.getUserId(), // 操作人ID
                    currentUser.getUserName(), // 操作人姓名
                    currentUser.getDept().getDeptName() // 操作人部门
            );
            
            // 3. 检查提交结果
            if (result != null && (boolean) result.get("success")) {
                // 4. 获取新状态
                String newState = (String) result.get("newState");
                // 5. 设置新状态
                sciLectureReport.setState(newState);
                // 6. 保存结项申请
                return sciLectureReportMapper.SciLectureReportOverAdd(sciLectureReport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
//    public List<SciLectureReport> selectSciLectureReportListByKYS(SciLectureReport sciLectureReport) {
//        return sciLectureReportMapper.selectSciLectureReportListBy(sciLectureReport);
//    }

    /**
     * 新增讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSciLectureReport(SciLectureReport sciLectureReport)
    {
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
            sciLectureReportOpinion.setConcate("新增记录");
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
    public HashMap<String, Object> checkConflict(SciLectureReport sciLectureReport)
    {
        HashMap<String, Object> map = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 设置时间格式
            LocalDateTime endTime = LocalDateTime.parse(sciLectureReport.getReportTime(), formatter)
                    .plusMinutes(sciLectureReport.getReportDuration()); // 计算结束时间
            sciLectureReport.setReportEndTime(endTime.format(formatter));
            int number = sciLectureReportMapper.checkConflict(sciLectureReport); // 进行数据比对
            if (number > 0){
                map.put("conflict", true);
                map.put("message", "有冲突");
            }else {
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
    public int updateSciLectureReport(SciLectureReport sciLectureReport)
    {
        // 根据讲座报告分类同步积分
        syncLectureReportIntegral(sciLectureReport);
        
        int number = sciLectureReportMapper.updateSciLectureReport(sciLectureReport);
        if (sciLectureReport.getUrlFlag().equals("gengxin")){
            SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
            // 操作用户id
            // 获取当前用户id并将数据类型从Integer转换为Long在给sciLectureReportOpinion.setUid()
            Integer userId = sciLectureReport.getUserId();
            sciLectureReportOpinion.setUid(userId != null ? userId.longValue() : null);
            // 被修改的报告的id
            sciLectureReportOpinion.setBaogaoId(sciLectureReport.getId());
            sciLectureReportOpinion.setConcate("数据所有者更新数据");
            sciLectureReportOpinion.setState("更新");
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
    public int deleteSciLectureReportByIds(String ids)
    {
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
    public int updateSciLectureReportByIds(String ids)
    {
        // 1. 参数校验
        if (ids == null || ids.trim().isEmpty()) {
            return 0;
        }

        // 2. 转换ID格式
        String[] idArray = ids.split(",");

        // 3. 仅处理单ID情况
        if (idArray.length == 1) {
            try {
                // 4. 转换为Integer类型
                Integer id = Integer.valueOf(idArray[0].trim());
                // 获取当前的用户信息
                SysUser currentUser = ShiroUtils.getSysUser();
                
                // 5. 获取当前讲座报告的状态
                SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
                if (report == null) {
                    return 0;
                }
                
                // 6. 直接更新状态为待教研室审核
                String newState = "LECTURE_JYS_AUDIT";
                int updateResult = sciLectureReportMapper.criticism(id, newState);
                
                // 7. 记录审批意见
                SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                sciLectureReportOpinion.setUid(currentUser.getUserId());
                sciLectureReportOpinion.setBaogaoId(id);
                sciLectureReportOpinion.setConcate("数据所有者提交");
                sciLectureReportOpinion.setState("提交");
                opinionMapper.opinionadd(sciLectureReportOpinion);
                
                return updateResult;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        // 10. 非单ID情况直接返回
        return 0;
    }

    /**
     * 删除讲座报告信息
     * 
     * @param id 讲座报告主键
     * @return 结果
     */
    @Override
    public int deleteSciLectureReportById(Integer id)
    {
        return sciLectureReportMapper.deleteSciLectureReportById(id);
    }

    // 通过批阅修改讲座报告的状态，并将批阅意见插入数据库
    @Override
    public int criticism(Integer rid, Long userId, String remark, String urlFlag) {
        try {
            // 1. 获取当前讲座报告
            SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(rid);
            if (report == null) {
                return 0;
            }
            
            // 2. 获取当前用户信息
            SysUser currentUser = ShiroUtils.getSysUser();
            
            // 3. 使用流程管理服务进行审批
            java.util.Map<String, Object> result = approvalProcessService.approve(
                    "LECTURE_APPROVAL", // 讲座报告审批流程编码
                    rid.longValue(), // 业务ID
                    report.getState(), // 当前状态
                    remark != null && !remark.isEmpty() ? remark : "通过", // 审批意见
                    userId, // 操作人ID
                    currentUser.getUserName(), // 操作人姓名
                    currentUser.getDept().getDeptName() // 操作人部门
            );
            
            // 4. 检查审批结果
                if (result != null && (boolean) result.get("success")) {
                    // 5. 获取新状态
                    String newState = (String) result.get("newState");
                    // 6. 更新讲座报告状态
                    int updateResult = sciLectureReportMapper.criticism(rid, newState);
                
                // 7. 记录审批意见
                SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                sciLectureReportOpinion.setUid(userId);
                sciLectureReportOpinion.setBaogaoId(rid); // 被批阅的报告id
                if (remark.equals("")){
                    remark = "通过";
                }
                sciLectureReportOpinion.setConcate(remark);
                sciLectureReportOpinion.setState("通过");
                opinionMapper.opinionadd(sciLectureReportOpinion); // 将批阅记录插入数据库
                
                // 8. 如果是科研处通过，计算科研分
                if (newState != null && newState.equals("LECTURE_PASSED")) {
                    if (report != null && report.getReportClassify() != null && !report.getReportClassify().isEmpty()) {
                        Integer classifyId = Integer.parseInt(report.getReportClassify());
                        SciLectureReportIntegral sciLectureReportIntegral = reportIntegralMapper.selectSciLectureReportIntegralById(classifyId);
                        if (sciLectureReportIntegral != null && sciLectureReportIntegral.getIntegral() != null) {
                            int kyf = sciLectureReportMapper.reportKeyanfen(rid, sciLectureReportIntegral.getIntegral());
                        }
                    }
                }
                
                return updateResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 驳回业务处理方法
    @Override
    public int reject(Integer id, Long userId, String remark, String urlFlag) {
        try {
            // 1. 获取当前讲座报告
            SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(id);
            if (report == null) {
                return 0;
            }
            
            // 2. 获取当前用户信息
            SysUser currentUser = ShiroUtils.getSysUser();
            
            // 3. 使用流程管理服务进行驳回
            java.util.Map<String, Object> result = approvalProcessService.reject(
                    "LECTURE_APPROVAL", // 讲座报告审批流程编码
                    id.longValue(), // 业务ID
                    report.getState(), // 当前状态
                    remark, // 审批意见
                    userId, // 操作人ID
                    currentUser.getUserName(), // 操作人姓名
                    currentUser.getDept().getDeptName() // 操作人部门
            );
            
            // 4. 检查驳回结果
            if (result != null && (boolean) result.get("success")) {
                // 5. 获取新状态
                String newState = (String) result.get("newState");
                // 6. 更新讲座报告状态
                int updateResult = sciLectureReportMapper.criticism(id, newState);
                
                // 7. 记录审批意见
                SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                sciLectureReportOpinion.setUid(userId);
                sciLectureReportOpinion.setBaogaoId(id); // 被批阅的报告id
                sciLectureReportOpinion.setConcate(remark);
                
                // 8. 根据操作类型设置状态
                if (urlFlag.equals("tuihui") || urlFlag.equals("zgqxtuihui")) {
                    sciLectureReportOpinion.setState("撤回");
                } else {
                    sciLectureReportOpinion.setState("驳回");
                }
                
                opinionMapper.opinionadd(sciLectureReportOpinion); // 将批阅记录插入数据库
                
                // 9. 如果是科研室驳回，清除科研分
                if (newState != null && newState.equals("LECTURE_REJECTED")) {
                    sciLectureReportMapper.reportKeyanfen(id, "0");
                }
                
                return updateResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public List<SciLectureReport> getStatsQuery(Map<String, String> params) {
        return sciLectureReportMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
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
     * 教师查询讲座报告列表（课题名称查询）
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListCx(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportListCx(sciLectureReport);
    }

    /**
     * 教研室查询讲座报告列表（第一作者、课题名称查询）
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListCxList(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportListCxList(sciLectureReport);
    }

    /**
     * 学院查询讲座报告列表（专业、第一作者、课题名称查询）
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListXY(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportListXY(sciLectureReport);
    }

    /**
     * 科研处查询讲座报告列表（学院、专业、第一作者、课题名称查询）
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListKY(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportListKY(sciLectureReport);
    }

    /**
     * 查询已结项的讲座报告列表
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListOVER(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }

    /**
     * 查询教研室相关的讲座报告列表
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectSciLectureReportListJX(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
    }

    /**
     * 查询用户参与的其他讲座报告列表
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciLectureReport> selectOtherListByUid(SciLectureReport sciLectureReport) {
        return sciLectureReportMapper.selectSciLectureReportList(sciLectureReport);
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
        return sciLectureReportMapper.selectSciLectureReportListAll(sciLectureReport);
    }

}
