package com.ruoyi.web.controller.IntraSchoolProject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;

//http://localhost:8081/IntraSchPro
@Controller
@RequestMapping("/IntraSchPro")
public class SciIntraSchoolProController extends BaseController {
  private String prefix = "system/IntraSchPro";
  private static final String TEC_TRA_PROCESS_CODE = "TEC_TRA_APPLY";
  private static final String TEC_TRA_DRAFT = "TEC_TRA_DRAFT";
  private static final String TEC_TRA_JYS_AUDIT = "TEC_TRA_JYS_AUDIT";
  private static final String TEC_TRA_KYC_AUDIT = "TEC_TRA_KYC_AUDIT";
  private static final String TEC_TRA_PASSED = "TEC_TRA_PASSED";
  private static final String TEC_TRA_REJECTED = "TEC_TRA_REJECTED";
  //
  @Autowired
  private ISciIntraSchProApplyService sciIntraSchProApplyService;

  @Autowired
  private ISysUserService userService;
  @Autowired
  private ISciIntraSchProPiyueService piyueService;
  @Autowired
  private SciIntraSchProReamountService sciIntraSchProReamountService;

  @Autowired
  private SciIntraSchProScoreService sciIntraSchProScoreService;

  @Autowired
  private SciTecTraScoreCalculator sciTecTraScoreCalculator;

  @Autowired
  private ISysApprovalStateService sysApprovalStateService;
  //private String role_str="";
  /**
   * 成果转化列表页面入口，传入当前活跃角色供前端按钮显隐判断
   * 优先从 Shiro Session 读取切换后的 activeRoleId，回退到 panRole_str()
   */
  @GetMapping("")
  String view(ModelMap mmap) {
    mmap.put("roleStr", getActiveRoleKey());
    return prefix + "/view";
  }

  /**
   * 获取当前登录用户的基本信息（用户名、用户ID）
   */
  @PostMapping("/getLoginData")
  @ResponseBody
  public Map get_LoginName() {
    Map<String, Object> userData = new HashMap();
    userData.put("userName", userService.selectUserById(getUserId()).getUserName());
    userData.put("userId", userService.selectUserById(getUserId()).getUserId());
    //System.out.println("userService.selectUserById(getUserId())="+userService.selectUserById(getUserId()).getUserName() );

    return userData;
  }

  /**
   * 查询校内横向课题列表
   */
  @PostMapping("/list/{tableId}")
  @ResponseBody
  public TableDataInfo list(@PathVariable("tableId") String tableId, String year, SciIntraSchoolPro sciIntraSchoolPro) {

    sciIntraSchoolPro.setYear(year);
    //System.out.println("year = " + year);
    sciIntraSchoolPro.setUid(getUserId());
    System.out.println("this userid is=" + getUserId());

    /*1：超级管理员，2：普通角色，100：普通教师，101：科研处，102：教研室管理员，103：学院负责人*/

    startPage();
    //判断当前用户的角色
    String role_str = panRole_str();
    System.out.println("role_str = " + role_str);
    List<SciIntraSchoolPro> list = selectListByRole(role_str, tableId, sciIntraSchoolPro);
    populateScores(list);
    populateApprovalStages(list);
    TableDataInfo data = getDataTable(list);

    //System.out.println("data = " + data);
    return data;
  }

  /**
   * 查询全部成果转化列表（不分tableId），合并立项、审批、结题三个tab的数据并去重排序分页
   */
  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo listAll(String year, SciIntraSchoolPro sciIntraSchoolPro) {
    sciIntraSchoolPro.setYear(year);
    sciIntraSchoolPro.setUid(getUserId());

    String roleStr = panRole_str();
    List<SciIntraSchoolPro> mergedList = new ArrayList<>();
    mergedList.addAll(selectListByRole(roleStr, "bootstrap-table0", sciIntraSchoolPro));
    mergedList.addAll(selectListByRole(roleStr, "bootstrap-table1", sciIntraSchoolPro));
    mergedList.addAll(selectListByRole(roleStr, "bootstrap-table2", sciIntraSchoolPro));

    Map<Integer, SciIntraSchoolPro> distinctMap = new LinkedHashMap<>();
    for (SciIntraSchoolPro item : mergedList) {
      if (item != null && item.getId() != null && !distinctMap.containsKey(item.getId())) {
        distinctMap.put(item.getId(), item);
      }
    }

    List<SciIntraSchoolPro> distinctList = new ArrayList<>(distinctMap.values());
    distinctList.sort((a, b) -> {
        int p = Integer.compare(getStatePriority(a.getState()), getStatePriority(b.getState()));
        if (p != 0) return p;
        String ta = a.getCreatetime(), tb = b.getCreatetime();
        if (ta != null && tb != null) return tb.compareTo(ta);
        return Integer.compare(a.getId() != null ? a.getId() : 0, b.getId() != null ? b.getId() : 0);
    });

    PageDomain pageDomain = TableSupport.buildPageRequest();
    Integer pageNum = pageDomain.getPageNum();
    Integer pageSize = pageDomain.getPageSize();
    Integer total = distinctList.size();

    int fromIndex = (pageNum - 1) * pageSize;
    int toIndex = Math.min(pageNum * pageSize, total);

    if (fromIndex > total) {
      distinctList = new ArrayList<>();
    } else {
      distinctList = distinctList.subList(fromIndex, toIndex);
    }

    TableDataInfo data = getDataTable(distinctList);
    data.setTotal(total);
    populateScores(distinctList);
    populateApprovalStages(distinctList);
    return data;
  }

  /**
   * 列表页显示当前金额下四位负责人的应得总分，
   * 前端再根据当前登录人的负责人顺位取对应分值展示。
   */
  private void populateScores(List<SciIntraSchoolPro> list) {
    for (SciIntraSchoolPro item : list) {
      try {
        String amountStr = item.getAmount();
        if (amountStr == null || amountStr.isEmpty()) {
          continue;
        }
        amountStr = amountStr.replaceAll("[^0-9.]", "");
        if (amountStr.isEmpty()) {
          continue;
        }
        Map<Integer, SciTecTraScoreCalculator.ScoreDetail> scoreDetails =
                sciTecTraScoreCalculator.calculateScoreDetails(new BigDecimal(amountStr));
        for (Map.Entry<Integer, SciTecTraScoreCalculator.ScoreDetail> entry : scoreDetails.entrySet()) {
          String score = String.valueOf(entry.getValue().getTotalScore());
          switch (entry.getKey()) {
            case 1:
              item.setFirstPoints(score);
              break;
            case 2:
              item.setSecondPoints(score);
              break;
            case 3:
              item.setThirdPoints(score);
              break;
            case 4:
              item.setForthPoints(score);
              break;
            default:
              break;
          }
        }
      } catch (Exception e) {
        System.out.println("populateScores error: " + e.getMessage());
      }
    }
  }

  /**
   * 为列表每条记录填充审批阶段名称（从审批流程配置表中查询状态码对应的显示名称）
   */
  private void populateApprovalStages(List<SciIntraSchoolPro> list) {
    if (list == null || list.isEmpty()) {
      return;
    }

    Map<String, String> stateNameMap = sysApprovalStateService.selectSysApprovalStateByProcessCode(TEC_TRA_PROCESS_CODE)
            .stream()
            .filter(state -> state.getStateCode() != null && state.getStateName() != null)
            .collect(Collectors.toMap(SysApprovalState::getStateCode, SysApprovalState::getStateName, (oldValue, newValue) -> oldValue));

    for (SciIntraSchoolPro item : list) {
      String stateCode = mapTecTraStateToStatusCode(item.getState());
      item.setApprovalStage(stateNameMap.getOrDefault(stateCode, item.getStateDes()));
    }
  }




  /**
   * 将旧数字状态码映射为新的语义化状态编码（如 "15" -> "TEC_TRA_DRAFT"）
   */
  private String mapTecTraStateToStatusCode(String state) {
    if (state == null || state.trim().isEmpty()) {
      return TEC_TRA_DRAFT;
    }
    switch (state) {
      case "15":
        return TEC_TRA_DRAFT;
      case "1":
      case "11":
        return TEC_TRA_JYS_AUDIT;
      case "2":
        return TEC_TRA_KYC_AUDIT;
      case "4":
      case "6":
        return TEC_TRA_PASSED;
      case "3":
      case "5":
      case "12":
        return TEC_TRA_REJECTED;
      default:
        return state;
    }
  }

  /**
   * 获取状态的排序优先级，数值越小越靠前（草稿1 < 驳回2 < JYS审批3 < KYC审批4 < 通过5）
   */
  private int getStatePriority(String state) {
    if (state == null) return 6;
    switch (state) {
      case "15": case "16": case "TEC_TRA_DRAFT":
        return 1;
      case "3": case "5": case "9": case "10": case "12": case "14": case "TEC_TRA_REJECTED":
        return 2;
      case "1": case "7": case "8": case "11": case "13": case "TEC_TRA_JYS_AUDIT":
        return 3;
      case "2": case "TEC_TRA_KYC_AUDIT":
        return 4;
      case "4": case "6": case "TEC_TRA_PASSED":
        return 5;
      default:
        return 6;
    }
  }

  /**
   * 根据当前用户角色和tableId路由到对应的数据查询方法
   * 五个角色各有三张表（已完结/审批中/结题中），共15条路由分支
   */
  private List<SciIntraSchoolPro> selectListByRole(String roleStr, String tableId, SciIntraSchoolPro sciIntraSchoolPro) {
    switch (roleStr) {
      case "dept_teacher":
        switch (tableId) {
          case "bootstrap-table0":
            return sciIntraSchProApplyService.sel_IntraSchPro_isOVER_dept_teacher(sciIntraSchoolPro);
          case "bootstrap-table1":
            return sciIntraSchProApplyService.sel_IntraSchPro_approval_dept_teacher(sciIntraSchoolPro);
          case "bootstrap-table2":
            return sciIntraSchProApplyService.sel_IntraSchPro_closure_dept_teacher(sciIntraSchoolPro);
          default:
            return new ArrayList<>();
        }
      case "sci_tesearch":
        switch (tableId) {
          case "bootstrap-table0":
            return sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
          case "bootstrap-table1":
            return sciIntraSchProApplyService.sel_IntraSchPro_approval_ky(sciIntraSchoolPro);
          case "bootstrap-table2":
            return sciIntraSchProApplyService.sel_IntraSchPro_closure_ky(sciIntraSchoolPro);
          default:
            return new ArrayList<>();
        }
      case "research":
        switch (tableId) {
          case "bootstrap-table0":
            List<SciIntraSchoolPro> overList = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
            overList.forEach(item -> item.setRole("research"));
            return overList;
          case "bootstrap-table1":
            return sciIntraSchProApplyService.sel_IntraSchPro_approval_jy(sciIntraSchoolPro);
          case "bootstrap-table2":
            return sciIntraSchProApplyService.sel_IntraSchPro_closure_jy(sciIntraSchoolPro);
          default:
            return new ArrayList<>();
        }
      case "admin":
        switch (tableId) {
          case "bootstrap-table0":
            return sciIntraSchProApplyService.sel_IntraSchPro_isOVER_admin(sciIntraSchoolPro);
          case "bootstrap-table1":
            return sciIntraSchProApplyService.sel_IntraSchPro_approval_admin(sciIntraSchoolPro);
          case "bootstrap-table2":
            return sciIntraSchProApplyService.sel_IntraSchPro_closure_admin(sciIntraSchoolPro);
          default:
            return new ArrayList<>();
        }
      case "teacher":
        switch (tableId) {
          case "bootstrap-table0":
            return sciIntraSchProApplyService.sel_my_IntraSchPro_isOVER(sciIntraSchoolPro);
          case "bootstrap-table1":
            return sciIntraSchProApplyService.sel_IntraSchPro_approval_my(sciIntraSchoolPro);
          case "bootstrap-table2":
            return sciIntraSchProApplyService.sel_IntraSchPro_closure_my(sciIntraSchoolPro);
          default:
            return new ArrayList<>();
        }
      default:
        return new ArrayList<>();
    }
  }

  /**
   * 判断当前登陆用户的身份
   * dept_teacher，sci_tesearch，research，admin，teacher
   *
   * @return
   */
  private String panRole_str() {
    String role_str = "";
    List<Long> collage_role_ids = new ArrayList<>(Arrays.asList(103L, 104L, 105L, 106L, 107L, 108L, 116L, 117L, 118L, 119L,120L));

    //当前登陆角色列表，如果一个人有多个角色，列表就多一项
    List<SysRole> roles = getSysUser().getRoles();
    //代表六个身份
    List<Integer> roles_list = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

    for (SysRole r : roles) {
      System.out.println("r.getRoleKey()=" + r.getRoleKey());
      //普通教师
      if (r.getRoleId() == 100) {
        roles_list.set(2, 1);
      }
      //科研处
      if (r.getRoleId() == 101) {
        roles_list.set(3, 1);
      }
      //教研室
      if (r.getRoleId() == 102) {
        roles_list.set(4, 1);
      }
      //学院负责人
      if (collage_role_ids.contains(r.getRoleId())) {
        roles_list.set(5, 1);
      }
      //超级管理员
      if (r.getRoleId() == 1) {
        roles_list.set(1, 1);
      }
    }
    //学院
    if (roles_list.get(5) == 1) {
      role_str = "dept_teacher";
    }
//        科研处
    else if (roles_list.get(3) == 1) {
      role_str = "sci_tesearch";

    }
//        教研室
    else if (roles_list.get(4) == 1) {
      System.out.println("this is 教研");
      role_str = "research";

    }
    //admin
    else if (roles_list.get(1) == 1) {
      role_str = "admin";

    }

    String rolesString = roles_list.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(""));
    //只是普通教师
    if (rolesString.equals("001000")) {
      role_str = "teacher";
    }
    return role_str;


  }

  /**
   * 获取当前会话的活跃角色标识（roleKey）
   * 优先从 Shiro Session 读取切换后的 activeRoleId，
   * 若未取到则回退到 panRole_str()（基于角色优先级）
   * 确保前端按钮显隐反映用户当前切换到的角色
   */
  private String getActiveRoleKey() {
    try {
      Subject subject = SecurityUtils.getSubject();
      if (subject != null) {
        Session session = subject.getSession(false);
        if (session != null) {
          Long activeRoleId = (Long) session.getAttribute("activeRoleId");
          if (activeRoleId != null) {
            for (SysRole role : getSysUser().getRoles()) {
              if (activeRoleId.equals(role.getRoleId())) {
                return role.getRoleKey();
              }
            }
          }
        }
      }
    } catch (Exception e) {
      // ignore
    }
    return panRole_str();
  }

  /**
   * 成果转化新增页面，加载用户列表供负责人下拉框选择
   */
  @GetMapping("/add")
  public String add(ModelMap mmap) {
    // SysUser user=getSysUser();
    //获取角色列表给添加页面的负责人下拉框
    List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
    for (int a = 0; a < userList.size(); a++) {
      if (userList.get(a).getUserId() == getUserId()) {
        SysUser user = userList.get(a);
        user.setFlag(true);
        userList.set(a, user);
        break;
      }
    }
    SysUser other = new SysUser();
    other.setUserId(-1L);
    other.setUserName("其他");
    userList.add(other);
    mmap.put("sysUsers", userList);
    return prefix + "/add";
  }

  /**
   * 新增保存成果转化
   */

  @Log(title = "申请成果转化", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(SciIntraSchoolPro sciIntraSchoolPro) throws IOException {

    Set<String> countAuthors = new HashSet<>();
    countAuthors.add(sciIntraSchoolPro.getFirstPersonId());
    countAuthors.add(sciIntraSchoolPro.getSecondPersonId ());
    countAuthors.add(sciIntraSchoolPro.getThirdPersonId());
    countAuthors.add(sciIntraSchoolPro.getFourthPersonId());
//      if (countAuthors.size() < 4){
//          return error("负责人不能少于四个");
//      }
    //插入这个课题
    sciIntraSchoolPro.setUid(getUserId());
    int id = sciIntraSchProApplyService.insert_SchPro_Apply(sciIntraSchoolPro);

    //插入这个课题的积分明细
    return toAjax(sciIntraSchProScoreService.set_SchPro_score_noScore(sciIntraSchoolPro));
  }

  /**
   * 成果转化编辑页面入口，根据课题状态路由到不同模板（立项编辑/结题前编辑/结题编辑）
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
    System.out.println("id = " + id);
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    System.out.println("sciIntraSchoolPro.getOverFiling() = " + sciIntraSchoolPro.getOverFiling());
    List<SysUser> userList1 = userService.selectAllUser();

    mmap.put("sysUsers1", userList1);
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);

    if (Arrays.asList("1", "2", "3", "4", "5", "11", "12", "15", TEC_TRA_DRAFT, TEC_TRA_JYS_AUDIT, TEC_TRA_KYC_AUDIT, TEC_TRA_REJECTED).contains(sciIntraSchoolPro.getState())) {
      System.out.println("1");
      return prefix + "/edit";
    } else if (sciIntraSchoolPro.getState().equals("6") || TEC_TRA_PASSED.equals(sciIntraSchoolPro.getState())) {
      System.out.println("2");
      return prefix + "/is_Over";
    } else {
      System.out.println("3");
      return prefix + "/edit_Over";
    }
    //return prefix + "/edit";
  }

  /**
   * 成果转化详情/批阅页面，设置部门匹配标志和角色信息供前端判定按钮显隐
   * 使用 getActiveRoleKey() 确保角色切换后前端按钮正确显隐
   */
  @GetMapping("/detail/{id}/{urlFlag}")
  public String detail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap) {
    //批阅的数据的user_id
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    //获取当前用户的部门名字
    String user_dname = sciIntraSchProApplyService.getuser_dnameById(getUserId());
    System.out.println("user_dname = " + user_dname);
    //使用活跃角色（从Session读取切换后的角色），确保角色切换后前端按钮正确显隐
    sciIntraSchoolPro.setRole(getActiveRoleKey());
    System.out.println("sciIntraSchoolPro = " + sciIntraSchoolPro);
    List<SysUser> userList1 = userService.selectAllUser();
    sciIntraSchoolPro.setUrlFlag(urlFlag);

    mmap.put("sysUsers1", userList1);
    //判断自己的部门是不是和这个项目的部门相同，如果是就设置为1，不是就设置为0

    String role_str = panRole_str();
    if (user_dname.equals(sciIntraSchoolPro.getDname()) || role_str.equals("sci_tesearch") || role_str.equals("admin")) {
      sciIntraSchoolPro.setDeptNamekey("1");
    } else {
      sciIntraSchoolPro.setDeptNamekey("0");
    }
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    //mmap.put("urlFlag",urlFlag);
    System.out.println("SciIntraSchoolProController.detail");
    return prefix + "/detail";
  }

  /**
   * 查看
   *
   * @param id
   * @param urlFlag
   * @param mmap
   * @return
   */
  @GetMapping("/overdetail/{id}/{urlFlag}")
  public String overdetail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap) {
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);

    //使用活跃角色（从Session读取切换后的角色），确保角色切换后前端按钮正确显隐
    sciIntraSchoolPro.setRole(getActiveRoleKey());
    System.out.println("sciIntraSchoolPro = " + sciIntraSchoolPro);
    List<SysUser> userList1 = userService.selectAllUser();
    sciIntraSchoolPro.setUrlFlag(urlFlag);
    mmap.put("sysUsers1", userList1);
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    //mmap.put("urlFlag",urlFlag);
    return prefix + "/overdetail";
  }

  /**
   * 根据项目金额计算各负责人积分
   */
  @PostMapping("/calculateScore")
  @ResponseBody
  public AjaxResult calculateScore(@RequestParam("amount") double amount) {
    Map<String, Object> result = new HashMap<>();
    Map<Integer, SciTecTraScoreCalculator.ScoreDetail> scoreDetails =
            sciTecTraScoreCalculator.calculateScoreDetails(BigDecimal.valueOf(amount));
    for (Map.Entry<Integer, SciTecTraScoreCalculator.ScoreDetail> entry : scoreDetails.entrySet()) {
      result.put(String.valueOf(entry.getKey()), entry.getValue().getTotalScore());
    }
    return AjaxResult.success(result);
  }

  /**
   * 驳回原因
   *
   * @param kid
   * @returnx`
   */
  @PostMapping("/bhyy/{kid}")
  @ResponseBody
  public TableDataInfo bhyy(@PathVariable("kid") Integer kid) {
    SciIntraSchProPiyue ob = new SciIntraSchProPiyue();
    ob.setSchxktId(kid);
    List<SciIntraSchProPiyue> list = new ArrayList<>(piyueService.selectIntraSchProPiyueList(ob));
    list.sort((left, right) -> {
      Date leftTime = left.getCreateTime();
      Date rightTime = right.getCreateTime();
      if (leftTime == null && rightTime == null) {
        return 0;
      }
      if (leftTime == null) {
        return 1;
      }
      if (rightTime == null) {
        return -1;
      }
      return rightTime.compareTo(leftTime);
    });
    for (SciIntraSchProPiyue item : list) {
      item.setState(mapPiyueState(item.getState()));
      item.setConcate(mapPiyueConcate(item.getState(), item.getConcate()));
    }
    return getDataTable(list);
  }

  /**
   * 将批阅记录的原始状态文本映射为简化的操作类型（新增/撤回/驳回/提交/通过/修改）
   */
  private String mapPiyueState(String raw) {
    if (raw == null) return "";
    if (raw.contains("草稿") || raw.contains("新建")) return "新增";
    if (raw.contains("撤回")) return "撤回";
    if (raw.contains("驳回")) return "驳回";
    if (raw.contains("提交") || raw.contains("申请")) return "提交";
    if (raw.contains("通过") || raw.contains("同意")) return "通过";
    if (raw.contains("修改")) return "修改";
    return raw;
  }

  /**
   * 获取批阅记录的备注内容，若原始备注为空则使用映射后的操作类型作为显示文本
   */
  private String mapPiyueConcate(String mappedState, String originalConcate) {
    if (originalConcate != null && !originalConcate.trim().isEmpty()) {
      return originalConcate;
    }
    return mappedState;
  }


  /**
   * 提交草稿到教研室审批
   * 参考论文模块实现，调用公有方法 approvalProcessService.submitApproval()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:edit", "system:intraSch:add"}, logical = Logical.OR)
  @Log(title = "成果转化提交草稿", businessType = BusinessType.UPDATE)
  @PostMapping("/subDraft/{id}")
  @ResponseBody
  public AjaxResult subDraft(@PathVariable("id") Integer id) {
    String sid = String.valueOf(id);
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    if (!canEditOrDeleteTecTra(sciIntraSchoolPro)) {
      return AjaxResult.error("当前账号无此操作权限");
    }
    String state = sciIntraSchoolPro.getState();
    if (state.equals("15") || TEC_TRA_DRAFT.equals(state) || TEC_TRA_REJECTED.equals(state)) {
      state = TEC_TRA_JYS_AUDIT;
    } else if (sciIntraSchoolPro.getState().equals("16")) {
      state = "7";
    }
    return toAjax(sciIntraSchProApplyService.subDraft(sid, getUserId(), state));
  }

  /**
   * 立项审批驳回
   * 参考论文模块实现，调用公有方法 approvalProcessService.reject()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYPY", "system:intraSch:KYPY"}, logical = Logical.OR)
  @Log(title = "成果转化立项驳回", businessType = BusinessType.UPDATE)
  @PostMapping("/sch_hxBh")
  @ResponseBody
  public AjaxResult hxBh(String id, String remark, String urlFlag) {
    int result = sciIntraSchProApplyService.sch_hxBh(id, getUserId(), remark, urlFlag);
    if (result <= 0) {
      return AjaxResult.error("审批驳回失败，请检查当前状态和审批权限");
    }
    return AjaxResult.success("驳回成功");
  }

  /**
   * 立项审批通过
   * 参考论文模块实现，调用公有方法 approvalProcessService.approve()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYPY", "system:intraSch:KYPY"}, logical = Logical.OR)
  @Log(title = "成果转化立项通过", businessType = BusinessType.UPDATE)
  @PostMapping("/sch_hxPass")
  @ResponseBody
  public AjaxResult hxPass(String id, String urlFlag) {
    int rows = sciIntraSchProApplyService.sch_hxPass(id, getUserId(), urlFlag);
    if (rows <= 0) {
      return AjaxResult.error("审批通过失败，请检查当前状态和审批权限");
    }
    SciIntraSchoolPro updated = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    if (updated != null && TEC_TRA_PASSED.equals(updated.getState())) {
      sciIntraSchProScoreService.set_SchPro_score(updated, 1);
    }
    return AjaxResult.success("审批通过");
  }

  /**
   * 判断当前用户是否有审批权限：pro=教研室审批(状态1/JYS_AUDIT)，hecha=科研处审批(状态2/KYC_AUDIT)
   */
  private boolean canApproveTecTra(SciIntraSchoolPro apply, String urlFlag) {
    if (apply == null) {
      return false;
    }
    String roleStr = panRole_str();
    String state = apply.getState();
    if ("pro".equals(urlFlag)) {
      return ("research".equals(roleStr) || "admin".equals(roleStr)) && ("1".equals(state) || TEC_TRA_JYS_AUDIT.equals(state));
    }
    if ("hecha".equals(urlFlag)) {
      return ("sci_tesearch".equals(roleStr) || "admin".equals(roleStr)) && ("2".equals(state) || TEC_TRA_KYC_AUDIT.equals(state));
    }
    return false;
  }

  /**
   * 更新校内横向课题
   *
   * @param
   * @return
   */
  @PostMapping("/sc_edit")
  @ResponseBody
  public AjaxResult sc_editSave(SciIntraSchoolPro sciIntraSchoolPro) {
    SciIntraSchoolPro current = sciIntraSchProApplyService.sel_IntraSchPro_by_id(sciIntraSchoolPro.getId());
    if (!canEditOrDeleteTecTra(current)) {
      return AjaxResult.error("当前账号无此操作权限");
    }
    if (current != null && isRejectedTecTraState(current.getState())) {
      sciIntraSchoolPro.setState(TEC_TRA_DRAFT);
    }
    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro, getUserId()));
  }

  /**
   * 被退回，重新提交
   *
   * @param sciIntraSchoolPro
   * @return
   */
  @PostMapping("/change_sc_edit")
  @ResponseBody
  public AjaxResult change_sc_edit(SciIntraSchoolPro sciIntraSchoolPro) {
    SciIntraSchoolPro current = sciIntraSchProApplyService.sel_IntraSchPro_by_id(sciIntraSchoolPro.getId());
    if (!canEditOrDeleteTecTra(current)) {
      return AjaxResult.error("当前账号无此操作权限");
    }
    System.out.println("sciIntraSchoolPro.getState() = " + sciIntraSchoolPro.getState());
    if (sciIntraSchoolPro.getState().equals("3") || sciIntraSchoolPro.getState().equals("5") || sciIntraSchoolPro.getState().equals("12")) {
      sciIntraSchoolPro.setState("1");
    } else if (sciIntraSchoolPro.getState().equals("9") || sciIntraSchoolPro.getState().equals("10") || sciIntraSchoolPro.getState().equals("14")) {
      sciIntraSchoolPro.setState("7");
    }

    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro, getUserId()));
  }

  /**
   * 立项审批撤回
   * 参考论文模块实现，调用公有方法 approvalProcessService.recall()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYCH", "system:intraSch:KYCH"}, logical = Logical.OR)
  @Log(title = "成果转化立项撤回", businessType = BusinessType.UPDATE)
  @PostMapping("/retract")
  @ResponseBody
  public AjaxResult retract(String id, String remark, String urlFlag) {
    SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    if (isPassedTecTraState(sciIntraSchoolPro1.getState())) {
      sciIntraSchProScoreService.update_SchPro_score(sciIntraSchoolPro1);
    }
    int result = sciIntraSchProApplyService.sch_hxCH(id, getUserId(), remark, urlFlag);
    if (result <= 0) {
      return AjaxResult.error("撤回失败，请检查当前状态和审批权限");
    }
    return AjaxResult.success("撤回成功");
  }

  /**
   * 撤回页面入口，设置urlFlag为hecha供前端判定
   */
  @GetMapping("/recall/{id}")
  public String recall(@PathVariable("id") Integer id, ModelMap mmap) {
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    sciIntraSchoolPro.setUrlFlag("hecha");
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    return prefix + "/recall";
  }

  /**
   * 执行撤回操作，先扣除积分（若已通过）再修改状态
   * 参考论文模块实现，调用公有方法 approvalProcessService.recall()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYCH", "system:intraSch:KYCH"}, logical = Logical.OR)
  @Log(title = "成果转化撤回", businessType = BusinessType.UPDATE)
  @PostMapping("/recallsave")
  @ResponseBody
  public AjaxResult recallSave(String id, String state, String remark, String urlFlag) {
    SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    if (isPassedTecTraState(sciIntraSchoolPro1.getState())) {
      sciIntraSchProScoreService.update_SchPro_score(sciIntraSchoolPro1);
    }
    int result = sciIntraSchProApplyService.sch_hxCH(id, getUserId(), remark, urlFlag);
    if (result <= 0) {
      return AjaxResult.error("撤回失败，请检查当前状态和审批权限");
    }
    return AjaxResult.success("撤回成功");
  }

  /**
   * 判断当前用户是否有撤回权限：admin可撤回任意，教研室可撤回到科研处审批状态的，科研处可撤回已通过的
   */
  private boolean canRecallTecTra(SciIntraSchoolPro apply) {
    if (apply == null) {
      return false;
    }
    String roleStr = panRole_str();
    if ("admin".equals(roleStr)) {
      return true;
    }
    if ("research".equals(roleStr) && ("2".equals(apply.getState()) || TEC_TRA_KYC_AUDIT.equals(apply.getState()))) {
      return true;
    }
    return "sci_tesearch".equals(roleStr) && isPassedTecTraState(apply.getState());
  }

  /**
   * 判断是否为已通过状态（旧编码4/6 或 TEC_TRA_PASSED）
   */
  private boolean isPassedTecTraState(String state) {
    return "4".equals(state) || "6".equals(state) || TEC_TRA_PASSED.equals(state);
  }

  /**
   * 判断是否为驳回状态（旧编码3/5/12 或 TEC_TRA_REJECTED）
   */
  private boolean isRejectedTecTraState(String state) {
    return "3".equals(state) || "5".equals(state) || "12".equals(state) || TEC_TRA_REJECTED.equals(state);
  }

  /**
   * 判断当前用户是否有权限编辑/删除课题：admin可操作任意，其他角色只能操作自己的
   */
  private boolean canEditOrDeleteTecTra(SciIntraSchoolPro apply) {
    if (apply == null) return false;
    if ("admin".equals(panRole_str())) return true;
    return apply.getUserId() != null && apply.getUserId().longValue() == getUserId().longValue();
  }

  /**
   * 结项审批撤回
   * 参考论文模块实现，调用公有方法 approvalProcessService.recall()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYCH", "system:intraSch:KYCH"}, logical = Logical.OR)
  @Log(title = "成果转化结项撤回", businessType = BusinessType.UPDATE)
  @PostMapping("/over_retract")
  @ResponseBody
  public AjaxResult over_retract(String id, String remark, String urlFlag) {
    SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    sciIntraSchProScoreService.update_SchPro_score_jt(sciIntraSchoolPro1);
    int result = sciIntraSchProApplyService.sch_hxOverCH(id, getUserId(), remark, urlFlag);
    if (result <= 0) {
      return AjaxResult.error("结项撤回失败，请检查当前状态和审批权限");
    }
    return AjaxResult.success("结项撤回成功");
  }

  /**
   * 保存结题编辑，需校验操作权限
   */
  @PostMapping("/edit_Over")
  @ResponseBody
  //todo:这里的sciHorizontalApply里面getState()是个null
  public AjaxResult editSave_Over(SciIntraSchoolPro sciIntraSchoolPro) {
    SciIntraSchoolPro current = sciIntraSchProApplyService.sel_IntraSchPro_by_id(sciIntraSchoolPro.getId());
    if (!canEditOrDeleteTecTra(current)) {
      return AjaxResult.error("当前账号无此操作权限");
    }
    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro, getUserId()));
  }

  /**
   * 结项成果转化
   */
  @GetMapping("/overadd")
  public String overadd(Integer id, ModelMap mmap) {
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
    mmap.put("sysUsers", userList);
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    return prefix + "/overadd";
  }

  /**
   * 第一次申请结项（需上传文件），修改课题状态并插入流程记录
   */
  @PostMapping("/sch_overadd")
  @ResponseBody
  public AjaxResult sch_overaddSave(SciIntraSchoolPro sciIntraSchoolPro) {
    //前端页面写死的7
    String state = sciIntraSchoolPro.getState();
    //System.out.println("state = " + state);
    String id = String.valueOf(sciIntraSchoolPro.getId());
    // 修改课题状态，插入流程记录
    sciIntraSchProApplyService.overApply(id, state,getUserId());
    //增加结题文件等新的字段
    return toAjax(sciIntraSchProApplyService.update_IntraSchPro_OverApply(sciIntraSchoolPro));
  }

  /**
   * 结项审批通过
   * 参考论文模块实现，调用公有方法 approvalProcessService.approve()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYPY", "system:intraSch:KYPY"}, logical = Logical.OR)
  @Log(title = "成果转化结项通过", businessType = BusinessType.UPDATE)
  @PostMapping("/sch_hxover")
  @ResponseBody
  public AjaxResult hxover(String id, String urlFlag) {
    int rows = sciIntraSchProApplyService.sch_hxover(id, getUserId(), urlFlag);
    if (rows <= 0) {
      return AjaxResult.error("结项审批通过失败，请检查当前状态和审批权限");
    }
    if ("KYCOVER".equals(urlFlag)) {
      SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
      sciIntraSchProScoreService.set_SchPro_score(sciIntraSchoolPro1, 1);
    }
    return AjaxResult.success("结项审批通过");
  }

  /**
   * 判断当前用户是否有结题审批权限：JYSOVER=教研室结题审批，KYCOVER=科研处结题审批，dept_teacher=学院负责人审批
   */
  private boolean canApproveOverTecTra(String urlFlag) {
    String roleStr = panRole_str();
    if ("admin".equals(roleStr)) {
      return true;
    }
    if ("JYSOVER".equals(urlFlag)) {
      return "research".equals(roleStr);
    }
    if ("KYCOVER".equals(urlFlag)) {
      return "sci_tesearch".equals(roleStr);
    }
    if ("dept_teacher".equals(urlFlag)) {
      return "dept_teacher".equals(roleStr);
    }
    return false;
  }

  /**
   * 结项审批驳回
   * 参考论文模块实现，调用公有方法 approvalProcessService.reject()
   * 公有方法中已包含后端身份校验和所有审批逻辑
   */
  @RequiresPermissions(value = {"system:intraSch:JYPY", "system:intraSch:KYPY"}, logical = Logical.OR)
  @Log(title = "成果转化结项驳回", businessType = BusinessType.UPDATE)
  @PostMapping("/sch_hxoverBh")
  @ResponseBody
  public AjaxResult hxoverBh(String id, String remark, String urlFlag) {
    int result = sciIntraSchProApplyService.sch_hxoverBh(id, getUserId(), remark, urlFlag);
    if (result <= 0) {
      return AjaxResult.error("结项驳回失败，请检查当前状态和审批权限");
    }
    return AjaxResult.success("结项驳回成功");
  }


  /**
   * 删除成果转化课题，逐一校验每条记录的编辑/删除权限
   */
  @Log(title = "删除校内横向课题", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    for (String id : ids.split(",")) {
      SciIntraSchoolPro apply = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id.trim()));
      if (!canEditOrDeleteTecTra(apply)) {
        return AjaxResult.error("当前账号无此操作权限");
      }
    }
    return toAjax(sciIntraSchProApplyService.deleteSciSCHHorizontalApplyByIds(ids));
  }

  /**
   * 增加项目金额
   *
   * @param sciIntraSchProReamount
   * @return
   */
  @PostMapping("/Reamount")
  @ResponseBody
  public AjaxResult Reamount(SciIntraSchProReamount sciIntraSchProReamount) {
    System.out.println("sciIntraSchProReamount = " + sciIntraSchProReamount);
    String userid = String.valueOf(getUserId());
    sciIntraSchProReamount.setApplyId(userid);
    return toAjax(sciIntraSchProReamountService.insertAmount(sciIntraSchProReamount));
  }

//    List<String> fourHead = Arrays.asList(sciIntraSchoolPro.getFirstPersonId(),sciIntraSchoolPro.getSecondPersonId(),sciIntraSchoolPro.getSecondPersonId(),sciIntraSchoolPro.getThirdPersonId());
//        System.out.println("fourHead = " + fourHead);

  /**
   * 暂未使用
   *
   * @param request
   * @return
   */
  @PostMapping("/getscore_byId")
  @ResponseBody
  public Map<String, Object> getscore(@RequestBody Map<String, Object> request) {
    Object id = request.get("id");

    Map<String, Object> resp = new HashMap<>();

    Integer score = sciIntraSchProScoreService.getScoreById(id, getUserId());
    System.out.println("score = " + score);
    if (score == null) {
      resp.put("score", 0);
    } else {
      resp.put("score", score);
    }


    return resp;

  }


}
