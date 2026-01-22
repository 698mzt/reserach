package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciIntraSchoolScore;
import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import com.ruoyi.system.service.SciIntraSchProScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SciIntraSchProScoreServiceImpl implements SciIntraSchProScoreService {

  @Autowired
  private SciIntraSchProScoreMapper sciIntraSchProScoreMapper;


  @Override
  public int set_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro, int key) {
    int re = 0;
    String amountStr = sciIntraSchoolPro.getAmount();
    double amount = 0.0;
    try {
      amount = Double.parseDouble(amountStr);
    } catch (NumberFormatException e) {
      System.out.println("set_SchPro_score:无法转换为数字：" + amountStr);
    }
    
    // 根据经费金额直接计算对应的积分值，符合哈尔滨信息工程学院的规定
    int[] scores = calculateScoresByAmount(amount, key);
    
    // 只为前4个成员分配科研分，后续成员不分配
    // 处理固定的前四个负责人
    for (int i = 1; i <= 4; i++) {
      String userid = i == 1 ? sciIntraSchoolPro.getFirstPersonId() : i == 2 ? sciIntraSchoolPro.getSecondPersonId() : i == 3 ? sciIntraSchoolPro.getThirdPersonId() : sciIntraSchoolPro.getFourthPersonId();
      
      // 如果当前成员为空，则跳过
      if (userid == null || userid.trim().isEmpty() || userid.equals("-1") || userid.equals("null")) {
          continue;
      }
      
      int useridd = 0;
      try {
        useridd = Integer.parseInt(userid);
      } catch (NumberFormatException e) {
        System.out.println("set_SchPro_score:无法转换为整数：" + userid);
      }
      
      // 使用计算出的积分值
      if (key == 0) {
        re = sciIntraSchProScoreMapper.set_SchPro_score(scores[i-1], sciIntraSchoolPro.getId(), useridd);
      } else if (key == 1) {
        //设置结题积分（这里也使用相同的积分值，如果需要不同可以调整calculateScoresByAmount方法）
        re = sciIntraSchProScoreMapper.set_SchPro_JT_score(scores[i-1], sciIntraSchoolPro.getId(), useridd);
      }
    }
    
    // 注意：动态添加的成员（第五及以后）不分配积分，符合要求
    
    return re;
  }

  @Override
  public Integer getScoreById(Object id, Long uid) {
    return sciIntraSchProScoreMapper.getScoreById(id, uid);
  }

  /**
   * 开题撤回时积分
   *
   * @param sciIntraSchoolPro1
   * @return
   */
  @Override
  public Integer update_SchPro_score(SciIntraSchoolPro sciIntraSchoolPro1) {
    int re = 0;
    // 只为前4个成员更新积分
    for (int i = 1; i <= 4; i++) {
      String userid = i == 1 ? sciIntraSchoolPro1.getFirstPersonId() : i == 2 ? sciIntraSchoolPro1.getSecondPersonId() : i == 3 ? sciIntraSchoolPro1.getThirdPersonId() : sciIntraSchoolPro1.getFourthPersonId();
      
      // 如果当前成员为空，则跳过
      if (userid == null || userid.trim().isEmpty() || userid.equals("-1") || userid.equals("null")) {
          continue;
      }
      
      int useridd = 0;
      try {
        useridd = Integer.parseInt(userid);
      } catch (NumberFormatException e) {
        System.out.println("update_SchPro_score:无法转换为整数：" + userid);
      }
      re = sciIntraSchProScoreMapper.update_SchPro_score(sciIntraSchoolPro1.getId(), useridd);
    }
    return re;
  }
  /**
   * 结题撤回时积分
   *
   * @param sciIntraSchoolPro1
   * @return
   */
  @Override
  public Integer update_SchPro_score_jt(SciIntraSchoolPro sciIntraSchoolPro1) {
    int re = 0;
    // 只为前4个成员更新积分
    for (int i = 1; i <= 4; i++) {
      String userid = i == 1 ? sciIntraSchoolPro1.getFirstPersonId() : i == 2 ? sciIntraSchoolPro1.getSecondPersonId() : i == 3 ? sciIntraSchoolPro1.getThirdPersonId() : sciIntraSchoolPro1.getFourthPersonId();
      
      // 如果当前成员为空，则跳过
      if (userid == null || userid.trim().isEmpty() || userid.equals("-1") || userid.equals("null")) {
          continue;
      }
      
      int useridd = 0;
      try {
        useridd = Integer.parseInt(userid);
      } catch (NumberFormatException e) {
        System.out.println("update_SchPro_score:无法转换为整数：" + userid);
      }
      re = sciIntraSchProScoreMapper.update_SchPro_score_jt(sciIntraSchoolPro1.getId(), useridd);
    }
    return re;
  }

  /**
   * 申请开题的时候就就设置积分为0
   *
   * @param sciIntraSchoolPro
   * @return
   */
  @Override
  public int set_SchPro_score_noScore(SciIntraSchoolPro sciIntraSchoolPro) {
    int re = 0;
    // 只为前4个成员设置积分为0
    for (int i = 1; i <= 4; i++) {
      String userid = i == 1 ? sciIntraSchoolPro.getFirstPersonId() : i == 2 ? sciIntraSchoolPro.getSecondPersonId() : i == 3 ? sciIntraSchoolPro.getThirdPersonId() : sciIntraSchoolPro.getFourthPersonId();
      
      // 如果当前成员为空，则跳过
      if (userid == null || userid.trim().isEmpty() || userid.equals("-1") || userid.equals("null")) {
          continue;
      }
      
      int useridd = 0;
      try {
        useridd = Integer.parseInt(userid);
      } catch (NumberFormatException e) {
        System.out.println("set_SchPro_score:无法转换为整数：" + userid);
      }
      SciIntraSchoolScore sciIntraSchoolScore = new SciIntraSchoolScore(sciIntraSchoolPro.getId(), i, 0, useridd, 0);
      re = sciIntraSchProScoreMapper.set_SchPro_score_noScore(sciIntraSchoolScore);
    }
    
    // 动态添加的成员（第五及以后）不设置积分，符合要求
    
    return re;
  }


  /**
   * 根据经费金额和开题/结题类型计算前四名成员的积分
   * 
   * @param amount 经费金额（万元）
   * @param key 开题还是结题 0 开题 1 结题
   * @return 包含前四名成员积分的数组
   */
  private int[] calculateScoresByAmount(double amount, int key) {
    // 根据哈尔滨信息工程学院的规定，根据经费金额返回对应的积分
    if (amount >= 20 && amount <= 50) {
        // 经费到款20-50万元：第一排名：1540分，第二排名：700分，第三排名：280分，第四排名：140分
        return new int[]{1540, 700, 280, 140};
    } else if (amount >= 5 && amount < 10) {
        // 经费到款5-10万元：第一排名：440分，第二排名：200分，第三排名：80分，第四排名：40分
        return new int[]{440, 200, 80, 40};
    } else if (amount >= 10 && amount < 20) {
        // 对于10-20万之间的经费，按比例计算积分
        // 按照线性插值计算
        // 从10万（较高值）到20万（较低值）进行线性插值
        double ratio = (20 - amount) / 10; // 金额越大，系数越小
        int firstScore = (int)(440 + (1540 - 440) * (1 - ratio)); // 在440-1540之间插值
        int secondScore = (int)(200 + (700 - 200) * (1 - ratio)); // 在200-700之间插值
        int thirdScore = (int)(80 + (280 - 80) * (1 - ratio)); // 在80-280之间插值
        int fourthScore = (int)(40 + (140 - 40) * (1 - ratio)); // 在40-140之间插值
        return new int[]{firstScore, secondScore, thirdScore, fourthScore};
    } else if (amount > 50) {
        // 对于超过50万的情况，使用最高积分或按比例增加
        // 这里使用最高积分，也可以考虑按比例增加
        return new int[]{1540, 700, 280, 140};
    } else if (amount >= 0.5 && amount < 5) {
        // 对于0.5-5万之间的经费，按比例计算积分
        double ratio = (5 - amount) / 4.5; // 金额越大，系数越大
        int firstScore = (int)(440 * (amount / 5)); // 在0-440之间按比例计算
        int secondScore = (int)(200 * (amount / 5)); // 在0-200之间按比例计算
        int thirdScore = (int)(80 * (amount / 5)); // 在0-80之间按比例计算
        int fourthScore = (int)(40 * (amount / 5)); // 在0-40之间按比例计算
        return new int[]{firstScore, secondScore, thirdScore, fourthScore};
    } else {
        // 低于0.5万或无效金额，返回0分
        return new int[]{0, 0, 0, 0};
    }
  }


}