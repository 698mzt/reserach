package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.service.OverViewService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 课题概览Controller
 *
 * @author 14740
 * @date 2025-01-5
 */
@Controller
@RequestMapping("/system/overView")
public class SysOverViewController extends BaseController {
    private String prefix = "system/overView";

    @Autowired
    private OverViewService overViewService;

    @RequiresPermissions("system:overView:view")
    @GetMapping()
    public String overView()
    {
        return prefix + "/view";
    }

    @RequiresPermissions("system:overView:view")
    @PostMapping("/list/{pname}")
    @ResponseBody
    public TableDataInfo list(String year, @PathVariable("pname")String pname, SciHorizontalApply sciHorizontalApply, SciHorizontalApplyVertical sciHorizontalApplyVertical)
    {
        startPage();
        System.out.println(pname);
        sciHorizontalApply.setUid(getUserId());
        List<?> list = new ArrayList<>();
        if (pname.equals("横向课题")){
            list = overViewService.selectOtherListByUid(sciHorizontalApply);
        }
        if (pname.equals("纵向课题")){
            list = overViewService.selectOtherListByUid2(sciHorizontalApplyVertical);
        }

        TableDataInfo data = getDataTable(list);
        return data;
    }
}
