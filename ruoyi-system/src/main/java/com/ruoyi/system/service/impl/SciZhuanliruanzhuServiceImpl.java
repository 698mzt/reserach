package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhuPiyue;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciZhuanliruanzhuPiyueMapper;
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

    @Autowired
    private SciZhuanliruanzhuPiyueMapper sciZhuanliruanzhuPiyueMapper;



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
//    public int hxPass(String id,Long uid,String urlFlag,List score,List persion,Integer applyId) {
        public int hxPass(String id,Long uid,String urlFlag) {
        String state = "0";
        SciZhuanliruanzhu sciZhuanliruanzhu = new SciZhuanliruanzhu();

        if(urlFlag.equals("hecha")){
            state ="4";
        }else if(urlFlag.equals("pro")){
            state ="2";
        }
        else if(urlFlag.equals("chayue")){
            state ="6";
//                        以负责人列表大小为准，顺序匹配每个负责人所对应的分数，记录到sciUserScore中。
//            for (int i = 0; i < persion.size(); i++) {
//                sciZhuanliruanzhu.setUserId(Integer.valueOf(persion.get(i).toString()));
//                sciZhuanliruanzhu.setChangeValue(score.get(i).toString());
//
//                sciZhuanliruanzhuMapper.insertScoreHistory(sciZhuanliruanzhu);
//            }


        }

        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(uid);
        sciZhuanliruanzhuPiyue.setHxktId(Integer.valueOf(id));
        sciZhuanliruanzhuPiyue.setConcate("同意");
        sciZhuanliruanzhuPiyue.setState("通过");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }
//    @Override
//    public int hxover(String id,Long uid,String urlFlag) {
//        String state = "0";
//        if(urlFlag.equals("JYSOVER")){
//            state ="8";
//        }else if(urlFlag.equals("KYCOVER")){
//            state ="6";
//        }
//        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
//        System.out.println(a);
//        return a;
//    }

    @Override
    public int hxBh(String id,Long uid, String remark,String urlFlag) {
        String state = "0";
        if(urlFlag.equals("hecha")){
            state ="5";
        }else if(urlFlag.equals("pro")){
            state ="3";
        }else if(urlFlag.equals("chayue")){
            state ="7";
        }
        int a = sciZhuanliruanzhuMapper.hxPass(id,state);
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(uid);
        sciZhuanliruanzhuPiyue.setHxktId(Integer.valueOf(id));
        sciZhuanliruanzhuPiyue.setConcate(remark);
        sciZhuanliruanzhuPiyue.setState("被驳回");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }

//    @Override
//    public int hxoverBh(String id, Long uid, String remark, String urlFlag) {
//        String state = "0";
//        if(urlFlag.equals("JYSOVER")){
//            state ="9";
//        }else if(urlFlag.equals("KYCOVER")){
//            state ="10";
//        }
//
//        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);
//        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
//        sciHorizontalPiyue.setUid(uid);
//        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
//        sciHorizontalPiyue.setConcate(remark);
//        sciHorizontalPiyue.setState("被驳回");
////        sciZhuanliruanzhuMapper.insertSciHorizontalPiyue(sciHorizontalPiyue);
//        return a;
//    }



//    @Override
//    public int collegeAudit(String id, Long userId, String urlFlag) {
//        // 实现学院审核人审核通过的逻辑
//        // ...
//        return result;
//    }
//
//    @Override
//    public int collegeBh(String id, Long userId, String remark, String urlFlag) {
//        // 实现学院审核人驳回的逻辑
//        // ...
//        return result;
//    }


    @Override
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList4(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList4(sciZhuanliruanzhu);
    }
    @Override
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList3(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList3(sciZhuanliruanzhu);
    }
    @Override
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList2(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList2(sciZhuanliruanzhu);
    }
    @Override
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList1(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
    }

    @Override
    public int recall(Integer id, String state,Long uid, String remark, String urlFlag) {
        String newState = state;
        switch (state){
//            教研室
            case "2":
                newState = "3";
                break;
            //            学院
            case "4":
                newState = "5";
                break;
            //            科研处
            case "6":
                newState = "7";
                break;
        }


//        设置状态
        int a =sciZhuanliruanzhuMapper.hxPass(id.toString(),newState);
//        插入日志
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(uid);
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate(remark);
        sciZhuanliruanzhuPiyue.setState("撤回上一条操作");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }

}
