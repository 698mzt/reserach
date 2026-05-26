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
        "sjjxcgj", "sjkxjsj", "stjjzrkxlpj", "stjjsklpj", "xjjxcgj", "yyxkyxjcgj", "xhjjxcgj", "xhjkycgj"
    );

    // 学术报告字段
    private static final List<String> REPORT_FIELDS = Arrays.asList(
        "jbgj", "jbgn", "cjgj", "cjgn", "xjxs"
    );

    @Override
    public List<Map<String, Object>> selectYJCGSJYS(String deptId, Long userId) {
        // 1. 查询所有小类数据
        List<Map<String, Object>> rawData = statisticJZMapper.selectAllJZ(deptId, userId);

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
        // 1. 查询所有教研室的小类数据（由parentId过滤）
        List<Map<String, Object>> rawData = statisticJZMapper.selectYJCGSXY(parentId);

        // 2. 按教研室分组处理数据
        Map<String, List<Map<String, Object>>> deptGroupedData = new LinkedHashMap<>();
        
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

        // 6. 非零优先排序，并保持总计行在末尾
        sortCollegeRows(result);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectYJCGSXYByCollege(Long collegeId) {
        // 1. 获取学院名称
        String collegeName = statisticJZMapper.selectDeptNameById(collegeId);
        if (collegeName == null) {
            collegeName = "";
        }

        // 2. 查询该学院下所有教研室的小类数据
        List<Map<String, Object>> rawData = statisticJZMapper.selectYJCGSXYByDeptId(collegeId);

        // 3. 覆盖parentName为学院名称
        for (Map<String, Object> row : rawData) {
            row.put("parentName", collegeName);
        }

        // 4. 按教研室分组
        Map<String, List<Map<String, Object>>> deptGroupedData = new LinkedHashMap<>();
        for (Map<String, Object> row : rawData) {
            String deptName = (String) row.get("deptName");
            if (deptName != null && !deptName.isEmpty()) {
                deptGroupedData.computeIfAbsent(deptName, k -> new ArrayList<>()).add(row);
            }
        }

        // 5. 为每个教研室计算汇总数据
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : deptGroupedData.entrySet()) {
            String deptName = entry.getKey();
            List<Map<String, Object>> deptData = entry.getValue();
            List<Map<String, Object>> mergedDeptData = mergeToCategories(deptData);
            Map<String, Object> deptTotalRow = calculateDeptTotalForCollege(mergedDeptData, deptName);
            result.add(deptTotalRow);
        }

        // 6. 计算学院总计行
        String page = "xy";
        Map<String, Object> collegeTotalRow = calculateTotalRow(result, page);
        result.add(collegeTotalRow);

        // 7. 非零优先排序
        sortCollegeRows(result);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectYJCGSXX() {
        List<Map<String, Object>> rawData = statisticJZMapper.selectYJCGSXX();
        Map<String, List<Map<String, Object>>> deptGroupedData = new LinkedHashMap<>();

        for (Map<String, Object> row : rawData) {
            String parentName = String.valueOf(row.get("partenName") == null ? "" : row.get("partenName")).trim();
            String deptName = String.valueOf(row.get("deptName") == null ? "" : row.get("deptName")).trim();
            if (parentName.isEmpty() || deptName.isEmpty()) {
                continue;
            }
            String groupKey = parentName + "||" + deptName;
            deptGroupedData.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(row);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : deptGroupedData.entrySet()) {
            List<Map<String, Object>> mergedDeptData = mergeToCategories(entry.getValue());
            if (mergedDeptData.isEmpty()) {
                continue;
            }
            String deptName = String.valueOf(mergedDeptData.get(0).get("deptName"));
            Map<String, Object> deptTotalRow = calculateDeptTotalForCollege(mergedDeptData, deptName);
            result.add(deptTotalRow);
        }

        Map<String, Object> schoolTotalRow = calculateTotalRow(result, "xx");
        result.add(schoolTotalRow);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectYJCGSKYC(String userName) {
        List<Map<String, Object>> rawData = statisticJZMapper.selectYJCGSKYC(userName);
        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> mergedData = mergeToCategories(rawData);
        if (!mergedData.isEmpty()) {
            Map<String, Object> totalRow = calculateTotalRow(mergedData, "xx");
            mergedData.add(totalRow);
        }
        return mergedData;
    }

    @Override
    public List<Map<String, Object>> selectAllYJCGSKYC(String userName) {
        List<Map<String, Object>> rawData = statisticJZMapper.selectAllYJCGSKYC(userName);
        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> mergedData = mergeToCategories(rawData);
        if (!mergedData.isEmpty()) {
            Map<String, Object> totalRow = calculateTotalRow(mergedData, "xx");
            mergedData.add(totalRow);
        }
        return mergedData;
    }

    @Override
    public List<Map<String, Object>> selectYJCGSKYCByDept(String userName) {
        // 1. 获取教师级别的合并数据
        List<Map<String, Object>> teacherData = selectYJCGSKYC(userName);
        if (teacherData == null || teacherData.isEmpty()) {
            return new ArrayList<>();
        }
        // 2. 过滤掉总计行，只保留教师数据行
        List<Map<String, Object>> teacherRows = new ArrayList<>();
        for (Map<String, Object> row : teacherData) {
            Object pn = row.get("parentName");
            if (!"总计".equals(pn)) {
                teacherRows.add(row);
            }
        }
        if (teacherRows.isEmpty()) {
            return new ArrayList<>();
        }
        // 3. 按教研组分组合并
        Map<String, List<Map<String, Object>>> deptGroups = new LinkedHashMap<>();
        for (Map<String, Object> row : teacherRows) {
            String deptName = (String) row.get("deptName");
            if (deptName == null || deptName.trim().isEmpty()) {
                deptName = (String) row.get("parentName");
            }
            if (deptName == null || deptName.trim().isEmpty()) {
                continue;
            }
            deptGroups.computeIfAbsent(deptName, k -> new ArrayList<>()).add(row);
        }
        // 4. 聚合每个教研室的教师数据
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : deptGroups.entrySet()) {
            result.add(aggregateDeptAchievementRow(entry.getKey(), entry.getValue()));
        }
        // 5. 非零优先排序
        sortCollegeRows(result);
        // 6. 添加总计行
        if (!result.isEmpty()) {
            Map<String, Object> totalRow = calculateTotalRow(result, "xx");
            result.add(totalRow);
        }
        return result;
    }

    /**
     * 将同一教研室的多个教师业绩成果行聚合为一行
     */
    private Map<String, Object> aggregateDeptAchievementRow(String deptName, List<Map<String, Object>> rows) {
        Map<String, Object> deptRow = new HashMap<>();
        if (!rows.isEmpty()) {
            deptRow.put("parentName", rows.get(0).get("parentName"));
        }
        deptRow.put("deptName", deptName);
        // 横向和成果转化（特殊计算，按明细格式显示）
        List<String> amountFields = Arrays.asList("横向课题科研项目", "成果转化");
        for (String field : amountFields) {
            Map<Double, Integer> amountCountMap = new HashMap<>();
            int totalCount = 0;
            double totalAmount = 0D;

            for (Map<String, Object> row : rows) {
                Object detailValue = row.get(field + "_明细");
                if (detailValue instanceof String) {
                    parseAndCountAmounts((String) detailValue, amountCountMap);
                }
                Object val = row.get(field + "_汇总");
                if (!(val instanceof String)) val = row.get(field);
                if (val instanceof String) {
                    AmountSummary summary = parseAmountSummary((String) val);
                    totalCount += summary.count;
                    totalAmount += summary.amount;
                }
            }

            String detailText = buildResultString(amountCountMap);
            String summaryText = buildSummaryString(totalCount, totalAmount);
            deptRow.put(field, detailText);
            deptRow.put(field + "_汇总", detailText);
            deptRow.put(field + "_明细", detailText);
        }
        // 纵向科研项目（纯个数格式）
        List<String> verticalFields = Arrays.asList("纵向科研项目-校级以上", "纵向科研项目-校级");
        for (String field : verticalFields) {
            int total = 0;
            for (Map<String, Object> row : rows) {
                Object val = row.get(field);
                if (val instanceof String) {
                    total += parseNumberFromField((String) val);
                }
            }
            deptRow.put(field, total > 0 ? total + "个" : "0个");
        }
        // 普通计算字段
        List<String> normalFields = Arrays.asList("学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)");
        for (String field : normalFields) {
            int total = 0;
            for (Map<String, Object> row : rows) {
                Object val = row.get(field);
                if (val instanceof String) {
                    total += parseNumberFromField((String) val);
                }
            }
            deptRow.put(field, total > 0 ? total + "个" : "0个");
        }
        return deptRow;
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
            String hxktSummary = mergeSpecialFields(row, HORIZONTAL_FIELDS);
            String hxktDetail = buildDetailString(row, HORIZONTAL_FIELDS);
            mergedRow.put("横向课题科研项目", hxktSummary);
            mergedRow.put("横向课题科研项目_汇总", hxktSummary);
            mergedRow.put("横向课题科研项目_明细", hxktDetail);
            mergedRow.put("hxktCnt", extractCountFromSpecial(hxktSummary));

            // 合并纵向科研项目-校级以上（纯个数格式）
            List<String> verticalAbove = Arrays.asList("zcgjjjkyxm", "zcsbjjjkyxm", "zcsbjjgxm", "zcsbjzxkyxm", "zctjjxhjkyxm");
            String zxktxjValue = mergeVerticalFields(row, verticalAbove);
            mergedRow.put("纵向科研项目-校级以上", zxktxjValue);
            mergedRow.put("zxktxjCnt", parseNumberFromField(zxktxjValue));

            // 合并纵向科研项目-校级（纯个数格式）
            List<String> verticalSchool = Arrays.asList("zcxjjxglxmywyys", "zcxjjxglxmywyyx");
            String zxktxjysValue = mergeVerticalFields(row, verticalSchool);
            mergedRow.put("纵向科研项目-校级", zxktxjysValue);
            mergedRow.put("zxktxjysCnt", parseNumberFromField(zxktxjysValue));

            // 合并成果转化（特殊计算）
            String cgzhSummary = mergeSpecialFields(row, ACHIEVEMENT_FIELDS);
            String cgzhDetail = buildDetailString(row, ACHIEVEMENT_FIELDS);
            mergedRow.put("成果转化", cgzhSummary);
            mergedRow.put("成果转化_汇总", cgzhSummary);
            mergedRow.put("成果转化_明细", cgzhDetail);
            mergedRow.put("cgzhCnt", extractCountFromSpecial(cgzhSummary));
            
            // 合并学术论文（普通计算）
            int paperCount = mergeNormalFields(row, PAPER_FIELDS);
            mergedRow.put("学术论文", paperCount > 0 ? paperCount + "个" : "0个");
            mergedRow.put("xslwCnt", paperCount);
            
            // 合并教材著作（普通计算）
            int bookCount = mergeNormalFields(row, BOOK_FIELDS);
            mergedRow.put("教材著作", bookCount > 0 ? bookCount + "个" : "0个");
            mergedRow.put("jczzCnt", bookCount);
            
            // 合并专利（普通计算）
            int patentCount = mergeNormalFields(row, PATENT_FIELDS);
            mergedRow.put("专利", patentCount > 0 ? patentCount + "个" : "0个");
            mergedRow.put("zlCnt", patentCount);
            
            // 合并软著（普通计算）
            int softwareCount = mergeNormalFields(row, SOFTWARE_FIELDS);
            mergedRow.put("软著", softwareCount > 0 ? softwareCount + "个" : "0个");
            mergedRow.put("rzCnt", softwareCount);
            
            // 合并奖励类（普通计算）
            int awardCount = mergeNormalFields(row, AWARD_FIELDS);
            mergedRow.put("奖励", awardCount > 0 ? awardCount + "个" : "0个");
            mergedRow.put("jlCnt", awardCount);
            
            // 合并学术报告（普通计算）
            int reportCount = mergeNormalFields(row, REPORT_FIELDS);
            mergedRow.put("学术报告(讲座类)", reportCount > 0 ? reportCount + "个" : "0个");
            mergedRow.put("jzbgCnt", reportCount);
            
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
        int totalCount = 0;
        double totalAmount = 0D;

        for (String field : fields) {
            Object value = row.get(field);
            if (value instanceof String) {
                AmountSummary summary = parseAmountSummary((String) value);
                totalCount += summary.count;
                totalAmount += summary.amount;
            }
        }

        return buildSummaryString(totalCount, totalAmount);
    }

    /**
     * 合并纵向科研项目字段（纯个数格式）
     * @param row 数据行
     * @param fields 字段列表
     * @return 合并后的个数字符串，如 "5个"
     */
    private String mergeVerticalFields(Map<String, Object> row, List<String> fields) {
        int totalCount = 0;

        for (String field : fields) {
            Object value = row.get(field);
            if (value instanceof String) {
                totalCount += parseNumberFromField((String) value);
            }
        }

        return totalCount > 0 ? totalCount + "个" : "0个";
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
        
        // 横向和成果转化（特殊计算，按明细格式显示）
        List<String> amountFields = Arrays.asList("横向课题科研项目", "成果转化");
        for (String field : amountFields) {
            Map<Double, Integer> amountCountMap = new HashMap<>();
            int totalCount = 0;
            double totalAmount = 0D;

            for (Map<String, Object> row : mergedData) {
                Object detailValue = row.get(field + "_明细");
                if (detailValue instanceof String) {
                    parseAndCountAmounts((String) detailValue, amountCountMap);
                }
                Object summaryValue = row.get(field + "_汇总");
                Object value = summaryValue instanceof String ? summaryValue : row.get(field);
                if (value instanceof String) {
                    AmountSummary summary = parseAmountSummary((String) value);
                    totalCount += summary.count;
                    totalAmount += summary.amount;
                }
            }

            String detailText = buildResultString(amountCountMap);
            String summaryText = buildSummaryString(totalCount, totalAmount);
            totalRow.put(field, detailText);
            totalRow.put(field + "_汇总", detailText);
            totalRow.put(field + "_明细", detailText);
        }

        // 纵向科研项目（纯个数格式）
        List<String> verticalFields = Arrays.asList("纵向科研项目-校级以上", "纵向科研项目-校级");
        for (String field : verticalFields) {
            int totalCount = 0;

            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    totalCount += parseNumberFromField((String) value);
                }
            }

            totalRow.put(field, totalCount > 0 ? totalCount + "个" : "0个");
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
        
        // 横向和成果转化（特殊计算，按明细格式显示）
        List<String> amountFields = Arrays.asList("横向课题科研项目", "成果转化");
        for (String field : amountFields) {
            Map<Double, Integer> amountCountMap = new HashMap<>();
            int totalCount = 0;
            double totalAmount = 0D;

            for (Map<String, Object> row : mergedData) {
                Object detailValue = row.get(field + "_明细");
                if (detailValue instanceof String) {
                    parseAndCountAmounts((String) detailValue, amountCountMap);
                }
                Object summaryValue = row.get(field + "_汇总");
                Object value = summaryValue instanceof String ? summaryValue : row.get(field);
                if (value instanceof String) {
                    AmountSummary summary = parseAmountSummary((String) value);
                    totalCount += summary.count;
                    totalAmount += summary.amount;
                }
            }

            String detailText = buildResultString(amountCountMap);
            String summaryText = buildSummaryString(totalCount, totalAmount);
            totalRow.put(field, detailText);
            totalRow.put(field + "_汇总", detailText);
            totalRow.put(field + "_明细", detailText);
        }

        // 纵向科研项目（纯个数格式）
        List<String> verticalFields = Arrays.asList("纵向科研项目-校级以上", "纵向科研项目-校级");
        for (String field : verticalFields) {
            int totalCount = 0;

            for (Map<String, Object> row : mergedData) {
                Object value = row.get(field);
                if (value instanceof String) {
                    totalCount += parseNumberFromField((String) value);
                }
            }

            totalRow.put(field, totalCount > 0 ? totalCount + "个" : "0个");
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
     * @param fieldValue 字段值，格式如"3个(10万)，2个（20万）"，支持空格和半角/全角括号混用
     * @param amountCountMap 金额计数映射
     */
    private void parseAndCountAmounts(String fieldValue, Map<Double, Integer> amountCountMap) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return;
        }

        String[] items = fieldValue.split("[,，]");
        for (String item : items) {
            AmountSummary summary = parseAmountSummary(item.trim());
            if (summary.count <= 0 || summary.amount <= 0D) {
                continue;
            }
            amountCountMap.put(summary.amount, amountCountMap.getOrDefault(summary.amount, 0) + summary.count);
        }
    }

    private AmountSummary parseAmountSummary(String fieldValue) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return new AmountSummary(0, 0D);
        }

        Pattern pattern = Pattern.compile("(\\d+)个\\s*[（(](\\d+\\.?\\d*)万[)）]");
        Matcher matcher = pattern.matcher(fieldValue);
        int totalCount = 0;
        double totalAmount = 0D;
        while (matcher.find()) {
            int count = Integer.parseInt(matcher.group(1));
            double amount = Double.parseDouble(matcher.group(2));
            if (count == 0 || amount == 0D) {
                continue;
            }
            totalCount += count;
            totalAmount += amount;
        }
        return new AmountSummary(totalCount, totalAmount);
    }

    private String buildSummaryString(int totalCount, double totalAmount) {
        if (totalCount <= 0 || totalAmount <= 0D) {
            return "0个(0万)";
        }
        return totalCount + "个（" + String.format("%.2f", totalAmount) + "万）";
    }

    private void sortCollegeRows(List<Map<String, Object>> rows) {
        if (rows == null || rows.size() <= 1) {
            return;
        }
        Map<String, Object> totalRow = null;
        List<Map<String, Object>> normalRows = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            if (isTotalRow(row)) {
                totalRow = row;
                continue;
            }
            normalRows.add(row);
        }
        normalRows.sort((left, right) -> {
            boolean leftNonZero = hasAnyNonZeroValue(left);
            boolean rightNonZero = hasAnyNonZeroValue(right);
            if (leftNonZero != rightNonZero) {
                return leftNonZero ? -1 : 1;
            }
            String leftDeptName = String.valueOf(left.get("deptName") == null ? "" : left.get("deptName")).trim();
            String rightDeptName = String.valueOf(right.get("deptName") == null ? "" : right.get("deptName")).trim();
            return leftDeptName.compareTo(rightDeptName);
        });
        rows.clear();
        rows.addAll(normalRows);
        if (totalRow != null) {
            rows.add(totalRow);
        }
    }

    private boolean isTotalRow(Map<String, Object> row) {
        String deptName = String.valueOf(row.get("deptName") == null ? "" : row.get("deptName")).trim();
        return "总计".equals(deptName);
    }

    private boolean hasAnyNonZeroValue(Map<String, Object> row) {
        return hasNonZeroField(row, "横向课题科研项目")
            || hasNonZeroField(row, "纵向科研项目-校级以上")
            || hasNonZeroField(row, "纵向科研项目-校级")
            || hasNonZeroField(row, "成果转化")
            || hasNonZeroField(row, "学术论文")
            || hasNonZeroField(row, "教材著作")
            || hasNonZeroField(row, "专利")
            || hasNonZeroField(row, "软著")
            || hasNonZeroField(row, "奖励")
            || hasNonZeroField(row, "学术报告(讲座类)");
    }

    private boolean hasNonZeroField(Map<String, Object> row, String fieldName) {
        Object value = row.get(fieldName);
        if (!(value instanceof String)) {
            return false;
        }
        return parseNumberFromField((String) value) > 0;
    }

    private static class AmountSummary {
        private final int count;
        private final double amount;

        private AmountSummary(int count, double amount) {
            this.count = count;
            this.amount = amount;
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
     * 从特殊格式字符串（如"2个（3.50万），1个（4.00万）"）中提取总个数
     * 支持空格和半角/全角括号混用
     * @param fieldValue 字段值
     * @return 总个数
     */
    private int extractCountFromSpecial(String fieldValue) {
        if (fieldValue == null || fieldValue.trim().isEmpty() || fieldValue.equals("0个(0万)")) {
            return 0;
        }
        int total = 0;
        Pattern pattern = Pattern.compile("(\\d+)个\\s*[（(](\\d+\\.?\\d*)万[)）]");
        Matcher matcher = pattern.matcher(fieldValue);
        while (matcher.find()) {
            total += Integer.parseInt(matcher.group(1));
        }
        return total;
    }

    /**
     * 从字段值中解析数字
     * 优先匹配"X个"格式开头的数字，避免把金额中的数字也累加进去
     * @param fieldValue 字段值
     * @return 解析出的数字
     */
    private int parseNumberFromField(String fieldValue) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return 0;
        }

        // 先尝试匹配"X个"格式（如"3个"、"2个（5万）"），只取开头的数量数字
        Pattern leadingPattern = Pattern.compile("^(\\d+)个");
        Matcher leadingMatcher = leadingPattern.matcher(fieldValue.trim());
        if (leadingMatcher.find()) {
            return Integer.parseInt(leadingMatcher.group(1));
        }

        // 兜底：匹配所有数字并累加
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(fieldValue);

        int totalCount = 0;
        while (matcher.find()) {
            totalCount += Integer.parseInt(matcher.group());
        }

        return totalCount;
    }

    /**
     * 构建明细字符串，将各子字段的非零值用逗号拼接
     * 如："2个（3.50万）, 1个（4.00万）, 1个（8.00万）"
     * @param row 数据行
     * @param fields 字段列表
     * @return 明细字符串
     */
    private String buildDetailString(Map<String, Object> row, List<String> fields) {
        List<String> parts = new ArrayList<>();
        for (String field : fields) {
            Object value = row.get(field);
            if (value instanceof String) {
                String s = (String) value;
                // 排除空值、0个、0万的情况
                if (s != null && !s.trim().isEmpty()
                        && !s.equals("0个") && !s.equals("0个（0万）")
                        && !s.equals("0个 (0万)") && !s.equals("0个(0万)")
                        && !s.startsWith("0个")) {
                    parts.add(s);
                }
            }
        }
        if (parts.isEmpty()) {
            return "0个（0万）";
        }
        return String.join("，", parts);
    }
}
