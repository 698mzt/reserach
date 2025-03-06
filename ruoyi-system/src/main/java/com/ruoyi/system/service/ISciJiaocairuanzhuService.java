package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciJiaocairuanzhu;

/**
 * 教材软著Service接口
 *
 * @author ruoyi
 * @date 2024-11-21
 */
public interface ISciJiaocairuanzhuService
{
    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    public SciJiaocairuanzhu selectSciJiaocairuanzhuById(Integer id);

    /**
     * 查询教材软著列表
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著集合
     */
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 新增教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    public int insertSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 修改教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    public int updateSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu);

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的教材软著主键集合
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuByIds(String ids);

    /**
     * 删除教材软著信息
     *
     * @param id 教材软著主键
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuById(Integer id);



    int hxPass(String id,Long uid,String urlFlag);

    int updateJifen(Long id, int jifen);



//    int hxPass(String id,Long uid,String urlFlag,List score,List persion,Integer applyId);
//    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList4(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList3(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList2(SciJiaocairuanzhu sciJiaocairuanzhu);

    List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList1(SciJiaocairuanzhu sciJiaocairuanzhu);

    int recall(Integer id, String state, Long userId, String remark, String urlFlag);
//    int hxoverBh(String id, Long userId, String remark, String urlFlag);

//    int collegeAudit(String id, Long userId, String urlFlag);

}