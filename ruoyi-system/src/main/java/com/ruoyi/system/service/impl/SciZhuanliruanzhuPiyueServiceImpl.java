package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SciZhuanliruanzhuPiyue;
import com.ruoyi.system.mapper.SciZhuanliruanzhuPiyueMapper;
import com.ruoyi.system.service.ISciZhuanliruanzhuPiyueService;
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
public class SciZhuanliruanzhuPiyueServiceImpl implements ISciZhuanliruanzhuPiyueService
{
    @Autowired
    private SciZhuanliruanzhuPiyueMapper sciZhuanliruanzhuPiyueMapper;

    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public SciZhuanliruanzhuPiyue selectSciZhuanliruanzhuPiyueById(Integer id)
    {
        return sciZhuanliruanzhuPiyueMapper.selectSciZhuanliruanzhuPiyueById(id);
    }


    @Override
    public List<SciZhuanliruanzhuPiyue> selectSciZhuanliruanzhuPiyueList(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue)
    {
        return sciZhuanliruanzhuPiyueMapper.selectSciZhuanliruanzhuPiyueList(sciZhuanliruanzhuPiyue);
    }


    @Override
    public int insertSciZhuanliruanzhuPiyue(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue)
    {
        sciZhuanliruanzhuPiyue.setCreateTime(DateUtils.getNowDate());
        return sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
    }


    @Override
    public int updateSciZhuanliruanzhuPiyue(SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue)
    {
        return sciZhuanliruanzhuPiyueMapper.updateSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
    }

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param ids 需要删除的横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciZhuanliruanzhuPiyueByIds(String ids)
    {
        return sciZhuanliruanzhuPiyueMapper.deleteSciZhuanliruanzhuPiyueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB信息
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciZhuanliruanzhuPiyueById(Integer id)
    {
        return sciZhuanliruanzhuPiyueMapper.deleteSciZhuanliruanzhuPiyueById(id);
    }
}
