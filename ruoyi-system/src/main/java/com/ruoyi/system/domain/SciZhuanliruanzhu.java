package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教材软著对象 sci_zhuanliruanzhu
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public class SciZhuanliruanzhu extends BaseEntity
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
                ", shoquanhao='" + shoquanhao + '\'' +
                ", huopishijain=" + huopishijain +
                ", shifouyingyon='" + shifouyingyon + '\'' +
                ", hangyelianhe='" + hangyelianhe + '\'' +
                ", fenlei='" + fenlei + '\'' +
                ", paiming='" + paiming + '\'' +

                ", state='" + state + '\'' +
                '}';
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    private Date createtime;

    public String getWenjian() {
        return wenjian;
    }

    public void setWenjian(String wenjian) {
        this.wenjian = wenjian;
    }



    public Long getParentId() {
        return parentId;
    }

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
    @Excel(name = "学院")
    private String xueyuan;

    /** 教研室 */
    @Excel(name = "教研室")
    private String jiaoyanshi;

    /** 工号 */
    private Integer gonghao;

    /** 姓名 */
    @Excel(name = "姓名")
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

//    /** 类型 */
//    @Excel(name = "类型",dictType = "sys_zhuanli_leibie")
    private String leixing;

    /** 授权号 */
    @Excel(name = "授权号")
    private String shoquanhao;

    /** 获批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "获批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date huopishijain;

    /** 是否应用 */
    @Excel(name = "是否应用",dictType = "sys_yes_no")
    private String shifouyingyon;

    /** 是否行业联合 */
    @Excel(name = "是否行业联合",dictType = "sys_yes_no")
    private String hangyelianhe;

    /** 分类 */
    @Excel(name = "分类",dictType = "sys_zhuanli_fenlei")
    private String fenlei;

    /** 排名 */
    @Excel(name = "排名",dictType = "sys_zhuanliruanzhu_fzr")
    private String paiming;

    @Excel(name = "积分")
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
    public void setShoquanhao(String shoquanhao) 
    {
        this.shoquanhao = shoquanhao;
    }

    public String getShoquanhao() 
    {
        return shoquanhao;
    }
    public void setHuopishijain(Date huopishijain) 
    {
        this.huopishijain = huopishijain;
    }

    public Date getHuopishijain() 
    {
        return huopishijain;
    }
    public void setShifouyingyon(String shifouyingyon) 
    {
        this.shifouyingyon = shifouyingyon;
    }

    public String getShifouyingyon() 
    {
        return shifouyingyon;
    }
    public void setHangyelianhe(String hangyelianhe) 
    {
        this.hangyelianhe = hangyelianhe;
    }

    public String getHangyelianhe() 
    {
        return hangyelianhe;
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
