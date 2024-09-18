package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciHorizontalPiyue;

/**
 * 横向课题的审核意见; InnoDB free: 11264 kBMapper接口
 * 
 * @author 张聪
 * @date 2024-08-21
 */
public interface SciHorizontalPiyueMapper 
{
    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    public SciHorizontalPiyue selectSciHorizontalPiyueById(Integer id);

    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB列表
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 横向课题的审核意见; InnoDB free: 11264 kB集合
     */
    public List<SciHorizontalPiyue> selectSciHorizontalPiyueList(SciHorizontalPiyue sciHorizontalPiyue);

    /**
     * 新增横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    public int insertSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue);

    /**
     * 修改横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    public int updateSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue);

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    public int deleteSciHorizontalPiyueById(Integer id);

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciHorizontalPiyueByIds(String[] ids);
}
