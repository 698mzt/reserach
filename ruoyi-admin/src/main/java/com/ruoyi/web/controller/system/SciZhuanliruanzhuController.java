package com.ruoyi.web.controller.system;

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
import org.springframework.web.bind.annotation.*;
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
 *  2024-11-21
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

    /**f
     * 查询专利软著列表
     */

    @RequiresPermissions("system:zhuanliruanzhu:list")
    @Log(title = "查询专利软著列表", businessType = BusinessType.OTHER)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciZhuanliruanzhu sciZhuanliruanzhu,String year)
    {

        sciZhuanliruanzhu.setYear(year);
        sciZhuanliruanzhu.setUid(getUserId());

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
                case "discuss_college":
                case "dzgc_college":
                case "yssj_college":
                case "student_college":
                case "marxism_college":
                case "general":
                    role = "dept_teacher";
                    break label;
                case "admin":
                    role = "admin";
                    break label;
            }
        }
        sciZhuanliruanzhu.setRole(role);

        SysUser user = getSysUser();

        //设置部门id，传输过去用来为查询设置部门限制
        sciZhuanliruanzhu.setDeptId(getSysUser().getDeptId());

//          无用了//设置部门父id，传输过去用来为查询设置部门限制，这个是为查询部门负责人时，查询出部门负责人的部门，并设置查询条件，查询出部门负责人的部门下的所有子部门，
        sciZhuanliruanzhu.setParentId(user.getDept().getParentId());

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


            case "admin":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
                break;
            //        教师
            default:
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
                break;
        }


        return getDataTable(list);
    }

    private String resolvePersonalRank(SciZhuanliruanzhu row, String uid) {
        if (row == null || uid == null || uid.isEmpty()) {
            return "";
        }
        if (uid.equals(safeStr(row.getFirstPersonId()))) return "1";
        if (uid.equals(safeStr(row.getSecondPersonId()))) return "2";
        if (uid.equals(safeStr(row.getThirdPersonId()))) return "3";
        if (uid.equals(safeStr(row.getFourthPersonId()))) return "4";
        if (isInMembers(row.getMembers(), uid)) return "5";
        return "";
    }

    private boolean isInMembers(String members, String uid) {
        if (members == null || members.isEmpty() || uid == null || uid.isEmpty()) {
            return false;
        }
        String quoted = "\"" + uid + "\"";
        return members.contains(quoted) || members.equals(uid);
    }

    private String safeStr(String s) {
        return s == null ? "" : s;
    }

    /**
     * 导出专利软著列表
     */
    @RequiresPermissions("system:zhuanliruanzhu:export")
    @Log(title = "导出专利软著列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciZhuanliruanzhu sciZhuanliruanzhu)
    {

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
                case "admin":
                    role = "admin";
                    break label;
            }
        }
        sciZhuanliruanzhu.setRole(role);

        //设置部门id，传输过去用来为查询设置部门限制
        sciZhuanliruanzhu.setDeptId(getSysUser().getDeptId());

        SysUser user = getSysUser();
        sciZhuanliruanzhu.setParentId(user.getDept().getParentId());

        List<SciZhuanliruanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);   //导出科研处的下的所有数据
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList31(sciZhuanliruanzhu);  //导出此学院下的所有数据
                break;
//        教研室
            case "research":
                list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList21(sciZhuanliruanzhu);   //导出此教研室下的所有数据
                break;

        }

        // 处理状态显示：状态为6显示"已完结"，其他显示"审批中"
        for (SciZhuanliruanzhu item : list) {
            if (item.getState() != null && "6".equals(item.getState())){
                item.setState("已完结");
            } else {
                item.setState("审批中");
            }
        }

//        List<SciZhuanliruanzhu> list = sciZhuanliruanzhuService.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
        ExcelUtil<SciZhuanliruanzhu> util = new ExcelUtil<SciZhuanliruanzhu>(SciZhuanliruanzhu.class);
        return util.exportExcel(list, "专利软著数据");
    }



    /**
     * 检查专利名称与负责人级别是否重复
     * 限制一个老师最大十条数据
     */
    @RequiresPermissions("system:zhuanliruanzhu:add")
    @Log(title = "检查专利名称与负责人级别是否重复", businessType = BusinessType.OTHER)
    @PostMapping("/checkDuplicate")
    @ResponseBody
    public AjaxResult checkDuplicate(@RequestParam String mingcheng, @RequestParam(required = false, defaultValue = "1") String paiming) {
        int exists = sciZhuanliruanzhuService.checkExist(mingcheng, paiming, getUserId());
        if (exists == 1) {
            return AjaxResult.error("该专利名称的该负责人级别已存在，不可重复添加");
        } else if (exists == 2) {
            return AjaxResult.error("教师添加的专利软著数据不允许大于十条");
        } else if (exists == 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("请联系管理员解决");
        }
    }
    /**
     * 新增专利软著
     */

    //get请求一般是加载表单页面，而不是处理表单提交
    @Log(title = "新增专利软著", businessType = BusinessType.OTHER)
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();

        // 获取当前用户的部门ID
        Long currentUserDeptId = null;
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId().equals(getUserId())){
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a,user);
                // 直接从数据库查询当前用户的部门ID
                SysUser currentUserWithDept = userService.selectUserById(getUserId());
                if (currentUserWithDept != null && currentUserWithDept.getDeptId() != null) {
                    currentUserDeptId = currentUserWithDept.getDeptId();
                }
                break;
            }
        }

        // 如果找到当前用户的部门ID，按部门重新排序
        if (currentUserDeptId != null) {
            final Long finalCurrentUserDeptId = currentUserDeptId;
            userList.sort((u1, u2) -> {
                boolean u1SameDept = u1.getDeptId() != null && u1.getDeptId().equals(finalCurrentUserDeptId);
                boolean u2SameDept = u2.getDeptId() != null && u2.getDeptId().equals(finalCurrentUserDeptId);

                if (u1SameDept && !u2SameDept) return -1;
                if (!u1SameDept && u2SameDept) return 1;
                return u1.getUserName().compareTo(u2.getUserName());
            });
        }

        mmap.put("sysUsers",userList);
        return prefix + "/add";
    }



    /**
     * 新增保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:add")
    @Log(title = "新增保存专利软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciZhuanliruanzhu sciZhuanliruanzhu, String[] members)
    {
        sciZhuanliruanzhu.setUserId(getUserId().intValue());
        // 添加这一行：设置默认排名
        sciZhuanliruanzhu.setPaiming("1");

        // 处理成员数据
        if (members != null && members.length > 0) {
            // 将成员数组转换为JSON字符串
            StringBuilder membersJson = new StringBuilder("[");
            for (int i = 0; i < members.length; i++) {
                if (members[i] != null && !members[i].isEmpty()) {
                    membersJson.append("\"").append(members[i]).append("\"");
                    if (i < members.length - 1) {
                        membersJson.append(",");
                    }
                }
            }
            membersJson.append("]");
            sciZhuanliruanzhu.setMembers(membersJson.toString());
        }

        return toAjax(sciZhuanliruanzhuService.insertSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 修改专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @Log(title = "修改专利软著", businessType = BusinessType.OTHER)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        // 编辑页面的“个人排名”也要按当前登录人的槽位动态计算（1/2/3/4），避免显示第一/第二负责人字样
        Long currentUid = getUserId();
        String currentUidStr = currentUid == null ? "" : String.valueOf(currentUid);
        String personalRank = resolvePersonalRank(sciZhuanliruanzhu, currentUidStr);
        if (personalRank != null && !personalRank.isEmpty()) {
            sciZhuanliruanzhu.setPaiming(personalRank);
        }
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        List<SysUser> userList1 =  userService.selectAllUser();

        // 获取当前用户的部门ID进行排序
        Long currentUserDeptId = null;
        SysUser currentUserWithDept = userService.selectUserById(getUserId());
        if (currentUserWithDept != null && currentUserWithDept.getDeptId() != null) {
            currentUserDeptId = currentUserWithDept.getDeptId();
        }

        // 如果找到当前用户的部门ID，按部门重新排序
        if (currentUserDeptId != null) {
            final Long finalCurrentUserDeptId = currentUserDeptId;
            userList1.sort((u1, u2) -> {
                boolean u1SameDept = u1.getDeptId() != null && u1.getDeptId().equals(finalCurrentUserDeptId);
                boolean u2SameDept = u2.getDeptId() != null && u2.getDeptId().equals(finalCurrentUserDeptId);

                if (u1SameDept && !u2SameDept) return -1;
                if (!u1SameDept && u2SameDept) return 1;
                return u1.getUserName().compareTo(u2.getUserName());
            });
        }

        mmap.put("sysUsers1",userList1);
        return prefix + "/edit";
    }


    /**
     * 修改保存专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:edit")
    @Log(title = "修改保存专利软著", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        sciZhuanliruanzhu.setUserId(Integer.valueOf(getSysUser().getUserId().toString()));
        // 处理更多成员数据，转换为与添加页面一致的JSON数组格式
        String members = sciZhuanliruanzhu.getMembers();
        if (members != null && !members.isEmpty() && !members.equals("null")) {
            // 如果不是JSON数组格式，转换为JSON数组格式
            if (!members.startsWith("[")) {
                sciZhuanliruanzhu.setMembers("[\"" + members + "\"]");
            }
        }
        return toAjax(sciZhuanliruanzhuService.updateSciZhuanliruanzhu(sciZhuanliruanzhu));
    }

    /**
     * 删除专利软著
     */
    @RequiresPermissions("system:zhuanliruanzhu:remove")
    @Log(title = "删除专利软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciZhuanliruanzhuService.deleteSciZhuanliruanzhuByIds(ids));
    }



//    @RequiresPermissions("system:zhuanliruanzhu:process","system:zhuanliruanzhu:info")

    //批阅
    /**
     * 查看专利软著详情
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:process","system:zhuanliruanzhu:info"},logical= Logical.OR)
    @Log(title = "查看专利软著详情", businessType = BusinessType.OTHER)
    @GetMapping("/detail/{id}/{urlFlag}")

    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        if (sciZhuanliruanzhu == null) {
            mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
            mmap.put("error", "数据不存在，请刷新页面重试");
            mmap.put("errorMsg", "数据不存在，请刷新页面重试");
        } else {
            sciZhuanliruanzhu.setUrlFlag(urlFlag);
            List<SysUser> userList1 =  userService.selectAllUser();
            mmap.put("sysUsers1",userList1);
            mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        }
        return prefix + "/detail";
    }

    /**
     * 根据分类计算预计科研分（主持人/成员1/成员2/成员3），用于新增/编辑页面实时展示
     * @param fenlei 分类值（来自 sys_zhuanli_fenlei）
     */
    @GetMapping("/calcExpectedScores")
    @ResponseBody
    public AjaxResult calcExpectedScores(String fenlei) {
        if (fenlei == null || fenlei.trim().isEmpty()) {
            java.util.Map<String, String> zeroMap = new java.util.HashMap<>();
            zeroMap.put("firstScore", "0");
            zeroMap.put("secondScore", "0");
            zeroMap.put("thirdScore", "0");
            zeroMap.put("fourthScore", "0");
            return AjaxResult.success(zeroMap);
        }

        java.util.Map<String, String> scores = sciZhuanliruanzhuService.calculateExpectedScores(fenlei);
        return AjaxResult.success(scores);
    }


    /**
     * 专利软著审核通过
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue","system:zhuanliruanzhu:info"},logical= Logical.OR)
    @Log(title = "专利软著审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag, String amount, SciProjectScoreCfg sciProjectScoreCfg)
    {

        return toAjax(sciZhuanliruanzhuService.hxPass(id,getUserId(),urlFlag));
    }

    /**
     * 专利软著被驳回
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "专利软著被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sciZhuanliruanzhuService.hxBh(id,getUserId(),remark,urlFlag));
    }




    /**
     * 撤销专利软著
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销专利软著", businessType = BusinessType.OTHER)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(id);
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sciZhuanliruanzhu", sciZhuanliruanzhu);
        return prefix + "/recall";
    }

    /**
     * 撤销
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sciZhuanliruanzhuService.recall(id,state,getUserId(),remark,urlFlag));
    }

    /**
     * 查看批阅意见
     */
    @RequiresPermissions(value={"system:zhuanliruanzhu:hecha","system:zhuanliruanzhu:process","system:zhuanliruanzhu:edit","system:zhuanliruanzhu:chayue"},logical= Logical.OR)
    @Log(title = "查看批阅意见", businessType = BusinessType.OTHER)
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

