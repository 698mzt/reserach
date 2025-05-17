package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.SciHorizontalReamountService;
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
    private ISciHorizontalPiyueService piyueService;

    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    // 设置角色集合。若后期需要添加新的学院管理员角色，将其权限字符添加到集合中即可
    private static final Set<String> TEACHER_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher", // 软件学院管理员
            "discuss_college", // 商学院管理员
            "dzgc_college", // 电子工程学院管理员
            "art_design_college", // 艺术设计学院管理员
            "cxcy_college", // 创新创业学院管理员
            "marxism_college" // 马克思主义学院管理员
    ));

//    访问的首页
    @RequiresPermissions("system:apply_vertical:view")
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }

    /**
     * 查询横向课题列表
     */
    @RequiresPermissions("system:apply_vertical:list")
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
            for (SciHorizontalApplyVertical apply: distinctList){
                apply.setUserdnameId(did);
                apply.setUserynameId(yid);
                apply.setUid(getUserId());
                List<SciUserScore> score = sciUserScoreMapper.selectScoreVerticalById(apply.getId());
                ArrayList<Integer> allscore = new ArrayList<>();
                for(SciUserScore score1: score){
                    if (apply.getFirstPersonId().equals(score1.getUserId()) && apply.getFirstPersonId().equals(getUserId().toString()))
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    else if (apply.getSecondPersonId().equals(score1.getUserId()) && apply.getSecondPersonId().equals(getUserId().toString())){
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    }else if (apply.getThirdPersonId().equals(score1.getUserId()) && apply.getThirdPersonId().equals(getUserId().toString())){
                        allscore.add(Integer.parseInt(score1.getChangeValue()));
                    }else if (apply.getFourthPersonId().equals(score1.getUserId()) && apply.getFourthPersonId().equals(getUserId().toString())){
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
        startPage();
        TableDataInfo data= getDataTable(distinctList);
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
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();
        System.out.println(getUserId());
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId().equals(getUserId())){
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a,user);
                break;
            }
        }
        mmap.put("sysUsers",userList);
        return prefix + "/add";
    }
    /**
     * 保存新增立项申请
     */
    @RequiresPermissions("system:apply_vertical:add")
    @Log(title = "立项申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setState("99");
        return toAjax(sciHorizontalApplyVerticalService.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 提交申请进行审批
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/push/{id}")
    @ResponseBody
    @Transactional
    public AjaxResult push(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        sciHorizontalApplyVertical.setNewsql("1");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 结项
     */
    @RequiresPermissions("system:apply_vertical:add")
    @GetMapping("/overadd")
    public String overadd( Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList =  userService.selectAllUser();
        mmap.put("sysUsers",userList);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
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
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/edit";
    }

    /**
     *保存修改申请
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setNewsql("111");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
    }

    /**
     * 修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/overedit/{id}")
    public String overedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overedit";
    }
    /**
     *保存修改结项
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新立项申请", businessType = BusinessType.UPDATE)
    @PostMapping("/overedit")
    @ResponseBody
    public AjaxResult overeditSave(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        sciHorizontalApplyVertical.setNewsql("111");
        return toAjax(sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical));
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


    /**  查询要审批的纵向课题*/
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/detail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/applyPass")
    @ResponseBody
    public AjaxResult applyPass(String id,String urlFlag,String type,SciProjectScoreCfg sciProjectScoreCfg)
    {
        sciProjectScoreCfg.setFundsType(type);
        List<SciProjectScoreCfg> list = sciProjectScoreCfgMapper.selectVerticalScoreCfgList(sciProjectScoreCfg);
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String verticalId = String.valueOf(sciHorizontalApplyVertical.getId());
        List score = new ArrayList();
        List persion = new ArrayList();
        for (SciProjectScoreCfg scoreCfg : list) {
            score.add(scoreCfg.getStartScore());
        }
        if (sciHorizontalApplyVertical.getFirstPersonId() != null && !sciHorizontalApplyVertical.getFirstPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFirstPersonId());
        }
        if (sciHorizontalApplyVertical.getSecondPersonId() != null && !sciHorizontalApplyVertical.getSecondPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getSecondPersonId());
        }
        if (sciHorizontalApplyVertical.getThirdPersonId() != null && !sciHorizontalApplyVertical.getThirdPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getThirdPersonId());
        }
        if (sciHorizontalApplyVertical.getFourthPersonId() != null && !sciHorizontalApplyVertical.getFourthPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFourthPersonId());
        }

        return toAjax(sciHorizontalApplyVerticalService.applyPass(id,getUserId(),urlFlag,score,persion,verticalId));
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
    @GetMapping("/overdetail/{id}/{urlFlag}")
    public String overdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overdetail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题申请通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/overPass")
    @ResponseBody
    public AjaxResult overPass(String id,String urlFlag,String type,SciProjectScoreCfg sciProjectScoreCfg)
    {
        sciProjectScoreCfg.setFundsType(type);
        List<SciProjectScoreCfg> list = sciProjectScoreCfgMapper.selectVerticalScoreCfgList(sciProjectScoreCfg);
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String verticalId = String.valueOf(sciHorizontalApplyVertical.getId());
        List score = new ArrayList();
        List persion = new ArrayList();
        for (SciProjectScoreCfg scoreCfg : list) {
            score.add(scoreCfg.getEndScore());
        }
        if (sciHorizontalApplyVertical.getFirstPersonId() != null && !sciHorizontalApplyVertical.getFirstPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFirstPersonId());
        }
        if (sciHorizontalApplyVertical.getSecondPersonId() != null && !sciHorizontalApplyVertical.getSecondPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getSecondPersonId());
        }
        if (sciHorizontalApplyVertical.getThirdPersonId() != null && !sciHorizontalApplyVertical.getThirdPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getThirdPersonId());
        }
        if (sciHorizontalApplyVertical.getFourthPersonId() != null && !sciHorizontalApplyVertical.getFourthPersonId().isEmpty()) {
            persion.add(sciHorizontalApplyVertical.getFourthPersonId());
        }
        return toAjax(sciHorizontalApplyVerticalService.overPass(id,getUserId(),urlFlag,score,persion,verticalId));
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
    @GetMapping("/overView/{id}/{urlFlag}")
    public String overView(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag,ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/overView";
    }



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
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
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
