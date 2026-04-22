package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciJiaocairuanzhuPiyue;

import java.util.List;
/**
 * 横向课题的审核意见; InnoDB free: 11264 kBService接口
 *
 * @author 张聪
 * @date 2024-08-21
 */
public interface ISciJiaocairuanzhuPiyueService
{
    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    public SciJiaocairuanzhuPiyue selectSciJiaocairuanzhuPiyueById(Integer id);



    public List<SciJiaocairuanzhuPiyue> selectSciJiaocairuanzhuPiyueList(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue);


    public int insertSciJiaocairuanzhuPiyue(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue);


    public int updateSciJiaocairuanzhuPiyue(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue);

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param ids 需要删除的横向课题的审核意见; InnoDB free: 11264 kB主键集合
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuPiyueByIds(String ids);

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB信息
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    public int deleteSciJiaocairuanzhuPiyueById(Integer id);
}
