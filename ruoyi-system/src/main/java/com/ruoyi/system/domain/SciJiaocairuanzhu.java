package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
public class SciJiaocairuanzhu extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    public Date getCreatetime() {
        return createtime;
    }

    @Override
    public String toString() {
        return "SciZhuanliruanzhu{" +
                "createtime=" + createtime +
                ", wenjian='" + wenjian + '\'' +
                ", parentId=" + parentId +
                ", deptId=" + deptId +
                ", role='" + role + '\'' +
                ", jifen='" + jifen + '\'' +
                ", year='" + year + '\'' +
                ", urlFlag='" + urlFlag + '\'' +
                ", yname='" + yname + '\'' +
                ", dname='" + dname + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", uid=" + uid +
                ", id=" + id +
                ", xueyuan='" + xueyuan + '\'' +
                ", jiaoyanshi='" + jiaoyanshi + '\'' +
                ", gonghao=" + gonghao +
                ", xingming='" + xingming + '\'' +
                ", xingzhengzhiwu='" + xingzhengzhiwu + '\'' +
                ", shehuizhicheng='" + shehuizhicheng + '\'' +
                ", xiaoneizhicheng='" + xiaoneizhicheng + '\'' +
                ", jiaoshixingming='" + jiaoshixingming + '\'' +
                ", mingcheng='" + mingcheng + '\'' +
//                ", leixing='" + leixing + '\'' +
                ", isbn='" + isbn + '\'' +
                ", chubanshijian=" + chubanshijian +
                ", chubanshe='" + chubanshe + '\'' +
                ", benxiaoshifoyongshu='" + benxiaoshifoyongshu + '\'' +
                ", fenlei='" + fenlei + '\'' +
                ", paiming='" + paiming + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getWenjian() {
        return wenjian;
    }

    public void setWenjian(String wenjian) {
        this.wenjian = wenjian;
    }




    public Long getParentId() {
        return parentId;
    }
    private Date createtime;


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    private Long parentId;




    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    private Long deptId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    //    查询年份
    private String year;


    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    private String urlFlag;

    public String getYname() {
        return yname;
    }

    public void setYname(String yname) {
        this.yname = yname;
    }

    private String yname;


    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    private String dname;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String  userName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private Integer userId;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    private Long uid;

    /**  */
    private Integer id;

    /** 学院 */
    @Excel(name = "学院", sort = 1)
    private String xueyuan;

    /** 教研室 */
    @Excel(name = "教研室", sort = 2)
    private String jiaoyanshi;

    /** 工号 */
    private Integer gonghao;
    
    /** 工号（login_name，用于核算导出） */
    @Excel(name = "工号", sort = 3)
    private String loginName;

    /** 姓名 */
    @Excel(name = "姓名", sort = 4)
    private String xingming;

    /** 行政职务 */
    private String xingzhengzhiwu;

    /** 社会职称 */
    private String shehuizhicheng;

    /** 校内职称 */
    private String xiaoneizhicheng;

    /** 教师姓名 */
    private String jiaoshixingming;

    /** 名称 */
    @Excel(name = "名称")
    private String mingcheng;

    /** 类型 */
//    @Excel(name = "类型",dictType = "sys_jiaocairuanzhu_leibie")
  private String leixing;

    /** isbn */
    @Excel(name = "ISBN")
    private String isbn;

    /** 出版时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出版时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date chubanshijian;

    /** 出版社 */
    @Excel(name = "出版社")
    private String chubanshe;

    /** 本校是否用书 */
    @Excel(name = "本校是否用书",dictType = "sys_yes_no")
    private String benxiaoshifoyongshu;

    /** 分类 */
    @Excel(name = "分类",dictType = "sys_jiaocairuanzhu_fenlei")
    private String fenlei;

    /** 排名 */
    @Excel(name = "排名",dictType = "sys_jiaocairuanzhu_fzr")
    private String paiming;

    /** 科研分 */
    @Excel(name = "积分", sort = 5)
    private String jifen;
    @Excel(name = "文件")
    private String wenjian;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Excel(name = "状态")
    private String state;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setXueyuan(String xueyuan)
    {
        this.xueyuan = xueyuan;
    }

    public String getXueyuan()
    {
        return xueyuan;
    }
    public void setJiaoyanshi(String jiaoyanshi)
    {
        this.jiaoyanshi = jiaoyanshi;
    }

    public String getJiaoyanshi()
    {
        return jiaoyanshi;
    }
    public void setGonghao(Integer gonghao)
    {
        this.gonghao = gonghao;
    }

    public Integer getGonghao()
    {
        return gonghao;
    }
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }
    public String getLoginName()
    {
        return loginName;
    }
    public void setXingming(String xingming)
    {
        this.xingming = xingming;
    }

    public String getXingming()
    {
        return xingming;
    }
    public void setXingzhengzhiwu(String xingzhengzhiwu)
    {
        this.xingzhengzhiwu = xingzhengzhiwu;
    }

    public String getXingzhengzhiwu()
    {
        return xingzhengzhiwu;
    }
    public void setShehuizhicheng(String shehuizhicheng)
    {
        this.shehuizhicheng = shehuizhicheng;
    }

    public String getShehuizhicheng()
    {
        return shehuizhicheng;
    }
    public void setXiaoneizhicheng(String xiaoneizhicheng)
    {
        this.xiaoneizhicheng = xiaoneizhicheng;
    }

    public String getXiaoneizhicheng()
    {
        return xiaoneizhicheng;
    }
    public void setJiaoshixingming(String jiaoshixingming)
    {
        this.jiaoshixingming = jiaoshixingming;
    }

    public String getJiaoshixingming()
    {
        return jiaoshixingming;
    }
    public void setMingcheng(String mingcheng)
    {
        this.mingcheng = mingcheng;
    }
    public String getMingcheng()
    {
        return mingcheng;
    }
    public void setLeixing(String leixing)
    {
        this.leixing = leixing;
    }

    public String getLeixing()
    {
        return leixing;
    }
    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getIsbn()
    {
        return isbn;
    }
    public void setChubanshijian(Date chubanshijian)
    {
        this.chubanshijian = chubanshijian;
    }

    public Date getChubanshijian()
    {
        return chubanshijian;
    }
    public void setChubanshe(String chubanshe)
    {
        this.chubanshe = chubanshe;
    }

    public String getChubanshe()
    {
        return chubanshe;
    }
    public void setBenxiaoshifoyongshu(String benxiaoshifoyongshu)
    {
        this.benxiaoshifoyongshu = benxiaoshifoyongshu;
    }

    public String getBenxiaoshifoyongshu()
    {
        return benxiaoshifoyongshu;
    }
    public void setFenlei(String fenlei)
    {
        this.fenlei = fenlei;
    }

    public String getFenlei()
    {
        return fenlei;
    }
    public void setPaiming(String paiming)
    {
        this.paiming = paiming;
    }

    public String getPaiming()
    {
        return paiming;
    }
    public String getStateDes() {
        if (this.state == null) {
            return "";
        }
        switch (this.state) {
            case "0":
                return "草稿箱";
            case "1":
                return "待教研室处理";
            case "2":
                return "待学院审核";
            case "3":
                return "教研室退回";
            case "4":
                return "待科研处审核";
            case "5":
                return "学院退回";
            case "6":
                return "科研处通过";
            case "7":
                return "科研处退回";

        }
        return this.state;
    }


}
