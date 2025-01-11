package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciLectureReportOpinion;

import java.util.List;

/**
 * 讲座报告审核Service接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface ISciLectureReportOpinionService
{
    public List<SciLectureReportOpinion> opinionlist(SciLectureReportOpinion sciLectureReportOpinion);
}
