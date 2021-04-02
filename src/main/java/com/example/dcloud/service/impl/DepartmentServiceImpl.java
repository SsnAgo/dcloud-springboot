package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.SchoolMapper;
import com.example.dcloud.pojo.Department;
import com.example.dcloud.mapper.DepartmentMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-29
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public RespBean addDepartment(Department department) {
        // 如果一个学校下面添加两个相同的学院肯定是不行的
        // 判断department表中有没有其他的schoolId相同的并且name也相同的
        Department exist = departmentMapper.selectOne(new QueryWrapper<Department>().eq("schoolId", department.getSchoolId()).eq("name", department.getName()));
        if (null != exist){
            return RespBean.error("同一个学校下面不能有重名的学院");
        }
        if (departmentMapper.insert(department) == 1){
            return RespBean.success("新增学院成功");
        }
        return RespBean.error("新增学院失败");
    }

    @Override
    public RespBean updateDepartment(Department department) {
        Department exist = departmentMapper.selectOne(new QueryWrapper<Department>().eq("schoolId", department.getSchoolId()).eq("name", department.getName()).ne("id",department.getId()));
        if (null != exist){
            return RespBean.error("同一个学校下面不能有重名的学院");
        }
        if (departmentMapper.insert(department) == 1){
            return RespBean.success("修改学院成功");
        }
        return RespBean.error("修改学院失败");
    }
}
