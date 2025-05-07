package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SciPaperAr;
import org.apache.ibatis.annotations.Param;

/**
 * 论文Mapper接口
 *
 * @author ruoyi
 * @date 2024-11-07
 */
public interface SciPaperAMapper {
    /**
     * 查询论文
     *
     * @param id 论文主键
     * @return 论文
     */
    public SciPaperA selectSciPaperAById(Long id);
    public SciPaperA selectUser();
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

    public int insertSciPaperAr(SciPaperAr sciPaperAr);
    public int insertSciPaperArbh(SciPaperAr sciPaperAr);

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

    List<SciPaperA> selectSciPaperArole(Long userId);

    public List<String> selectSciPaperAByroleId(Long userId);

    public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA); //, @Param("userId") Long userId);

    int pytg(@Param("id") String id, @Param("state") String state);

    public int updateSciPaperAState(Integer id );

    List<SciPaperAr> selectSciPaperArList(SciPaperAr sciPaperAr);


    public int updateSciPaperArs(@Param("id")String id, @Param("points")int points);

    List<SciPaperA> selectSciPaperAListXY(SciPaperA sciPaperA);

    List<SciPaperA> selectSciPaperAListKY(SciPaperA sciPaperA);

    List<SciPaperA> selectAllPaperName(String query);

    List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA);
}
