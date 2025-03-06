package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SciPaperCfg;
import com.ruoyi.system.mapper.SciPaperACfgMapper;
import com.ruoyi.system.service.IsciPaperACfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SciPaperACfgServicelmpl implements IsciPaperACfgService {
    @Autowired
    private SciPaperACfgMapper sciPaperACfgMapper;
    @Override
    public List<SciPaperCfg> selectSciPaperACfg(Integer id) {
        return sciPaperACfgMapper.selectSciPaperACfg(id);
    }

    @Override
    public int insertSciPaperCfg(SciPaperCfg sciPaperAcfg) {
        return sciPaperACfgMapper.insertSciPaperCfg(sciPaperAcfg);
    }


}
