package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SciZhuanliruanzhuScoreCfg;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DataScopeUtils;
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
import org.springframework.transaction.annotation.Transactional;


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
    @DataScope(deptAlias = "d", userAlias = "u")
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
    @Transactional(rollbackFor = Exception.class)
    public int insertSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        //  设置默认状态为草稿箱
        sciZhuanliruanzhu.setState("0");

        // 计算预计积分（并保存到 expected_jifen）
        String jifen = calculateScore(sciZhuanliruanzhu.getFenlei(), sciZhuanliruanzhu.getPaiming());
        sciZhuanliruanzhu.setJifen(jifen); // 兼容旧逻辑：仍保留原 jifen 字段
        sciZhuanliruanzhu.setExpectedJifen(jifen);
        // 新增/编辑阶段最终积分未确认
        sciZhuanliruanzhu.setFinalJifen(null);

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
     * 根据分类和排名计算积分
     */
    private String calculateScore(String fenlei, String paiming) {
        if (fenlei == null || paiming == null) {
            return "0";
        }

        SciZhuanliruanzhuScoreCfg scoreCfg = new SciZhuanliruanzhuScoreCfg();
        scoreCfg.setFenLei(fenlei);
        scoreCfg.setPaiMing(paiming);

        List<SciZhuanliruanzhuScoreCfg> configList = sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgList(scoreCfg);

        if (configList != null && !configList.isEmpty()) {
            return configList.get(0).getTotalScore();
        }

        return "0";
    }

    /**
     * 根据分类计算预计科研分（仅排名1~4：主持人/成员1/成员2/成员3）
     *
     * @param fenlei 分类值（来自 sys_zhuanli_fenlei）
     * @return 包含 firstScore/secondScore/thirdScore/fourthScore 的Map
     */
    @Override
    public Map<String, String> calculateExpectedScores(String fenlei) {
        Map<String, String> res = new java.util.HashMap<>();
        res.put("firstScore", calculateScore(fenlei, "1"));
        res.put("secondScore", calculateScore(fenlei, "2"));
        res.put("thirdScore", calculateScore(fenlei, "3"));
        res.put("fourthScore", calculateScore(fenlei, "4"));
        return res;
    }

    @Override
    public String calculateScoreByFenleiAndRank(String fenlei, String paiming) {
        return calculateScore(fenlei, paiming);
    }

    /**
     * 修改专利软著
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSciZhuanliruanzhu(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        // 重新计算预计积分，并保存到 expected_jifen
        String jifen = calculateScore(sciZhuanliruanzhu.getFenlei(), sciZhuanliruanzhu.getPaiming());
        sciZhuanliruanzhu.setJifen(jifen); // 兼容旧逻辑
        sciZhuanliruanzhu.setExpectedJifen(jifen);
        // 编辑时清空最终积分（未最终确认则不显示）
        sciZhuanliruanzhu.setFinalJifen(null);

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
     * 批量删除专利
     *
     * @param ids 需要删除的专利软著主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public int deleteSciZhuanliruanzhuById(Integer id)
    {
        return sciZhuanliruanzhuMapper.deleteSciZhuanliruanzhuById(id);
    }

    /**
     * 更新专利软著积分
     *
     * @param id 专利软著主键
     * @param jifen 积分值
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJifen(Long id, int jifen) {
        return sciZhuanliruanzhuMapper.updateJifen(id, jifen);
    }

    /**
     * 专利软著审核通过
     *
     * @param id 专利软著主键
     * @param uid 用户ID
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
//            SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
//            String a = sciZhuanliruanzhu.getFenlei();
//            String b = sciZhuanliruanzhu.getPaiming();
//            SciZhuanliruanzhuScoreCfg sciZhuanliruanzhuScoreCfg = new SciZhuanliruanzhuScoreCfg();
//            sciZhuanliruanzhuScoreCfg.setFenLei(a);
//            sciZhuanliruanzhuScoreCfg.setPaiMing(b);
//            List<SciZhuanliruanzhuScoreCfg> c = sciZhuanliruanzhuScoreCfgMapper.selectSciZhuanliruanzhuScoreCfgList(sciZhuanliruanzhuScoreCfg);
//
//            int jifen = 0;
//            for (SciZhuanliruanzhuScoreCfg cfg : c) {
//                jifen = Integer.parseInt(cfg.getTotalScore());
//                System.out.println("Jifen: " + jifen);
//            }
//            sciZhuanliruanzhuMapper.updateJifen(Long.valueOf(id), jifen);
//

        }

        // 科研处通过（state=6）时：写入最终积分（final_jifen）
        if (urlFlag.equals("chayue")) {
            SciZhuanliruanzhu sci = sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuById(Integer.valueOf(id));
            String finalJifen;
            if (sci != null) {
                // 计算年度
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                // 统计年度内用户的软著数量
                int count = sciZhuanliruanzhuMapper.countSoftWorksByYear(Long.valueOf(sci.getUserId()), currentYear);
                // 计算基础分数
                String baseScore = calculateScore(sci.getFenlei(), sci.getPaiming());
                // 非转化软著按50%核算
                if ("N".equals(sci.getShifouyingyon())) {
                    double score = Double.parseDouble(baseScore) * 0.5;
                    baseScore = String.valueOf(Math.round(score));
                }
                // 年度内不超过5项
                if (count > 5) {
                    finalJifen = "0";
                } else {
                    finalJifen = baseScore;
                }
            } else {
                finalJifen = "0";
            }
            sciZhuanliruanzhuMapper.updateFinalJifen(id, finalJifen);
            // 科研处认定通过时间，用于科研统计年度（上年12/1～当年11/30）计算
            sciZhuanliruanzhuMapper.updateKyjcPassTime(id, new Date());
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

    /**
     * 专利软著审核驳回
     *
     * @param id 专利软著主键
     * @param uid 用户ID
     * @param remark 驳回意见
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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



    /**
     * 查询专利软著列表（科研处）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList4(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList4(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（学院）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList3(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList3(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教研室）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList2(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList2(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教师）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList1(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList1(sciZhuanliruanzhu);
    }

    /**
     * 查询专利软著列表（学院导出）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList31(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList31(sciZhuanliruanzhu);
    }
    /**
     * 查询专利软著列表（教研室导出）
     *
     * @param sciZhuanliruanzhu 专利软著
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciZhuanliruanzhu> selectSciZhuanliruanzhuList21(SciZhuanliruanzhu sciZhuanliruanzhu)
    {
        return sciZhuanliruanzhuMapper.selectSciZhuanliruanzhuList21(sciZhuanliruanzhu);
    }
    /**
     * 撤回专利软著
     *
     * @param id 专利软著主键
     * @param state 当前状态
     * @param uid 用户ID
     * @param remark 撤回原因
     * @param urlFlag 操作标识
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
                sciZhuanliruanzhuMapper.updateFinalJifen(id.toString(), null);
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


    /**
     * 检查专利软著是否存在
     *
     * @param mingcheng 专利名称
     * @param paiming 排名
     * @param userId 用户ID
     * @return 检查结果（0-可以添加，1-重复，2-超过限制）
     */
    @Override
    public int checkExist(String mingcheng, String paiming,Long userId) {
//        判断该专利名称的该负责人级别已存在，不可重复添加
        if (sciZhuanliruanzhuMapper.checkExist(mingcheng, paiming) > 0) {
            return 1;
//        校验教师的数据是否大于十条
        } else if (sciZhuanliruanzhuMapper.checkUserCount(userId) >= 10) {
            return 2;
        } else {
//            提示联系管理员解决
            return 0;
        }
    }

    /**
     * 统计查询专利软著列表
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQuery(Map<String, String> params) {
        return sciZhuanliruanzhuMapper.getStatsQuery(params);
    }

    /**
     * 统计查询专利软著列表（导出Excel）
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQueryToExcil(Map<String, String> params) {
        return sciZhuanliruanzhuMapper.getStatsQueryToExcil(params);
    }

    /**
     * 统计查询专利软著列表（审核）
     *
     * @param params 查询参数
     * @return 专利软著集合
     */
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciZhuanliruanzhu> getStatsQueryToCheck(Map<String, String> params) {
        // 手动处理数据权限，因为 @DataScope 只支持 BaseEntity 类型，而这里使用的是 Map
        DataScopeUtils.applyDataScopeToMap(params, "d", "u", "");
        return sciZhuanliruanzhuMapper.getStatsQueryToCheck(params);
    }




}
