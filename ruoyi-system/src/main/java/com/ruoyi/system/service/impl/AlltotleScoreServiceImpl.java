package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.mapper.SciIntraSchProScoreMapper;
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
    int size = alltotleScores.size();
    int row = 0;
    for (AlltotleScore alltotleScore : alltotleScores) {
      Long userid = alltotleScore.getUserId() ;
      Long scoreByUId = sciIntraSchProScoreMapper.getScoreByUId(userid);
      if (scoreByUId == 0){
        continue;
      }
      alltotleScore.setCgzh(scoreByUId.toString());
      alltotleScoreService.updateAlltotleScore(alltotleScore);
      row += 1;
    }
    if (row == size){
      return 1;
    }else{
      return 0;
    }
  }
}
