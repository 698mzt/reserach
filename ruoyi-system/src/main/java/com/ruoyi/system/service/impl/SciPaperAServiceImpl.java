package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.PaperUserScoreServiceMapper;
import com.ruoyi.system.mapper.SciPaperACfgMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciPaperAMapper;
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
    private static final String PAPER_PROCESS_CODE = "PAPER_APPROVAL";

    @Autowired
    private SciPaperAMapper sciPaperAMapper;
    @Autowired
    private SciPaperACfgMapper sciPaperACfgMapper;
    @Autowired
    private PaperUserScoreServiceMapper paperUserScoreServiceImplMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IApprovalProcessService approvalProcessService;
    @Autowired
    private IPageRenderService pageRenderService;

    /**
     * 查询论文
     *
     * @param id 论文主键
     * @return 论文
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public SciPaperA selectSciPaperAById(Long id) {
        SciPaperA paper = sciPaperAMapper.selectSciPaperAById(id);
        if (paper != null) {
            SysUser currentUser = ShiroUtils.getSysUser();
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(),
                    pageRenderService.buildCurrentPermissions(currentUser));
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return paper;
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
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAList(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        // 为每条论文记录计算并填充分数，并填充页面渲染数据
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListAll(SciPaperA sciPaperA) {
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAListAll(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SciPaperA> selectSciPaperAExport(List<String> ListRowId, SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAExport(ListRowId, sciPaperA);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListKY(SciPaperA sciPaperA) {
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAListKY(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListXY(SciPaperA sciPaperA) {
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAListXY(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
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
     * 
     * @param id 论文ID
     * @return 更新结果
     */
    @Override
    public int updateSciPaperAState(Integer id) {
        return sciPaperAMapper.updateSciPaperAState(id);
    }

    /**
     * 查询论文列表（查询列表）
     * 
     * @param sciPaperA 论文实体
     * @return 论文列表
     */
    @Override
    @DataScope(deptAlias = "pt", userAlias = "u")
    public List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA) {
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAListCxList(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
    }

    /**
     * 根据用户ID查询论文角色
     * 
     * @param userId 用户ID
     * @return 论文列表
     */
    @Override
    public List<SciPaperA> selectSciPaperArole(Long userId) {
        return sciPaperAMapper.selectSciPaperArole(userId);
    }

    /**
     * 根据用户ID查询论文列表
     * 
     * @param sciPaperA 论文实体
     * @return 论文列表
     */
    @Override
    public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA) {
        List<SciPaperA> list = sciPaperAMapper.selectSciPaperAListCx(sciPaperA);
        SysUser currentUser = ShiroUtils.getSysUser();
        Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
        for (SciPaperA paper : list) {
            calculateAndFillScores(paper);
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    "PAPER", "system:paper", "PAPER_APPROVAL",
                    paper.getState(), paper.getId(), paper.getUserId(), permissions);
            paper.setStatusMeta(result.getStatusMeta());
            paper.setActions(result.getActions());
        }
        return list;
    }

    /**
     * 根据用户ID查询角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<String> selectSciPaperAByroleId(Long userId) {
        return sciPaperAMapper.selectSciPaperAByroleId(userId);
    }

    /**
     * 论文审批通过
     * 
     * @param id      论文ID
     * @param userId  用户ID
     * @param comment 审批意见
     * @param order   论文类别
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pytg(String id, Long userId, String comment, String order) {
        SciPaperA paper = sciPaperAMapper.selectSciPaperAById(Long.valueOf(id));
        if (paper == null) {
            return -1;
        }

        String currentState = paper.getState();
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        ApprovalRequest approveRequest = ApprovalRequest.of(PAPER_PROCESS_CODE,
                Long.valueOf(id), currentState, comment,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        ApprovalResult result = approvalProcessService.approve(approveRequest);

        if (!result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();
        boolean isLast = result.isLast();

        // 科研处审批通过时计算科研分
        if (isLast) {
            List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(order);
            int res = setPaperUserScore(id, point_list);
            if (res < 1 || res > 4) {
                return -1;
            }

            if (!point_list.isEmpty()) {
                int researchScore = point_list.get(0);
                sciPaperAMapper.updatePaperResearchScore(Long.valueOf(id), researchScore);
            }
        }

        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setUid(userId);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        sciPaperAr.setState("通过");
        sciPaperAr.setConcate(comment != null && !comment.isEmpty() ? comment : "通过");
        sciPaperAMapper.insertSciPaperAr(sciPaperAr);

        return 1;
    }

    /**
     * 实时计算论文科研分
     * 
     * @param paperCategory         论文类别
     * @param authors               作者信息，key为作者排名，value为用户ID
     * @param communicationAuthorId 通讯作者ID
     * @return 各作者科研分，key为作者排名+用户ID，value为分数
     */
    @Override
    public Map<String, Integer> calculatePaperScore(String paperCategory, Map<String, String> authors,
            String communicationAuthorId) {
        Map<String, Integer> result = new HashMap<>();

        // 从配置中获取该论文类别的分数列表
        List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(paperCategory);

        if (point_list == null || point_list.isEmpty()) {
            return result;
        }

        // 检查一作是否是本校老师
        String firstAuthorId = authors.get("1");
        boolean isFirstAuthorLocal = firstAuthorId != null && !firstAuthorId.equals("-1") && !firstAuthorId.equals("");

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

        // 获取实际的作者排名列表，过滤掉无效作者
        List<Integer> actualAuthorRanks = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String authorOrder = String.valueOf(i);
            String userId = authors.get(authorOrder);
            if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                actualAuthorRanks.add(i);
            }
        }

        // 计算各作者分数
        for (int i = 1; i <= 4; i++) {
            String authorOrder = String.valueOf(i);
            String userId = authors.get(authorOrder);

            int score = 0;
            if (userId != null && !userId.equals("-1") && !userId.equals("")) {
                boolean isCorrespondingAuthor = userId.equals(communicationAuthorId);

                // 确定该作者应得的分数位置
                int scorePosition = 1;

                // 特殊情况：一作同时是通讯作者，保持一作分数不变
                if (i == 1 && isCorrespondingAuthor) {
                    scorePosition = 1;
                }
                // 无通讯作者情况
                else if (correspondingAuthorRank == -1) {
                    if (isFirstAuthorLocal) {
                        // 一作是本校老师，直接按作者排名计算分数
                        scorePosition = i;
                    } else {
                        // 一作不是本校老师，其他作者的分数从一作位置开始
                        if (i == 1) {
                            // 一作不是本校老师，不给分
                            scorePosition = -1;
                        } else {
                            // 其他作者，分数从一作位置开始
                            scorePosition = i - 1;
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
                        } else {
                            // 一作不是本校老师，通讯作者按一作分数算
                            scorePosition = 1;
                        }
                    }
                    // 普通作者的情况
                    else {
                        if (i == 1) {
                            // 一作
                            if (isFirstAuthorLocal) {
                                // 一作是本校老师，一作按一作分数算
                                scorePosition = 1;
                            } else {
                                // 一作不是本校老师，一作按0分算
                                scorePosition = -1;
                            }
                        } else {
                            // 其他普通作者
                            if (isFirstAuthorLocal) {
                                // 一作是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i + 1
                                    scorePosition = i + 1;
                                } else {
                                    // 作者在通讯作者之后，分数位置 = i
                                    scorePosition = i;
                                }
                            } else {
                                // 一作不是本校老师的情况
                                if (i < correspondingAuthorRank) {
                                    // 作者在通讯作者之前，分数位置 = i
                                    scorePosition = i;
                                } else if (i > correspondingAuthorRank) {
                                    // 作者在通讯作者之后，分数位置 = i - 1
                                    scorePosition = i - 1;
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
                } else if (targetIndex >= 0 && targetIndex < point_list.size()) {
                    score = point_list.get(targetIndex);
                } else {
                    // 如果越界，取最后一个分数
                    score = point_list.get(point_list.size() - 1);
                }
            }

            // 存储结果，key为作者排名+用户ID
            String key = authorOrder + "_" + userId;
            result.put(key, score);
        }

        // 特殊处理：当一作不是本校老师且存在通讯作者时，重新计算通讯作者之前的普通作者分数
        if (!isFirstAuthorLocal && correspondingAuthorRank > -1) {
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
                        result.put(key, newScore);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 计算论文作者科研分
     * 计分逻辑：
     * 1. 如果一作不是本校老师，通讯作者是本校老师，那么通讯作者按一作分算，其他顺延
     * 2. 如果一作是本校老师，通讯作者按二作分数算，其他顺延
     * 
     * @param id         论文ID
     * @param point_list 分数列表
     * @return 更新结果数量
     */
    public int setPaperUserScore(String id, List<Integer> point_list) {
        List<Paper_user_score> paperUserScores = paperUserScoreServiceImplMapper
                .getpaperUserScoreListByPaperId(Long.valueOf(id));

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
            }
        });
        return res.get();
    }

    /**
     * 论文审批驳回或撤回
     * 
     * @param id            论文ID
     * @param userId        用户ID
     * @param remark        备注信息
     * @param operationType 操作类型：reject(驳回) 或 recall(撤回)
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pybh(String id, Long userId, String remark, String operationType) {
        SciPaperA paper = sciPaperAMapper.selectSciPaperAById(Long.valueOf(id));
        if (paper == null) {
            return -1;
        }

        String currentState = paper.getState();
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        ApprovalResult result = null;
        if ("reject".equals(operationType)) {
            ApprovalRequest rejectRequest = ApprovalRequest.of(PAPER_PROCESS_CODE,
                    Long.valueOf(id), currentState, remark,
                    userId, user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "");
            result = approvalProcessService.reject(rejectRequest);
        } else if ("recall".equals(operationType)) {
            ApprovalRequest recallRequest = ApprovalRequest.of(PAPER_PROCESS_CODE,
                    Long.valueOf(id), currentState, remark,
                    userId, user.getUserName(),
                    user.getDept() != null ? user.getDept().getDeptName() : "");
            result = approvalProcessService.recall(recallRequest);
        }

        if (result == null || !result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();

        // 科研处撤回时清空科研分
        if ("recall".equals(operationType) && "PAPER_KYC_AUDIT".equals(newState)) {
            int points = 0;
            paperUserScoreServiceImplMapper.updateScoreByPaperId(Long.valueOf(id), points);
        }

        int a = sciPaperAMapper.pytg(id, newState);

        // 记录审批意见
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setUid(userId);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        if ("reject".equals(operationType)) {
            sciPaperAr.setState("修改");
            sciPaperAr.setConcate(remark != null && !remark.isEmpty() ? remark : "修改");
        } else if ("recall".equals(operationType)) {
            sciPaperAr.setState("撤回");
            sciPaperAr.setConcate(remark != null && !remark.isEmpty() ? remark : "撤回");
        }
        sciPaperAMapper.insertSciPaperAr(sciPaperAr);

        return a;
    }

    /**
     * 论文审批操作（通过/驳回/撤回）
     * @param id 论文ID
     * @param userId 用户ID
     * @param comment 审批意见
     * @param operationType 操作类型：approve(通过)、reject(驳回)、recall(撤回)
     * @param order 论文类别（仅通过时需要）
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(String id, Long userId, String comment, String operationType, String order) {
        SciPaperA paper = sciPaperAMapper.selectSciPaperAById(Long.valueOf(id));
        if (paper == null) {
            return -1;
        }

        String currentState = paper.getState();
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        ApprovalResult result = null;
        ApprovalRequest request = ApprovalRequest.of(PAPER_PROCESS_CODE,
                Long.valueOf(id), currentState, comment,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        if ("approve".equals(operationType)) {
            result = approvalProcessService.approve(request);
        } else if ("reject".equals(operationType)) {
            result = approvalProcessService.reject(request);
        } else if ("recall".equals(operationType)) {
            result = approvalProcessService.recall(request);
        }

        if (result == null || !result.isSuccess()) {
            return -1;
        }

        String newState = result.getNewState();

        // 驳回操作直接设置为PAPER_REJECTED状态，不进入下一审批节点
        if ("reject".equals(operationType)) {
            newState = SciPaperA.PAPER_REJECTED;
        }

        if ("approve".equals(operationType)) {
            boolean isLast = result.isLast();
            if (isLast) {
                List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(order);
                int res = setPaperUserScore(id, point_list);
                if (res < 1 || res > 4) {
                    return -1;
                }

                if (!point_list.isEmpty()) {
                    int researchScore = point_list.get(0);
                    sciPaperAMapper.updatePaperResearchScore(Long.valueOf(id), researchScore);
                }
            }
        } else if ("recall".equals(operationType) && "PAPER_KYC_AUDIT".equals(newState)) {
            int points = 0;
            paperUserScoreServiceImplMapper.updateScoreByPaperId(Long.valueOf(id), points);
        }

        int a = sciPaperAMapper.pytg(id, newState);

        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setUid(userId);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        if ("approve".equals(operationType)) {
            sciPaperAr.setState("通过");
            sciPaperAr.setConcate(comment != null && !comment.isEmpty() ? comment : "通过");
        } else if ("reject".equals(operationType)) {
            sciPaperAr.setState("修改");
            sciPaperAr.setConcate(comment != null && !comment.isEmpty() ? comment : "修改");
        } else if ("recall".equals(operationType)) {
            sciPaperAr.setState("撤回");
            sciPaperAr.setConcate(comment != null && !comment.isEmpty() ? comment : "撤回");
        }
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
     * 
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
    @DataScope(deptAlias = "sd", userAlias = "su")
    public List<SciPaperA> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "sd", "su", "");
        return sciPaperAMapper.getStatsQueryToCheck(params);
    }

    /**
     * 计算并填充分数到论文对象
     * 根据论文类别和作者信息，计算每个作者的应得分数
     * 
     * @param paper 论文对象
     */
    private void calculateAndFillScores(SciPaperA paper) {
        if (paper.getPaperCategory() == null || paper.getPaperCategory().isEmpty()) {
            return;
        }

        Map<String, String> authors = new HashMap<>();
        String firstPersonId = paper.getFirstPersonId();
        String secondPersonId = paper.getSecondPersonId();
        String thirdPersonId = paper.getThirdPersonId();
        String fourthPersonId = paper.getFourthPersonId();
        String communicationAuthorId = paper.getCommunicationAuthorId();

        if (firstPersonId != null && !firstPersonId.isEmpty()) {
            authors.put("1", firstPersonId);
        }
        if (secondPersonId != null && !secondPersonId.isEmpty()) {
            authors.put("2", secondPersonId);
        }
        if (thirdPersonId != null && !thirdPersonId.isEmpty()) {
            authors.put("3", thirdPersonId);
        }
        if (fourthPersonId != null && !fourthPersonId.isEmpty()) {
            authors.put("4", fourthPersonId);
        }

        Map<String, Integer> scores = calculatePaperScore(paper.getPaperCategory(), authors, communicationAuthorId);

        if (firstPersonId != null && !firstPersonId.isEmpty()) {
            String firstKey = "1_" + firstPersonId;
            paper.setFirstAuthorScore(String.valueOf(scores.getOrDefault(firstKey, 0)));
        } else {
            paper.setFirstAuthorScore("0");
        }

        if (secondPersonId != null && !secondPersonId.isEmpty()) {
            String secondKey = "2_" + secondPersonId;
            paper.setSecondAuthorScore(String.valueOf(scores.getOrDefault(secondKey, 0)));
        } else {
            paper.setSecondAuthorScore("0");
        }

        if (thirdPersonId != null && !thirdPersonId.isEmpty()) {
            String thirdKey = "3_" + thirdPersonId;
            paper.setThirdAuthorScore(String.valueOf(scores.getOrDefault(thirdKey, 0)));
        } else {
            paper.setThirdAuthorScore("0");
        }

        if (fourthPersonId != null && !fourthPersonId.isEmpty()) {
            String fourthKey = "4_" + fourthPersonId;
            paper.setFourthAuthorScore(String.valueOf(scores.getOrDefault(fourthKey, 0)));
        } else {
            paper.setFourthAuthorScore("0");
        }

        Integer correspondingScore = 0;
        if (communicationAuthorId != null && !communicationAuthorId.isEmpty()) {
            if (communicationAuthorId.equals(firstPersonId)) {
                String firstKey = "1_" + firstPersonId;
                correspondingScore = scores.getOrDefault(firstKey, 0);
            } else if (communicationAuthorId.equals(secondPersonId)) {
                String secondKey = "2_" + secondPersonId;
                correspondingScore = scores.getOrDefault(secondKey, 0);
            } else if (communicationAuthorId.equals(thirdPersonId)) {
                String thirdKey = "3_" + thirdPersonId;
                correspondingScore = scores.getOrDefault(thirdKey, 0);
            } else if (communicationAuthorId.equals(fourthPersonId)) {
                String fourthKey = "4_" + fourthPersonId;
                correspondingScore = scores.getOrDefault(fourthKey, 0);
            } else {
                correspondingScore = calculateCorrespondingAuthorScore(paper.getPaperCategory(), authors,
                        communicationAuthorId);
            }
        }
        paper.setCorrespondingAuthorScore(String.valueOf(correspondingScore));
    }

    /**
     * 计算通讯作者分数
     * 当通讯作者不在1-4作中时，根据规则计算分数
     * 
     * @param paperCategory         论文类别
     * @param authors               作者信息
     * @param communicationAuthorId 通讯作者ID
     * @return 通讯作者分数
     */
    private Integer calculateCorrespondingAuthorScore(String paperCategory, Map<String, String> authors,
            String communicationAuthorId) {
        // 从配置中获取该论文类别的分数列表
        List<Integer> pointList = sciPaperACfgMapper.selectSciPaperACfgPointList(paperCategory);

        if (pointList == null || pointList.isEmpty()) {
            return 0;
        }

        // 检查一作是否是本校老师
        String firstAuthorId = authors.get("1");
        boolean isFirstAuthorLocal = firstAuthorId != null && !firstAuthorId.equals("-1") && !firstAuthorId.isEmpty();

        // 根据规则计算通讯作者分数
        if (!isFirstAuthorLocal) {
            // 一作不是本校老师，通讯作者按一作分数算
            return pointList.get(0);
        } else {
            // 一作是本校老师，通讯作者按二作分数算
            return pointList.size() > 1 ? pointList.get(1) : pointList.get(0);
        }
    }

}
