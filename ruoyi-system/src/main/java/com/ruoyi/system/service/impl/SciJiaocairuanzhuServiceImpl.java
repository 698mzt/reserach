package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciJiaocairuanzhuMember;
import com.ruoyi.system.domain.SciJiaocairuanzhuPiyue;
import com.ruoyi.system.domain.SciJiaocairuanzhuScoreCfg;
import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMemberMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuScoreCfgMapper;
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
        
        String state = "TEXTBOOK_DRAFT";

        if (urlFlag.equals("hecha")) {
            // 学院审批 --> 科研处审批（保留兼容）
            state = "TEXTBOOK_KYC_AUDIT";
        } else if (urlFlag.equals("tijiao")) {
            // 本人提交  草稿--> 教研室审批
            state = "TEXTBOOK_JYS_AUDIT";

        } else if (urlFlag.equals("pro")) {
            // 教研室审批 -- 科研处审批
            state = "TEXTBOOK_KYC_AUDIT";
        } else if (urlFlag.equals("chayue")) {
            // 科研处审批 -->科研处通过
            state = "TEXTBOOK_PASSED";
            SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(Integer.valueOf(id));
            String a = sciJiaocairuanzhu.getFenlei();
            String b = sciJiaocairuanzhu.getPaiming();
            SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg = new SciJiaocairuanzhuScoreCfg();
            sciJiaocairuanzhuScoreCfg.setFenLei(a);
            sciJiaocairuanzhuScoreCfg.setPaiMing(b);
            List<SciJiaocairuanzhuScoreCfg> c = sciJiaocairuanzhuScoreCfgMapper.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);
            //计算积分
            int jifen = 0;
            for (SciJiaocairuanzhuScoreCfg cfg : c) {
                jifen = Integer.parseInt(cfg.getTotalScore());
                System.out.println("Jifen: " + jifen);
            }
            // 更改积分
            sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id), jifen);


        }
        // 更改状态
        int a = sciJiaocairuanzhuMapper.hxPass(id, state);
        // 插入批阅记录
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        String comment = "";
        if (urlFlag.equals("tijiao")) {
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("提交申请");
            sciJiaocairuanzhuPiyue.setState("提交");
            comment = "提交申请";
        } else if (urlFlag.equals("pro") ) {
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("教研室同意");
            sciJiaocairuanzhuPiyue.setState("教研室通过");
            comment = "教研室同意";
        }else if (urlFlag.equals("hecha") ) {
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("学院同意");
            sciJiaocairuanzhuPiyue.setState("学院通过");
            comment = "学院同意";
        }else if (urlFlag.equals("chayue")) {
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("科研处同意");
            sciJiaocairuanzhuPiyue.setState("科研处通过");
            comment = "科研处同意";
        }
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        // 保存审批历史记录
        saveApprovalHistory(Integer.valueOf(id), oldState, state, uid, "approve", comment);

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
        
        String state = "TEXTBOOK_REJECTED";
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        String comment = remark;
        if (urlFlag.equals("hecha")) {
            sciJiaocairuanzhuPiyue.setState("被学院驳回");
            comment = "学院驳回: " + remark;
        } else if (urlFlag.equals("pro")) {
            sciJiaocairuanzhuPiyue.setState("被教研室驳回");
            comment = "教研室驳回: " + remark;
        } else if (urlFlag.equals("chayue")) {
            sciJiaocairuanzhuPiyue.setState("被科研处驳回");
            comment = "科研处驳回: " + remark;
        }
        int a = sciJiaocairuanzhuMapper.hxPass(id, state);

        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
        sciJiaocairuanzhuPiyue.setConcate(remark);

        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        // 保存审批历史记录
        saveApprovalHistory(Integer.valueOf(id), oldState, state, uid, "reject", comment);
        
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
        
        String newState = state;
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();

        switch (state) {
//            科研处审批撤回 --> 教研室审批
            case "TEXTBOOK_KYC_AUDIT":
                newState = "TEXTBOOK_JYS_AUDIT";
                sciJiaocairuanzhuPiyue.setState("科研处撤回");
                break;
            //            已通过撤回 --> 科研处审批
            case "TEXTBOOK_PASSED":
                newState = "TEXTBOOK_KYC_AUDIT";
                sciJiaocairuanzhuPiyue.setState("科研处撤回");
                sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id), 0);
                break;
        }


//        设置状态
        int a = sciJiaocairuanzhuMapper.hxPass(id.toString(), newState);
//        插入日志
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(id);
        sciJiaocairuanzhuPiyue.setConcate(remark);

        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        
        // 保存审批历史记录
        saveApprovalHistory(id, oldState, newState, uid, "recall", remark);
        
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
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuListAll(sciJiaocairuanzhu);
    }
}
