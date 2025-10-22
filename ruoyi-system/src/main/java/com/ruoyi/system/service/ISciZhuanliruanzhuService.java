package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import org.apache.ibatis.annotations.Options;

/**
 * 专利软著Service接口
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public interface ISciZhuanliruanzhuService
{
    /**
     * 查询专利软著
     * 
     * @param id 专利软著主键
     * @return 专利软著
     */
    public SciZhuanliruanzhu selectSciZhuanliruanzhuById(Integer id);

    /**
     * 查询专利软著列表
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 新增专利软著
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 修改专利软著
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    public int updateSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 批量删除专利软著
     * 
     * @param ids 需要删除的专利软著主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuByIds(String ids);

    /**
     * 删除专利软著信息
     * 
     * @param id 专利软著主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuById(Integer id);



    int hxPass(String id,Long uid,String urlFlag);

    int updateJifen(Long id, int jifen);



//    int hxPass(String id,Long uid,String urlFlag,List score,List persion,Integer applyId);
//    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList4(SciZhuanliruanzhu sciZhuanliruanzhu);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList3(SciZhuanliruanzhu sciZhuanliruanzhu);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList2(SciZhuanliruanzhu sciZhuanliruanzhu);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList1(SciZhuanliruanzhu sciZhuanliruanzhu);

    int recall(Integer id, String state, Long userId, String remark, String urlFlag);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList31(SciZhuanliruanzhu sciZhuanliruanzhu);

    List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList21(SciZhuanliruanzhu sciZhuanliruanzhu);
//    int hxoverBh(String id, Long userId, String remark, String urlFlag);

//    int collegeAudit(String id, Long userId, String urlFlag);
    /**
     * 检查是否已存在相同专利名称和负责人级别
     * @param mingcheng 专利名称
     * @param paiming 负责人级别
     * @return 是否存在
     */
    boolean checkExist(String mingcheng, String paiming);

    /**
     * 统计查询教材专著数据
     *
     * @param params 查询参数
     * @return 教材专著集合
     */
    List<SciZhuanliruanzhu> getStatsQuery(Map<String, String> params);


}
