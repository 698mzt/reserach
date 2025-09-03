package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.SciJYSKYMapper;
import com.ruoyi.system.service.IStatisticKYGZLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IStatisticKYGZLServiceImpl implements IStatisticKYGZLService {

    @Autowired
    private SciJYSKYMapper sciJYSKYMapper;

    @Override
    public List<Map<String, Object>> selectKYGZLJYS(Long deptId) {
        List<Map<String, Object>> list = sciJYSKYMapper.selectJYSKY(deptId);
        
        // 创建一个Map来存储各字段的总计
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("userName", "总计");
        totalMap.put("hxkt", 0);
        totalMap.put("zxktxj", 0);
        totalMap.put("zxktxjys", 0);
        totalMap.put("cgzh", 0);
        totalMap.put("xslw", 0);
        totalMap.put("jczz", 0);
        totalMap.put("zlrz", 0);
        totalMap.put("jl", 0);
        totalMap.put("jzbg", 0);
        
        // 遍历列表，累加各字段的值
        for (Map<String, Object> map : list) {
            totalMap.put("hxkt", (Integer)totalMap.get("hxkt") + Integer.parseInt((String)map.get("hxkt")));
            totalMap.put("zxktxj", (Integer)totalMap.get("zxktxj") + Integer.parseInt((String)map.get("zxktxj")));
            totalMap.put("zxktxjys", (Integer)totalMap.get("zxktxjys") + Integer.parseInt((String)map.get("zxktxjys")));
            totalMap.put("cgzh", (Integer)totalMap.get("cgzh") + Integer.parseInt((String)map.get("cgzh")));
            totalMap.put("xslw", (Integer)totalMap.get("xslw") + Integer.parseInt((String)map.get("xslw")));
            totalMap.put("jczz", (Integer)totalMap.get("jczz") + Integer.parseInt((String)map.get("jczz")));
            totalMap.put("zlrz", (Integer)totalMap.get("zlrz") + Integer.parseInt((String)map.get("zlrz")));
            totalMap.put("jl", (Integer)totalMap.get("jl") + Integer.parseInt((String)map.get("jl")));
            totalMap.put("jzbg", (Integer)totalMap.get("jzbg") + Integer.parseInt((String)map.get("jzbg")));
        }
        
        // 将总计Map添加到结果列表中
        list.add(totalMap);
        
        return list;
    }
}
