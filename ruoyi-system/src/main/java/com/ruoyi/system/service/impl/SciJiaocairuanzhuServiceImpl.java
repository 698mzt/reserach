package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.ApprovalRequest;
import com.ruoyi.system.domain.ApprovalResult;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciJiaocairuanzhuMember;
import com.ruoyi.system.domain.SciJiaocairuanzhuPiyue;
import com.ruoyi.system.domain.SciJiaocairuanzhuScoreCfg;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalNode;
import com.ruoyi.system.domain.PageRenderStatusMeta;
import com.ruoyi.system.domain.PageRenderActionItem;
import com.ruoyi.system.domain.PageRenderContext;
import com.ruoyi.system.constant.PageRenderActionConstants;
import com.ruoyi.system.constant.PageRenderColorConstants;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMemberMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuScoreCfgMapper;
import com.ruoyi.system.service.IApprovalProcessService;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMapper;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.service.ISciJiaocairuanzhuService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;


/**
 * 教材软著Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-21
 */
@Service
public class SciJiaocairuanzhuServiceImpl implements ISciJiaocairuanzhuService {
    @Autowired
    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @Autowired
    private SciJiaocairuanzhuPiyueMapper sciJiaocairuanzhuPiyueMapper;

    @Autowired
    private SciJiaocairuanzhuScoreCfgMapper sciJiaocairuanzhuScoreCfgMapper;

    @Autowired
    private SciJiaocairuanzhuMemberMapper sciJiaocairuanzhuMemberMapper;

//    @Autowired
//    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysApprovalHistoryService sysApprovalHistoryService;

    @Autowired
    private IApprovalProcessService approvalProcessService;


    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    @Override
    public SciJiaocairuanzhu selectSciJiaocairuanzhuById(Integer id) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(id);
    }

    /**
     * 查询教材软著列表
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著
     */
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
    }

    /**
     * 新增教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    @Override
    public int insertSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu) {
//        try {
//            // 获取当前用户信息
//            SysUser currentUser = userService.selectUserById(getUserId());
//
//            // 获取学院、专业、教师信息
//            String collegeName = currentUser.getCollegeName();  // 假设用户实体中有学院名称
//            String majorName = currentUser.getMajorName();    // 假设用户实体中有专业名称
//            String teacherName = currentUser.getUserName();     // 教师名称
//
//            // 获取模块名称
//            String moduleName = sciJiaocairuanzhu.getModuleName();  // 假设 SciJiaocairuanzhu 实体中有模块名称
//
//            // 获取文件名
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = FilenameUtils.getExtension(originalFilename);
//            String fileName = FilenameUtils.getBaseName(originalFilename);
//
//            // 获取当前年份
//            String year = new SimpleDateFormat("yyyy").format(new Date());
//
//            // 构建文件路径
//            String filePath = collegeName + "/" + majorName + "/" + teacherName + "/" + moduleName + "/" + year + "-" + fileName + "-" + teacherName + "." + fileExtension;
//
//            // 构建文件保存路径
//            String savePath = "path/to/upload/directory/" + filePath;  // 替换为实际的上传目录
//
//            // 创建目录（如果不存在）
//            File directory = new File(savePath.substring(0, savePath.lastIndexOf("/")));
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            // 保存文件
//            File dest = new File(savePath);
//            file.transferTo(dest);
//
//            // 设置文件路径到 SciJiaocairuanzhu 对象
//            sciJiaocairuanzhu.setFilePath(filePath);
//
//            // 保存到数据库
//            sciJiaocairuanzhuMapper.insertSciJiaocairuanzhu(sciJiaocairuanzhu);
//
//            return AjaxResult.success("保存成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error("保存失败");
//        }
        return sciJiaocairuanzhuMapper.insertSciJiaocairuanzhu(sciJiaocairuanzhu);
    }

    /**
     * 修改教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    @Override
    public int updateSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.updateSciJiaocairuanzhu(sciJiaocairuanzhu);
    }

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuByIds(String ids) {
        return sciJiaocairuanzhuMapper.deleteSciJiaocairuanzhuByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除教材软著信息
     *
     * @param id 教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuById(Integer id) {
        return sciJiaocairuanzhuMapper.deleteSciJiaocairuanzhuById(id);
    }


    @Override
    public int updateJifen(Long id, int jifen) {
        return sciJiaocairuanzhuMapper.updateJifen(id, jifen);
    }


    @Override
    @Transactional
    public int hxPass(String id, Long uid, String urlFlag) {
        // 获取原始状态
        SciJiaocairuanzhu originalJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(Integer.valueOf(id));
        String oldState = originalJiaocairuanzhu.getState();
        
        // 获取操作人信息
        SysUser operator = userService.selectUserById(uid);
        if (operator == null) {
            return 0;
        }
        
        // 构建审批请求
        String comment = "";
        if (urlFlag.equals("tijiao")) {
            comment = "提交申请";
        } else if (urlFlag.equals("pro")) {
            comment = "教研室同意";
        } else if (urlFlag.equals("hecha")) {
            comment = "学院同意";
        } else if (urlFlag.equals("chayue")) {
            comment = "科研处同意";
        }
        
        ApprovalRequest request = ApprovalRequest.of(
                "textbook_approval",
                Long.valueOf(id),
                oldState,
                comment,
                uid,
                operator.getUserName(),
                operator.getDept().getDeptName()
        );
        
        // 调用审批通过方法
        ApprovalResult result;
        if (urlFlag.equals("tijiao")) {
            // 提交申请
            result = approvalProcessService.submitApproval(request);
        } else {
            // 审批通过
            result = approvalProcessService.approve(request);
        }
        
        if (!result.isSuccess()) {
            return 0;
        }
        
        // 更改状态
        int a = sciJiaocairuanzhuMapper.hxPass(id, result.getNewState());
        
        // 插入批阅记录
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
        sciJiaocairuanzhuPiyue.setConcate(comment);
        
        if (urlFlag.equals("tijiao")) {
            sciJiaocairuanzhuPiyue.setState("提交");
        } else if (urlFlag.equals("pro")) {
            sciJiaocairuanzhuPiyue.setState("教研室通过");
        } else if (urlFlag.equals("hecha")) {
            sciJiaocairuanzhuPiyue.setState("学院通过");
        } else if (urlFlag.equals("chayue")) {
            sciJiaocairuanzhuPiyue.setState("科研处通过");
            // 计算积分
            SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(Integer.valueOf(id));
            String a1 = sciJiaocairuanzhu.getFenlei();
            String b1 = sciJiaocairuanzhu.getPaiming();
            SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg = new SciJiaocairuanzhuScoreCfg();
            sciJiaocairuanzhuScoreCfg.setFenLei(a1);
            sciJiaocairuanzhuScoreCfg.setPaiMing(b1);
            List<SciJiaocairuanzhuScoreCfg> c = sciJiaocairuanzhuScoreCfgMapper.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);
            int jifen = 0;
            for (SciJiaocairuanzhuScoreCfg cfg : c) {
                jifen = Integer.parseInt(cfg.getTotalScore());
                System.out.println("Jifen: " + jifen);
            }
            // 更改积分
            sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id), jifen);
        }
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        return a;
    }
    
    /**
     * 保存审批历史记录
     * @param businessId 业务ID
     * @param oldState 原状态
     * @param newState 新状态
     * @param operatorId 操作人ID
     * @param action 操作类型
     * @param comment 审批意见
     */
    private void saveApprovalHistory(Integer businessId, String oldState, String newState, Long operatorId, String action, String comment) {
        try {
            SysUser operator = userService.selectUserById(operatorId);
            if (operator == null) {
                return;
            }
            
            SysApprovalHistory history = new SysApprovalHistory();
            history.setProcessCode("textbook_approval");
            history.setBusinessId(businessId.longValue());
            
            // 根据状态设置审批节点信息
            if (oldState.equals("TEXTBOOK_DRAFT") && newState.equals("TEXTBOOK_JYS_AUDIT")) {
                // 提交申请
                history.setNodeId(1L);
                history.setNodeName("提交申请");
            } else if (oldState.equals("TEXTBOOK_JYS_AUDIT") && newState.equals("TEXTBOOK_KYC_AUDIT")) {
                // 教研室审批
                history.setNodeId(2L);
                history.setNodeName("教研室审批");
            } else if (oldState.equals("TEXTBOOK_KYC_AUDIT") && newState.equals("TEXTBOOK_PASSED")) {
                // 科研处审批
                history.setNodeId(3L);
                history.setNodeName("科研处审批");
            } else if (newState.equals("TEXTBOOK_REJECTED")) {
                // 驳回
                if (oldState.equals("TEXTBOOK_JYS_AUDIT")) {
                    history.setNodeId(2L);
                    history.setNodeName("教研室审批");
                } else if (oldState.equals("TEXTBOOK_KYC_AUDIT")) {
                    history.setNodeId(3L);
                    history.setNodeName("科研处审批");
                } else {
                    history.setNodeId(1L);
                    history.setNodeName("提交申请");
                }
            } else if (oldState.equals("TEXTBOOK_KYC_AUDIT") && newState.equals("TEXTBOOK_JYS_AUDIT")) {
                // 科研处撤回
                history.setNodeId(3L);
                history.setNodeName("科研处审批");
            } else if (oldState.equals("TEXTBOOK_PASSED") && newState.equals("TEXTBOOK_KYC_AUDIT")) {
                // 已通过撤回
                history.setNodeId(3L);
                history.setNodeName("科研处审批");
            } else {
                // 默认节点信息
                history.setNodeId(1L);
                history.setNodeName("提交申请");
            }
            
            history.setAction(action);
            history.setOperatorId(operatorId);
            history.setOperatorName(operator.getUserName());
            history.setOperatorDept(operator.getDept().getDeptName());
            history.setOldState(oldState);
            history.setNewState(newState);
            history.setComment(comment);
            
            sysApprovalHistoryService.insertSysApprovalHistory(history);
        } catch (Exception e) {
            // 记录错误日志，但不影响主流程
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public int hxBh(String id, Long uid, String remark, String urlFlag) {
        // 获取原始状态
        SciJiaocairuanzhu originalJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(Integer.valueOf(id));
        String oldState = originalJiaocairuanzhu.getState();
        
        // 获取操作人信息
        SysUser operator = userService.selectUserById(uid);
        if (operator == null) {
            return 0;
        }
        
        // 构建审批请求
        String comment = remark;
        if (urlFlag.equals("hecha")) {
            comment = "学院驳回: " + remark;
        } else if (urlFlag.equals("pro")) {
            comment = "教研室驳回: " + remark;
        } else if (urlFlag.equals("chayue")) {
            comment = "科研处驳回: " + remark;
        }
        
        // 直接设置状态为草稿
        String newState = "TEXTBOOK_DRAFT";
        
        // 更改状态
        int a = sciJiaocairuanzhuMapper.hxPass(id, newState);

        // 插入批阅记录
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
        sciJiaocairuanzhuPiyue.setConcate(remark);
        
        if (urlFlag.equals("hecha")) {
            sciJiaocairuanzhuPiyue.setState("被学院驳回");
        } else if (urlFlag.equals("pro")) {
            sciJiaocairuanzhuPiyue.setState("被教研室驳回");
        } else if (urlFlag.equals("chayue")) {
            sciJiaocairuanzhuPiyue.setState("被科研处驳回");
        }
        
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        // 保存审批历史记录
        saveApprovalHistory(Integer.valueOf(id), oldState, newState, uid, "驳回", comment);
        
        return a;
    }


    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList4(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList4(sciJiaocairuanzhu);
    }

    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList3(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList3(sciJiaocairuanzhu);
    }

    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList2(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList2(sciJiaocairuanzhu);
    }

    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList1(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList1(sciJiaocairuanzhu);
    }

    // 新方法：教师查询
    @Override
    public List<SciJiaocairuanzhu> selectSciPaperAListCx(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList1(sciJiaocairuanzhu);
    }

    // 新方法：教研室查询
    @Override
    public List<SciJiaocairuanzhu> selectSciPaperAListCxList(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList2(sciJiaocairuanzhu);
    }

    // 新方法：学院查询
    @Override
    public List<SciJiaocairuanzhu> selectSciPaperAListXY(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList3(sciJiaocairuanzhu);
    }

    // 新方法：科研处查询
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciJiaocairuanzhu> selectSciPaperAListKY(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList4(sciJiaocairuanzhu);
    }

    // 新方法：管理员查询
    @Override
    public List<SciJiaocairuanzhu> selectSciPaperAList(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
    }


    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList31(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList31(sciJiaocairuanzhu);
    }

    /**
     * 查询下一级状态方法
     * 获取当前审批节点信息及下一步节点信息
     * @param currentState 当前业务数据的状态
     * @return 包含当前节点、下一节点等信息的ApprovalResult
     */
    @Override
    public ApprovalResult getNextState(String currentState) {
        return approvalProcessService.getCurrentNode("textbook_approval", currentState);
    }

    /**
     * 操作记录方法
     * 记录审批操作的历史信息
     * @param businessId 业务ID
     * @param oldState 原状态
     * @param newState 新状态
     * @param operatorId 操作人ID
     * @param action 操作类型
     * @param comment 审批意见
     * @return 操作结果
     */
    @Override
    @Transactional
    public Map<String, Object> recordApprovalAction(Integer businessId, String oldState, String newState, Long operatorId, String action, String comment) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        try {
            SysUser operator = userService.selectUserById(operatorId);
            if (operator == null) {
                result.put("message", "操作人不存在");
                return result;
            }

            // 获取当前节点信息
            ApprovalResult nodeResult = getNextState(oldState);
            if (!nodeResult.isSuccess()) {
                result.put("success", false);
                result.put("message", nodeResult.getMessage());
                return result;
            }

            SysApprovalNode currentNode = nodeResult.getCurrentNode();

            // 保存审批历史记录
            SysApprovalHistory history = new SysApprovalHistory();
            history.setProcessCode("textbook_approval");
            history.setBusinessId(businessId.longValue());
            history.setNodeId(currentNode != null ? currentNode.getId() : null);
            history.setNodeName(currentNode != null ? currentNode.getNodeNm() : "");
            history.setAction(action);
            history.setOperatorId(operatorId);
            history.setOperatorName(operator.getUserName());
            history.setOperatorDept(operator.getDept().getDeptName());
            history.setOldState(oldState);
            history.setNewState(newState);
            history.setComment(comment);
            history.setCreateTime(new Date());

            int insertResult = sysApprovalHistoryService.insertSysApprovalHistory(history);
            if (insertResult > 0) {
                result.put("success", true);
                result.put("message", "操作记录保存成功");
            } else {
                result.put("message", "操作记录保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "系统异常：" + e.getMessage());
        }

        return result;
    }

    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList21(SciJiaocairuanzhu sciJiaocairuanzhu) {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList21(sciJiaocairuanzhu);
    }

    @Override
    @Transactional
    public int recall(Integer id, String state, Long uid, String remark, String urlFlag) {
        // 获取原始状态
        SciJiaocairuanzhu originalJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(id);
        String oldState = originalJiaocairuanzhu.getState();
        
        // 获取操作人信息
        SysUser operator = userService.selectUserById(uid);
        if (operator == null) {
            return 0;
        }
        
        // 构建审批请求
        ApprovalRequest request = ApprovalRequest.of(
                "textbook_approval",
                Long.valueOf(id),
                oldState,
                remark,
                uid,
                operator.getUserName(),
                operator.getDept().getDeptName()
        );
        
        // 调用审批撤回方法
        ApprovalResult result = approvalProcessService.recall(request);
        
        if (!result.isSuccess()) {
            return 0;
        }
        
        // 更改状态
        int a = sciJiaocairuanzhuMapper.hxPass(id.toString(), result.getNewState());
        
        // 插入日志
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(id);
        sciJiaocairuanzhuPiyue.setConcate(remark);
        sciJiaocairuanzhuPiyue.setState("科研处撤回");
        
        // 如果是已通过撤回，清空积分
        if (oldState.equals("TEXTBOOK_PASSED")) {
            sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id), 0);
        }
        
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        return a;
    }


    @Override
    public boolean checkExist(String mingcheng, String paiming, Long userId) {
        return sciJiaocairuanzhuMapper.checkExist(mingcheng, paiming, userId) > 0;
    }

    @Override
    public List<SciJiaocairuanzhu> getStatsQuery(Map<String, String> params) {
        return sciJiaocairuanzhuMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciJiaocairuanzhu> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciJiaocairuanzhuMapper.getStatsQueryToCheck(params);
    }
    
    @Override
    @Transactional
    public int saveJiaocairuanzhuMembers(Integer jiaocaiId, String membersJson) {
        // 先删除该教材著作已有的成员信息
        sciJiaocairuanzhuMemberMapper.deleteSciJiaocairuanzhuMemberByJiaocaiId(jiaocaiId);
        
        // 解析JSON字符串，获取成员列表
        JSONArray membersArray = JSON.parseArray(membersJson);
        
        // 遍历成员列表，插入新的成员信息
        for (int i = 0; i < membersArray.size(); i++) {
            JSONObject memberObj = membersArray.getJSONObject(i);
            SciJiaocairuanzhuMember member = new SciJiaocairuanzhuMember();
            member.setJiaocaiId(jiaocaiId);
            member.setMemberId(memberObj.getString("memberId"));
            member.setMemberName(memberObj.getString("memberName"));
            member.setRanking(memberObj.getString("ranking"));
            member.setResearchScore(memberObj.getString("researchScore"));
            
            // 根据角色设置对应的字段
            String role = memberObj.getString("role");
            if ("主编".equals(role)) {
                member.setIsChiefEditor(1);
                member.setIsAssociateEditor(0);
                member.setIsMember(0);
            } else if ("副主编".equals(role)) {
                member.setIsChiefEditor(0);
                member.setIsAssociateEditor(1);
                member.setIsMember(0);
            } else if ("成员".equals(role)) {
                member.setIsChiefEditor(0);
                member.setIsAssociateEditor(0);
                member.setIsMember(1);
            } else {
                // 默认设置为成员
                member.setIsChiefEditor(0);
                member.setIsAssociateEditor(0);
                member.setIsMember(1);
            }
            
            sciJiaocairuanzhuMemberMapper.insertSciJiaocairuanzhuMember(member);
        }
        
        return membersArray.size();
    }

    @Override
    public List<SciJiaocairuanzhuMember> getJiaocairuanzhuMembers(Integer jiaocaiId) {
        return sciJiaocairuanzhuMemberMapper.selectSciJiaocairuanzhuMemberByJiaocaiId(jiaocaiId);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuListAll(SciJiaocairuanzhu sciJiaocairuanzhu) {
        List<SciJiaocairuanzhu> list = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuListAll(sciJiaocairuanzhu);
        fillPageRenderData(list, getCurrentUser());
        return list;
    }

    /**
     * 教材软著旧状态码（0-7）映射为统一状态编码
     */
    private String mapStateToStatusCode(String state) {
        if (state == null) {
            return "TEXTBOOK_DRAFT";
        }

        if (state.contains("_")) {
            return state;
        }

        switch (state) {
            case "0":
                return "TEXTBOOK_DRAFT";
            case "1":
                return "TEXTBOOK_JYS_AUDIT";
            case "2":
            case "4":
                return "TEXTBOOK_KYC_AUDIT";
            case "3":
            case "5":
            case "7":
                return "TEXTBOOK_REJECTED";
            case "6":
                return "TEXTBOOK_PASSED";
            default:
                return "TEXTBOOK_DRAFT";
        }
    }

    /**
     * 根据状态编码获取颜色类型
     */
    private String getColorTypeByStateCode(String stateCode) {
        if (stateCode == null) {
            return PageRenderColorConstants.COLOR_DEFAULT;
        }
        if (stateCode.endsWith("_DRAFT")) {
            return PageRenderColorConstants.COLOR_DEFAULT;
        }
        if (stateCode.endsWith("_JYS_AUDIT")) {
            return PageRenderColorConstants.COLOR_WARNING;
        }
        if (stateCode.endsWith("_KYC_AUDIT")) {
            return PageRenderColorConstants.COLOR_INFO;
        }
        if (stateCode.endsWith("_PASSED")) {
            return PageRenderColorConstants.COLOR_SUCCESS;
        }
        if (stateCode.endsWith("_REJECTED")) {
            return PageRenderColorConstants.COLOR_DANGER;
        }
        return PageRenderColorConstants.COLOR_DEFAULT;
    }

    /**
     * 根据状态编码获取状态文案
     */
    private String getStateTextByStateCode(String stateCode) {
        if (stateCode == null) {
            return "未知";
        }
        switch (stateCode) {
            case "TEXTBOOK_DRAFT":
                return "草稿";
            case "TEXTBOOK_JYS_AUDIT":
                return "教研室审批中";
            case "TEXTBOOK_KYC_AUDIT":
                return "科研处审批中";
            case "TEXTBOOK_PASSED":
                return "已通过";
            case "TEXTBOOK_REJECTED":
                return "已驳回";
            default:
                return stateCode;
        }
    }

    /**
     * 构建教材软著状态元数据
     */
    private PageRenderStatusMeta buildStatusMeta(String state) {
        String normalizedState = mapStateToStatusCode(state);
        return PageRenderStatusMeta.of(normalizedState, getStateTextByStateCode(normalizedState), getColorTypeByStateCode(normalizedState));
    }

    /**
     * 构建教材软著按钮动作列表
     */
    private List<PageRenderActionItem> buildTextbookActions(SciJiaocairuanzhu entity, SysUser currentUser) {
        List<PageRenderActionItem> actions = new ArrayList<>();
        String state = mapStateToStatusCode(entity.getState());
        boolean isOwner = currentUser != null && entity.getUserId() != null 
                && currentUser.getUserId().equals(Long.valueOf(entity.getUserId()));
        boolean isAdmin = currentUser != null && hasRole(currentUser, "admin");

        boolean isDraft = state.endsWith("_DRAFT");
        boolean isJysAudit = "TEXTBOOK_JYS_AUDIT".equals(state);
        boolean isKycAudit = "TEXTBOOK_KYC_AUDIT".equals(state);
        boolean isPassed = state.endsWith("_PASSED");
        boolean isRejected = state.endsWith("_REJECTED");
        boolean isInAudit = isJysAudit || isKycAudit;

        if (hasPermission("system:jiaocairuanzhu:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW, "查看",
                    PageRenderColorConstants.COLOR_INFO, 1));
        }

        if (!isDraft && hasPermission("system:jiaocairuanzhu:info")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_VIEW_PROCESS, "查看流程",
                    PageRenderColorConstants.COLOR_INFO, 2));
        }

        if ((isDraft || isRejected) && (isOwner || isAdmin) && hasPermission("system:jiaocairuanzhu:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_EDIT, "编辑",
                    PageRenderColorConstants.COLOR_PRIMARY, 3));
        }

        if (isDraft && (isOwner || isAdmin) && hasPermission("system:jiaocairuanzhu:remove")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REMOVE, "删除",
                    PageRenderColorConstants.COLOR_DANGER, 4, "确定要删除该教材软著吗？"));
        }

        if (isDraft && (isOwner || isAdmin) && hasPermission("system:jiaocairuanzhu:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要提交该教材软著吗？"));
        }

        if (isRejected && (isOwner || isAdmin) && hasPermission("system:jiaocairuanzhu:edit")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_SUBMIT, "重新提交",
                    PageRenderColorConstants.COLOR_SUCCESS, 5, "确定要重新提交该教材软著吗？"));
        }

        if (isJysAudit && hasPermission("system:jiaocairuanzhu:process")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REVIEW, "教研室审批",
                    PageRenderColorConstants.COLOR_PRIMARY, 6));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 7, "确定要通过该教材软著吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 8, "确定要驳回该教材软著吗？"));
        }

        if (isJysAudit && isOwner && hasPermission("system:jiaocairuanzhu:remove")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 9, "确定要撤回该教材软著吗？"));
        }

        if (isKycAudit && hasPermission("system:jiaocairuanzhu:chayue")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_KY_REVIEW, "科研处审批",
                    PageRenderColorConstants.COLOR_PRIMARY, 6));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_APPROVE, "通过",
                    PageRenderColorConstants.COLOR_SUCCESS, 7, "确定要通过该教材软著吗？"));
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_REJECT, "驳回",
                    PageRenderColorConstants.COLOR_DANGER, 8, "确定要驳回该教材软著吗？"));
        }

        if (isKycAudit && hasPermission("system:jiaocairuanzhu:process")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 9, "确定要撤回该教材软著吗？"));
        }

        if (isPassed && hasPermission("system:jiaocairuanzhu:chayue")) {
            actions.add(PageRenderActionItem.of(
                    PageRenderActionConstants.ACTION_RECALL, "撤回",
                    PageRenderColorConstants.COLOR_WARNING, 9, "确定要撤回该教材软著吗？"));
        }

        Collections.sort(actions);
        return actions;
    }

    /**
     * 填充页面渲染数据
     */
    private void fillPageRenderData(List<SciJiaocairuanzhu> list, SysUser currentUser) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (SciJiaocairuanzhu entity : list) {
            entity.setStatusMeta(buildStatusMeta(entity.getState()));
            entity.setActions(buildTextbookActions(entity, currentUser));
        }
    }

    /**
     * 填充单条数据的页面渲染数据
     */
    private void fillPageRenderData(SciJiaocairuanzhu entity, SysUser currentUser) {
        if (entity == null) {
            return;
        }
        entity.setStatusMeta(buildStatusMeta(entity.getState()));
        entity.setActions(buildTextbookActions(entity, currentUser));
    }

    /**
     * 判断用户是否拥有指定角色
     */
    private boolean hasRole(SysUser user, String roleKey) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream().anyMatch(r -> roleKey.equals(r.getRoleKey()));
    }

    /**
     * 判断当前用户是否拥有指定权限
     */
    private boolean hasPermission(String permission) {
        try {
            return org.apache.shiro.SecurityUtils.getSubject().isPermitted(permission);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前登录用户
     */
    private SysUser getCurrentUser() {
        try {
            return (SysUser) org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
