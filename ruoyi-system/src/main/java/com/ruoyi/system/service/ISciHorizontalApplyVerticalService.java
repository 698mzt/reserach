package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApplyVertical;

import java.util.List;

public interface ISciHorizontalApplyVerticalService {
    /**
     * 查询纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题主键
     * @return 纵向课题
     */
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalList(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 保存立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    public int insertSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * id查询立项申请
     *
     * @param id 纵向课题
     * @return 结果
     */
    public SciHorizontalApplyVertical selectSciHorizontalApplyVerticalById(Integer id);

    /**
     * 修改立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    public int updateSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 删除纵向课题信息
     *
     * @param ids 纵向课题主键
     * @return 结果
     */
    int deleteSciHorizontalApplyVerticalByIds(String ids);


    int applyPass(String id, Long userId, String urlFlag);

    int applyBh(String id, Long userId, String remark, String urlFlag);

    int openPass(String id, Long userId, String urlFlag);

    int openBh(String id, Long userId, String remark, String urlFlag);

    int midPass(String id, Long userId, String urlFlag);

    int midBh(String id, Long userId, String remark, String urlFlag);

    int overPass(String id, Long userId, String urlFlag);

    int overBh(String id, Long userId, String remark, String urlFlag);

//    开题列表
    /**
     * 查询开题申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListKT(SciHorizontalApplyVertical sciHorizontalApplyVertical);

     /**
     * 查询中期申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListZQ(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 查询结项申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJX(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 查询结项申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListOVER(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    }
