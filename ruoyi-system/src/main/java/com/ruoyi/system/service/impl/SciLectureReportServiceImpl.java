package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciLectureReportIntegral;
import com.ruoyi.system.domain.SciLectureReportOpinion;
import com.ruoyi.system.mapper.SciLectureReportIntegralMapper;
import com.ruoyi.system.mapper.SciLectureReportOpinionMapper;
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
        if (sciLectureReport.getState().equals("4")){
            sciLectureReport.setState("6");
        }
        return sciLectureReportMapper.SciLectureReportOverAdd(sciLectureReport);
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
        int number =sciLectureReportMapper.insertSciLectureReport(sciLectureReport);

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
                String state = "1"; // 固定状态值
                // 获取当前的用户信息
                SysUser currentUser = ShiroUtils.getSysUser();
                SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
                // 向批阅记录实例中添加用户id
                sciLectureReportOpinion.setUid(currentUser.getUserId());
                // 提交的报告的id
                sciLectureReportOpinion.setBaogaoId(id);
                sciLectureReportOpinion.setConcate("数据所有者提交");
                sciLectureReportOpinion.setState("提交");
                // 将批阅记录插入数据库
                opinionMapper.opinionadd(sciLectureReportOpinion);
                // 5. 调用Mapper执行更新
                return sciLectureReportMapper.criticism(id, state);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        // 7. 非单ID情况直接返回
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
        String state;
        if (urlFlag.equals("pro")){
            state = "6"; // 教研室通过
        }else if (urlFlag.equals("check")){
            state = "4"; // 科研室通过
            SciLectureReport report = sciLectureReportMapper.selectSciLectureReportById(rid);
            SciLectureReportIntegral sciLectureReportIntegral = reportIntegralMapper.selectSciLectureReportIntegralById(report.getRepIntId());
            int kyf = sciLectureReportMapper.reportKeyanfen(rid, sciLectureReportIntegral.getIntegral());
        } else if (urlFlag.equals("xypro")) {
            state = "2"; // 学院通过
        } else {
            return 0;
        }
        int i = sciLectureReportMapper.criticism(rid, state);  // 修改讲座报告的状态
        SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
        sciLectureReportOpinion.setUid(userId);
        sciLectureReportOpinion.setBaogaoId(rid); // 被批阅的报告id
        if (remark.equals("")){
            remark = "通过";
        }
        sciLectureReportOpinion.setConcate(remark);
        sciLectureReportOpinion.setState("通过");
        opinionMapper.opinionadd(sciLectureReportOpinion); // 将批阅记录插入数据库
        return i;
    }

    // 驳回业务处理方法
    @Override
    public int reject(Integer id, Long userId, String remark, String urlFlag) {
        // 批阅记录实例
        SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
        String state;
        // 根据不同的操作设计讲座报告数据表中数据的状态，并将驳回原因插入讲座报告批阅记录表中，其中3为教研室驳回，5为科研室驳回，7为学院驳回
        switch (urlFlag) {
            case "pro": // 教研室驳回操作
                sciLectureReportOpinion.setState("驳回");
                state = "3";
                break;
            case "tuihui": // 教研室撤回操作
                sciLectureReportOpinion.setState("撤回");
                state = "1";
                break;
            case "check": // 科研室驳回操作
                sciLectureReportOpinion.setState("驳回");
                state = "5";
                sciLectureReportMapper.reportKeyanfen(id, "0");
                break;
            case "zgqxtuihui": // 科研室撤回操作
                sciLectureReportOpinion.setState("撤回");
                state = "2";
                sciLectureReportMapper.reportKeyanfen(id, "0");
                break;
            case "xypro": // 学院驳回操作
                sciLectureReportOpinion.setState("驳回");
                state = "7";
                break;
            case "xytuihui": // 学院撤回操作
                sciLectureReportOpinion.setState("撤回");
                state = "6";
                break;
            default:
                return 0;
        }
        int i = sciLectureReportMapper.criticism(id, state);  // 修改讲座报告的状态
        sciLectureReportOpinion.setUid(userId);
        sciLectureReportOpinion.setBaogaoId(id); // 被批阅的报告id
        sciLectureReportOpinion.setConcate(remark);
//        sciLectureReportOpinion.setState("未通过");
        opinionMapper.opinionadd(sciLectureReportOpinion); // 将批阅记录插入数据库
        return i;
    }
    @Override
    public List<SciLectureReportOpinion> getStatsQuery(Map<String, String> params) {
        return sciLectureReportMapper.getStatsQuery(params);
    }

    @Override
    public List<SciLectureReport> getStatsQueryToCheck(Map<String, String> params) {
        return sciLectureReportMapper.getStatsQueryToCheck(params);
    }

}
