package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.common.core.text.Convert;

/**
 * 横向课题的审核意见; InnoDB free: 11264 kBService业务层处理
 * 
 * @author 张聪
 * @date 2024-08-21
 */
@Service
public class SciHorizontalPiyueServiceImpl implements ISciHorizontalPiyueService 
{
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;

    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public SciHorizontalPiyue selectSciHorizontalPiyueById(Integer id)
    {
        return sciHorizontalPiyueMapper.selectSciHorizontalPiyueById(id);
    }

    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB列表
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public List<SciHorizontalPiyue> selectSciHorizontalPiyueList(SciHorizontalPiyue sciHorizontalPiyue)
    {
        return sciHorizontalPiyueMapper.selectSciHorizontalPiyueList(sciHorizontalPiyue);
    }

    /**
     * 新增横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    @Override
    public int insertSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue)
    {
        sciHorizontalPiyue.setCreateTime(DateUtils.getNowDate());
        return sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
    }

    /**
     * 修改横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param sciHorizontalPiyue 横向课题的审核意见; InnoDB free: 11264 kB
     * @return 结果
     */
    @Override
    public int updateSciHorizontalPiyue(SciHorizontalPiyue sciHorizontalPiyue)
    {
        return sciHorizontalPiyueMapper.updateSciHorizontalPiyue(sciHorizontalPiyue);
    }

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     * 
     * @param ids 需要删除的横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalPiyueByIds(String ids)
    {
        return sciHorizontalPiyueMapper.deleteSciHorizontalPiyueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB信息
     * 
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalPiyueById(Integer id)
    {
        return sciHorizontalPiyueMapper.deleteSciHorizontalPiyueById(id);
    }
}
