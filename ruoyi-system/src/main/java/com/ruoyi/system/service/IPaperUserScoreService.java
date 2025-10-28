package com.ruoyi.system.service;

import com.ruoyi.system.domain.Paper_user_score;

import java.util.List;

public interface IPaperUserScoreService {
    /**
     * 批量插入论文作者得分记录
     */
    int batchInsertPaperUserScore(List<Paper_user_score> paperUserScoreList);
    
    /**
     * 根据论文ID删除作者得分记录
     */
    int deletePaperUserScoreByPaperId(Long paperId);
    
    /**
     * 根据论文ID查询作者得分记录
     */
    List<Paper_user_score> getPaperUserScoreListByPaperId(Long paperId);
}
