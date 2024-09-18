package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EduTeaCourse;
import com.ruoyi.system.domain.SysUserRole;

/**
 * InnoDB free: 4096 kBMapper接口
 * 
 * @author ruoyi
 * @date 2024-08-14
 */
public interface EduTeaCourseMapper 
{
    /**
     * 查询InnoDB free: 4096 kB
     * 
     * @param tid InnoDB free: 4096 kB主键
     * @return InnoDB free: 4096 kB
     */
    public List<EduTeaCourse> selectEduTeaCourseByTid(Integer tid);

    /**
     * 查询InnoDB free: 4096 kB列表
     * 
     * @param eduTeaCourse InnoDB free: 4096 kB
     * @return InnoDB free: 4096 kB集合
     */
    public List<EduTeaCourse> selectEduTeaCourseList(EduTeaCourse eduTeaCourse);

    /**
     * 新增InnoDB free: 4096 kB
     * 
     * @param eduTeaCourse InnoDB free: 4096 kB
     * @return 结果
     */
    public int insertEduTeaCourse(EduTeaCourse eduTeaCourse);

    /**
     * 修改InnoDB free: 4096 kB
     * 
     * @param eduTeaCourse InnoDB free: 4096 kB
     * @return 结果
     */
    public int updateEduTeaCourse(EduTeaCourse eduTeaCourse);

    /**
     * 删除InnoDB free: 4096 kB
     * 
     * @param tid InnoDB free: 4096 kB主键
     * @return 结果
     */
    public int deleteEduTeaCourseByTid(Integer tid);

    /**
     * 批量删除InnoDB free: 4096 kB
     * 
     * @param tids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduTeaCourseByTids(String[] tids);

    public int batchTeaCoures( List<EduTeaCourse> eduTeaCourse);
}
