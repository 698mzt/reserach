package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuPiyueMapper;
import com.ruoyi.system.mapper.SciJiaocairuanzhuScoreCfgMapper;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciJiaocairuanzhuMapper;
import com.ruoyi.system.service.ISciJiaocairuanzhuService;
import com.ruoyi.common.core.text.Convert;



/**
 * 教材软著Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-21
 */
@Service
public class SciJiaocairuanzhuServiceImpl implements ISciJiaocairuanzhuService
{
    @Autowired
    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @Autowired
    private SciJiaocairuanzhuPiyueMapper sciJiaocairuanzhuPiyueMapper;


    @Autowired
    private SciJiaocairuanzhuScoreCfgMapper sciJiaocairuanzhuScoreCfgMapper;


//    @Autowired
//    private SciJiaocairuanzhuMapper sciJiaocairuanzhuMapper;

    @Autowired
    private ISysUserService userService;


    /**
     * 查询教材软著
     *
     * @param id 教材软著主键
     * @return 教材软著
     */
    @Override
    public SciJiaocairuanzhu selectSciJiaocairuanzhuById(Integer id)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(id);
    }

    /**
     * 查询教材软著列表
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 教材软著
     */
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList(sciJiaocairuanzhu);
    }

    /**
     * 新增教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    @Override
    public int insertSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
//        try {
//            // 获取当前用户信息
//            SysUser currentUser = userService.selectUserById(getUserId());
//
//            // 获取学院、专业、教师信息
//            String collegeName = currentUser.getCollegeName();  // 假设用户实体中有学院名称
//            String majorName = currentUser.getMajorName();    // 假设用户实体中有专业名称
//            String teacherName = currentUser.getUserName();     // 教师名称
//
//            // 获取模块名称
//            String moduleName = sciJiaocairuanzhu.getModuleName();  // 假设 SciJiaocairuanzhu 实体中有模块名称
//
//            // 获取文件名
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = FilenameUtils.getExtension(originalFilename);
//            String fileName = FilenameUtils.getBaseName(originalFilename);
//
//            // 获取当前年份
//            String year = new SimpleDateFormat("yyyy").format(new Date());
//
//            // 构建文件路径
//            String filePath = collegeName + "/" + majorName + "/" + teacherName + "/" + moduleName + "/" + year + "-" + fileName + "-" + teacherName + "." + fileExtension;
//
//            // 构建文件保存路径
//            String savePath = "path/to/upload/directory/" + filePath;  // 替换为实际的上传目录
//
//            // 创建目录（如果不存在）
//            File directory = new File(savePath.substring(0, savePath.lastIndexOf("/")));
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            // 保存文件
//            File dest = new File(savePath);
//            file.transferTo(dest);
//
//            // 设置文件路径到 SciJiaocairuanzhu 对象
//            sciJiaocairuanzhu.setFilePath(filePath);
//
//            // 保存到数据库
//            sciJiaocairuanzhuMapper.insertSciJiaocairuanzhu(sciJiaocairuanzhu);
//
//            return AjaxResult.success("保存成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error("保存失败");
//        }
        return sciJiaocairuanzhuMapper.insertSciJiaocairuanzhu(sciJiaocairuanzhu);
    }

    /**
     * 修改教材软著
     *
     * @param sciJiaocairuanzhu 教材软著
     * @return 结果
     */
    @Override
    public int updateSciJiaocairuanzhu(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.updateSciJiaocairuanzhu(sciJiaocairuanzhu);
    }

    /**
     * 批量删除教材软著
     *
     * @param ids 需要删除的教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuByIds(String ids)
    {
        return sciJiaocairuanzhuMapper.deleteSciJiaocairuanzhuByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除教材软著信息
     *
     * @param id 教材软著主键
     * @return 结果
     */
    @Override
    public int deleteSciJiaocairuanzhuById(Integer id)
    {
        return sciJiaocairuanzhuMapper.deleteSciJiaocairuanzhuById(id);
    }


    @Override
    public int updateJifen(Long id, int jifen) {
        return sciJiaocairuanzhuMapper.updateJifen(id, jifen);
    }


    @Override

    public int hxPass(String id,Long uid,String urlFlag) {
        String state = "8";
//        SciJiaocairuanzhu sciJiaocairuanzhu = new SciJiaocairuanzhu();

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
            SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuById(Integer.valueOf(id));
            String a = sciJiaocairuanzhu.getFenlei();
            String b = sciJiaocairuanzhu.getPaiming();
            SciJiaocairuanzhuScoreCfg sciJiaocairuanzhuScoreCfg = new SciJiaocairuanzhuScoreCfg();
            sciJiaocairuanzhuScoreCfg.setFenLei(a);
            sciJiaocairuanzhuScoreCfg.setPaiMing(b);
            List<SciJiaocairuanzhuScoreCfg> c = sciJiaocairuanzhuScoreCfgMapper.selectSciJiaocairuanzhuScoreCfgList(sciJiaocairuanzhuScoreCfg);

            int jifen = 0;
            for (SciJiaocairuanzhuScoreCfg cfg : c) {
                jifen = Integer.parseInt(cfg.getTotalScore());
                System.out.println("Jifen: " + jifen);
            }
            sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id), jifen);


        }

        int a =  sciJiaocairuanzhuMapper.hxPass(id,state);

        if (urlFlag.equals("tijiao")){
            SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("提交申请");
            sciJiaocairuanzhuPiyue.setState("提交");
            sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        }
       else if (urlFlag.equals("pro") || urlFlag.equals("hecha") || urlFlag.equals("chayue")) {

            SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
            sciJiaocairuanzhuPiyue.setUid(uid);
            sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
            sciJiaocairuanzhuPiyue.setConcate("同意");
            sciJiaocairuanzhuPiyue.setState("通过");
            sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);}
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
        int a = sciJiaocairuanzhuMapper.hxPass(id,state);
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(Integer.valueOf(id));
        sciJiaocairuanzhuPiyue.setConcate(remark);
        sciJiaocairuanzhuPiyue.setState("被驳回");
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        return a;
    }




    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList4(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList4(sciJiaocairuanzhu);
    }
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList3(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList3(sciJiaocairuanzhu);
    }
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList2(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList2(sciJiaocairuanzhu);
    }
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList1(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList1(sciJiaocairuanzhu);
    }
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList31(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList31(sciJiaocairuanzhu);
    }
    @Override
    public List<SciJiaocairuanzhu> selectSciJiaocairuanzhuList21(SciJiaocairuanzhu sciJiaocairuanzhu)
    {
        return sciJiaocairuanzhuMapper.selectSciJiaocairuanzhuList21(sciJiaocairuanzhu);
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
                sciJiaocairuanzhuMapper.updateJifen(Long.valueOf(id),0);
                break;
        }


//        设置状态
        int a =sciJiaocairuanzhuMapper.hxPass(id.toString(),newState);
//        插入日志
        SciJiaocairuanzhuPiyue sciJiaocairuanzhuPiyue = new SciJiaocairuanzhuPiyue();
        sciJiaocairuanzhuPiyue.setUid(uid);
        sciJiaocairuanzhuPiyue.setJiaocai_id(id);
        sciJiaocairuanzhuPiyue.setConcate(remark);
        sciJiaocairuanzhuPiyue.setState("撤回上一条操作");
        sciJiaocairuanzhuPiyueMapper.insertSciJiaocairuanzhuPiyue(sciJiaocairuanzhuPiyue);
        return a;
    }

}
