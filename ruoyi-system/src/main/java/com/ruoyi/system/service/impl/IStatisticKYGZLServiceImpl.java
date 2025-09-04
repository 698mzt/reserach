package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.SciJYSKYMapper;
import com.ruoyi.system.service.IStatisticKYGZLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科研工作量
 * */
@Service
public class IStatisticKYGZLServiceImpl implements IStatisticKYGZLService {

    @Autowired
    private SciJYSKYMapper sciJYSKYMapper;

    /**
     * 教研室
     * */
    @Override
    public List<Map<String, Object>> selectKYGZLJYS(Long deptId) {
        List<Map<String, Object>> list = sciJYSKYMapper.selectKYGZLJYS(deptId);
        
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

    /**
     * 学院
     * */
    @Override
    public List<Map<String, Object>> selectKYGZLXY(Long parentId) {
        List<Map<String, Object>> list = sciJYSKYMapper.selectKYGZLXY(parentId);
        
        // 创建一个Map来存储各字段的总计
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("partenName", "");
        totalMap.put("deptName", "总计");
        
        // 初始化所有数值字段为0
        double hxktTotal = 0.0;
        double zxktxjTotal = 0.0;
        double zxktxjysTotal = 0.0;
        double cgzhTotal = 0.0;
        double xslwTotal = 0.0;
        double jczzTotal = 0.0;
        double zlrzTotal = 0.0;
        double jlTotal = 0.0;
        double jzbgTotal = 0.0;
        
        // 遍历列表，累加各字段的值
        for (Map<String, Object> map : list) {
            // 安全地获取并累加数值，处理可能的null值和不同数据类型
            hxktTotal += getDoubleValue(map.get("hxkt"));
            zxktxjTotal += getDoubleValue(map.get("zxktxj"));
            zxktxjysTotal += getDoubleValue(map.get("zxktxjys"));
            cgzhTotal += getDoubleValue(map.get("cgzh"));
            xslwTotal += getDoubleValue(map.get("xslw"));
            jczzTotal += getDoubleValue(map.get("jczz"));
            zlrzTotal += getDoubleValue(map.get("zlrz"));
            jlTotal += getDoubleValue(map.get("jl"));
            jzbgTotal += getDoubleValue(map.get("jzbg"));
        }
        
        // 将总计以整数形式存储到totalMap中
        totalMap.put("hxkt", (int)Math.round(hxktTotal));
        totalMap.put("zxktxj", (int)Math.round(zxktxjTotal));
        totalMap.put("zxktxjys", (int)Math.round(zxktxjysTotal));
        totalMap.put("cgzh", (int)Math.round(cgzhTotal));
        totalMap.put("xslw", (int)Math.round(xslwTotal));
        totalMap.put("jczz", (int)Math.round(jczzTotal));
        totalMap.put("zlrz", (int)Math.round(zlrzTotal));
        totalMap.put("jl", (int)Math.round(jlTotal));
        totalMap.put("jzbg", (int)Math.round(jzbgTotal));
        
        // 将总计Map添加到结果列表中
        list.add(totalMap);
        
        return list;
    }
    
    /**
     * 安全地将Object转换为double值
     * @param value 要转换的值
     * @return double值，如果转换失败则返回0.0
     */
    private double getDoubleValue(Object value) {
        if (value == null) {
            return 0.0;
        }
        
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                String strValue = ((String) value).trim();
                if (strValue.isEmpty()) {
                    return 0.0;
                }
                return Double.parseDouble(strValue);
            }
        } catch (NumberFormatException e) {
            // 如果转换失败，返回0.0
            return 0.0;
        }
        
        return 0.0;
    }

//    /**
//     * 学校
//     * */
//    @Override
//    public List<Map<String, Object>> selectKYGZLXX(Long deptId) {
//        return List.of();
//    }
}
