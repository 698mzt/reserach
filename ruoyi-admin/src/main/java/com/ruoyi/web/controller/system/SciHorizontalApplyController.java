package com.ruoyi.web.controller.system;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciHorizontalApplyMapper;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 横向课题Controller
 *
 * @author zhansan
 * @date 2024-08-16
 */
@Controller
@RequestMapping("/system/apply")
public class SciHorizontalApplyController extends BaseController
{
    private String prefix = "system/apply";

    @Autowired
    private ISciHorizontalApplyService sciHorizontalApplyService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISciHorizontalPiyueService piyueService;
    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;
    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;
    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;

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

    @RequiresPermissions("system:apply:view")
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    /**
     * 查询横向课题列表
     */

    @RequiresPermissions("system:apply:list")
    @PostMapping("/list/{tableId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("tableId") String tableId,String year, SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApply.setYear(year);
        sciHorizontalApply.setUid(getUserId());
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
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
        sciHorizontalApply.setRole(role);
        sciHorizontalApply.setTableId(tableId);
        List<SciHorizontalApply> list = new ArrayList<>();
        List<SciHorizontalApply> Alist = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVERKYC(sciHorizontalApply);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApplyKYC(sciHorizontalApply);
                        break;
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectAmountListKYC(sciHorizontalApply);
                        break;
                }
                break;
//        教研室
            case "research":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByJYS(sciHorizontalApply);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApplyJYS(sciHorizontalApply);
                        break;
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectAmountListJYS(sciHorizontalApply);
                        break;
                }
                break;
//      学院负责人
            case "dept_teacher":
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByDept(sciHorizontalApply);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverDept(sciHorizontalApply);
                        break;
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectAmountListDept(sciHorizontalApply);
                        break;
                }
                break;
//        教师
            default:
                switch (tableId) {
                    case "bootstrap-table0":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
                        break;
                    case "bootstrap-table1":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyList(sciHorizontalApply);
                        break;
                    case "bootstrap-table2":
                        list = sciHorizontalApplyService.selectSciHorizontalApplyListByOverApply(sciHorizontalApply);
                        break;
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectAmountList(sciHorizontalApply);
                        break;
                }
                break;
        }

        List<SciHorizontalApply> list1 = sciHorizontalApplyService.selectOtherListByUid(sciHorizontalApply);
        list.addAll(list1);
        SysUser  sysUser =getSysUser();

        // 去重操作
        List<SciHorizontalApply> distinctList = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                SciHorizontalApply::getTopName, // 使用 topName 作为键
                                Function.identity(), // 值为原对象
                                (existing, replacement) -> existing, // 如果有重复，保留第一个出现的对象
                                LinkedHashMap::new // 保持插入顺序
                        ),
                        map -> new ArrayList<>(map.values()) // 将 Map 的值转换为 List
                ));

        Integer did = sysUser.getDeptId().intValue();
        Integer yid = sysUser.getParentId().intValue();

        List<SciHorizontalApply> Alist1 = new ArrayList<>();
        if (roles.size() > 1) {
            for (SysRole r : roles) {
                if ("teacher".equals(r.getRoleKey())) {
                    Alist1 = sciHorizontalReamountService.selectAmountList(sciHorizontalApply);
                    break;
                }
            }
        }
        Alist1.addAll(Alist);
        List<SciHorizontalApply> AdistinctList = Alist1.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                SciHorizontalApply::getReid, // 使用 reid 作为键
                                Function.identity(), // 值为原对象
                                (existing, replacement) -> existing, // 如果有重复，保留第一个出现的对象
                                LinkedHashMap::new // 保持插入顺序
                        ),
                        map -> new ArrayList<>(map.values()) // 将 Map 的值转换为 List
                ));

        if (tableId.equals("bootstrap-table3"))
            distinctList.addAll(AdistinctList);

//        计算到账金额
        List<creditedAmount> creditedAmount = sciHorizontalApplyService.selectCreditedAmount();
        for (SciHorizontalApply apply : distinctList) {
            for (creditedAmount amount: creditedAmount){
                if (Objects.equals(apply.getId(), amount.getApplyId())){
                    apply.setCreditedAmount(amount.getCreditedAmount());
                }
            }
        }


//        计算分
        if(!distinctList.isEmpty()){
            // 批量获取所有相关的积分记录
            Set<Integer> applyIds = distinctList.stream()
                    .map(SciHorizontalApply::getId)
                    .collect(Collectors.toSet());
            
            // 从数据库批量查询积分记录
            List<SciUserScore> allScores = sciUserScoreMapper.selectScoreHistoryByApplyIds(applyIds);
            
            // 构建按applyId分组的积分映射，过滤掉applyId为null的记录
            Map<String, List<SciUserScore>> scoreMap = allScores.stream()
                    .filter(score -> score.getApplyId() != null) // 过滤掉applyId为null的记录
                    .collect(Collectors.groupingBy(SciUserScore::getApplyId, 
                            Collectors.toList()));
            
            String currentUserId = getUserId().toString();
            
            for (SciHorizontalApply apply: distinctList){
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
        Integer total = distinctList.size();

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
     * 导出横向课题列表
     */
    @RequiresPermissions("system:apply:export")
    @Log(title = "导出横向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApply sciHorizontalApply)
    {
        // 获取当前用户的所有角色
        List<SysRole> roles = getSysUser().getRoles();


        // 遍历 roles 列表并提取每个 SysRole 的 roleKey
        for (SysRole role : roles) {
            if (!role.getRoleKey().equals("teacher")){
                if (role.getRoleKey().equals("dept_teacher"))
                    sciHorizontalApply.setYnameId(getSysUser().getParentId().toString());
                else if (role.getRoleKey().equals("research")) {
                    sciHorizontalApply.setDnameId(Integer.valueOf(getSysUser().getDeptId().toString()));
                }
            }
        }

        List<SciHorizontalApply> list = sciHorizontalApplyService.exportSciHorizontalApplyList(sciHorizontalApply);
        List<SciHorizontalApply> newList = new ArrayList<>();
        for (SciHorizontalApply apply: list ) {
                if(apply.getState().equals("6")){
                    apply.setState("结项");
                }else{
                    apply.setState("在研");
                }
                newList.add(apply);
        }
        ExcelUtil<SciHorizontalApply> util = new ExcelUtil<SciHorizontalApply>(SciHorizontalApply.class);
        return util.exportExcel(newList, "横向课题数据");
    }

    /**
     * 新增横向课题草稿箱
     */
    @RequiresPermissions("system:apply:add")
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
     * 新增保存横向课题草稿箱
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(SciHorizontalApply sciHorizontalApply, SciHorizontalReamount sciHorizontalReamount, javax.servlet.http.HttpServletRequest request)
    {
        Integer id = sciHorizontalApplyService.insertSciHorizontalApply(sciHorizontalApply);
        if (id == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }

        // 接收成员数组（主持人为 firstPersonId，其余从 name=members 收集）
        String first = sciHorizontalApply.getFirstPersonId();
        String second = request.getParameter("secondPersonId");
        String third = request.getParameter("thirdPersonId");
        String fourth = request.getParameter("fourthPersonId");
        String[] members = request.getParameterValues("members"); // 第5位及以后，可能为 null

//        首先创建一个空列表all，然后依次检查并添加第一到第四成员的ID（非空且不为空字符串），接着遍历第五及以后的成员数组members，
//        将其中非空且不为空字符串的成员ID也加入列表。最终得到一个包含所有有效成员ID的列表，供后续保存使用
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


        // 将成员按顺序写入 sci_persion（ranking 从1开始）
        // 复用已有 service 能力：sciHorizontalApplyService.insertPersionBatch(applyId, personIds)
        // 若无批量方法，可在 service 内部循环插入
        try {
            sciHorizontalApplyService.saveApplyPersons(id, all); // 需要在 service 层实现该方法
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }

        sciHorizontalReamount.setApplyId(id.toString());
        sciHorizontalReamount.setState("99");
        sciHorizontalReamount.setUid(getUserId());
        if (sciHorizontalReamount.getReAmount() != null && !sciHorizontalReamount.getReAmount().isEmpty()){
            sciHorizontalReamountService.insertAmount(sciHorizontalReamount);
        }
        return toAjax(id);
    }

    /**
     * 提交申请进行审批
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/push/{id}")
    @ResponseBody
    @Transactional
    public AjaxResult push(SciHorizontalApply sciHorizontalApply,SciHorizontalReamount sciHorizontalReamount)
    {
        SciHorizontalApply sciHorizontalApply1 = sciHorizontalApplyService.selectSciHorizontalApplyById(sciHorizontalApply.getId());
        sciHorizontalApply.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        if ( sciHorizontalApply1.getValidityDate() !=  null && !sciHorizontalApply1.getValidityDate().isEmpty()){
            sciHorizontalApply.setNewsql("999");
            sciHorizontalApply.setState("7");
        }
        else{
            sciHorizontalApply.setNewsql("99");
            sciHorizontalApply.setState("1");
        }
        int a = sciHorizontalApplyService.updateSciHorizontalApply(sciHorizontalApply);
        sciHorizontalReamount.setState("1");
        sciHorizontalReamountService.push(getUserId(),sciHorizontalApply.getId(),"1");
        return toAjax(a);
    }

    /**
     * 结项横向课题
     */
    @RequiresPermissions("system:apply:add")
    @GetMapping("/overadd")
    public String overadd( Integer id,ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList.add(other);

        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询所有成员（按 ranking 升序），用于展示第5位及以后
        List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overadd";
    }

    /**
     * 保存结项横向课题
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请结项横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/overadd")
    @ResponseBody
    public AjaxResult overaddSave(SciHorizontalApply sciHorizontalApply,SciHorizontalReamount sciHorizontalReamount)
    {
        Integer id = sciHorizontalApply.getId();
        sciHorizontalReamount.setApplyId(id.toString());
        sciHorizontalReamount.setState("1");
        sciHorizontalApply.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        int result = sciHorizontalApplyService.overSaveSciHorizontalApply(sciHorizontalApply);
        if (result == -1) {
            return AjaxResult.error("请选择有效的结项日期");
        } else if (result == -2) {
            return AjaxResult.error("解析错误，尝试输入正确的数据！！！若输入正确数据后仍然报错，请联系管理员处理。");
        }
        if (sciHorizontalReamount.getReAmount() != null && !sciHorizontalReamount.getReAmount().isEmpty()) {
            sciHorizontalReamountService.insertAmount(sciHorizontalReamount);
        }
        return toAjax(result);
    }

//detail 审批
    @RequiresPermissions("system:apply:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        //        创建一个"其他"
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 传递当前登录用户ID，用于权限判断
        mmap.put("currentUserId", getUserId());
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/detail";
    }

    @RequiresPermissions("system:apply:info")
    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 传递当前登录用户ID，用于权限判断
        mmap.put("currentUserId", getUserId());
        // 查询全部成员并注入第5位及以后
        List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overdetail";
    }

    /**已结项查看 */
    @RequiresPermissions("system:apply:info")
    @GetMapping("/overView/{id}/{urlFlag}")
    public String overView(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overView";
    }


    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxPass(id,getUserId(),urlFlag));
    }
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "结项横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxover")
    @ResponseBody
    public AjaxResult hxover(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxover(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "横向课题被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxBh(id,getUserId(),remark,urlFlag));
    }
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "结项横向课题被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxoverBh")
    @ResponseBody
    public AjaxResult hxoverBh(String id,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.hxoverBh(id,getUserId(),remark,urlFlag));
    }

    /**
     * 修改横向课题
     */
    @RequiresPermissions("system:apply:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询 sci_persion 的全部成员，供编辑页预渲染第5位及以后
        List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/edit";
    }
    /**
     * 修改保存横向课题
     */
    @RequiresPermissions("system:apply:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(SciHorizontalApply sciHorizontalApply, SciHorizontalReamount sciHorizontalReamount, javax.servlet.http.HttpServletRequest request)
    {
        sciHorizontalApply.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        sciHorizontalApply.setNewsql("");
        if (sciHorizontalApply.getState().equals("3") || sciHorizontalApply.getState().equals("5") || sciHorizontalApply.getState().equals("22")){
            sciHorizontalApply.setNewsql("");
            sciHorizontalApply.setState("99");
        }
        if (sciHorizontalReamount.getReAmount() != null && !sciHorizontalReamount.getReAmount().isEmpty()) {
            sciHorizontalReamount.setState("99");
            sciHorizontalReamountService.insertAmount(sciHorizontalReamount);
        }
        int update = sciHorizontalApplyService.updateSciHorizontalApply(sciHorizontalApply);
        if (update == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }

        // 合并编辑页的成员（前四位 + 第5位起的动态 members[]），顺序不乱
        String first = request.getParameter("firstPersonId");
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
            sciHorizontalApplyService.saveApplyPersons(sciHorizontalApply.getId(), all);
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }

        return toAjax(update);
    }

    @RequiresPermissions("system:apply:edit")
    @GetMapping("/overedit/{id}")
    public String overedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询 sci_persion 全部成员，供页面预渲染第5位及以后
        List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/overedit";
    }

    @RequiresPermissions("system:apply:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/overedit")
    @ResponseBody
    @Transactional
    public AjaxResult overeditSave(SciHorizontalApply sciHorizontalApply, javax.servlet.http.HttpServletRequest request)
    {
        sciHorizontalApply.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        sciHorizontalApply.setNewsql("");
        if (sciHorizontalApply.getState().equals("9") || sciHorizontalApply.getState().equals("10") || sciHorizontalApply.getState().equals("44")){
            sciHorizontalApply.setNewsql("999");
            sciHorizontalApply.setState("999");
        }
        int update = sciHorizontalApplyService.updateSciHorizontalApply(sciHorizontalApply);
        if (update == -1) {
            return AjaxResult.error("课题名称或课题编号已存在");
        }

        // 合并成员（前四位 + 第5位起的动态 members[]），顺序不乱
        String first = String.valueOf(getUserId());
        String second = request.getParameter("secondPersonId");
        String third = request.getParameter("thirdPersonId");
        String fourth = request.getParameter("fourthPersonId");
        String[] members = request.getParameterValues("members");

        java.util.List<String> all = new java.util.ArrayList<>();
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
            sciHorizontalApplyService.saveApplyPersons(sciHorizontalApply.getId(), all);
        } catch (Exception e) {
            return AjaxResult.error("成员信息保存失败");
        }

        return toAjax(update);
    }

    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciHorizontalPiyue ob = new SciHorizontalPiyue();
        ob.setHxktId(kid);
        List<SciHorizontalPiyue> list = piyueService.selectSciHorizontalPiyueList(ob);
        return getDataTable(list);
    }
    @RequiresPermissions("system:apply:edit")
    @PostMapping("/abhyy/{kid}")
    @ResponseBody
    public TableDataInfo abhyy(@PathVariable("kid")Integer kid)
    {
        SciHorizontalPiyue ob = new SciHorizontalPiyue();
        ob.setHxktId(kid);
        List<SciHorizontalPiyue> list = piyueService.selectSciHorizontalAmountPiyueList(ob);
        return getDataTable(list);
    }

    /**
     * 删除横向课题
     */
    @RequiresPermissions("system:apply:remove")
    @Log(title = "删除横向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        sciHorizontalApplyService.deletePersionByid(ids);
        return toAjax(sciHorizontalApplyService.deleteSciHorizontalApplyByIds(ids));
    }

    /**
     * 删除审批 横向课题操作
     */
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/recall";
    }
    /**
     * 删除审批 横向课题操作
     */
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyService.recall(id,state,getUserId(),remark,urlFlag));
    }


    /**
     * 添加到账金额
     */
    @RequiresPermissions("system:apply:edit")
    @GetMapping("/Reamount/{id}")
    public String Reamount(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        String allAmount = sciHorizontalApplyService.selectSciHorizontalApplyById(id).getAmount();
        sciHorizontalApply.setAmount(allAmount);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(id);
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/amountModel";
    }
//    @RequiresPermissions("system:apply:edit")
    @Log(title = "添加到账金额", businessType = BusinessType.INSERT)
    @PostMapping("/Reamount")
    @ResponseBody
    public AjaxResult Reamount(SciHorizontalReamount sciHorizontalReamount)
    {
        sciHorizontalReamount.setUid(getUserId());
        int state = sciHorizontalReamountService.insertAmount(sciHorizontalReamount);
        if (state == -1) {
            return  AjaxResult.error("课题总金额必须大于0");
        }
        if (state == -2) {
            return  AjaxResult.error("追加金额必须大于0");
        }
        return toAjax(state);
    }

    /**
     * 审批到账金额
     */
    @RequiresPermissions("system:apply:info")
    @GetMapping("/reamountdetail/{id}/{urlFlag}")
    public String reamountdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalReamountService.selectAmountById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApply.setUrlFlag(urlFlag);
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 传递当前登录用户ID，用于权限判断
        mmap.put("currentUserId", getUserId());
        // 查询全部成员并注入第5位及以后
        List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(sciHorizontalApply.getId());
        List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/reamountdetail";
    }
    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/amountPass")
    @ResponseBody
    public AjaxResult amountPass(String id,String reid,String urlFlag,String amount,Double scale,String amountType,SciProjectScoreCfg sciProjectScoreCfg)
    {
//        初始化一个新对象，存储最大值和最小值
        SciProjectScoreCfg sciProjectScoreCfg1 = new SciProjectScoreCfg();
//        查询积分的所有范围
        List<SciProjectScoreCfg> list= sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg);
        Integer applyId;
        Double Damount;
        try {
            Damount = Double.valueOf(amount);
        } catch (NumberFormatException e) {
            return AjaxResult.error("金额无效");
        }
//        查询项目金额在积分的哪个范围内，并将范围记录到sciProjectScoreCfg1中
        for (SciProjectScoreCfg scoreCfg : list) {
            if (Damount >= Double.valueOf(scoreCfg.getFundsMin()) && Damount <= Double.valueOf(scoreCfg.getFundsMax())) {
                sciProjectScoreCfg1.setFundsMin(scoreCfg.getFundsMin());
                sciProjectScoreCfg1.setFundsMax(scoreCfg.getFundsMax());
                break;
            }
        }
//       查询范围为 min-max 的分数
        List<SciProjectScoreCfg> score_list = sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg1);
//        查询该条数据的负责人id
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(Integer.valueOf(id));
        applyId = sciHorizontalApply.getId();
//        将负责人和积分顺序存储到列表中传到实现类中
        List score = new ArrayList();
        List persion = new ArrayList();
        for (SciProjectScoreCfg scoreCfg : score_list) {
            if (amountType.equals("1")){
                Double  a = Double.valueOf(scoreCfg.getStartScore());
                Double b = a * scale;
                Long c = Math.round(b);
                score.add(c.toString());
            }else if (amountType.equals("2")){
                score.add(scoreCfg.getEndScore());
            }
        }
        if (sciHorizontalApply.getFirstPersonId() != null && !sciHorizontalApply.getFirstPersonId().isEmpty()) {
            persion.add(sciHorizontalApply.getFirstPersonId());
        }
        if (sciHorizontalApply.getSecondPersonId() != null && !sciHorizontalApply.getSecondPersonId().isEmpty()) {
            persion.add(sciHorizontalApply.getSecondPersonId());
        }
        if (sciHorizontalApply.getThirdPersonId() != null && !sciHorizontalApply.getThirdPersonId().isEmpty()) {
            persion.add(sciHorizontalApply.getThirdPersonId());
        }
        if (sciHorizontalApply.getFourthPersonId() != null && !sciHorizontalApply.getFourthPersonId().isEmpty()) {
            persion.add(sciHorizontalApply.getFourthPersonId());
        }

        return toAjax(sciHorizontalApplyService.amountPass(id,reid,getUserId(),urlFlag,score,persion,applyId,amountType));
    }

    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "金额被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/amountBh")
    @ResponseBody
    public AjaxResult amountBh(String id,String reid,String remark,String urlFlag)
    {

        return toAjax(sciHorizontalApplyService.removeAmount(id,reid,getUserId(),remark,urlFlag));
    }

    /**
     * 修改追加金额
     */
    @RequiresPermissions("system:apply:edit")
    @GetMapping("/reamountedit/{id}")
    public String reamountedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApply sciHorizontalApply = sciHorizontalReamountService.selectAmountById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        //        创建一个“其他”
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        //        将"其他"追加到userList中
        userList1.add(other);

        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApply", sciHorizontalApply);
        // 查询全部成员并注入第5位及以后
        java.util.List<String> allMemberIds = sciHorizontalApplyService.selectPersionIdsByApplyId(sciHorizontalApply.getId());
        java.util.List<String> extraMembers = new java.util.ArrayList<>();
        if (allMemberIds != null && allMemberIds.size() > 4) {
            extraMembers = allMemberIds.subList(4, allMemberIds.size());
        }
        mmap.put("extraMembers", extraMembers);
        return prefix + "/reamountedit";
    }
    @RequiresPermissions("system:apply:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/reamountedit")
    @ResponseBody
    public AjaxResult reamounteditSave(SciHorizontalReamount sciHorizontalReamount)
    {
        sciHorizontalReamount.setState("1");
        return toAjax(sciHorizontalReamountService.amountedit(sciHorizontalReamount));
    }

    /**
     * 删除金额
     */
    @RequiresPermissions("system:apply:remove")
    @Log(title = "删除横向课题", businessType = BusinessType.DELETE)
    @PostMapping( "/reamountremove/{id}")
    @ResponseBody
    public AjaxResult reamountremove(@PathVariable("id") Integer id)
    {
        return toAjax(sciHorizontalReamountService.reamountremove(id));
    }

    @RequiresPermissions("system:apply:view")
    @GetMapping("/todoCount")
    @ResponseBody
    public AjaxResult getTodoCount() {
        SysUser currentUser = getSysUser();
        Map<String, Integer> counts = sciHorizontalApplyService.getTodoCount(currentUser);

        // 添加纵向课题统计 - 使用正确的统计方法
        counts.put("vertical_apply", sciHorizontalApplyService.countVerticalApply(currentUser));
        counts.put("vertical_audit", sciHorizontalApplyService.countVerticalAudit(currentUser));
        counts.put("vertical_complete", sciHorizontalApplyService.countVerticalComplete(currentUser));

        // 添加横向课题统计
        counts.put("horizontal_apply", sciHorizontalApplyService.countHorizontalApply(currentUser));
        counts.put("horizontal_audit", sciHorizontalApplyService.countHorizontalAudit(currentUser));
        counts.put("horizontal_complete", sciHorizontalApplyService.countHorizontalComplete(currentUser));

        // 添加成果转化统计
        counts.put("achievement_apply", sciHorizontalApplyService.countAchievementApply(currentUser));
        counts.put("achievement_audit", sciHorizontalApplyService.countAchievementAudit(currentUser));
        counts.put("achievement_complete", sciHorizontalApplyService.countAchievementComplete(currentUser));

        // 添加论文统计
        counts.put("paper_apply", sciHorizontalApplyService.countPaperApply(currentUser));
        counts.put("paper_audit", sciHorizontalApplyService.countPaperAudit(currentUser));
        counts.put("paper_complete", sciHorizontalApplyService.countPaperComplete(currentUser));

        // 添加教材软著统计
        counts.put("textbook_apply", sciHorizontalApplyService.countTextbookApply(currentUser));
        counts.put("textbook_audit", sciHorizontalApplyService.countTextbookAudit(currentUser));
        counts.put("textbook_complete", sciHorizontalApplyService.countTextbookComplete(currentUser));

        // 添加专利软著统计
        counts.put("patent_apply", sciHorizontalApplyService.countPatentApply(currentUser));
        counts.put("patent_audit", sciHorizontalApplyService.countPatentAudit(currentUser));
        counts.put("patent_complete", sciHorizontalApplyService.countPatentComplete(currentUser));

        // 添加奖励统计
        counts.put("reward_apply", sciHorizontalApplyService.countRewardApply(currentUser));
        counts.put("reward_audit", sciHorizontalApplyService.countRewardAudit(currentUser));
        counts.put("reward_complete", sciHorizontalApplyService.countRewardComplete(currentUser));

        // 添加讲座报告统计
        counts.put("lecture_apply", sciHorizontalApplyService.countLectureApply(currentUser));
        counts.put("lecture_audit", sciHorizontalApplyService.countLectureAudit(currentUser));
        counts.put("lecture_complete", sciHorizontalApplyService.countLectureComplete(currentUser));

        return AjaxResult.success(counts);
    }
}
