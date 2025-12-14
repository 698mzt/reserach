package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SciHorizontalApplyMapper;
import com.ruoyi.system.mapper.SciHorizontalPiyueMapper;
import com.ruoyi.system.mapper.SciHorizontalReamountMapper;
import com.ruoyi.system.service.SciHorizontalReamountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SciHorizontalReamountServiceImpl implements SciHorizontalReamountService {

    @Autowired
    private SciHorizontalReamountMapper sciHorizontalReamountMapper;
    @Autowired
    private SciHorizontalApplyMapper sciHorizontalApplyMapper;
    @Autowired
    private SciHorizontalPiyueMapper sciHorizontalPiyueMapper;

    @Override
    public int insertAmount(SciHorizontalReamount sciHorizontalReamount) {
        try {
            // 横向课题的id
            Integer id = Integer.valueOf(sciHorizontalReamount.getApplyId());

            // 获取总共金额
            SciHorizontalApply apply = sciHorizontalApplyMapper.selectSciHorizontalApplyById(id);
            if (apply == null) {
                throw new IllegalArgumentException("未找到对应课题信息");
            }

            String allAmountStr = apply.getAmount();
            if (allAmountStr == null || allAmountStr.isEmpty()) {
                throw new IllegalArgumentException("课题总金额为空");
            }

            // 获取当前追加金额
            String newAmountStr = sciHorizontalReamount.getReAmount();
            if (newAmountStr == null || newAmountStr.isEmpty()) {
                throw new IllegalArgumentException("追加金额为空");
            }

            // 使用BigDecimal进行精确计算
            BigDecimal allAmount = new BigDecimal(allAmountStr);
            BigDecimal newAmount = new BigDecimal(newAmountStr);

            // 验证金额有效性
            if (allAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return -1;
//                throw new IllegalArgumentException("课题总金额必须大于0");
            }

            if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return -2;
//                throw new IllegalArgumentException("追加金额必须大于0");
            }

//            获取到账金额
            String creditedAmountValue = sciHorizontalApplyMapper.selectCreditedAmountById(id);
            BigDecimal creditedAmount = new BigDecimal(creditedAmountValue != null ? creditedAmountValue : "0");
            BigDecimal threshold = allAmount.multiply(BigDecimal.valueOf(1));

            // 新增金额大于总金额
//            if (newAmount.compareTo(threshold) > 0) {
//                return -1;
//            }
//
//            // 追加总金额超过项目金额
//            if (creditedAmount.add(newAmount).compareTo(threshold) > 0) {
//                return -2;
//            }

            int a = sciHorizontalReamountMapper.insertAmount(sciHorizontalReamount);
            SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
            sciHorizontalPiyue.setUid(sciHorizontalReamount.getUid());
            sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
            sciHorizontalPiyue.setConcate("新增金额");
            sciHorizontalPiyue.setState("新增");
            sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
            return a;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("金额格式不正确", e);
        }
    }


    @Override
    public int insertVerticalAmount(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.insertVerticalAmount(sciHorizontalReamount);
    }

    @Override
//    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectAmountList(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountList(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SciHorizontalApply> selectAmountListJYS(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListJYS(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectAmountListDept(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListDept(sciHorizontalApply);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SciHorizontalApply> selectAmountListKYC(SciHorizontalApply sciHorizontalApply) {
        return sciHorizontalReamountMapper.selectAmountListKYC(sciHorizontalApply);
    }

    @Override
    public SciHorizontalApply selectAmountById(Integer id) {
        return sciHorizontalReamountMapper.selectAmountById(id);
    }

    @Override
    public int amountpass(String id, String state) {
        return sciHorizontalReamountMapper.amountpass(id,state);
    }

    @Override
    public int amountedit(SciHorizontalReamount sciHorizontalReamount) {
        return sciHorizontalReamountMapper.amountedit(sciHorizontalReamount);
    }

    @Override
    public int reamountremove(Integer id) {
        return sciHorizontalReamountMapper.reamountremove(id);
    }

    @Override
    public List<SciHorizontalReamount> selectAmountListById(Integer id) {
        return sciHorizontalReamountMapper.selectAmountListById(id);
    }

    @Override
    public int push(Long uid,Integer id,String state) {
        int a = sciHorizontalReamountMapper.push(id,state);
        SciHorizontalPiyue sciHorizontalPiyue = new SciHorizontalPiyue();
        sciHorizontalPiyue.setUid(uid);
        sciHorizontalPiyue.setHxktId(Integer.valueOf(id));
        sciHorizontalPiyue.setConcate("提交审批");
        sciHorizontalPiyue.setState("提交");
        sciHorizontalPiyueMapper.insertHorizontalAmountPiyue(sciHorizontalPiyue);
        return a;
    }


}
