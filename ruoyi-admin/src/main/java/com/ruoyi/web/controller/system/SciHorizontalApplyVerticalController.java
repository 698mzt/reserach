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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    private SciHorizontalReamountService sciHorizontalReamountService;

    @Autowired
    private ISciHorizontalPiyueService piyueService;

    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

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
        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        label:
        for (SysRole r :roles){
            switch (r.getRoleKey()) {
                case "sci_tesearch":
                    role = "sci_tesearch";
                    break label;
                case "research":
                    role = "research";
                    break label;
                case "dept_teacher":
                    role = "dept_teacher";
                    break label;
            }
        }
        sciHorizontalApplyVertical.setRole(role);
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        List<SciHorizontalApplyVertical> Alist = new ArrayList<>();
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
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectVerticalAmountListKYC(sciHorizontalApplyVertical);
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
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectVerticalAmountListJYS(sciHorizontalApplyVertical);
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
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectVerticalAmountListDept(sciHorizontalApplyVertical);
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
                    case "bootstrap-table3":
                        Alist = sciHorizontalReamountService.selectVerticalAmountList(sciHorizontalApplyVertical);
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
        Integer yid = sysUser.getDeptId().intValue();
        distinctList.addAll(Alist);
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

        TableDataInfo data= getDataTable(distinctList);

        return data;
    }

    /**
     * 导出横向课题列表
     */
    @RequiresPermissions("system:apply_vertical:export")
    @Log(title = "导出纵向课题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
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
    public AjaxResult addSave(SciHorizontalApplyVertical sciHorizontalApplyVertical,SciHorizontalReamount sciHorizontalReamount)
    {
        Integer id = sciHorizontalApplyVerticalService.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        sciHorizontalReamount.setVerticalId(id.toString());
        sciHorizontalReamount.setState("1");
        return toAjax(sciHorizontalReamountService.insertVerticalAmount(sciHorizontalReamount));
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
    public AjaxResult overaddSave(SciHorizontalApplyVertical sciHorizontalApplyVertical,SciHorizontalReamount sciHorizontalReamount)
    {
        sciHorizontalApplyVerticalService.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        sciHorizontalReamount.setVerticalId(sciHorizontalApplyVertical.getId().toString());
        sciHorizontalReamount.setState("1");
        return toAjax(sciHorizontalReamountService.insertVerticalAmount(sciHorizontalReamount));
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
        sciHorizontalApplyVertical.setState("1");
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
        sciHorizontalApplyVertical.setState("1111");
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
    public AjaxResult applyPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.applyPass(id,getUserId(),urlFlag));
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
    public AjaxResult overPass(String id,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.overPass(id,getUserId(),urlFlag));
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

    /**
     * 添加到账金额
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/Reamount/{id}")
    public String Reamount(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/reamount";
    }
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "添加到账金额", businessType = BusinessType.INSERT)
    @PostMapping("/Reamount")
    @ResponseBody
    public AjaxResult Reamount(SciHorizontalReamount sciHorizontalReamount)
    {
        return toAjax(sciHorizontalReamountService.insertAmount(sciHorizontalReamount));
    }

    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciHorizontalPiyue ob = new SciHorizontalPiyue();
        ob.setVerticalId(kid);
        List<SciHorizontalPiyue> list = piyueService.selectVerticalAmountPiyueList(ob);
        return getDataTable(list);
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
     * 删除审批 横向课题操作
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
     * 删除审批 横向课题操作
     */
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "横向课题审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.recall(id,state,getUserId(),remark,urlFlag));
    }

    /**
     * 撤回审批金额操作
     */
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @GetMapping("/recallamount/{id}")
    public String recallamount(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(id);
        SciHorizontalApplyVertical sciHorizontalApplyVertical1 = sciHorizontalReamountService.selectVerticalAmountById(id);
        sciHorizontalApplyVertical.setReAmount(sciHorizontalApplyVertical1.getReAmount());
        sciHorizontalApplyVertical.setReamountUrl(sciHorizontalApplyVertical1.getReamountUrl());
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/recallamount";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "撤回金额", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallamountsave")
    @ResponseBody
    public AjaxResult recallamountSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.recallamount(id,state,getUserId(),remark,urlFlag));
    }

    /**
     * 修改追加金额
     */
    @RequiresPermissions("system:apply_vertical:edit")
    @GetMapping("/reamountedit/{id}")
    public String reamountedit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalReamountService.selectVerticalAmountById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/reamountedit";
    }
    @RequiresPermissions("system:apply_vertical:edit")
    @Log(title = "更新横向课题", businessType = BusinessType.UPDATE)
    @PostMapping("/reamountedit")
    @ResponseBody
    public AjaxResult reamounteditSave(SciHorizontalReamount sciHorizontalReamount)
    {
        sciHorizontalReamount.setState("1");
        return toAjax(sciHorizontalReamountService.amountedit(sciHorizontalReamount));
    }

    /**
     * 审批到账金额
     */
    @RequiresPermissions("system:apply_vertical:info")
    @GetMapping("/reamountdetail/{id}/{urlFlag}")
    public String reamountdetail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalReamountService.selectVerticalAmountById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciHorizontalApplyVertical.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciHorizontalApplyVertical", sciHorizontalApplyVertical);
        return prefix + "/reamountdetail";
    }
    @RequiresPermissions(value={"system:apply_vertical:JYS","system:apply_vertical:KYC","system:apply_vertical:Dept"},logical= Logical.OR)
    @Log(title = "纵向课题审核金额", businessType = BusinessType.UPDATE)
    @PostMapping( "/amountPass")
    @ResponseBody
    public AjaxResult amountPass(String id,String reid,String urlFlag,String amount,Double scale,String amountType,SciProjectScoreCfg sciProjectScoreCfg)
    {
//        初始化一个新对象，存储最大值和最小值
        SciProjectScoreCfg sciProjectScoreCfg1 = new SciProjectScoreCfg();
//        查询积分的所有范围
        List<SciProjectScoreCfg> list= sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg);
        Integer applyId;
        Integer Damount;
        try {
            Damount = Integer.valueOf(amount);
        } catch (NumberFormatException e) {
            return AjaxResult.error("金额无效");
        }
//        查询项目金额在积分的哪个范围内，并将范围记录到sciProjectScoreCfg1中
        for (SciProjectScoreCfg scoreCfg : list) {
            if (Damount >= Integer.valueOf(scoreCfg.getFundsMin()) && Damount < Integer.valueOf(scoreCfg.getFundsMax())) {
                sciProjectScoreCfg1.setFundsMin(scoreCfg.getFundsMin());
                sciProjectScoreCfg1.setFundsMax(scoreCfg.getFundsMax());
                break;
            }
        }
//       查询范围为 min-max 的分数
        List<SciProjectScoreCfg> score_list = sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg1);
//        查询该条数据的负责人id
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        applyId = sciHorizontalApplyVertical.getId();
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

        return toAjax(sciHorizontalApplyVerticalService.amountPass(id,reid,getUserId(),urlFlag,score,persion,applyId,amountType));
    }

    @RequiresPermissions(value={"system:apply:hecha","system:apply:process","system:apply:Dept"},logical= Logical.OR)
    @Log(title = "金额被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/amountBh")
    @ResponseBody
    public AjaxResult amountBh(String id,String reid,String remark,String urlFlag)
    {
        return toAjax(sciHorizontalApplyVerticalService.amountBh(id,reid,getUserId(),remark,urlFlag));
    }
}
