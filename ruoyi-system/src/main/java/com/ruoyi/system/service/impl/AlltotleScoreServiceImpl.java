package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AlltotleScoreMapper;
import com.ruoyi.system.domain.AlltotleScore;
import com.ruoyi.system.service.IAlltotleScoreService;
import com.ruoyi.common.core.text.Convert;

/**
 * 统计积分Service业务层处理
 *
 * @author ruoyi
 * @date 2025-09-03
 */
@Service
public class AlltotleScoreServiceImpl implements IAlltotleScoreService
{
  private static final Logger log = LoggerFactory.getLogger(AlltotleScoreServiceImpl.class);

  @Autowired
  private AlltotleScoreMapper alltotleScoreMapper;
  @Autowired
  private IAlltotleScoreService alltotleScoreService;

  @Autowired
  private SciIntraSchProScoreMapper sciIntraSchProScoreMapper;
  /**
   * 查询统计积分
   *
   * @param userId 统计积分主键
   * @return 统计积分
   */
  @Override
  public AlltotleScore selectAlltotleScoreByUserId(Long userId)
  {
    return alltotleScoreMapper.selectAlltotleScoreByUserId(userId);
  }

  /**
   * 查询统计积分列表
   *
   * @param alltotleScore 统计积分
   * @return 统计积分
   */
  @Override
  public List<AlltotleScore> selectAlltotleScoreList(AlltotleScore alltotleScore)
  {
    return alltotleScoreMapper.selectAlltotleScoreList(alltotleScore);
  }

  /**
   * 新增统计积分
   *
   * @param alltotleScore 统计积分
   * @return 结果
   */
  @Override
  public int insertAlltotleScore(AlltotleScore alltotleScore)
  {
    return alltotleScoreMapper.insertAlltotleScore(alltotleScore);
  }

  /**
   * 修改统计积分
   *
   * @param alltotleScore 统计积分
   * @return 结果
   */
  @Override
  public int updateAlltotleScore(AlltotleScore alltotleScore)
  {
    return alltotleScoreMapper.updateAlltotleScore(alltotleScore);
  }

  /**
   * 批量删除统计积分
   *
   * @param userIds 需要删除的统计积分主键
   * @return 结果
   */
  @Override
  public int deleteAlltotleScoreByUserIds(String userIds)
  {
    return alltotleScoreMapper.deleteAlltotleScoreByUserIds(Convert.toStrArray(userIds));
  }

  /**
   * 删除统计积分信息
   *
   * @param userId 统计积分主键
   * @return 结果
   */
  @Override
  public int deleteAlltotleScoreByUserId(Long userId)
  {
    return alltotleScoreMapper.deleteAlltotleScoreByUserId(userId);
  }

  @Override
  public int synchronousAlltotleScore() {
    List<AlltotleScore> alltotleScores = alltotleScoreService.selectAlltotleScoreList(null);
    int totalCount = alltotleScores.size();
    int successCount = 0;
    int failCount = 0;
    int zeroValueCount = 0;
    log.info("成果转化积分同步开始，总记录数：{}", totalCount);
    for (AlltotleScore alltotleScore : alltotleScores) {
      Long userId = alltotleScore.getUserId();
      Long scoreByUId = sciIntraSchProScoreMapper.getScoreByUId(userId);
      long currentScore = scoreByUId == null ? 0L : scoreByUId;
      if (currentScore == 0L) {
        zeroValueCount += 1;
      }
      alltotleScore.setCgzh(String.valueOf(currentScore));
      int updateRows = alltotleScoreService.updateAlltotleScore(alltotleScore);
      if (updateRows > 0) {
        successCount += 1;
      } else {
        failCount += 1;
        log.warn("成果转化积分同步失败，userId：{}，同步值：{}", userId, currentScore);
      }
    }
    log.info("成果转化积分同步结束，总记录数：{}，成功：{}，失败：{}，零值：{}", totalCount, successCount, failCount, zeroValueCount);
    return failCount == 0 ? 1 : 0;
  }
}
