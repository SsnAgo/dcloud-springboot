package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/user")
@Api(tags = "UserController")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public User userInfo(Principal principal){
        if (null == principal){
            return null;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        User user = (User) usernamePasswordAuthenticationToken.getPrincipal();
        user = userService.getUserInfo(user);
        System.out.println("controller里的user : " + user);
        user.setPassword(null);
        return user;
    }

    @ApiOperation("获取所有用户(分页) 除了用户自己")
    @GetMapping("/")
    public RespPageBean getAllEmployees(@RequestParam(defaultValue = "1") Integer currentPage,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        User user) {
        return userService.getUsersByPage(currentPage, size, user);

    }


}
