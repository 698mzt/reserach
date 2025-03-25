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
    public boolean judge(Long deptId, String loginName) {
       String deptName = collegeProjectMapper.getDeptName(deptId);
        if (loginName.equals("admin")) {
            return true;
        }else
        if (deptName != null && deptName.toLowerCase().contains("学院")) {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> selAll(Long deptId) {
        return collegeProjectMapper.selAll(deptId);
    }
}
