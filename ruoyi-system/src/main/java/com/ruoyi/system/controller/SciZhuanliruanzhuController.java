package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciZhuanliruanzhuMapper;
import com.ruoyi.system.service.ISciHorizontalPiyueService;
import com.ruoyi.system.service.ISciZhuanliruanzhuPiyueService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;

/**
 * 专利软著Controller
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
@Controller
@RequestMapping("/system/zhuanliruanzhu")
public class SciZhuanliruanzhuController extends BaseController
{
    private String prefix = "system/zhuanliruanzhu";

    @Autowired
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciZhuanliruanzhuPiyueService piyueService;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    @Autowired
    private SciZhuanliruanzhuMapper sciZhuanliruanzhuMapper;

    @RequiresPermissions("system:zhuanliruanzhu:view")
    @GetMapping()
    public String zhuanliruanzhu()
    {
        return prefix + "/zhuanliruanzhu";
    }

    /**
     * 查询专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhu sciZhuanliruanzhu,String year)
    {
        sciZhuanliruanzhu.setYear(year);
        sciZhuanliruanzhu.setUid(getUserId());


//        startPage();
//        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);

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
        sciZhuanliruanzhu.setRole(role);

        SysUser user = getSysUser();

        //设置部门id，传输过去用来为查询设置部门限制
        sciZhuanliruanzhu.setDeptId(getSysUser().getDeptId());

//          无用了//设置部门父id，传输过去用来为查询设置部门限制，这个是为查询部门负责人时，查询出部门负责人的部门，并设置查询条件，查询出部门负责人的部门下的所有子部门，
//        sciZhuanliruanzhu.setParentId(user.getDept().getParentId());
//        sciZhuanliruanzhu.setParentId(getSysUser().getAncestors());
        System.out.println(getSysUser());
        System.out.println(user.getDept().getParentId());



//        if (user != null && user.getDept() != null) {
//            Long parentDeptId = user.getDept().getParentId();
//            System.out.println("Parent Department ID: " + parentDeptId);
//        } else {
//            System.out.println("User or Department information is not available.");
//        }
//        SysUser user = getSysUser();
//        System.out.println(user);  // 检查整个对象
//        System.out.println(user.getAncestors());  // 检查 parentId

//        System.out.println(getSysUser().getDeptId());
//
//        // 输出 sciZhuanliruanzhu 对象到控制台
        System.out.println(sciZhuanliruanzhu);

        List<SciZhuanliruanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList4(sciZhuanliruanzhu);
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList3(sciZhuanliruanzhu);
                break;
//        教研室
            case "research":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList2(sciZhuanliruanzhu);
                break;

//        教师
            default:
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
                break;
        }

//        List<SciHorizontalApply> list1 = new ArrayList<>();
//        list1 = sciHorizontalApplyService.selectOtherListByUid(sciHorizontalApply);
//        list.addAll(list1);

//        // 去重操作
//        List<SciZhuanliruanzhu> distinctList = list.stream()
//                .collect(Collectors.collectingAndThen(
//                        Collectors.toMap(
//                                SciZhuanliruanzhu::getTopName,  // 假设 getTopName 是 SciZhuanliruanzhu 类的方法
//                                Function.identity(),
//                                (existing, replacement) -> existing
//                        ),
//                        map -> new ArrayList<>(map.values())
//                ));
//
//        if(!distinctList.isEmpty()) {
//            for (SciZhuanliruanzhu apply : distinctList) {
//                List<SciZhuanliruanzhu> score = sciZhuanliruanzhuMapper.selectScoreHistoryById(apply.getId());
//                ArrayList<Integer> allscore = new ArrayList<>();
//                for (SciZhuanliruanzhu score1 : score) {
//
//
////                    if (apply.getUserId().equals(score1.getUserId()) && apply.getUserId().equals(getUserId().toString()))
//                        allscore.add(Integer.parseInt(score1.getChangeValue()));
////                    if (apply.getFirstPersonId().equals(score1.getUserId()) && apply.getFirstPersonId().equals(getUserId().toString()))
////                        allscore.add(Integer.parseInt(score1.getChangeValue()));
////                    else if (apply.getSecondPersonId().equals(score1.getUserId()) && apply.getSecondPersonId().equals(getUserId().toString())) {
////                        allscore.add(Integer.parseInt(score1.getChangeValue()));
////                    } else if (apply.getThirdPersonId().equals(score1.getUserId()) && apply.getThirdPersonId().equals(getUserId().toString())) {
////                        allscore.add(Integer.parseInt(score1.getChangeValue()));
////                    } else if (apply.getFourthPersonId().equals(score1.getUserId()) && apply.getFourthPersonId().equals(getUserId().toString())) {
////                        allscore.add(Integer.parseInt(score1.getChangeValue()));
////                    }
//                }
//                Integer count = 0;
//                for (int i : allscore) {
//                    count += i;
//                }
//                apply.setScore(count.toString());
//            }
//        }


        return getDataTable(list);
    }

    /**
     * 导出专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:export")
    @Log(title = "专利软著", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
        ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
        return util.exportExcel(list, "专利软著数据");
    }

    /**
     * 新增专利软著
     */
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {

        List<SysUser> userList =  userService.selectAllUser();
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
     * 新增保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:add")
    @Log(title = "专利软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.insertSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 修改专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);

        // 获取用户列表并添加到模型中
//        List<SysUser> sysUsers = userService.selectUserList(null);
//        mmap.put("sysUsers", sysUsers);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);

        return prefix + "/edit";
    }

    /**
     * 修改保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @Log(title = "专利软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return toAjax(sciZhuanliruanzhuService.updateSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 删除专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:remove")
    @Log(title = "专利软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciZhuanliruanzhuService.deleteSciZhuanliruanzhuByIds(ids));
    }



//    @RequiresPermissions("system:zhuanliruanzhu:process","system:zhuanliruanzhu:info")
    @RequiresPermissions(value={"system:zhuanliruanzhu:process","system:zhuanliruanzhu:info"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        sciZhuanliruanzhu.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/detail";
    }


//    @RequiresPermissions("system:apply:edit")
//    @PostMapping("/bhyy/{kid}")
//    @ResponseBody
//    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
//    {
//        SciHorizontalPiyue ob = new SciHorizontalPiyue();
//        ob.setHxktId(kid);
//        List<SciHorizontalPiyue> list = piyueService.selectSciHorizontalPiyueList(ob);
//        return getDataTable(list);
//    }



    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "专利软著审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag, String amount, SciProjectScoreCfg sciProjectScoreCfg)
    {
        //        初始化一个新对象，存储最大值和最小值
//        SciProjectScoreCfg sciProjectScoreCfg1 = new SciProjectScoreCfg();
//
////        查询积分的所有范围
//        List<SciProjectScoreCfg> list= sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg);
//
//        Integer applyId;
//        Integer Damount;
//        try {
//            Damount = Integer.valueOf(amount);
//        } catch (NumberFormatException e) {
//            return AjaxResult.error("金额无效");
//        }
//
////        查询项目金额在积分的哪个范围内，并将范围记录到sciProjectScoreCfg1中
//        for (SciProjectScoreCfg scoreCfg : list) {
//            if (Damount >= Integer.valueOf(scoreCfg.getFundsMin()) && Damount < Integer.valueOf(scoreCfg.getFundsMax())) {
//                sciProjectScoreCfg1.setFundsMin(scoreCfg.getFundsMin());
//                sciProjectScoreCfg1.setFundsMax(scoreCfg.getFundsMax());
//                break;
//            }
//        }
//
////       查询范围为 min-max 的分数
//        List<SciProjectScoreCfg> score_list = sciProjectScoreCfgMapper.selectSciProjectScoreCfgList(sciProjectScoreCfg1);
//
//        //        查询该条数据的负责人id
//        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(Integer.valueOf(id));
//        applyId = sciZhuanliruanzhu.getId();
//
////        将负责人和积分顺序存储到列表中传到实现类中
//        List score = new ArrayList();
//        List persion = new ArrayList();
//        for (SciProjectScoreCfg scoreCfg : score_list) {
//            score.add(scoreCfg.getStartScore());
//        }
//
//
//        if (sciZhuanliruanzhu.getUid() != null && sciZhuanliruanzhu.getUid() != 0L) {
//            persion.add(sciZhuanliruanzhu.getUid().toString());  // 如果需要添加字符串形式的 UID
//        }


//        if (sciZhuanliruanzhu.getUid() != null && !sciZhuanliruanzhu.getUid().isEmpty()) {
//            persion.add(sciZhuanliruanzhu.getUid());
//        }
//        if (sciZhuanliruanzhu.getSecondPersonId() != null && !sciZhuanliruanzhu.getSecondPersonId().isEmpty()) {
//            persion.add(sciZhuanliruanzhu.getSecondPersonId());
//        }
//        if (sciZhuanliruanzhu.getThirdPersonId() != null && !sciZhuanliruanzhu.getThirdPersonId().isEmpty()) {
//            persion.add(sciZhuanliruanzhu.getThirdPersonId());
//        }
//        if (sciZhuanliruanzhu.getFourthPersonId() != null && !sciZhuanliruanzhu.getFourthPersonId().isEmpty()) {
//            persion.add(sciZhuanliruanzhu.getFourthPersonId());
//        }


//        return toAjax(sciZhuanliruanzhuService.hxPass(id,getUserId(),urlFlag,score,persion,applyId));
        return toAjax(sciZhuanliruanzhuService.hxPass(id,getUserId(),urlFlag));
    }
//    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process"},logical= Logical.OR)
//    @Log(title = "结项专利软著审核通过", businessType = BusinessType.UPDATE)
//    @PostMapping( "/hxover")
//    @ResponseBody
//    public AjaxResult hxover(String id,String urlFlag)
//    {
//        return toAjax(sciZhuanliruanzhuService.hxover(id,getUserId(),urlFlag));
//    }

    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "专利软著被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciZhuanliruanzhuService.hxBh(id,getUserId(),remark,urlFlag));
    }
//    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process"},logical= Logical.OR)
//    @Log(title = "结项专利软著被驳回", businessType = BusinessType.UPDATE)
//    @PostMapping( "/hxoverBh")
//    @ResponseBody
//    public AjaxResult hxoverBh(String id,String remark,String urlFlag)
//    {
//        return toAjax(sciZhuanliruanzhuService.hxoverBh(id,getUserId(),remark,urlFlag));
//    }




//    /**
//     * 学院审核人审核通过
//     */
//    @RequiresPermissions("system:zhuanliruanzhu:collegeAudit")
//    @Log(title = "专利软著学院审核通过", businessType = BusinessType.UPDATE)
//    @PostMapping("/collegeAudit")
//    @ResponseBody
//    public AjaxResult collegeAudit(String id, String urlFlag) {
//        return toAjax(sciZhuanliruanzhuService.collegeAudit(id, getUserId(), urlFlag));
//    }
//
//    /**
//     * 学院审核人驳回
//     */
//    @RequiresPermissions("system:zhuanliruanzhu:collegeAudit")
//    @Log(title = "专利软著学院驳回", businessType = BusinessType.UPDATE)
//    @PostMapping("/collegeBh")
//    @ResponseBody
//    public AjaxResult collegeBh(String id, String remark, String urlFlag) {
//        return toAjax(sciZhuanliruanzhuService.collegeBh(id, getUserId(), remark, urlFlag));
//    }





    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/recall";
    }


    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciZhuanliruanzhuService.recall(id,state,getUserId(),remark,urlFlag));
    }


    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:edit","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciZhuanliruanzhuPiyue ob = new SciZhuanliruanzhuPiyue();
        ob.setHxktId(kid);
        List<SciZhuanliruanzhuPiyue> list = piyueService.selectSciZhuanliruanzhuPiyueList(ob);
        return getDataTable(list);
    }
}

