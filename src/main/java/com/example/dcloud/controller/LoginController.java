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

    @ApiOperation("通过username登录，登录之后返回token")
    @PostMapping("/loginByUsername")
    public RespBean loginByUsername(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.loginByUsername(loginVo.getUsername(),loginVo.getPassword(),request);
    }

    @ApiOperation("通过手机号登录，登录之后返回token")
    @PostMapping("/loginByPhone")
    public RespBean loginByPhone(@RequestBody LoginVo loginVo, HttpServletRequest request){
        return userService.loginByPhone(loginVo.getPhone(),loginVo.getCode(),request);
    }

    @ApiOperation(value = "用户注销功能")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功");
    }






}
