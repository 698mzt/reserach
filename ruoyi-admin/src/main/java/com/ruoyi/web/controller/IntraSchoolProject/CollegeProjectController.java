package com.ruoyi.web.controller.IntraSchoolProject;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.service.ICollegeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/CollegeProject")
public class CollegeProjectController extends BaseController {
    private String prefix = "system/CollegeProject";

    @Autowired
    private ICollegeProjectService collegeProjectService;
    @GetMapping("")
    String view() {
        System.out.println("CollegeProjectController.view");
        return prefix + "/view";
    }
//http://localhost:8081/CollegeProject/list
    @PostMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> list() {
        System.out.println("CollegeProjectController.list");
        Long DeptId = getSysUser().getDeptId();
        List<Map<String, Object>> lists = new ArrayList<>();
        System.out.println("judge(DeptId)=" + collegeProjectService.judge(DeptId,getLoginName()));

        //判断是否为学院，admin
        if (collegeProjectService.judge(DeptId,getLoginName())) {
            //查询所有本学院项目
            lists=collegeProjectService.selAll(DeptId);
            //System.out.println("lists = " + lists);
            //将 lists 中的字节数组转换为字符串
            lists = convertByteArraysToString(lists);

        }
        for (Map<String, Object> list : lists) {
            System.out.println("list = " + list);
        }


//        Map<String, Object> result = new HashMap<>();
//        for (Map<String, Object> map : lists) {
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                System.out.println("key="+key+":" +"existingValue = " + value +value.getClass());
//                if (result.containsKey(key)) {
//                    Object existingValue = result.get(key);
//
//                    if (existingValue instanceof Integer) {
//                        int sum = (int) existingValue + (int) value;
//                        result.put(key, sum);
//                    } else if (existingValue instanceof String) {
//                        // 处理字符串类型的总计
//                        String parts1 = ((String) existingValue).split("个")[0].trim();
//                        String parts2 = ((String) value).split("个")[0].trim();
//                        int num1 = Integer.parseInt(parts1);
//                        int num2 = Integer.parseInt(parts2);
//                        int sumNum = num1 + num2;
//
//                        parts1 = ((String) existingValue).split("万")[0].split("\\(")[1].trim();
//                        parts2 = ((String) value).split("万")[0].split("\\(")[1].trim();
//                        double amount1 = Double.parseDouble(parts1);
//                        double amount2 = Double.parseDouble(parts2);
//                        double sumAmount = amount1 + amount2;
//
//                        String newStr = sumNum + " 个 ( " + sumAmount + " 万 )";
//                        result.put(key, newStr);
//                    }
//                } else {
//                    result.put(key, value);
//                }
//            }
//        }
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> currentMap : lists) {
            for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                //System.out.println("key=" + key + ":" + "existingValue = " + value + value.getClass());
                if (result.containsKey(key)) {
                    Object existingValue = result.get(key);
                    if (existingValue instanceof Integer) {
                        int sum = (int) existingValue + (int) value;
                        result.put(key, sum);
                    } else if (existingValue instanceof String) {
                        // 处理字符串类型的总计
                        String parts1 = ((String) existingValue).split("个")[0].trim();
                        String parts2 = ((String) value).split("个")[0].trim();
                        int num1 = Integer.parseInt(parts1);
                        int num2 = Integer.parseInt(parts2);
                        int sumNum = num1 + num2;

                        parts1 = ((String) existingValue).split("万")[0].split("\\(")[1].trim();
                        parts2 = ((String) value).split("万")[0].split("\\(")[1].trim();
                        double amount1 = Double.parseDouble(parts1);
                        double amount2 = Double.parseDouble(parts2);
                        double sumAmount = amount1 + amount2;

                        String newStr = sumNum + " 个 ( " + sumAmount + " 万 )";
                        result.put(key, newStr);
                    } else if (existingValue instanceof BigDecimal) {
                        BigDecimal sum = ((BigDecimal) existingValue).add((BigDecimal) value);
                        result.put(key, sum);
                    }
                } else {
                    result.put(key, value);
                }
            }
        }

        //result.forEach((key, value) -> System.out.println(key + " = " + value));

        result.put("cgzhdeptId","总计");
        lists.add(result);
        return lists;
    }

    /**
     * 将 lists 中的字节数组转换为字符串
     * @param lists 包含 Map 的列表
     * @return 处理后的列表
     */
    public static List<Map<String, Object>> convertByteArraysToString(List<Map<String, Object>> lists) {
        for (Map<String, Object> list : lists) {
            for (Map.Entry<String, Object> entry : list.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof byte[]) {
                    // 指定字符编码为 UTF-8
                    String strValue = new String((byte[]) value, StandardCharsets.UTF_8);
                    entry.setValue(strValue);
                }
            }
        }
        return lists;
    }
}
