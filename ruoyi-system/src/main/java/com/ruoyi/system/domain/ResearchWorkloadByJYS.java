package com.ruoyi.system.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 教研室科研业绩计划表-科研工作量（统计积分）
 *
 * @author guoqiang
 * @data 2025-12-08
 */
@Data
public class ResearchWorkloadByJYS {

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
//    @Excel(name = "教师名称")
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

    // ==================== 纵向科研项目(zxktxj) 小类字段 ====================
    /** 主持国家基金科研项目 */
    private String zcgjjjkyxm;
    /** 主持省部级基金科研项目 */
    private String zcsbjjjkyxm;
    /** 主持省部级高校项目 */
    private String zcsbjjgxm;
    /** 主持省部级纵向科研项目 */
    private String zcsbjzxkyxm;
    /** 主持厅局级或横向科研项目 */
    private String zctjjxhjkyxm;
    /** 主持校级管理类项目无万元预算 */
    private String zcxjjxglxmywyys;
    /** 主持校级管理类项目有万元预算 */
    private String zcxjjxglxmywyyx;

    // ==================== 横向科研项目(hxkt) 小类字段 ====================
    /** 二万元以下 */
    private String ewyyx;
    /** 二到五万 */
    private String edwwy;
    /** 五到十万 */
    private String wdswy;
    /** 十到二十万 */
    private String sdeswy;
    /** 二十到五十万 */
    private String esdsswwy;
    /** 五十万到一百万 */
    private String wswwydwswy;
    /** 五十到一百万 */
    private String wsdqswwy;
    /** 千万到一百万 */
    private String qswdybwy;

    // ==================== 成果转化(cgzh) 小类字段 ====================
    /** 二以下信息 */
    private String eyxxx;
    /** 二到五万 */
    private String edwxx;
    /** 五到十项 */
    private String wdsxx;
    /** 十到二十项 */
    private String sdesxx;
    /** 二十到五十项 */
    private String esdsswxx;
    /** 五十万到百万项 */
    private String sswdwsxx;
    /** 大于五十万 */
    private String dywsxx;

    // ==================== 学术论文(xslw) 小类字段 ====================
    /** SCI论文 */
    @JsonProperty("SCI")
    private String sci;
    /** EI论文 */
    @JsonProperty("EI")
    private String ei;
    /** 核心期刊 */
    private String hx;
    /** 三网收录 */
    private String sw;
    /** 普通期刊 */
    private String pt;
    /** 校报 */
    private String xb;

    // ==================== 教材著作(jczz) 小类字段 ====================
    /** 出版专著（一类出版社） */
    private String cbzz1;
    /** 出版专著（二类出版社） */
    private String cbzz2;
    /** 出版译著（一类出版社） */
    private String cbyz1;
    /** 出版译著（二类出版社） */
    private String cbyz2;
    /** 出版教材（国家规划，省级规划教材） */
    private String cbjc1;
    /** 出版教材 */
    private String cbjc2;
    /** 自编教材（校内使用） */
    private String zbjc;

    // ==================== 专利软著(zl/rz) 小类字段 ====================
    /** 授权发明专利 */
    private String sqfmzl;
    /** 实用新型专利 */
    private String syxxzl;
    /** 外型设计专利 */
    private String wxsjzl;
    /** 计算机软件著作权 */
    private String jsjrjzzq;

    // ==================== 奖励(jl) 小类字段 ====================
    /** 省级教学成果奖 */
    private String sjjxcgj;
    /** 省级科学技术奖 */
    private String sjkxjsj;
    /** 厅级科技进步类评审 */
    private String stjjzrkxlpj;
    /** 厅级技术类评审 */
    private String stjjsklpj;
    /** 校级教学成果奖 */
    private String xjjxcgj;
    /** 优秀科研成果奖 */
    private String yyxkyxjcgj;
    /** 学会教学成果奖 */
    private String xhjjxcgj;
    /** 学会科研成果奖 */
    private String xhjkycgj;

    // ==================== 讲座报告(jzbg) 小类字段 ====================
    /** 报告国际 */
    private String jbgj;
    /** 报告国内 */
    private String jbgn;
    /** 参加国际 */
    private String cjgj;
    /** 参加国内 */
    private String cjgn;
    /** 学术交流 */
    private String xjxs;

    /** 明细JSON（存放Alltotle明细，供"按金额分组"模式使用） */
    private String detailJson;

    private Integer hxktCnt;
    private java.math.BigDecimal hxktSum;
    private Integer zxktxjCnt;
    private java.math.BigDecimal zxktxjSum;
    private Integer zxktxjysCnt;
    private java.math.BigDecimal zxktxjysSum;
    private Integer cgzhCnt;
    private java.math.BigDecimal cgzhSum;
    private Integer xslwCnt;
    private java.math.BigDecimal xslwSum;
    private Integer jczzCnt;
    private java.math.BigDecimal jczzSum;
    private Integer zlCnt;
    private java.math.BigDecimal zlSum;
    private Integer rzCnt;
    private java.math.BigDecimal rzSum;
    private Integer jlCnt;
    private java.math.BigDecimal jlSum;
    private Integer jzbgCnt;
    private java.math.BigDecimal jzbgSum;

    /** 10大类别原始积分值（来自 Alltotle_score，用于显示积分） */
    private String hxktScore;
    private String zxktxjScore;
    private String zxktxjysScore;
    private String cgzhScore;
    private String xslwScore;
    private String jczzScore;
    private String zlScore;
    private String rzScore;
    private String jlScore;
    private String jzbgScore;

    /** 10大类别总工作量合计（万元），用于前端图表聚合 */
    private java.math.BigDecimal __score;
}
