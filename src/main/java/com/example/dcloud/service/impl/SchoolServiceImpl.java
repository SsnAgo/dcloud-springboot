package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.DepartmentMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.School;
import com.example.dcloud.mapper.SchoolMapper;
import com.example.dcloud.service.ISchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<School> getSchools() {
        return schoolMapper.getSchools();
    }

    @Override
    @Transactional
    public RespBean addSchool(School school) {
        // 先判断学校编号或者学校名字有没有重复的
        School exist = schoolMapper.selectOne(new QueryWrapper<School>().eq("name", school.getName()));
        if (exist != null) {
            return RespBean.error("该学校已存在");
        }
        exist = schoolMapper.selectOne(new QueryWrapper<School>().eq("schoolCode", school.getSchoolCode()));
        if (exist != null) {
            return RespBean.error("该学校编号已存在");
        }
        if (schoolMapper.insert(school) == 1) {
            return RespBean.success("添加学校成功",school);
        }
        return RespBean.error("添加学校失败");
    }

    @Override
    public RespBean deleteSchool(Integer sid) {
        // 查询department表，如果有schoolid为sid的  就不能删除
        //departmentMapper.selectCount();
        return null;
    }
}
