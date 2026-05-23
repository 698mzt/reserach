package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.domain.ResearchWorkload;
import com.ruoyi.system.domain.ResearchWorkloadByJYS;
import com.ruoyi.system.mapper.AlltotleMapper;
import com.ruoyi.system.mapper.StatisticMapper;
import com.ruoyi.system.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 统计服务实现：整合 Alltotle_score（汇总）和 Alltotle（明细）两张表
 * 支持两种显示模式：
 *   Mode A 按金额分组 → 从 Alltotle 构建 detailJson
 *   Mode B 汇总总计 → 从 Alltotle 计算的 Cnt/Sum
 */
@Service
public class IStatisticServiceImpl implements IStatisticService {

    @Autowired
    private StatisticMapper statisticMapper;

    @Autowired
    private AlltotleMapper alltotleMapper;

    /** 解析 "X个（Y万）" 格式的正则 */
    private static final Pattern GEWAN_PATTERN = Pattern.compile("(\\d+)\\s*个\\s*[（(]\\s*([\\d.]+)\\s*万\\s*[)）]");

    /** 10大类 → 类别key映射 */
    private static final Map<String, Map<String, String>> CATEGORY_FIELDS = new LinkedHashMap<>();
    static {
        Map<String, String> hxkt = new LinkedHashMap<>();
        hxkt.put("ewyyx", "2万元以下");
        hxkt.put("edwwy", "2-5万元（含2）");
        hxkt.put("wdswy", "5-10万元（含5）");
        hxkt.put("sdeswy", "10-20万元（含10）");
        hxkt.put("esdsswwy", "20-35万元（含20）");
        hxkt.put("wswwydwswy", "35-50万元（含35）");
        hxkt.put("wsdqswwy", "50-75万元（含50）");
        hxkt.put("qswdybwy", "75-100万元（含75）");
        CATEGORY_FIELDS.put("hxkt", hxkt);

        Map<String, String> zxktxjys = new LinkedHashMap<>();
        zxktxjys.put("zcgjjjkyxm", "主持国家基金科研项目");
        zxktxjys.put("zcsbjjjkyxm", "主持省部级基金科研项目");
        zxktxjys.put("zcsbjjgxm", "主持省部级教改项目");
        zxktxjys.put("zcsbjzxkyxm", "主持省部级纵向科研项目");
        zxktxjys.put("zctjjxhjkyxm", "主持厅局级、学会级科研项目");
        CATEGORY_FIELDS.put("zxktxjys", zxktxjys);

        Map<String, String> zxktxj = new LinkedHashMap<>();
        zxktxj.put("zcxjjxglxmywyys", "主持校级教学管理项目（1万元以上）");
        zxktxj.put("zcxjjxglxmywyyx", "主持校级教学管理项目（1万元以下）");
        CATEGORY_FIELDS.put("zxktxj", zxktxj);

        Map<String, String> cgzh = new LinkedHashMap<>();
        cgzh.put("eyxxx", "2万元以下");
        cgzh.put("edwxx", "2-5万元");
        cgzh.put("wdsxx", "5-10万元");
        cgzh.put("sdesxx", "10-20万元");
        cgzh.put("esdsswxx", "20-35万元");
        cgzh.put("sswdwsxx", "35-50万元");
        cgzh.put("dywsxx", "大于50万元");
        CATEGORY_FIELDS.put("cgzh", cgzh);

        Map<String, String> xslw = new LinkedHashMap<>();
        xslw.put("SCI", "SCI");
        xslw.put("EI", "EI");
        xslw.put("hx", "核心");
        xslw.put("sw", "三网");
        xslw.put("pt", "普通");
        xslw.put("xb", "校办");
        CATEGORY_FIELDS.put("xslw", xslw);

        Map<String, String> jczz = new LinkedHashMap<>();
        jczz.put("cbzz1", "出版专著（一类出版社）");
        jczz.put("cbzz2", "出版专著（二类出版社）");
        jczz.put("cbyz1", "出版译著（一类出版社）");
        jczz.put("cbyz2", "出版译著（二类出版社）");
        jczz.put("cbjc1", "出版教材（国家规划，省级规划教材）");
        jczz.put("cbjc2", "出版教材");
        jczz.put("zbjc", "自编教材（校内使用）");
        CATEGORY_FIELDS.put("jczz", jczz);

        Map<String, String> zl = new LinkedHashMap<>();
        zl.put("sqfmzl", "授权发明专利");
        zl.put("syxxzl", "实用新型专利");
        zl.put("wxsjzl", "外型设计专利");
        CATEGORY_FIELDS.put("zl", zl);

        Map<String, String> rz = new LinkedHashMap<>();
        rz.put("jsjrjzzq", "计算机软件著作权");
        CATEGORY_FIELDS.put("rz", rz);

        Map<String, String> jl = new LinkedHashMap<>();
        jl.put("sjjxcgj", "省级教学成果奖");
        jl.put("sjkxjsj", "省级科学技术奖");
        jl.put("stjjzrkxlpj", "市、厅局级自然科学类评奖");
        jl.put("stjjsklpj", "市、厅局级社科类评奖");
        jl.put("xjjxcgj", "校级教学成果奖");
        jl.put("yyxkyxjcgj", "应用型科研校级成果奖");
        jl.put("xhjjxcgj", "学会级教学成果奖");
        jl.put("xhjkycgj", "学会级科研成果奖");
        CATEGORY_FIELDS.put("jl", jl);

        Map<String, String> jzbg = new LinkedHashMap<>();
        jzbg.put("jbgj", "举办国际");
        jzbg.put("jbgn", "举办国内");
        jzbg.put("cjgj", "参加国际");
        jzbg.put("cjgn", "参加国内");
        jzbg.put("xjxs", "校级学术");
        CATEGORY_FIELDS.put("jzbg", jzbg);
    }

    @Override
    public List<ResearchWorkload> selectAllTeacher(List<String> dictValues, String pname, String dname, String userName,
                                                   Long scopeUserId, Long scopeDeptId, Long collegeParentId) {
        List<ResearchWorkload> list = statisticMapper.selectAllTeacher(dictValues, pname, dname, userName,
                scopeUserId, scopeDeptId, collegeParentId);
        if (list == null || list.isEmpty()) {
            return list;
        }

        // 收集所有 userId
        List<Long> userIds = new ArrayList<>();
        for (ResearchWorkload wl : list) {
            if (wl.getUserId() != null) {
                try {
                    userIds.add(Long.valueOf(wl.getUserId()));
                } catch (NumberFormatException ignored) {}
            }
        }

        // 批量查询 Alltotle 明细
        Map<Long, Alltotle> alltotleMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<Alltotle> alltotleList = alltotleMapper.selectAlltotleByUserIds(userIds);
            if (alltotleList != null) {
                for (Alltotle a : alltotleList) {
                    alltotleMap.put(a.getUserId(), a);
                }
            }
        }

        // 为每个教师填充得分（基于alltotle_score）和 detailJson（基于Alltotle）
        for (ResearchWorkload wl : list) {
            // 1. 填充积分值（来自 Alltotle_score 的原始值）
            fillScoreFromAlltotleScore(wl);
            wl.set__score(computeTotalScore(wl));

            // 2. 用 Alltotle 填充 detailJson（金额分组明细）
            Alltotle detail = null;
            if (wl.getUserId() != null) {
                try {
                    detail = alltotleMap.get(Long.valueOf(wl.getUserId()));
                } catch (NumberFormatException ignored) {}
            }
            if (detail != null) {
                wl.setDetailJson(buildDetailJson(detail));
                // 3. 从 Alltotle 明细补充小类原始字段，供前端直接按字段展示
                fillDetailFields(wl, detail);
                // 4. 从 Alltotle 明细补充 Cnt/Sum（个数/金额）
                fillCntSum(wl, detail);
            } else {
                wl.setDetailJson("{}");
            }
        }

        return list;
    }

    @Override
    public List<ResearchWorkloadByJYS> selectAllDept(List<String> dictValues, String pname, String dname, String userName, Long collegeParentId) {
        List<ResearchWorkloadByJYS> list = statisticMapper.selectAllDept(dictValues, pname, dname, userName, collegeParentId);
        if (list == null || list.isEmpty()) {
            return list;
        }

        // 收集所有 deptId
        List<Long> deptIds = new ArrayList<>();
        for (ResearchWorkloadByJYS wl : list) {
            if (wl.getDeptId() != null) {
                try {
                    deptIds.add(Long.valueOf(wl.getDeptId()));
                } catch (NumberFormatException ignored) {}
            }
        }

        // 批量查询 Alltotle 并按 deptId 分组
        Map<Long, List<Alltotle>> deptAlltotle = new HashMap<>();
        if (!deptIds.isEmpty()) {
            List<Alltotle> alltotleList = alltotleMapper.selectAlltotleByDeptIds(deptIds);
            if (alltotleList != null) {
                for (Alltotle a : alltotleList) {
                    deptAlltotle.computeIfAbsent(a.getDeptId(), k -> new ArrayList<>()).add(a);
                }
            }
        }

        // 为每个教研室填充得分（基于alltotle_score聚合值）和 detailJson（基于Alltotle）
        for (ResearchWorkloadByJYS wl : list) {
            // 1. 填充积分值（来自 Alltotle_score 的原始聚合值）
            fillScoreFromAlltotleScoreForJYS(wl);
            wl.set__score(computeTotalScoreByJYS(wl));

            // 2. 用 Alltotle 填充 detailJson（金额分组明细）
            Long deptId = null;
            if (wl.getDeptId() != null) {
                try { deptId = Long.valueOf(wl.getDeptId()); } catch (NumberFormatException ignored) {}
            }
            List<Alltotle> details = deptAlltotle.get(deptId);
            if (details != null && !details.isEmpty()) {
                wl.setDetailJson(buildAggregatedDetailJson(details));
                fillDetailFieldsForJYS(wl, details);
                // 3. 从 Alltotle 明细补充 Cnt/Sum（个数/金额）
                fillCntSumByJYS(wl, details);
            } else {
                wl.setDetailJson("{}");
            }
        }

        return list;
    }

    /**
     * 解析 "X个（Y万）" 格式，支持逗号分隔的多条记录，返回 [累计个数, 累计总金额(分)]
     * 数据库中的Y万已经是该组项目的合计总金额（非单价），直接累加即可
     */
    private int[] parseCntSum(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new int[]{0, 0};
        }
        Matcher m = GEWAN_PATTERN.matcher(value);
        int totalCnt = 0;
        int totalSum = 0;
        while (m.find()) {
            int cnt = Integer.parseInt(m.group(1));
            BigDecimal amountWan = new BigDecimal(m.group(2));
            totalCnt += cnt;
            // Y万已是该组总金额，直接累加（转分为单位）
            totalSum += amountWan.multiply(new BigDecimal("10000")).intValue();
        }
        if (totalCnt > 0) {
            return new int[]{totalCnt, totalSum};
        }
        return new int[]{0, 0};
    }

    /**
     * 将字符串解析为整数，支持 "X个" 格式和小数字符串（如"26.0"），非数字或null时返回0
     */
    private int parseIntOrZero(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return 0;
        }
        String cleaned = raw.trim().replace("个", "").trim();
        if (cleaned.isEmpty()) {
            return 0;
        }
        try {
            // 先尝试直接解析整数
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            // 如果失败，尝试解析为Double再取整（处理"26.0"这种情况）
            try {
                return (int) Double.parseDouble(cleaned);
            } catch (NumberFormatException e2) {
                return 0;
            }
        }
    }

    /** 将字符串解析为 BigDecimal，非数字或null时返回 ZERO */
    private java.math.BigDecimal parseBigDecimalOrZero(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return java.math.BigDecimal.ZERO;
        }
        try {
            return new java.math.BigDecimal(raw.trim());
        } catch (NumberFormatException e) {
            return java.math.BigDecimal.ZERO;
        }
    }

    /**
     * 通过反射获取 Alltotle 对象的字段值
     */
    private String getFieldValue(Alltotle detail, String fieldName) {
        try {
            String getter = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            Method method = Alltotle.class.getMethod(getter);
            Object val = method.invoke(detail);
            return val != null ? val.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 为单个教师构建明细JSON（从Alltotle表）
     */
    private String buildDetailJson(Alltotle detail) {
        StringBuilder sb = new StringBuilder("{");
        boolean firstCat = true;
        for (Map.Entry<String, Map<String, String>> catEntry : CATEGORY_FIELDS.entrySet()) {
            String categoryKey = catEntry.getKey();
            Map<String, String> fieldMap = catEntry.getValue();
            StringBuilder items = new StringBuilder();
            for (Map.Entry<String, String> fieldEntry : fieldMap.entrySet()) {
                String fieldName = fieldEntry.getKey();
                String label = fieldEntry.getValue();
                String rawValue = getFieldValue(detail, fieldName);
                int[] cs = parseCntSum(rawValue);
                if (cs[0] == 0 && cs[1] == 0) {
                    continue;
                }
                if (items.length() > 0) items.append(",");
                BigDecimal sumWan = new BigDecimal(cs[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
                items.append("{\"label\":\"").append(escapeJson(label)).append("\",")
                     .append("\"cnt\":").append(cs[0]).append(",")
                     .append("\"sum\":").append(sumWan.stripTrailingZeros().toPlainString()).append("}");
            }
            if (!firstCat) sb.append(",");
            firstCat = false;
            if (items.length() == 0) {
                sb.append("\"").append(categoryKey).append("\":[]");
            } else {
                sb.append("\"").append(categoryKey).append("\":[").append(items).append("]");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 为教研室聚合多个教师的明细JSON
     */
    private String buildAggregatedDetailJson(List<Alltotle> details) {
        // 对每个类别，聚合所有教师的同金额段数据
        StringBuilder sb = new StringBuilder("{");
        boolean firstCat = true;
        for (Map.Entry<String, Map<String, String>> catEntry : CATEGORY_FIELDS.entrySet()) {
            String categoryKey = catEntry.getKey();
            Map<String, String> fieldMap = catEntry.getValue();
            // fieldName → [totalCnt, totalSum]
            Map<String, int[]> aggregated = new LinkedHashMap<>();
            for (Alltotle detail : details) {
                for (String fieldName : fieldMap.keySet()) {
                    String rawValue = getFieldValue(detail, fieldName);
                    int[] cs = parseCntSum(rawValue);
                    if (cs[0] == 0 && cs[1] == 0) continue;
                    int[] existing = aggregated.get(fieldName);
                    if (existing == null) {
                        aggregated.put(fieldName, new int[]{cs[0], cs[1]});
                    } else {
                        existing[0] += cs[0];
                        existing[1] += cs[1];
                    }
                }
            }
            StringBuilder items = new StringBuilder();
            for (Map.Entry<String, String> fieldEntry : fieldMap.entrySet()) {
                String fieldName = fieldEntry.getKey();
                String label = fieldEntry.getValue();
                int[] cs = aggregated.get(fieldName);
                if (cs == null || (cs[0] == 0 && cs[1] == 0)) continue;
                if (items.length() > 0) items.append(",");
                BigDecimal sumWan = new BigDecimal(cs[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
                items.append("{\"label\":\"").append(escapeJson(label)).append("\",")
                     .append("\"cnt\":").append(cs[0]).append(",")
                     .append("\"sum\":").append(sumWan.stripTrailingZeros().toPlainString()).append("}");
            }
            if (!firstCat) sb.append(",");
            firstCat = false;
            if (items.length() == 0) {
                sb.append("\"").append(categoryKey).append("\":[]");
            } else {
                sb.append("\"").append(categoryKey).append("\":[").append(items).append("]");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 转义JSON字符串中的特殊字符
     */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * 从Alltotle明细计算单个教师的 Cnt 和 Sum，填充到 ResearchWorkload
     * Alltotle_score 所有字段存的都是积分值（非个数），
     * 而 Alltotle 存的是正确的个数，所以所有类别统一以 Alltotle 为准覆盖。
     */
    private void fillCntSum(ResearchWorkload wl, Alltotle detail) {
        int[] hxkt = sumCategory(detail, CATEGORY_FIELDS.get("hxkt"));
        wl.setHxktCnt(hxkt[0]);
        wl.setHxktSum(new BigDecimal(hxkt[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));

        int[] zxktxj = sumCategoryZxktxj(detail, CATEGORY_FIELDS.get("zxktxj"));
        wl.setZxktxjCnt(zxktxj[0]);
        wl.setZxktxjSum(BigDecimal.ZERO);

        int[] zxktxjys = sumCategoryZxktxj(detail, CATEGORY_FIELDS.get("zxktxjys"));
        wl.setZxktxjysCnt(zxktxjys[0]);
        wl.setZxktxjysSum(BigDecimal.ZERO);

        int[] cgzh = sumCategory(detail, CATEGORY_FIELDS.get("cgzh"));
        wl.setCgzhCnt(cgzh[0]);
        wl.setCgzhSum(new BigDecimal(cgzh[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));

        int[] xslw = sumCategoryXslw(detail);
        wl.setXslwCnt(xslw[0]);
        wl.setXslwSum(BigDecimal.ZERO);

        int[] jczz = sumCategoryJczz(detail);
        wl.setJczzCnt(jczz[0]);
        wl.setJczzSum(BigDecimal.ZERO);

        int[] zl = sumCategoryZl(detail);
        wl.setZlCnt(zl[0]);
        wl.setZlSum(BigDecimal.ZERO);

        int[] rz = sumCategoryRz(detail);
        wl.setRzCnt(rz[0]);
        wl.setRzSum(BigDecimal.ZERO);

        int[] jl = sumCategoryJl(detail);
        wl.setJlCnt(jl[0]);
        wl.setJlSum(BigDecimal.ZERO);

        int[] jzbg = sumCategoryJzbg(detail);
        wl.setJzbgCnt(jzbg[0]);
        wl.setJzbgSum(BigDecimal.ZERO);
    }

    /**
     * 将 Alltotle 的小类字段值复制到 ResearchWorkload 的对应字段
     * 使用 BeanInfo 遍历属性，处理大写字段名（如 SCI/EI）与 Lombok 小写字段的匹配问题
     */
    private void fillDetailFields(ResearchWorkload wl, Alltotle detail) {
        List<String> skipFields = Arrays.asList("userId","userName","partenId","partenName","deptId","deptName","class");
        try {
            BeanInfo detailBeanInfo = Introspector.getBeanInfo(Alltotle.class);
            BeanInfo workloadBeanInfo = Introspector.getBeanInfo(ResearchWorkload.class);
            Map<String, PropertyDescriptor> workloadPdMap = new HashMap<>();
            for (PropertyDescriptor pd : workloadBeanInfo.getPropertyDescriptors()) {
                workloadPdMap.put(pd.getName(), pd);
            }
            for (PropertyDescriptor pd : detailBeanInfo.getPropertyDescriptors()) {
                String name = pd.getName();
                if (skipFields.contains(name)) continue;
                Method readMethod = pd.getReadMethod();
                if (readMethod == null) continue;
                // 先精确匹配，再尝试小写匹配（处理 Alltotle 的 SCI/EI 与 ResearchWorkload 的 sci/ei 不一致问题）
                PropertyDescriptor targetPd = workloadPdMap.get(name);
                if (targetPd == null) {
                    targetPd = workloadPdMap.get(name.toLowerCase());
                }
                if (targetPd == null || targetPd.getWriteMethod() == null) continue;
                Object val = readMethod.invoke(detail);
                if (val != null) {
                    targetPd.getWriteMethod().invoke(wl, val.toString());
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 将 Alltotle 的小类字段值复制到 ResearchWorkloadByJYS 的对应字段
     * 同一教研室下按字段聚合：金额类字段保留原始片段并用中文逗号拼接，计数字段直接累加为“X个”
     */
    private void fillDetailFieldsForJYS(ResearchWorkloadByJYS wl, List<Alltotle> details) {
        wl.setZcgjjjkyxm(sumCountField(details, "zcgjjjkyxm"));
        wl.setZcsbjjjkyxm(sumCountField(details, "zcsbjjjkyxm"));
        wl.setZcsbjjgxm(sumCountField(details, "zcsbjjgxm"));
        wl.setZcsbjzxkyxm(sumCountField(details, "zcsbjzxkyxm"));
        wl.setZctjjxhjkyxm(sumCountField(details, "zctjjxhjkyxm"));
        wl.setZcxjjxglxmywyys(sumCountField(details, "zcxjjxglxmywyys"));
        wl.setZcxjjxglxmywyyx(sumCountField(details, "zcxjjxglxmywyyx"));

        wl.setEwyyx(joinAmountField(details, "ewyyx"));
        wl.setEdwwy(joinAmountField(details, "edwwy"));
        wl.setWdswy(joinAmountField(details, "wdswy"));
        wl.setSdeswy(joinAmountField(details, "sdeswy"));
        wl.setEsdsswwy(joinAmountField(details, "esdsswwy"));
        wl.setWswwydwswy(joinAmountField(details, "wswwydwswy"));
        wl.setWsdqswwy(joinAmountField(details, "wsdqswwy"));
        wl.setQswdybwy(joinAmountField(details, "qswdybwy"));

        wl.setEyxxx(joinAmountField(details, "eyxxx"));
        wl.setEdwxx(joinAmountField(details, "edwxx"));
        wl.setWdsxx(joinAmountField(details, "wdsxx"));
        wl.setSdesxx(joinAmountField(details, "sdesxx"));
        wl.setEsdsswxx(joinAmountField(details, "esdsswxx"));
        wl.setSswdwsxx(joinAmountField(details, "sswdwsxx"));
        wl.setDywsxx(joinAmountField(details, "dywsxx"));

        wl.setSci(sumCountField(details, "SCI"));
        wl.setEi(sumCountField(details, "EI"));
        wl.setHx(sumCountField(details, "hx"));
        wl.setSw(sumCountField(details, "sw"));
        wl.setPt(sumCountField(details, "pt"));
        wl.setXb(sumCountField(details, "xb"));

        wl.setCbzz1(sumCountField(details, "cbzz1"));
        wl.setCbzz2(sumCountField(details, "cbzz2"));
        wl.setCbyz1(sumCountField(details, "cbyz1"));
        wl.setCbyz2(sumCountField(details, "cbyz2"));
        wl.setCbjc1(sumCountField(details, "cbjc1"));
        wl.setCbjc2(sumCountField(details, "cbjc2"));
        wl.setZbjc(sumCountField(details, "zbjc"));

        wl.setSqfmzl(sumCountField(details, "sqfmzl"));
        wl.setSyxxzl(sumCountField(details, "syxxzl"));
        wl.setWxsjzl(sumCountField(details, "wxsjzl"));
        wl.setJsjrjzzq(sumCountField(details, "jsjrjzzq"));

        wl.setSjjxcgj(sumCountField(details, "sjjxcgj"));
        wl.setSjkxjsj(sumCountField(details, "sjkxjsj"));
        wl.setStjjzrkxlpj(sumCountField(details, "stjjzrkxlpj"));
        wl.setStjjsklpj(sumCountField(details, "stjjsklpj"));
        wl.setXjjxcgj(sumCountField(details, "xjjxcgj"));
        wl.setYyxkyxjcgj(sumCountField(details, "yyxkyxjcgj"));
        wl.setXhjjxcgj(sumCountField(details, "xhjjxcgj"));
        wl.setXhjkycgj(sumCountField(details, "xhjkycgj"));

        wl.setJbgj(sumCountField(details, "jbgj"));
        wl.setJbgn(sumCountField(details, "jbgn"));
        wl.setCjgj(sumCountField(details, "cjgj"));
        wl.setCjgn(sumCountField(details, "cjgn"));
        wl.setXjxs(sumCountField(details, "xjxs"));
    }

    private String joinAmountField(List<Alltotle> details, String fieldName) {
        List<String> parts = new ArrayList<>();
        for (Alltotle detail : details) {
            String raw = getFieldValue(detail, fieldName);
            if (raw == null || raw.trim().isEmpty()) {
                continue;
            }
            int[] cs = parseCntSum(raw);
            if (cs[0] == 0 && cs[1] == 0) {
                continue;
            }
            parts.add(raw.trim());
        }
        return parts.isEmpty() ? "0个(0万)" : String.join("，", parts);
    }

    private String sumCountField(List<Alltotle> details, String fieldName) {
        int total = 0;
        for (Alltotle detail : details) {
            total += parseIntOrZero(getFieldValue(detail, fieldName));
        }
        return total > 0 ? total + "个" : "0个";
    }

    /**
     * 基于 Alltotle_score 原始值填充积分字段
     */
    private void fillScoreFromAlltotleScore(ResearchWorkload wl) {
        wl.setHxktScore(wl.getHxkt());
        wl.setZxktxjScore(wl.getZxktxj());
        wl.setZxktxjysScore(wl.getZxktxjys());
        wl.setCgzhScore(wl.getCgzh());
        wl.setXslwScore(wl.getXslw());
        wl.setJczzScore(wl.getJczz());
        wl.setZlScore(wl.getZl());
        wl.setRzScore(wl.getRz());
        wl.setJlScore(wl.getJl());
        wl.setJzbgScore(wl.getJzbg());
    }

    /**
     * 基于 Alltotle_score 聚合值填充积分字段
     */
    private void fillScoreFromAlltotleScoreForJYS(ResearchWorkloadByJYS wl) {
        wl.setHxktScore(wl.getHxkt());
        wl.setZxktxjScore(wl.getZxktxj());
        wl.setZxktxjysScore(wl.getZxktxjys());
        wl.setCgzhScore(wl.getCgzh());
        wl.setXslwScore(wl.getXslw());
        wl.setJczzScore(wl.getJczz());
        wl.setZlScore(wl.getZl());
        wl.setRzScore(wl.getRz());
        wl.setJlScore(wl.getJl());
        wl.setJzbgScore(wl.getJzbg());
    }

    /** 从 Score 字段计算总分（科研工作量） */
    private java.math.BigDecimal computeTotalScore(ResearchWorkload wl) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        total = total.add(parseBigDecimalOrZero(wl.getHxktScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZxktxjScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZxktxjysScore()));
        total = total.add(parseBigDecimalOrZero(wl.getCgzhScore()));
        total = total.add(parseBigDecimalOrZero(wl.getXslwScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJczzScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZlScore()));
        total = total.add(parseBigDecimalOrZero(wl.getRzScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJlScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJzbgScore()));
        return total;
    }

    /**
     * 从Alltotle明细列表聚合教研室的 Cnt 和 Sum
     * Alltotle_score 所有字段存的都是积分值（非个数），
     * 而 Alltotle 存的是正确的个数，所以所有类别统一以 Alltotle 为准覆盖。
     */
    private void fillCntSumByJYS(ResearchWorkloadByJYS wl, List<Alltotle> details) {
        int[] hxkt = sumCategoryList(details, CATEGORY_FIELDS.get("hxkt"));
        wl.setHxktCnt(hxkt[0]);
        wl.setHxktSum(new BigDecimal(hxkt[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));

        int[] zxktxj = sumCategoryZxktxjList(details, CATEGORY_FIELDS.get("zxktxj"));
        wl.setZxktxjCnt(zxktxj[0]);
        wl.setZxktxjSum(BigDecimal.ZERO);

        int[] zxktxjys = sumCategoryZxktxjList(details, CATEGORY_FIELDS.get("zxktxjys"));
        wl.setZxktxjysCnt(zxktxjys[0]);
        wl.setZxktxjysSum(BigDecimal.ZERO);

        int[] cgzh = sumCategoryList(details, CATEGORY_FIELDS.get("cgzh"));
        wl.setCgzhCnt(cgzh[0]);
        wl.setCgzhSum(new BigDecimal(cgzh[1]).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));

        int[] xslw = sumCategoryXslwList(details);
        wl.setXslwCnt(xslw[0]);
        wl.setXslwSum(BigDecimal.ZERO);

        int[] jczz = sumCategoryJczzList(details);
        wl.setJczzCnt(jczz[0]);
        wl.setJczzSum(BigDecimal.ZERO);

        int[] zl = sumCategoryZlList(details);
        wl.setZlCnt(zl[0]);
        wl.setZlSum(BigDecimal.ZERO);

        int[] rz = sumCategoryRzList(details);
        wl.setRzCnt(rz[0]);
        wl.setRzSum(BigDecimal.ZERO);

        int[] jl = sumCategoryJlList(details);
        wl.setJlCnt(jl[0]);
        wl.setJlSum(BigDecimal.ZERO);

        int[] jzbg = sumCategoryJzbgList(details);
        wl.setJzbgCnt(jzbg[0]);
        wl.setJzbgSum(BigDecimal.ZERO);
    }

    /** 汇总10大类别Sum得到总工作量（万元），用于前端__score（教研室） */
    private java.math.BigDecimal computeTotalScoreByJYS(ResearchWorkloadByJYS wl) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        total = total.add(parseBigDecimalOrZero(wl.getHxktScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZxktxjScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZxktxjysScore()));
        total = total.add(parseBigDecimalOrZero(wl.getCgzhScore()));
        total = total.add(parseBigDecimalOrZero(wl.getXslwScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJczzScore()));
        total = total.add(parseBigDecimalOrZero(wl.getZlScore()));
        total = total.add(parseBigDecimalOrZero(wl.getRzScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJlScore()));
        total = total.add(parseBigDecimalOrZero(wl.getJzbgScore()));
        return total;
    }

    /** 对单个 Alltotle，汇总一个类别（带金额）的 Cnt 和 Sum（Sum 单位为分） */
    private int[] sumCategory(Alltotle detail, Map<String, String> fieldMap) {
        int totalCnt = 0, totalSum = 0;
        for (String fieldName : fieldMap.keySet()) {
            String raw = getFieldValue(detail, fieldName);
            int[] cs = parseCntSum(raw);
            totalCnt += cs[0];
            totalSum += cs[1];
        }
        return new int[]{totalCnt, totalSum};
    }

    /** 对 Alltotle 列表，聚合一个类别的 Cnt 和 Sum */
    private int[] sumCategoryList(List<Alltotle> details, Map<String, String> fieldMap) {
        int totalCnt = 0, totalSum = 0;
        for (Alltotle detail : details) {
            int[] cs = sumCategory(detail, fieldMap);
            totalCnt += cs[0];
            totalSum += cs[1];
        }
        return new int[]{totalCnt, totalSum};
    }

    /** 纵向课题：仅统计个数（字段为 "X个" 格式），无金额 */
    private int[] sumCategoryZxktxj(Alltotle detail, Map<String, String> fieldMap) {
        int total = 0;
        for (String fieldName : fieldMap.keySet()) {
            String raw = getFieldValue(detail, fieldName);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryZxktxjList(List<Alltotle> details, Map<String, String> fieldMap) {
        int total = 0;
        for (Alltotle detail : details) {
            total += sumCategoryZxktxj(detail, fieldMap)[0];
        }
        return new int[]{total, 0};
    }

    /** 学术论文：仅统计个数，无金额 */
    private int[] sumCategoryXslw(Alltotle detail) {
        int total = 0;
        String[] fields = {"SCI", "EI", "hx", "sw", "pt", "xb"};
        for (String f : fields) {
            String raw = getFieldValue(detail, f);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryXslwList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryXslw(d)[0];
        return new int[]{total, 0};
    }

    /** 教材著作：仅统计个数 */
    private int[] sumCategoryJczz(Alltotle detail) {
        int total = 0;
        String[] fields = {"cbzz1", "cbzz2", "cbyz1", "cbyz2", "cbjc1", "cbjc2", "zbjc"};
        for (String f : fields) {
            String raw = getFieldValue(detail, f);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryJczzList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryJczz(d)[0];
        return new int[]{total, 0};
    }

    /** 专利：仅统计个数 */
    private int[] sumCategoryZl(Alltotle detail) {
        int total = 0;
        String[] fields = {"sqfmzl", "syxxzl", "wxsjzl"};
        for (String f : fields) {
            String raw = getFieldValue(detail, f);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryZlList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryZl(d)[0];
        return new int[]{total, 0};
    }

    /** 软著：仅统计个数 */
    private int[] sumCategoryRz(Alltotle detail) {
        String raw = getFieldValue(detail, "jsjrjzzq");
        int total = parseIntOrZero(raw);
        return new int[]{total, 0};
    }

    private int[] sumCategoryRzList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryRz(d)[0];
        return new int[]{total, 0};
    }

    /** 奖励：仅统计个数 */
    private int[] sumCategoryJl(Alltotle detail) {
        int total = 0;
        String[] fields = {"sjjxcgj", "sjkxjsj", "stjjzrkxlpj", "stjjsklpj",
                           "xjjxcgj", "yyxkyxjcgj", "xhjjxcgj", "xhjkycgj"};
        for (String f : fields) {
            String raw = getFieldValue(detail, f);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryJlList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryJl(d)[0];
        return new int[]{total, 0};
    }

    /** 学术报告(讲座类)：仅统计个数 */
    private int[] sumCategoryJzbg(Alltotle detail) {
        int total = 0;
        String[] fields = {"jbgj", "jbgn", "cjgj", "cjgn", "xjxs"};
        for (String f : fields) {
            String raw = getFieldValue(detail, f);
            total += parseIntOrZero(raw);
        }
        return new int[]{total, 0};
    }

    private int[] sumCategoryJzbgList(List<Alltotle> details) {
        int total = 0;
        for (Alltotle d : details) total += sumCategoryJzbg(d)[0];
        return new int[]{total, 0};
    }
}
