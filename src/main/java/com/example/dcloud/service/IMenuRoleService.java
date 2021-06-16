package com.example.dcloud.service;

import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 根据角色id获取该角色的菜单
     * @param rid
     * @return
     */
    List<Menu> getMenusByRoleId(Integer rid);

    /**
     * 更新角色菜单
     * @param ids
     * @return
     */
    RespBean updateRoleMenus(Integer rid,List<Integer> ids);
}
