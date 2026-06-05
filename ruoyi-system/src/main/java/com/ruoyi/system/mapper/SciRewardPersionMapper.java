package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciRewardPersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 奖励成员Mapper接口
 * 
 * 用于奖励模块的成员关联数据操作，支持成员账号登录后查看自己参与的奖励项目
 * 
 * @author wh
 * @date 2024-12-23
 */
public interface SciRewardPersionMapper {

    /**
     * 插入奖励成员记录
     * 
     * @param sciRewardPersion 成员信息
     * @return 影响行数
     */
    int insertPersion(SciRewardPersion sciRewardPersion);

    /**
     * 根据奖励ID删除成员记录
     * 
     * @param rewardId 奖励ID
     * @return 影响行数
     */
    int deletePersionByRewardId(@Param("rewardId") Integer rewardId);

    /**
     * 批量删除成员记录
     * 
     * @param ids 成员ID数组
     * @return 影响行数
     */
    int deletePersionByIds(@Param("ids") String[] ids);

    /**
     * 根据奖励ID查询成员ID列表
     * 
     * @param rewardId 奖励ID
     * @return 成员ID列表
     */
    List<String> selectPersionIdsByRewardId(@Param("rewardId") Integer rewardId);

    /**
     * 根据奖励ID查询成员详细信息列表
     * 
     * @param rewardId 奖励ID
     * @return 成员信息列表
     */
    List<SciRewardPersion> selectPersionsByRewardId(@Param("rewardId") Integer rewardId);

    /**
     * 根据成员ID查询参与的奖励关联列表
     * 
     * 核心方法：用于成员账号登录后查询自己参与的所有奖励项目
     * 
     * @param persionId 成员用户ID
     * @return 奖励成员关联列表
     */
    List<SciRewardPersion> selectPersionsByPersionId(@Param("persionId") String persionId);

    /**
     * 更新成员积分信息
     * 
     * @param sciRewardPersion 成员信息（含积分字段）
     * @return 影响行数
     */
    int updatePersionScore(SciRewardPersion sciRewardPersion);

    /**
     * 根据奖励ID和成员ID查询成员记录
     * 
     * 核心方法：用于查询当前登录用户在某个奖励中的个人积分信息
     * 
     * @param rewardId 奖励ID
     * @param persionId 成员用户ID
     * @return 成员记录（包含排名、预计积分、实际积分）
     */
    SciRewardPersion selectPersionByRewardIdAndPersionId(
            @Param("rewardId") Integer rewardId, 
            @Param("persionId") String persionId);
}