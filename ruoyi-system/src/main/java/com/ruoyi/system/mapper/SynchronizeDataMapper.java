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

}