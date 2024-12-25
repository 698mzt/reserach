package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciZhuanliruanzhu;

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
    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);
    int hxoverBh(String id, Long userId, String remark, String urlFlag);





}
