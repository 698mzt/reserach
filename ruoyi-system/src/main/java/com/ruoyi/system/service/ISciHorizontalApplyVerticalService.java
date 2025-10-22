package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciHorizontalApply;
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


    int applyPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId);

    int applyBh(String id, Long userId, String remark, String urlFlag);

    int overPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId);

    int overBh(String id, Long userId, String remark, String urlFlag);


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

    /**
     * 导出纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalAllList(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    int recall(Integer id, String state, Long userId, String remark, String urlFlag);


    List<SciHorizontalApplyVertical> selectOtherListByUid(SciHorizontalApplyVertical sciHorizontalApplyVertical);

    /**
     * 按顺序保存“其他成员”（第5位开始）到 sci_persion_vertical
     */
    void saveVerticalPersons(Integer verticalId, java.util.List<String> personIds);

    /**
     * 查询纵向课题的“其他成员”
     * */
    List<String> selectPersionIdsByVerticalId(Integer id);

    void resetVerticalPersons(Integer id, List<String> all);
}
