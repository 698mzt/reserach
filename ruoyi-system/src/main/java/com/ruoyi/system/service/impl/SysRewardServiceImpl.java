package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciRewardScoreCfgMapper;
import com.ruoyi.system.mapper.SysRewardMapper;
import com.ruoyi.system.mapper.SysRewardPiyueMapper;
import com.ruoyi.system.service.ISciRewardScoreCfgService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRewardService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.IPageRenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 奖励Service业务层处理
 * 
 * 
 * @author ruoyi
 * @date 2024-12-23
 */
@Service
public class SysRewardServiceImpl implements ISysRewardService
{
    /**
     * 奖励审批流程编码
     * 对应数据库 sys_approval_process 表的 process_code 字段
     */
    private static final String REWARD_PROCESS_CODE = "REWARD_APPLY";

    /**
     * 奖励状态常量定义
     */
    public static final String REWARD_DRAFT = "REWARD_DRAFT";           // 草稿状态
    public static final String REWARD_JYS_AUDIT = "REWARD_JYS_AUDIT";   // 教研室审批状态
    public static final String REWARD_KYC_AUDIT = "REWARD_KYC_AUDIT";   // 科研处审批状态
    public static final String REWARD_PASSED = "REWARD_PASSED";         // 审批通过状态
    public static final String REWARD_REJECTED = "REWARD_REJECTED";     // 审批驳回状态

    @Autowired
    private SysRewardMapper sysRewardMapper;

    @Autowired
    private SysRewardPiyueMapper sysRewardPiyueMapper;

    @Autowired
    private SciRewardScoreCfgMapper sciRewardScoreCfgMapper;

    // 注入积分配置服务，用于自动计算积分
    @Autowired
    private ISciRewardScoreCfgService scoreCfgService;

    @Autowired
    private IPageRenderService pageRenderService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 注入审批流程服务（核心）
     * 用于统一处理审批通过、驳回、撤回等操作
     */
    @Autowired
    private IApprovalProcessService approvalProcessService;

    /**
     * 注入用户服务
     * 用于获取操作用户信息
     */
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询奖励
     *
     * @param id 奖励主键
     * @return 奖励
     */
    @Override
    public SysReward selectSysRewardById(Long id)
    {
        SysReward reward = sysRewardMapper.selectSysRewardById(id);
        if (reward != null) {
            fillPageRenderData(reward);
        }
        return reward;
    }

    /**
     * 查询奖励列表（统一查询方法，通过@DataScope控制数据权限）
     *
     * @param sysReward 奖励
     * @return 奖励列表
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysReward> selectSysRewardList(SysReward sysReward)
    {
        List<SysReward> list = sysRewardMapper.selectSysRewardList(sysReward);
        for (SysReward reward : list) {
            fillPageRenderData(reward);
        }
        return list;
    }

    @Override
    public int overReward(String id, String state) {
        return sysRewardMapper.overReward(id,state);
    }

    /**
     * 新增奖励
     *
     * @param sysReward 奖励
     * @return 结果
     */
    @Override
    public int insertSysReward(SysReward sysReward)
    {
        // 预计积分由前端传入，直接保存
        // 实际积分在审核通过后才计算，初始化为空或0
        
        // 原有保存逻辑
        int a = sysRewardMapper.insertSysReward(sysReward);
        int id = Integer.parseInt(sysReward.getId().toString());
        
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(sysReward.getUserId());
        sysRewardPiyue.setRewardId(id);
        sysRewardPiyue.setConcate("新增");
        sysRewardPiyue.setState("新增");
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
        return a;
    }

    /**
     * 修改奖励
     *
     * @param sysReward 奖励
     * @return 结果
     */
    @Override
    public int updateSysReward(SysReward sysReward)
    {
        // 预计积分由前端传入，直接保存
        // 实际积分在审核通过后才计算，不在这里更新
        
        // 原有更新逻辑
        sysRewardMapper.updateSysReward(sysReward);
        
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(sysReward.getUserId());
        sysRewardPiyue.setRewardId(Integer.valueOf(sysReward.getId().toString()));
        if (sysReward.getState().equals("1")) {
            sysRewardPiyue.setConcate("提交");
            sysRewardPiyue.setState("提交");
        } else {
            sysRewardPiyue.setConcate("修改");
            sysRewardPiyue.setState("修改");
        }
        return sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
    }

    /**
     * 批量删除奖励
     *
     * @param ids 需要删除的奖励主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardByIds(String ids)
    {
        return sysRewardMapper.deleteSysRewardByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除奖励信息
     *
     * @param id 奖励主键
     * @return 结果
     */
    @Override
    public int deleteSysRewardById(Long id)
    {
        return sysRewardMapper.deleteSysRewardById(id);
    }

    /**
     * 统一审批操作（通过/驳回/撤回）
     * 
     * 使用统一的审批流程服务进行状态管理：
     *
     *   approve: 审批通过，状态流转到下一节点或终态
     *   reject: 审批驳回，状态回退到指定节点（通常为草稿）
     *   recall: 撤回审批，状态回退到上一审批节点
     *
     * 
     *积分计算规则：
     *   最后一个审批节点通过时，根据奖励分类、等级、排名计算积分
     *   撤回操作如果回退到科研处审批前状态，重置积分
     *
     * 
     * @param id 奖励ID
     * @param userId 用户ID
     * @param comment 审批意见
     * @param operationType 操作类型：approve(通过)、reject(驳回)、recall(撤回)
     * @return 操作结果，成功返回影响行数，失败返回-1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(String id, Long userId, String comment, String operationType) {
        // 1. 查询奖励信息
        SysReward reward = sysRewardMapper.selectSysRewardById(Long.valueOf(id));
        if (reward == null) {
            return -1;
        }

        // 2. 获取当前状态和操作用户信息
        String currentState = reward.getState();
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return -1;
        }

        // 3. 构建审批请求
        ApprovalRequest request = ApprovalRequest.of(REWARD_PROCESS_CODE,
                Long.valueOf(id), currentState, comment,
                userId, user.getUserName(),
                user.getDept() != null ? user.getDept().getDeptName() : "");

        // 4. 执行审批操作
        ApprovalResult result = null;
        if ("approve".equals(operationType)) {
            result = approvalProcessService.approve(request);
        } else if ("reject".equals(operationType)) {
            result = approvalProcessService.reject(request);
        } else if ("recall".equals(operationType)) {
            result = approvalProcessService.recall(request);
        }

        // 5. 检查审批结果
        if (result == null || !result.isSuccess()) {
            return -1;
        }

        // 6. 获取新状态
        String newState = result.getNewState();

        // 7. 特殊处理：驳回操作直接设置为驳回状态
        if ("reject".equals(operationType)) {
            newState = REWARD_REJECTED;
            // 更新数据库状态（绕过审批流程服务的自动更新，使用自定义状态）
            sysRewardMapper.hxPass(id, newState);
        } else {
            // 审批流程服务已自动更新状态，无需额外操作
        }

        // 8. 积分计算逻辑（仅最后一个审批节点通过时计算）
        if ("approve".equals(operationType) && result.isLast()) {
            calculateAndUpdateScore(id);
        }

        // 9. 撤回时重置积分（当撤回会影响已计算的积分时）
        if ("recall".equals(operationType) && REWARD_KYC_AUDIT.equals(newState)) {
            sysRewardMapper.resetJifenById(Long.valueOf(id));
        }

        // 10. 记录审批意见
        saveApprovalOpinion(id, userId, comment, operationType);

        return 1;
    }

    /**
     * 根据奖励信息计算并更新积分
     * 
     * @param id 奖励ID
     */
    private void calculateAndUpdateScore(String id) {
        SysReward sysReward = sysRewardMapper.selectSysRewardById(Long.valueOf(id));
        if (sysReward == null) {
            return;
        }

        String fenlei = sysReward.getRewardFenlei();
        String dengji = sysReward.getRewardDengji();
        String paiming = sysReward.getRewardPaiming();

        SciRewardScoreCfg sciRewardScoreCfg = new SciRewardScoreCfg();
        sciRewardScoreCfg.setFenLei(fenlei);
        sciRewardScoreCfg.setDengJi(dengji);
        sciRewardScoreCfg.setPaiMing(paiming);

        List<SciRewardScoreCfg> cfgList = sciRewardScoreCfgMapper.selectSciRewardScoreCfgList(sciRewardScoreCfg);
        if (cfgList != null && !cfgList.isEmpty()) {
            int jifen = Integer.parseInt(cfgList.get(0).getTotalScore());
            sysRewardMapper.updateJifen(Long.valueOf(id), jifen);
        }
    }

    /**
     * 保存审批意见记录
     * 
     * @param id 奖励ID
     * @param userId 用户ID
     * @param comment 审批意见
     * @param operationType 操作类型
     */
    private void saveApprovalOpinion(String id, Long userId, String comment, String operationType) {
        SysRewardPiyue sysRewardPiyue = new SysRewardPiyue();
        sysRewardPiyue.setUid(userId);
        sysRewardPiyue.setRewardId(Integer.valueOf(id));
        
        if ("approve".equals(operationType)) {
            sysRewardPiyue.setConcate(comment != null ? comment : "审批通过");
            sysRewardPiyue.setState("通过");
        } else if ("reject".equals(operationType)) {
            sysRewardPiyue.setConcate(comment != null ? comment : "审批驳回");
            sysRewardPiyue.setState("驳回");
        } else if ("recall".equals(operationType)) {
            sysRewardPiyue.setConcate(comment != null ? comment : "审批撤回");
            sysRewardPiyue.setState("撤回");
        }
        
        sysRewardPiyueMapper.insertSysRewardPiyue(sysRewardPiyue);
    }

    /**
     * 兼容旧接口：审批通过
     * 
     * @deprecated 建议使用 {@link #approve(String, Long, String, String)} 方法
     */
    @Override
    public int hxPass(String id, Long uid, String urlFlag) {
        return approve(id, uid, "同意", "approve");
    }

    /**
     * 兼容旧接口：审批驳回
     * 
     * @deprecated 建议使用 {@link #approve(String, Long, String, String)} 方法
     */
    @Override
    public int hxBh(String id, Long uid, String remark, String urlFlag) {
        return approve(id, uid, remark, "reject");
    }

    /**
     * 兼容旧接口：撤回奖励
     * 
     * @deprecated 建议使用 {@link #approve(String, Long, String, String)} 方法
     */
    @Override
    public int recall(Integer id, String state, Long uid, String remark, String urlFlag) {
        return approve(id.toString(), uid, remark, "recall");
    }

    @Override
    public List<SysReward> selectSysRewardListByKYC(SysReward sysReward) {
        return null;
    }

    @Override
    public List<SysReward> selectSysRewardListByJYS(SysReward sysReward) {
        return null;
    }

    @Override
    public List<SysReward> selectSysRewardListByXUE(SysReward sysReward) {
        return null;
    }

    @Override
    public List<SysReward> getStatsQuery(Map<String, String> params) {
        return sysRewardMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysReward> getStatsQueryToCheck(Map<String, String> params) {
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sysRewardMapper.getStatsQueryToCheck(params);
    }

    /**
     * 填充页面渲染数据（statusMeta和actions）
     * 通过sysMenuService获取用户权限，构造PageRenderContext并调用PageRenderService生成渲染数据
     *
     * @param reward 奖励对象
     */
    private void fillPageRenderData(SysReward reward) {
        try {
            SysUser currentUser = ShiroUtils.getSysUser();
            if (currentUser == null) {
                return;
            }

            List<String> permissions = new ArrayList<>();
            Set<String> permsSet = sysMenuService.selectPermsByUserId(currentUser.getUserId());
            if (permsSet != null) {
                permissions.addAll(permsSet);
            }

            List<String> roleKeys = new ArrayList<>();
            if (currentUser.getRoles() != null) {
                roleKeys = currentUser.getRoles().stream()
                        .map(SysRole::getRoleKey)
                        .collect(Collectors.toList());
            }

            PageRenderContext context = new PageRenderContext();
            context.setModuleCode("REWARD");
            context.setBusinessId(reward.getId());
            context.setCurrentState(reward.getState());
            context.setCreatorId(reward.getUserId());
            context.setCurrentUser(currentUser);
            context.setPermissions(permissions);
            context.setRoleKeys(roleKeys);
            context.setPermPrefix("system:reward");
            context.setProcessCode(REWARD_PROCESS_CODE);

            PageRenderStatusMeta statusMeta = pageRenderService.buildStatusMeta(context);
            List<PageRenderActionItem> actions = pageRenderService.buildActions(context);

            reward.setStatusMeta(statusMeta);
            reward.setActions(actions);
        } catch (Exception e) {
            // 页面渲染数据填充失败不影响主流程
        }
    }
}