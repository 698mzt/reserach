package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhuPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciZhuanliruanzhuPiyueMapper;
import com.ruoyi.system.mapper.SciZhuanliruanzhuScoreCfgMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciZhuanliruanzhuMapper;
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;


/**
 * 专利软著Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-21
 */
@Service
public class SciZhuanliruanzhuServiceImpl implements ISciZhuanliruanzhuService
{
    @Autowired
    private SciZhuanliruanzhuMapper sciZhuanliruanzhuMapper;

    @Autowired
    private SciZhuanliruanzhuPiyueMapper sciZhuanliruanzhuPiyueMapper;


    @Autowired
    private SciZhuanliruanzhuScoreCfgMapper sciZhuanliruanzhuScoreCfgMapper;


//    @Autowired
//    private SciZhuanliruanzhuMapper sciZhuanliruanzhuMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IApprovalProcessService approvalProcessService;


    /**
     * 查询专利软著
     *
     * @param id 专利软著主键
     * @return 专利软著
     */
    @Override
    public SciZhuanliruanzhu selectSciZhuanliruanzhuById(Integer id) {
        SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(id);
        if (sciZhuanliruanzhu != null && (sciZhuanliruanzhu.getUserName() == null || sciZhuanliruanzhu.getUserName().isEmpty()) && sciZhuanliruanzhu.getUserId() != null) {
            try {
                SysUser user = userService.selectUserById(Long.valueOf(sciZhuanliruanzhu.getUserId()));
                if (user != null && user.getUserName() != null) {
                    sciZhuanliruanzhu.setUserName(user.getUserName());
                }
            } catch (Exception e) {
                // 处理异常
            }
        }
        return sciZhuanliruanzhu;
    }

    /**
     * 查询专利软著列表
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList(sciZhuanliruanzhu);
    }

    /**
     * 新增专利软著
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        //  设置默认状态为草稿箱
        sciZhuanliruanzhu.setState("0");

        // 计算预计积分（并保存到 expected_jifen）
        String jifen = calculateScore(sciZhuanliruanzhu.getFenlei(), sciZhuanliruanzhu.getPaiming());
        sciZhuanliruanzhu.setJifen(jifen); // 兼容旧逻辑：仍保留原 jifen 字段
        sciZhuanliruanzhu.setExpectedJifen(jifen);

        // 处理计算机软件著作权的预计积分（非转化时乘以0.5）
        if ("4".equals(sciZhuanliruanzhu.getFenlei()) && "N".equals(sciZhuanliruanzhu.getShifouyingyon())) {
            if (sciZhuanliruanzhu.getExpectedJifen() != null && !sciZhuanliruanzhu.getExpectedJifen().isEmpty()) {
                try {
                    double expectedJifen = Double.parseDouble(sciZhuanliruanzhu.getExpectedJifen());
                    // 乘以0.5
                    expectedJifen *= 0.5;
                    // 转换回字符串
                    sciZhuanliruanzhu.setExpectedJifen(String.valueOf(expectedJifen));
                } catch (NumberFormatException e) {
                    // 处理转换异常
                }
            }
        }

        // 新增/编辑阶段最终积分未确认
        sciZhuanliruanzhu.setFinalJifen(null);

        int a = sciZhuanliruanzhuMapper.insertSciZhuanliruanzhu(sciZhuanliruanzhu);
        int id = sciZhuanliruanzhu.getId();


        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(Long.valueOf(sciZhuanliruanzhu.getUserId()));
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate("新增");
        sciZhuanliruanzhuPiyue.setState("新增");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);

        return a;
    }

    /**
     * 根据分类和排名计算积分
     */
    private String calculateScore(String fenlei, String paiming) {
        if (fenlei == null || paiming == null) {
            return "0";
        }

        SciZhuanliruanzhuScoreCfg scoreCfg = new SciZhuanliruanzhuScoreCfg();
        scoreCfg.setFenLei(fenlei);
        scoreCfg.setPaiMing(paiming);

        List<SciZhuanliruanzhuScoreCfg> configList = sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgList(scoreCfg);

        if (configList != null && !configList.isEmpty()) {
            return configList.get(0).getTotalScore();
        }

        return "0";
    }

    /**
     * 根据分类计算预计科研分（仅排名1~4：主持人/成员1/成员2/成员3）
     *
     * @param fenlei 分类值（来自 sys_zhuanli_fenlei）
     * @return 包含 firstScore/secondScore/thirdScore/fourthScore 的Map
     */
    @Override
    public Map<String, String> calculateExpectedScores(String fenlei) {
        Map<String, String> res = new java.util.HashMap<>();
        res.put("firstScore", calculateScore(fenlei, "1"));
        res.put("secondScore", calculateScore(fenlei, "2"));
        res.put("thirdScore", calculateScore(fenlei, "3"));
        res.put("fourthScore", calculateScore(fenlei, "4"));
        return res;
    }

    @Override
    public String calculateScoreByFenleiAndRank(String fenlei, String paiming) {
        return calculateScore(fenlei, paiming);
    }

    /**
     * 修改专利软著
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        // 重新计算预计积分，并保存到 expected_jifen
        String jifen = calculateScore(sciZhuanliruanzhu.getFenlei(), sciZhuanliruanzhu.getPaiming());
        sciZhuanliruanzhu.setJifen(jifen); // 兼容旧逻辑
        sciZhuanliruanzhu.setExpectedJifen(jifen);

        // 处理计算机软件著作权的预计积分（非转化时乘以0.5）
        if ("4".equals(sciZhuanliruanzhu.getFenlei()) && "N".equals(sciZhuanliruanzhu.getShifouyingyon())) {
            if (sciZhuanliruanzhu.getExpectedJifen() != null && !sciZhuanliruanzhu.getExpectedJifen().isEmpty()) {
                try {
                    double expectedJifen = Double.parseDouble(sciZhuanliruanzhu.getExpectedJifen());
                    // 乘以0.5
                    expectedJifen *= 0.5;
                    // 转换回字符串
                    sciZhuanliruanzhu.setExpectedJifen(String.valueOf(expectedJifen));
                } catch (NumberFormatException e) {
                    // 处理转换异常
                }
            }
        }

        // 编辑时清空最终积分（未最终确认则不显示）
        sciZhuanliruanzhu.setFinalJifen(null);

        int a = sciZhuanliruanzhuMapper.updateSciZhuanliruanzhu(sciZhuanliruanzhu);
        int id = sciZhuanliruanzhu.getId();
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(Long.valueOf(sciZhuanliruanzhu.getUserId()));
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate("修改");
        sciZhuanliruanzhuPiyue.setState("修改");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }
    /**
     * 批量删除专利
     *
     * @param ids 需要删除的专利软著主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSciZhuanliruanzhuByIds(String ids)
    {
        return sciZhuanliruanzhuMapper.deleteSciZhuanliruanzhuByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除专利软著信息
     *
     * @param id 专利软著主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSciZhuanliruanzhuById(Integer id)
    {
        return sciZhuanliruanzhuMapper.deleteSciZhuanliruanzhuById(id);
    }

    /**
     * 更新专利软著积分
     *
     * @param id 专利软著主键
     * @param jifen 积分值
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJifen(Long id, int jifen) {
        return sciZhuanliruanzhuMapper.updateJifen(id, jifen);
    }

    /**
     * 专利软著审核通过
     *
     * @param id 专利软著主键
     * @param uid 用户ID
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int hxPass(String id, Long uid, String urlFlag) {
        // 获取专利软著信息，用于获取当前状态
        SciZhuanliruanzhu sci = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
        if (sci == null) {
            return 0;
        }

        // 构建审批请求
        ApprovalRequest request = new ApprovalRequest();
        request.setProcessCode("PATENT_APPLY"); // 专利软著审批流程编码
        request.setBusinessId(Long.valueOf(id));
        request.setCurrentState(sci.getState());
        request.setOperatorId(uid);

        // 获取操作人信息
        SysUser user = userService.selectUserById(uid);
        if (user != null) {
            request.setOperatorName(user.getUserName());
            if (user.getDept() != null) {
                request.setOperatorDept(user.getDept().getDeptName());
            }
        }

        ApprovalResult result;

        // 根据操作类型执行不同的审批操作
        if (urlFlag.equals("tijiao")) {
            // 提交审批
            result = approvalProcessService.submitApproval(request);
        } else {
            // 审批通过
            result = approvalProcessService.approve(request);
        }

        if (!result.isSuccess()) {
            throw new RuntimeException("审批操作失败: " + result.getMessage());
        }

        // 科研处通过（流程结束）时：写入最终积分（final_jifen）
        if (result.isLast()) {
            String finalJifen;
            if (sci != null) {
                // 计算年度
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                // 统计年度内用户的软著数量
                int count = sciZhuanliruanzhuMapper.countSoftWorksByYear(Long.valueOf(sci.getUserId()), currentYear);
                // 计算基础分数
                String baseScore = calculateScore(sci.getFenlei(), sci.getPaiming());
                // 非转化软著按50%核算
                if ("N".equals(sci.getShifouyingyon())) {
                    double score = Double.parseDouble(baseScore) * 0.5;
                    baseScore = String.valueOf(Math.round(score));
                }
                // 年度内不超过5项
                if (count > 5) {
                    finalJifen = "0";
                } else {
                    finalJifen = baseScore;
                }
            } else {
                finalJifen = "0";
            }
            sciZhuanliruanzhuMapper.updateFinalJifen(id, finalJifen);
            // 科研处认定通过时间，用于科研统计年度（上年12/1～当年11/30）计算
            sciZhuanliruanzhuMapper.updateKyjcPassTime(id, new Date());
        }

        // 审批流程服务已经更新了业务表状态并记录了审批历史，这里不需要再手动更新
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int hxPass(String id, Long uid, String newState, boolean isLast) {
        if (isLast) {
            SciZhuanliruanzhu sci = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
            String finalJifen;
            if (sci != null) {
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                int count = sciZhuanliruanzhuMapper.countSoftWorksByYear(Long.valueOf(sci.getUserId()), currentYear);
                String baseScore = calculateScore(sci.getFenlei(), sci.getPaiming());
                if ("N".equals(sci.getShifouyingyon())) {
                    double score = Double.parseDouble(baseScore) * 0.5;
                    baseScore = String.valueOf(Math.round(score));
                }
                if (count >= 5) {
                    finalJifen = "0";
                } else {
                    finalJifen = baseScore;
                }
            } else {
                finalJifen = "0";
            }
            sciZhuanliruanzhuMapper.updateFinalJifen(id, finalJifen);
            sciZhuanliruanzhuMapper.updateKyjcPassTime(id, new Date());
        }

        int a = sciZhuanliruanzhuMapper.hxPass(id, newState);

        SciZhuanliruanzhuPiyue piyue = new SciZhuanliruanzhuPiyue();
        piyue.setUid(uid);
        piyue.setHxktId(Integer.valueOf(id));
        if ("1".equals(newState)) {
            piyue.setConcate("提交申请");
            piyue.setState("提交");
        } else if (isLast) {
            piyue.setConcate("同意");
            piyue.setState("通过");
        } else {
            piyue.setConcate("同意");
            piyue.setState("通过");
        }
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(piyue);
        return a;
    }

    /**
     * 专利软著审核驳回
     *
     * @param id 专利软著主键
     * @param uid 用户ID
     * @param remark 驳回意见
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int hxBh(String id, Long uid, String remark, String urlFlag) {
        // 获取专利软著信息，用于获取当前状态
        SciZhuanliruanzhu sci = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
        if (sci == null) {
            return 0;
        }

        // 构建审批请求
        ApprovalRequest request = new ApprovalRequest();
        request.setProcessCode("PATENT_APPLY"); // 专利软著审批流程编码
        request.setBusinessId(Long.valueOf(id));
        request.setCurrentState(sci.getState());
        request.setOperatorId(uid);
        request.setComment(remark);

        // 获取操作人信息
        SysUser user = userService.selectUserById(uid);
        if (user != null) {
            request.setOperatorName(user.getUserName());
            if (user.getDept() != null) {
                request.setOperatorDept(user.getDept().getDeptName());
            }
        }

        // 执行驳回操作
        ApprovalResult result = approvalProcessService.reject(request);

        if (!result.isSuccess()) {
            throw new RuntimeException("驳回操作失败: " + result.getMessage());
        }

        // 审批流程服务已经更新了业务表状态并记录了审批历史，这里不需要再手动更新
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int hxBh(String id, Long uid, String remark, String newState, boolean fromApprovalProcess) {
        int a = sciZhuanliruanzhuMapper.hxPass(id, newState);
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(uid);
        sciZhuanliruanzhuPiyue.setHxktId(Integer.valueOf(id));
        sciZhuanliruanzhuPiyue.setConcate(remark != null ? remark : "驳回");
        sciZhuanliruanzhuPiyue.setState("被驳回");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recallDirect(String id, Long uid, String newState, String remark) {
        if ("4".equals(newState)) {
            sciZhuanliruanzhuMapper.updateJifen(Long.valueOf(id), 0);
            sciZhuanliruanzhuMapper.updateFinalJifen(id, null);
        }
        int a = sciZhuanliruanzhuMapper.hxPass(id, newState);
        SciZhuanliruanzhuPiyue piyue = new SciZhuanliruanzhuPiyue();
        piyue.setUid(uid);
        piyue.setHxktId(Integer.valueOf(id));
        piyue.setConcate(remark != null ? remark : "撤回");
        piyue.setState("撤回");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(piyue);
        return a;
    }



    /**
     * 查询专利软著列表（科研处）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList4(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList4(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（学院）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList3(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList3(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教研室）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList2(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList2(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教师）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList1(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
    }

    /**
     * 查询专利软著列表（学院导出）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList31(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList31(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教研室导出）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList21(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList21(sciZhuanliruanzhu);
    }
    /**
     * 撤回专利软著
     *
     * @param id 专利软著主键
     * @param state 当前状态
     * @param uid 用户ID
     * @param remark 撤回原因
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recall(Integer id, String state, Long uid, String remark, String urlFlag) {
        // 构建审批请求
        ApprovalRequest request = new ApprovalRequest();
        request.setProcessCode("PATENT_APPLY"); // 专利软著审批流程编码
        request.setBusinessId(Long.valueOf(id));
        request.setCurrentState(state);
        request.setOperatorId(uid);
        request.setComment(remark);

        // 获取操作人信息
        SysUser user = userService.selectUserById(uid);
        if (user != null) {
            request.setOperatorName(user.getUserName());
            if (user.getDept() != null) {
                request.setOperatorDept(user.getDept().getDeptName());
            }
        }

        // 执行撤回操作
        ApprovalResult result = approvalProcessService.recall(request);

        if (!result.isSuccess()) {
            throw new RuntimeException("撤回操作失败: " + result.getMessage());
        }

        // 审批流程服务已经更新了业务表状态并记录了审批历史，这里不需要再手动更新
        return 1;
    }


    /**
     * 检查专利软著是否存在
     *
     * @param mingcheng 专利名称
     * @param paiming 排名
     * @param userId 用户ID
     * @return 检查结果（0-可以添加，1-重复，2-超过限制）
     */
    @Override
    public int checkExist(String mingcheng, String paiming,Long userId) {
//        判断该专利名称的该负责人级别已存在，不可重复添加
        if (sciZhuanliruanzhuMapper.checkExist(mingcheng, paiming) > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 统计查询专利软著列表
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQuery(Map<String, String> params) {
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciZhuanliruanzhuMapper.getStatsQuery(params);
    }

    /**
     * 统计查询专利软著列表（导出Excel）
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQueryToExcil(Map<String, String> params) {
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciZhuanliruanzhuMapper.getStatsQueryToExcil(params);
    }

    /**
     * 统计查询专利软著列表（审核）
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciZhuanliruanzhuMapper.getStatsQueryToCheck(params);
    }




}
