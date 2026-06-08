package com.ruoyi.system.service.impl;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.domain.AlltotleScore;
import com.ruoyi.system.mapper.AlltotleMapper;
import com.ruoyi.system.mapper.AlltotleScoreMapper;
import com.ruoyi.system.mapper.ResearchDashboardMapper;
import com.ruoyi.system.service.IResearchDashboardService;

/**
 * 科研首页数据聚合实现.
 */
@Service
public class ResearchDashboardServiceImpl implements IResearchDashboardService
{
    private static final List<String> COLLEGE_ROLES = Arrays.asList(
            "dept_teacher", "discuss_college", "dzgc_college", "yssj_college",
            "student_college", "marxism_college", "general");

    /*
     * Dashboard queue states only: count records actionable by the active role.
     * Do not mix old numeric states, member-only data, or credited-amount todos.
     */
    private static final List<String> HORIZONTAL_TEACHER_STATES = Arrays.asList(
            "HORIZONTAL_APPLY_DRAFT", "HORIZONTAL_APPLY_REJECTED", "HORIZONTAL_APPLY_PASSED",
            "HORIZONTAL_OVER_DRAFT", "HORIZONTAL_OVER_REJECTED");
    private static final List<String> HORIZONTAL_RESEARCH_STATES = Arrays.asList(
            "HORIZONTAL_APPLY_JYS_AUDIT", "HORIZONTAL_OVER_JYS_AUDIT");
    private static final List<String> HORIZONTAL_COLLEGE_STATES = Arrays.asList(
            "HORIZONTAL_APPLY_XY_AUDIT", "HORIZONTAL_OVER_XY_AUDIT");
    private static final List<String> HORIZONTAL_KYC_STATES = Arrays.asList(
            "HORIZONTAL_APPLY_KYC_AUDIT", "HORIZONTAL_OVER_KYC_AUDIT");
    private static final List<String> REAMOUNT_TEACHER_STATES = Arrays.asList(
            "REAMOUNT_DRAFT", "REAMOUNT_REJECTED");
    private static final List<String> REAMOUNT_RESEARCH_STATES = Collections.singletonList("REAMOUNT_JYS_AUDIT");
    private static final List<String> REAMOUNT_KYC_STATES = Collections.singletonList("REAMOUNT_KYC_AUDIT");

    private static final List<String> VERTICAL_TEACHER_STATES = Arrays.asList(
            "VERTICAL_APPLY_DRAFT", "VERTICAL_APPLY_PASSED", "VERTICAL_APPLY_REJECTED",
            "VERTICAL_OVER_DRAFT", "VERTICAL_OVER_REJECTED");
    private static final List<String> VERTICAL_RESEARCH_STATES = Arrays.asList(
            "VERTICAL_APPLY_JYS_AUDIT", "VERTICAL_OVER_JYS_AUDIT");
    private static final List<String> VERTICAL_COLLEGE_STATES = Arrays.asList(
            "VERTICAL_APPLY_XY_AUDIT", "VERTICAL_OVER_XY_AUDIT");
    private static final List<String> VERTICAL_KYC_STATES = Arrays.asList(
            "VERTICAL_APPLY_KYC_AUDIT", "VERTICAL_OVER_KYC_AUDIT");

    // Achievement still has legacy numeric states in existing data; keep aliases that render as the same TEC_TRA status.
    private static final List<String> ACHIEVEMENT_TEACHER_STATES = Arrays.asList("TEC_TRA_DRAFT", "15", "TEC_TRA_PASSED", "4", "6");
    private static final List<String> ACHIEVEMENT_RESEARCH_STATES = Collections.singletonList("TEC_TRA_JYS_AUDIT");
    private static final List<String> ACHIEVEMENT_COLLEGE_STATES = Collections.singletonList("TEC_TRA_XY_AUDIT");
    private static final List<String> ACHIEVEMENT_KYC_STATES = Collections.singletonList("TEC_TRA_KYC_AUDIT");

    private static final List<String> PAPER_TEACHER_STATES = Arrays.asList("PAPER_DRAFT", "PAPER_REJECTED");
    private static final List<String> PAPER_RESEARCH_STATES = Collections.singletonList("PAPER_JYS_AUDIT");
    private static final List<String> PAPER_COLLEGE_STATES = Collections.singletonList("PAPER_XY_AUDIT");
    private static final List<String> PAPER_KYC_STATES = Collections.singletonList("PAPER_KYC_AUDIT");

    private static final List<String> TEXTBOOK_TEACHER_STATES = Arrays.asList("TEXTBOOK_DRAFT", "TEXTBOOK_REJECTED");
    private static final List<String> TEXTBOOK_RESEARCH_STATES = Collections.singletonList("TEXTBOOK_JYS_AUDIT");
    private static final List<String> TEXTBOOK_COLLEGE_STATES = Collections.singletonList("TEXTBOOK_XY_AUDIT");
    private static final List<String> TEXTBOOK_KYC_STATES = Collections.singletonList("TEXTBOOK_KYC_AUDIT");

    private static final List<String> PATENT_TEACHER_STATES = Arrays.asList("PATENT_DRAFT", "PATENT_REJECTED");
    private static final List<String> PATENT_RESEARCH_STATES = Collections.singletonList("PATENT_JYS_AUDIT");
    private static final List<String> PATENT_COLLEGE_STATES = Collections.singletonList("PATENT_XY_AUDIT");
    private static final List<String> PATENT_KYC_STATES = Collections.singletonList("PATENT_KYC_AUDIT");

    private static final List<String> REWARD_TEACHER_STATES = Arrays.asList("REWARD_DRAFT", "REWARD_REJECTED");
    private static final List<String> REWARD_RESEARCH_STATES = Collections.singletonList("REWARD_JYS_AUDIT");
    private static final List<String> REWARD_COLLEGE_STATES = Collections.singletonList("REWARD_XY_AUDIT");
    private static final List<String> REWARD_KYC_STATES = Collections.singletonList("REWARD_KYC_AUDIT");

    private static final List<String> LECTURE_TEACHER_STATES = Arrays.asList("LECTURE_DRAFT", "LECTURE_REJECTED");
    private static final List<String> LECTURE_RESEARCH_STATES = Collections.singletonList("LECTURE_JYS_AUDIT");
    private static final List<String> LECTURE_COLLEGE_STATES = Collections.singletonList("LECTURE_XY_AUDIT");
    private static final List<String> LECTURE_KYC_STATES = Collections.singletonList("LECTURE_KYC_AUDIT");

    @Autowired
    private ResearchDashboardMapper dashboardMapper;

    @Autowired
    private AlltotleMapper alltotleMapper;

    @Autowired
    private AlltotleScoreMapper alltotleScoreMapper;

    @Override
    public Map<String, Object> getDashboard(SysUser user, String year)
    {
        String safeYear = StringUtils.isNotEmpty(year) ? year : String.valueOf(Year.now().getValue());
        String roleKey = determineRoleKey(user);
        Map<String, Integer> todoCounts = buildTodoCounts(user, roleKey, safeYear);
        List<Alltotle> alltotles = selectAlltotleScope(user, roleKey);
        List<AlltotleScore> scores = selectAlltotleScoreScope(user, roleKey);
        List<Map<String, Object>> progressItems = buildProgressItems(alltotles, scores);
        int totalCount = progressItems.stream().mapToInt(item -> intValue(item.get("count"))).sum();
        double totalScore = progressItems.stream().mapToDouble(item -> doubleValue(item.get("score"))).sum();
        int totalTodo = todoCounts.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Object> data = new HashMap<>();
        data.put("role", buildRole(user, roleKey));
        data.put("year", safeYear);
        data.put("metrics", buildMetrics(roleKey, totalCount, totalTodo, totalScore));
        data.put("workEntries", buildWorkEntries(todoCounts));
        data.put("queueItems", buildQueueItems(todoCounts, roleKey));
        data.put("progressItems", progressItems);
        data.put("riskItems", buildRiskItems(todoCounts));
        return data;
    }

    private Map<String, Integer> buildTodoCounts(SysUser user, String roleKey, String year)
    {
        Map<String, Integer> counts = new LinkedHashMap<>();
        // Current queue covers only the eight fixed business modules.
        counts.put("horizontal", countHorizontal(user, roleKey, year));
        counts.put("vertical", countVertical(user, roleKey, year));
        counts.put("achievement", countAchievement(user, roleKey, year));
        counts.put("paper", countModule(user, roleKey, year,
                PAPER_TEACHER_STATES, PAPER_RESEARCH_STATES, PAPER_COLLEGE_STATES, PAPER_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countPaperByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countPaperByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countPaperByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countPaperByUserAndStates(userId, states, year); }
                }));
        counts.put("textbook", countModule(user, roleKey, year,
                TEXTBOOK_TEACHER_STATES, TEXTBOOK_RESEARCH_STATES, TEXTBOOK_COLLEGE_STATES, TEXTBOOK_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countTextbookByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countTextbookByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countTextbookByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countTextbookByUserAndStates(userId, states, year); }
                }));
        counts.put("patent", countModule(user, roleKey, year,
                PATENT_TEACHER_STATES, PATENT_RESEARCH_STATES, PATENT_COLLEGE_STATES, PATENT_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countPatentByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countPatentByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countPatentByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countPatentByUserAndStates(userId, states, year); }
                }));
        counts.put("reward", countModule(user, roleKey, year,
                REWARD_TEACHER_STATES, REWARD_RESEARCH_STATES, REWARD_COLLEGE_STATES, REWARD_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countRewardByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countRewardByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countRewardByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countRewardByUserAndStates(userId, states, year); }
                }));
        counts.put("lecture", countModule(user, roleKey, year,
                LECTURE_TEACHER_STATES, LECTURE_RESEARCH_STATES, LECTURE_COLLEGE_STATES, LECTURE_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countLectureByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countLectureByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countLectureByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countLectureByUserAndStates(userId, states, year); }
                }));
        return counts;
    }

    private int countHorizontal(SysUser user, String roleKey, String year)
    {
        int horizontalCount = countModule(user, roleKey, year,
                HORIZONTAL_TEACHER_STATES, HORIZONTAL_RESEARCH_STATES, HORIZONTAL_COLLEGE_STATES, HORIZONTAL_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countHorizontalByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countHorizontalByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countHorizontalByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countHorizontalByUserAndStates(userId, states, year); }
                });
        return horizontalCount + countReamount(user, roleKey, year);
    }

    private int countReamount(SysUser user, String roleKey, String year)
    {
        Long userId = user != null ? user.getUserId() : null;
        Long deptId = user != null ? user.getDeptId() : null;
        if ("admin".equals(roleKey) || "sci_tesearch".equals(roleKey))
        {
            return dashboardMapper.countReamountByStates(REAMOUNT_KYC_STATES, year);
        }
        if ("research".equals(roleKey))
        {
            return deptId == null ? 0 : dashboardMapper.countReamountByDeptAndStates(deptId, REAMOUNT_RESEARCH_STATES, year);
        }
        if ("teacher".equals(roleKey))
        {
            return userId == null ? 0 : dashboardMapper.countReamountByUserAndStates(userId, REAMOUNT_TEACHER_STATES, year);
        }
        return 0;
    }

    private int countVertical(SysUser user, String roleKey, String year)
    {
        return countModule(user, roleKey, year,
                VERTICAL_TEACHER_STATES, VERTICAL_RESEARCH_STATES, VERTICAL_COLLEGE_STATES, VERTICAL_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countVerticalByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countVerticalByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countVerticalByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countVerticalByUserAndStates(userId, states, year); }
                });
    }

    private int countAchievement(SysUser user, String roleKey, String year)
    {
        return countModule(user, roleKey, year,
                ACHIEVEMENT_TEACHER_STATES, ACHIEVEMENT_RESEARCH_STATES, ACHIEVEMENT_COLLEGE_STATES, ACHIEVEMENT_KYC_STATES,
                new ModuleCounter()
                {
                    public int all(List<String> states, String year) { return dashboardMapper.countAchievementByStates(states, year); }
                    public int dept(Long deptId, List<String> states, String year) { return dashboardMapper.countAchievementByDeptAndStates(deptId, states, year); }
                    public int deptWithChildren(Long deptId, List<String> states, String year) { return dashboardMapper.countAchievementByDeptAndStatesWithChildren(deptId, states, year); }
                    public int user(Long userId, List<String> states, String year) { return dashboardMapper.countAchievementByUserAndStates(userId, states, year); }
                });
    }

    private int countModule(SysUser user, String roleKey, String year, List<String> personalStates,
            List<String> researchStates, List<String> collegeStates, List<String> kycStates, ModuleCounter counter)
    {
        Long userId = user != null ? user.getUserId() : null;
        Long deptId = user != null ? user.getDeptId() : null;
        Long collegeDeptId = getCollegeDeptId(user);
        int personal = userId == null ? 0 : counter.user(userId, personalStates, year);
        // Admin follows the research-office queue; manager roles do not add personal draft/rejected items.
        if ("admin".equals(roleKey) || "sci_tesearch".equals(roleKey))
        {
            return counter.all(kycStates, year);
        }
        if ("dept_teacher".equals(roleKey))
        {
            return collegeDeptId == null ? 0 : counter.deptWithChildren(collegeDeptId, collegeStates, year);
        }
        if ("research".equals(roleKey))
        {
            return deptId == null ? 0 : counter.dept(deptId, researchStates, year);
        }
        return personal;
    }

    private List<Alltotle> selectAlltotleScope(SysUser user, String roleKey)
    {
        if (user == null)
        {
            return Collections.emptyList();
        }
        if ("teacher".equals(roleKey))
        {
            Alltotle one = alltotleMapper.selectAlltotleByUserId(user.getUserId());
            return one == null ? Collections.emptyList() : Collections.singletonList(one);
        }
        Alltotle query = new Alltotle();
        if ("research".equals(roleKey))
        {
            query.setDeptId(user.getDeptId());
        }
        else if ("dept_teacher".equals(roleKey))
        {
            query.setPartenId(getCollegeDeptId(user));
        }
        return alltotleMapper.selectAlltotleList(query);
    }

    private List<AlltotleScore> selectAlltotleScoreScope(SysUser user, String roleKey)
    {
        if (user == null)
        {
            return Collections.emptyList();
        }
        if ("teacher".equals(roleKey))
        {
            AlltotleScore one = alltotleScoreMapper.selectAlltotleScoreByUserId(user.getUserId());
            return one == null ? Collections.emptyList() : Collections.singletonList(one);
        }
        AlltotleScore query = new AlltotleScore();
        if ("research".equals(roleKey))
        {
            query.setDeptId(user.getDeptId());
        }
        else if ("dept_teacher".equals(roleKey))
        {
            query.setPartenId(getCollegeDeptId(user));
        }
        return alltotleScoreMapper.selectAlltotleScoreList(query);
    }

    private List<Map<String, Object>> buildWorkEntries(Map<String, Integer> counts)
    {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(work("horizontal", "横向课题", "system/apply", "fa-handshake-o", "blue", counts));
        items.add(work("vertical", "纵向课题", "system/apply_vertical", "fa-cube", "teal", counts));
        items.add(work("achievement", "成果转化", "IntraSchPro", "fa-refresh", "ink", counts));
        items.add(work("paper", "论文", "system/paper", "fa-file-text-o", "blue", counts));
        items.add(work("textbook", "教材专著", "system/jiaocairuanzhu", "fa-book", "amber", counts));
        items.add(work("patent", "专利软著", "system/zhuanliruanzhu", "fa-certificate", "teal", counts));
        items.add(work("reward", "奖励", "system/reward", "fa-trophy", "amber", counts));
        items.add(work("lecture", "讲座报告", "system/report", "fa-microphone", "blue", counts));
        return items;
    }

    private List<Map<String, Object>> buildQueueItems(Map<String, Integer> counts, String roleKey)
    {
        String status = statusForRole(roleKey);
        return buildWorkEntries(counts).stream()
                .filter(item -> intValue(item.get("badge")) > 0)
                .sorted((a, b) -> Integer.compare(intValue(b.get("badge")), intValue(a.get("badge"))))
                .limit(10)
                .map(item -> {
                    int count = intValue(item.get("badge"));
                    Map<String, Object> queue = new HashMap<>();
                    queue.put("module", item.get("name"));
                    queue.put("title", "待处理 " + count + " 条");
                    queue.put("status", status);
                    queue.put("owner", scopeName(roleKey));
                    queue.put("deadline", "尽快处理");
                    queue.put("level", count >= 10 ? "danger" : "warn");
                    queue.put("url", item.get("url"));
                    return queue;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> buildProgressItems(List<Alltotle> alltotles, List<AlltotleScore> scores)
    {
        double maxScore = Math.max(100D, totalScore(scores));
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(progress("横向课题", countHorizontalResults(alltotles), sum(scores, "hxkt"), maxScore, "blue"));
        items.add(progress("纵向课题", countVerticalResults(alltotles), sum(scores, "zxktxj") + sum(scores, "zxktxjys"), maxScore, "teal"));
        items.add(progress("成果转化", countFields(alltotles, "eyxxx", "edwxx", "wdsxx", "sdesxx", "esdsswxx", "sswdwsxx", "dywsxx"), sum(scores, "cgzh"), maxScore, "ink"));
        items.add(progress("论文", countFields(alltotles, "SCI", "EI", "hx", "sw", "pt", "xb"), sum(scores, "xslw"), maxScore, "blue"));
        items.add(progress("教材专著", countFields(alltotles, "cbzz1", "cbzz2", "cbyz1", "cbyz2", "cbjc1", "cbjc2", "zbjc"), sum(scores, "jczz"), maxScore, "amber"));
        items.add(progress("专利软著", countFields(alltotles, "sqfmzl", "syxxzl", "wxsjzl", "jsjrjzzq"), sum(scores, "zlrz") + sum(scores, "zl") + sum(scores, "rz"), maxScore, "green"));
        items.add(progress("奖励", countFields(alltotles, "sjjxcgj", "sjkxjsj", "stjjzrkxlpj", "stjjsklpj", "xjjxcgj", "yyxkyxjcgj", "xhjjxcgj", "xhjkycgj"), sum(scores, "jl"), maxScore, "amber"));
        items.add(progress("讲座报告", countFields(alltotles, "jbgj", "jbgn", "cjgj", "cjgn", "xjxs"), sum(scores, "jzbg"), maxScore, "teal"));
        return items;
    }

    private List<Map<String, Object>> buildMetrics(String roleKey, int totalCount, int totalTodo, double totalScore)
    {
        List<Map<String, Object>> metrics = new ArrayList<>();
        if ("teacher".equals(roleKey))
        {
            metrics.add(metric("我的申报", String.valueOf(totalCount), "当前年度汇总成果", "primary"));
            metrics.add(metric("待我处理", String.valueOf(totalTodo), "来自各业务菜单待办", "warning"));
            metrics.add(metric("科研积分", formatScore(totalScore), "来自统计积分汇总", "success"));
            metrics.add(metric("已归档成果", String.valueOf(Math.max(0, totalCount - totalTodo)), "按统计汇总估算", "info"));
        }
        else
        {
            metrics.add(metric("待处理", String.valueOf(totalTodo), "当前角色待办事项", "warning"));
            metrics.add(metric("年度成果", String.valueOf(totalCount), "当前数据范围成果", "primary"));
            metrics.add(metric("科研积分", formatScore(totalScore), "来自统计积分汇总", "success"));
            metrics.add(metric("数据范围", scopeName(roleKey), "跟随当前切换角色", "info"));
        }
        return metrics;
    }

    private List<Map<String, Object>> buildRiskItems(Map<String, Integer> counts)
    {
        List<Map<String, Object>> risks = new ArrayList<>();
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        Map.Entry<String, Integer> max = counts.entrySet().stream()
                .max((a, b) -> Integer.compare(a.getValue(), b.getValue()))
                .orElse(null);
        risks.add(risk("待办积压", "各菜单待处理事项合计", String.valueOf(total), total >= 20 ? "red" : "amber"));
        if (max != null && max.getValue() > 0)
        {
            risks.add(risk("重点模块", moduleName(max.getKey()) + "待处理数量最高", String.valueOf(max.getValue()), max.getValue() >= 10 ? "red" : "blue"));
        }
        risks.add(risk("数据一致", "首页角标使用菜单同源统计", "8", "blue"));
        return risks;
    }

    private Map<String, Object> buildRole(SysUser user, String roleKey)
    {
        SysRole role = firstRole(user);
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", role == null ? null : role.getRoleId());
        map.put("roleKey", roleKey);
        map.put("roleName", role == null ? roleName(roleKey) : role.getRoleName());
        return map;
    }

    private Map<String, Object> work(String key, String name, String url, String icon, String color, Map<String, Integer> counts)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("key", key);
        item.put("name", name);
        item.put("url", url);
        item.put("icon", icon);
        item.put("color", color);
        item.put("badge", counts.getOrDefault(key, 0));
        return item;
    }

    private Map<String, Object> progress(String name, int count, double score, double maxScore, String type)
    {
        Map<String, Object> item = new HashMap<>();
        int percent = maxScore <= 0 ? 0 : (int) Math.min(100, Math.round(score * 100D / maxScore));
        item.put("name", name);
        item.put("count", count);
        item.put("score", score);
        item.put("percent", percent);
        item.put("text", count + "项 / " + formatScore(score) + "分");
        item.put("type", type);
        return item;
    }

    private Map<String, Object> metric(String label, String value, String extra, String type)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("label", label);
        item.put("value", value);
        item.put("extra", extra);
        item.put("type", type);
        return item;
    }

    private Map<String, Object> risk(String title, String desc, String value, String level)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("title", title);
        item.put("desc", desc);
        item.put("value", value);
        item.put("level", level);
        return item;
    }

    private int countHorizontalResults(List<Alltotle> list)
    {
        return countFields(list, "ewyyx", "edwwy", "wdswy", "sdeswy", "esdsswwy", "wswwydwswy", "wsdqswwy", "qswdybwy");
    }

    private int countVerticalResults(List<Alltotle> list)
    {
        return countFields(list, "zcgjjjkyxm", "zcsbjjjkyxm", "zcsbjjgxm", "zcsbjzxkyxm", "zctjjxhjkyxm", "zcxjjxglxmywyys", "zcxjjxglxmywyyx");
    }

    private int countFields(List<Alltotle> list, String... fields)
    {
        int count = 0;
        for (Alltotle item : list)
        {
            for (String field : fields)
            {
                count += parseLeadingInt(readProperty(item, field));
            }
        }
        return count;
    }

    private double totalScore(List<AlltotleScore> scores)
    {
        return sum(scores, "hxkt") + sum(scores, "zxktxj") + sum(scores, "zxktxjys") + sum(scores, "cgzh")
                + sum(scores, "xslw") + sum(scores, "jczz") + sum(scores, "zlrz") + sum(scores, "zl")
                + sum(scores, "rz") + sum(scores, "jl") + sum(scores, "jzbg");
    }

    private double sum(List<AlltotleScore> scores, String field)
    {
        double total = 0D;
        for (AlltotleScore score : scores)
        {
            total += parseDouble(readProperty(score, field));
        }
        return total;
    }

    private String readProperty(Object bean, String field)
    {
        if (bean == null || StringUtils.isEmpty(field))
        {
            return null;
        }
        try
        {
            String getter = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Object value = bean.getClass().getMethod(getter).invoke(bean);
            return value == null ? null : String.valueOf(value);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private int parseLeadingInt(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return 0;
        }
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (Character.isDigit(c))
            {
                digits.append(c);
            }
            else if (digits.length() > 0)
            {
                break;
            }
        }
        return digits.length() == 0 ? 0 : Integer.parseInt(digits.toString());
    }

    private double parseDouble(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return 0D;
        }
        try
        {
            return Double.parseDouble(text.trim());
        }
        catch (NumberFormatException e)
        {
            return 0D;
        }
    }

    private int intValue(Object value)
    {
        return value instanceof Number ? ((Number) value).intValue() : parseLeadingInt(String.valueOf(value));
    }

    private double doubleValue(Object value)
    {
        return value instanceof Number ? ((Number) value).doubleValue() : parseDouble(String.valueOf(value));
    }

    private String determineRoleKey(SysUser user)
    {
        List<SysRole> roles = user == null ? Collections.emptyList() : user.getRoles();
        // Follow the system-level role switch by reading activeRoleId from the session first.
        Long activeRoleId = getActiveRoleId();
        if (activeRoleId != null)
        {
            for (SysRole role : roles)
            {
                if (role != null && Objects.equals(activeRoleId, role.getRoleId()))
                {
                    return normalizeRoleKey(role.getRoleKey());
                }
            }
        }
        if (hasRole(roles, "admin")) return "admin";
        if (hasRole(roles, "sci_tesearch")) return "sci_tesearch";
        if (roles.stream().anyMatch(role -> COLLEGE_ROLES.contains(role.getRoleKey()))) return "dept_teacher";
        if (hasRole(roles, "research")) return "research";
        return "teacher";
    }

    private Long getActiveRoleId()
    {
        try
        {
            // /system/role/switch writes the current active role into the Shiro session.
            org.apache.shiro.session.Session session = org.apache.shiro.SecurityUtils.getSubject().getSession(false);
            Object activeRoleId = session == null ? null : session.getAttribute("activeRoleId");
            if (activeRoleId instanceof Number)
            {
                return ((Number) activeRoleId).longValue();
            }
            if (activeRoleId != null)
            {
                return Long.valueOf(String.valueOf(activeRoleId));
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }

    private String normalizeRoleKey(String roleKey)
    {
        if ("admin".equals(roleKey) || "sci_tesearch".equals(roleKey) || "research".equals(roleKey))
        {
            return roleKey;
        }
        if (COLLEGE_ROLES.contains(roleKey))
        {
            return "dept_teacher";
        }
        return "teacher";
    }

    private boolean hasRole(List<SysRole> roles, String roleKey)
    {
        return roles != null && roles.stream().anyMatch(r -> Objects.equals(roleKey, r.getRoleKey()));
    }

    private SysRole firstRole(SysUser user)
    {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty())
        {
            return null;
        }
        Long activeRoleId = getActiveRoleId();
        if (activeRoleId != null)
        {
            for (SysRole role : user.getRoles())
            {
                if (role != null && Objects.equals(activeRoleId, role.getRoleId()))
                {
                    return role;
                }
            }
        }
        return user.getRoles().get(0);
    }

    private Long getCollegeDeptId(SysUser user)
    {
        if (user == null)
        {
            return null;
        }
        if (user.getDeptId() != null)
        {
            return user.getDeptId();
        }
        return user.getDept() == null ? null : user.getDept().getDeptId();
    }

    private String roleName(String roleKey)
    {
        switch (roleKey)
        {
            case "admin": return "系统管理员";
            case "sci_tesearch": return "科研处";
            case "dept_teacher": return "学院";
            case "research": return "教研室";
            default: return "普通教师";
        }
    }

    private String statusForRole(String roleKey)
    {
        switch (roleKey)
        {
            case "sci_tesearch":
            case "admin": return "待科研处处理";
            case "dept_teacher": return "待学院处理";
            case "research": return "待教研室处理";
            default: return "待本人处理";
        }
    }

    private String scopeName(String roleKey)
    {
        switch (roleKey)
        {
            case "sci_tesearch":
            case "admin": return "全校";
            case "dept_teacher": return "学院";
            case "research": return "教研室";
            default: return "个人";
        }
    }

    private String moduleName(String key)
    {
        switch (key)
        {
            case "horizontal": return "横向课题";
            case "vertical": return "纵向课题";
            case "achievement": return "成果转化";
            case "paper": return "论文";
            case "textbook": return "教材专著";
            case "patent": return "专利软著";
            case "reward": return "奖励";
            case "lecture": return "讲座报告";
            default: return key;
        }
    }

    private String formatScore(double score)
    {
        return score == Math.rint(score) ? String.valueOf((long) score) : String.format("%.1f", score);
    }

    private interface ModuleCounter
    {
        int all(List<String> states, String year);
        int dept(Long deptId, List<String> states, String year);
        int deptWithChildren(Long deptId, List<String> states, String year);
        int user(Long userId, List<String> states, String year);
    }
}
