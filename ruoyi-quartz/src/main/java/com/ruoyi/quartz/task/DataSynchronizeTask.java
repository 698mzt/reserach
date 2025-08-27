package com.ruoyi.quartz.task;

import com.ruoyi.system.mapper.SynchronizeDataMapper;
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

  /**
   * 同步所有数据
   * 包括横向课题和纵向课题数据
   */
  public void synchronizeAll() {
    // 同步横向课题数据
    synchronizeHX();

    // 同步纵向课题数据
    synchronizeZX();

    //自动同步成果转化
    synchronousAlltotle();
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
}
