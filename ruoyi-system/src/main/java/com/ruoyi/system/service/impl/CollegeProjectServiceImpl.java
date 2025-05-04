package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.CollegeProjectMapper;
import com.ruoyi.system.mapper.SciIntraSchProApplyMapper;
import com.ruoyi.system.service.ICollegeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CollegeProjectServiceImpl implements ICollegeProjectService {

    @Autowired
    private CollegeProjectMapper collegeProjectMapper;
    @Override
    public Integer judge(Long deptId, String loginName) {
       String deptName = collegeProjectMapper.getDeptName(deptId);
        System.out.println("deptName.toLowerCase()="+deptName.toLowerCase());
        if (loginName.equals("admin")) {
            return 2;
        }else if (deptName != null && deptName.toLowerCase().contains("学院")) {
            //虽然有些普通教师的部门也带有学院二字，但是他们看不到这个一个的目录
            //漏洞：如果这个老师他知道这个url，也是可以通过url获得学院才能看到的数据
            return 1;
        } else if (deptName.toLowerCase().contains("科研处")) {
            return 2;
        }
        return 3;
    }

    @Override
    public List<Map<String, Object>> selAll(Long deptId) {
        return collegeProjectMapper.selAll(deptId);
    }


}
