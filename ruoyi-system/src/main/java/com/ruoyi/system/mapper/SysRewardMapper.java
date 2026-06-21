package com.ruoyi.system.mapper;
import com.ruoyi.system.domain.SysReward;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 奖励Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-23
 */
public interface SysRewardMapper
{
    /**
     * 查询奖励
     *
     * @param id 奖励主键
     * @return 奖励
     */
    public SysReward selectSysRewardById(Long id);

    /**
     * 管理员：所有条件查询奖励列表
     *
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardList(SysReward sysReward);

    /**
     * 教师：仅课题名称查询奖励列表
     *
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardListCx(SysReward sysReward);

    /**
     * 教研室：课题名称+主持人查询奖励列表
     *
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardListCxList(SysReward sysReward);

    /**
     * 学院：课题名称+主持人+专业查询奖励列表
     *
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardListxY(SysReward sysReward);

    /**
     * 科研处：课题名称+主持人+专业+学院查询奖励列表
     *
     * @param sysReward 奖励
     * @return 奖励集合
     */
    public List<SysReward> selectSysRewardListKY(SysReward sysReward);

    /**
     * 新增奖励
     *
     * @param sysReward 奖励
     * @return 结果
     */
    public int insertSysReward(SysReward sysReward);

    /**
     * 修改奖励
     *
     * @param sysReward 奖励
     * @return 结果
     */
    public int updateSysReward(SysReward sysReward);

    public int updateApprovalEditableFields(SysReward sysReward);

    /**
     * 删除奖励
     *
     * @param id 奖励主键
     * @return 结果
     */
    public int deleteSysRewardById(Long id);

    /**
     * 批量删除奖励
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysRewardByIds(String[] ids);

    int hxPass(@Param("id") String id, @Param("state") String state);

    int hxBh(@Param("id") String id,@Param("state") String state);

    List<SysReward> selectSysRewardListByKYC(SysReward sysReward);

    List<SysReward> selectSysRewardListByJYS(SysReward sysReward);

    List<SysReward> selectSysRewardListByXUE(SysReward sysReward);

//    List<SysReward> selectSysRewardListByOverReward(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardJYS(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOverRewardKYC(SysReward sysReward);
//    List<SysReward> selectSysRewardListByOVER(SysReward sysReward);

    int overReward(@Param("id") String id,@Param("state") String state);

    void updateJifen(@Param("id")  Long id,@Param("jifen") int jifen);

    void resetJifenById(@Param("id") Long id);

    void updateExpectedJifen(@Param("id") Long id, @Param("expectedJifen") String expectedJifen);

    /**
     * 统计查询奖励数据
     *
     * @param params 查询参数
     * @return 奖励集合
     */
    List<SysReward> getStatsQuery(Map<String, String> params);

    /**
     * 核算查询奖励数据
     *
     * @param params 查询参数
     * @return 奖励集合
     */
    List<SysReward> getStatsQueryToCheck(Map<String, String> params);

    List<SysReward> selectRewardsByIds(@Param("rewardIds") List<Integer> rewardIds);

    /**
     * 根据成员ID查询参与的奖励列表（包含个人积分信息）
     * 
     * 核心方法：用于成员账号登录后查询自己参与的所有奖励项目
     * 通过关联表 sci_reward_persion 获取成员在每个奖励中的排名、预计积分和实际积分
     * 
     * @param persionId 成员用户ID
     * @param year 年份过滤条件
     * @return 奖励列表（包含个人积分信息）
     */
    List<SysReward> selectRewardsByPersionId(@Param("persionId") String persionId, @Param("year") String year);
}