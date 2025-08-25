package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciLectureReport;
import org.apache.ibatis.annotations.Param;

/**
 * 讲座报告Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface SciLectureReportMapper 
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
     * 查询讲座报告（导出）
     *
     * @param ids 讲座报告id集合
     * @return 讲座报告集合
     */
    public List<SciLectureReport> selectSciLectureReportListByIds(String[] ids);

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
    public int checkConflict(SciLectureReport sciLectureReport);
    /**
     * 修改讲座报告
     * 
     * @param sciLectureReport 讲座报告
     * @return 结果
     */
    public int updateSciLectureReport(SciLectureReport sciLectureReport);

    /**
     * 删除讲座报告
     * 
     * @param id 讲座报告主键
     * @return 结果
     */
    public int deleteSciLectureReportById(Integer id);

    /**
     * 批量删除讲座报告
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciLectureReportByIds(String[] ids);
    public int deleteSciLectureReportOpinionBy(String[] ids);

    int criticism(@Param("id") Integer id, @Param("state") String state);
    int reportKeyanfen(@Param("id") Integer id, @Param("keyanfen") String keyanfen);

//    int SciLectureReportOverAdd(@Param("id") Integer id, @Param("actualNumber") Integer actualNumber, @Param("actualTime") String actualTime);
    int SciLectureReportOverAdd(SciLectureReport sciLectureReport);

//    List<SciLectureReport> selectSciLectureReportListBy(SciLectureReport sciLectureReport);
}
