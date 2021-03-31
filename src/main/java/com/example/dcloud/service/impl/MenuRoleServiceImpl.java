package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.MenuRole;
import com.example.dcloud.mapper.MenuRoleMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Resource
    private MenuRoleMapper menuRoleMapper;

    @Override
    public List<Menu> getMenusByRoleId(Integer rid) {
        return menuRoleMapper.getMenusByRoleId(rid);
    }

    @Override
    @Transactional
    public RespBean updateRoleMenus(Integer rid, Integer[] ids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (null == ids || ids.length == 0){
            return RespBean.success("更新角色菜单成功");
        }
        Integer result = menuRoleMapper.insertRecords(rid,ids);
        if (result == ids.length) {
            return RespBean.success("更新角色菜单成功");
        }
        return RespBean.error("更新角色菜单失败");
    }
}
