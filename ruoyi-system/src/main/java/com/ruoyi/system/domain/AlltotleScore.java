package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 统计积分对象 Alltotle_score
 *
 * @author ruoyi
 * @date 2025-09-03
 */
public class AlltotleScore extends BaseEntity
{
  private static final long serialVersionUID = 1L;

  /** $column.columnComment */
  private Long userId;

  /** $column.columnComment */
  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
  private String userName;

  /** $column.columnComment */
  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
  private Long partenId;

  /** $column.columnComment */
  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
  private String partenName;

  /** $column.columnComment */
  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
  private Long deptId;

  /** $column.columnComment */
  @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
  private String deptName;

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

  /** 专利、软著 */
  @Excel(name = "专利、软著")
  private String zlrz;
  private String zl;
  private String rz;

  /** 奖励 */
  @Excel(name = "奖励")
  private String jl;

  /** 讲座报告 */
  @Excel(name = "讲座报告")
  private String jzbg;

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
  public void setHxkt(String hxkt)
  {
    this.hxkt = hxkt;
  }

  public String getHxkt()
  {
    return hxkt;
  }
  public void setZxktxj(String zxktxj)
  {
    this.zxktxj = zxktxj;
  }

  public String getZxktxj()
  {
    return zxktxj;
  }
  public void setZxktxjys(String zxktxjys)
  {
    this.zxktxjys = zxktxjys;
  }

  public String getZxktxjys()
  {
    return zxktxjys;
  }
  public void setCgzh(String cgzh)
  {
    this.cgzh = cgzh;
  }

  public String getCgzh()
  {
    return cgzh;
  }
  public void setXslw(String xslw)
  {
    this.xslw = xslw;
  }

  public String getXslw()
  {
    return xslw;
  }
  public void setJczz(String jczz)
  {
    this.jczz = jczz;
  }

  public String getJczz()
  {
    return jczz;
  }
  public void setZlrz(String zlrz)
  {
    this.zlrz = zlrz;
  }

  public String getZlrz()
  {
    return zlrz;
  }
  public void setJl(String jl)
  {
    this.jl = jl;
  }

  public String getJl()
  {
    return jl;
  }
  public void setJzbg(String jzbg)
  {
    this.jzbg = jzbg;
  }

  public String getJzbg()
  {
    return jzbg;
  }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getRz() {
        return rz;
    }

    public void setRz(String rz) {
        this.rz = rz;
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
      .append("hxkt", getHxkt())
      .append("zxktxj", getZxktxj())
      .append("zxktxjys", getZxktxjys())
      .append("cgzh", getCgzh())
      .append("xslw", getXslw())
      .append("jczz", getJczz())
      .append("zlrz", getZlrz())
      .append("jl", getJl())
      .append("jzbg", getJzbg())
      .toString();
  }
}
