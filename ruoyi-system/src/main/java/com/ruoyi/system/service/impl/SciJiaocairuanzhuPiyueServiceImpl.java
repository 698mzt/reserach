package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SciJiaocairuanzhuPiyue;
import com.ruoyi.system.mapper.SciJiaocairuanzhuPiyueMapper;
import com.ruoyi.system.service.ISciJiaocairuanzhuPiyueService;
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
public class SciJiaocairuanzhuPiyueServiceImpl implements ISciJiaocairuanzhuPiyueService
{
    @Autowired
    private SciJiaocairuanzhuPiyueMapper sciJiaocairuanzhuPiyueMapper;

    /**
     * 查询横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 横向课题的审核意见; InnoDB free: 11264 kB
     */
    @Override
    public SciJiaocairuanzhuPiyue selectSciJiaocairuanzhuPiyueById(Integer id)
    {
        return sciJiaocairuanzhuPiyueMapper.selectSciJiaocairuanzhuPiyueById(id);
    }


    @Override
    public List<SciJiaocairuanzhuPiyue> selectSciJiaocairuanzhuPiyueList(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue)
    {
        return sciJiaocairuanzhuPiyueMapper.selectSciJiaocairuanzhuPiyueList(sciJiaocairuanzhuPiyue);
    }


    @Override
    public int insertSciJiaocairuanzhuPiyue(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue)
    {
        sciJiaocairuanzhuPiyue.setCreateTime(DateUtils.getNowDate());
        return sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
    }


    @Override
    public int updateSciJiaocairuanzhuPiyue(SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue)
    {
        return sciJiaocairuanzhuPiyueMapper.updateSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
    }

    /**
     * 批量删除横向课题的审核意见; InnoDB free: 11264 kB
     *
     * @param ids 需要删除的横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuPiyueByIds(String ids)
    {
        return sciJiaocairuanzhuPiyueMapper.deleteSciJiaocairuanzhuPiyueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除横向课题的审核意见; InnoDB free: 11264 kB信息
     *
     * @param id 横向课题的审核意见; InnoDB free: 11264 kB主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuPiyueById(Integer id)
    {
        return sciJiaocairuanzhuPiyueMapper.deleteSciJiaocairuanzhuPiyueById(id);
    }
}
