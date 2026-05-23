package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysApprovalHistory;
import com.ruoyi.system.domain.SysApprovalProcess;
import com.ruoyi.system.domain.SysApprovalState;
import com.ruoyi.system.mapper.SysApprovalHistoryMapper;
import com.ruoyi.system.mapper.SysApprovalProcessMapper;
import com.ruoyi.system.mapper.SysApprovalStateMapper;
import com.ruoyi.system.service.ISysApprovalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysApprovalHistoryServiceImpl implements ISysApprovalHistoryService {
    @Autowired
    private SysApprovalHistoryMapper sysApprovalHistoryMapper;

    @Autowired
    private SysApprovalProcessMapper sysApprovalProcessMapper;

    @Autowired
    private SysApprovalStateMapper sysApprovalStateMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public SysApprovalHistory selectSysApprovalHistoryById(Long id) {
        if (id == null) {
            return null;
        }
        return sysApprovalHistoryMapper.selectSysApprovalHistoryById(id);
    }

    @Override
    public List<SysApprovalHistory> selectSysApprovalHistoryList(SysApprovalHistory sysApprovalHistory) {
        if (sysApprovalHistory == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalHistory> list = sysApprovalHistoryMapper.selectSysApprovalHistoryList(sysApprovalHistory);
        if (list != null && !list.isEmpty()) {
            fillRelatedData(list);
            if (sysApprovalHistory.getBusinessName() != null && !sysApprovalHistory.getBusinessName().trim().isEmpty()) {
                final String businessNameSearch = sysApprovalHistory.getBusinessName().trim();
                list = list.stream()
                    .filter(h -> h.getBusinessName() != null && h.getBusinessName().contains(businessNameSearch))
                    .collect(java.util.stream.Collectors.toList());
            }
        }
        return list != null ? list : new java.util.ArrayList<>();
    }

    /**
     * 根据业务表名和业务ID获取业务名称
     * @param businessTable 业务表名
     * @param businessId 业务ID
     * @return 业务名称
     */
    private String getBusinessName(String businessTable, Long businessId) {
        try {
            switch (businessTable) {
                case "sci_horizontal_apply":
                    return jdbcTemplate.queryForObject("SELECT top_name FROM sci_horizontal_apply WHERE id = ?", String.class, businessId);
                case "sci_horizontal_apply_vertical":
                    return jdbcTemplate.queryForObject("SELECT top_name FROM sci_horizontal_apply_vertical WHERE id = ?", String.class, businessId);
                case "sci_paper_a":
                    return jdbcTemplate.queryForObject("SELECT paper_title FROM sci_paper_a WHERE id = ?", String.class, businessId);
                case "sci_jiaocairuanzhu":
                    return jdbcTemplate.queryForObject("SELECT mingcheng FROM sci_jiaocairuanzhu WHERE id = ?", String.class, businessId);
                case "sci_IntraSchPro_apply":
                    return jdbcTemplate.queryForObject("SELECT top_name FROM sci_IntraSchPro_apply WHERE id = ?", String.class, businessId);
                case "sys_reward":
                    return jdbcTemplate.queryForObject("SELECT reward_name FROM sys_reward WHERE id = ?", String.class, businessId);
                case "sci_lecture_report":
                    return jdbcTemplate.queryForObject("SELECT report_theme FROM sci_lecture_report WHERE id = ?", String.class, businessId);
                case "sci_zhuanliruanzhu":
                    return jdbcTemplate.queryForObject("SELECT mingcheng FROM sci_zhuanliruanzhu WHERE id = ?", String.class, businessId);
                case "sci_horizontal_reamount":
                    return jdbcTemplate.queryForObject("SELECT top_name FROM sci_horizontal_apply WHERE id = (SELECT apply_id FROM sci_horizontal_reamount WHERE reid = ?)", String.class, businessId);
                default:
                    return null;
            }
        } catch (Exception e) {
            // 如果查询失败，返回null
            return null;
        }
    }

    /**
     * 填充关联数据
     * @param historyList 审批历史列表
     */
    private void fillRelatedData(List<SysApprovalHistory> historyList) {
        // 获取所有流程编码
        Map<String, SysApprovalProcess> processMap = new HashMap<>();
        Map<String, Map<String, SysApprovalState>> stateMap = new HashMap<>();

        // 收集所有流程编码
        for (SysApprovalHistory history : historyList) {
            String processCode = history.getProcessCode();
            if (processCode != null && !processMap.containsKey(processCode)) {
                SysApprovalProcess process = sysApprovalProcessMapper.selectSysApprovalProcessByProcessCode(processCode);
                if (process != null) {
                    processMap.put(processCode, process);
                    // 获取该流程的所有状态
                    List<SysApprovalState> states = sysApprovalStateMapper.selectSysApprovalStateByProcessCode(processCode);
                    if (states != null && !states.isEmpty()) {
                        Map<String, SysApprovalState> stateCodeMap = new HashMap<>();
                        for (SysApprovalState state : states) {
                            stateCodeMap.put(state.getStateCode(), state);
                        }
                        stateMap.put(processCode, stateCodeMap);
                    }
                }
            }
        }

        // 填充关联数据
        for (SysApprovalHistory history : historyList) {
            String processCode = history.getProcessCode();
            // 填充流程名称
            if (processCode != null && processMap.containsKey(processCode)) {
                history.setProcessName(processMap.get(processCode).getProcessName());
            }

            // 填充状态名称
            if (processCode != null && stateMap.containsKey(processCode)) {
                Map<String, SysApprovalState> stateCodeMap = stateMap.get(processCode);
                // 填充原状态名称
                if (history.getOldState() != null && stateCodeMap.containsKey(history.getOldState())) {
                    history.setOldStateName(stateCodeMap.get(history.getOldState()).getStateName());
                }
                // 填充新状态名称
                if (history.getNewState() != null && stateCodeMap.containsKey(history.getNewState())) {
                    history.setNewStateName(stateCodeMap.get(history.getNewState()).getStateName());
                }
            }

            // 填充业务名称
            if (processCode != null && processMap.containsKey(processCode)) {
                String businessTable = processMap.get(processCode).getBusinessTable();
                if (businessTable != null && history.getBusinessId() != null) {
                    history.setBusinessName(getBusinessName(businessTable, history.getBusinessId()));
                }
            }
        }
    }

    @Override
    public List<SysApprovalHistory> selectSysApprovalHistoryByBusinessId(String processCode, Long businessId) {
        if (processCode == null || processCode.trim().isEmpty() || businessId == null) {
            return new java.util.ArrayList<>();
        }
        List<SysApprovalHistory> list = sysApprovalHistoryMapper.selectSysApprovalHistoryByBusinessId(processCode,
                businessId);
        if (list != null && !list.isEmpty()) {
            fillRelatedData(list);
        }
        return list != null ? list : new java.util.ArrayList<>();
    }

    @Override
    public SysApprovalHistory selectLastRejectByBusinessId(String processCode, Long businessId) {
        if (processCode == null || processCode.trim().isEmpty() || businessId == null) {
            return null;
        }
        return sysApprovalHistoryMapper.selectLastRejectByBusinessId(processCode, businessId);
    }

    @Override
    public int insertSysApprovalHistory(SysApprovalHistory sysApprovalHistory) {
        if (sysApprovalHistory == null) {
            return 0;
        }
        return sysApprovalHistoryMapper.insertSysApprovalHistory(sysApprovalHistory);
    }

    @Override
    public int deleteSysApprovalHistoryByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return sysApprovalHistoryMapper.deleteSysApprovalHistoryByIds(ids);
    }

    @Override
    public int deleteSysApprovalHistoryById(Long id) {
        if (id == null) {
            return 0;
        }
        return sysApprovalHistoryMapper.deleteSysApprovalHistoryById(id);
    }
}
