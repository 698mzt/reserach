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
    for (int i = 0; i < alltotle.size(); i++){
       int updaterow = alltotleMapper.updateAlltotle(alltotle.get(i));
      updaterows += updaterow;
    }
    // 获取全部然后统一改全部     获取全部改变化的
    if (updaterows==alltotle.size()){
      return 1;
    }else{
      return 0;
    }

  }

  /**
   * 找到这个项目需要统计负责人
   * @param allOverSchProToAlltotle 单个项目
   * @param alltotle 已经统计过的负责人
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
          judgeInalltole= true;
          alltotle = AddMount_count(alltotle, j, mount);
          break;
        }
      }
      // 如果没有找到
      if (!judgeInalltole) {
        // 没找到就添加 然后增加成果转化金额，增加个数
        alltotle.add(new Alltotle(userId));
        alltotle = AddMount_count(alltotle, alltotle.size()-1, mount);
      }

    }
    return alltotle;
  }

  /**
   * 添加金额和个数 处理成  x个（y万） 的格式
   * @param alltotle 需要添加的 alltotle
   * @param j        需要添加的索引
   * @param amount    需要添加的金额
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
