package com.ruoyi.quartz.task;

import com.ruoyi.system.mapper.SynchronizeDataMapper;
import com.ruoyi.system.mapper.SynchronizeZlrzMapper;
import com.ruoyi.system.mapper.SynrewardDataMapper;
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
  private SynrewardDataMapper synrewardDataMapper;


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

    //同步奖励数据
    Reward();
  }


  /**
   * 同步横向课题数据
   */
  public void SynchronizeUser() {
    System.out.println("用户数据开始同步");
    synchronizeDataMapper.SynchronizeUser();
  }
  public void SynchronizeDeleteUser() {
    synchronizeDataMapper.SynchronizeDeleteUser();
    System.out.println("用户数据同步完成");
  }

  /**
   * 同步横向课题数据
   */
  public void synchronizeHX() {
    System.out.println("横向课题数据开始同步");
    synchronizeDataMapper.SynchronizeHX();
    System.out.println("横向课题数据同步完成");
  }

  /**
   * 同步纵向课题数据
   */
  public void synchronizeZX() {
    System.out.println("纵向课题数据开始同步");
    synchronizeDataMapper.SynchronizeZX();
    System.out.println("纵向课题数据同步完成");
  }

  /**
   * 同步成果转化数据
   */
  private void synchronousAlltotle() {
    System.out.println("成果转化数据开始同步");
    synchronousAlltotle.synchronousAlltotle();
    System.out.println("成果转化数据同步完成");
  }


  /**
   * 同步专利软著数据
   */
  private void synchronizeZLRZ() {
    System.out.println("专利软著数据开始同步");
    synchronizeZlrzMapper.synchronizeZLRZ();
    System.out.println("专利软著数据同步完成");

  }


  /**
   * 同步奖励数据
   */
  private void Reward() {
    System.out.println("奖励数据开始同步");
    synrewardDataMapper.Reward();
    System.out.println("奖励数据同步完成");
  }
}
