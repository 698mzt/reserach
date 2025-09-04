package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.StatisticJZMapper;
import com.ruoyi.system.service.IStatisticYJCGSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IStatisticYJCGSServiceImpl implements IStatisticYJCGSService {

    @Autowired
    private StatisticJZMapper statisticJZMapper;

    // 横向课题字段（需要特殊计算）
    private static final List<String> HORIZONTAL_FIELDS = Arrays.asList(
        "ewyyx", "edwwy", "wdswy", "sdeswy", "esdsswwy", "wswwydwswy", "wsdqswwy", "qswdybwy"
    );

    // 纵向课题字段（需要特殊计算）
    private static final List<String> VERTICAL_FIELDS = Arrays.asList(
        "zcgjjjkyxm", "zcsbjjjkyxm", "zcsbjjgxm", "zcsbjzxkyxm", 
        "zctjjxhjkyxm", "zcxjjxglxmywyys", "zcxjjxglxmywyyx"
    );

    // 成果转化字段（需要特殊计算）
    private static final List<String> ACHIEVEMENT_FIELDS = Arrays.asList(
        "eyxxx", "edwxx", "wdsxx", "sdesxx", "esdsswxx", "sswdwsxx", "dywsxx"
    );

    // 学术论文字段
    private static final List<String> PAPER_FIELDS = Arrays.asList(
        "SCI", "EI", "hx", "sw", "pt", "xb"
    );

    // 教材著作字段
    private static final List<String> BOOK_FIELDS = Arrays.asList(
        "cbzz1", "cbzz2", "cbyz1", "cbyz2", "cbjc1", "cbjc2", "zbjc"
    );

    // 专利字段
    private static final List<String> PATENT_FIELDS = Arrays.asList(
        "sqfmzl", "syxxzl", "wxsjzl"
    );

    // 软著字段
    private static final List<String> SOFTWARE_FIELDS = Arrays.asList(
        "jsjrjzzq"
    );

    // 奖励类字段
    private static final List<String> AWARD_FIELDS = Arrays.asList(
        "sjjxcgj", "sjkxjsj", "sjyljpkpb", "stjjzrkxlpj", "stjjsklpj", "xjjxcgj", "yyxkyxjcgj", "xjjpkpb"
    );

    // 学术报告字段
    private static final List<String> REPORT_FIELDS = Arrays.asList(
        "jbgj", "jbgn", "cjgj", "cjgn", "xjxs"
    );

    @Override
    public List<Map<String, Object>> selectYJCGSJYS(String deptId) {
        // 1. 查询所有小类数据
        List<Map<String, Object>> rawData = statisticJZMapper.selectAllJZ(deptId);

        String page = "jys";
        // 2. 将小类数据合并成大类
        List<Map<String, Object>> mergedData = mergeToCategories(rawData);
        
        // 3. 计算总计行
        Map<String, Object> totalRow = calculateTotalRow(mergedData, page);
        
        // 4. 将总计行添加到结果中
        mergedData.add(totalRow);
        
        return mergedData;
    }

    @Override
    public List<Map<String, Object>> selectYJCGSXY(Long parentId) {
        // 1. 查询该学院下所有教研室的小类数据
        List<Map<String, Object>> rawData = statisticJZMapper.selectYJCGSXY(parentId);

        // 2. 按教研室分组处理数据
        Map<String, List<Map<String, Object>>> deptGroupedData = new HashMap<>();
        
        // 按教研室分组
        for (Map<String, Object> row : rawData) {
            String deptName = (String) row.get("deptName");
            if (deptName != null && !deptName.isEmpty()) {
                deptGroupedData.computeIfAbsent(deptName, k -> new ArrayList<>()).add(row);
            }
        }
        
        // 3. 为每个教研室计算汇总数据
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : deptGroupedData.entrySet()) {
            String deptName = entry.getKey();
            List<Map<String, Object>> deptData = entry.getValue();
            
            // 合并该教研室的数据
            List<Map<String, Object>> mergedDeptData = mergeToCategories(deptData);
            
            // 计算该教研室的总计
            Map<String, Object> deptTotalRow = calculateDeptTotalForCollege(mergedDeptData, deptName);
            result.add(deptTotalRow);
        }

        // 4. 计算学院总计行
        String page = "xy";
        Map<String, Object> collegeTotalRow = calculateTotalRow(result, page);

        // 5. 将学院总计行添加到结果中
        result.add(collegeTotalRow);

        return result;
    }


    /**
     * 将小类数据合并成大类
     * @param rawData 原始小类数据
     * @return 合并后的大类数据
     */
    private List<Map<String, Object>> mergeToCategories(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map<String, Object> row : rawData) {
            Map<String, Object> mergedRow = new HashMap<>();
            
            // 基本信息
            mergedRow.put("parentName", row.get("partenName"));
            mergedRow.put("deptName", row.get("deptName"));
            mergedRow.put("userName", row.get("userName"));
            
            // 合并横向课题科研项目（特殊计算）
            mergedRow.put("横向课题科研项目", mergeSpecialFields(row, HORIZONTAL_FIELDS));
            
            // 合并纵向科研项目-校级以上（特殊计算）
            List<String> verticalAbove = Arrays.asList("zcgjjjkyxm", "zcsbjjjkyxm", "zcsbjjgxm", "zcsbjzxkyxm", "zctjjxhjkyxm");
            mergedRow.put("纵向科研项目-校级以上", mergeSpecialFields(row, verticalAbove));
            
            // 合并纵向科研项目-校级（特殊计算）
            List<String> verticalSchool = Arrays.asList("zcxjjxglxmywyys", "zcxjjxglxmywyyx");
            mergedRow.put("纵向科研项目-校级", mergeSpecialFields(row, verticalSchool));
            
            // 合并成果转化（特殊计算）
            mergedRow.put("成果转化", mergeSpecialFields(row, ACHIEVEMENT_FIELDS));
            
            // 合并学术论文（普通计算）
            int paperCount = mergeNormalFields(row, PAPER_FIELDS);
            mergedRow.put("学术论文", paperCount > 0 ? paperCount + "个" : "0个");
            
            // 合并教材著作（普通计算）
            int bookCount = mergeNormalFields(row, BOOK_FIELDS);
            mergedRow.put("教材著作", bookCount > 0 ? bookCount + "个" : "0个");
            
            // 合并专利（普通计算）
            int patentCount = mergeNormalFields(row, PATENT_FIELDS);
            mergedRow.put("专利", patentCount > 0 ? patentCount + "个" : "0个");
            
            // 合并软著（普通计算）
            int softwareCount = mergeNormalFields(row, SOFTWARE_FIELDS);
            mergedRow.put("软著", softwareCount > 0 ? softwareCount + "个" : "0个");
            
            // 合并奖励类（普通计算）
            int awardCount = mergeNormalFields(row, AWARD_FIELDS);
            mergedRow.put("奖励", awardCount > 0 ? awardCount + "个" : "0个");
            
            // 合并学术报告（普通计算）
            int reportCount = mergeNormalFields(row, REPORT_FIELDS);
            mergedRow.put("学术报告(讲座类)", reportCount > 0 ? reportCount + "个" : "0个");
            
            result.add(mergedRow);
        }
        
        return result;
    }

    /**
     * 合并特殊字段（需要保持原格式进行复杂计算）
     * @param row 数据行
     * @param fields 字段列表
     * @return 合并后的字符串
     */
    private String mergeSpecialFields(Map<String, Object> row, List<String> fields) {
        Map<Double, Integer> amountCountMap = new HashMap<>();
        
        for (String field : fields) {
            Object value = row.get(field);
            if (value instanceof String) {
                parseAndCountAmounts((String) value, amountCountMap);
            }
        }
        
        return buildResultString(amountCountMap);
    }

    /**
     * 合并普通字段（直接累加数量）
     * @param row 数据行
     * @param fields 字段列表
     * @return 累加后的数量
     */
    private int mergeNormalFields(Map<String, Object> row, List<String> fields) {
        int total = 0;
        
        for (String field : fields) {
            Object value = row.get(field);
            if (value instanceof String) {
                total += parseNumberFromField((String) value);
            }
        }
        
        return total;
    }

    /**
     * 计算教研室在学院页面的总计行
     * @param mergedData 合并后的数据
     * @param deptName 教研室名称
     * @return 教研室总计行
     */
    private Map<String, Object> calculateDeptTotalForCollege(List<Map<String, Object>> mergedData, String deptName) {
        Map<String, Object> totalRow = new HashMap<>();
        totalRow.put("deptName", deptName);
        
        // 从第一行数据中获取parentName
        if (!mergedData.isEmpty()) {
            Object parentName = mergedData.get(0).get("parentName");
            totalRow.put("parentName", parentName);
        }
        
        // 特殊计算字段
        List<String> specialFields = Arrays.asList("横向课题科研项目", "纵向科研项目-校级以上", "纵向科研项目-校级", "成果转化");
        for (String field : specialFields) {
            Map<Double, Integer> amountCountMap = new HashMap<>();
            
            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    parseAndCountAmounts((String) value, amountCountMap);
                }
            }
            
            totalRow.put(field, buildResultString(amountCountMap));
        }
        
        // 普通计算字段
        List<String> normalFields = Arrays.asList("学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)");
        for (String field : normalFields) {
            int total = 0;
            
            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    total += parseNumberFromField((String) value);
                }
            }
            
            totalRow.put(field, total > 0 ? total + "个" : "0个");
        }
        
        return totalRow;
    }

    /**
     * 计算总计行
     * @param mergedData 合并后的数据
     * @return 总计行
     */
    private Map<String, Object> calculateTotalRow(List<Map<String, Object>> mergedData, String page) {
        Map<String, Object> totalRow = new HashMap<>();
        if (page.equals("jys"))
            totalRow.put("userName", "总计");
        if (page.equals("xy")) {
            totalRow.put("deptName", "总计");
            // 从第一行数据中获取parentName
            if (!mergedData.isEmpty()) {
                Object parentName = mergedData.get(0).get("parentName");
                totalRow.put("parentName", parentName);
            }
        }
        if (page.equals("xx"))
            totalRow.put("parentName", "总计");
        
        // 特殊计算字段
        List<String> specialFields = Arrays.asList("横向课题科研项目", "纵向科研项目-校级以上", "纵向科研项目-校级", "成果转化");
        for (String field : specialFields) {
            Map<Double, Integer> amountCountMap = new HashMap<>();
            
            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    parseAndCountAmounts((String) value, amountCountMap);
                }
            }
            
            totalRow.put(field, buildResultString(amountCountMap));
        }
        
        // 普通计算字段
        List<String> normalFields = Arrays.asList("学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)");
        for (String field : normalFields) {
            int total = 0;
            
            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    total += parseNumberFromField((String) value);
                }
            }
            
            // 如果总计为0，则显示空字符串
            totalRow.put(field, total > 0 ? total + "个" : "0个");
        }
        
        return totalRow;
    }

    /**
     * 解析字段值并累加相同金额的数量
     * @param fieldValue 字段值，格式如"3个(10万)，2个(20万)"
     * @param amountCountMap 金额计数映射
     */
    private void parseAndCountAmounts(String fieldValue, Map<Double, Integer> amountCountMap) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return;
        }
        
        // 解析数据，提取数量和金额
        Pattern pattern = Pattern.compile("(\\d+)个[（(](\\d+\\.?\\d*)万[)）]");
        String[] items = fieldValue.split("[,，]");
        
        for (String item : items) {
            Matcher matcher = pattern.matcher(item.trim());
            if (matcher.matches()) {
                // 提取数量和金额
                int count = Integer.parseInt(matcher.group(1));
                double amount = Double.parseDouble(matcher.group(2));
                
                // 过滤掉0个（0万）或0个（0.00万）的情况
                if (count == 0 || amount == 0.0) {
                    continue;
                }
                
                // 累加相同金额的数量
                amountCountMap.put(amount, amountCountMap.getOrDefault(amount, 0) + count);
            }
        }
    }

    /**
     * 构建结果字符串
     * @param amountCountMap 金额计数映射
     * @return 结果字符串，格式如"2个（10万），1个（20万）"
     */
    private String buildResultString(Map<Double, Integer> amountCountMap) {
        // 如果没有有效数据，返回空字符串
        if (amountCountMap.isEmpty()) {
            return "0个(0万)";
        }
        
        // 按金额排序
        List<Double> amounts = new ArrayList<>(amountCountMap.keySet());
        Collections.sort(amounts);
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < amounts.size(); i++) {
            double amount = amounts.get(i);
            int count = amountCountMap.get(amount);
            
            if (i > 0) {
                result.append("，");
            }
            
            // 格式化金额
            String amountStr = String.format("%.2f", amount);
            
            result.append(count).append("个（").append(amountStr).append("万）");
        }
        
        return result.toString();
    }

    /**
     * 从字段值中解析数字
     * @param fieldValue 字段值
     * @return 解析出的数字
     */
    private int parseNumberFromField(String fieldValue) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return 0;
        }
        
        // 使用正则表达式匹配数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(fieldValue);
        
        int totalCount = 0;
        while (matcher.find()) {
            totalCount += Integer.parseInt(matcher.group());
        }
        
        return totalCount;
    }
}
