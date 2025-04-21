package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SciPaperAr;

/**
 * 论文Service接口
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
public interface ISciPaperAService 
{
    /**
     * 查询论文
     * 
     * @param id 论文主键
     * @return 论文
     */
    public SciPaperA selectSciPaperAById(Long id);



    List<SciPaperAr> selectSciPaperArList(SciPaperAr sciPaperAr);
    /**
     * 查询论文列表
     * 
     * @param sciPaperA 论文
     * @return 论文集合
     */
    public List<SciPaperA> selectSciPaperAList(SciPaperA sciPaperA);
    public List<SciPaperA> selectSciPaperAListKY(SciPaperA sciPaperA);
    public List<SciPaperA> selectSciPaperAListXY(SciPaperA sciPaperA);
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
     * 批量删除论文
     * 
     * @param ids 需要删除的论文主键集合
     * @return 结果
     */
    public int deleteSciPaperAByIds(String ids);

    /**
     * 删除论文信息
     *
     * @param id 论文主键
     * @return 结果
     */
    public int deleteSciPaperAById(Long id);
    public int updateSciPaperAState(Integer id);

    public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA);

    List<String> selectSciPaperAByroleId(Long userId);

    List<SciPaperA> selectSciPaperArole(Long userId);

    int pytg(String id, Long uid, String urlFlag,String order,String user_order);

    int pybh(String id, Long userId, String remark, String urlFlag);

    List<SciPaperA> selectAllPaperName(String query);
}
