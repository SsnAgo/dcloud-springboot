package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.Menu;
import com.example.dcloud.mapper.MenuMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Stream;

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

    @Override
    public List<Menu> getMenus(String search) {
        return menuMapper.getMenus(search);
    }

    @Override
    @Transactional
    public RespBean updateMenu(Menu menu) {
        // 先检查菜单名有没有重复
        Menu exist = menuMapper.selectOne(new QueryWrapper<Menu>().eq("name",menu.getName()).ne("id",menu.getId()));
        if (exist != null) {
            return RespBean.error("菜单名不能重复");
        }
        if (menu.getSequence() == null){
            return RespBean.error("顺序不能为空");
        }
        exist = menuMapper.selectOne(new QueryWrapper<Menu>().eq("sequence",menu.getSequence()).ne("id",menu.getId()));
        if (exist != null) {
            return RespBean.error("顺序不能重复");
        }
        // 没问题了就更新
        if (menuMapper.updateById(menu) == 1) {
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }
}
