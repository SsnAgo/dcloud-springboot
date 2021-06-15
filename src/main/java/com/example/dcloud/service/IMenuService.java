package com.example.dcloud.service;

import com.example.dcloud.dto.MenuSeqDto;
import com.example.dcloud.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;

import java.util.List;
import java.util.Map;

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
     * 根据菜单名查询 如果为空 就查全部
     * @param search
     * @return
     */
    List<Menu> getMenus(String search);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    RespBean updateMenu(Menu menu);

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    RespBean saveMenu(Menu menu);

    /**
     * 删除菜单
     * @param id
     * @return
     */
    RespBean delMenu(Integer id);

    /**
     * 调换菜单顺序
     * @param menuSeqList
     * @return
     */
    Map<String,Object> changeMenuSeq(List<MenuSeqDto> menuSeqList);
}
