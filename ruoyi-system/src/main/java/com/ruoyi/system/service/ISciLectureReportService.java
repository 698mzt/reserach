package com.ruoyi.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciLectureReport;
import com.ruoyi.system.domain.SciLectureReportOpinion;

/**
 * 讲座报告Service接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface ISciLectureReportService 
{
    /**
     * 查询讲座报告
     * 
     * @param id 讲座报告主键
     * @return 讲座报告
     */
    public SciLectureReport selectSciLectureReportById(Integer id);

    /**
     * 查询讲座报告列表
     * 
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    public List<SciLectureReport> selectSciLectureReportList(SciLectureReport sciLectureReport);

    /**
     * 查询讲座报告列表(导出）
     *
     * @param ids 需要导出的讲座报告id集合
     * @return 讲座报告集合
     */
    public List<SciLectureReport> selectSciLectureReportListByIds(String ids);

    /**
     * 新增讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    public int insertSciLectureReport(SciLectureReport sciLectureReport);

    /**
     * 新增讲座报告时场地校验
     *
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    public HashMap<String, Object> checkConflict(SciLectureReport sciLectureReport);

    /**
     * 修改讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    public int updateSciLectureReport(SciLectureReport sciLectureReport);

    /**
     * 批量删除讲座报告
     * 
     * @param ids 需要删除的讲座报告主键集合
     * @return 结果
     */
    public int deleteSciLectureReportByIds(String ids);
    /**
     * 提交状态是草稿的讲座报告
     */
    public int updateSciLectureReportByIds(String ids);

    /**
     * 删除讲座报告信息
     * 
     * @param id 讲座报告主键
     * @return 结果
     */
    public int deleteSciLectureReportById(Integer id);

    /**
     *通过批阅操作修改讲座报告的状态
     */
    int criticism(Integer id, Long userId,String remark,String urlFlag);

    int reject(Integer id, Long userId, String remark, String urlFlag);

    /**
     * 撤回审批
     */
    int recall(Integer id, Long userId, String remark);



    int SciLectureReportOverAdd(SciLectureReport sciLectureReport);

    List<SciLectureReport> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询讲座报告数据
     *
     * @param params 查询参数
     * @return 讲座报告集合
     */
    List<SciLectureReport> getStatsQueryToCheck(Map<String, String> params);

//    List<SciLectureReport> selectSciLectureReportListByKYS(SciLectureReport sciLectureReport);



    /**
     * 统一查询讲座报告列表
     * 通过@DataScope控制数据权限，移除状态过滤
     *
     * @param sciLectureReport 讲座报告
     * @return 讲座报告集合
     */
    List<SciLectureReport> selectSciLectureReportListAll(SciLectureReport sciLectureReport);
}
