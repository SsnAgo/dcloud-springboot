package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.dcloud.mapper.MenuRoleMapper;
import com.example.dcloud.pojo.Menu;
import com.example.dcloud.mapper.MenuMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.UserUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private MenuRoleMapper menuRoleMapper;

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
        Integer menuSize = getMenus(null).size();
        // 先检查菜单名有没有重复
        Menu exist = menuMapper.selectOne(new QueryWrapper<Menu>().eq("name", menu.getName()).ne("id", menu.getId()));
        if (exist != null) {
            return RespBean.error("菜单名不能重复");
        }
        if (menu.getSequence() == null) {
            return RespBean.error("顺序不能为空");
        }
        if (menu.getSequence() < 1 || menu.getSequence() > menuSize) {
            return RespBean.error("请输入正确的顺序范围");
        }
        // 范围没问题 就去更新列表
        Integer pos = updateMenusSequence(menu.getId(), menu.getSequence());
        if (pos == 0){
            return RespBean.error("修改顺序失败");
        }
        // 获取修改完的顺序
        menu.setSequence(pos);
        if (menuMapper.updateById(menu) == 1) {
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @Override
    @Transactional
    public RespBean saveMenu(Menu menu) {
        menu.setParentId(1);
        if (menuMapper.insert(menu) == 1) {
            Integer pos = updateMenusSequence(menu.getId(), menu.getSequence());
            if (pos == 0){
                return RespBean.error("修改顺序失败");
            }
            // 获取修改完的顺序
            menu.setSequence(pos);
            menuMapper.updateById(menu);
            return RespBean.success("新增菜单成功");
        }
        return RespBean.error("新增菜单失败");
    }

    @Override
    @Transactional
    public RespBean delMenu(Integer id) {
        Integer pos = updateMenusSequence(id, -1);
        if (pos == 0) {
            System.out.println("重置顺序成功");
        }
        // 删除 菜单-角色关联表里的项
        menuRoleMapper.deleteMenu(id);
        if (menuMapper.deleteById(id) == 1) {
            return RespBean.success("删除菜单成功");
        }
        return RespBean.error("删除菜单失败");
    }

    /**
     *
     * @param mid 传入的menuId
     * @param seq 要调整到的位置 如果是删除就是-1
     * @return 返回更改后的位置  如果是删除或者修改失败就会返回0
     */
    private Integer updateMenusSequence(Integer mid, Integer seq) {

        // 获取mids列表 将此mid插入到对应seq的下标位置
        List<Menu> menus = getMenus(null);
        List<Integer> ids = menus.stream().map(Menu::getId).collect(Collectors.toList());
        // 移除这个id先
        ids.remove(mid);
        // 插入到对应位置
        if (seq != -1) {
            ids.add(seq - 1,mid);
        }
        // 返回的当前顺序
        Integer res = 0;
        // 根据索引生成顺序
        for (Integer id: ids) {
            Integer pos = ids.indexOf(id) + 1;
            menuMapper.updateMenuSequence(id,pos);
            if (id == mid) {
                res = pos;
            }
        }
        // 去批量更改
        return res;
    }
}
