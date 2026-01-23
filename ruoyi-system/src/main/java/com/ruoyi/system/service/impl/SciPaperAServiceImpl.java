package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Paper_user_score;
import com.ruoyi.system.domain.SciPaperAr;
import com.ruoyi.system.mapper.PaperUserScoreServiceMapper;
import com.ruoyi.system.mapper.SciPaperACfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciPaperAMapper;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 论文Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-07
 */
@Service
public class SciPaperAServiceImpl implements ISciPaperAService {
    @Autowired
    private SciPaperAMapper sciPaperAMapper;
    @Autowired
    private SciPaperACfgMapper sciPaperACfgMapper;
    @Autowired
    private PaperUserScoreServiceMapper paperUserScoreServiceImplMapper;    /**
     * 查询论文
     *
     * @param id 论文主键
     * @return 论文
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public SciPaperA selectSciPaperAById(Long id) {
        return sciPaperAMapper.selectSciPaperAById(id);
    }

    /**
     * 查询论文列表
     *
     * @param sciPaperA 论文
     * @return 论文
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAList(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAList(sciPaperA);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAExport(List<String> ListRowId, SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAExport(ListRowId, sciPaperA);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListKY(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListKY(sciPaperA);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListXY(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListXY(sciPaperA);
    }

    /**
     * 新增论文
     *
     * @param sciPaperA 论文
     * @return 结果
     */
    @Override
    public int insertSciPaperA(SciPaperA sciPaperA) {

        return sciPaperAMapper.insertSciPaperA(sciPaperA);
    }


    /**
     * 修改论文
     *
     * @param sciPaperA 论文
     * @return 结果
     */
    @Override
    public int updateSciPaperA(SciPaperA sciPaperA) {
        return sciPaperAMapper.updateSciPaperA(sciPaperA);
    }

    /**
     * 批量删除论文
     *
     * @param ids 需要删除的论文主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSciPaperAByIds(String ids) {
        paperUserScoreServiceImplMapper.deletePaperUserScoreByPaperId(Long.valueOf(ids));
        return sciPaperAMapper.deleteSciPaperAByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除论文信息
     *
     * @param id 论文主键
     * @return 结果
     */
    @Override
    public int deleteSciPaperAById(Long id) {
        return sciPaperAMapper.deleteSciPaperAById(id);
    }

    /**
     * 更新论文状态
     * @param id 论文ID
     * @return 更新结果
     */
    @Override
    public int updateSciPaperAState(Integer id) {
        return sciPaperAMapper.updateSciPaperAState(id);
    }

    /**
     * 查询论文列表（查询列表）
     * @param sciPaperA 论文实体
     * @return 论文列表
     */
    @Override
    @DataScope(deptAlias = "pt", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListCxList(sciPaperA);
    }

    /**
     * 根据用户ID查询论文角色
     * @param userId 用户ID
     * @return 论文列表
     */
    @Override
    public List<SciPaperA> selectSciPaperArole(Long userId) {
        return sciPaperAMapper.selectSciPaperArole(userId);
    }

    /**
     * 根据用户ID查询论文列表
     * @param sciPaperA 论文实体
     * @return 论文列表
     */
    @Override
    public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListCx(sciPaperA);
    }

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<String> selectSciPaperAByroleId(Long userId) {
        return sciPaperAMapper.selectSciPaperAByroleId(userId);
    }

    /**
     * 论文批阅点击通过
     * @param id 论文ID
     * @param uid 用户ID
     * @param urlFlag URL标识
     * @param order 论文类别
     * @param user_order 用户排名
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pytg(String id, Long uid, String urlFlag, String order, String user_order) {
        String state = "0";
        SciPaperAr sciPaperAr = new SciPaperAr();
        if (urlFlag.equals("pro")) {
            state = "2"; //教研室通过
            sciPaperAr.setConcate("教研室通过");
        } else if (urlFlag.equals("xytg")) {
            state = "4"; //学院通过
            sciPaperAr.setConcate("学院通过");
        } else if (urlFlag.equals("kytg")) {
            state = "8"; //科研处通过
            sciPaperAr.setConcate("科研处通过");
            // 查询积分表
            List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(order);
            // 通过之后设置积分
            int res = setPaperUserScore(id, point_list);
            if (res < 1 || res >4){
                return -1;
            }

        }
        int a = sciPaperAMapper.pytg(id, state);

        sciPaperAr.setUid(uid);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        sciPaperAr.setState("通过");
        sciPaperAMapper.insertSciPaperAr(sciPaperAr);
        return a;
    }

    /**
     * 实时计算论文科研分
     * @param paperCategory 论文类别
     * @param authors 作者信息，key为作者排名，value为用户ID
     * @param communicationAuthorId 通讯作者ID
     * @return 各作者科研分，key为作者排名+用户ID，value为分数
     */
    @Override
    public Map<String, Integer> calculatePaperScore(String paperCategory, Map<String, String> authors, String communicationAuthorId) {
        Map<String, Integer> result = new HashMap<>();
        // System.out.println("=== 开始计算论文科研分 ===");
        // System.out.println("论文类别: " + paperCategory);
        // System.out.println("作者信息: " + authors);
        // System.out.println("通讯作者ID: " + communicationAuthorId);
        
        // 从配置中获取该论文类别的分数列表
        List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(paperCategory);
        // System.out.println("分数列表: " + point_list);
        
        if (point_list == null || point_list.isEmpty()) {
            // System.out.println("未找到该论文类别的分数配置");
            return result;
        }
        
        // 检查一作是否是本校老师
        String firstAuthorId = authors.get("1");
        boolean isFirstAuthorLocal = firstAuthorId != null && !firstAuthorId.equals("-1") && !firstAuthorId.equals("");
        // System.out.println("一作是否是本校老师: " + isFirstAuthorLocal);
        
        // 找出通讯作者的排名
        int correspondingAuthorRank = -1;
        // 只有当通讯作者ID不为空时，才查找通讯作者排名
        if (communicationAuthorId != null && !communicationAuthorId.equals("")) {
            for (Map.Entry<String, String> entry : authors.entrySet()) {
                String authorOrder = entry.getKey();
                String userId = entry.getValue();
                if (userId != null && userId.equals(communicationAuthorId)) {
                    correspondingAuthorRank = Integer.parseInt(authorOrder);
                    break;
                }
            }
        }
        // System.out.println("通讯作者排名: " + correspondingAuthorRank);
        
        // 获取实际的作者排名列表，过滤掉无效作者
        List<Integer> actualAuthorRanks = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String authorOrder = String.valueOf(i);
            String userId = authors.get(authorOrder);
            if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                actualAuthorRanks.add(i);
            }
        }
        // System.out.println("实际作者排名列表: " + actualAuthorRanks);
        
        // 计算各作者分数
        for (int i = 1; i <= 4; i++) {
            String authorOrder = String.valueOf(i);
            String userId = authors.get(authorOrder);
            // System.out.println("处理作者: " + authorOrder + ", 用户ID: " + userId);
            
            int score = 0;
            if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                boolean isCorrespondingAuthor = userId.equals(communicationAuthorId);
                // System.out.println("是否是通讯作者: " + isCorrespondingAuthor);
                
                // 确定该作者应得的分数位置
                int scorePosition = 1;
                
                // 特殊情况：一作同时是通讯作者，保持一作分数不变
                if (i == 1 && isCorrespondingAuthor) {
                    scorePosition = 1;
                    // System.out.println("一作同时是通讯作者，保持一作分数不变，分数位置: " + scorePosition);
                }
                // 无通讯作者情况
                else if (correspondingAuthorRank == -1) {
                    if (isFirstAuthorLocal) {
                        // 一作是本校老师，直接按作者排名计算分数
                        scorePosition = i;
                        // System.out.println("无通讯作者，一作是本校老师，按作者排名计算分数位置: " + scorePosition);
                    } else {
                        // 一作不是本校老师，其他作者的分数从一作位置开始
                        if (i == 1) {
                            // 一作不是本校老师，不给分
                            scorePosition = -1;
                            // System.out.println("无通讯作者，一作不是本校老师，一作分数位置: -1");
                        } else {
                            // 其他作者，分数从一作位置开始
                            scorePosition = i - 1;
                            // System.out.println("无通讯作者，一作不是本校老师，作者分数位置: " + scorePosition);
                        }
                    }
                } 
                // 有通讯作者情况
                else {
                    if (isCorrespondingAuthor) {
                        // 通讯作者的分数位置
                        if (isFirstAuthorLocal) {
                            // 一作是本校老师，通讯作者按二作分数算
                            scorePosition = 2;
                            // System.out.println("一作是本校老师，通讯作者分数位置: 2");
                        } else {
                            // 一作不是本校老师，通讯作者按一作分数算
                            scorePosition = 1;
                            // System.out.println("一作不是本校老师，通讯作者分数位置: 1");
                        }
                    } 
                    // 普通作者的情况
                    else {
                        if (i == 1) {
                            // 一作
                            if (isFirstAuthorLocal) {
                                // 一作是本校老师，一作按一作分数算
                                scorePosition = 1;
                                // System.out.println("一作是本校老师，一作分数位置: 1");
                            } else {
                                // 一作不是本校老师，一作按0分算
                                scorePosition = -1;
                                // System.out.println("一作不是本校老师，一作分数位置: -1");
                            }
                        } else {
                            // 其他普通作者
                            if (isFirstAuthorLocal) {
                                // 一作是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i + 1
                                    scorePosition = i + 1;
                                    // System.out.println("一作是本校老师，作者在通讯作者之前，分数位置: " + scorePosition);
                                } else {
                                    // 作者在通讯作者之后，分数位置 = i
                                    scorePosition = i;
                                    // System.out.println("一作是本校老师，作者在通讯作者之后，分数位置: " + scorePosition);
                                }
                            } else {
                                // 一作不是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i
                                    scorePosition = i;
                                    // System.out.println("一作不是本校老师，作者在通讯作者之前，分数位置: " + scorePosition);
                                } else if (i > correspondingAuthorRank) {
                                    // 作者在通讯作者之后，分数位置 = i - 1
                                    scorePosition = i - 1;
                                    // System.out.println("一作不是本校老师，作者在通讯作者之后，分数位置: " + scorePosition);
                                } else {
                                    // 作者是通讯作者，已经在上面处理
                                }
                            }
                        }
                    }
                }
                
                // 转换为索引，从0开始
                int targetIndex = scorePosition - 1;
                
                // 确保索引不越界
                if (scorePosition == -1) {
                    // 一作不是本校老师，一作不给分
                    score = 0;
                    // System.out.println("一作不是本校老师，一作分数: 0");
                } else if (targetIndex >= 0 && targetIndex < point_list.size()) {
                    score = point_list.get(targetIndex);
                    // System.out.println("作者分数: " + score + " (分数位置: " + scorePosition + ", 目标索引: " + targetIndex + ")");
                } else {
                    // 如果越界，取最后一个分数
                    score = point_list.get(point_list.size() - 1);
                    // System.out.println("作者分数(越界处理): " + score + " (分数位置: " + scorePosition + ", 目标索引: " + targetIndex + ")");
                }
            } else {
                // System.out.println("用户ID无效，不给分");
            }
            
            // 存储结果，key为作者排名+用户ID
            String key = authorOrder + "_" + userId;
            result.put(key, score);
            // System.out.println("存储结果: " + key + " => " + score);
        }
        
        // 特殊处理：当一作不是本校老师且存在通讯作者时，重新计算通讯作者之前的普通作者分数
        if (!isFirstAuthorLocal && correspondingAuthorRank > -1) {
            // System.out.println("=== 特殊处理：一作不是本校老师且存在通讯作者 ===");
            // 遍历所有作者，重新计算通讯作者之前的普通作者分数
            for (int i = 1; i <= 4; i++) {
                String authorOrder = String.valueOf(i);
                String userId = authors.get(authorOrder);
                if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                    boolean isCorrespondingAuthor = userId.equals(communicationAuthorId);
                    if (!isCorrespondingAuthor && i < correspondingAuthorRank) {
                        // 作者在通讯作者之前且不是通讯作者
                        // 重新计算分数位置：当前排名
                        int newScorePosition = i;
                        // 转换为索引，从0开始
                        int newTargetIndex = newScorePosition - 1;
                        int newScore = 0;
                        if (newTargetIndex >= 0 && newTargetIndex < point_list.size()) {
                            newScore = point_list.get(newTargetIndex);
                        } else {
                            newScore = point_list.get(point_list.size() - 1);
                        }
                        String key = authorOrder + "_" + userId;
                        // System.out.println("重新计算作者: " + authorOrder + ", 用户ID: " + userId + ", 新分数位置: " + newScorePosition + ", 新分数: " + newScore);
                        result.put(key, newScore);
                    }
                }
            }
        }
        
        // System.out.println("=== 分数计算完成 ===");
        // System.out.println("最终结果: " + result);
        
        return result;
    }
    
    /**
     * 计算论文作者科研分
     * 计分逻辑：
     * 1. 如果一作不是本校老师，通讯作者是本校老师，那么通讯作者按一作分算，其他顺延
     * 2. 如果一作是本校老师，通讯作者按二作分数算，其他顺延
     * @param id 论文ID
     * @param point_list 分数列表
     * @return 更新结果数量
     */
    public int setPaperUserScore (String id, List<Integer> point_list){
        List<Paper_user_score> paperUserScores = paperUserScoreServiceImplMapper.getpaperUserScoreListByPaperId(Long.valueOf(id));

        // 检查是否存在第一作者（author_order = 1）
        boolean hasFirstAuthor = paperUserScores.stream()
                .anyMatch(score -> "1".equals(score.getAuthorOrder()));
        
        // 检查一作是否是本校老师
        Paper_user_score firstAuthor = paperUserScores.stream()
                .filter(score -> "1".equals(score.getAuthorOrder()))
                .findFirst()
                .orElse(null);
        boolean isFirstAuthorLocal = firstAuthor != null && !firstAuthor.getAuthorLevel().equals("-1");
        
        // 找出通讯作者
        Paper_user_score correspondingAuthor = paperUserScores.stream()
                .filter(score -> "0".equals(score.getAuthorLevel()))
                .findFirst()
                .orElse(null);
        boolean hasCorrespondingAuthor = correspondingAuthor != null;

        AtomicInteger res = new AtomicInteger(0); // 使用 AtomicInteger 替代 int
        // 先处理所有普通作者，保存一作的分数
        Map<Long, Integer> originalScores = new HashMap<>();
        paperUserScores.forEach(score -> {
            try {
                // 先处理普通作者，保存分数
                if (!"0".equals(score.getAuthorLevel())) {
                    // 普通作者
                    String authorOrder = score.getAuthorOrder();
                    int authorIndex = Integer.parseInt(authorOrder) - 1;
                    
                    int calculatedScore = 0;
                    if (!isFirstAuthorLocal) {
                        // 一作不是本校老师，其他作者顺延
                        if (authorIndex == 0) {
                            // 一作不是本校老师，不给分
                            calculatedScore = 0;
                        } else {
                            // 其他作者位置减1
                            int newIndex = Math.max(0, authorIndex - 1);
                            calculatedScore = point_list.get(Math.min(newIndex, point_list.size() - 1));
                        }
                    } else {
                        // 一作是本校老师
                        if (hasCorrespondingAuthor) {
                            // 有通讯作者，其他作者按正常顺序计算
                            calculatedScore = point_list.get(Math.min(authorIndex, point_list.size() - 1));
                        } else {
                            // 没有通讯作者，按正常顺序计算
                            calculatedScore = point_list.get(Math.min(authorIndex, point_list.size() - 1));
                        }
                    }
                    // 保存计算出的分数
                    originalScores.put(score.getPusId(), calculatedScore);
                }
            } catch (NumberFormatException e) {
                // System.out.println("author_level或author_order不是有效数字: " + score.getAuthorLevel() + ", " + score.getAuthorOrder());
            }
        });
        
        // 然后处理通讯作者，注意避免覆盖一作的分数
        paperUserScores.forEach(score -> {
            try {
                if ("0".equals(score.getAuthorLevel())) {
                    // 检查该通讯作者是否同时是一作
                    boolean isFirstAndCorresponding = paperUserScores.stream()
                            .anyMatch(s -> s.getPusId().equals(score.getPusId()) && "1".equals(s.getAuthorOrder()));
                    
                    if (isFirstAndCorresponding) {
                        // 一作同时是通讯作者，保持一作的分数不变
                        Integer points = originalScores.get(score.getPusId());
                        if (points != null) {
                            int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                            res.addAndGet(i);
                        }
                    } else {
                        // 普通通讯作者，按原有逻辑处理
                        if (!hasFirstAuthor && !point_list.isEmpty()) {
                            // 如果没有第一作者，通讯作者按第一作者分数计算
                            Integer points = point_list.get(0);
                            int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                            res.addAndGet(i);
                        } else {
                            if (!isFirstAuthorLocal) {
                                // 一作不是本校老师，通讯作者按一作分数算
                                Integer points = point_list.get(0);
                                int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                                res.addAndGet(i);
                            } else {
                                // 一作是本校老师，通讯作者按二作分数算
                                Integer points = point_list.size() > 1 ? point_list.get(1) : point_list.get(0);
                                int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                                res.addAndGet(i);
                            }
                        }
                    }
                } else {
                    // 普通作者，使用之前计算的分数
                    Integer points = originalScores.get(score.getPusId());
                    if (points != null) {
                        int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                        res.addAndGet(i);
                    }
                }
            } catch (NumberFormatException e) {
                // System.out.println("author_level或author_order不是有效数字: " + score.getAuthorLevel() + ", " + score.getAuthorOrder());
            }
        });
        return res.get();
    }
    /**
     * 通过批阅点击驳回 , 或者通过撤回点击驳回
     * @param id 论文ID
     * @param userId 用户ID
     * @param remark 备注信息
     * @param urlFlag URL标识
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pybh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        SciPaperAr sciPaperAr = new SciPaperAr();
        if (urlFlag.equals("xytg")) {
            sciPaperAr.setState("学院驳回");
            state = "5";
        } else if (urlFlag.equals("xyth")) {
            sciPaperAr.setState("学院撤回");
            state = "2";
        } else if (urlFlag.equals("pro")) {
            sciPaperAr.setState("教研室驳回");
            state = "3";
        } else if (urlFlag.equals("proth")) {
            sciPaperAr.setState("教研室撤回");
            state = "1";
        } else if (urlFlag.equals("kytg")) {
            sciPaperAr.setState("科研处驳回");
            state = "7";
        } else if (urlFlag.equals("kyth")) {
            sciPaperAr.setState("科研处撤回");
            state = "4";
            int points = 0;
            int b = paperUserScoreServiceImplMapper.updateScoreByPaperId(Long.valueOf(id), points);
        }
        int a = sciPaperAMapper.pytg(id, state);

        sciPaperAr.setUid(userId);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        sciPaperAr.setConcate(remark);

        sciPaperAMapper.insertSciPaperAr(sciPaperAr);
        return a;
    }

    @Override
    public List<SciPaperAr> selectSciPaperArList(SciPaperAr sciPaperAr) {
        return sciPaperAMapper.selectSciPaperArList(sciPaperAr);
    }

    @Override
    public List<SciPaperA> selectAllPaperName(String query) {
        return sciPaperAMapper.selectAllPaperName(query);
    }

    /**
     * 检查论文是否存在
     * @param paper 论文实体
     * @return 存在数量
     */
    @Override
    public Integer selectSciPaperA(SciPaperA paper) {
        return sciPaperAMapper.selectSciPaperA(paper);
    }

    @Override
    public List<SciPaperA> getStatsQuery(Map<String, String> params) {
        return sciPaperAMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "sd",userAlias = "su")
    public List<SciPaperA> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "sd", "su", "");
        return sciPaperAMapper.getStatsQueryToCheck(params);
    }
}
