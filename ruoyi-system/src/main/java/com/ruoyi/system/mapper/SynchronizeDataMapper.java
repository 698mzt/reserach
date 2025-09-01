package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据同步 Mapper 接口
 */
@Mapper
public interface SynchronizeDataMapper {

    /**
     * 同步横向课题数据
     * 对应 XML 中 id="SynchronizeHX" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeHX();

    /**
     * 同步纵向课题数据
     * 对应 XML 中 id="SynchronizeZX" 的 update 语句
     *
     * @return 更新记录数
     */
    int SynchronizeZX();

    /**
     * 同步用户数据
     * 对应 XML 中 id="SynchronizeUser" 的 insert 语句
     *
     * @return 更新记录数
     */
    int SynchronizeUser();

    /**
     * 删除不存在的用户数据
     * 对应 XML 中 id="SynchronizeDeleteUser" 的 delete语句
     *
     * @return 更新记录数
     */
    int SynchronizeDeleteUser();

    /**
     * 同步讲座报告数据
     * 对应 XML 中 id="SynchronizeReport" 的 delete语句
     *
     * @return 更新记录数
     */
    int SynchronizeReport();
}