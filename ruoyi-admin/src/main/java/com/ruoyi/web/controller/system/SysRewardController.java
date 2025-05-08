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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


    @RequiresPermissions("system:reward:view")
    @GetMapping()
    public String reward()
    {
        return prefix + "/reward";
    }

    /**
     * 查询奖励列表
     */
    @RequiresPermissions("system:reward:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String time,SysReward sysReward, @RequestParam(value = "dname", required = false) String dname)
    {
//        sysReward.setRewardTime(time);
        sysReward.setDname(dname);
        sysReward.setUid(getUserId());
        startPage();
//        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);
//        return getDataTable(list);


        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        for (SysRole r :roles){
            if(r.getRoleKey().equals("sci_tesearch")){
                role ="sci_tesearch";
                break;
            }else if (r.getRoleKey().equals("dept_teacher")){
                role="dept_teacher";
                break;
            }
            else if (r.getRoleKey().equals("research")){
                role="research";
                break;
            }
        }
        sysReward.setRole(role);
        List<SysReward> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
//            switch (tableId){
//                case "bootstrap-table0":
//                    list = sysRewardService.selectSysRewardListByOVER(sysReward);
//                    break;
//                case "bootstrap-table1":
            list = sysRewardService.selectSysRewardListByKYC(sysReward);
//                    break;
//                case "bootstrap-table2":
//                    list = sysRewardService.selectSysRewardListByOverRewardKYC(sysReward);
//                    break;
//            }
        }
        //学院负责人
        else if(role.equals("dept_teacher")){
            list = sysRewardService.selectSysRewardListByXUE(sysReward);
        }
//        教研室
        else if(role.equals("research")){
//            switch (tableId) {
//                case "bootstrap-table0":
//                    list = sysRewardService.selectSysRewardListByOVER(sysReward);
//                    break;
//                case "bootstrap-table1":
            list = sysRewardService.selectSysRewardListByJYS(sysReward);
//                    break;
//                case "bootstrap-table2":
//                    list = sysRewardService.selectSysRewardListByOverRewardJYS(sysReward);
//                    break;
//            }
        }
        //普通教师
        else{
//            switch (tableId) {
//                case "bootstrap-table0":
//                    list = sysRewardService.selectSysRewardListByOVER(sysReward);
//                    break;
//                case "bootstrap-table1":
            list = sysRewardService.selectSysRewardList(sysReward);
//                    break;
//                case "bootstrap-table2":
//                    list = sysRewardService.selectSysRewardListByOverReward(sysReward);
//                    break;
//            }
        }

        TableDataInfo data= getDataTable(list);

        return data;
    }

    /**
     * 导出奖励列表
     */
    @RequiresPermissions("system:reward:export")
    @Log(title = "奖励", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysReward sysReward) {
        List<SysReward> list = sysRewardService.selectSysRewardList(sysReward);
        List<SysRole> roles = getSysUser().getRoles();
        String role = "";
        for (SysRole r : roles){
            if(r.getRoleKey().equals("sci_tesearch")){
                role ="sci_tesearch";
                break;
            }else if (r.getRoleKey().equals("dept_teacher")){
                role="dept_teacher";
                break;
            }
            else if (r.getRoleKey().equals("research")){
                role="research";
                break;
            }
        }

        List<SysReward> list1;
        if(role.equals("sci_tesearch")){
            list1 = sysRewardService.selectSysRewardListByKYC(sysReward);
        }else if(role.equals("dept_teacher")){
            list1 = sysRewardService.selectSysRewardListByXUE(sysReward);
        }else if(role.equals("research")){
            list1 = sysRewardService.selectSysRewardListByJYS(sysReward);
        }else{
            list1 = sysRewardService.selectSysRewardList(sysReward);
        }
        // 使用 getDictLabel 方法转换 rewardPaiming 字段为对应的字典标签
        for (SysReward reward : list) {
            if (reward.getRewardPaiming() != null) {
                String label = getDictLabel("sys_reward_paiming", reward.getRewardPaiming());
                reward.setRewardPaiming(label);  // 假设你允许直接修改原字段
            }
            if (reward.getRewardFenlei() != null) {
                String label = getDictLabel("sys_reward_fenlei", reward.getRewardFenlei());
                reward.setRewardFenlei(label);  // 假设你允许直接修改原字段
            }
            if (reward.getRewardDengji() != null) {
                String label = getDictLabel("sys_reward_dengji", reward.getRewardDengji());
                reward.setRewardDengji(label);  // 假设你允许直接修改原字段
            }
            if (reward.getState() != null) {
                String label = getDictLabel("sys_reward_sg", reward.getState());
                reward.setState(label);  // 假设你允许直接修改原字段
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
     * 新增保存奖励
     */
    @RequiresPermissions("system:reward:add")
    @Log(title = "奖励", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysReward sysReward)
    {
        sysReward.setState("11");
        return toAjax(sysRewardService.insertSysReward(sysReward));
    }

    /**
     * 提交申请进行审批
     */
    @RequiresPermissions("system:apply:add")
    @Log(title = "申请横向课题", businessType = BusinessType.INSERT)
    @PostMapping("/push/{id}")
    @ResponseBody
    @Transactional
    public AjaxResult push(SysReward sysReward)
    {
        String state = "1";
        sysReward.setState(state);
        sysReward.setUserId(getSysUser().getUserId());
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
        mmap.put("sysReward", sysReward);
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
        sysReward.setState("11");
        return toAjax(sysRewardService.updateSysReward(sysReward));
    }

    /**
     * 删除奖励
     */
    @RequiresPermissions("system:reward:remove")
    @Log(title = "奖励", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysRewardService.deleteSysRewardByIds(ids));
    }

    //    bhyy审批记录
    @RequiresPermissions("system:reward:edit")
    @PostMapping("/bhyy/{kid}")
    @ResponseBody
    public TableDataInfo bhyy(@PathVariable("kid")Integer kid)
    {
        SysRewardPiyue ob = new SysRewardPiyue();
        ob.setRewardId(kid);
        List<SysRewardPiyue> list = piyueService.selectSysRewardPiyueList(ob);
        return getDataTable(list);
    }

    //跳转批阅界面
    @RequiresPermissions("system:reward:info")
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Integer id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(Long.valueOf(id));
        List<SysUser> userList1 =  userService.selectAllUser();
        sysReward.setUrlFlag(urlFlag);
        mmap.put("sysUsers1",userList1);
        mmap.put("sysReward", sysReward);
        return prefix + "/detail";
    }

    @RequiresPermissions(value={"system:reward:hecha","system:reward:process","system:reward:chayue"},logical= Logical.OR)
    @Log(title = "奖励审核通过", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxPass")
    @ResponseBody
    public AjaxResult hxPass(String id,String urlFlag)
    {

        return toAjax(sysRewardService.hxPass(id,getUserId(),urlFlag));
    }

    @RequiresPermissions(value={"system:reward:hecha","system:reward:process","system:reward:chayue"},logical= Logical.OR)
    @Log(title = "奖励被驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/hxBh")
    @ResponseBody
    public AjaxResult hxBh(String id,String remark,String urlFlag)
    {

        return toAjax(sysRewardService.hxBh(id,getUserId(),remark,urlFlag));
    }



    @RequiresPermissions(value={"system:reward:hecha","system:reward:process","system:reward:chayue"},logical= Logical.OR)
    @GetMapping("/recall/{id}")
    public String recall(@PathVariable("id") Integer id, ModelMap mmap)
    {
        SysReward sysReward = sysRewardService.selectSysRewardById(Long.valueOf(id));
        List<SysUser> userList1 =  userService.selectAllUser();
        mmap.put("sysUsers1",userList1);
        mmap.put("sysReward", sysReward);
        return prefix + "/recall";
    }
    @RequiresPermissions(value={"system:reward:hecha","system:reward:process","system:reward:chayue"},logical= Logical.OR)
    @Log(title = "撤销", businessType = BusinessType.UPDATE)
    @PostMapping( "/recallsave")
    @ResponseBody
    public AjaxResult recallSave(Integer id,String state,String remark,String urlFlag)
    {
        return toAjax(sysRewardService.recall(id,state,getUserId(),remark,urlFlag));
    }
}
