package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.SciUserScore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SciUserScoreMapper {
    /** 插入积分*/
    public int insertScoreHistory(SciUserScore sciUserScore);

    /** 查询指定id积分*/
    public List<SciUserScore> selectScoreHistoryById(Integer id);

    /** 批量查询指定applyIds的积分*/
    public List<SciUserScore> selectScoreHistoryByApplyIds(@Param("applyIds") Set<Integer> applyIds);

    /** 批量查询指定verticalIds的积分*/
    public List<SciUserScore> selectScoreVerticalByApplyIds(@Param("applyIds") Set<Integer> applyIds);

    /** 根据用户ID和课题ID查询该成员上一次插入的预期科研分 */
    public String selectLastExpectedValueByUserIdAndVerticalId(@Param("userId") String userId, @Param("verticalId") String verticalId);

    /** 删除指定id的积分*/
    public int deleteScoreById(@Param("id")String id, @Param("status")String status);
    public int deleteVerticalScoreById(@Param("id")String id, @Param("status")String status);

    List<SciUserScore> selectScoreVerticalById(Integer id);

    void insertScoreVertical(SciUserScore sciUserScore);

}
