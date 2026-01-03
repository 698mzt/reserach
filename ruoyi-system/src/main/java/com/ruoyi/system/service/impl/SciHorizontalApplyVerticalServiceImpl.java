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
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        if (sciHorizontalApplyVertical.getRole().equals("research"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJYS(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("sci_tesearch"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKYC(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("dept_teacher"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListDept(sciHorizontalApplyVertical);
        else
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
        return list;
    }

    /**
     * 保存立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
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
        return id;
    }

    @Override
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

    @Override
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


    @Override
    public java.util.List<String> selectPersionIdsByVerticalId(Integer verticalId) {
        return sciHorizontalApplyVerticalMapper.selectPersionIdsByVerticalId(verticalId);
    }

    @Override
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

    @Override
    public int applyPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,String SubjectSource) {
        String state = "0";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(verticalId);
        if(urlFlag.equals("JYS")){
            state ="2";
        }else if(urlFlag.equals("KYC")){
            state ="6";
            if (persion.size() > score.size()) {
                throw new RuntimeException("积分配置与成员数量不匹配");
            }
            String status = "立项";
            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
            sciUserScore.setChangeStatus("立项");
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                sciUserScoreMapper.insertScoreVertical(sciUserScore);
            }
        }else if(urlFlag.equals("Dept")){
            state ="4";
        }
        int a =  sciHorizontalApplyVerticalMapper.applyPass(id,state,SubjectSource);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setVerticalId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertVerticalPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int applyBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="3";
        }else if(urlFlag.equals("KYC")){
            state ="7";
        }else if (urlFlag.equals("Dept")){
            state ="5";
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



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int overPass(String id, Long userId, String urlFlag,List score,List persion,String verticalId,String subjectSource) {
        String state = "0";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setVerticalId(verticalId);
        if(urlFlag.equals("JYS")){
            state ="22";
        }else if(urlFlag.equals("KYC")){
            state ="66";
            if (persion.size() > score.size()) {
                throw new RuntimeException("积分配置与成员数量不匹配");
            }
            String status = "结项";
            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
            sciUserScore.setChangeStatus("结项");
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                sciUserScoreMapper.insertScoreVertical(sciUserScore);
            }
        }else if(urlFlag.equals("Dept")){
            state ="44";
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

    @Override
    public int overBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="33";
        }else if(urlFlag.equals("KYC")){
            state ="77";
        }else if (urlFlag.equals("Dept")){
            state ="55";
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

    @Override
    public int recall(Integer id, String state, Long userId, String remark, String urlFlag) {
        String newState = state;
        switch (state){
//            教研室
            case "2": case "3":
                newState = "1";
                break;
            case "22": case "33":
                newState = "11";
                break;
//                学院
            case "4":  case "5":
                newState = "2";
                break;
            case "44": case"55":
                newState = "22";
                break;
//                科研处
            case "6":  case "7":
                newState = "4";
                break;
            case "66": case "77":
                newState = "44";
                break;
        }
        if(state.equals("6")){
            String status = "立项";
            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
        }else
        if(state.equals("66")){
            String status = "结项";
            sciUserScoreMapper.deleteVerticalScoreById(id.toString(),status);
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
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> getStatsQuery(Map<String, String> params) {
        return sciHorizontalApplyVerticalMapper.getStatsQuery(params);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApplyVertical> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciHorizontalApplyVerticalMapper.getStatsQueryToCheck(params);
    }


}
