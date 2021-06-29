package com.example.dcloud.service.impl;

import com.example.dcloud.pojo.Role;
import com.example.dcloud.mapper.RoleMapper;
import com.example.dcloud.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoles(String search) {
        return roleMapper.getRoles(search);
    }
}
