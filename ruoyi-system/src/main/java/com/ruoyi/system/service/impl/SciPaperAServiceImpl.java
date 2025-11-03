package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.common.annotation.DataScope;
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
    @Transactional
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

    @Override
    public int updateSciPaperAState(Integer id) {
        int a = sciPaperAMapper.updateSciPaperAState(id);
        return a;
    }

    @Override
    public List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListCxList(sciPaperA);
    }

    @Override
    public List<SciPaperA> selectSciPaperArole(Long userId) {
        return sciPaperAMapper.selectSciPaperArole(userId);
    }

    /*通过uderId查*/
    @Override
    //  @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA) {
        return sciPaperAMapper.selectSciPaperAListCx(sciPaperA);
    }

    @Override
    // @DataScope(deptAlias = "d",userAlias = "u")
    public List<String> selectSciPaperAByroleId(Long userId) {
        //数据权限
        List<String> a = sciPaperAMapper.selectSciPaperAByroleId(userId);
        return a;
    }

    /**
     * 论文批阅点击通过
     * @param id
     * @param uid
     * @param urlFlag
     * @param order
     * @param user_order
     * @return
     */
    @Override
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
            // 原来积分设置
            //int points = sciPaperACfgMapper.selectSciPaperACfgPoints(order, user_order);
            //System.out.println("points = " + points);
            //int b = sciPaperAMapper.updateSciPaperArs(id, points);

            // 通过之后设置积分
            List<Integer> point_list = sciPaperACfgMapper.selectSciPaperACfgPointList(order);
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
     * 如果一作不是本校老师 ,那么通讯作者的分数就是一作的分数 , 无论这个通讯作者在这个论文中是几作
     * 如果一作是本校老师，不是通讯作者 , 那么通讯作者获得的分数是他原来在这个论文中担任几作的分数
     * @param id
     * @param point_list
     * @return
     */
    public int setPaperUserScore (String id, List<Integer> point_list){
        List<Paper_user_score> paperUserScores = paperUserScoreServiceImplMapper.getpaperUserScoreListByPaperId(Long.valueOf(id));

        // 检查是否存在第一作者（author_order = 1）
        boolean hasFirstAuthor = paperUserScores.stream()
                .anyMatch(score -> "1".equals(score.getAuthorOrder()));
        AtomicInteger res = new AtomicInteger(0); // 使用 AtomicInteger 替代 int
        paperUserScores.forEach(score -> {
            try {
                // 如果是通讯作者 (author_level = 0)
                //System.out.println("score.getAuthorLevel() = " + score.getAuthorLevel() + score.getAuthorLevel().equals("0"));
                if ("0".equals(score.getAuthorLevel())) {
                    // 如果没有第一作者，通讯作者按第一作者分数计算
                    if (!hasFirstAuthor && !point_list.isEmpty()) {
                        Integer points = point_list.get(0); // 第一作者分数
                        //System.out.println("通讯作者按第一作者计算, 分数: " + points);
                        int i = paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                        res.addAndGet(i); // 累加更新结果
                    } else {
                        // 有第一作者的情况下，通讯作者按正常顺序计算
                        int levelIndex = Integer.parseInt(score.getAuthorLevel());
                        if (levelIndex >= 0 && levelIndex < point_list.size()) {
                            Integer points = point_list.get(levelIndex);
                            //System.out.println("通讯作者, 对应分数: " + points);
                            int i =paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                            res.addAndGet(i); // 累加更新结果
                        }
                    }
                } else {
                    // 普通作者按原有逻辑处理
                    String authorLevel = score.getAuthorLevel();
                    int levelIndex = Integer.parseInt(authorLevel) - 1;

                    if (levelIndex >= 0 && levelIndex < point_list.size()) {
                        Integer points = point_list.get(levelIndex);
                        //System.out.println("作者等级: " + authorLevel + ", 对应分数: " + points);
                        int i =paperUserScoreServiceImplMapper.updateScoreById(score.getPusId(), points);
                        res.addAndGet(i);
                    } else {
                        System.out.println("作者等级 " + authorLevel + " 超出point_list范围");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("author_level不是有效数字: " + score.getAuthorLevel());
            }
        });
        return res.get();
    }
    /**
     * 通过批阅点击驳回 , 或者通过撤回点击驳回
     * @param id
     * @param userId
     * @param remark
     * @param urlFlag
     * @return
     */
    @Override
    public int pybh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        SciPaperAr sciPaperAr = new SciPaperAr();
        System.out.println("urlFlag = " + urlFlag);
        if (urlFlag.equals("xytg")  ) {
            sciPaperAr.setState("学院驳回");
            state = "5";
        } else if ( urlFlag.equals("xyth")) {
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
        }else if (urlFlag.equals("kyth")) {
            sciPaperAr.setState("科研处撤回");
            state = "4";
            int points = 0;
            int b = paperUserScoreServiceImplMapper.updateScoreByPaperId(Long.valueOf(id), points);
        }
        int a = sciPaperAMapper.pytg(id, state);

        sciPaperAr.setUid(userId);
        sciPaperAr.setAr_id(Integer.valueOf(id));
        //System.out.println("remark132131 = " + remark);

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

    @Override
    public Integer selectSciPaperA(SciPaperA paper) {
        System.out.println("--------------------------");
        Integer a = sciPaperAMapper.selectSciPaperA(paper);
        System.out.println("a = " + a);
        return a;
    }

    @Override
    public List<SciPaperA> getStatsQuery(Map<String, String> params) {
        return sciPaperAMapper.getStatsQuery(params);
    }
}
