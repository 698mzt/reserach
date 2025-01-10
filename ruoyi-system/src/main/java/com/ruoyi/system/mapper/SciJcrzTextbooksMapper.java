package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SciJcrzTextbooks;

/**
 * 教材软著Mapper接口
 *
 * @author zwh
 * @date 2024-11-14
 */
public interface SciJcrzTextbooksMapper
{
    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    public SciJcrzTextbooks selectSciJcrzTextbooksById(Long id);

    /**
     * 查询教材软著列表
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 教材软著集合
     */
    public List<SciJcrzTextbooks> selectSciJcrzTextbooksList(SciJcrzTextbooks sciJcrzTextbooks);

    /**
     * 新增教材软著
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 结果
     */
    public int insertSciJcrzTextbooks(SciJcrzTextbooks sciJcrzTextbooks);

    /**
     * 修改教材软著
     *
     * @param sciJcrzTextbooks 教材软著
     * @return 结果
     */
    public int updateSciJcrzTextbooks(SciJcrzTextbooks sciJcrzTextbooks);

    /**
     * 删除教材软著
     *
     * @param id 教材软著主键
     * @return 结果
     */
    public int deleteSciJcrzTextbooksById(Long id);

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSciJcrzTextbooksByIds(String[] ids);
}
