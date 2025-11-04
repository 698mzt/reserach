package com.ruoyi.web.controller.IntraSchoolProject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;

import static net.sf.jsqlparser.parser.feature.Feature.set;

//http://localhost:8081/IntraSchPro
@Controller
@RequestMapping("/IntraSchPro")
public class SciIntraSchoolProController extends BaseController {
  private String prefix = "system/IntraSchPro";

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

  //private String role_str="";
  @GetMapping("")
  String view() {
    return prefix + "/view";
  }

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
//        List<Long> roleid_list=sciIntraSchProApplyService.getRoleid_list(getUserId());
//        System.out.println("roleid_list = " + roleid_list);
//        System.out.println("roleid_list  " + roleid_list.contains(100L));

    startPage();
    //判断当前用户的角色
    String role_str = panRole_str();
    System.out.println("role_str = " + role_str);
    List<SciIntraSchoolPro> list = new ArrayList<>();

    //学院
    if (role_str.equals("dept_teacher")) {

      System.out.println("this is 学院负责人");
      switch (tableId) {
        case "bootstrap-table0":
          list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER_dept_teacher(sciIntraSchoolPro);
          System.out.println("list = " + list);
          break;
        case "bootstrap-table1":
          list = sciIntraSchProApplyService.sel_IntraSchPro_approval_dept_teacher(sciIntraSchoolPro);
          break;
        case "bootstrap-table2":
          list = sciIntraSchProApplyService.sel_IntraSchPro_closure_dept_teacher(sciIntraSchoolPro);
          break;
      }
    }
//        科研处
    else if (role_str.equals("sci_tesearch")) {

      System.out.println("this is 科研");
      switch (tableId) {
        case "bootstrap-table0":
          list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
          System.out.println("list = " + list);
          break;
        case "bootstrap-table1":
          list = sciIntraSchProApplyService.sel_IntraSchPro_approval_ky(sciIntraSchoolPro);
          break;
        case "bootstrap-table2":
          list = sciIntraSchProApplyService.sel_IntraSchPro_closure_ky(sciIntraSchoolPro);
          break;
      }
    }
//        教研室
    else if (role_str.equals("research")) {
      System.out.println("this is 教研");

      switch (tableId) {
        case "bootstrap-table0":
          list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER(sciIntraSchoolPro);
          //System.out.println("this is table0 list = " + list);
          System.out.println("this is table0 list = " + list);
          break;
        case "bootstrap-table1":
          list = sciIntraSchProApplyService.sel_IntraSchPro_approval_jy(sciIntraSchoolPro);
          // System.out.println("this is table1 list = " + list);
          System.out.println("this is table1 list = ");
          break;
        case "bootstrap-table2":
          list = sciIntraSchProApplyService.sel_IntraSchPro_closure_jy(sciIntraSchoolPro);
          //System.out.println("this is table2 list = " + list);
          System.out.println("this is table2 list = " + list);
          System.out.println(" = ");
          break;
      }
    }
    //admin
    else if (role_str.equals("admin")) {
      System.out.println("this is admin");

      switch (tableId) {
        case "bootstrap-table0":
          list = sciIntraSchProApplyService.sel_IntraSchPro_isOVER_admin(sciIntraSchoolPro);
          System.out.println("list = " + list);
          break;
        case "bootstrap-table1":
          list = sciIntraSchProApplyService.sel_IntraSchPro_approval_admin(sciIntraSchoolPro);
          break;
        case "bootstrap-table2":
          list = sciIntraSchProApplyService.sel_IntraSchPro_closure_admin(sciIntraSchoolPro);
          break;
      }
    }

    if (role_str.equals("teacher")) {

      System.out.println("this is 普通教师");
      switch (tableId) {
        case "bootstrap-table0":
          list = sciIntraSchProApplyService.sel_my_IntraSchPro_isOVER(sciIntraSchoolPro);
          System.out.println("list = " + list);
          break;
        case "bootstrap-table1":
          list = sciIntraSchProApplyService.sel_IntraSchPro_approval_my(sciIntraSchoolPro);
          break;
        case "bootstrap-table2":
          list = sciIntraSchProApplyService.sel_IntraSchPro_closure_my(sciIntraSchoolPro);
          break;
      }
    }
    TableDataInfo data = getDataTable(list);

    //System.out.println("data = " + data);
    return data;
  }

  /**
   * 判断当前登陆用户的身份
   * dept_teacher，sci_tesearch，research，admin，teacher
   *
   * @return
   */
  private String panRole_str() {
    String role_str = "";
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
      if (r.getRoleId() == 103 || r.getRoleId() == 104 || r.getRoleId() == 105 || r.getRoleId() == 106 || r.getRoleId() == 107 || r.getRoleId() == 108) {
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
    //sciIntraSchProScoreService.set_SchPro_score_noScore(sciIntraSchoolPro);
//        return toAjax(sciIntraSchProApplyService.insert_SchPro_Apply(sciIntraSchoolPro));

    //数据库里面这个的默认值是15 草稿
    //System.out.println("addSave:"+sciIntraSchoolPro.getState());
      Set<String> countAuthors = new HashSet<>();
      countAuthors.add(sciIntraSchoolPro.getFirstPersonId());
      countAuthors.add(sciIntraSchoolPro.getSecondPersonId ());
      countAuthors.add(sciIntraSchoolPro.getThirdPersonId());
      countAuthors.add(sciIntraSchoolPro.getFourthPersonId());
      if (countAuthors.size() < 4){
          return error("负责人不能少于四个");
      }
    //插入这个课题
    sciIntraSchoolPro.setUid(getUserId());
    int id = sciIntraSchProApplyService.insert_SchPro_Apply(sciIntraSchoolPro);

    //插入这个课题的积分明细
    return toAjax(sciIntraSchProScoreService.set_SchPro_score_noScore(sciIntraSchoolPro));
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
    System.out.println("id = " + id);
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    System.out.println("sciIntraSchoolPro.getOverFiling() = " + sciIntraSchoolPro.getOverFiling());
    List<SysUser> userList1 = userService.selectAllUser();

    mmap.put("sysUsers1", userList1);
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    System.out.println("SciIntraSchoolProController.edit");
    //1,2,3,4,5,11,12
    //7,8,9,10,13,14
//        if (sciIntraSchoolPro.getState().equals("3")||sciIntraSchoolPro.getState().equals("5")){
//            System.out.println("1");
//            return prefix + "/edit";
//        }else if(sciIntraSchoolPro.getState().equals("6")){
//            System.out.println("2");
//            return prefix + "/is_Over";
//        } else {
//            System.out.println("3");
//            return prefix + "/edit_Over";
//        }

    if (Arrays.asList("1", "2", "3", "4", "5", "11", "12", "15").contains(sciIntraSchoolPro.getState())) {
      System.out.println("1");
      return prefix + "/edit";
    } else if (sciIntraSchoolPro.getState().equals("6")) {
      System.out.println("2");
      return prefix + "/is_Over";
    } else {
      System.out.println("3");
      return prefix + "/edit_Over";
    }
    //return prefix + "/edit";
  }

  @GetMapping("/detail/{id}/{urlFlag}")
  public String detail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap) {
    //批阅的数据的user_id
    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    //获取当前用户的部门名字
    String user_dname = sciIntraSchProApplyService.getuser_dnameById(getUserId());
    System.out.println("user_dname = " + user_dname);
    //这里把全局变量role_str放进去用于对detail.html处理的判定
    sciIntraSchoolPro.setRole(panRole_str());
    System.out.println("sciIntraSchoolPro = " + sciIntraSchoolPro);
    List<SysUser> userList1 = userService.selectAllUser();
    sciIntraSchoolPro.setUrlFlag(urlFlag);

    mmap.put("sysUsers1", userList1);
    //判断自己的部门是不是和这个项目的部门相同，如果是就设置为1，不是就设置为0

    String role_str = panRole_str();
    if (user_dname.equals(sciIntraSchoolPro.getDname()) || role_str.equals("sci_tesearch")) {
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

    //这里把全局变量role_str放进去用于对overdetail.html处理的判定
    sciIntraSchoolPro.setRole(panRole_str());
    System.out.println("sciIntraSchoolPro = " + sciIntraSchoolPro);
    List<SysUser> userList1 = userService.selectAllUser();
    sciIntraSchoolPro.setUrlFlag(urlFlag);
    mmap.put("sysUsers1", userList1);
    mmap.put("sciIntraSchoolPro", sciIntraSchoolPro);
    //mmap.put("urlFlag",urlFlag);
    return prefix + "/overdetail";
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
    List<SciIntraSchProPiyue> list = piyueService.selectIntraSchProPiyueList(ob);
    return getDataTable(list);
  }


  /**
   * 更改自己的草稿状态，提交到教研室，加入操作记录
   *只用在view页面点击确认就可以直接提交草稿  view.html
   * @param id
   * @return
   */
  @PostMapping("/subDraft/{id}")
  @ResponseBody

  public AjaxResult subDraft(@PathVariable("id") Integer id) {
//        int data=0;
//        return AjaxResult.success("操作成功", data);
    String sid = String.valueOf(id);

    SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(id);
    String state = sciIntraSchoolPro.getState();
    if (state.equals("15")) {
      state = "1";
    } else if (sciIntraSchoolPro.getState().equals("16")) {
      state = "7";
    }
    return toAjax(sciIntraSchProApplyService.subDraft(sid, getUserId(),state));
  }

  /**
   * 驳回
   *
   * @param id
   * @param remark
   * @param urlFlag
   * @return
   */
  @PostMapping("/sch_hxBh")
  @ResponseBody
  public AjaxResult hxBh(String id, String remark, String urlFlag) {

    return toAjax(sciIntraSchProApplyService.sch_hxBh(id, getUserId(), remark, urlFlag));
  }

  /**
   * 开题通过
   *
   * @param id
   * @param urlFlag
   * @return
   */
  @PostMapping("/sch_hxPass")
  @ResponseBody
  public AjaxResult hxPass(String id, String urlFlag) {
    //如果是科研室通过，就设置积分
    if (urlFlag.equals("hecha")) {
      SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
      //key=0 代表开题
      sciIntraSchProScoreService.set_SchPro_score(sciIntraSchoolPro1, 0);
    }
    // 通过，修改状态
    return toAjax(sciIntraSchProApplyService.sch_hxPass(id, getUserId(), urlFlag));
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

    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro));
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
    System.out.println("sciIntraSchoolPro.getState() = " + sciIntraSchoolPro.getState());
    if (sciIntraSchoolPro.getState().equals("3") || sciIntraSchoolPro.getState().equals("5") || sciIntraSchoolPro.getState().equals("12")) {
      sciIntraSchoolPro.setState("1");
    } else if (sciIntraSchoolPro.getState().equals("9") || sciIntraSchoolPro.getState().equals("10") || sciIntraSchoolPro.getState().equals("14")) {
      sciIntraSchoolPro.setState("7");
    }

    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro));
  }

  /**
   * 开题撤回
   *
   * @param
   * @return
   */
  @PostMapping("/retract")
  @ResponseBody
  public AjaxResult retract(String id, String remark, String urlFlag) {
//        System.out.println("data = " + data);
//        SciIntraSchoolPro sciIntraSchoolPro = new SciIntraSchoolPro();
//
//        if ( data.get("state").equals("4")){
//            sciIntraSchoolPro.setState("2");
//        }else if (data.get("state").equals("2")){
//            sciIntraSchoolPro.setState("11");
//        }else if (data.get("state").equals("11")){
//            sciIntraSchoolPro.setState("1");
//
//        }
//        String idString = (String) data.get("id");
//        Integer id = Integer.parseInt(idString);
//        sciIntraSchoolPro.setId(id);
    //更改积分
    SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    int i = sciIntraSchProScoreService.update_SchPro_score(sciIntraSchoolPro1);
    System.out.println("i = " + i);
    //撤回
    return toAjax(sciIntraSchProApplyService.sch_hxCH(id, getUserId(), remark, urlFlag));
  }

  /**
   * 结题撤回
   *
   * @param
   * @return
   */
  @PostMapping("/over_retract")
  @ResponseBody
  public AjaxResult over_retract(String id, String remark, String urlFlag) {
//        System.out.println("data = " + data);
//        SciIntraSchoolPro sciIntraSchoolPro = new SciIntraSchoolPro();
//
//        if ( data.get("state").equals("6")){
//            sciIntraSchoolPro.setState("8");
//        }else if (data.get("state").equals("8")){
//            sciIntraSchoolPro.setState("13");
//        }else if (data.get("state").equals("13")){
//            sciIntraSchoolPro.setState("7");
//        }
//        String idString = (String) data.get("id");
//        Integer id = Integer.parseInt(idString);
//        sciIntraSchoolPro.setId(id);
    //更改积分
    SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
    int i = sciIntraSchProScoreService.update_SchPro_score_jt(sciIntraSchoolPro1);
    return toAjax(sciIntraSchProApplyService.sch_hxOverCH(id, getUserId(), remark, urlFlag));
  }

  @PostMapping("/edit_Over")
  @ResponseBody
  //todo:这里的sciHorizontalApply里面getState()是个null
  public AjaxResult editSave_Over(SciIntraSchoolPro sciIntraSchoolPro) {
    return toAjax(sciIntraSchProApplyService.updateIntraSchoolApply(sciIntraSchoolPro));
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

  // 第一次申请结项的时候调用（要上传文件）  overadd.html
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
   * 结项通过
   *
   * @param id
   * @param urlFlag
   * @return
   */
  @PostMapping("/sch_hxover")
  @ResponseBody
  public AjaxResult hxover(String id, String urlFlag) {
    System.out.println("SciIntraSchoolProController.hxover" + "id=" + id + " urlFlag=" + urlFlag);
    if (urlFlag.equals("KYCOVER")) {
      SciIntraSchoolPro sciIntraSchoolPro1 = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.valueOf(id));
      sciIntraSchProScoreService.set_SchPro_score(sciIntraSchoolPro1, 1);
    }
    return toAjax(sciIntraSchProApplyService.sch_hxover(id, getUserId(), urlFlag));
  }

  @PostMapping("/sch_hxoverBh")
  @ResponseBody
  public AjaxResult hxoverBh(String id, String remark, String urlFlag) {
    return toAjax(sciIntraSchProApplyService.sch_hxoverBh(id, getUserId(), remark, urlFlag));
  }


  @Log(title = "删除校内横向课题", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
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
