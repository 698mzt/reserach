package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Alltotle;

/**
 * AlltotleMapper接口
 *
 * @author ruoyi
 * @date 2025-08-26
 */
public interface AlltotleMapper
{
  /**
   * 查询Alltotle
   *
   * @param userId Alltotle主键
   * @return Alltotle
   */
  public Alltotle selectAlltotleByUserId(Long userId);

  /**
   * 查询Alltotle列表
   *
   * @param alltotle Alltotle
   * @return Alltotle集合
   */
  public List<Alltotle> selectAlltotleList(Alltotle alltotle);

  /**
   * 新增Alltotle
   *
   * @param alltotle Alltotle
   * @return 结果
   */
  public int insertAlltotle(Alltotle alltotle);

  /**
   * 修改Alltotle
   *
   * @param alltotle Alltotle
   * @return 结果
   */
  public int updateAlltotle(Alltotle alltotle);

  /**
   * 删除Alltotle
   *
   * @param userId Alltotle主键
   * @return 结果
   */
  public int deleteAlltotleByUserId(Long userId);

  /**
   * 批量删除Alltotle
   *
   * @param userIds 需要删除的数据主键集合
   * @return 结果
   */
  public int deleteAlltotleByUserIds(String[] userIds);
}
