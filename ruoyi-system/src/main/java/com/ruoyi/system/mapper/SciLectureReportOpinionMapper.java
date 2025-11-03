package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciLectureReportOpinion;

import java.util.List;
import java.util.Map;

/**
 * 讲座报告审核表Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface SciLectureReportOpinionMapper
{
    public List<SciLectureReportOpinion> opinionlist(SciLectureReportOpinion sciLectureReportOpinion);

    // 添加审核意见
    int opinionadd(SciLectureReportOpinion sciLectureReportOpinion);

}
