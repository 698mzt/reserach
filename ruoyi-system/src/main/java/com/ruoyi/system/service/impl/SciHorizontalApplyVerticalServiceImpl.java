package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciHorizontalApplyVerticalMapper;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Collections;

import static com.ruoyi.common.utils.ShiroUtils.getSysUser;

/**
 * 纵向课题Service业务层处理
 *
 */
@Service
public class SciHorizontalApplyVerticalServiceImpl implements ISciHorizontalApplyVerticalService {

    @Autowired
    private SciHorizontalApplyVerticalMapper sciHorizontalApplyVerticalMapper;
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;
    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;
    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;

    /**
     * 查询纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 纵向课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalList(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
    }

    /**
     * 查询纵向课题列表（统一查询，支持所有状态）
     * 功能：统一查询纵向课题列表，支持多条件筛选和排序
     * 按照2026年度数据权限优化需求，所有管理员角色均可查看所有状态的课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListAll(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListAll(sciHorizontalApplyVertical);
    }

    /**
     * 保存立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        // 新增：插入前查重
        SciHorizontalApplyVertical query = new SciHorizontalApplyVertical();
        query.setTopName(sciHorizontalApplyVertical.getTopName());
        query.setTopNumber(sciHorizontalApplyVertical.getTopNumber());
        // 这里只查重名称和编号
        List<SciHorizontalApplyVertical> existList = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalList(query);
        if (existList != null && !existList.isEmpty()) {
            // 课题名称或编号已存在，返回-1
            return -1;
        }
        sciHorizontalApplyVerticalMapper.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical);
        Integer id = sciHorizontalApplyVertical.getId() ;
        SciHorizontalPersion sciHorizontalPersion = new SciHorizontalPersion();
        sciHorizontalPersion.setVerticalid(id);
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFirstPersonId())) {
            sciHorizontalPersion.setRanking("1");
            sciHorizontalPersion.setPersionid(sciHorizontalApplyVertical.getFirstPersonId());
            sciHorizontalApplyVerticalMapper.insertPersionVertical(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getSecondPersonId())) {
            sciHorizontalPersion.setRanking("2");
            sciHorizontalPersion.setPersionid(sciHorizontalApplyVertical.getSecondPersonId());
            sciHorizontalApplyVerticalMapper.insertPersionVertical(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getThirdPersonId())) {
            sciHorizontalPersion.setRanking("3");
            sciHorizontalPersion.setPersionid(sciHorizontalApplyVertical.getThirdPersonId());
            sciHorizontalApplyVerticalMapper.insertPersionVertical(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFourthPersonId())) {
            sciHorizontalPersion.setRanking("4");
            sciHorizontalPersion.setPersionid(sciHorizontalApplyVertical.getFourthPersonId());
            sciHorizontalApplyVerticalMapper.insertPersionVertical(sciHorizontalPersion);
        }
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApplyVertical.getUserId()));
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("新增数据");
        sciHorizontalPiyue.setState("新增");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        
        // 收集所有成员ID和对应的预期科研分
        Map<String, String> memberExpectedScores = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFirstPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore1())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getFirstPersonId(), sciHorizontalApplyVertical.getExpectedScore1());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getSecondPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore2())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getSecondPersonId(), sciHorizontalApplyVertical.getExpectedScore2());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getThirdPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore3())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getThirdPersonId(), sciHorizontalApplyVertical.getExpectedScore3());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFourthPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore4())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getFourthPersonId(), sciHorizontalApplyVertical.getExpectedScore4());
        }
        
        // 存储预期科研分
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(id.toString());
        sciUserScore.setScoreType("纵向课题");
        sciUserScore.setChangeStatus("立项");
        
        for (Map.Entry<String, String> entry : memberExpectedScores.entrySet()) {
            sciUserScore.setUserId(entry.getKey());
            sciUserScore.setExpectedValue(entry.getValue());
            // 初始积分设为0，审批通过时会更新
            sciUserScore.setChangeValue("0");
            sciUserScoreMapper.insertScoreVertical(sciUserScore);
        }
        
        return id;
    }

    /**
     * 保存纵向课题成员（从第5位开始）
     * 功能：保存纵向课题的成员列表，从第5位开始排序
     * SQL：DELETE FROM sci_persion_vertical WHERE verticalid = ?
     * SQL：INSERT INTO sci_persion_vertical
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVerticalPersons(Integer verticalId, java.util.List<String> personIds) {
        if (verticalId == null || personIds == null || personIds.isEmpty()){
            return;
        }
        // 先清空原有成员
        SciHorizontalPersion del = new SciHorizontalPersion();
        del.setVerticalid(verticalId);
        sciHorizontalApplyVerticalMapper.deletePersionVerticalByVerticalId(verticalId);
        SciHorizontalPersion rec = new SciHorizontalPersion();
        rec.setVerticalid(verticalId);
        int startRank = 5; // 从第5位开始
        for (int i = 0; i < personIds.size(); i++) {
            String pid = personIds.get(i);
            if (StringUtils.isEmpty(pid)) continue;
            rec.setRanking(String.valueOf(startRank + i));
            rec.setPersionid(pid);
            sciHorizontalApplyVerticalMapper.insertPersionVertical(rec);
        }
    }

    /**
     * 重置纵向课题成员
     * 功能：重置纵向课题的成员列表，先删除原有成员，再插入新成员
     * SQL：DELETE FROM sci_persion_vertical WHERE verticalid = ?
     * SQL：INSERT INTO sci_persion_vertical
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetVerticalPersons(Integer verticalId, java.util.List<String> personIds) {
        if (verticalId == null) {
            return;
        }

        sciHorizontalApplyVerticalMapper.deletePersionVerticalByVerticalId(verticalId);
        // 按顺序插入，ranking 从 1 开始
        SciHorizontalPersion rec = new SciHorizontalPersion();
        rec.setVerticalid(verticalId);
        for (int i = 0; i < personIds.size(); i++) {
            rec.setRanking(String.valueOf(i + 1));
            rec.setPersionid(personIds.get(i));
            sciHorizontalApplyVerticalMapper.insertPersionVertical(rec);
        }
    }

    /**
     * id查询立项申请
     *
     * @param id 纵向课题
     * @return 结果
     */
    @Override
    public SciHorizontalApplyVertical selectSciHorizontalApplyVerticalById(Integer id) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(id);
    }


    /**
     * 查询纵向课题成员ID列表
     * 功能：根据课题ID查询所有成员的ID列表
     * SQL：SELECT persionid FROM sci_persion_vertical WHERE verticalid = ? ORDER BY ranking
     */
    @Override
    public java.util.List<String> selectPersionIdsByVerticalId(Integer verticalId) {
        return sciHorizontalApplyVerticalMapper.selectPersionIdsByVerticalId(verticalId);
    }

    /**
     * 修改申请
     * 功能：更新纵向课题申请信息，添加审批记录
     * SQL：UPDATE sci_horizontal_apply_vertical
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(getSysUser().getUserId());
        sciHorizontalPiyue.setVerticalId(sciHorizontalApplyVertical.getId());
        switch (sciHorizontalApplyVertical.getNewsql()) {
//            草稿箱提交时的记录
            case "99":
            case "999":
                sciHorizontalPiyue.setConcate("提交申请");
                sciHorizontalPiyue.setState("提交");
                break;
//                申请结项的记录
            case "11":
                sciHorizontalPiyue.setConcate("申请结项");
                sciHorizontalPiyue.setState("提交");
                break;
            case "111":
                String newState = sciHorizontalApplyVertical.getState();
                switch (newState) {
                    // 教研室
                    case "33":
                        newState = "999";
                        break;
                    case "3":
                        newState = "99";
                        break;
                    // 学院
                    case "5":
                        newState = "99";
                        break;
                    case "55":
                        newState = "999";
                        break;
                    // 科研处
                    case "77":
                        newState = "999";
                        break;
                    case "7":
                        newState = "99";
                        break;
                    // 教师
                    case "99":
                        break;
                }
                sciHorizontalApplyVertical.setState(newState);
                sciHorizontalPiyue.setConcate("修改");
                sciHorizontalPiyue.setState("修改");
                break;
        }
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        
        // 更新预期科研分
        Integer verticalId = sciHorizontalApplyVertical.getId();
        // 先删除旧的预期科研分记录
//        sciUserScoreMapper.deleteVerticalScoreById(verticalId.toString(), "立项");
        
        // 收集所有成员ID和对应的预期科研分
        Map<String, String> memberExpectedScores = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFirstPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore1())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getFirstPersonId(), sciHorizontalApplyVertical.getExpectedScore1());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getSecondPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore2())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getSecondPersonId(), sciHorizontalApplyVertical.getExpectedScore2());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getThirdPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore3())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getThirdPersonId(), sciHorizontalApplyVertical.getExpectedScore3());
        }
        if (StringUtils.isNotEmpty(sciHorizontalApplyVertical.getFourthPersonId()) && StringUtils.isNotEmpty(sciHorizontalApplyVertical.getExpectedScore4())) {
            memberExpectedScores.put(sciHorizontalApplyVertical.getFourthPersonId(), sciHorizontalApplyVertical.getExpectedScore4());
        }
        
        // 存储预期科研分
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(verticalId.toString());
        sciUserScore.setScoreType("纵向课题");
        sciUserScore.setChangeStatus("立项");
        
        for (Map.Entry<String, String> entry : memberExpectedScores.entrySet()) {
            sciUserScore.setUserId(entry.getKey());
            sciUserScore.setExpectedValue(entry.getValue());
            // 初始积分设为0，审批通过时会更新
            sciUserScore.setChangeValue("0");
            sciUserScoreMapper.insertScoreVertical(sciUserScore);
        }
        
        return sciHorizontalApplyVerticalMapper.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
    }

    /**
     * 删除纵向课题信息
     *
     * @param ids 纵向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyVerticalByIds(String ids) {
        return sciHorizontalApplyVerticalMapper.deleteSciHorizontalApplyVerticalByIds(Convert.toStrArray(ids));
    }

    /**
     * 纵向课题申请通过
     * 功能：审批通过纵向课题申请，计算并分配科研分
     * SQL：DELETE FROM sci_user_score WHERE vertical_id = ? AND change_status = ?
     * SQL：INSERT INTO sci_user_score
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ?, subject_source = ? WHERE id = ?
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,SciHorizontalApplyVertical sciHorizontalApplyVertical1) {
        String state = "";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(verticalId);
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        if(urlFlag.equals("JYS")){
            state ="V_APPLY_KYC"; // 教研室审核通过后直接流转到科研处审核
        }else if(urlFlag.equals("KYC")){
            state ="V_APPLY_PASS";
//            if (persion.size() > score.size()) {
//                throw new RuntimeException("积分配置与成员数量不匹配");
//            }
            String status = "立项";
//            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
            sciUserScore.setChangeStatus("立项");
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                // 使用前端传递的预期科研分，如果前端没有传递，则使用从数据库中查询的值
                String expectedScore = "0";
                if (sciHorizontalApplyVertical1 != null) {
                    switch (i) {
                        case 0:
                            expectedScore = sciHorizontalApplyVertical1.getExpectedScore1();
                            break;
                        case 1:
                            expectedScore = sciHorizontalApplyVertical1.getExpectedScore2();
                            break;
                        case 2:
                            expectedScore = sciHorizontalApplyVertical1.getExpectedScore3();
                            break;
                        case 3:
                            expectedScore = sciHorizontalApplyVertical1.getExpectedScore4();
                            break;
                    }
                }
                // 如果前端没有传递预期科研分，则从数据库中查询
                if (StringUtils.isEmpty(expectedScore) && sciHorizontalApplyVertical != null) {
                    switch (i) {
                        case 0:
                            expectedScore = sciHorizontalApplyVertical.getExpectedScore1();
                            break;
                        case 1:
                            expectedScore = sciHorizontalApplyVertical.getExpectedScore2();
                            break;
                        case 2:
                            expectedScore = sciHorizontalApplyVertical.getExpectedScore3();
                            break;
                        case 3:
                            expectedScore = sciHorizontalApplyVertical.getExpectedScore4();
                            break;
                    }
                }
                if (StringUtils.isEmpty(expectedScore)) {
                    expectedScore = "0";
                }
                sciUserScore.setExpectedValue(expectedScore);
                sciUserScoreMapper.insertScoreVertical(sciUserScore);
            }
        }
        int a =  sciHorizontalApplyVerticalMapper.applyPass(id,state,sciHorizontalApplyVertical1 != null ? sciHorizontalApplyVertical1.getSubjectSource() : null);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        return a;
    }

    /**
     * 纵向课题申请驳回
     * 功能：驳回纵向课题申请，添加审批记录
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ? WHERE id = ?
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyBh(String id, Long userId, String remark, String urlFlag) {
        String state = "";
        if(urlFlag.equals("JYS")){
            state ="V_APPLY_REJ";
        }else if(urlFlag.equals("KYC")){
            state ="V_APPLY_REJ";
        }
        int a =  sciHorizontalApplyVerticalMapper.applyPass(id,state,null);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        return a;
    }



    /**
     * 纵向课题结项申请通过
     * 功能：审批通过纵向课题结项申请，计算并分配科研分
     * SQL：DELETE FROM sci_user_score WHERE vertical_id = ? AND change_status = ?
     * SQL：INSERT INTO sci_user_score
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ?, subject_source = ? WHERE id = ?
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int overPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,String subjectSource) {
        String state = "";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(verticalId);
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        if(urlFlag.equals("JYS")){
            state ="V_OVER_KYC"; // 教研室审批通过后直接流转到科研处
        }else if(urlFlag.equals("KYC")){
            state ="V_OVER_PASS";
            if (persion.size() > score.size()) {
                throw new RuntimeException("积分配置与成员数量不匹配");
            }
            String status = "结项";
//            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
            sciUserScore.setChangeStatus("结项");
            
            // 查询该课题的立项积分记录，获取预期科研分
            List<SciUserScore> projectScoreRecords = sciUserScoreMapper.selectScoreVerticalByApplyIds(Collections.singleton(Integer.valueOf(id)));
            // 构建用户ID到预期科研分的映射
            Map<String, String> expectedScoreMap = new HashMap<>();
            for (SciUserScore scoreRecord : projectScoreRecords) {
                if ("立项".equals(scoreRecord.getChangeStatus()) && scoreRecord.getExpectedValue() != null) {
                    expectedScoreMap.put(scoreRecord.getUserId(), scoreRecord.getExpectedValue());
                }
            }
            
            for (int i = 0; i < persion.size(); i++) {
                String memberId = persion.get(i).toString();
                sciUserScore.setUserId(memberId);
                sciUserScore.setChangeValue(score.get(i).toString());
                // 从立项积分记录中获取预期科研分
                String expectedScore = expectedScoreMap.getOrDefault(memberId, "0");
                sciUserScore.setExpectedValue(expectedScore);
                sciUserScoreMapper.insertScoreVertical(sciUserScore);
            }
        }else if(urlFlag.equals("Dept")){
            state ="V_OVER_KYC";
        }
        int a =  sciHorizontalApplyVerticalMapper.overPass(id,state,subjectSource);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        return a;
    }

    /**
     * 纵向课题结项申请驳回
     * 功能：驳回纵向课题结项申请，添加审批记录
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ? WHERE id = ?
     * SQL：INSERT INTO sci_horizontal_piyue
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int overBh(String id, Long userId, String remark, String urlFlag) {
        String state = "";
        if(urlFlag.equals("JYS")){
            state ="V_OVER_REJ";
        }else if(urlFlag.equals("KYC")){
            state ="V_OVER_REJ";
        }else if (urlFlag.equals("Dept")){
            state ="V_OVER_REJ";
        }
        int a =  sciHorizontalApplyVerticalMapper.overPass(id,state,null);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        return a;
    }




    /**
     * 查询纵向课题结项列表
     * 功能：根据角色查询纵向课题结项列表，支持多条件筛选
     * SQL：根据角色不同，执行不同的查询语句
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJX(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        if (sciHorizontalApplyVertical.getRole().equals("research"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJYSJX(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("sci_tesearch"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKYCJX(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("dept_teacher")){
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListDeptJX(sciHorizontalApplyVertical);
        }
        else
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
        return list;
    }

    /**
     * 查询纵向课题已完成结项列表
     * 功能：查询纵向课题已完成结项的列表，支持多条件筛选
     * SQL：SELECT * FROM sci_horizontal_apply_vertical WHERE state = 'V_OVER_PASS'
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListOVER(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);

    }

    /**
     * 导出纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalAllList(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalAllList(sciHorizontalApplyVertical);
    }

    /**
     * 撤回操作
     * 功能：撤回纵向课题申请，删除积分记录，添加审批记录
     * SQL：DELETE FROM sci_user_score WHERE vertical_id = ? AND change_status = ?
     * SQL：INSERT INTO sci_horizontal_piyue
     * SQL：UPDATE sci_horizontal_apply_vertical SET state = ? WHERE id = ?
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recall(Integer id, String state, Long userId, String remark, String urlFlag) {
        String newState = state;
        switch (state){
//            立项申请 - 待教研室
            case "V_APPLY_JYS": case "V_APPLY_REJ":
                newState = "V_APPLY_DRAFT";
                break;
//            立项申请 - 待科研处
            case "V_APPLY_KYC":
                newState = "V_APPLY_JYS";
                break;
//            立项申请 - 已完结
            case "V_APPLY_PASS":
                newState = "V_APPLY_KYC";
                break;
//            结项申请 - 待教研室
            case "V_OVER_JYS": case "V_OVER_REJ":
                newState = "V_OVER_DRAFT";
                break;
//            结项申请 - 待科研处
            case "V_OVER_KYC":
                newState = "V_OVER_JYS";
                break;
//            结项申请 - 已完结
            case "V_OVER_PASS":
                newState = "V_OVER_KYC";
                break;
        }
        if(state.equals("V_APPLY_PASS")){
            String status = "立项";
//            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
        }else
        if(state.equals("V_OVER_PASS")){
            String status = "结项";
//            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
        }

//        插入日志
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(id);
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("撤回");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);

        return sciHorizontalApplyVerticalMapper.applyPass(id.toString(),newState,null);
    }



    /**
     * 根据用户ID查询纵向课题列表
     * 功能：根据用户ID和角色查询纵向课题列表，支持不同表格类型的筛选
     * SQL：根据角色和表格类型不同，执行不同的查询语句
     */
    @Override
    public List<SciHorizontalApplyVertical> selectOtherListByUid(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        String role = sciHorizontalApplyVertical.getRole();
        String tableId = sciHorizontalApplyVertical.getTableId();
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApplyVertical.setNewsql("00");
                    list  = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApplyVertical.setNewsql("01");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApplyVertical.setNewsql("02");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
            }
        }
//        教研室
        else if(role.equals("research")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApplyVertical.setNewsql("00");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApplyVertical.setNewsql("01");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApplyVertical.setNewsql("02");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
            }
        }
//        教师
        else {
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApplyVertical.setNewsql("00");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApplyVertical.setNewsql("01");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApplyVertical.setNewsql("02");
                    list = sciHorizontalApplyVerticalMapper.selectOtherListByUid(sciHorizontalApplyVertical);
                    break;
            }
        }
        return list;
    }

    /**
     * 结项保存校验：结项日期必须在申请日期之后
     */
    public int overSaveSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        // 获取原始申请信息
        SciHorizontalApplyVertical old = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(sciHorizontalApplyVertical.getId());
        String applyDateStr = old.getSigningData(); // 申请日期
        String overDateStr = sciHorizontalApplyVertical.getSigningData(); // 结项日期
        if (applyDateStr != null && overDateStr != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date applyDate = sdf.parse(applyDateStr);
                java.util.Date overDate = sdf.parse(overDateStr);
                if (!overDate.after(applyDate)) {
                    return -1;
                }
            } catch (Exception e) {
                return -2;
            }
        }
        // 校验通过，执行更新
        return sciHorizontalApplyVerticalMapper.updateSciHorizontalApplyVertical(sciHorizontalApplyVertical);
    }
    /**
     * 统计查询纵向课题数据
     * 功能：根据参数统计查询纵向课题数据，支持多条件筛选
     * SQL：根据参数执行不同的统计查询语句
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> getStatsQuery(Map<String, String> params) {
        return sciHorizontalApplyVerticalMapper.getStatsQuery(params);
    }

    /**
     * 统计查询纵向课题数据（带权限检查）
     * 功能：根据参数统计查询纵向课题数据，支持多条件筛选和数据权限检查
     * SQL：根据参数执行不同的统计查询语句
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciHorizontalApplyVerticalMapper.getStatsQueryToCheck(params);
    }


}
