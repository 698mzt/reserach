package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import org.apache.ibatis.annotations.Param;

/**
 * 专利软著Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public interface SciZhuanliruanzhuMapper
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
    public int insertSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 修改专利软著
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    public int updateSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 删除专利软著
     * 
     * @param id 专利软著主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuById(Integer id);

    /**
     * 批量删除专利软著
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuByIds(String[] ids);


    int hxPass(@Param("id") String id, @Param("state") String state);


    int updateJifen(@Param("id") Long id, @Param("jifen") int jifen);

    /**
     * 更新最终积分（final_jifen）
     * urlFlag="chayue"（科研处通过）时写入，未最终确认则为空
     */
    int updateFinalJifen(@Param("id") String id, @Param("finalJifen") String finalJifen);

    /**
     * 科研处通过时写入认定时间（仅更新该字段）
     */
    int updateKyjcPassTime(@Param("id") String id, @Param("kyjcPassTime") java.util.Date kyjcPassTime);

    int hxover(@Param("id") String id,@Param("state") String state);

    List<SysUser> selectUserList(@Param("user") SysUser user);

    public int insertScoreHistory(SciZhuanliruanzhu sciZhuanliruanzhu);


//    void setUid(Long userId);

//    void insertSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue);

    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList4(SciZhuanliruanzhu sciZhuanliruanzhu);
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList3(SciZhuanliruanzhu sciZhuanliruanzhu);
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList2(SciZhuanliruanzhu sciZhuanliruanzhu);
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList1(SciZhuanliruanzhu sciZhuanliruanzhu);

    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList31(SciZhuanliruanzhu sciZhuanliruanzhu);
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList21(SciZhuanliruanzhu sciZhuanliruanzhu);

    /**
     * 查询是否存在相同专利名称和负责人级别
     */
    int checkExist(@Param("mingcheng") String mingcheng, @Param("paiming") String paiming);

    int checkUserCount(@Param("userId") Long userId);

    /**
     * 统计查询专利软著数据
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    List<SciZhuanliruanzhu> getStatsQuery(Map<String, String> params);
    List<SciZhuanliruanzhu> getStatsQueryToExcil(Map<String, String> params);

    /**
     * 核算查询专利软著数据
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    List<SciZhuanliruanzhu> getStatsQueryToCheck(Map<String, String> params);

}
