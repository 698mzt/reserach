package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.SciPaperCfg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SciPaperACfgMapper {
    public List<SciPaperCfg> selectSciPaperACfg(Integer id);
    public int insertSciPaperCfg(SciPaperCfg sciPaperAcfg);

    public int selectSciPaperACfgPoints(@Param("order")String order, @Param("user_order")String user_order);
}
