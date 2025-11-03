package com.ruoyi.web.controller.IntraSchoolProject;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/StatsQuery")
public class StatsQueryController extends BaseController {
    private String prefix = "system/StatsQuery";
    @Autowired // 纵向课题 1
    private ISciHorizontalApplyVerticalService sciHorizontalApplyVerticalService;
    @Autowired // 横向课题 2
    private ISciHorizontalApplyService sciHorizontalApplyService;
    @Autowired // 成果转化 3
    private ISciIntraSchProApplyService sciIntraSchProApplyService;
    @Autowired // 论文 4
    private ISciPaperAService sciPaperAService;
    @Autowired // 教材专著 5 赵威翰
    private ISciJiaocairuanzhuService sciJiaocairuanzhuService;
    @Autowired // 专利软著 6 雷
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;
    @Autowired // 奖励 7 15204
    private ISysRewardService sysRewardService;
    @Autowired // 讲座报告Service接口 8
    private ISciLectureReportService sciLectureReportService;


    @GetMapping("")
    String view(Model model) {
        model.addAttribute("data", "123");

        return prefix + "/view";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestParam Map<String, String> params)
    {
        System.out.println("params = " + params);
        
        // 如果前端传递了noData标志，说明是初始加载，返回空数据
        if ("true".equals(params.get("noData"))) {
            return getDataTable(new ArrayList<>());
        }
        
        // 获取项目类别参数
        String remark = params.get("remark");
        System.out.println("项目类别: " + remark);
        
        startPage();
        List<Object> list = new ArrayList<>();
        
        // 根据项目类别返回不同的数据
        if (remark != null && !remark.isEmpty()) {
            if ("1".equals(remark)) {
                // 获取项目类别1的数据（纵向课题）
                List<SciHorizontalApplyVertical> statsQuery = sciHorizontalApplyVerticalService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if ("2".equals(remark)) {
                // 获取项目类别2的数据（横向课题）
                List<SciHorizontalApply> statsQuery = sciHorizontalApplyService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if ("3".equals(remark)) {
                // 查询项目类别3的数据（成果转化）
                List<SciIntraSchoolPro> statsQuery = sciIntraSchProApplyService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if("4".equals(remark)){
                // 获取项目类别4的数据（论文）
                List<SciPaperA> statsQuery = sciPaperAService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if("5".equals(remark)){
                // 获取项目类别5的数据（教材专著）
                List<SciJiaocairuanzhu> statsQuery = sciJiaocairuanzhuService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if("6".equals(remark)){
                // 获取项目类别6的数据（专利软著）
                List<SciZhuanliruanzhu> statsQuery = sciZhuanliruanzhuService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if("7".equals(remark)){
                // 获取项目类别7的数据（奖励）
                List<SysReward> statsQuery = sysRewardService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }else if("8".equals(remark)){
                // 获取项目类别8的数据（讲座报告）
                List<SciLectureReportOpinion> statsQuery = sciLectureReportService.getStatsQuery(params);
                return getDataTable(statsQuery);
            }
        } else {
            // 如果没有选择项目类别，返回空数据
            list = new ArrayList<>();
        }
        
        return getDataTable(list);
    }

}
