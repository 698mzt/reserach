package com.ruoyi.system.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
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
    public List<SciPaperA> selectSciPaperAListAll(SciPaperA sciPaperA);
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
     * 导出论文查询
     */
    public List<SciPaperA> selectSciPaperAExport(List<String> ListRowId,SciPaperA sciPaperA);
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

    public List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA);

    public Integer selectSciPaperA(SciPaperA paper);
    
    /**
     * 实时计算论文科研分
     * @param paperCategory 论文类别
     * @param authors 作者信息，key为作者排名，value为用户ID
     * @param communicationAuthorId 通讯作者ID
     * @return 各作者科研分，key为作者排名+用户ID，value为分数
     */
    public Map<String, Integer> calculatePaperScore(String paperCategory, Map<String, String> authors, String communicationAuthorId);

    /**
     * 统计查询论文数据
     *
     * @param params 查询参数
     * @return 论文集合
     */
    List<SciPaperA> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询论文数据
     *
     * @param params 查询参数
     * @return 论文集合
     */
    List<SciPaperA> getStatsQueryToCheck(Map<String, String> params);
}
