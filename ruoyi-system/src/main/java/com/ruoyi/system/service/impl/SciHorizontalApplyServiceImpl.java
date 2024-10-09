package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
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
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectSciHorizontalApplyList(SciHorizontalApply sciHorizontalApply)
    {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyList(sciHorizontalApply);
    }
    @Override
    public List<SciHorizontalApply> selectSciHorizontalApplyListByKYC(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalApplyMapper.selectSciHorizontalApplyListByKYC(sciHorizontalApply);
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
        return sciHorizontalApplyMapper.insertSciHorizontalApply(sciHorizontalApply);
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
        return sciHorizontalApplyMapper.updateSciHorizontalApply(sciHorizontalApply);
    }

    /**
     * 批量删除横向课题
     * 
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
        sciHorizontalPiyue.setState("教研室驳回");
        sciHorizontalPiyueMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }


}
