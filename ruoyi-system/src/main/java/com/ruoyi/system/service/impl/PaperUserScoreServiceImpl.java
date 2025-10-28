package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Paper_user_score;
import com.ruoyi.system.mapper.PaperUserScoreServiceMapper;
import com.ruoyi.system.service.IPaperUserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperUserScoreServiceImpl implements IPaperUserScoreService {
    @Autowired
    private PaperUserScoreServiceMapper paperUserScoreServiceImplMapper;
    
    @Override
    public int batchInsertPaperUserScore(List<Paper_user_score> paperUserScoreList) {
        if (paperUserScoreList == null || paperUserScoreList.isEmpty()) {
            return 0;
        }
        return paperUserScoreServiceImplMapper.batchInsertPaperUserScore(paperUserScoreList);
    }
    
    @Override
    public int deletePaperUserScoreByPaperId(Long paperId) {
        return paperUserScoreServiceImplMapper.deletePaperUserScoreByPaperId(paperId);
    }
    
    @Override
    public List<Paper_user_score> getPaperUserScoreListByPaperId(Long paperId) {
        return paperUserScoreServiceImplMapper.getpaperUserScoreListByPaperId(paperId);
    }
}
