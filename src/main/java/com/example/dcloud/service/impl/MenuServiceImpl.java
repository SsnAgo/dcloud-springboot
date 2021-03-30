package com.example.dcloud.service.impl;

import com.example.dcloud.pojo.Menu;
import com.example.dcloud.mapper.MenuMapper;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.UserUtils;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    @Override
    public List<Menu> getUserMenus() {
        User currentUser = UserUtils.getCurrentUser();
        List<Menu> menus = menuMapper.getMenusByUserId(currentUser.getId());
        return menus;
    }
}
