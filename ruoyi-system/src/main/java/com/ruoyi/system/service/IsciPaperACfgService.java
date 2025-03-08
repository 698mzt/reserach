package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciPaperCfg;

import java.util.List;

public interface IsciPaperACfgService {
    public List<SciPaperCfg> selectSciPaperACfg(Integer id);

    public int insertSciPaperCfg(SciPaperCfg sciPaperAcfg);

}
