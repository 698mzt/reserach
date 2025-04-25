package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.mapper.SciCollegeResearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collegeresearch")
public class SciCollegeResearch extends BaseController {
    @Autowired
    private SciCollegeResearchMapper sciCollegeResearchMapper;
    private String prefix = "/system/collegeresearch";
//为了导出写的
//    public  List<Map<String, Object>> list;
    @GetMapping
    public String index()
    {
        return prefix + "/index";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {

        Long deptId = getSysUser().getDeptId();

        System.out.println("deptId!@!#@!#@ = " + deptId);
        System.out.println("deptIdtoString = " + deptId.toString());

        List<Map<String, Object>> GeneraList = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> listout = new ArrayList<>();

        if ("1191".equals((deptId).toString())) {
            //System.out.println("deptIddawdadadwad = " + deptId);
            GeneraList = sciCollegeResearchMapper.GeneralCollegeSearcherList();
        } else {
            //查下属部门全部的教研室
            listout = sciCollegeResearchMapper.selectCollegeResearchID(deptId);
            //System.out.println("listout = " + listout);

            list = sciCollegeResearchMapper.selectCollegeResearch(deptId);
            //System.out.println("list!!!!!!!!!! = " + list);
            TableDataInfo data = getDataTable(list);
        }
        new TableDataInfo();
        TableDataInfo data;

        if(GeneraList!=null){
            System.out.println("进来了");
                data = getDataTable(GeneraList);
                data.setRows(GeneraList); // 数据列表;
                data.setTotal(GeneraList.size()); // 总记录数
            System.out.println("GeneraList = " + GeneraList);

            //"奖励-积分"
            int jl=0;
            //纵向课题科研项目-校级
            int q=0;
            //教材软著-积分
            int w=0;

            for (int i=0; i<GeneraList.size(); i++){
                if(GeneraList.get(i).get("学院名称").equals("软件学院")){

                    System.out.println(" 先输出一下"+GeneraList.get(i).get("学院名称"));
                };
            }


        }
        else {
                System.out.println("没有");
                data = getDataTable(list);
                data.setRows(list); // 数据列表
                data.setTotal(list.size()); // 总记录数
        }
        //全部的教研室 N
        //这个是之前没有查出所有的部门所写的循环，有跳过，没有添加进去当然也没有数据，不过在前端设置了默认值0
        // 优化了SQL语句后，这个循环应该似乎没有意义了
//        for (Map<String, Object> map : listout) {//循环全部教研室，获取每一个的部门名
//            String dept_name = (String) map.get("dept_name");
//            Long dept_id_long = (Long) map.get("dept_id"); // 获取 Long 类型的 dept_id
//            String dept_id = String.valueOf(dept_id_long); // 将 Long 类型转换为 String 类型
//            System.out.println("dept_id = " + dept_id);
//
//            boolean isMatched = false; // 标记是否匹配
//            for (Map<String, Object> map1 : list) {//循环当前有数据的list
//                System.out.println("map1.get(\"专业\") = " + map1.get("专业"));
//                String dept_name1 = String.valueOf(map1.get("专业"));
//                if (dept_id.equals(dept_name1)) { //判断是否匹配
//                    isMatched = true;
//                    break;
//                }
//            }
//            if (!isMatched) { // 如果没有匹配到，则向 list 添加 "专业" 键值对
//                Map<String, Object> newMap = new HashMap<>();
//                newMap.put("学院名称", map.get("学院名称"));
//                newMap.put("专业", dept_id);
//                newMap.put("专业名称", dept_name); // 向新的 Map 添加键值对
//                list.add(newMap); // 将新的 Map 添加到 list 中
//                System.out.println("newMap = " + newMap);
//            }
//        }
        System.out.println("list = " + data.getRows());
        return data;
    }
//个是之前导出的一个方法，需要实体类
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export()
//    {
//        list = getList();
//        List list1 = new ArrayList<>();
//        System.out.println("!!#@!#$#@%#%list = " + list);
//
//        for ( int i= 0 ; i < list.size(); i++){
////            list1.add(list.get(i));
////            System.out.println("list1 = " + list1);
//        }
//        Long deptId = getSysUser().getDeptId();
//        List<SciPA> sciPA = sciCollegeResearchMapper.selectCollegeResearchList(deptId);
//        ExcelUtil<SciPA> util = new ExcelUtil<>(SciPA.class);
//        return util.exportExcel(sciPA, "学院科研");
//
//
//    }
//
//    public List<Map<String, Object>> getList() {
//        return list;
//    }
//
//    public void setList(List<Map<String, Object>> list) {
//        this.list = list;
//    }
}
