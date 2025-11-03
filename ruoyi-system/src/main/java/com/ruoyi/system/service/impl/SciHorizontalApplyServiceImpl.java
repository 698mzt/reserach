package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 横向课题Service业务层处理
 *
 * @author zhansan
 * @date 2024-08-16
 */
@Slf4j  // 添加此注解
@Service
public class SciHorizontalApplyServiceImpl implements ISciHorizontalApplyService
{
    @Autowired
    private SciHorizontalApplyMapper sciHorizontalApplyMapper;
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;
    @Autowired
    private SciUserScoreMapper sciUserScoreMapper;
    @Autowired
    private SciHorizontalReamountService sciHorizontalReamountService;
    @Autowired
    private SciHorizontalApplyMapper sciIntraschproApplyMapper;
    @Autowired
    private SciHorizontalApplyMapper sciPaperAMapper;
    @Autowired
    private SciHorizontalApplyMapper sciJiaocairuanzhuMapper;
    @Autowired
    private SciHorizontalApplyMapper sciZhuanliruanzhuMapper;
    @Autowired
    private SciHorizontalApplyMapper sysRewardMapper;
    @Autowired
    private SciHorizontalApplyMapper sciLectureReportMapper;
    @Autowired
    private SciHorizontalReamountMapper sciHorizontalReamountMapper;

    /**
     * 查询横向课题
     *
     * @param id 横向课题主键
     * @return 横向课题
     */
    @Override
    public SciHorizontalApply selectSciHorizontalApplyById(Integer id)
    {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
    }

    /**
     * 查询横向课题列表
     *
     * @param sciHorizontalApply 横向课题
     * @return 横向课题
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply)
    {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyList(sciHorizontalApply);
    }
    @Override
    public List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply)
    {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
    }
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByJYS(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByJYS(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApply(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApply(sciHorizontalApply);
    }
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyJYS(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApplyJYS(sciHorizontalApply);
    }
    @Override
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverApplyKYC(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverApplyKYC(sciHorizontalApply);
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOVER(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOVER(sciHorizontalApply);
    }
    @Override
    @DataScope(deptAlias = "d")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOVERKYC(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOVERKYC(sciHorizontalApply);
    }


    @Override
    public int overApply(String id, String state) {
        return sciHorizontalApplyMapper.overApply(id,state);
    }



    /**
     * 新增横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply)
    {
        // 新增：插入前查重
        SciHorizontalApply query = new SciHorizontalApply();
        query.setTopName(sciHorizontalApply.getTopName());
        query.setTopNumber(sciHorizontalApply.getTopNumber());
        List<SciHorizontalApply> existList = sciHorizontalApplyMapper.selectSciHorizontalApplyList(query);
        if (existList != null && !existList.isEmpty()) {
            // 课题名称或编号已存在，返回-1
            return -1;
        }
        sciHorizontalApplyMapper.insertSciHorizontalApply(sciHorizontalApply);

        //插入人员的排名
        Integer id = sciHorizontalApply.getId();
        SciHorizontalPersion sciHorizontalPersion = new SciHorizontalPersion();
        sciHorizontalPersion.setApplyid(id);
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFirstPersonId())) {
            sciHorizontalPersion.setRanking("1");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFirstPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getSecondPersonId())) {
            sciHorizontalPersion.setRanking("2");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getSecondPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getThirdPersonId())) {
            sciHorizontalPersion.setRanking("3");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getThirdPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFourthPersonId())) {
            sciHorizontalPersion.setRanking("4");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFourthPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        Integer applyid = sciHorizontalApply.getId() ;
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(Integer.valueOf(applyid));
        sciHorizontalPiyue.setConcate("新增数据");
        sciHorizontalPiyue.setState("新增");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return id;
    }




    /**
     * 修改横向课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */
    @Override
    public int updateSciHorizontalApply(SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);

        Integer id = sciHorizontalApply.getId();
        SciHorizontalPersion sciHorizontalPersion = new SciHorizontalPersion();
        sciHorizontalPersion.setApplyid(id);
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFirstPersonId()) ) {
            sciHorizontalApplyMapper.deletePersion(sciHorizontalPersion);
            sciHorizontalPersion.setRanking("1");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFirstPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getSecondPersonId())) {
            sciHorizontalPersion.setRanking("2");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getSecondPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getThirdPersonId())) {
            sciHorizontalPersion.setRanking("3");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getThirdPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        if (StringUtils.isNotEmpty(sciHorizontalApply.getFourthPersonId())) {
            sciHorizontalPersion.setRanking("4");
            sciHorizontalPersion.setPersionid(sciHorizontalApply.getFourthPersonId());
            sciHorizontalApplyMapper.insertPersion(sciHorizontalPersion);
        }
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        if (sciHorizontalApply.getNewsql().equals("99")) {
            sciHorizontalPiyue.setConcate("提交申请");
            sciHorizontalPiyue.setState("提交");
        }else {
            sciHorizontalPiyue.setConcate("修改");
            sciHorizontalPiyue.setState("修改");
        }
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return '1';
    }

    /**
     * 批量删除横向课题
     * @param ids 需要删除的横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyByIds(String ids)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalApplyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除横向课题信息
     *
     * @param ids 横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyById(Integer ids)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalApplyById(ids);
    }

    /**
     * 删除横向课题另一个表信息
     *
     * @param id 横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalOverApplyById(Integer id)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalOverApplyById(id);
    }

    @Override
    public int hxPass(String id,Long uid,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="4";
        }else if(urlFlag.equals("pro")){
            state ="2";
//            学院通过
        }else if (urlFlag.equals("Dept")){
            state ="11";
        }

        int a =  sciHorizontalApplyMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int amountPass(String id,String reid, Long uid, String urlFlag, List score, List persion, Integer applyId,String amountType) {
        String state = "0";
        SciUserScore sciUserScore = new SciUserScore();
        sciUserScore.setApplyId(applyId.toString());
        if(urlFlag.equals("hecha")){
            state ="6";
//            以负责人列表大小为准，顺序匹配每个负责人所对应的分数，记录到sciUserScore中。
//            科研处
            for (int i = 0; i < persion.size(); i++) {
                sciUserScore.setUserId(persion.get(i).toString());
                sciUserScore.setChangeValue(score.get(i).toString());
                if (amountType.equals("1"))
                    sciUserScore.setChangeStatus("立项");
                if (amountType.equals("2"))
                    sciUserScore.setChangeStatus("结项");
                sciUserScoreMapper.insertScoreHistory(sciUserScore);
            }
//            教研室
        }else if(urlFlag.equals("pro")){
            state ="2";
//            学院通过
        }else if (urlFlag.equals("Dept")){
            state ="11";
        }
        int a =  sciHorizontalReamountService.amountpass(reid,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int hxover(String id,Long uid,String urlFlag) {
        String state = "0";
//        SciUserScore sciUserScore = new SciUserScore();
//        sciUserScore.setApplyId(applyId.toString());
        if(urlFlag.equals("JYSOVER")){
            state ="8";
        }else if(urlFlag.equals("KYCOVER")){
            state ="6";
            //            以负责人列表大小为准，顺序匹配每个负责人所对应的分数，记录到sciUserScore中。
//            for (int i = 0; i < persion.size(); i++) {
//                sciUserScore.setUserId(persion.get(i).toString());
//                sciUserScore.setChangeValue(score.get(i).toString());
//                sciUserScore.setChangeStatus("结项");
//                sciUserScoreMapper.insertScoreHistory(sciUserScore);
//            }
        }else if (urlFlag.equals("DeptOVER")){
            state ="33";
        }
        int a =  sciHorizontalApplyMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int hxBh(String id,Long uid, String remark,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="5";
        }else if(urlFlag.equals("pro")){
            state ="3";
        }else if (urlFlag.equals("Dept")){
            state ="22";
        }
        int a = sciHorizontalApplyMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("驳回");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int hxoverBh(String id, Long uid, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYSOVER")){
            state ="9";
        }else if(urlFlag.equals("KYCOVER")){
            state ="10";
        }else if (urlFlag.equals("DeptOVER")){
            state ="44";
        }
        int a =  sciHorizontalApplyMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }



    @Override
    public List<SciHorizontalApply> selectOtherListByUid(SciHorizontalApply sciHorizontalApply) {
        String role = sciHorizontalApply.getRole();
        String tableId = sciHorizontalApply.getTableId();
        List<SciHorizontalApply> list = new ArrayList<>();
//        科研处
        if(role.equals("sci_tesearch")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
//        教研室
        else if(role.equals("research")){
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
//        教师
        else {
            switch (tableId){
                case "bootstrap-table0":
                    sciHorizontalApply.setNewsql("00");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table1":
                    sciHorizontalApply.setNewsql("01");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
                case "bootstrap-table2":
                    sciHorizontalApply.setNewsql("02");
                    list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
                    break;
            }
        }
        return list;
    }

    //     申请结项流程
    @Override
    public int overSaveSciHorizontalApply(SciHorizontalApply sciHorizontalApply) {
        // 校验结项日期必须在立项日期后
        String signingData = sciHorizontalApply.getSigningData();
        String validityDate = sciHorizontalApply.getValidityDate();
        if (signingData != null && validityDate != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(signingData);
                Date end = sdf.parse(validityDate);
                if (!end.after(start)) {
                    return -1;
                }
            } catch (Exception e) {
                return -2;
            }
        }
//            获取到账金额
        String creditedAmountValue = sciHorizontalApplyMapper.selectCreditedAmountById(sciHorizontalApply.getId());
        if (creditedAmountValue != null && StringUtils.isNotEmpty(creditedAmountValue)){
            try {
//                新增总金额
                BigDecimal credited = new BigDecimal(creditedAmountValue);
                if (sciHorizontalApply.getReAmount() != null && StringUtils.isNotEmpty(sciHorizontalApply.getReAmount())){
                    credited = credited.add(new BigDecimal(sciHorizontalApply.getReAmount()));
                }
//                项目金额
                BigDecimal applied = new BigDecimal(sciHorizontalApply.getAmount());
                if (credited.compareTo(applied) < 0) {
                    return -3; //新增总金额小于项目金额
                }
                if (applied.compareTo(credited) < 0) {
                    return -4; //新增总金额大于项目金额
                }
            } catch (NumberFormatException e) {
                return -2; // 解析失败
            }
        }
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(sciHorizontalApply.getId());
        sciHorizontalPiyue.setConcate("提交结项");
        sciHorizontalPiyue.setState("提交");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.exportSciHorizontalApplyList(sciHorizontalApply);
    }

    @Override
    public int deletePersionByid(String ids) {
        return sciHorizontalApplyMapper.deletePersionByid(Convert.toStrArray(ids));
    }

    @Override
    @Transactional
    public void saveApplyPersons(Integer applyId, List<String> personIds) {
        if (applyId == null) {
            return;
        }
        // 去重并保持顺序
        LinkedHashSet<String> orderedSet = new LinkedHashSet<>();
        if (personIds != null) {
            for (String pid : personIds) {
                if (StringUtils.isNotEmpty(pid)) {
                    orderedSet.add(pid);
                }
            }
        }
        List<String> ordered = new ArrayList<>(orderedSet);

        // 先清空原有成员
        SciHorizontalPersion del = new SciHorizontalPersion();
        del.setApplyid(applyId);
        sciHorizontalApplyMapper.deletePersion(del);

        // 按顺序插入，ranking 从 1 开始
        SciHorizontalPersion rec = new SciHorizontalPersion();
        rec.setApplyid(applyId);
        for (int i = 0; i < ordered.size(); i++) {
            rec.setRanking(String.valueOf(i + 1));
            rec.setPersionid(ordered.get(i));
            sciHorizontalApplyMapper.insertPersion(rec);
        }
    }

    @Override
    public java.util.List<String> selectPersionIdsByApplyId(Integer applyId) {
        return sciHorizontalApplyMapper.selectPersionIdsByApplyId(applyId);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByDept(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByDept(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyListByOverDept(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByOverDept(sciHorizontalApply);
    }

    @Override
    public int sci_horizontal_piyue(Integer id) {
        return sciHorizontalApplyMapper.sci_horizontal_piyue(id);
    }

    @Override
    public int recall(Integer id, String state,Long uid, String remark, String urlFlag) {
        String newState = state;
        switch (state){
//            教研室
            case "2": case "3":
                newState = "1";
                break;
            case "8": case "9":
                newState = "7";
                break;
//                学院
            case "11":  case "22":
                newState = "2";
                break;
            case "33": case"44":
                newState = "8";
                break;
//                科研处
            case "4":  case "5":
                newState = "11";
                break;
            case "6": case "10":
                newState = "33";
                break;
//                教师
            case "7":
                newState = "4";
                break;
        }
        if(state.equals("4")){
            String status = "立项";
            sciUserScoreMapper.deleteScoreById(id.toString(),status);
        }else
        if(state.equals("6")){
            String status = "结项";
            sciUserScoreMapper.deleteScoreById(id.toString(),status);
        }
//        设置状态
        int a =sciHorizontalApplyMapper.hxPass(id.toString(),newState);
//        插入日志
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(id);
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("撤回");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public int amountBh(String id, String reid, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("pro")){
            state ="3";
        }else if(urlFlag.equals("hecha")){
            state ="5";
        }else if (urlFlag.equals("Dept")){
            state ="22";
        }
        int a =  sciHorizontalReamountService.amountpass(reid,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(userId);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
        sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
        return a;
    }

    @Override
    public List<creditedAmount> selectCreditedAmount() {
        return sciHorizontalApplyMapper.selectCreditedAmount();
    }

    /**
     * 提交课题
     *
     * @param sciHorizontalApply 横向课题
     * @return 结果
     */


    private static final Set<String> COLLEGE_ROLES = new HashSet<>(Arrays.asList(
            "dept_teacher",       // 软件学院管理员
            "discuss_college",    // 商学院管理员
            "dzgc_college",       // 电子工程学院管理员
            "art_design_college", // 艺术设计学院管理员
            "cxcy_college",       // 创新创业学院管理员
            "marxism_college"     // 马克思主义学院管理员
    ));
    @Override
    public Map<String, Integer> getTodoCount(SysUser user) {
        Map<String, Integer> counts = new HashMap<>();
        String mainRole = determineMainRole(user.getRoles());

        // 添加调试日志
        log.info("用户角色识别: ID={}, 部门={}, 识别角色={}, 实际角色={}",
                user.getUserId(),
                user.getDept().getDeptName(),
                mainRole,
                user.getRoles().stream()
                        .map(SysRole::getRoleKey)
                        .collect(Collectors.joining(",")));

//        counts.put("horizontal", calculateHorizontalTodoCount(user, mainRole));
//        counts.put("reamount", calculateReamountTodoCount(user, mainRole));//统计金额待办数量
//        return counts;
        // 计算课题待办数量
        int horizontalCount = calculateHorizontalTodoCount(user, mainRole);
        // 计算金额待办数量
        int reamountCount = calculateReamountTodoCount(user, mainRole);

        // 记录详细统计信息（调试用）
        log.info("待办统计 - 课题: {}, 金额: {}, 总计: {}",
                horizontalCount, reamountCount, horizontalCount + reamountCount);

        // 保持使用 "horizontal" 键，但值为总数
        counts.put("horizontal", horizontalCount + reamountCount);
//        counts.put("reamount",reamountCount); //如果需要保留
        counts.put("vertical", calculateVerticalTodoCount(user, mainRole)); //统计纵向待办数量
        counts.put("achievement", calculateAchievementTodoCount(user, mainRole)); //统计成果待办数量
        counts.put("paper", calculatePaperTodoCount(user, mainRole)); // 统计论文待办数量
        counts.put("textbook", calculateTextbookTodoCount(user, mainRole)); // 统计教材软著待办数量
        counts.put("patent", calculatePatentTodoCount(user, mainRole)); // 统计专利软著待办数量
        counts.put("reward", calculateRewardTodoCount(user, mainRole)); // 统计奖励待办数量
        counts.put("lecture", calculateLectureTodoCount(user, mainRole)); // 统计讲座报告待办数量
        return counts;
    }
    /**
     * 计算金额审批待办数量
     */
    private int calculateReamountTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount2 = countReamountByStates(Arrays.asList("11", "33"));
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount1 = countReamountByDeptAndStates(collegeDeptId, Arrays.asList("2", "8"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
                int deptCount = countReamountByDeptAndStates(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalReamountTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
        }
        return count;
    }

    // 金额审批的个人待办统计
    private int countPersonalReamountTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countReamountByUserAndStates(userId, states, getCurrentYear());
    }

    // 金额审批的部门统计（包含子部门）
    private int countReamountByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countReamountByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countReamountByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    // 金额审批的状态统计
    private int countReamountByStates(List<String> states) {
        return sciHorizontalApplyMapper.countReamountByStates(states, getCurrentYear());
    }

    /**
     * 确定用户的主要角色
     */
    private String determineMainRole(List<SysRole> roles) {
        // 按角色优先级顺序判断
        if (hasRole(roles, "sci_tesearch")) return "sci_tesearch"; //科研处
//        if (hasRole(roles, "dept_teacher")) return "dept_teacher"; // 学院
        if (roles.stream().anyMatch(role -> COLLEGE_ROLES.contains(role.getRoleKey()))) { return "dept_teacher";}
        if (hasRole(roles, "research")) return "research";  //  教研室
        return "teacher";
    }

    private boolean hasRole(List<SysRole> roles, String roleKey) {
        return roles.stream().anyMatch(r -> roleKey.equals(r.getRoleKey()));
    }

    /**
     * 计算横向课题待办数量
     */
    private int countApprovalItems(List<String> states) {
        return sciHorizontalApplyMapper.countByStates(states, getCurrentYear());
    }

    //    private int countDeptApprovals(Long deptId, List<String> states) {
//        return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
//    }
// 修改后的部门统计方法
//    private int countDeptApprovals(Long deptId, List<String> states, String roleKey) {
//        return isCollegeLevelRole(roleKey) ?
//                sciHorizontalApplyMapper.countByDeptAndStatesWithChildren(deptId, states, getCurrentYear()) :
//                sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
//    }

    // 判断学院级角色
//    private boolean isCollegeLevelRole(String roleKey) {
//        return Arrays.asList("dept_teacher", "sci_tesearch").contains(roleKey);
//    }

    private int countResearchApprovals(Long deptId, List<String> states) {
        return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
    }

    private int countPersonalTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countByUserAndStates(userId, states, getCurrentYear());
    }

    private String getCurrentYear() {
        return String.valueOf(Year.now().getValue());
    }
    //    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
//        int count;
//        switch (roleKey) {
//            case "sci_tesearch":
////                count = countApprovalItems(Arrays.asList("4", "11"));
////                break;
//                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"));//部门
//                count = personalCount2 + deptCount2;
//                break;
//            case "dept_teacher":
//                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount1 = countDeptApprovals(user.getDeptId(), Arrays.asList("2", "1451"));//部门
//                count = personalCount1 + deptCount1;
//                break;
//            case "research":
//                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));//个人
//                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "4656"));//部门
//                count = personalCount + deptCount;
//                break;
//            default:
//                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                break;
//        }
//        return count;
//    }
//    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
//        List<String> states;
//        int count = 0;
//
//        // 根据角色获取状态列表
//        switch (roleKey) {
//            case "sci_tesearch":
//                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"), roleKey);
//                count = personalCount2 + deptCount2;
//                break;
//            case "dept_teacher":
//                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount1 = countDeptApprovals(user.getDeptId(), Arrays.asList("2", "1451"), roleKey);
//                count = personalCount1 + deptCount1;
//                break;
//            case "research":
//                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "4656"), roleKey);
//                count = personalCount + deptCount;
//                break;
//            default:
//                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5"));
//        }
//        return count;
//    }
    private int calculateHorizontalTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
//                int deptCount2 = countDeptApprovals(user.getDeptId(), Arrays.asList("11", "4568"), roleKey);
                // 部门待办：所有状态为11的项目（学院通过后提交到科研处）
//                int deptCount2 = sciHorizontalApplyMapper.countByState("11", getCurrentYear());
                int deptCount2 = sciHorizontalApplyMapper.countByStates(Arrays.asList("11", "33"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                // 个人待办：教师自己提交的项目
                int personalCount1 = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
                // 部门待办：包含所有子教研室的状态为2的项目
                int deptCount1 = countDeptApprovals(collegeDeptId, Arrays.asList("2", "8"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
                int deptCount = countDeptApprovals(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalTodos(user.getUserId(), Arrays.asList("99", "3", "22", "5", "4", "9", "44", "10"));
        }
        return count;
    }

    // 优化后的部门审批统计方法
    private int countDeptApprovals(Long deptId, List<String> states, String roleKey) {
        // 学院和科研处角色需要递归查询子部门
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        }
        // 教研室只需查询本部门
        else {
            return sciHorizontalApplyMapper.countByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    // 判断学院级角色
    private boolean isCollegeLevelRole(String roleKey) {
        return "dept_teacher".equals(roleKey) || "sci_tesearch".equals(roleKey);
    }



    //8.31纵向课题待办
    // 添加纵向课题统计方法
    private int calculateVerticalTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount2 = sciHorizontalApplyMapper.countVerticalByStates(Arrays.asList("4", "44"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount1 = countVerticalByDeptAndStates(collegeDeptId, Arrays.asList("2", "22"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
                int deptCount = countVerticalByDeptAndStates(user.getDeptId(), Arrays.asList("1", "11"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalVerticalTodos(user.getUserId(), Arrays.asList("99", "3", "5", "7", "6", "33", "55", "77"));
        }
        return count;
    }

    // 添加纵向课题的个人待办统计
    private int countPersonalVerticalTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countVerticalByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加纵向课题的部门统计
    private int countVerticalByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countVerticalByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countVerticalByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //8.31论文待办
    // 添加论文待办统计方法
    private int calculatePaperTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount2 = sciHorizontalApplyMapper.countPaperByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount1 = countPaperByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
                int deptCount = countPaperByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalPaperTodos(user.getUserId(), Arrays.asList("99", "3", "5", "0"));
        }
        return count;
    }

    // 添加论文的个人待办统计
    private int countPersonalPaperTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countPaperByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加论文的部门统计
    private int countPaperByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countPaperByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countPaperByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.6成果转化待办
    // 添加成果转化待办统计方法
    private int calculateAchievementTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount2 = sciHorizontalApplyMapper.countAchievementByStates(Arrays.asList("2", "8"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount1 = countAchievementByDeptAndStates(collegeDeptId, Arrays.asList("11", "13"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
                int deptCount = countAchievementByDeptAndStates(user.getDeptId(), Arrays.asList("1", "7"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalAchievementTodos(user.getUserId(), Arrays.asList("15", "3", "12", "5", "4", "9", "14", "10"));
        }
        return count;
    }

    // 添加成果转化的个人待办统计
    private int countPersonalAchievementTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countAchievementByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加成果转化的部门统计
    private int countAchievementByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countAchievementByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countAchievementByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15教材软著
    // 添加教材软著待办统计方法
    private int calculateTextbookTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countTextbookByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount1 = countTextbookByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount = countTextbookByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalTextbookTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
        }
        return count;
    }

    // 添加教材软著的个人待办统计
    private int countPersonalTextbookTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countTextbookByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加教材软著的部门统计
    private int countTextbookByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countTextbookByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countTextbookByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15专利软著
    // 添加专利软著待办统计方法
    private int calculatePatentTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countPatentByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount1 = countPatentByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
                int deptCount = countPatentByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalPatentTodos(user.getUserId(), Arrays.asList("0", "3", "5", "7"));
        }
        return count;
    }

    // 添加专利软著的个人待办统计
    private int countPersonalPatentTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countPatentByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加专利软著的部门统计
    private int countPatentByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countPatentByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countPatentByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.15 奖励
    // 添加奖励待办统计方法
    private int calculateRewardTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount2 = sciHorizontalApplyMapper.countRewardByStates(Arrays.asList("4"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount1 = countRewardByDeptAndStates(collegeDeptId, Arrays.asList("2"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
                int deptCount = countRewardByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalRewardTodos(user.getUserId(), Arrays.asList("11", "3", "5", "7"));
        }
        return count;
    }

    // 添加奖励的个人待办统计
    private int countPersonalRewardTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countRewardByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加奖励的部门统计
    private int countRewardByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countRewardByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countRewardByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    //9.14 讲座报告
    // 添加讲座报告待办统计方法
    private int calculateLectureTodoCount(SysUser user, String roleKey) {
        int count = 0;
        switch (roleKey) {
            case "sci_tesearch": // 科研处
                int personalCount2 = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount2 = sciHorizontalApplyMapper.countLectureByStates(Arrays.asList("2"), getCurrentYear());
                count = personalCount2 + deptCount2;
                break;
            case "dept_teacher": // 学院
                Long collegeDeptId = user.getDept().getParentId();
                int personalCount1 = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount1 = countLectureByDeptAndStates(collegeDeptId, Arrays.asList("6"), roleKey);
                count = personalCount1 + deptCount1;
                break;
            case "research": // 教研室
                int personalCount = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
                int deptCount = countLectureByDeptAndStates(user.getDeptId(), Arrays.asList("1"), roleKey);
                count = personalCount + deptCount;
                break;
            default: // 普通教师
                count = countPersonalLectureTodos(user.getUserId(), Arrays.asList("0", "3", "7", "5"));
        }
        return count;
    }

    // 添加讲座报告的个人待办统计
    private int countPersonalLectureTodos(Long userId, List<String> states) {
        return sciHorizontalApplyMapper.countLectureByUserAndStates(userId, states, getCurrentYear());
    }

    // 添加讲座报告的部门统计
    private int countLectureByDeptAndStates(Long deptId, List<String> states, String roleKey) {
        if (isCollegeLevelRole(roleKey)) {
            return sciHorizontalApplyMapper.countLectureByDeptAndStatesWithChildren(deptId, states, getCurrentYear());
        } else {
            return sciHorizontalApplyMapper.countLectureByDeptAndStates(deptId, states, getCurrentYear());
        }
    }

    @Override
    public int countVerticalApply(SysUser user) {
        String mainRole = determineMainRole(user.getRoles());
        log.info("纵向课题申报统计 - 用户ID: {}, 角色: {}", user.getUserId(), mainRole);
        int count = calculateVerticalTodoCount(user, mainRole);
        log.info("纵向课题申报统计结果: {}", count);
        return count;
    }

    @Override
    public int countVerticalAudit(SysUser user) {
        log.info("纵向课题审核统计 - 用户ID: {}", user.getUserId());
        // 审核中的状态列表
        List<String> auditStates = Arrays.asList("1", "2", "4", "6", "11", "22", "44");

        // 仅查询用户自己的数据
        int count = countPersonalVerticalTodos(user.getUserId(), auditStates);
        log.info("用户审核统计: {}", count);

        return count;
    }

    @Override
    public int countVerticalComplete(SysUser user) {
        // 完成状态
        List<String> completeStates = Arrays.asList("66");
        int count = countPersonalVerticalTodos(user.getUserId(), completeStates);
        log.info("纵向课题完成统计 - 用户ID: {}, 结果: {}", user.getUserId(), count);
        return count;
    }

    //****************************************************************************************
    // 横向课题统计实现
    @Override
    public int countHorizontalApply(SysUser user) {
        return sciHorizontalApplyMapper.countHorizontalApplyByUser(user.getUserId());
    }

    @Override
    public int countHorizontalAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "11", "4", "7", "8", "33");
        return sciHorizontalApplyMapper.countHorizontalApplyByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countHorizontalComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciHorizontalApplyMapper.countHorizontalApplyByUserAndStates1(user.getUserId(), completeStates);
    }

    // 成果转化统计实现
    @Override
    public int countAchievementApply(SysUser user) {
        return sciIntraschproApplyMapper.countIntraschproApplyByUser(user.getUserId());
    }

    @Override
    public int countAchievementAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "11", "2", "4", "7", "13", "8");
        return sciIntraschproApplyMapper.countIntraschproApplyByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countAchievementComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciIntraschproApplyMapper.countIntraschproApplyByUserAndStates1(user.getUserId(), completeStates);
    }

    // 论文统计实现
    @Override
    public int countPaperApply(SysUser user) {
        return sciPaperAMapper.countPaperAByUser(user.getUserId());
    }

    @Override
    public int countPaperAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciPaperAMapper.countPaperAByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countPaperComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("8");
        return sciPaperAMapper.countPaperAByUserAndStates1(user.getUserId(), completeStates);
    }

    // 教材软著统计实现
    @Override
    public int countTextbookApply(SysUser user) {
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUser(user.getUserId());
    }

    @Override
    public int countTextbookAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countTextbookComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciJiaocairuanzhuMapper.countJiaocairuanzhuByUserAndStates1(user.getUserId(), completeStates);
    }

    // 专利软著统计实现
    @Override
    public int countPatentApply(SysUser user) {
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUser(user.getUserId());
    }

    @Override
    public int countPatentAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countPatentComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sciZhuanliruanzhuMapper.countZhuanliruanzhuByUserAndStates1(user.getUserId(), completeStates);
    }

    // 奖励统计实现
    @Override
    public int countRewardApply(SysUser user) {
        return sysRewardMapper.countRewardByUser(user.getUserId());
    }

    @Override
    public int countRewardAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "2", "4");
        return sysRewardMapper.countRewardByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countRewardComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("6");
        return sysRewardMapper.countRewardByUserAndStates1(user.getUserId(), completeStates);
    }

    // 讲座报告统计实现
    @Override
    public int countLectureApply(SysUser user) {
        return sciLectureReportMapper.countLectureReportByUser(user.getUserId());
    }

    @Override
    public int countLectureAudit(SysUser user) {
        List<String> auditStates = Arrays.asList("1", "6", "2");
        return sciLectureReportMapper.countLectureReportByUserAndStates1(user.getUserId(), auditStates);
    }

    @Override
    public int countLectureComplete(SysUser user) {
        List<String> completeStates = Arrays.asList("4");
        return sciLectureReportMapper.countLectureReportByUserAndStates1(user.getUserId(), completeStates);
    }
    /**
     * 提交课题
     *
     * @return 结果
     */

    @Override
    public List<SciHorizontalApply> getStatsQuery(Map<String, String> params) {
        return sciHorizontalApplyMapper.getStatsQuery(params);
    }

    /**
     * 删除被驳回的到账金额
     * */
    @Override
    public int removeAmount(String id, String reid, Long userId, String remark, String urlFlag) {
        return sciHorizontalReamountMapper.reamountremove(Integer.valueOf(reid));
    }
}
