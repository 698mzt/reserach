package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SciPaperCfg;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IsciPaperACfgService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 论文得分配置Controller
 */
@Controller
@RequestMapping("/system/papercfg")
 public class SciPaperAcfgController extends BaseController {
    private String prefix = "system/papercfg";

    @Autowired
    private IsciPaperACfgService isciPaperACfgService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISciPaperAService iSciPaperAService;

    @RequiresPermissions("system:papercfg:view")
    @GetMapping()
    public String papercfg()
    {
        return prefix + "/papercfg";
    }

    /**
     * 查询论文得分列表
     */
    @RequiresPermissions("system:papercfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciPaperCfg sciPaperCfg)
    {
       Integer userId= Math.toIntExact(getUserId());
       sciPaperCfg.getId();

       List<SciPaperCfg> list = new ArrayList<>();
       list.addAll(isciPaperACfgService.selectSciPaperACfg(userId));
       return getDataTable(list);
    }

    /**
    * 新增积分
     * */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        //获取全部的user信息
        List<SysUser> userList =  userService.selectAllUser();
        SciPaperA sciPaperAList = new SciPaperA();
        List<SciPaperA> sciPaperA =iSciPaperAService.selectSciPaperAList(sciPaperAList);

        mmap.put("user",userList);


        return prefix + "/add";
    }
   @RequiresPermissions("system:papercfg:add")
   @Log(title = "论文积分", businessType = BusinessType.INSERT)
   @PostMapping("/add")
   @ResponseBody
   public AjaxResult addSave(SciPaperCfg sciPaperAcfg)
   {

      String user_name = userService.selectUserByLoginName(getLoginName()).getUserName();
       System.out.println("sciPaperAcfg = " + sciPaperAcfg);

      return toAjax(isciPaperACfgService.insertSciPaperCfg(sciPaperAcfg));
   }

}
