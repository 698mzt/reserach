package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.SciUserScore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SciUserScoreMapper {
    /** 插入积分*/
    public int insertScoreHistory(SciUserScore sciUserScore);

    /** 查询指定id积分*/
    public List<SciUserScore> selectScoreHistoryById(Integer id);

    /** 删除指定id的积分*/
    public int deleteScoreById(@Param("id")String id, @Param("status")String status);

    List<SciUserScore> selectScoreVerticalById(Integer id);

    void insertScoreVertical(SciUserScore sciUserScore);

}
