package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import org.apache.ibatis.annotations.Param;

/**
 * 教材软著Mapper接口
 *
 * @author ruoyi
 * @date 2025-02-10
 */
public interface SciJiaocairuanzhuMapper
{


    /**
     * 查询专利软著
     *
     * @param id 专利软著主键
     * @return 专利软著
     */
    public SciJiaocairuanzhu selectSciJiaocairuanzhuById(Integer id);

    /**
     * 查询专利软著列表
     *
     * @param sciJiaocairuanzhu 专利软著
     * @return 专利软著集合
     */
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 新增专利软著
     *
     * @param sciJiaocairuanzhu 专利软著
     * @return 结果
     */
    public int insertSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 修改专利软著
     *
     * @param sciJiaocairuanzhu 专利软著
     * @return 结果
     */
    public int updateSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 删除专利软著
     *
     * @param id 专利软著主键
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuById(Integer id);

    /**
     * 批量删除专利软著
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuByIds(String[] ids);


    int hxPass(@Param("id") String id, @Param("state") String state);


    int updateJifen(@Param("id") Long id, @Param("jifen") int jifen);

    int hxover(@Param("id") String id,@Param("state") String state);

    List<SysUser> selectUserList(@Param("user") SysUser user);

    public int insertScoreHistory(SciJiaocairuanzhu sciJiaocairuanzhu);


//    void setUid(Long userId);

//    void insertSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue);

    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList4(SciJiaocairuanzhu sciJiaocairuanzhu);
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList3(SciJiaocairuanzhu sciJiaocairuanzhu);
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList2(SciJiaocairuanzhu sciJiaocairuanzhu);
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList1(SciJiaocairuanzhu sciJiaocairuanzhu);
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList31(SciJiaocairuanzhu sciJiaocairuanzhu);
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList21(SciJiaocairuanzhu sciJiaocairuanzhu);
    
    // 新方法：教师查询
    public List<SciJiaocairuanzhu> selectSciPaperAListCx(SciJiaocairuanzhu sciJiaocairuanzhu);
    
    // 新方法：教研室查询
    public List<SciJiaocairuanzhu> selectSciPaperAListCxList(SciJiaocairuanzhu sciJiaocairuanzhu);
    
    // 新方法：学院查询
    public List<SciJiaocairuanzhu> selectSciPaperAListXY(SciJiaocairuanzhu sciJiaocairuanzhu);
    
    // 新方法：科研处查询
    public List<SciJiaocairuanzhu> selectSciPaperAListKY(SciJiaocairuanzhu sciJiaocairuanzhu);
    
    // 新方法：管理员查询
    public List<SciJiaocairuanzhu> selectSciPaperAList(SciJiaocairuanzhu sciJiaocairuanzhu);
    /**
     * 查询是否存在相同专利名称和负责人级别
     */
    int checkExist(@Param("mingcheng") String mingcheng, @Param("paiming") String paiming, @Param("userId") Long userId);

    /**
     * 统计查询教材专著数据
     *
     * @param params 查询参数
     * @return 教材专著集合
     */
    List<SciJiaocairuanzhu> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询教材专著数据
     *
     * @param params 查询参数
     * @return 教材专著集合
     */
    List<SciJiaocairuanzhu> getStatsQueryToCheck(Map<String, String> params);

    /**
     * 统一查询教材软著列表（所有角色均可查看所有状态）
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著集合
     */
    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuListAll(SciJiaocairuanzhu sciJiaocairuanzhu);

}
