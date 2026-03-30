package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciProjectScoreCfgService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* 纵向课题Controller
* */
@Controller
@RequestMapping("/system/apply_vertical")
public class SciHorizontalApplyVerticalController extends BaseController {
    private String prefix = "system/apply_vertical";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciHorizontalApplyVerticalService sciHorizontalApplyVerticalService;

    @Autowired
    private ISciProjectScoreCfgService sciProjectScoreCfgService;

    @Autowired
    private ISciHorizontalPiyueService piyueService;

    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;


    // 计算纵向课题预期积分

    /**
     * 计算纵向课题预期积分
     * 功能：根据课题类型和排名计算预期科研分
     * 按照2026年度新的科研分计算标准
     */
    @PostMapping("/calculateScore")
    @ResponseBody
    @Log(title = "积分计算", businessType = BusinessType.OTHER)
    public AjaxResult calculateScore(@RequestParam(required = false) String topType,
                                     @RequestParam(required = false) String subjectSource,
                                     @RequestParam(required = false) Double amount) {
        try {
            // 计算每位成员的预期科研分
            List<Integer> expectedScores = new ArrayList<>();
            // 主持人（排名第一）
            expectedScores.add(calculateScoreByTypeAndRank(topType, 1).intValue());
            // 成员1（排名第二）
            expectedScores.add(calculateScoreByTypeAndRank(topType, 2).intValue());
            // 成员2（排名第三）
            expectedScores.add(calculateScoreByTypeAndRank(topType, 3).intValue());
            // 成员3（排名第四）
            expectedScores.add(calculateScoreByTypeAndRank(topType, 4).intValue());
            
            return AjaxResult.success(expectedScores);
        } catch (Exception e) {
            return AjaxResult.error("积分计算失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据课题类型和排名计算积分
     * 按照2026年度新的科研分计算标准
     */
    private Double calculateScoreByTypeAndRank(String topType, int rank) {
        // 映射数字类型的topType到课题类型标签
        String typeLabel = mapTopTypeToLabel(topType);
        
        // 根据课题类型和排名计算积分
        switch (typeLabel) {
            case "主持国家基金科研项目":
                switch (rank) {
                    case 1: return 8000.0;
                    case 2: return 3200.0;
                    case 3: return 1600.0;
                    case 4: return 800.0;
                    default: return 0.0;
                }
            case "主持省部级基金科研项目":
                switch (rank) {
                    case 1: return 4000.0;
                    case 2: return 1600.0;
                    case 3: return 800.0;
                    case 4: return 400.0;
                    default: return 0.0;
                }
            case "主持省部级教改项目":
            case "主持省部级纵向科研项目":
                switch (rank) {
                    case 1: return 400.0;
                    case 2: return 160.0;
                    case 3: return 80.0;
                    case 4: return 40.0;
                    default: return 0.0;
                }
            case "主持厅局级、学会级纵向科研项目":
                switch (rank) {
                    case 1: return 300.0;
                    case 2: return 120.0;
                    case 3: return 60.0;
                    case 4: return 30.0;
                    default: return 0.0;
                }
            case "主持校级教学（管理）改革项目（含实验室建设项目）1万元以上（含1万元）":
                switch (rank) {
                    case 1: return 30.0;
                    case 2: return 12.0;
                    case 3: return 6.0;
                    case 4: return 3.0;
                    default: return 0.0;
                }
            case "主持校级教学（管理）改革项目（含实验室建设项目）1万元以下":
                switch (rank) {
                    case 1: return 20.0;
                    case 2: return 8.0;
                    case 3: return 4.0;
                    case 4: return 2.0;
                    default: return 0.0;
                }
            default:
                return 0.0;
        }
    }
    
    /**
     * 映射数字类型的topType到课题类型标签
     */
    private String mapTopTypeToLabel(String topType) {
        // 根据实际的sci_vertical_type字典值进行映射
        switch (topType) {
            case "1": return "主持国家基金科研项目";
            case "2": return "主持省部级基金科研项目";
            case "3": return "主持省部级教改项目";
            case "4": return "主持省部级纵向科研项目";
            case "5": return "主持厅局级、学会级纵向科研项目";
            case "6": return "主持校级教学（管理）改革项目（含实验室建设项目）1万元以上（含1万元）";
            case "7": return "主持校级教学（管理）改革项目（含实验室建设项目）1万元以下";
            default: return topType; // 如果是文本类型，直接返回
        }
    }

    // 设置角色集合。若后期需要添加新的学院管理员角色，将其权限字符添加到集合中即可
    private static final Set<String> TEACHER_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher", // 软件学院管理员
            "discuss_college", // 商学院管理员
            "dzgc_college", // 电子工程学院管理员
            "yssj_college", // 艺术设计学院管理员
            "student_college", // 学生处学院管理员
            "marxism_college", // 马克思主义学院管理员
            "general" //综合院部管理员
    ));

//    访问的首页
    @RequiresPermissions("system:apply_vertical:view")
    @Log(title = "访问纵向课题首页", businessType = BusinessType.OTHER)
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    /**
     * 查询纵向课题列表
     * 功能：根据角色和表格ID查询纵向课题列表
     * SQL：根据角色不同，执行不同的查询语句
     */
    @RequiresPermissions("system:apply_vertical:list")
    @Log(title = "查询纵向课题列表", businessType = BusinessType.OTHER)
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId,String year ,SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setUid(getUserId());
        sciHorizontalApplyVertical.setYear(year);
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        label:
        for (SysRole r : roles) {
            if ("sci_tesearch".equals(r.getRoleKey())) {
                role = "sci_tesearch";
                break;
            } else if ("research".equals(r.getRoleKey())) {
                role = "research";
                break;
            } else if (TEACHER_ROLES.contains(r.getRoleKey())) {
                role = "dept_teacher";
                break;
            }
        }
        sciHorizontalApplyVertical.setRole(role);
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//        教研室
            case "research":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//      学院负责人
            case "dept_teacher":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
//        教师
            default:
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
                        break;
                }
                break;
        }
        List<SciHorizontalApplyVertical> list1 = new ArrayList<>();
        list1 = sciHorizontalApplyVerticalService.selectOtherListByUid(sciHorizontalApplyVertical);
        list.addAll(list1);
        SysUser  sysUser =getSysUser();

        // 去重操作
        List<SciHorizontalApplyVertical> distinctList = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                SciHorizontalApplyVertical::getTopName, // 使用 topName 作为键
                                Function.identity(), // 值为原对象
                                (existing, replacement) -> existing, // 如果有重复，保留第一个出现的对象
                                LinkedHashMap::new // 保持插入顺序
                        ),
                        map -> new ArrayList<>(map.values()) // 将 Map 的值转换为 List
                ));

        Integer did = sysUser.getDeptId().intValue();
        Integer yid = sysUser.getParentId().intValue();
        if(!distinctList.isEmpty()){
            // 批量获取所有相关的积分记录
            Set<Integer> applyIds = distinctList.stream()
                    .map(SciHorizontalApplyVertical::getId)
                    .collect(Collectors.toSet());
            
            // 从数据库批量查询积分记录
            List<SciUserScore> allScores = sciUserScoreMapper.selectScoreVerticalByApplyIds(applyIds);
            
            // 构建按verticalId分组的积分映射，过滤掉verticalId为null的记录
            Map<String, List<SciUserScore>> scoreMap = allScores.stream()
                    .filter(score -> score.getVerticalId() != null) // 过滤掉verticalId为null的记录
                    .collect(Collectors.groupingBy(SciUserScore::getVerticalId, 
                            Collectors.toList()));
            
            String currentUserId = getUserId().toString();
            
            for (SciHorizontalApplyVertical apply: distinctList){
                apply.setUserdnameId(did);
                apply.setUserynameId(yid);
                apply.setUid(getUserId());
                
                List<SciUserScore> score = scoreMap.getOrDefault(apply.getId().toString(), new ArrayList<>());
                ArrayList<Integer> allscore = new ArrayList<>();
                
                for(SciUserScore score1: score){
                    if (apply.getFirstPersonId().equals(score1.getUserId()) && apply.getFirstPersonId().equals(currentUserId)) {
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    } else if (apply.getSecondPersonId().equals(score1.getUserId()) && apply.getSecondPersonId().equals(currentUserId)) {
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    } else if (apply.getThirdPersonId().equals(score1.getUserId()) && apply.getThirdPersonId().equals(currentUserId)) {
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    } else if (apply.getFourthPersonId().equals(score1.getUserId()) && apply.getFourthPersonId().equals(currentUserId)) {
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    }
                }
                
                Integer count = 0;
                for (int i : allscore){
                    count += i;
                }
                apply.setScore(count.toString());
            }
        }

        // 获取分页参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        int total = distinctList.size();

        // 计算当前页的起始和结束索引
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(pageNum * pageSize, total);

        // 防止越界
        if (fromIndex > total) {
            distinctList = new ArrayList<>();
        } else {
            distinctList = distinctList.subList(fromIndex, toIndex);
        }

        // 返回分页数据
        TableDataInfo data = getDataTable(distinctList);
        data.setTotal(total);
        return data;
    }

    /**
     * 导出纵向课题列表
     */
    @RequiresPermissions("system:apply_vertical:export")
    @Log(title = "导出纵向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        // 获取当前用户的所有角色
        List<SysRole> roles = getSysUser().getRoles();

        // 遍历 roles 列表并提取每个 SysRole 的 roleKey
        for (SysRole role : roles) {
            if (!role.getRoleKey().equals("teacher")){
                if (role.getRoleKey().equals("dept_teacher"))
                    sciHorizontalApplyVertical.setYnameId(getSysUser().getParentId().toString());
                else if (role.getRoleKey().equals("research")) {
                    sciHorizontalApplyVertical.setDnameId(Integer.valueOf(getSysUser().getDeptId().toString()));
                }
            }
        }
        List<SciHorizontalApplyVertical> list = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalAllList(sciHorizontalApplyVertical);
        for (SciHorizontalApplyVertical applyVeryical: list ) {
            if(applyVeryical.getState().equals("4444")){
                applyVeryical.setState("结项");
            }else{
                applyVeryical.setState("在研");
            }
        }
        ExcelUtil<SciHorizontalApplyVertical> util = new ExcelUtil<SciHorizontalApplyVertical>(SciHorizontalApplyVertical.class);
        return util.exportExcel(list, "纵向课题数据");
    }

    /**
     * 新增立项申请课题
     * 功能：跳转到新增纵向课题立项申请页面
     * SQL：SELECT * FROM sys_user WHERE dept_id = ?
     */
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "跳转到新增纵向课题页面", businessType = BusinessType.OTHER)
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        SysUser user1=getSysUser();
        Integer deptId = user1.getDeptId().intValue();
        List<SysUser> userList =  userService.selectUser(deptId);
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId().equals(getUserId())){
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a,user);
                break;
            }
        }
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList.add(other);

        mmap.put("sysUsers",userList);
        return prefix + "/add";
    }
    /**
     * 保存新增立项申请
     * 功能：保存纵向课题立项申请信息，包括成员信息
     * SQL：INSERT INTO sci_horizontal_apply_vertical
     * SQL：INSERT INTO sci_persion_vertical
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "立项申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(SciHorizontalApplyVertical sciHorizontalApplyVertical, javax.servlet.http.HttpServletRequest request)
    {
        sciHorizontalApplyVertical.setState("99");
        int result = sciHorizontalApplyVerticalService.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        if (result == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }
        // 合并编辑页成员（前四位 + 动态 members[]），保序去重并写入 sci_persion_vertical（ranking 从1开始）
        String first = String.valueOf(getUserId());
        String second = request.getParameter("secondPersonId");
        String third = request.getParameter("thirdPersonId");
        String fourth = request.getParameter("fourthPersonId");
        String[] members = request.getParameterValues("members");
        List<String> all = new ArrayList<>();
        if (first != null && !first.isEmpty()) all.add(first);
        if (second != null && !second.isEmpty()) all.add(second);
        if (third != null && !third.isEmpty()) all.add(third);
        if (fourth != null && !fourth.isEmpty()) all.add(fourth);
        if (members != null) {
            for (String m : members) {
                if (m != null && !m.isEmpty()) all.add(m);
            }
        }

        try {
            sciHorizontalApplyVerticalService.resetVerticalPersons(sciHorizontalApplyVertical.getId(), all);
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }
        return toAjax(result);
    }

    /**
     * 提交申请进行审批
     * 功能：提交纵向课题申请进行审批
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ?, new_sql = ? WHERE id = ?
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "提交纵向课题申请", businessType = BusinessType.UPDATE)
    @PostMapping("/push/{id}")
    @ResponseBody
    @Transactional
    public AjaxResult push(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical1 = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(sciHorizontalApplyVertical.getId());
        sciHorizontalApplyVertical.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        sciHorizontalApplyVertical.setNewsql("999");
        if (sciHorizontalApplyVertical1.getValidityData() !=  null && !sciHorizontalApplyVertical1.getValidityData().isEmpty())
            sciHorizontalApplyVertical.setState("11");
        else
            sciHorizontalApplyVertical.setState("1");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 结项
     */
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "跳转到纵向课题结项页面", businessType = BusinessType.OTHER)
    @GetMapping("/overadd")
    public String overadd( Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList.add(other);

        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overadd";
    }
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "结项", businessType = BusinessType.INSERT)
    @PostMapping("/overadd")
    @ResponseBody
    public AjaxResult overaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {

        sciHorizontalApplyVertical.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        sciHorizontalApplyVertical.setState("11");
        sciHorizontalApplyVertical.setNewsql("11");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 修改申请
     * 功能：跳转到修改纵向课题申请页面
     * SQL：SELECT * FROM sci_horizontal_apply_vertical WHERE id = ?
     * SQL：SELECT * FROM sys_user
     * SQL：SELECT * FROM sci_persion_vertical WHERE verticalid = ?
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "跳转到修改纵向课题页面", businessType = BusinessType.OTHER)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/edit";
    }

    /**
     * 保存修改申请
     * 功能：保存修改后的纵向课题申请信息，包括成员信息
     * SQL：UPDATE sci_horizontal_apply_vertical
     * SQL：DELETE FROM sci_persion_vertical WHERE verticalid = ?
     * SQL：INSERT INTO sci_persion_vertical
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(SciHorizontalApplyVertical sciHorizontalApplyVertical, javax.servlet.http.HttpServletRequest request)
    {
        sciHorizontalApplyVertical.setNewsql("111");
        // 合并编辑页成员（前四位 + 动态 members[]），保序去重并写入 sci_persion_vertical（ranking 从1开始）
        String first = String.valueOf(getUserId());
        String second = request.getParameter("secondPersonId");
        String third = request.getParameter("thirdPersonId");
        String fourth = request.getParameter("fourthPersonId");
        String[] members = request.getParameterValues("members");
        List<String> all = new ArrayList<>();
        if (first != null && !first.isEmpty()) all.add(first);
        if (second != null && !second.isEmpty()) all.add(second);
        if (third != null && !third.isEmpty()) all.add(third);
        if (fourth != null && !fourth.isEmpty()) all.add(fourth);
        if (members != null) {
            for (String m : members) {
                if (m != null && !m.isEmpty()) all.add(m);
            }
        }

        try {
            sciHorizontalApplyVerticalService.resetVerticalPersons(sciHorizontalApplyVertical.getId(), all);
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }
        int result = sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        if (result == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }
        return toAjax(result);
    }

    /**
     * 修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "跳转到修改纵向课题结项页面", businessType = BusinessType.OTHER)
    @GetMapping("/overedit/{id}")
    public String overedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overedit";
    }
    /**
     *保存修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/overedit")
    @ResponseBody
    @Transactional
    public AjaxResult overeditSave(SciHorizontalApplyVertical sciHorizontalApplyVertical, javax.servlet.http.HttpServletRequest request)
    {
        sciHorizontalApplyVertical.setNewsql("111");
        // 合并编辑页成员（前四位 + 动态 members[]），保序去重并写入 sci_persion_vertical（ranking 从1开始）
        String first = String.valueOf(getUserId());
        String second = request.getParameter("secondPersonId");
        String third = request.getParameter("thirdPersonId");
        String fourth = request.getParameter("fourthPersonId");
        String[] members = request.getParameterValues("members");
        List<String> all = new ArrayList<>();
        if (first != null && !first.isEmpty()) all.add(first);
        if (second != null && !second.isEmpty()) all.add(second);
        if (third != null && !third.isEmpty()) all.add(third);
        if (fourth != null && !fourth.isEmpty()) all.add(fourth);
        if (members != null) {
            for (String m : members) {
                if (m != null && !m.isEmpty()) all.add(m);
            }
        }

        try {
            sciHorizontalApplyVerticalService.resetVerticalPersons(sciHorizontalApplyVertical.getId(), all);
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }
        int result = sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        if (result == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }
        return toAjax(result);
    }


    /**  删除纵向课题*/
    @RequiresPermissions("system:apply_vertical:remove")
    @Log(title = "删除纵向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciHorizontalApplyVerticalService.deleteSciHorizontalApplyVerticalByIds(ids));
    }


    /**
     * 查询要审批的纵向课题
     * 功能：跳转到纵向课题审批页面
     * SQL：SELECT * FROM sci_horizontal_apply_vertical WHERE id = ?
     * SQL：SELECT * FROM sys_user
     * SQL：SELECT * FROM sci_persion_vertical WHERE verticalid = ?
     */
    @RequiresPermissions("system:apply_vertical:info")
    @Log(title = "跳转到纵向课题审批页面", businessType = BusinessType.OTHER)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/detail";
    }
    /**
     * 纵向课题申请通过
     * 功能：审批通过纵向课题申请，计算并分配科研分
     * 按照2026年度新的科研分计算标准
     */
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/applyPass")
    @ResponseBody
    public AjaxResult applyPass(String id,String urlFlag,String type,String weight,SciProjectScoreCfg sciProjectScoreCfg, SciHorizontalApplyVertical sciHorizontalApplyVertical1)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String verticalId = String.valueOf(sciHorizontalApplyVertical.getId());
        
        // 计算积分（开题70%）
        List score = new ArrayList();
        List persion = new ArrayList();
        
        // 映射数字类型的type到课题类型标签
        String mappedType = mapTopTypeToLabel(type);
        
        // 检查是否为2026年度国家基金项目、省级基金项目与外单位联合申报
        boolean is2026JointProject = false;
        // 这里需要根据实际情况判断，暂时假设subjectSource包含"联合申报"字样
        if (sciHorizontalApplyVertical1.getSubjectSource() != null && sciHorizontalApplyVertical1.getSubjectSource().contains("联合申报")) {
            // 检查是否为国家基金项目或省级基金项目
            if (mappedType.equals("主持国家基金科研项目") || mappedType.equals("主持省部级基金科研项目")) {
                is2026JointProject = true;
            }
        }
        
        // 计算各排名的积分
        double openingRatio = is2026JointProject ? 1.0 : 0.7;
        
        // 主持人（排名第一）
        if (sciHorizontalApplyVertical.getFirstPersonId() != null && !sciHorizontalApplyVertical.getFirstPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFirstPersonId());
            double score1 = calculateScoreByTypeAndRank(type, 1) * openingRatio;
            score.add(String.valueOf(score1));
        }
        
        // 成员1（排名第二）
        if (sciHorizontalApplyVertical.getSecondPersonId() != null && !sciHorizontalApplyVertical.getSecondPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getSecondPersonId());
            double score2 = calculateScoreByTypeAndRank(type, 2) * openingRatio;
            score.add(String.valueOf(score2));
        }
        
        // 成员2（排名第三）
        if (sciHorizontalApplyVertical.getThirdPersonId() != null && !sciHorizontalApplyVertical.getThirdPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getThirdPersonId());
            double score3 = calculateScoreByTypeAndRank(type, 3) * openingRatio;
            score.add(String.valueOf(score3));
        }
        
        // 成员3（排名第四）
        if (sciHorizontalApplyVertical.getFourthPersonId() != null && !sciHorizontalApplyVertical.getFourthPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFourthPersonId());
            double score4 = calculateScoreByTypeAndRank(type, 4) * openingRatio;
            score.add(String.valueOf(score4));
        }

        return toAjax(sciHorizontalApplyVerticalService.applyPass(id,getUserId(),urlFlag,score,persion,verticalId,sciHorizontalApplyVertical1.getSubjectSource()));
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/applyBh")
    @ResponseBody
    public AjaxResult applyBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.applyBh(id,getUserId(),remark,urlFlag));
    }


    //  结项批阅
    @RequiresPermissions("system:apply_vertical:info")
    @Log(title = "跳转到纵向课题结项审批页面", businessType = BusinessType.OTHER)
    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overdetail";
    }


    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/overPass")
    @ResponseBody
    public AjaxResult overPass(String id,String urlFlag,String type,String weight,SciProjectScoreCfg sciProjectScoreCfg,SciHorizontalApplyVertical sciHorizontalApplyVertical1)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String verticalId = String.valueOf(sciHorizontalApplyVertical.getId());
        
        // 计算积分（结项30%）
        List score = new ArrayList();
        List persion = new ArrayList();
        
        // 映射数字类型的type到课题类型标签
        String mappedType = mapTopTypeToLabel(type);
        
        // 检查是否为2026年度国家基金项目、省级基金项目与外单位联合申报
        boolean is2026JointProject = false;
        // 这里需要根据实际情况判断，暂时假设subjectSource包含"联合申报"字样
        if (sciHorizontalApplyVertical1.getSubjectSource() != null && sciHorizontalApplyVertical1.getSubjectSource().contains("联合申报")) {
            // 检查是否为国家基金项目或省级基金项目
            if (mappedType.equals("主持国家基金科研项目") || mappedType.equals("主持省部级基金科研项目")) {
                is2026JointProject = true;
            }
        }
        
        // 计算各排名的积分
        double closingRatio = is2026JointProject ? 1.0 : 0.3;
        
        // 主持人（排名第一）
        if (sciHorizontalApplyVertical.getFirstPersonId() != null && !sciHorizontalApplyVertical.getFirstPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFirstPersonId());
            double score1 = calculateScoreByTypeAndRank(type, 1) * closingRatio;
            score.add(String.valueOf(score1));
        }
        
        // 成员1（排名第二）
        if (sciHorizontalApplyVertical.getSecondPersonId() != null && !sciHorizontalApplyVertical.getSecondPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getSecondPersonId());
            double score2 = calculateScoreByTypeAndRank(type, 2) * closingRatio;
            score.add(String.valueOf(score2));
        }
        
        // 成员2（排名第三）
        if (sciHorizontalApplyVertical.getThirdPersonId() != null && !sciHorizontalApplyVertical.getThirdPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getThirdPersonId());
            double score3 = calculateScoreByTypeAndRank(type, 3) * closingRatio;
            score.add(String.valueOf(score3));
        }
        
        // 成员3（排名第四）
        if (sciHorizontalApplyVertical.getFourthPersonId() != null && !sciHorizontalApplyVertical.getFourthPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFourthPersonId());
            double score4 = calculateScoreByTypeAndRank(type, 4) * closingRatio;
            score.add(String.valueOf(score4));
        }
        
        int result = sciHorizontalApplyVerticalService.overPass(id, getUserId(), urlFlag, score, persion, verticalId, sciHorizontalApplyVertical1.getSubjectSource());
        return toAjax(result);
    }

    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/overBh")
    @ResponseBody
    public AjaxResult overBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.overBh(id,getUserId(),remark,urlFlag));
    }

    /** 查看 */
    @RequiresPermissions("system:apply_vertical:info")
    @Log(title = "跳转到纵向课题查看页面", businessType = BusinessType.OTHER)
    @GetMapping("/overView/{id}/{urlFlag}")
    public String overView(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag,ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        if (urlFlag.equals("ZX")){
            sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        }
        return prefix + "/overView";
    }



    @Log(title = "查询纵向课题批阅意见", businessType = BusinessType.OTHER)
    @PostMapping("/abhyy/{kid}")
    @ResponseBody
    public TableDataInfo abhyy(@PathVariable("kid")Integer kid)
    {
        SciHorizontalPiyue ob = new SciHorizontalPiyue();
        ob.setVerticalId(kid);
        List<SciHorizontalPiyue> list = piyueService.selectVerticalPiyueList(ob);
        return getDataTable(list);
    }

    /**
     * 撤回操作
     */
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "跳转到纵向课题撤回页面", businessType = BusinessType.OTHER)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        // 查询 sci_persion_vertical 全部成员，供页面预渲染第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyVerticalService.selectPersionIdsByVerticalId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/recall";
    }
    /**
     *  撤回操作
     */
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "撤回操作", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.recall(id,state,getUserId(),remark,urlFlag));
    }
}
