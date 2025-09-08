package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhuPiyue;
import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciZhuanliruanzhuPiyueMapper;
import com.ruoyi.system.mapper.SciZhuanliruanzhuScoreCfgMapper;
import com.ruoyi.system.service.ISysUserService;
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


    @Autowired
    private SciZhuanliruanzhuScoreCfgMapper sciZhuanliruanzhuScoreCfgMapper;


//    @Autowired
//    private SciZhuanliruanzhuMapper sciZhuanliruanzhuMapper;

    @Autowired
    private ISysUserService userService;


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
        int a = sciZhuanliruanzhuMapper.insertSciZhuanliruanzhu(sciZhuanliruanzhu);
        int id = sciZhuanliruanzhu.getId();
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(Long.valueOf(sciZhuanliruanzhu.getUserId()));
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate("新增");
        sciZhuanliruanzhuPiyue.setState("新增");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
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
        int a = sciZhuanliruanzhuMapper.updateSciZhuanliruanzhu(sciZhuanliruanzhu);
        int id = sciZhuanliruanzhu.getId();
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(Long.valueOf(sciZhuanliruanzhu.getUserId()));
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate("修改");
        sciZhuanliruanzhuPiyue.setState("修改");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
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
    public int updateJifen(Long id, int jifen) {
        return sciZhuanliruanzhuMapper.updateJifen(id, jifen);
    }


    @Override

        public int hxPass(String id,Long uid,String urlFlag) {
        String state = "8";
//        SciZhuanliruanzhu sciZhuanliruanzhu = new SciZhuanliruanzhu();

        if(urlFlag.equals("hecha")){
            state ="4";
        }
        else if(urlFlag.equals("tijiao")){
            state ="1";

        }else if(urlFlag.equals("pro")){
            state ="2";
        }
        else if(urlFlag.equals("chayue")) {
            state = "6";
            SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
            String a = sciZhuanliruanzhu.getFenlei();
            String b = sciZhuanliruanzhu.getPaiming();
            SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg = new SciZhuanliruanzhuScoreCfg();
            sciZhuanliruanzhuScoreCfg.setFenLei(a);
            sciZhuanliruanzhuScoreCfg.setPaiMing(b);
            List<SciZhuanliruanzhuScoreCfg> c = sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);

            int jifen = 0;
            for (SciZhuanliruanzhuScoreCfg cfg : c) {
                jifen = Integer.parseInt(cfg.getTotalScore());
                System.out.println("Jifen: " + jifen);
            }
            sciZhuanliruanzhuMapper.updateJifen(Long.valueOf(id), jifen);


        }

        int a =  sciZhuanliruanzhuMapper.hxPass(id,state);

        if (urlFlag.equals("tijiao")){
            SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
            sciZhuanliruanzhuPiyue.setUid(uid);
            sciZhuanliruanzhuPiyue.setHxktId(Integer.valueOf(id));
            sciZhuanliruanzhuPiyue.setConcate("提交申请");
            sciZhuanliruanzhuPiyue.setState("提交");
            sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        }
        else if (urlFlag.equals("pro") || urlFlag.equals("hecha") || urlFlag.equals("chayue")) {

            SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
            sciZhuanliruanzhuPiyue.setUid(uid);
            sciZhuanliruanzhuPiyue.setHxktId(Integer.valueOf(id));
            sciZhuanliruanzhuPiyue.setConcate("同意");
            sciZhuanliruanzhuPiyue.setState("通过");
            sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);}
        return a;
    }


    @Override
    public int hxBh(String id,Long uid, String remark,String urlFlag) {
        String state = "8";

        if(urlFlag.equals("hecha")){
            state ="5";
        }
       else if(urlFlag.equals("pro")){
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
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList31(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList31(sciZhuanliruanzhu);
    }
    @Override
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList21(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList21(sciZhuanliruanzhu);
    }

    @Override
    public int recall(Integer id, String state,Long uid, String remark, String urlFlag) {
        String newState = state;
        switch (state){
//            教研室
            case "2":
                newState = "1";
                break;
            //            学院
            case "4":
                newState = "2";
                break;
            //            科研处
            case "6":
                newState = "4";
                sciZhuanliruanzhuMapper.updateJifen(Long.valueOf(id),0);
                break;
        }


//        设置状态
        int a =sciZhuanliruanzhuMapper.hxPass(id.toString(),newState);
//        插入日志
        SciZhuanliruanzhuPiyue sciZhuanliruanzhuPiyue = new SciZhuanliruanzhuPiyue();
        sciZhuanliruanzhuPiyue.setUid(uid);
        sciZhuanliruanzhuPiyue.setHxktId(id);
        sciZhuanliruanzhuPiyue.setConcate(remark);
        sciZhuanliruanzhuPiyue.setState("撤回");
        sciZhuanliruanzhuPiyueMapper.insertSciZhuanliruanzhuPiyue(sciZhuanliruanzhuPiyue);
        return a;
    }



    @Override
    public boolean checkExist(String mingcheng, String paiming) {
        return sciZhuanliruanzhuMapper.checkExist(mingcheng, paiming) > 0;
    }


}
