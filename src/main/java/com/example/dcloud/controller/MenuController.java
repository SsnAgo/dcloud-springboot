package com.example.dcloud.controller;


import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/menu/manage")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @ApiOperation("获取当前用户菜单")
    @GetMapping("/currentUser")
    public List<Menu> getUserMenus(){
        return menuService.getUserMenus();
    }

    @ApiOperation("管理员获取所有菜单列表")
    @GetMapping("/")
    public List<Menu> getMenus(Menu menu){
        return menuService.getMenus(menu);
    }

    @ApiOperation("管理员修改菜单")
    @PutMapping("/")
    public RespBean updateMenu(Menu menu){
        if (menuService.updateById(menu)){
            return RespBean.success("修改菜单成功");
        }
        return RespBean.error("修改菜单失败");
    }




}
