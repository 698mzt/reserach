package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SciHorizontalPersion;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciHorizontalApplyMapper;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.common.core.text.Convert;

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
    public int insertSciHorizontalApply(SciHorizontalApply sciHorizontalApply)
    {
        sciHorizontalApplyMapper.insertSciHorizontalApply(sciHorizontalApply);
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
        return '1';
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
        sciHorizontalApplyMapper.deletePersion(sciHorizontalPersion);
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
     * @param id 横向课题主键
     * @return 结果
     */
    @Override
    public int deleteSciHorizontalApplyById(Integer id)
    {
        return sciHorizontalApplyMapper.deleteSciHorizontalApplyById(id);
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
    public int hxover(String id,Long uid,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYSOVER")){
            state ="8";
        }else if(urlFlag.equals("KYCOVER")){
            state ="6";
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
        }
        int a = sciHorizontalApplyMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
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
                    list = list = sciHorizontalApplyMapper.selectOtherListByUid(sciHorizontalApply);
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
        return sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);
    }

    @Override
    public List<SciHorizontalApply> exportSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.exportSciHorizontalApplyList(sciHorizontalApply);
    }

}
