package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SciLectureReport;
import com.ruoyi.system.domain.SciLectureReportOpinion;
import com.ruoyi.system.mapper.SciLectureReportMapper;
import com.ruoyi.system.mapper.SciLectureReportOpinionMapper;
import com.ruoyi.system.service.ISciLectureReportOpinionService;
import com.ruoyi.system.service.ISciLectureReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 讲座报告Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
@Service
public class SciLectureReportOpinionServiceImpl implements ISciLectureReportOpinionService
{
    @Autowired
    private SciLectureReportOpinionMapper sciLectureReportOpinionMapper;

    @Override
    public List<SciLectureReportOpinion> opinionlist(SciLectureReportOpinion sciLectureReportOpinion) {

        return sciLectureReportOpinionMapper.opinionlist(sciLectureReportOpinion);
    }
}
