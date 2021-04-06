package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IUserService;
import com.example.dcloud.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "LoginController")
public class LoginController {
    @Resource
    private IUserService userService;

    @ApiOperation("管理系统通过username/phone + password登录，登录之后返回token")
    @PostMapping("/manage/loginByPassword")
    public RespBean manageloginByPassword(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.manageLoginByPassword(loginVo.getUsernameOrPhone(),loginVo.getPassword(),request);
    }

    @ApiOperation("管理系统通过phone+code登录，登录之后返回token")
    @PostMapping("/manage/loginByCode")
    public RespBean manageLoginByCode(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.manageLoginByCode(loginVo.getUsernameOrPhone(),loginVo.getCode(),request);
    }

    @ApiOperation("管理系统通过username/phone + password登录，登录之后返回token")
    @PostMapping("/mobile/loginByPassword")
    public RespBean mobileLoginByPassword(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.mobileLoginByPassword(loginVo.getUsernameOrPhone(),loginVo.getPassword(),request);
    }

    @ApiOperation("管理系统通过phone+code登录，登录之后返回token")
    @PostMapping("/mobile/loginByCode")
    public RespBean mobileLoginByCode(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.mobileLoginByCode(loginVo.getUsernameOrPhone(),loginVo.getCode(),request);
    }
    @ApiOperation(value = "用户注销功能")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功");
    }







}
