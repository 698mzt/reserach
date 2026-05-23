package com.ruoyi.quartz.task;

import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.system.domain.Alltotle;
import com.ruoyi.system.mapper.SynchronizeDataMapper;
import com.ruoyi.system.service.IAlltotleScoreService;
import com.ruoyi.system.service.IAlltotleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据同步定时任务
 *
 * @author ruoyi
 */
@Component("dataSynchronizeTask")
public class DataSynchronizeTask {
  private static final Logger log = LoggerFactory.getLogger(DataSynchronizeTask.class);

  @Autowired
  private SynchronizeDataMapper synchronizeDataMapper;

  @Autowired
  private IAlltotleService synchronousAlltotle;


  @Autowired
  private IAlltotleScoreService alltotleScoreService;

  @Autowired
  private IAlltotleService alltotleService;
  /**
   * 同步所有数据
   * 包括横向课题和纵向课题数据
   */
//  todo:积分同步需要写（横向、纵向定时任务写完了）
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
   * 同步用户数据
   */
  public void SynchronizeUser() {
    log.info("用户数据开始同步");
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
    log.info("用户数据同步完成");
  }



  /**
   * 同步横向课题数据
   */
  public void synchronizeHX() {
    log.info("横向课题数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeHX();
//    积分（12/15修改，按年份查询）
    synchronizeDataMapper.SynchronizeScoreHX();
    log.info("横向课题数据同步完成");
  }



  /**
   * 同步纵向课题数据
   */
  public void synchronizeZX() {
    log.info("纵向课题数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeZX();
//    积分（12/15修改，按年份查询）
//    校级以上
    synchronizeDataMapper.SynchronizeScoreZXXJYS();
//    校级
    synchronizeDataMapper.SynchronizeScoreZXXJ();
    log.info("纵向课题数据同步完成");
  }



  /**
   * 同步成果转化数据
   */
  private void synchronousAlltotle() {
    log.info("成果转化数据开始同步");
//    todo：这个同步数据 是否为同步个数
    int totalSyncResult = synchronousAlltotle.synchronousAlltotle();
//    todo：需要同步积分,按年份查询
    int scoreSyncResult = alltotleScoreService.synchronousAlltotleScore();
    log.info("成果转化数据同步完成，个数同步结果：{}，积分同步结果：{}", totalSyncResult, scoreSyncResult);
    //todo：缓存
//    Alltotle alltotle = new Alltotle();
//    List<Alltotle> lists = alltotleService.selectFourColtotleList(alltotle);
//    CacheUtils.put(ShiroConstants.SYS_COL_PROJ_PLAN_CACHE,lists);
//    log.info("缓存同步完成");

  }



  /**
   * 同步专利软著数据
   */
  private void synchronizeZLRZ() {
    log.info("专利软著数据开始同步");
//    个数
    synchronizeDataMapper.synchronizeZLRZ();
//    积分
    synchronizeDataMapper.SynchronizeScoreZL();
    synchronizeDataMapper.SynchronizeScoreRZ();
    log.info("专利软著数据同步完成");

  }



  /**
   * 同步教材软著数据
   */
  private void synchronizeJCRZ() {
    log.info("教材软著数据开始同步");
//    个数
    synchronizeDataMapper.synchronizeJCRZ();
//    积分
    synchronizeDataMapper.SynchronizeScoreJCZZ();
    log.info("教材软著数据同步完成");
  }



  /**
   * 同步奖励数据
   */
  private void Reward() {
    log.info("奖励数据开始同步");
//    个数
    synchronizeDataMapper.Reward();
//    积分
    synchronizeDataMapper.SynchronizeScoreJL();
    log.info("奖励数据同步完成");
  }



  /**
   * 同步论文
   */
  private void syncronzedFlow() {
    log.info("论文数据开始同步");
//    个数
    synchronizeDataMapper.syncronzedFlow();
//    积分
    synchronizeDataMapper.SynchronizeScoreXSLW();
    log.info("论文数据同步完成");
  }



  /**
   * 同步讲座报告
   */
  private void SynchronizeReport() {
    log.info("讲座报告数据开始同步");
//    个数
    synchronizeDataMapper.SynchronizeReport();
//    积分
    synchronizeDataMapper.SynchronizeScoreJZBG();
    log.info("讲座报告数据同步完成");
  }

}
