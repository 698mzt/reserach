package com.ruoyi.quartz.task;

import com.ruoyi.system.mapper.SynchronizeDataMapper;
import com.ruoyi.system.mapper.SynchronizeZlrzMapper;
import com.ruoyi.system.mapper.SynchronizeJcrzMapper;
import com.ruoyi.system.mapper.SynrewardDataMapper;
import com.ruoyi.system.mapper.synchronizedFlowMapper;
import com.ruoyi.system.service.IAlltotleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据同步定时任务
 *
 * @author ruoyi
 */
@Component("dataSynchronizeTask")
public class DataSynchronizeTask {

  @Autowired
  private SynchronizeDataMapper synchronizeDataMapper;

  @Autowired
  private IAlltotleService synchronousAlltotle;

  @Autowired
  private SynchronizeZlrzMapper synchronizeZlrzMapper;
  @Autowired
  private SynchronizeJcrzMapper synchronizeJcrzMapper;

  @Autowired
  private SynrewardDataMapper synrewardDataMapper;

  @Autowired
  private synchronizedFlowMapper synchronizedFlowMapper;


  /**
   * 同步所有数据
   * 包括横向课题和纵向课题数据
   */
  public void synchronizeAll() {
    // 同步用户数据
    SynchronizeUser();

    // 删除不存在的用户数据
    SynchronizeDeleteUser();

    // 同步横向课题数据
    synchronizeHX();

    // 同步纵向课题数据
    synchronizeZX();

    //自动同步成果转化
    synchronousAlltotle();

    //同步专利软著
    synchronizeZLRZ();

    //同步教材软著
    synchronizeJCRZ();

    //同步奖励数据
    Reward();

    //同步论文
    syncronzedFlow();

    //同步讲座报告
    SynchronizeReport();
  }


  /**
   * 同步横向课题数据
   */
  public void SynchronizeUser() {
    System.out.println("用户数据开始同步");
//    个数表
    synchronizeDataMapper.SynchronizeUser();
//    积分表
    synchronizeDataMapper.SynchronizeUserForScore();
  }
  public void SynchronizeDeleteUser() {
//    个数表
    synchronizeDataMapper.SynchronizeDeleteUser();
//    积分表
    synchronizeDataMapper.SynchronizeDeleteUserForScore();
    System.out.println("用户数据同步完成");
  }

  /**
   * 同步横向课题数据
   */
  public void synchronizeHX() {
    System.out.println("横向课题数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeHX();
    System.out.println("横向课题数据同步完成");
  }

  /**
   * 同步纵向课题数据
   */
  public void synchronizeZX() {
    System.out.println("纵向课题数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeZX();
    System.out.println("纵向课题数据同步完成");
  }

  /**
   * 同步成果转化数据
   */
  private void synchronousAlltotle() {
    System.out.println("成果转化数据开始同步");
//    todo：这个同步数据 是否为同步个数
    synchronousAlltotle.synchronousAlltotle();
    System.out.println("成果转化数据同步完成");
  }


  /**
   * 同步专利软著数据
   */
  private void synchronizeZLRZ() {
    System.out.println("专利软著数据开始同步");
//    个数
    synchronizeZlrzMapper.synchronizeZLRZ();
    System.out.println("专利软著数据同步完成");

  }

  /**
   * 同步教材软著数据
   */
  private void synchronizeJCRZ() {
    System.out.println("教材软著数据开始同步");
//    个数
    synchronizeJcrzMapper.synchronizeJCRZ();
    System.out.println("教材软著数据同步完成");
  }


  /**
   * 同步奖励数据
   */
  private void Reward() {
    System.out.println("奖励数据开始同步");
//    个数
    synrewardDataMapper.Reward();
    System.out.println("奖励数据同步完成");
  }

  /**
   * 同步论文
   */
  private void syncronzedFlow() {
    System.out.println("论文数据开始同步");
//    个数
    synchronizedFlowMapper.syncronzedFlow();
    System.out.println("论文数据同步完成");
  }

  /**
   * 同步讲座报告
   */
  private void SynchronizeReport() {
    System.out.println("讲座报告数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeReport();
    System.out.println("讲座报告数据同步完成");
  }

}
