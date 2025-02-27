package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ruoyi.common.annotation.DataScope;
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
    public int insertSciLectureReport(SciLectureReport sciLectureReport)
    {
        return sciLectureReportMapper.insertSciLectureReport(sciLectureReport);
    }

    /**
     * 修改讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    @Override
    public int updateSciLectureReport(SciLectureReport sciLectureReport)
    {
        return sciLectureReportMapper.updateSciLectureReport(sciLectureReport);
    }

    /**
     * 批量删除讲座报告
     * 
     * @param ids 需要删除的讲座报告主键
     * @return 结果
     */
    @Override
    public int deleteSciLectureReportByIds(String ids)
    {
        return sciLectureReportMapper.deleteSciLectureReportByIds(Convert.toStrArray(ids));
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
        String state;
        if (urlFlag.equals("pro") || urlFlag.equals("tuihui")){
            state = "3"; // 教研室驳回
        }else if (urlFlag.equals("check") || urlFlag.equals("zgqxtuihui")){
            state = "5"; // 科研室驳回
            int kyf = sciLectureReportMapper.reportKeyanfen(id,"0");
        }else if(urlFlag.equals("xypro") || urlFlag.equals("xytuihui")){
            state = "7"; // 学院驳回
        }
        else {
            return 0;
        }
        int i = sciLectureReportMapper.criticism(id, state);  // 修改讲座报告的状态
        SciLectureReportOpinion sciLectureReportOpinion = new SciLectureReportOpinion();
        sciLectureReportOpinion.setUid(userId);
        sciLectureReportOpinion.setBaogaoId(id); // 被批阅的报告id
        sciLectureReportOpinion.setConcate(remark);
        sciLectureReportOpinion.setState("未通过");
        opinionMapper.opinionadd(sciLectureReportOpinion); // 将批阅记录插入数据库
        return i;
    }

}
