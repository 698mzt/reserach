package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciZhuanliruanzhuMapper;
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.common.core.text.Convert;

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


    /**
     * 查询专利软著
     * 
     * @param id 专利软著主键
     * @return 专利软著
     */
    @Override
    public SciZhuanliruanzhu selectSciZhuanliruanzhuById(Integer id)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(id);
    }

    /**
     * 查询专利软著列表
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著
     */
    @Override
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
    public int insertSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.insertSciZhuanliruanzhu(sciZhuanliruanzhu);
    }

    /**
     * 修改专利软著
     * 
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    @Override
    public int updateSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.updateSciZhuanliruanzhu(sciZhuanliruanzhu);
    }

    /**
     * 批量删除专利软著
     * 
     * @param ids 需要删除的专利软著主键
     * @return 结果
     */
    @Override
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
    public int deleteSciZhuanliruanzhuById(Integer id)
    {
        return sciZhuanliruanzhuMapper.deleteSciZhuanliruanzhuById(id);
    }


    @Override
    public int hxPass(String id,Long uid,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="4";
        }else if(urlFlag.equals("pro")){
            state ="2";
        }

        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("同意");
        sciHorizontalPiyue.setState("通过");
//        sciZhuanliruanzhuMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
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
        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
        System.out.println(a);
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
        int a = sciZhuanliruanzhuMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
//        sciZhuanliruanzhuMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
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

        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate(remark);
        sciHorizontalPiyue.setState("被驳回");
//        sciZhuanliruanzhuMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
        return a;
    }


}
