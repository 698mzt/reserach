package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.mapper.StatisticMapper;
import com.ruoyi.system.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IStatisticServiceImpl implements IStatisticService {

    @Autowired
    private StatisticMapper statisticMapper;

    // 横向课题字段
    private static final List<String> HORIZONTAL_FIELDS = Arrays.asList(
        "ewyyx", "edwwy", "wdswy", "sdeswy", "esdsswwy", "wswwydwswy", "wsdqswwy", "qswdybwy"
    );

    // 纵向课题字段
    private static final List<String> VERTICAL_FIELDS = Arrays.asList(
        "zcgjjjkyxm", "zcsbjjjkyxm", "zcsbjjgxm", "zcsbjzxkyxm", 
        "zctjjxhjkyxm", "zcxjjxglxmywyys", "zcxjjxglxmywyyx"
    );

    // 成果转化字段
    private static final List<String> ACHIEVEMENT_FIELDS = Arrays.asList(
        "eyxxx", "edwxx", "wdsxx", "sdesxx", "esdsswxx", "sswdwsxx", "dywsxx"
    );

    @Override
    public List<ResearchWorkload> selectAllTeacher(List<String> dictValues,String pname, String dname) {
        // 这里保持原有的按部门ID查询的逻辑
        return statisticMapper.selectAllTeacher(dictValues,pname, dname);
    }

    @Override
    public List<ResearchWorkloadByJYS> selectAllDept(List<String> dictValues, String pname, String dname) {
        // 这里保持原有的按部门ID查询的逻辑
        return statisticMapper.selectAllDept(dictValues,pname, dname);
    }
    
    /**
     * 计算总计行
     * @param list 数据列表
     * @return 总计行
     */
    private Map<String, Object> calculateTotalRow(List<Map<String, Object>> list) {
        // 创建总计行映射
        Map<String, Object> totalRow = new HashMap<>();
        totalRow.put("userName", "总计");

        // 处理三大类字段（横向课题、纵向课题、成果转化）
        processThreeCategories(totalRow, list);

        // 处理其他字段
        processOtherFields(totalRow, list);

        return totalRow;
    }

    /**
     * 处理三大类字段（横向课题、纵向课题、成果转化）
     * @param totalRow 总计行
     * @param list 数据列表
     */
    private void processThreeCategories(Map<String, Object> totalRow, List<Map<String, Object>> list) {
        // 合并三大类字段数据
        mergeThreeCategoriesData(totalRow, list, HORIZONTAL_FIELDS);
        mergeThreeCategoriesData(totalRow, list, VERTICAL_FIELDS);
        mergeThreeCategoriesData(totalRow, list, ACHIEVEMENT_FIELDS);
    }

    /**
     * 合并三大类字段数据
     * @param totalRow 总计行
     * @param list 数据列表
     * @param fields 字段列表
     */
    private void mergeThreeCategoriesData(Map<String, Object> totalRow, List<Map<String, Object>> list, List<String> fields) {
        for (String field : fields) {
            // 使用Map来存储金额以及个数
            Map<Double, Integer> amountCountMap = new HashMap<>();

            // 遍历所有行，累加相同金额的数量
            for (Map<String, Object> row : list) {
                Object value = row.get(field);
                if (value instanceof String) {
                    parseAndCountAmounts((String) value, amountCountMap);
                }
            }

            // 构建结果字符串
            String result = buildTotalResultString(amountCountMap);
            totalRow.put(field, result);
        }
    }

    /**
     * 解析字段值并累加相同金额的数量
     * @param fieldValue 字段值，格式如"3个(10万)，2个(20万)"
     * @param amountCountMap 金额计数映射
     */
    public void parseAndCountAmounts(String fieldValue, Map<Double, Integer> amountCountMap) {
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

                // 累加相同金额的数量
                amountCountMap.put(amount, amountCountMap.getOrDefault(amount, 0) + count);
            }
        }
    }

    /**
     * 构建总计结果字符串
     * @param amountCountMap 金额计数映射
     * @return 结果字符串，格式如"2个（10万），1个（20万）"
     */
    public String buildTotalResultString(Map<Double, Integer> amountCountMap) {
        if (amountCountMap.isEmpty()) {
            return "0个（0万）";
        }

        // 检查是否所有数据都是0个（0万）
        if (amountCountMap.size() == 1 && amountCountMap.containsKey(0.0) && amountCountMap.get(0.0) > 0) {
            return "0个（0万）";
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

            // 当金额为0时，显示"0万"而不是"0.00万"
            String amountStr;
            if (amount == 0.0) {
                amountStr = "0";
            } else {
                // 对于非零金额，如果是整数则不显示小数点，否则保留两位小数
                if (amount == Math.floor(amount)) {
                    amountStr = String.format("%.0f", amount);
                } else {
                    amountStr = String.format("%.2f", amount);
                }
            }

            result.append(count).append("个（").append(amountStr).append("万）");
        }

        return result.toString();
    }

    /**
     * 处理其他字段
     * @param totalRow 总计行
     * @param list 数据列表
     */
    private void processOtherFields(Map<String, Object> totalRow, List<Map<String, Object>> list) {
        if (list.isEmpty()) {
            return;
        }

        // 获取第一行的所有字段
        Map<String, Object> firstRow = list.get(0);
        Set<String> allFields = new HashSet<>(firstRow.keySet());

        // 移除三大类字段和非统计字段
        allFields.removeAll(HORIZONTAL_FIELDS);
        allFields.removeAll(VERTICAL_FIELDS);
        allFields.removeAll(ACHIEVEMENT_FIELDS);
        allFields.remove("userId");
        allFields.remove("userName");
        allFields.remove("partenId");
        allFields.remove("partenName");
        allFields.remove("deptId");
        allFields.remove("deptName");

        // 处理每个其他字段
        for (String field : allFields) {
            int totalCount = 0;

            // 遍历所有行，累计数据
            for (Map<String, Object> row : list) {
                Object value = row.get(field);
                if (value instanceof String) {
                    totalCount += parseNumberFromField((String) value);
                }
            }

            // 设置总计值
            totalRow.put(field, totalCount + "个");
        }
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