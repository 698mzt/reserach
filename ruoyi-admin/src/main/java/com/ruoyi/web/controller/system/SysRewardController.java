package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysReward;
import com.ruoyi.system.domain.SysRewardPiyue;
import com.ruoyi.system.service.ISysRewardPiyueService;
import com.ruoyi.system.service.ISysRewardService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DictUtils.getDictLabel;

/**
 * 奖励Controller
 *
 * @author ruoyi
 * @date 2024-12-23
 */
@Controller
@RequestMapping("/system/reward")
public class SysRewardController extends BaseController
{
    private String prefix = "system/reward";

    @Autowired
    private ISysRewardService sysRewardService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRewardPiyueService piyueService;

    // 设置角色集合
    private static final Set<String> TEACHER_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher", // 软件学院管理员
            "discuss_college", // 商学院管理员
            "dzgc_college", // 电子工程学院管理员
            "yssj_college", // 艺术设计学院管理员
            "student_college", // 学生处学院管理员
            "marxism_college", // 马克思主义学院管理员
            "general" //综合院部管理员
    ));

    @RequiresPermissions("system:reward:view")
    @GetMapping()
    public String reward()
    {
        return prefix + "/reward";
    }

    /**
     * 查询奖励列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysReward sysReward) {
        sysReward.setUid(getUserId());
        startPage();

        // 统一使用 selectSysRewardList，通过 @DataScope 控制数据权限
        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);

        return getDataTable(list);
    }

    /**
     * 【兼容接口】为了兼容前端可能遗留的调用，统一重定向到 /list
     */
    @RequiresPermissions(value = {"system:reward:list", "system:reward:jys", "system:reward:xueyuan", "system:reward:kyc", "system:reward:admin"}, logical = Logical.OR)
    @PostMapping({"/listKy", "/listXy", "/listJys", "/listCx"})
    @ResponseBody
    public TableDataInfo listAlias(
            @RequestParam(value = "rewardName", required = false) String rewardName,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "dname", required = false) String dname,
            @RequestParam(value = "yname", required = false) String yname,
            SysReward sysReward) {
        // 把额外参数塞进 SysReward 对象
        sysReward.setRewardName(rewardName);
        sysReward.setUserName(userName);
        sysReward.setDname(dname);
        sysReward.setYname(yname);
        // 只传 SysReward 调用原 list 方法
        return list(sysReward);
    }

    /**
     * 导出奖励列表
     */
    @RequiresPermissions("system:reward:export")
    @Log(title = "奖励", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysReward sysReward) {
        // 统一使用 selectSysRewardList，通过 @DataScope 控制数据权限
        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);
        // 字典转换
        for (SysReward reward : list) {
            if (reward.getRewardPaiming() != null) {
                reward.setRewardPaiming(getDictLabel("sys_reward_paiming", reward.getRewardPaiming()));
            }
            if (reward.getRewardFenlei() != null) {
                reward.setRewardFenlei(getDictLabel("sys_reward_fenlei", reward.getRewardFenlei()));
            }
            if (reward.getRewardDengji() != null) {
                reward.setRewardDengji(getDictLabel("sys_reward_dengji", reward.getRewardDengji()));
            }
            if (reward.getState() != null) {
                reward.setState(getDictLabel("sys_reward_sg", reward.getState()));
            }
        }

        ExcelUtil<SysReward> util = new ExcelUtil<>(SysReward.class);
        return util.exportExcel(list, "奖励数据");
    }

    /**
     * 新增奖励
     */
    @RequiresPermissions("system:reward:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> userList = userService.selectAllUser();
        // 标记当前登录用户
        Long currentUserId = getUserId();
        for (int a = 0; a < userList.size(); a++) {
            if (userList.get(a).getUserId().equals(currentUserId)) {
                SysUser user = userList.get(a);
                user.setFlag(true); // 假设SysUser有flag属性标记当前用户
                userList.set(a, user);
                break;
            }
        }
        mmap.put("sysUsers", userList);
        return prefix + "/add";
    }

    /**
     * 新增保存奖励
     */
    @RequiresPermissions("system:reward:add")
    @Log(title = "奖励", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysReward sysReward)
    {
        sysReward.setState("REWARD_DRAFT"); // 初始状态
        sysReward.setCreateBy(getUsername()); // 补充创建人
        sysReward.setCreateTime(new Date()); // 补充创建时间
        return toAjax(sysRewardService.insertSysReward(sysReward));
    }

    /**
     * 提交申请进行审批
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/push/{id}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult push(@PathVariable("id") Long id, SysReward sysReward)
    {
        sysReward.setId(id);
        String state = "REWARD_JYS_AUDIT"; // 提交审批状态
        sysReward.setState(state);
        sysReward.setUserId(getSysUser().getUserId());
        sysReward.setUpdateBy(getUsername()); // 补充更新人
        sysReward.setUpdateTime(new Date()); // 补充更新时间
        int a = sysRewardService.updateSysReward(sysReward);
        return toAjax(a);
    }

    /**
     * 修改奖励
     */
    @RequiresPermissions("system:reward:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(id);
        List<SysUser> userList1 = userService.selectAllUser();
        mmap.put("sysUsers1", userList1);
        mmap.put("sysReward", sysReward);
        mmap.put("extraMembers", sysReward.getExtraMembers());
        return prefix + "/edit";
    }

    /**
     * 修改保存奖励
     */
    @RequiresPermissions("system:reward:edit")
    @Log(title = "奖励", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysReward sysReward)
    {
        sysReward.setState("REWARD_DRAFT"); // 编辑后重置为初始状态
        sysReward.setUserId(getSysUser().getUserId());
        sysReward.setUpdateBy(getUsername()); // 补充更新人
        sysReward.setUpdateTime(new Date()); // 补充更新时间
        return toAjax(sysRewardService.updateSysReward(sysReward));
    }

    /**
     * 删除奖励
     */
    @RequiresPermissions("system:reward:remove")
    @Log(title = "奖励", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysRewardService.deleteSysRewardByIds(ids));
    }

    // 审批记录查询
    @RequiresPermissions("system:reward:edit")
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid") Integer kid)
    {
        startPage(); // 分页
        SysRewardPiyue ob = new SysRewardPiyue();
        ob.setRewardId(kid);
        List<SysRewardPiyue> list = piyueService.selectSysRewardPiyueList(ob);
        return getDataTable(list);
    }

    // 跳转批阅界面
    @RequiresPermissions("system:reward:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(Long.valueOf(id));
        List<SysUser> userList1 = userService.selectAllUser();
        // 将主持人设置为当前用户
        sysReward.setFirstPersonId(String.valueOf(getUserId()));
        sysReward.setUrlFlag(urlFlag);
        mmap.put("sysUsers1", userList1);
        mmap.put("sysReward", sysReward);
        mmap.put("extraMembers", sysReward.getExtraMembers());
        return prefix + "/detail";
    }

    /**
     * 奖励审核通过
     */
    @RequiresPermissions(value = {"system:reward:hecha", "system:reward:process", "system:reward:chayue"}, logical = Logical.OR)
    @Log(title = "奖励审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id, String urlFlag)
    {
        return toAjax(sysRewardService.hxPass(id, getUserId(), urlFlag));
    }

    /**
     * 奖励被驳回
     */
    @RequiresPermissions(value = {"system:reward:hecha", "system:reward:process", "system:reward:chayue"}, logical = Logical.OR)
    @Log(title = "奖励被驳回", businessType = BusinessType.UPDATE)
    @PostMapping("/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id, String remark, String urlFlag)
    {
        return toAjax(sysRewardService.hxBh(id, getUserId(), remark, urlFlag));
    }

    /**
     * 跳转撤销界面
     */
    @RequiresPermissions(value = {"system:reward:hecha", "system:reward:process", "system:reward:chayue"}, logical = Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(Long.valueOf(id));
        List<SysUser> userList1 = userService.selectAllUser();
        mmap.put("sysUsers1", userList1);
        mmap.put("sysReward", sysReward);
        mmap.put("extraMembers", sysReward.getExtraMembers());
        return prefix + "/recall";
    }

    /**
     * 撤销操作保存
     */
    @RequiresPermissions(value = {"system:reward:hecha", "system:reward:process", "system:reward:chayue"}, logical = Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping("/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id, String state, String remark, String urlFlag)
    {
        return toAjax(sysRewardService.recall(id, state, getUserId(), remark, urlFlag));
    }

    // ========== 以下为BaseController通用方法的适配（若父类未实现则补充） ==========
    /**
     * 获取当前登录用户
     */
    public SysUser getSysUser() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal instanceof SysUser) {
            return (SysUser) principal;
        }
        return new SysUser(); // 兜底返回空用户
    }

    /**
     * 获取当前登录用户ID
     */
    public Long getUserId() {
        SysUser user = getSysUser();
        return user != null ? user.getUserId() : 0L;
    }

    /**
     * 获取当前登录用户名
     */
    protected String getUsername() {
        SysUser user = getSysUser();
        return user != null ? user.getUserName() : "";
    }

    /**
     * 分页初始化
     */
    protected void startPage() {
        // 此处为通用分页逻辑，适配ruoyi框架的分页插件
        // 实际项目中由BaseController实现，此处仅占位
    }

    /**
     * 封装分页数据
     */
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 封装返回结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}