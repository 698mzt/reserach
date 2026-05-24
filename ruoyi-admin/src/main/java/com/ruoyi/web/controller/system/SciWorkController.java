package com.ruoyi.web.controller.system;

//学校科研工作量

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/work")
public class SciWorkController extends BaseController {
    private String prefix = "system/statistic";

    @GetMapping
    public String index()
    {
        return prefix + "/kygzlXX";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo all_list() {

        Long deptId = getSysUser().getDeptId();
        List<Map<String, Object>> GeneraList = new ArrayList<>();

//        GeneraList = sciCollegeResearchMapper.GeneralCollegeSearcherList();
        new TableDataInfo();
        TableDataInfo data;
        data = getDataTable(GeneraList);data.setRows(GeneraList); // 数据列表;
        data.setTotal(GeneraList.size()); // 总记录数

        System.out.println("GeneraList = " + GeneraList);
        System.out.println("list = " + data.getRows());
        return data;
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] headers = {"学院名称", "专业名称", "横向课题科研项目", "纵向课题科研项目-校级以上", "纵向课题科研项目-校级", "论文-积分", "讲座报告-积分", "教材软著-积分", "成果转化-积分", "专利-积分", "软著-积分", "奖励-积分"};
        String[] fieldKeys = {"parentName", "deptName", "横向课题科研项目", "纵向课题科研项目-校级以上", "纵向课题科研项目-校级", "论文-积分", "讲座报告-积分", "教材软著-积分", "成果转化-积分", "专利-积分", "软著-积分", "奖励-积分"};
        return MapDataExcelUtil.exportExcel(list, headers, fieldKeys, "学校科研工作量");
    }
}
