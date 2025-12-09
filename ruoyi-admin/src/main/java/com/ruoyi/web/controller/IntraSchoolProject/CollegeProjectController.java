package com.ruoyi.web.controller.IntraSchoolProject;

import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.service.IAlltotleService;
import com.ruoyi.system.service.ICollegeProjectService;
import com.ruoyi.system.service.ISysUserService;
import com.zaxxer.hikari.util.FastList;
import io.swagger.models.auth.In;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.system.service.ISysDictTypeService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.config.datasource.DynamicDataSourceContextHolder.log;

// 学院科研工作任务计划表
@Controller
@RequestMapping("/CollegeProject")
public class CollegeProjectController extends BaseController {
    private String prefix = "system/CollegeProject";
    final private static List<Long> colleges = Arrays.asList(1149L, 1150L, 1139L, 1140L, 1143L, 1145L, 1147L, 1155L);
    @Autowired
    private IAlltotleService alltotleService;
    @Autowired
    private ICollegeProjectService collegeProjectService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Value("${system.dict.type.sys_acade_dept:sys_acade_dept}")
    private String dictType;

    @GetMapping("")
    String view() {
        System.out.println("CollegeProjectController.view");
        return prefix + "/view";
    }

    //http://localhost:8081/CollegeProject/list
    @PostMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> list() {
        //System.out.println("CollegeProjectController.list");
        Long deptId = getSysUser().getDeptId();
        List<Map<String, Object>> lists = new ArrayList<>();
        //System.out.println("judge(DeptId)=" + collegeProjectService.judge(deptId, getLoginName()));

        //判断身份
        int judgeResult = collegeProjectService.judge(deptId, getLoginName());
        if (judgeResult == 1) {
            lists = processProjects(collegeProjectService.selAll(deptId));
        } else if (judgeResult == 2) {
            //List<Long> colleges = Arrays.asList(1149L, 1150L, 1139L, 1140L, 1143L, 1145L, 1147L, 1155L);
            for (Long college : colleges) {
                lists.addAll(processProjects(collegeProjectService.selAll(college)));
                System.out.println("lists = " + lists);
            }
        }
        return lists;
    }

    //加上总计这一行
    private List<Map<String, Object>> processProjects(List<Map<String, Object>> projects) {
        //projects = convertByteArraysToString(projects);
        Map<String, Object> result = add_total(projects);
        result.put("cgzhdeptId", "总计");
        projects.add(result);
        return projects;
    }

    /**
     * 将 lists 中的字节数组转换为字符串
     *
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

    //增加总计那一行的数据
    public static Map<String, Object> add_total(List<Map<String, Object>> lists) {
        System.out.println("lists = " + lists);
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> currentMap : lists) {
            //如果他是学院，不显示学院这一行
//            Object cgzhdeptId = currentMap.get("cgzhdeptId");
//            if (cgzhdeptId instanceof Long) {
//                Long longValue = (Long) cgzhdeptId;
//                if (colleges.contains(longValue)) {
//                    continue;
//                }
//            }
            for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
                String key = entry.getKey();
                if (key.equals("parent_deptid")) {
                    continue;
                }
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
        return result;

    }

    // 查询alltotal 列表
    @PostMapping("/getAlltotallists")
    @ResponseBody
    public List<Alltotle> getAlltotallists() {
        //Alltotle alltotle = new Alltotle();
        SysUser sysUser = userService.selectUserById(getUserId());
        List<Alltotle> reLists = new ArrayList<>();
        //获取指定部门的统计
        List<Alltotle> lists = alltotleService.selectFourColtotleList(getDeptIdString());
        if (getUserId() == 1L) {
            return lists;
        } else {
            for (Alltotle list : lists) {
                if (Objects.equals(list.getPartenId(), sysUser.getParentId())) {
                    reLists.add(list);
                }
            }
            return reLists;
        }

    }
    private String getDeptIdString() {
        List<SysDictData> dictDataList = sysDictTypeService.selectDictDataByType(dictType);
        String deptIdString = dictDataList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.joining("','"));
        return "'"+deptIdString+"'";

    }
}
