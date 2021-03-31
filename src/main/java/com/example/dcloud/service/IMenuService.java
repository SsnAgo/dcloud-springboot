package com.example.dcloud.service;

import com.example.dcloud.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();


    /**
     * 获取当前用户菜单
     */
    List<Menu> getUserMenus();

    /**
     * 根据条件查询 如果为空 就查全部
     * @param menu
     * @return
     */
    List<Menu> getMenus(Menu menu);
}
