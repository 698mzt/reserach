package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciPaperAMapper;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.common.core.text.Convert;

/**
 * 论文Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
@Service
public class SciPaperAServiceImpl implements ISciPaperAService 
{
    @Autowired
    private SciPaperAMapper sciPaperAMapper;

    /**
     * 查询论文
     * 
     * @param id 论文主键
     * @return 论文
     */
    @Override
    public SciPaperA selectSciPaperAById(Long id)
    {
        return sciPaperAMapper.selectSciPaperAById(id);
    }

    /**
     * 查询论文列表
     * 
     * @param sciPaperA 论文
     * @return 论文
     */
    @Override
    public List<SciPaperA> selectSciPaperAList(SciPaperA sciPaperA)
    {
        return sciPaperAMapper.selectSciPaperAList(sciPaperA);
    }

    /**
     * 新增论文
     * 
     * @param sciPaperA 论文
     * @return 结果
     */
    @Override
    public int insertSciPaperA(SciPaperA sciPaperA)
    {
        return sciPaperAMapper.insertSciPaperA(sciPaperA);
    }

    /**
     * 修改论文
     * 
     * @param sciPaperA 论文
     * @return 结果
     */
    @Override
    public int updateSciPaperA(SciPaperA sciPaperA)
    {
        return sciPaperAMapper.updateSciPaperA(sciPaperA);
    }

    /**
     * 批量删除论文
     * 
     * @param ids 需要删除的论文主键
     * @return 结果
     */
    @Override
    public int deleteSciPaperAByIds(String ids)
    {
        return sciPaperAMapper.deleteSciPaperAByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除论文信息
     * 
     * @param id 论文主键
     * @return 结果
     */
    @Override
    public int deleteSciPaperAById(Long id)
    {
        return sciPaperAMapper.deleteSciPaperAById(id);
    }
}
