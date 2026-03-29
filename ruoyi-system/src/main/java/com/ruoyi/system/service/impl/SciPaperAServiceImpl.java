package com.ruoyi.system.service.impl;

import java.util.*;
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
     * 根据论文类别获取对应分数配置（新论文科研分标准）
     * @param paperCategory 论文类别（字典值）
     * @return 分数列表 [第一作者分, 第二作者分, 第三作者分, 第四作者分]
     */
    private List<Integer> getScoreConfigByCategory(String paperCategory) {
        Map<String, List<Integer>> scoreConfigMap = new HashMap<>();
        
        // SCI 分区
        scoreConfigMap.put("1", Arrays.asList(2000, 760, 400, 200));  // SCI I区
        scoreConfigMap.put("2", Arrays.asList(1000, 380, 200, 100));  // SCI II区
        scoreConfigMap.put("3", Arrays.asList(800, 320, 160, 80));    // SCI III区
        scoreConfigMap.put("4", Arrays.asList(480, 200, 100, 50));    // SCI IV区
        
        // 其他类别
        scoreConfigMap.put("5", Arrays.asList(400, 180, 80, 40));     // EI期刊论文
        scoreConfigMap.put("6", Arrays.asList(280, 120, 60, 30));     // EI期刊论文（会议）
        scoreConfigMap.put("7", Arrays.asList(280, 120, 60, 30));     // 核心期刊
        scoreConfigMap.put("8", Arrays.asList(200, 0, 0, 0));         // 三网收录（只有第一作者有分）
        
        return scoreConfigMap.getOrDefault(paperCategory, Arrays.asList(0, 0, 0, 0));
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
        
        // 从数据库获取分数配置
        List<Integer> pointList = sciPaperACfgMapper.selectSciPaperACfgPointList(paperCategory);
        
        // 处理分数数据为空的边界情况
        if (pointList == null || pointList.isEmpty()) {
            // 为所有作者返回0分
            for (int i = 1; i <= 4; i++) {
                String authorOrder = String.valueOf(i);
                String userId = authors.get(authorOrder);
                if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                    String key = authorOrder + "_" + userId;
                    result.put(key, 0);
                }
            }
            return result;
        }
        
        // 检查一作是否是本校老师
        String firstAuthorId = authors.get("1");
        boolean isFirstAuthorLocal = firstAuthorId != null && !firstAuthorId.equals("-1") && !firstAuthorId.equals("");
        
        // 找出通讯作者的排名
        int correspondingAuthorRank = -1;
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
        
        // 计算各作者分数
        for (int i = 1; i <= 4; i++) {
            String authorOrder = String.valueOf(i);
            String userId = authors.get(authorOrder);
            
            int score = 0;
            if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                boolean isCorrespondingAuthor = userId.equals(communicationAuthorId);
                
                // 特殊情况：一作同时是通讯作者，保持一作分数不变
                if (i == 1 && isCorrespondingAuthor) {
                    score = pointList.get(0);
                }
                // 无通讯作者情况
                else if (correspondingAuthorRank == -1) {
                    if (isFirstAuthorLocal) {
                        // 一作是本校老师，直接按作者排名计算分数
                        score = i <= pointList.size() ? pointList.get(i - 1) : pointList.get(pointList.size() - 1);
                    } else {
                        // 一作不是本校老师，其他作者的分数从一作位置开始
                        if (i == 1) {
                            score = 0;
                        } else {
                            int newIndex = Math.max(0, i - 2);
                            score = newIndex < pointList.size() ? pointList.get(newIndex) : pointList.get(pointList.size() - 1);
                        }
                    }
                } 
                // 有通讯作者情况
                else {
                    if (isCorrespondingAuthor) {
                        // 通讯作者的分数
                        if (isFirstAuthorLocal) {
                            // 规则二：一作是本校老师，通讯作者按二作分数算
                            score = pointList.size() > 1 ? pointList.get(1) : pointList.get(0);
                        } else {
                            // 规则一：一作不是本校老师，通讯作者按一作分数算
                            score = pointList.get(0);
                        }
                    } 
                    // 普通作者的情况
                    else {
                        if (i == 1) {
                            // 一作
                            if (isFirstAuthorLocal) {
                                score = pointList.get(0);
                            } else {
                                score = 0;
                            }
                        } else {
                            // 其他普通作者
                            if (isFirstAuthorLocal) {
                                // 一作是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i
                                    score = i <= pointList.size() ? pointList.get(i - 1) : pointList.get(pointList.size() - 1);
                                } else {
                                    // 作者在通讯作者之后，分数位置 = i + 1
                                    int newIndex = i;
                                    score = newIndex < pointList.size() ? pointList.get(newIndex) : pointList.get(pointList.size() - 1);
                                }
                            } else {
                                // 一作不是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i - 1
                                    int newIndex = i - 2;
                                    score = newIndex >= 0 && newIndex < pointList.size() ? pointList.get(newIndex) : 0;
                                } else if (i > correspondingAuthorRank) {
                                    // 作者在通讯作者之后，分数位置 = i - 1
                                    int newIndex = i - 2;
                                    score = newIndex >= 0 && newIndex < pointList.size() ? pointList.get(newIndex) : 0;
                                }
                            }
                        }
                    }
                }
            }
            
            // 存储结果，key为作者排名+用户ID
            String key = authorOrder + "_" + userId;
            result.put(key, score);
        }
        
        return result;
    }
    
    /**
     * 计算论文作者科研分
     * 计分逻辑：
     * 1. 如果一作不是本校老师，通讯作者是本校老师，那么通讯作者按一作分算，其他顺延
     * 2. 如果一作是本校老师，通讯作者按二作分数算，其他顺延
     * 3. 特殊情况：一作同时是通讯作者，保持一作分数不变
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
        
        // 先处理所有普通作者
        Map<Long, Integer> authorScores = new HashMap<>();
        
        // 处理普通作者
        for (Paper_user_score score : paperUserScores) {
            try {
                if (!"0".equals(score.getAuthorLevel())) {
                    // 普通作者
                    String authorOrder = score.getAuthorOrder();
                    int authorIndex = Integer.parseInt(authorOrder) - 1;
                    
                    int calculatedScore = 0;
                    
                    if (authorIndex == 0) {
                        // 一作
                        if (isFirstAuthorLocal) {
                            calculatedScore = point_list.get(0);
                        } else {
                            calculatedScore = 0;
                        }
                    } else {
                        // 其他普通作者
                        if (hasCorrespondingAuthor) {
                            // 有通讯作者
                            int correspondingAuthorIndex = -1;
                            for (Paper_user_score s : paperUserScores) {
                                if ("0".equals(s.getAuthorLevel())) {
                                    correspondingAuthorIndex = Integer.parseInt(s.getAuthorOrder()) - 1;
                                    break;
                                }
                            }
                            
                            if (authorIndex < correspondingAuthorIndex) {
                                // 作者在通讯作者之前
                                if (isFirstAuthorLocal) {
                                    calculatedScore = authorIndex < point_list.size() ? point_list.get(authorIndex) : point_list.get(point_list.size() - 1);
                                } else {
                                    int newIndex = authorIndex - 1;
                                    calculatedScore = newIndex >= 0 && newIndex < point_list.size() ? point_list.get(newIndex) : 0;
                                }
                            } else {
                                // 作者在通讯作者之后
                                if (isFirstAuthorLocal) {
                                    int newIndex = authorIndex + 1;
                                    calculatedScore = newIndex < point_list.size() ? point_list.get(newIndex) : point_list.get(point_list.size() - 1);
                                } else {
                                    int newIndex = authorIndex - 1;
                                    calculatedScore = newIndex >= 0 && newIndex < point_list.size() ? point_list.get(newIndex) : 0;
                                }
                            }
                        } else {
                            // 无通讯作者
                            if (isFirstAuthorLocal) {
                                calculatedScore = authorIndex < point_list.size() ? point_list.get(authorIndex) : point_list.get(point_list.size() - 1);
                            } else {
                                int newIndex = authorIndex - 1;
                                calculatedScore = newIndex >= 0 && newIndex < point_list.size() ? point_list.get(newIndex) : 0;
                            }
                        }
                    }
                    
                    authorScores.put(score.getPusId(), calculatedScore);
                }
            } catch (NumberFormatException e) {
            }
        }
        
        // 处理通讯作者
        for (Paper_user_score score : paperUserScores) {
            try {
                if ("0".equals(score.getAuthorLevel())) {
                    // 检查该通讯作者是否同时是一作
                    boolean isFirstAndCorresponding = false;
                    for (Paper_user_score s : paperUserScores) {
                        if (s.getPusId().equals(score.getPusId()) && "1".equals(s.getAuthorOrder())) {
                            isFirstAndCorresponding = true;
                            break;
                        }
                    }
                    
                    if (isFirstAndCorresponding) {
                        // 特殊情况：一作同时是通讯作者，保持一作分数不变
                        Integer points = authorScores.get(score.getPusId());
                        if (points != null) {
                            int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                            res.addAndGet(i);
                        }
                    } else {
                        // 普通通讯作者
                        int calculatedScore = 0;
                        if (isFirstAuthorLocal) {
                            // 规则二：一作是本校老师，通讯作者按二作分数算
                            calculatedScore = point_list.size() > 1 ? point_list.get(1) : point_list.get(0);
                        } else {
                            // 规则一：一作不是本校老师，通讯作者按一作分数算
                            calculatedScore = point_list.get(0);
                        }
                        int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), calculatedScore);
                        res.addAndGet(i);
                    }
                } else {
                    // 普通作者
                    Integer points = authorScores.get(score.getPusId());
                    if (points != null) {
                        int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                        res.addAndGet(i);
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
        
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
