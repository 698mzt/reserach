package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据同步 Mapper 接口
 */
@Mapper
public interface SynchronizeDataMapper {
    /**
     * 同步用户数据
     * 对应 XML 中 id="SynchronizeUser" 的 insert 语句
     *
     * @return 更新记录数
     */
    int SynchronizeUser();
    int SynchronizeUserForScore();
    /**
     * 删除不存在的用户数据
     * 对应 XML 中 id="SynchronizeDeleteUser" 的 delete语句
     *
     * @return 更新记录数
     */
    int SynchronizeDeleteUser();
    int SynchronizeDeleteUserForScore();



    /**
     * 同步横向课题个数数据
     * 对应 XML 中 id="SynchronizeHX" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeHX();
    /**
     * 同步横向课题总分数据
     * 对应 XML 中 id="SynchronizeScoreHX" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeScoreHX();



    /**
     * 同步纵向课题数据
     * 对应 XML 中 id="SynchronizeZX" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeZX();
    /**
     * 同步纵向课题校级以上总分数据
     * 对应 XML 中 id="SynchronizeScoreZXXJYS" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeScoreZXXJYS();
    /**
     * 同步纵向课题校级总分数据
     * 对应 XML 中 id="SynchronizeScoreZXXJ" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeScoreZXXJ();



    /**
     * 同步专利软著数据
     */
    int synchronizeZLRZ();
    /**
     * 同步教材软著积分
     */
    int SynchronizeScoreZLRZ();


    /**
     * 同步教材软著个数
     */
    int synchronizeJCRZ();
    /**
     * 同步教材软著积分
     */
    int SynchronizeScoreJCZZ();



    /**
     * 同步奖励个数
     */
    int Reward();
    /**
     * 同步奖励积分
     */
    int SynchronizeScoreJL();


    /**
     * 同步论文个数
     */
    int syncronzedFlow();
    /**
     * 同步论文积分
     */
    int SynchronizeScoreXSLW();



    /**
     * 同步讲座报告数据
     * 对应 XML 中 id="SynchronizeReport" 的 delete语句
     *
     * @return 更新记录数
     */
    int SynchronizeReport();
    /**
     * 同步讲座报告积分
     */
    int SynchronizeScoreJZBG();
}