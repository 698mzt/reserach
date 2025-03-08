package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPersion;
import org.apache.ibatis.annotations.Options;

/**
 * 横向课题Service接口
 *
 * @author zhansan
 * @date 2024-08-16
 */
public interface ISciHorizontalApplyService
{
    /**
     * 查询横向课题
     *
     * @param id 横向课题主键
     * @return 横向课题
     */
    public SciHorizontalApply selectSciHorizontalApplyById(Integer id);

    /**
     * 查询横向课题列表
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题集合
     */
    public List<SciHorizontalApply> selectSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply);

    /**
     * 新增横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply);




    /**
     * 修改横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    public int updateSciHorizontalApply(SciHorizontalApply sciHorizontalApply);

    /**
     * 批量删除横向课题
     *
     * @param ids 需要删除的横向课题主键集合
     * @return 结果
     */
    public int deleteSciHorizontalApplyByIds(String ids);

    /**
     * 删除横向课题信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalApplyById(Integer id);

    /**
     * 删除横向课题另一个信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    public int deleteSciHorizontalOverApplyById(Integer id);

    int hxPass(String id,Long uid,String urlFlag);
    int amountPass(String id,String reid,Long uid,String urlFlag,List score,List persion,Integer applyId,String amountType);
    int hxover(String id,Long uid,String urlFlag);

    int hxBh(String id,Long uid, String remark,String urlFlag);
    int hxoverBh(String id, Long userId, String remark, String urlFlag);

    List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApply(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyJYS(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyKYC(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOVER(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOVERKYC(SciHorizontalApply sciHorizontalApply);

    int overApply(String id, String state);


    List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply);
    //申请结项流程
    int overSaveSciHorizontalApply(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply);

    int deletePersionByid(String ids);

    List<SciHorizontalApply> selectSciHorizontalApplyListByDept(SciHorizontalApply sciHorizontalApply);

    List<SciHorizontalApply> selectSciHorizontalApplyListByOverDept(SciHorizontalApply sciHorizontalApply);

    public int sci_horizontal_piyue(Integer id);

//    int rercallState(Integer id);

    int recall(Integer id, String newState,Long uid, String remark, String urlFlag);


    int amountBh(String id, String reid, Long userId, String remark, String urlFlag);
}
