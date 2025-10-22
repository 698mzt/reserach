package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class StatsQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //横向
    private SciHorizontalApply sciHorizontalApply;
    //纵向
    private SciHorizontalApplyVertical sciHorizontalApplyVertical;
    //成果转换
    private SciIntraSchoolPro sciIntraSchoolPro;
    //论文
    private SciPA sciPA;
    //教材专著 赵威翰
    private SciJiaocairuanzhu sciJiaocairuanzhu;
    //专利软著 教材软著 雷
    private SciZhuanliruanzhu sciZhuanliruanzhu;
    //奖励
    private SysReward sysReward;
    //讲座报告 黄秀军
    private SciLectureReportIntegral sciLectureReportIntegral;

    // 查询条件
    // 项目类别 (模块名称)
    private String remark;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
    // 学院
    private String college;
    // 专业
    private String major;
    // 职称
    private String jobTitle;
    // 课题名称
    private String topicName;

}
