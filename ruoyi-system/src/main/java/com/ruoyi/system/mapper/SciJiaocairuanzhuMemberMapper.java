package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SciJiaocairuanzhuMember;

import java.util.List;

/**
 * 教材著作成员Mapper接口
 * 
 * @author yzk
 * @date 2026-01-19
 */

public interface SciJiaocairuanzhuMemberMapper {
    /**
     * 新增教材著作成员
     * 
     * @param sciJiaocairuanzhuMember 教材著作成员
     * @return 结果
     */
    int insertSciJiaocairuanzhuMember(SciJiaocairuanzhuMember sciJiaocairuanzhuMember);
    
    /**
     * 批量删除教材著作成员
     * 
     * @param jiaocaiId 教材著作ID
     * @return 结果
     */
    int deleteSciJiaocairuanzhuMemberByJiaocaiId(Integer jiaocaiId);
    
    /**
     * 查询教材著作成员列表
     * 
     * @param jiaocaiId 教材著作ID
     * @return 教材著作成员列表
     */
    List<SciJiaocairuanzhuMember> selectSciJiaocairuanzhuMemberByJiaocaiId(Integer jiaocaiId);
}