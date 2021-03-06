package com.example.dcloud.controller;


import com.example.dcloud.dto.MenuSeqDto;
import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "MenuController")
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @ApiOperation("获取当前用户菜单")
    @GetMapping("/currentUser")
    public List<Menu> getUserMenus(){
        return menuService.getUserMenus();
    }

    @ApiOperation("管理员获取所有菜单列表")
    @GetMapping("/manage/")
    public List<Menu> getMenus(String search){
        return menuService.getMenus(search);
    }

    @ApiOperation("管理员修改菜单")
    @PutMapping("/manage/")
    public RespBean updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @ApiOperation("管理员新增菜单")
    @PostMapping("/manage/")
    public RespBean addMenu(@RequestBody Menu menu) {
        if (!StringUtils.hasText(menu.getName())) {
            return RespBean.error("菜单名不能为空");
        }
        return menuService.saveMenu(menu);
    }

    @ApiOperation("管理员删除菜单")
    @DeleteMapping("/manage/")
    public RespBean delMenu(@RequestParam Integer id){
        return menuService.delMenu(id);
    }

    @ApiOperation("调换菜单顺序，并返回最新的顺序(返回所有菜单和当前用户菜单)")
    @PutMapping("/manage/seq")
    public Map<String,Object> changeMenuSeq(@RequestBody List<MenuSeqDto> menuSeqList){
        return menuService.changeMenuSeq(menuSeqList);
    }
}
