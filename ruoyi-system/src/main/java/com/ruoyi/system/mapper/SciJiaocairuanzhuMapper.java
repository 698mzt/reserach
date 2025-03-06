package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
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

}
