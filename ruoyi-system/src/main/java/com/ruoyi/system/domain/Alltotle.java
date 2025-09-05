package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Alltotle对象 Alltotle
 *
 * @author ruoyi 天秦晋
 * @date 2025-08-26
 */
public class Alltotle extends BaseEntity
{
  private static final long serialVersionUID = 1L;

  public static List<String> ge_wan_list = Arrays.asList("zcgjjjkyxm", "zcsbjjjkyxm","zcsbjjgxm","zcsbjzxkyxm","zctjjxhjkyxm","zcxjjxglxmywyys","zcxjjxglxmywyyx","ewyyx","edwwy","wdswy","sdeswy","esdsswwy","wswwydwswy","wsdqswwy","qswdybwy","eyxxx","edwxx","wdsxx","sdesxx","esdsswxx","sswdwsxx","dywsxx");
  public static List<String> no_need_add_list = Arrays.asList("userId","userName","partenId","partenName","deptId","deptName");
  /** 用户id */
  @Excel(name = "用户id")
  private Long userId;

  /** 用户名 */
  @Excel(name = "用户名")
  private String userName;

  /** 父部门id */
  @Excel(name = "父部门id")
  private Long partenId;

  /** 父部门名字 */
  @Excel(name = "父部门名字")
  private String partenName;

  /** 部门id */
  @Excel(name = "部门id")
  private Long deptId;

  /** 部门名字 */
  @Excel(name = "部门名字")
  private String deptName;

  /** 主持国家基金科研项目 */
  @Excel(name = "主持国家基金科研项目")
  private String zcgjjjkyxm;

  /** 主持省部级基金科研项目 */
  @Excel(name = "主持省部级基金科研项目")
  private String zcsbjjjkyxm;

  /** 主持省部级教改项目 */
  @Excel(name = "主持省部级教改项目")
  private String zcsbjjgxm;

  /** 主持省部级纵向科研项目 */
  @Excel(name = "主持省部级纵向科研项目")
  private String zcsbjzxkyxm;

  /** 主持厅局级、学会级科研项目 */
  @Excel(name = "主持厅局级、学会级科研项目")
  private String zctjjxhjkyxm;

  /** 主持校级教学管理项目（1万元以上） */
  @Excel(name = "主持校级教学管理项目", readConverterExp = "1=万元以上")
  private String zcxjjxglxmywyys;

  /** 主持校级教学管理项目（1万元以下） */
  @Excel(name = "主持校级教学管理项目", readConverterExp = "1=万元以下")
  private String zcxjjxglxmywyyx;

  /** 2万元以下 */
  @Excel(name = "2万元以下")
  private String ewyyx;

  /** 2-5万元（含2） */
  @Excel(name = "2-5万元", readConverterExp = "含=2")
  private String edwwy;

  /** 5-10万元（含5） */
  @Excel(name = "5-10万元", readConverterExp = "含=5")
  private String wdswy;

  /** 10-20万元（含10） */
  @Excel(name = "10-20万元", readConverterExp = "含=10")
  private String sdeswy;

  /** 20-35万元（含20） */
  @Excel(name = "20-35万元", readConverterExp = "含=20")
  private String esdsswwy;

  /** 35-50万元（含35） */
  @Excel(name = "35-50万元", readConverterExp = "含=35")
  private String wswwydwswy;

  /** 50-75万元（含50） */
  @Excel(name = "50-75万元", readConverterExp = "含=50")
  private String wsdqswwy;

  /** 75-100万元（含75） */
  @Excel(name = "75-100万元", readConverterExp = "含=75")
  private String qswdybwy;

  /** 2以下信息（成果转化） */
  @Excel(name = "2以下信息", readConverterExp = "成=果转化")
  private String eyxxx;

  /** 2-5信息（成果转化） */
  @Excel(name = "2-5信息", readConverterExp = "成=果转化")
  private String edwxx;

  /** 5-10信息（成果转化） */
  @Excel(name = "5-10信息", readConverterExp = "成=果转化")
  private String wdsxx;

  /** 10-20信息（成果转化） */
  @Excel(name = "10-20信息", readConverterExp = "成=果转化")
  private String sdesxx;

  /** 20-35信息（成果转化） */
  @Excel(name = "20-35信息", readConverterExp = "成=果转化")
  private String esdsswxx;

  /** 35-50信息（成果转化） */
  @Excel(name = "35-50信息", readConverterExp = "成=果转化")
  private String sswdwsxx;

  /** 大于50信息（成果转化） */
  @Excel(name = "大于50信息", readConverterExp = "成=果转化")
  private String dywsxx;

  /** SCI */
  @Excel(name = "SCI")
  private String SCI;

  /** EI */
  @Excel(name = "EI")
  private String EI;

  /** 核心 */
  @Excel(name = "核心")
  private String hx;

  /** 三网 */
  @Excel(name = "三网")
  private String sw;

  /** 普通 */
  @Excel(name = "普通")
  private String pt;

  /** 校办 */
  @Excel(name = "校办")
  private String xb;

  /** 出版专著（一类出版社） */
  @Excel(name = "出版专著", readConverterExp = "一=类出版社")
  private String cbzz1;

  /** 出版专著（二类出版社） */
  @Excel(name = "出版专著", readConverterExp = "二=类出版社")
  private String cbzz2;

  /** 出版译著（一类出版社） */
  @Excel(name = "出版译著", readConverterExp = "一=类出版社")
  private String cbyz1;

  /** 出版译著（二类出版社） */
  @Excel(name = "出版译著", readConverterExp = "二=类出版社")
  private String cbyz2;

  /** 出版教材（国家规划，省级规划教材） */
  @Excel(name = "出版教材", readConverterExp = "国=家规划，省级规划教材")
  private String cbjc1;

  /** 出版教材 */
  @Excel(name = "出版教材")
  private String cbjc2;

  /** 自编教材（校内使用） */
  @Excel(name = "自编教材", readConverterExp = "校=内使用")
  private String zbjc;
  /** 授权发明专利 */
  @Excel(name = "授权发明专利")
  private String sqfmzl;

  /** 实用新型专利 */
  @Excel(name = "实用新型专利")
  private String syxxzl;

  /** 外型设计专利 */
  @Excel(name = "外型设计专利")
  private String wxsjzl;

  /** 计算机软件著作权 */
  @Excel(name = "计算机软件著作权")
  private String jsjrjzzq;

  /** 省级教学成果奖 */
  @Excel(name = "省级教学成果奖")
  private String sjjxcgj;

  /** 省级科学技术奖 */
  @Excel(name = "省级科学技术奖")
  private String sjkxjsj;

  /** 省级一流精品课评比 */
  @Excel(name = "省级一流精品课评比")
  private String sjyljpkpb;

  /** 市、厅局级自然科学类评奖 */
  @Excel(name = "市、厅局级自然科学类评奖")
  private String stjjzrkxlpj;

  /** 市、厅局级社科类评奖 */
  @Excel(name = "市、厅局级社科类评奖")
  private String stjjsklpj;

  /** 校级教学成果奖 */
  @Excel(name = "校级教学成果奖")
  private String xjjxcgj;

  /** 应用型科研校级成果奖 */
  @Excel(name = "应用型科研校级成果奖")
  private String yyxkyxjcgj;

  /** 校级精品课评比 */
  @Excel(name = "校级精品课评比")
  private String xjjpkpb;

  /** 教师参加省级大赛获奖 */
  @Excel(name = "教师参加省级大赛获奖")
  private String jscjsjdshj;

  /** 举办国际 */
  @Excel(name = "举办国际")
  private String jbgj;

  /** 举办国内 */
  @Excel(name = "举办国内")
  private String jbgn;

  /** 参加国际 */
  @Excel(name = "参加国际")
  private String cjgj;

  /** 参加国内 */
  @Excel(name = "参加国内")
  private String cjgn;

  /** 校级学术 */
  @Excel(name = "校级学术")
  private String xjxs;

  public Alltotle(Long userId) {
    this.userId = userId;
  }
  public Alltotle( ) {

  }
  public void setUserId(Long userId)
  {
    this.userId = userId;
  }

  public Long getUserId()
  {
    return userId;
  }
  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getUserName()
  {
    return userName;
  }
  public void setPartenId(Long partenId)
  {
    this.partenId = partenId;
  }

  public Long getPartenId()
  {
    return partenId;
  }
  public void setPartenName(String partenName)
  {
    this.partenName = partenName;
  }

  public String getPartenName()
  {
    return partenName;
  }
  public void setDeptId(Long deptId)
  {
    this.deptId = deptId;
  }

  public Long getDeptId()
  {
    return deptId;
  }
  public void setDeptName(String deptName)
  {
    this.deptName = deptName;
  }

  public String getDeptName()
  {
    return deptName;
  }
  public void setZcgjjjkyxm(String zcgjjjkyxm)
  {
    this.zcgjjjkyxm = zcgjjjkyxm;
  }

  public String getZcgjjjkyxm()
  {
    return zcgjjjkyxm;
  }
  public void setZcsbjjjkyxm(String zcsbjjjkyxm)
  {
    this.zcsbjjjkyxm = zcsbjjjkyxm;
  }

  public String getZcsbjjjkyxm()
  {
    return zcsbjjjkyxm;
  }
  public void setZcsbjjgxm(String zcsbjjgxm)
  {
    this.zcsbjjgxm = zcsbjjgxm;
  }

  public String getZcsbjjgxm()
  {
    return zcsbjjgxm;
  }
  public void setZcsbjzxkyxm(String zcsbjzxkyxm)
  {
    this.zcsbjzxkyxm = zcsbjzxkyxm;
  }

  public String getZcsbjzxkyxm()
  {
    return zcsbjzxkyxm;
  }
  public void setZctjjxhjkyxm(String zctjjxhjkyxm)
  {
    this.zctjjxhjkyxm = zctjjxhjkyxm;
  }

  public String getZctjjxhjkyxm()
  {
    return zctjjxhjkyxm;
  }
  public void setZcxjjxglxmywyys(String zcxjjxglxmywyys)
  {
    this.zcxjjxglxmywyys = zcxjjxglxmywyys;
  }

  public String getZcxjjxglxmywyys()
  {
    return zcxjjxglxmywyys;
  }
  public void setZcxjjxglxmywyyx(String zcxjjxglxmywyyx)
  {
    this.zcxjjxglxmywyyx = zcxjjxglxmywyyx;
  }

  public String getZcxjjxglxmywyyx()
  {
    return zcxjjxglxmywyyx;
  }
  public void setEwyyx(String ewyyx)
  {
    this.ewyyx = ewyyx;
  }

  public String getEwyyx()
  {
    return ewyyx;
  }
  public void setEdwwy(String edwwy)
  {
    this.edwwy = edwwy;
  }

  public String getEdwwy()
  {
    return edwwy;
  }
  public void setWdswy(String wdswy)
  {
    this.wdswy = wdswy;
  }

  public String getWdswy()
  {
    return wdswy;
  }
  public void setSdeswy(String sdeswy)
  {
    this.sdeswy = sdeswy;
  }

  public String getSdeswy()
  {
    return sdeswy;
  }
  public void setEsdsswwy(String esdsswwy)
  {
    this.esdsswwy = esdsswwy;
  }

  public String getEsdsswwy()
  {
    return esdsswwy;
  }
  public void setWswwydwswy(String wswwydwswy)
  {
    this.wswwydwswy = wswwydwswy;
  }

  public String getWswwydwswy()
  {
    return wswwydwswy;
  }
  public void setWsdqswwy(String wsdqswwy)
  {
    this.wsdqswwy = wsdqswwy;
  }

  public String getWsdqswwy()
  {
    return wsdqswwy;
  }
  public void setQswdybwy(String qswdybwy)
  {
    this.qswdybwy = qswdybwy;
  }

  public String getQswdybwy()
  {
    return qswdybwy;
  }

  public void setEyxxx(String eyxxx) {
    this.eyxxx = eyxxx;
  }

  public void setEdwxx(String edwxx) {
    this.edwxx = edwxx;
  }

  public void setWdsxx(String wdsxx) {
    this.wdsxx = wdsxx;
  }

  public void setSdesxx(String sdesxx) {
    this.sdesxx = sdesxx;
  }

  public void setEsdsswxx(String esdsswxx) {
    this.esdsswxx = esdsswxx;
  }

  public void setSswdwsxx(String sswdwsxx) {
    this.sswdwsxx = sswdwsxx;
  }

  public void setDywsxx(String dywsxx) {
    this.dywsxx = dywsxx;
  }

  public void setEyxxx(String eyxxx, Double amount)
  {

    if (eyxxx== null || eyxxx.equals("")){
      this.eyxxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(eyxxx.substring(0, eyxxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( eyxxx.substring(eyxxx.indexOf("（") + 1,  eyxxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.eyxxx = num+"个（"+allamonts+"万）";
    }

  }

  public String getEyxxx()
  {
    return eyxxx;
  }
  public void setEdwxx(String edwxx, Double amount)
  {

    if (edwxx== null || edwxx.equals("")){
      this.edwxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(edwxx.substring(0, edwxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( edwxx.substring(edwxx.indexOf("（") + 1,  edwxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.edwxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getEdwxx()
  {
    return edwxx;
  }
  public void setWdsxx(String wdsxx, Double amount)
  {

    if (wdsxx== null || wdsxx.equals("")){
      this.wdsxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(wdsxx.substring(0, wdsxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( wdsxx.substring(wdsxx.indexOf("（") + 1,  wdsxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.wdsxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getWdsxx()
  {
    return wdsxx;
  }
  public void setSdesxx(String sdesxx, Double amount)
  {

    if (sdesxx== null || sdesxx.equals("")){
      this.sdesxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(sdesxx.substring(0, sdesxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( sdesxx.substring(sdesxx.indexOf("（") + 1,  sdesxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.sdesxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getSdesxx()
  {
    return sdesxx;
  }
  public void setEsdsswxx(String esdsswxx, Double amount)
  {

    if (esdsswxx== null || esdsswxx.equals("")){
      this.esdsswxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(esdsswxx.substring(0, esdsswxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( esdsswxx.substring(esdsswxx.indexOf("（") + 1,  esdsswxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.esdsswxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getEsdsswxx()
  {
    return esdsswxx;
  }
  public void setSswdwsxx(String sswdwsxx, Double amount)
  {

    if (sswdwsxx== null || sswdwsxx.equals("")){
      this.sswdwsxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(sswdwsxx.substring(0, sswdwsxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( sswdwsxx.substring(sswdwsxx.indexOf("（") + 1,  sswdwsxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.sswdwsxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getSswdwsxx()
  {
    return sswdwsxx;
  }
  public void setDywsxx(String dywsxx, Double amount)
  {

    if (dywsxx== null || dywsxx.equals("")){
      this.dywsxx = "1个 ("+amount+"万)";
    }else{
      Integer num = Integer.parseInt(dywsxx.substring(0, dywsxx.indexOf("个")).trim());
      Double amounts = Double.valueOf( dywsxx.substring(dywsxx.indexOf("（") + 1,  dywsxx.indexOf("万")).trim());
      Double allamonts = num + amounts;
      this.dywsxx = num+"个（"+allamonts+"万）";
    }
  }

  public String getDywsxx()
  {
    return dywsxx;
  }
  public void setSCI(String SCI)
  {
    this.SCI = SCI;
  }

  public String getSCI()
  {
    return SCI;
  }
  public void setEI(String EI)
  {
    this.EI = EI;
  }

  public String getEI()
  {
    return EI;
  }
  public void setHx(String hx)
  {
    this.hx = hx;
  }

  public String getHx()
  {
    return hx;
  }
  public void setSw(String sw)
  {
    this.sw = sw;
  }

  public String getSw()
  {
    return sw;
  }
  public void setPt(String pt)
  {
    this.pt = pt;
  }

  public String getPt()
  {
    return pt;
  }
  public void setXb(String xb)
  {
    this.xb = xb;
  }

  public String getXb()
  {
    return xb;
  }

  public String getCbzz1() {
    return cbzz1;
  }

  public void setCbzz1(String cbzz1) {
    this.cbzz1 = cbzz1;
  }

  public String getCbzz2() {
    return cbzz2;
  }

  public void setCbzz2(String cbzz2) {
    this.cbzz2 = cbzz2;
  }

  public String getCbyz1() {
    return cbyz1;
  }

  public void setCbyz1(String cbyz1) {
    this.cbyz1 = cbyz1;
  }

  public String getCbyz2() {
    return cbyz2;
  }

  public void setCbyz2(String cbyz2) {
    this.cbyz2 = cbyz2;
  }

  public String getCbjc1() {
    return cbjc1;
  }

  public void setCbjc1(String cbjc1) {
    this.cbjc1 = cbjc1;
  }

  public String getCbjc2() {
    return cbjc2;
  }

  public void setCbjc2(String cbjc2) {
    this.cbjc2 = cbjc2;
  }

  public String getZbjc() {
    return zbjc;
  }

  public void setZbjc(String zbjc) {
    this.zbjc = zbjc;
  }

  public void setSqfmzl(String sqfmzl)
  {
    this.sqfmzl = sqfmzl;
  }

  public String getSqfmzl()
  {
    return sqfmzl;
  }
  public void setSyxxzl(String syxxzl)
  {
    this.syxxzl = syxxzl;
  }

  public String getSyxxzl()
  {
    return syxxzl;
  }
  public void setWxsjzl(String wxsjzl)
  {
    this.wxsjzl = wxsjzl;
  }

  public String getWxsjzl()
  {
    return wxsjzl;
  }
  public void setJsjrjzzq(String jsjrjzzq)
  {
    this.jsjrjzzq = jsjrjzzq;
  }

  public String getJsjrjzzq()
  {
    return jsjrjzzq;
  }
  public void setSjjxcgj(String sjjxcgj)
  {
    this.sjjxcgj = sjjxcgj;
  }

  public String getSjjxcgj()
  {
    return sjjxcgj;
  }
  public void setSjkxjsj(String sjkxjsj)
  {
    this.sjkxjsj = sjkxjsj;
  }

  public String getSjkxjsj()
  {
    return sjkxjsj;
  }
  public void setSjyljpkpb(String sjyljpkpb)
  {
    this.sjyljpkpb = sjyljpkpb;
  }

  public String getSjyljpkpb()
  {
    return sjyljpkpb;
  }
  public void setStjjzrkxlpj(String stjjzrkxlpj)
  {
    this.stjjzrkxlpj = stjjzrkxlpj;
  }

  public String getStjjzrkxlpj()
  {
    return stjjzrkxlpj;
  }
  public void setStjjsklpj(String stjjsklpj)
  {
    this.stjjsklpj = stjjsklpj;
  }

  public String getStjjsklpj()
  {
    return stjjsklpj;
  }
  public void setXjjxcgj(String xjjxcgj)
  {
    this.xjjxcgj = xjjxcgj;
  }

  public String getXjjxcgj()
  {
    return xjjxcgj;
  }
  public void setYyxkyxjcgj(String yyxkyxjcgj)
  {
    this.yyxkyxjcgj = yyxkyxjcgj;
  }

  public String getYyxkyxjcgj()
  {
    return yyxkyxjcgj;
  }
  public void setXjjpkpb(String xjjpkpb)
  {
    this.xjjpkpb = xjjpkpb;
  }

  public String getXjjpkpb()
  {
    return xjjpkpb;
  }
  public void setJscjsjdshj(String jscjsjdshj)
  {
    this.jscjsjdshj = jscjsjdshj;
  }

  public String getJscjsjdshj()
  {
    return jscjsjdshj;
  }
  public void setJbgj(String jbgj)
  {
    this.jbgj = jbgj;
  }

  public String getJbgj()
  {
    return jbgj;
  }
  public void setJbgn(String jbgn)
  {
    this.jbgn = jbgn;
  }

  public String getJbgn()
  {
    return jbgn;
  }
  public void setCjgj(String cjgj)
  {
    this.cjgj = cjgj;
  }

  public String getCjgj()
  {
    return cjgj;
  }
  public void setCjgn(String cjgn)
  {
    this.cjgn = cjgn;
  }

  public String getCjgn()
  {
    return cjgn;
  }
  public void setXjxs(String xjxs)
  {
    this.xjxs = xjxs;
  }

  public String getXjxs()
  {
    return xjxs;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
      .append("userId", getUserId())
      .append("userName", getUserName())
      .append("partenId", getPartenId())
      .append("partenName", getPartenName())
      .append("deptId", getDeptId())
      .append("deptName", getDeptName())
      .append("zcgjjjkyxm", getZcgjjjkyxm())
      .append("zcsbjjjkyxm", getZcsbjjjkyxm())
      .append("zcsbjjgxm", getZcsbjjgxm())
      .append("zcsbjzxkyxm", getZcsbjzxkyxm())
      .append("zctjjxhjkyxm", getZctjjxhjkyxm())
      .append("zcxjjxglxmywyys", getZcxjjxglxmywyys())
      .append("zcxjjxglxmywyyx", getZcxjjxglxmywyyx())
      .append("ewyyx", getEwyyx())
      .append("edwwy", getEdwwy())
      .append("wdswy", getWdswy())
      .append("sdeswy", getSdeswy())
      .append("esdsswwy", getEsdsswwy())
      .append("wswwydwswy", getWswwydwswy())
      .append("wsdqswwy", getWsdqswwy())
      .append("qswdybwy", getQswdybwy())
      .append("eyxxx", getEyxxx())
      .append("edwxx", getEdwxx())
      .append("wdsxx", getWdsxx())
      .append("sdesxx", getSdesxx())
      .append("esdsswxx", getEsdsswxx())
      .append("sswdwsxx", getSswdwsxx())
      .append("dywsxx", getDywsxx())
      .append("SCI", getSCI())
      .append("EI", getEI())
      .append("hx", getHx())
      .append("sw", getSw())
      .append("pt", getPt())
      .append("xb", getXb())
      .append("cbzz1", getCbzz1())
      .append("cbzz2", getCbzz2())
      .append("cbyz1", getCbyz1())
      .append("cbyz2", getCbyz2())
      .append("cbjc1", getCbjc1())
      .append("cbjc2", getCbjc2())
      .append("zbjc", getZbjc())
      .append("sqfmzl", getSqfmzl())
      .append("syxxzl", getSyxxzl())
      .append("wxsjzl", getWxsjzl())
      .append("jsjrjzzq", getJsjrjzzq())
      .append("sjjxcgj", getSjjxcgj())
      .append("sjkxjsj", getSjkxjsj())
      .append("sjyljpkpb", getSjyljpkpb())
      .append("stjjzrkxlpj", getStjjzrkxlpj())
      .append("stjjsklpj", getStjjsklpj())
      .append("xjjxcgj", getXjjxcgj())
      .append("yyxkyxjcgj", getYyxkyxjcgj())
      .append("xjjpkpb", getXjjpkpb())
      .append("jbgj", getJbgj())
      .append("jbgn", getJbgn())
      .append("cjgj", getCjgj())
      .append("cjgn", getCjgn())
      .append("xjxs", getXjxs())
      .toString();
  }
}
