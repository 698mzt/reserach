package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.mapper.SciHorizontalApplyVerticalMapper;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.IPageRenderService;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.ShiroUtils.getSysUser;

/**
 * 纵向课题Service业务层处理
 *
 */
@Service
public class SciHorizontalApplyVerticalServiceImpl implements ISciHorizontalApplyVerticalService {

    private static final Logger log = LoggerFactory.getLogger(SciHorizontalApplyVerticalServiceImpl.class);

    @Autowired
    private SciHorizontalApplyVerticalMapper sciHorizontalApplyVerticalMapper;

    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;

    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;

    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;

    @Autowired
    private IApprovalProcessService approvalProcessService;

    @Autowired
    private IPageRenderService pageRenderService;

    /**
     * 查询纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 纵向课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalList(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
        // 为每条记录填充页面渲染数据
        list.forEach(this::fillPageRenderData);
        return list;
    }

    /**
     * 查询纵向课题列表（统一查询，支持所有状态）
     * 功能：统一查询纵向课题列表，支持多条件筛选和排序
     * 按照2026年度数据权限优化需求，所有管理员角色均可查看所有状态的课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListAll(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListAll(sciHorizontalApplyVertical);
        // 为每条记录填充页面渲染数据
        list.forEach(this::fillPageRenderData);
        return list;
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
        SciHorizontalApplyVertical apply = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(id);
        // 填充页面渲染数据
        fillPageRenderData(apply);
        return apply;
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
     * 使用公有方法实现审批流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,SciHorizontalApplyVertical sciHorizontalApplyVertical1) {
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String currentState = sciHorizontalApplyVertical.getState();
        
        // 创建审批请求对象
        ApprovalRequest request = ApprovalRequest.of(
                "VERTICAL_APPLY", // 流程编码
                Long.valueOf(id), // 业务ID
                currentState, // 当前状态
                "同意", // 审批意见
                userId, // 操作人ID
                getSysUser().getUserName(), // 操作人姓名
                getSysUser().getDept().getDeptName() // 操作人所属部门
        );
        
        // 调用审批通过方法
        ApprovalResult result = approvalProcessService.approve(request);
        
        // 如果审批失败，抛出异常
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        
        // 获取新状态
        String newState = result.getNewState();
        
        // 如果是科研处审批通过（立项用KYC，结项用KYCOVER），计算并分配科研分
        if ("KYC".equals(urlFlag) || "KYCOVER".equals(urlFlag)) {
            SciUserScore sciUserScore = new SciUserScore();
            sciUserScore.setVerticalId(verticalId);
            String status = "立项";
            sciUserScore.setChangeStatus("立项");
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                // 从 sci_user_score_vertical 表中查询该成员上一次插入的预期科研分
                String expectedScore = sciUserScoreMapper.selectLastExpectedValueByUserIdAndVerticalId(
                        persion.get(i).toString(), verticalId);
                if (StringUtils.isEmpty(expectedScore)) {
                    expectedScore = "0";
                }
                sciUserScore.setExpectedValue(expectedScore);
                sciUserScoreMapper.insertScoreVertical(sciUserScore);
            }
        }
        
        // 更新纵向课题状态
        int a = sciHorizontalApplyVerticalMapper.applyPass(id, newState, sciHorizontalApplyVertical1 != null ? sciHorizontalApplyVertical1.getSubjectSource() : null);
        
        // 添加审批记录
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
     * 使用公有方法实现审批流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyBh(String id, Long userId, String remark, String urlFlag) {
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String currentState = sciHorizontalApplyVertical.getState();
        
        // 创建审批请求对象
        ApprovalRequest request = ApprovalRequest.of(
                "VERTICAL_APPLY", // 流程编码
                Long.valueOf(id), // 业务ID
                currentState, // 当前状态
                remark, // 审批意见
                userId, // 操作人ID
                getSysUser().getUserName(), // 操作人姓名
                getSysUser().getDept().getDeptName() // 操作人所属部门
        );
        
        // 调用审批驳回方法
        ApprovalResult result = approvalProcessService.reject(request);
        
        // 如果审批失败，抛出异常
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        
        // 获取新状态
        String newState = result.getNewState();
        
        // 更新纵向课题状态
        int a = sciHorizontalApplyVerticalMapper.applyPass(id, newState, null);
        
        // 添加审批记录
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
     * 使用公有方法实现审批流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int overPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,String subjectSource) {
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String currentState = sciHorizontalApplyVertical.getState();
        
        // 创建审批请求对象
        ApprovalRequest request = ApprovalRequest.of(
                "VERTICAL_OVER", // 流程编码
                Long.valueOf(id), // 业务ID
                currentState, // 当前状态
                "同意", // 审批意见
                userId, // 操作人ID
                getSysUser().getUserName(), // 操作人姓名
                getSysUser().getDept().getDeptName() // 操作人所属部门
        );
        
        // 调用审批通过方法
        ApprovalResult result = approvalProcessService.approve(request);
        
        // 如果审批失败，抛出异常
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        
        // 获取新状态
        String newState = result.getNewState();
        
        // 如果是科研处审批通过（立项用KYC，结项用KYCOVER），计算并分配科研分
        if ("KYC".equals(urlFlag) || "KYCOVER".equals(urlFlag)) {
            SciUserScore sciUserScore = new SciUserScore();
            sciUserScore.setVerticalId(verticalId);
            String status = "结项";
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
        }
        
        // 更新纵向课题状态
        int a = sciHorizontalApplyVerticalMapper.overPass(id, newState, subjectSource);
        
        // 添加审批记录
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
     * 使用公有方法实现审批流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int overBh(String id, Long userId, String remark, String urlFlag) {
        // 通过id查询获取SciHorizontalApplyVertical对象
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalById(Integer.valueOf(id));
        String currentState = sciHorizontalApplyVertical.getState();
        
        // 创建审批请求对象
        ApprovalRequest request = ApprovalRequest.of(
                "VERTICAL_OVER", // 流程编码
                Long.valueOf(id), // 业务ID
                currentState, // 当前状态
                remark, // 审批意见
                userId, // 操作人ID
                getSysUser().getUserName(), // 操作人姓名
                getSysUser().getDept().getDeptName() // 操作人所属部门
        );
        
        // 调用审批驳回方法
        ApprovalResult result = approvalProcessService.reject(request);
        
        // 如果审批失败，抛出异常
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        
        // 获取新状态
        String newState = result.getNewState();
        
        // 更新纵向课题状态
        int a = sciHorizontalApplyVerticalMapper.overPass(id, newState, null);
        
        // 添加审批记录
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
        // 为每条记录填充页面渲染数据
        list.forEach(this::fillPageRenderData);
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
        List<SciHorizontalApplyVertical> list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);
        // 为每条记录填充页面渲染数据
        list.forEach(this::fillPageRenderData);
        return list;
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
//            立项申请 - 待教研室 / 被驳回 → 回退到草稿
            case "V_APPLY_JYS": case "V_APPLY_REJ":
            case "VERTICAL_APPLY_JYS_AUDIT": case "VERTICAL_APPLY_REJECTED":
                newState = "VERTICAL_APPLY_DRAFT";
                break;
//            立项申请 - 待科研处 → 回退到待教研室
            case "V_APPLY_KYC":
            case "VERTICAL_APPLY_KYC_AUDIT":
                newState = "VERTICAL_APPLY_JYS_AUDIT";
                break;
//            立项申请 - 已完结 → 回退到待科研处
            case "V_APPLY_PASS":
            case "VERTICAL_APPLY_PASSED":
                newState = "VERTICAL_APPLY_KYC_AUDIT";
                break;
//            结项申请 - 待教研室 / 被驳回 → 回退到草稿
            case "V_OVER_JYS": case "V_OVER_REJ":
            case "VERTICAL_OVER_JYS_AUDIT": case "VERTICAL_OVER_REJECTED":
                newState = "VERTICAL_OVER_DRAFT";
                break;
//            结项申请 - 待科研处 → 回退到待教研室
            case "V_OVER_KYC":
            case "VERTICAL_OVER_KYC_AUDIT":
                newState = "VERTICAL_OVER_JYS_AUDIT";
                break;
//            结项申请 - 已完结 → 回退到待科研处
            case "V_OVER_PASS":
            case "VERTICAL_OVER_PASSED":
                newState = "VERTICAL_OVER_KYC_AUDIT";
                break;
        }
        if(state.equals("V_APPLY_PASS") || state.equals("VERTICAL_APPLY_PASSED")){
            String status = "立项";
//            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
        }else
        if(state.equals("V_OVER_PASS") || state.equals("VERTICAL_OVER_PASSED")){
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

    /**
     * 填充页面渲染数据（状态展示信息和按钮动作列表）
     * 统一调用 pageRenderService.fillPageRenderData 公共服务构建状态与按钮动作
     * 传 null 给公共服务，由公共服务内部处理缓存
     *
     * @param apply 纵向课题对象
     */
    private void fillPageRenderData(SciHorizontalApplyVertical apply) {
        if (apply == null) {
            return;
        }
        try {
            SysUser currentUser = ShiroUtils.getSysUser();
            if (currentUser == null) {
                return;
            }

            String currentState = apply.getState();
            // 根据状态确定模块编码（立项/结项）
            String moduleCode = determineModuleCode(currentState);
            // 流程编码与模块编码一致
            String processCode = moduleCode;

            // 统一调用公共服务填充页面渲染数据（权限传 null，由公共服务内部处理缓存）
            PageRenderResult<?> result = pageRenderService.fillPageRenderData(
                    moduleCode,
                    "system:apply_vertical",
                    processCode,
                    currentState,
                    apply.getId() != null ? apply.getId().longValue() : null,
                    apply.getUserId() != null ? apply.getUserId().longValue() : null,
                    null
            );

            apply.setStatusMeta(result.getStatusMeta());
            apply.setActions(result.getActions());

            // 添加自定义业务按钮："申请结项"按钮
            Set<String> permissions = pageRenderService.buildCurrentPermissions(currentUser);
            addCustomActions(apply, apply.getActions(), currentUser, permissions);
        } catch (Exception e) {
            // 页面渲染数据填充失败不影响主流程，提供兜底数据
            log.error("填充纵向课题页面渲染数据失败, applyId={}", apply.getId(), e);
            apply.setStatusMeta(PageRenderStatusMeta.of(apply.getState(), "未知", PageRenderColorConstants.COLOR_DEFAULT));
            apply.setActions(new ArrayList<>());
        }
    }

    /**
     * 根据状态确定模块编码（立项/结项）
     * 优先匹配新状态编码前缀，兼容旧状态编码前缀
     *
     * @param state 当前状态
     * @return 模块编码
     */
    private String determineModuleCode(String state) {
        if (StringUtils.isEmpty(state)) {
            return "VERTICAL_APPLY";
        }
        // 结项状态：新编码以 VERTICAL_OVER 开头，旧编码以 V_OVER 或 V_1~V_7 开头
        if (state.startsWith("VERTICAL_OVER")
                || state.startsWith("V_OVER")
                || state.startsWith("V_1") || state.startsWith("V_2")
                || state.startsWith("V_3") || state.startsWith("V_4")
                || state.startsWith("V_5") || state.startsWith("V_6")
                || state.startsWith("V_7")) {
            return "VERTICAL_OVER";
        }
        return "VERTICAL_APPLY";
    }

    /**
     * 添加自定义业务按钮（通用规则引擎不涵盖的按钮）
     * 功能：为已通过状态添加"申请结项"按钮
     *
     * @param apply 纵向课题对象
     * @param actions 动作列表
     * @param currentUser 当前用户
     * @param permissions 权限列表
     */
    private void addCustomActions(SciHorizontalApplyVertical apply, List<PageRenderActionItem> actions,
                                   SysUser currentUser, Set<String> permissions) {
        if (apply == null || actions == null || currentUser == null) {
            return;
        }
        String state = apply.getState();
        if (StringUtils.isEmpty(state)) {
            return;
        }
        // 判断是否为创建者或管理员
        // 注意：apply.getUserId() 是 Integer，currentUser.getUserId() 是 Long，需要统一类型比较
        boolean isOwner = apply.getUserId() != null && currentUser.getUserId() != null
                && apply.getUserId().longValue() == currentUser.getUserId();
        boolean isAdmin = isAdminUser(currentUser);
        // 管理员或有 add 权限的用户可以"申请结项"
        boolean canAdd = isAdmin || permissions.contains("system:apply_vertical:add");

        // 立项已通过：创建者或管理员可以"申请结项"
        if ("VERTICAL_APPLY_PASSED".equals(state) && (isOwner || isAdmin) && canAdd) {
            PageRenderActionItem action = PageRenderActionItem.of(
                    "overApply",
                    "申请结项",
                    PageRenderColorConstants.COLOR_SUCCESS,
                    15);
            actions.add(action);
        }

        // 结项草稿：创建者或管理员可以"提交结项申请"
        if ("VERTICAL_OVER_DRAFT".equals(state) && (isOwner || isAdmin) && canAdd) {
            PageRenderActionItem action = PageRenderActionItem.of(
                    "submit",
                    "提交结项申请",
                    PageRenderColorConstants.COLOR_SUCCESS,
                    5,
                    "确定要提交该结项申请吗？");
            actions.add(action);
        }
    }

    /**
     * 判断当前用户是否为管理员
     *
     * @param currentUser 当前用户
     * @return 是否为管理员
     */
    private boolean isAdminUser(SysUser currentUser) {
        if (currentUser == null) {
            return false;
        }
        // 超级管理员（user_id = 1）
        if (currentUser.getUserId() != null && currentUser.getUserId().equals(1L)) {
            return true;
        }
        // 检查是否有 admin 角色
        if (currentUser.getRoles() != null) {
            for (SysRole role : currentUser.getRoles()) {
                if (role != null && "admin".equals(role.getRoleKey())) {
                    return true;
                }
            }
        }
        return false;
    }


}
