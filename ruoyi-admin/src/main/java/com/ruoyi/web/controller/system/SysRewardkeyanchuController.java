package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.IStatisticYJCGSService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rewardkeyanchu")
public class SysRewardkeyanchuController extends BaseController {

    @Autowired
    private IStatisticYJCGSService statisticYJCGSService;

    private String prefix = "system/statistic";

    @RequiresPermissions("statistic:achievement:view")
    @GetMapping()
    public String apply(Model model) {
        model.addAttribute("modalName", "科研处业绩成果数");
        model.addAttribute("searchMode", "teacherName");
        return prefix + "/yjcgKYC";
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        List<Map<String, Object>> list;
        if (getSysUser().isAdmin()) {
            list = statisticYJCGSService.selectAllYJCGSKYC(null);
        } else {
            list = statisticYJCGSService.selectYJCGSKYCByDept(null);
        }
        return getDataTable(list);
    }

    @RequiresPermissions("statistic:achievement:view")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        List<Map<String, Object>> list = statisticYJCGSService.selectYJCGSKYCByDept(null);
        String[] headers = {"学院", "专业", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)"};
        String[] fieldKeys = {"parentName", "deptName", "纵向科研项目-校级以上", "纵向科研项目-校级", "横向课题科研项目", "成果转化", "学术论文", "教材著作", "专利", "软著", "奖励", "学术报告(讲座类)"};
        return MapDataExcelUtil.exportExcel(list, headers, fieldKeys, "科研处业绩成果数");
    }
}
