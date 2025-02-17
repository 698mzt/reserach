package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.SciZhuanliruanzhuPiyue;

/**
 * 横向课题的审核意见; InnoDB free: 11264 kBService接口
 *
 * @author 张聪
 * @date 2024-08-21
 */
public interface ISciZhuanliruanzhuPiyueService
{
    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    public SciZhuanliruanzhuPiyue selectSciZhuanliruanzhuPiyueById(Integer id);



    public List<SciZhuanliruanzhuPiyue> selectSciZhuanliruanzhuPiyueList(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue);


    public int insertSciZhuanliruanzhuPiyue(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue);


    public int updateSciZhuanliruanzhuPiyue(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue);

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param ids 需要删除的横向课题的审核意见; InnoDB free: 11264 kB主键集合
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuPiyueByIds(String ids);

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB信息
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    public int deleteSciZhuanliruanzhuPiyueById(Integer id);
}
