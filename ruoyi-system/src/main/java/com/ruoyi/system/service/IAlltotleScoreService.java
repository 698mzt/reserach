package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AlltotleScore;

/**
 * 统计积分Service接口
 *
 * @author ruoyi
 * @date 2025-09-03
 */
public interface IAlltotleScoreService
{
  /**
   * 查询统计积分
   *
   * @param userId 统计积分主键
   * @return 统计积分
   */
  public AlltotleScore selectAlltotleScoreByUserId(Long userId);

  /**
   * 查询统计积分列表
   *
   * @param alltotleScore 统计积分
   * @return 统计积分集合
   */
  public List<AlltotleScore> selectAlltotleScoreList(AlltotleScore alltotleScore);

  /**
   * 新增统计积分
   *
   * @param alltotleScore 统计积分
   * @return 结果
   */
  public int insertAlltotleScore(AlltotleScore alltotleScore);

  /**
   * 修改统计积分
   *
   * @param alltotleScore 统计积分
   * @return 结果
   */
  public int updateAlltotleScore(AlltotleScore alltotleScore);

  /**
   * 批量删除统计积分
   *
   * @param userIds 需要删除的统计积分主键集合
   * @return 结果
   */
  public int deleteAlltotleScoreByUserIds(String userIds);

  /**
   * 删除统计积分信息
   *
   * @param userId 统计积分主键
   * @return 结果
   */
  public int deleteAlltotleScoreByUserId(Long userId);

  int synchronousAlltotleScore();
}
