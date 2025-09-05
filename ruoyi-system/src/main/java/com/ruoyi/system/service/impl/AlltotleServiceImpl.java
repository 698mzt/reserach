package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AlltotleMapper;
import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.service.IAlltotleService;
import com.ruoyi.common.core.text.Convert;

/**
 * AlltotleService业务层处理
 *
 * @author ruoyi
 * @date 2025-08-26
 */
@Service
public class AlltotleServiceImpl implements IAlltotleService {
  @Autowired
  private AlltotleMapper alltotleMapper;
  @Autowired
  private SciIntraSchProApplyMapper sciIntraSchProApplyMapper;

  private List<String> leaderKeyList = Arrays.asList("first_person_id", "second_person_id", "third_person_id", "fourth_person_id");
  //private List< String> leaderKeyList =  Arrays.asList("first_person_id");

  /**
   * 查询Alltotle
   *
   * @param userId Alltotle主键
   * @return Alltotle
   */
  @Override
  public Alltotle selectAlltotleByUserId(Long userId) {
    return alltotleMapper.selectAlltotleByUserId(userId);
  }

  /**
   * 查询Alltotle列表
   *
   * @param alltotle Alltotle
   * @return Alltotle
   */
  @Override
  public List<Alltotle> selectAlltotleList(Alltotle alltotle) {
    return alltotleMapper.selectAlltotleList(alltotle);
  }

  /**
   * 新增Alltotle
   *
   * @param alltotle Alltotle
   * @return 结果
   */
  @Override
  public int insertAlltotle(Alltotle alltotle) {
    return alltotleMapper.insertAlltotle(alltotle);
  }

  /**
   * 修改Alltotle
   *
   * @param alltotle Alltotle
   * @return 结果
   */
  @Override
  public int updateAlltotle(Alltotle alltotle) {
    return alltotleMapper.updateAlltotle(alltotle);
  }

  /**
   * 批量删除Alltotle
   *
   * @param userIds 需要删除的Alltotle主键
   * @return 结果
   */
  @Override
  public int deleteAlltotleByUserIds(String userIds) {
    return alltotleMapper.deleteAlltotleByUserIds(Convert.toStrArray(userIds));
  }

  /**
   * 删除Alltotle信息
   *
   * @param userId Alltotle主键
   * @return 结果
   */
  @Override
  public int deleteAlltotleByUserId(Long userId) {
    return alltotleMapper.deleteAlltotleByUserId(userId);
  }

  // 自动同步成果转化 alltotle表
  @Override
  public int synchronousAlltotle() {
    //获取现在alltotle的所有信息
    List<Alltotle> alltotle = new ArrayList<>();
    // 获取所有完结横向课题信息
    // 获取所有完结纵向课题信息
    //....
    // 获取所有完结成果转化信息 四位负责人id 金额
    List<Map<String, Object>> allOverSchProToAlltotles = sciIntraSchProApplyMapper.getAllOverSchProToAlltotle();
    for (int i = 0; i < allOverSchProToAlltotles.size(); i++) {
      // 判断alltotle中是否有这个课题的四位负责人
      alltotle = judgeLeaderInalltotle(allOverSchProToAlltotles.get(i), alltotle);
    }
    //System.out.println("synchronousAlltotle:allOverSchProToAlltotles = " + alltotle);
    // 修改alltotle表
    int updaterows = 0;
    for (int i = 0; i < alltotle.size(); i++) {
      int updaterow = alltotleMapper.updateAlltotle(alltotle.get(i));
      updaterows += updaterow;
    }
    // 获取全部然后统一改全部     获取全部改变化的
    if (updaterows == alltotle.size()) {
      return 1;
    } else {
      return 0;
    }

  }

  @Override
  public List<Alltotle> selectFourColtotleList(Alltotle alltotle) {
    List<Alltotle> list = alltotleMapper.selectFourColtotleList(alltotle);
    List<Alltotle> result = new ArrayList<>();
    if (list.isEmpty()) {
      return result;
    }
    String thisPartName = list.get(0).getPartenName();
    String thisDeptName = list.get(0).getDeptName();
    Alltotle sum_alltotle = new Alltotle();
    // 父部门统计
    Alltotle sum_alltotle_part = new Alltotle();
    for (int i = 0; i < list.size(); i++){
      // 插入返回值，如果是最后一个 或者 当前部门名称和上一部门名称不一致 就说明这个部门计算完毕 先插入返回值
      if (i == list.size() - 1 || !list.get(i).getDeptName().equals(thisDeptName)){
        result.add(sum_alltotle);
      }
      if (i == list.size() - 1 || !list.get(i).getPartenName().equals(thisPartName)){
        result.add(sum_alltotle_part);
      }
      if (i == 0 || !list.get(i).getPartenName().equals(thisPartName)){
        sum_alltotle_part = new Alltotle();
        sum_alltotle_part.setDeptName("计");
        sum_alltotle_part.setPartenName("统");
        thisPartName = list.get(i).getPartenName();
      }

      // 初始化 如果是第一个，或者当前部门名称和上一部门名称不一致 就说明这个部门计算完毕
      if (i==0 || !list.get(i).getDeptName().equals(thisDeptName)){
        sum_alltotle = new Alltotle();
        sum_alltotle.setDeptName(list.get(i).getDeptName());
        sum_alltotle.setPartenName(list.get(i).getPartenName());
        thisDeptName = list.get(i).getDeptName();
      }


      //是否只显示按照部门总计的数据
      //result.add(list.get(i));
      sum_alltotle = AlltotleSet(sum_alltotle, list.get(i));
      sum_alltotle_part = AlltotleSet(sum_alltotle_part, list.get(i));
    }
    // 添加最后一个分组的总和
    result.add(sum_alltotle);
    return result;
  }

  private Alltotle AlltotleSet(Alltotle sum_alltotle, Alltotle alltotle) {
    // 使用反射简化求和操作
    //getDeclaredFields()方法返回一个包含Field对象的数组，每个Field对象代表类中
    java.lang.reflect.Field[] fields = Alltotle.class.getDeclaredFields();
    for (java.lang.reflect.Field field : fields) {
      if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
        continue; // 跳过静态字段
      }

      field.setAccessible(true);
      try {
        String fieldName = field.getName();
        if (Alltotle.no_need_add_list.contains(fieldName)){
          continue;
        }
        // 获取字段值
        Object value1 = field.get(sum_alltotle);
        Object value2 = field.get(alltotle);

        String str1 = (String) value1;
        String str2 = (String) value2;

        // 判断应该使用哪个求和方法
        if (str1 != null && str2 != null && !str1.isEmpty() && !str2.isEmpty()) {
          String result;
          if (!Alltotle.ge_wan_list.contains(fieldName)) {
            result = sum_ge(str1, str2);
          } else {
            result = sum_ge_wan(str1, str2);
          }
          field.set(sum_alltotle, result);
        } else if ((str1 == null || str1.isEmpty()) && str2 != null && !str2.isEmpty()) {
          field.set(sum_alltotle, str2);
        }
        // 如果str1不为空而str2为空，则保持str1的值不变，不需要额外操作
      } catch (IllegalAccessException e) {
        // 处理异常
        e.printStackTrace();
      }
    }

    return sum_alltotle;
  }

  private String sum_ge_wan(String str1, String str2) {
    if (str1.contains("个 (") && str1.contains("万)")&&str2.contains("个 (") && str2.contains("万)")) {
      Integer num1 = Integer.parseInt(str1.split("个")[0]);
      Integer num2 = Integer.parseInt(str2.split("个")[0]);
      Integer num3 = Integer.parseInt(str1.split("万")[0].split("\\(")[1]);
      Integer num4 = Integer.parseInt(str2.split("万")[0].split("\\(")[1]);
      Integer sum1= num1+num2;
      Integer sum2= num3+num4;
      return sum1+"个 ("+sum2+"万)";
    }else {
      throw new RuntimeException("格式错误1"+str1+"/"+str2);
    }
  }
  private String sum_ge (String str1, String str2) {
    if (str1.contains("个") && str2.contains("个")){
      Integer num1 = Integer.parseInt(str1.split("个")[0]);
      Integer num2 = Integer.parseInt(str2.split("个")[0]);
      Integer sum= num1+num2;
      return sum+"个";
    }else{
      throw new RuntimeException("格式错误2"+str1+"/"+str2);
    }
  }
  /**
   * 找到这个项目需要统计负责人
   *
   * @param allOverSchProToAlltotle 单个项目
   * @param alltotle                已经统计过的负责人
   * @return 添加完成的负责人合集
   */
  private List<Alltotle> judgeLeaderInalltotle(Map<String, Object> allOverSchProToAlltotle, List<Alltotle> alltotle) {
    // 这个项目的金额
    Double mount = Double.valueOf((String) allOverSchProToAlltotle.get("amount"));

    for (int i = 0; i < leaderKeyList.size(); i++) {
      // 当前负责人的id
      Long userId = Long.valueOf((String) allOverSchProToAlltotle.get(leaderKeyList.get(i)));
      // 判断这个负责人是否在alltotle中
      Boolean judgeInalltole = false;
      //找alltotle有没有，如果有改 judgeInalltole 状态
      for (int j = 0; j < alltotle.size(); j++) {
        // 如果这个人已经在 alltotle 中
        if (alltotle.get(j).getUserId().equals(userId)) {
          // 增加成果转化金额，增加个数
          judgeInalltole = true;
          alltotle = AddMount_count(alltotle, j, mount);
          break;
        }
      }
      // 如果没有找到
      if (!judgeInalltole) {
        // 没找到就添加 然后增加成果转化金额，增加个数
        alltotle.add(new Alltotle(userId));
        alltotle = AddMount_count(alltotle, alltotle.size() - 1, mount);
      }

    }
    return alltotle;
  }

  /**
   * 添加金额和个数 处理成  x个（y万） 的格式
   *
   * @param alltotle 需要添加的 alltotle
   * @param j        需要添加的索引
   * @param amount   需要添加的金额
   * @return
   */
  private List<Alltotle> AddMount_count(List<Alltotle> alltotle, int j, Double amount) {
    if (amount < 2) {
      if (alltotle.get(j).getEyxxx() != null && alltotle.get(j).getEyxxx() != "") {
        alltotle.get(j).setEyxxx(alltotle.get(j).getEyxxx(), amount);
      } else {
        alltotle.get(j).setEyxxx(null, amount);
        return alltotle;
      }

    }
    if (amount >= 2 && amount < 5) {
      if (alltotle.get(j).getEdwxx() != null && alltotle.get(j).getEdwxx() != "") {
        alltotle.get(j).setEdwxx(alltotle.get(j).getEdwxx(), amount);
      } else {
        alltotle.get(j).setEdwxx(null, amount);
        return alltotle;
      }
    }
    if (amount >= 5 && amount < 10) {
      if (alltotle.get(j).getWdsxx() != null && alltotle.get(j).getWdsxx() != "") {
        alltotle.get(j).setWdsxx(alltotle.get(j).getWdsxx(), amount);
      } else {
        alltotle.get(j).setWdsxx(null, amount);
        return alltotle;
      }
    }
    if (amount >= 10 && amount < 20) {
      if (alltotle.get(j).getSdesxx() != null && alltotle.get(j).getSdesxx() != "") {
        alltotle.get(j).setSdesxx(alltotle.get(j).getSdesxx(), amount);
      } else {
        alltotle.get(j).setSdesxx(null, amount);
        return alltotle;
      }
    }
    if (amount >= 20 && amount < 35) {
      if (alltotle.get(j).getEsdsswxx() != null && alltotle.get(j).getEsdsswxx() != "") {
        alltotle.get(j).setEsdsswxx(alltotle.get(j).getEsdsswxx(), amount);
      } else {
        alltotle.get(j).setEsdsswxx(null, amount);
        return alltotle;
      }
    }
    if (amount >= 35 && amount < 50) {
      if (alltotle.get(j).getSswdwsxx() != null && alltotle.get(j).getSswdwsxx() != "") {
        alltotle.get(j).setSswdwsxx(alltotle.get(j).getSswdwsxx(), amount);
      } else {
        alltotle.get(j).setSswdwsxx(null, amount);
        return alltotle;
      }
    }
    if (amount >= 50) {
      if (alltotle.get(j).getDywsxx() != null && alltotle.get(j).getDywsxx() != "") {
        alltotle.get(j).setDywsxx(alltotle.get(j).getDywsxx(), amount);
      } else {
        alltotle.get(j).setDywsxx(null, amount);
        return alltotle;
      }
    }
    return alltotle;
  }

}
