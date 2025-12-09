package com.ruoyi.system.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 教研室科研业绩计划表-科研工作量（统计积分）
 *
 * @author guoqiang
 * @data 2025-12-08
 */
@Data
public class ResearchWorkload {

    /** 学院id */
    private String parentId;

    /** 学院名称 */
    @Excel(name = "学院名称")
    private String parentName;

    /** 专业id */
    private String deptId;

    /** 专业名称 */
    @Excel(name = "专业名称")
    private String deptName;

    /** 教师id */
    private String userId;

    /** 教师名称 */
    @Excel(name = "教师名称")
    private String userName;

    /** 横向科研项目 */
    @Excel(name = "横向科研项目")
    private String hxkt;

    /** 纵向科研项目-校级以上 */
    @Excel(name = "纵向科研项目-校级以上")
    private String zxktxj;

    /** 纵向科研项目-校级 */
    @Excel(name = "纵向科研项目-校级")
    private String zxktxjys;

    /** 成果转化 */
    @Excel(name = "成果转化")
    private String cgzh;

    /** 学术论文 */
    @Excel(name = "学术论文")
    private String xslw;

    /** 教材著作 */
    @Excel(name = "教材著作")
    private String jczz;

    /** 专利 */
    @Excel(name = "专利")
    private String zl;

    /** 软著 */
    @Excel(name = "软著")
    private String rz;

    /** 奖励 */
    @Excel(name = "奖励")
    private String jl;

    /** 讲座报告 */
    @Excel(name = "讲座报告")
    private String jzbg;
}
