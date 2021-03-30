package com.example.dcloud.controller;


import com.example.dcloud.pojo.Menu;
import com.example.dcloud.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
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
public class MenuController {

    @Resource
    private IMenuService menuService;

    @ApiOperation("获取当前用户菜单")
    @GetMapping("menu")
    public List<Menu> getUserMenus(){
        return menuService.getUserMenus();
    }
}
