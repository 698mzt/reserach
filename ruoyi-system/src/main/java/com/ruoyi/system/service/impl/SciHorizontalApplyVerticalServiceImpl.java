package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.mapper.SciHorizontalApplyVerticalMapper;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向课题Service业务层处理
 *
 */
@Service
public class SciHorizontalApplyVerticalServiceImpl implements ISciHorizontalApplyVerticalService {

    @Autowired
    private SciHorizontalApplyVerticalMapper sciHorizontalApplyVerticalMapper;

    /**
     * 查询纵向课题列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 纵向课题
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalList(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalList(sciHorizontalApplyVertical);
    }

    /**
     * 保存立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    public int insertSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.insertSciHorizontalApplyVertical(sciHorizontalApplyVertical);
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

    /**
     * 保存修改立项申请
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    public int updateSciHorizontalApplyVertical(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
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
    public int applyPass(String id, Long userId, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="2";
        }else if(urlFlag.equals("KYC")){
            state ="4";
        }
        int a =  sciHorizontalApplyVerticalMapper.applyPass(id,state);
        return a;
    }

    @Override
    public int applyBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="3";
        }else if(urlFlag.equals("KYC")){
            state ="5";
        }
        int a =  sciHorizontalApplyVerticalMapper.applyPass(id,state);
        return a;
    }

    @Override
    public int openPass(String id, Long userId, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="22";
        }else if(urlFlag.equals("KYC")){
            state ="44";
        }
        int a =  sciHorizontalApplyVerticalMapper.openPass(id,state);
        return a;
    }

    @Override
    public int openBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="33";
        }else if(urlFlag.equals("KYC")){
            state ="55";
        }
        int a =  sciHorizontalApplyVerticalMapper.openPass(id,state);
        return a;
    }

    @Override
    public int midPass(String id, Long userId, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="222";
        }else if(urlFlag.equals("KYC")){
            state ="444";
        }
        int a =  sciHorizontalApplyVerticalMapper.midPass(id,state);
        return a;
    }

    @Override
    public int midBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="333";
        }else if(urlFlag.equals("KYC")){
            state ="555";
        }
        int a =  sciHorizontalApplyVerticalMapper.midPass(id,state);
        return a;
    }

    @Override
    public int overPass(String id, Long userId, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="2222";
        }else if(urlFlag.equals("KYC")){
            state ="4444";
        }
        int a =  sciHorizontalApplyVerticalMapper.overPass(id,state);
        return a;
    }

    @Override
    public int overBh(String id, Long userId, String remark, String urlFlag) {
        String state = "0";
        if(urlFlag.equals("JYS")){
            state ="3333";
        }else if(urlFlag.equals("KYC")){
            state ="5555";
        }
        int a =  sciHorizontalApplyVerticalMapper.overPass(id,state);
        return a;
    }

    /**
     * 查询开题申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListKT(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        if (sciHorizontalApplyVertical.getRole().equals("research"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJYSKT(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("sci_tesearch"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKYCKT(sciHorizontalApplyVertical);
        else
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKT(sciHorizontalApplyVertical);
        return list;
    }

    /**
     * 查询中期申请列表
     *
     * @param sciHorizontalApplyVertical 纵向课题
     * @return 结果
     */
    @Override
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListZQ(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        if (sciHorizontalApplyVertical.getRole().equals("research"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJYSZQ(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("sci_tesearch"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKYCZQ(sciHorizontalApplyVertical);
        else
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListZQ(sciHorizontalApplyVertical);
        return list;
    }

    @Override
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListJX(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        List<SciHorizontalApplyVertical> list = new ArrayList<>();
        if (sciHorizontalApplyVertical.getRole().equals("research"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJYSJX(sciHorizontalApplyVertical);
        else if (sciHorizontalApplyVertical.getRole().equals("sci_tesearch"))
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListKYCJX(sciHorizontalApplyVertical);
        else
            list = sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListJX(sciHorizontalApplyVertical);
        return list;
    }

    @Override
    public List<SciHorizontalApplyVertical> selectSciHorizontalApplyVerticalListOVER(SciHorizontalApplyVertical sciHorizontalApplyVertical) {
        return sciHorizontalApplyVerticalMapper.selectSciHorizontalApplyVerticalListOVER(sciHorizontalApplyVertical);

    }


}
