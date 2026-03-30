package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMapper;
import com.ruoyi.system.service.ISciJiaocairuanzhuPiyueService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISciJiaocairuanzhuService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;

/**
 * 教材软著Controller
 *
 * @author ruoyi
 *  2024-11-21
 */
@Controller
@RequestMapping("/system/jiaocairuanzhu")
public class SciJiaocairuanzhuController extends BaseController
{
    private String prefix = "system/jiaocairuanzhu";

    @Autowired
    private ISciJiaocairuanzhuService sciJiaocairuanzhuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISciJiaocairuanzhuPiyueService piyueService;

    @Autowired
    private SciProjectScoreCfgMapper sciProjectScoreCfgMapper;

    @Autowired
    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @Autowired
    private com.ruoyi.system.mapper.SciJiaocairuanzhuMemberScoreMapper sciJiaocairuanzhuMemberScoreMapper;

    /**
     * 跳转到教材软著页面
     */
    @RequiresPermissions("system:jiaocairuanzhu:view")
    @Log(title = "教材软著", businessType = BusinessType.OTHER)
    @GetMapping()
    public String jiaocairuanzhu()
    {
        return prefix + "/jiaocairuanzhu";
    }

    /**
     * 查询教材软著列表
     * 根据用户角色和部门信息查询对应的教材软著数据
     */
    @RequiresPermissions("system:jiaocairuanzhu:list")
    @Log(title = "教材软著查找", businessType = BusinessType.OTHER)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciJiaocairuanzhu sciJiaocairuanzhu, String year)
    {
        sciJiaocairuanzhu.setYear(year);
        sciJiaocairuanzhu.setUid(getUserId());

        startPage();
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        label:
        for (SysRole r : roles){
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
        sciJiaocairuanzhu.setRole(role);

        SysUser user = getSysUser();

        //设置部门id，传输过去用来为查询设置部门限制
        sciJiaocairuanzhu.setDeptId(getSysUser().getDeptId());

        //设置部门父id，传输过去用来为查询设置部门限制
        sciJiaocairuanzhu.setParentId(user.getDept().getParentId());

        List<SciJiaocairuanzhu> list = new ArrayList<>();
        //根据角色查询不同范围的数据
        switch (role) {
            case "sci_tesearch":
                //科研处查询
                list = sciJiaocairuanzhuService.selectSciPaperAListKY(sciJiaocairuanzhu);
                break;
            case "dept_teacher":
                //学院负责人查询
                list = sciJiaocairuanzhuService.selectSciPaperAListXY(sciJiaocairuanzhu);
                break;
            case "research":
                //教研室查询
                list = sciJiaocairuanzhuService.selectSciPaperAListCxList(sciJiaocairuanzhu);
                break;
            case "admin":
                //管理员查询
                list = sciJiaocairuanzhuService.selectSciPaperAList(sciJiaocairuanzhu);
                break;
            default:
                //教师查询
                list = sciJiaocairuanzhuService.selectSciPaperAListCx(sciJiaocairuanzhu);
                break;
        }

        return getDataTable(list);
    }

    /**
     * 导出教材软著列表
     * 根据用户角色导出对应的教材软著数据为Excel文件
     */
    @RequiresPermissions("system:jiaocairuanzhu:export")
    @Log(title = "教材软著导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SciJiaocairuanzhu sciJiaocairuanzhu)
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
        sciJiaocairuanzhu.setRole(role);

        //设置部门id，传输过去用来为查询设置部门限制
        sciJiaocairuanzhu.setDeptId(getSysUser().getDeptId());

        SysUser user = getSysUser();
        sciJiaocairuanzhu.setParentId(user.getDept().getParentId());

        List<SciJiaocairuanzhu> list = new ArrayList<>();
//        科研处
        switch (role) {
            case "sci_tesearch":

                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);   //导出科研处的下的所有数据
                break;
            //      学院负责人
            case "dept_teacher":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList31(sciJiaocairuanzhu);  //导出此学院下的所有数据
                break;
//        教研室
            case "research":
                list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList21(sciJiaocairuanzhu);   //导出此教研室下的所有数据
                break;

        }

        // 处理状态显示：状态为6显示"已完结"，其他显示"审批中"
        for (SciJiaocairuanzhu item : list) {
            if (item.getState() != null && "6".equals(item.getState())){
                item.setState("已完结");
            } else {
                item.setState("审批中");
            }
        }

//        List<SciJiaocairuanzhu> list = sciJiaocairuanzhuService.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
        ExcelUtil<SciJiaocairuanzhu> util = new ExcelUtil<SciJiaocairuanzhu>(SciJiaocairuanzhu.class);
        return util.exportExcel(list, "教材软著数据");
    }



    /**
     * 检查教材名称与负责人级别是否重复
     * 防止同一教材名称下重复的负责人级别
     */
    @Log(title = "防止教材软著重复", businessType = BusinessType.OTHER)
    @PostMapping("/checkDuplicate")
    @ResponseBody
    public AjaxResult checkDuplicate(@RequestParam String mingcheng, @RequestParam(required = false) String paiming) {
        boolean exists = sciJiaocairuanzhuService.checkExist(mingcheng, paiming, getUserId());
        if (exists) {
            return AjaxResult.error("该教材名称的该负责人级别已存在，不可重复添加");
        } else {
            return AjaxResult.success();
        }
    }

    /**
     * 跳转到新增教材软著页面
     * 加载用户列表供选择
     */
    //get请求一般是加载表单页面，而不是处理表单提交
    @Log(title = "新增教材软著", businessType = BusinessType.OTHER)
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
        for (int a = 0; a < userList.size(); a++) {
            if(userList.get(a).getUserId().equals(getUserId())){
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
     * 新增保存教材软著
     * 保存教材著作基本信息、成员信息和批阅记录
     */
    @Log(title = "教材软著", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciJiaocairuanzhu sciJiaocairuanzhu, 
                              @RequestParam(value = "members", required = false) String members, 
                              @RequestParam(value = "totalScore", required = false) String totalScore,
                              @RequestParam(value = "memberScores", required = false) String memberScores)
    {
        // 设置科研总分，如果没有传递则默认为0
        if (totalScore != null && !totalScore.isEmpty()) {
            sciJiaocairuanzhu.setJifen(totalScore);
        } else {
            sciJiaocairuanzhu.setJifen("0");
        }
        
        // 保存教材著作基本信息
        sciJiaocairuanzhuService.insertSciJiaocairuanzhu(sciJiaocairuanzhu);
        
        // 保存成员信息
        if (members != null && !members.isEmpty()) {
            sciJiaocairuanzhuService.saveJiaocairuanzhuMembers(sciJiaocairuanzhu.getId(), members);
        }
        
        // 保存成员积分信息到sci_jiaocairuanzhu_member_score表
        if (memberScores != null && !memberScores.isEmpty()) {
            try {
                com.alibaba.fastjson.JSONArray scoresArray = com.alibaba.fastjson.JSON.parseArray(memberScores);
                List<com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore> scoreList = new ArrayList<>();
                for (int i = 0; i < scoresArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject scoreObj = scoresArray.getJSONObject(i);
                    com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore memberScore = new com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore();
                    memberScore.setJiaocairuanzhuId(sciJiaocairuanzhu.getId());
                    memberScore.setUserId(scoreObj.getString("userId"));
                    memberScore.setScore(scoreObj.getString("score"));
                    memberScore.setRanking(String.valueOf(i + 1));
                    scoreList.add(memberScore);
                }
                if (!scoreList.isEmpty()) {
                    sciJiaocairuanzhuMemberScoreMapper.insertSciJiaocairuanzhuMemberScoreBatch(scoreList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 保存批阅记录
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setJiaocai_id(sciJiaocairuanzhu.getId());
        sciJiaocairuanzhuPiyue.setConcate("新增教材专著草稿");
        sciJiaocairuanzhuPiyue.setState("新增教材专著草稿");
        sciJiaocairuanzhuPiyue.setUid(getUserId());
        return toAjax(piyueService.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue));
    }

    /**
     * 跳转到修改教材软著页面
     * 加载教材软著详情、用户列表和成员列表
     */
    @RequiresPermissions("system:jiaocairuanzhu:edit")
    @Log(title = "修改教材软著", businessType = BusinessType.OTHER)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);

        // 获取用户列表并添加到模型中
        SysUser user = new SysUser();
        user.setParams(new HashMap<String, Object>());
        List<SysUser> sysUsers = userService.selectUserList(user);
        mmap.put("sysUsers1", sysUsers);
        
        // 获取所有用户列表用于成员选择
        List<SysUser> allUsers = userService.selectAllUserSchPro(getUserId());
        // 设置当前用户为主持人
        for (int a = 0; a < allUsers.size(); a++) {
            if(allUsers.get(a).getUserId().equals(sciJiaocairuanzhu.getUserId())){
                SysUser currentUser = allUsers.get(a);
                currentUser.setFlag(true);
                allUsers.set(a, currentUser);
                break;
            }
        }
        mmap.put("sysUsers", allUsers);

        // 获取教材著作成员列表
        List<com.ruoyi.system.domain.SciJiaocairuanzhuMember> members = sciJiaocairuanzhuService.getJiaocairuanzhuMembers(id);
        mmap.put("members", members);

        return prefix + "/edit";
    }

    /**
     * 获取教材著作成员列表
     * 根据教材著作ID查询对应的成员信息
     */
    @Log(title = "获取教材著作成员列表", businessType = BusinessType.OTHER)
    @GetMapping("/getMembers/{id}")
    @ResponseBody
    public AjaxResult getMembers(@PathVariable("id") Integer id)
    {
        List<com.ruoyi.system.domain.SciJiaocairuanzhuMember> members = sciJiaocairuanzhuService.getJiaocairuanzhuMembers(id);
        return AjaxResult.success(members);
    }

    /**
     * 修改保存教材软著
     * 更新教材著作基本信息和成员信息
     */
    @RequiresPermissions("system:jiaocairuanzhu:edit")
    @Log(title = "更新教材软著信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciJiaocairuanzhu sciJiaocairuanzhu, 
                              @RequestParam(value = "members", required = false) String members, 
                              @RequestParam(value = "totalScore", required = false) String totalScore,
                              @RequestParam(value = "memberScores", required = false) String memberScores)
    {
        // 设置科研总分，如果没有传递则默认为0
        if (totalScore != null && !totalScore.isEmpty()) {
            sciJiaocairuanzhu.setJifen(totalScore);
        } else {
            sciJiaocairuanzhu.setJifen("0");
        }
        
        // 保存教材著作基本信息
        int result = sciJiaocairuanzhuService.updateSciJiaocairuanzhu(sciJiaocairuanzhu);
        
        // 保存成员信息
        if (members != null && !members.isEmpty()) {
            sciJiaocairuanzhuService.saveJiaocairuanzhuMembers(sciJiaocairuanzhu.getId(), members);
        }
        
        // 更新成员积分信息到sci_jiaocairuanzhu_member_score表
        if (memberScores != null && !memberScores.isEmpty()) {
            try {
                // 先删除旧的积分记录
                sciJiaocairuanzhuMemberScoreMapper.deleteSciJiaocairuanzhuMemberScoreByJiaocairuanzhuId(sciJiaocairuanzhu.getId());
                
                // 添加新的积分记录
                com.alibaba.fastjson.JSONArray scoresArray = com.alibaba.fastjson.JSON.parseArray(memberScores);
                List<com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore> scoreList = new ArrayList<>();
                for (int i = 0; i < scoresArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject scoreObj = scoresArray.getJSONObject(i);
                    com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore memberScore = new com.ruoyi.system.domain.SciJiaocairuanzhuMemberScore();
                    memberScore.setJiaocairuanzhuId(sciJiaocairuanzhu.getId());
                    memberScore.setUserId(scoreObj.getString("userId"));
                    memberScore.setScore(scoreObj.getString("score"));
                    memberScore.setRanking(String.valueOf(i + 1));
                    scoreList.add(memberScore);
                }
                if (!scoreList.isEmpty()) {
                    sciJiaocairuanzhuMemberScoreMapper.insertSciJiaocairuanzhuMemberScoreBatch(scoreList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return toAjax(result);
    }

    /**
     * 删除教材软著
     * 批量删除教材软著数据
     */
    @RequiresPermissions("system:jiaocairuanzhu:remove")
    @Log(title = "教材软著", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciJiaocairuanzhuService.deleteSciJiaocairuanzhuByIds(ids));
    }



//    @RequiresPermissions("system:jiaocairuanzhu:process","system:jiaocairuanzhu:info")

    /**
     * 跳转到教材软著详情页面
     * 用于批阅和查看教材软著详情
     */
    //批阅
    @RequiresPermissions(value={"system:jiaocairuanzhu:process","system:jiaocairuanzhu:info"},logical= Logical.OR)
    @Log(title = "教材软著详情页面", businessType = BusinessType.OTHER)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        // 创建一个初始化了params的SysUser对象，避免MyBatis参数解析错误
        SysUser user = new SysUser();
        user.setParams(new HashMap<String, Object>());
        List<SysUser> userList1 = userService.selectUserList(user);
        sciJiaocairuanzhu.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);
        return prefix + "/detail";
    }





    /**
     * 教材软著审核通过
     * 根据不同的审核阶段更新状态并记录审批记录
     */
    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue","system:jiaocairuanzhu:info"},logical= Logical.OR)
    @Log(title = "教材软著审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag, String amount, SciProjectScoreCfg sciProjectScoreCfg)
    {
        return toAjax(sciJiaocairuanzhuService.hxPass(id, getUserId(), urlFlag));
    }


    /**
     * 教材软著审核驳回
     * 根据不同的审核阶段驳回并记录驳回原因
     */
    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "教材软著被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id, String remark, String urlFlag)
    {
        return toAjax(sciJiaocairuanzhuService.hxBh(id, getUserId(), remark, urlFlag));
    }





    /**
     * 跳转到教材软著撤回页面
     * 用于撤回已提交的教材软著申请
     */
    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "教材软著", businessType = BusinessType.OTHER)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(id);
        List<SysUser> userList1 =  userService.selectUserList(null);
        // 获取教材著作成员列表
        List<com.ruoyi.system.domain.SciJiaocairuanzhuMember> members = sciJiaocairuanzhuService.getJiaocairuanzhuMembers(id);
        mmap.put("sysUsers1",userList1);
        mmap.put("sciJiaocairuanzhu", sciJiaocairuanzhu);
        mmap.put("members", members);
        return prefix + "/recall";
    }


    /**
     * 保存教材软著撤回操作
     * 撤回已提交的教材软著申请并记录撤回原因
     */
    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id, String state, String remark, String urlFlag)
    {
        return toAjax(sciJiaocairuanzhuService.recall(id, state, getUserId(), remark, urlFlag));
    }


    /**
     * 获取教材软著驳回原因
     * 根据教材著作ID查询对应的驳回记录
     */
    @RequiresPermissions(value={"system:jiaocairuanzhu:hecha","system:jiaocairuanzhu:process","system:jiaocairuanzhu:edit","system:jiaocairuanzhu:chayue"},logical= Logical.OR)
    @Log(title = "教材软著", businessType = BusinessType.OTHER)
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SciJiaocairuanzhuPiyue ob = new SciJiaocairuanzhuPiyue();
        ob.setJiaocai_id(kid);
        List<SciJiaocairuanzhuPiyue> list = piyueService.selectSciJiaocairuanzhuPiyueList(ob);
        return getDataTable(list);
    }




}

