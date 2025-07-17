package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SciHorizontalPersion;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciUserScore;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciProjectScoreCfgMapper;
import com.ruoyi.system.mapper.SciUserScoreMapper;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciHorizontalApplyMapper;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 横向课题Service业务层处理
 *
 * @author zhansan
 * @date 2024-08-16
 */
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
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(Long.valueOf(sciHorizontalApply.getUserId()));
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
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
        // 更新：插入前查重
        SciHorizontalApply query = new SciHorizontalApply();
        query.setTopName(sciHorizontalApply.getTopName());
        query.setTopNumber(sciHorizontalApply.getTopNumber());
        List<SciHorizontalApply> existList = sciHorizontalApplyMapper.selectSciHorizontalApplyList(query);
        // 排除自己
        existList.removeIf(item -> item.getId().equals(query.getId()));
        if (existList != null && !existList.isEmpty()) {
            // 课题名称或编号已存在，返回-1
            return -1;
        }
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

}
