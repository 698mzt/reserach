package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Paper_user_score;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperUserScoreServiceMapper {
    /**
     * 批量插入论文作者得分记录
     */
    int batchInsertPaperUserScore(List<Paper_user_score> paperUserScoreList);
    
    /**
     * 根据论文ID删除作者得分记录
     */
    int deletePaperUserScoreByPaperId(Long paperId);

    List<Paper_user_score> getpaperUserScoreListByPaperId(Long paperId);

    int updateScoreById(@Param("pusId")Long pusId, @Param("points")Integer points);

    int updateScoreByPaperId(@Param("paperId")Long paperId, @Param("points")int points);
}
