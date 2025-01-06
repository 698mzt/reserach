package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciPaperA;

/**
 * 论文Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
public interface SciPaperAMapper 
{
    /**
     * 查询论文
     * 
     * @param id 论文主键
     * @return 论文
     */
    public SciPaperA selectSciPaperAById(Long id);

    /**
     * 查询论文列表
     * 
     * @param sciPaperA 论文
     * @return 论文集合
     */
    public List<SciPaperA> selectSciPaperAList(SciPaperA sciPaperA);

    /**
     * 新增论文
     * 
     * @param sciPaperA 论文
     * @return 结果
     */
    public int insertSciPaperA(SciPaperA sciPaperA);

    /**
     * 修改论文
     * 
     * @param sciPaperA 论文
     * @return 结果
     */
    public int updateSciPaperA(SciPaperA sciPaperA);

    /**
     * 删除论文
     * 
     * @param id 论文主键
     * @return 结果
     */
    public int deleteSciPaperAById(Long id);

    /**
     * 批量删除论文
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciPaperAByIds(String[] ids);
}
