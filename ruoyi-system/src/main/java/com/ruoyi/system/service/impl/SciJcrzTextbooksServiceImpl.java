package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciJcrzTextbooksMapper;
import com.ruoyi.system.domain.SciJcrzTextbooks;
import com.ruoyi.system.service.ISciJcrzTextbooksService;
import com.ruoyi.common.core.text.Convert;

/**
 * 教材软著Service业务层处理
 *
 * @author zwh
 * @date 2024-11-14
 */
@Service
public class SciJcrzTextbooksServiceImpl implements ISciJcrzTextbooksService
{
    @Autowired
    private SciJcrzTextbooksMapper sciJcrzTextbooksMapper;

    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    @Override
    public SciJcrzTextbooks selectSciJcrzTextbooksById(Long id)
    {
        return sciJcrzTextbooksMapper.selectSciJcrzTextbooksById(id);
    }

    /**
     * 查询教材软著列表
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 教材软著
     */
    @Override
    public List<SciJcrzTextbooks> selectSciJcrzTextbooksList(SciJcrzTextbooks sciJcrzTextbooks)
    {
        return sciJcrzTextbooksMapper.selectSciJcrzTextbooksList(sciJcrzTextbooks);
    }

    /**
     * 新增教材软著
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 结果
     */
    @Override
    public int insertSciJcrzTextbooks(SciJcrzTextbooks sciJcrzTextbooks)
    {
        return sciJcrzTextbooksMapper.insertSciJcrzTextbooks(sciJcrzTextbooks);
    }

    /**
     * 修改教材软著
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 结果
     */
    @Override
    public int updateSciJcrzTextbooks(SciJcrzTextbooks sciJcrzTextbooks)
    {
        return sciJcrzTextbooksMapper.updateSciJcrzTextbooks(sciJcrzTextbooks);
    }

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJcrzTextbooksByIds(String ids)
    {
        return sciJcrzTextbooksMapper.deleteSciJcrzTextbooksByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除教材软著信息
     *
     * @param id 教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJcrzTextbooksById(Long id)
    {
        return sciJcrzTextbooksMapper.deleteSciJcrzTextbooksById(id);
    }
}
