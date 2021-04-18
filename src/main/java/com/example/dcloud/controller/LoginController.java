package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IUserService;
import com.example.dcloud.dto.LoginDto;
import com.example.dcloud.dto.RegisterDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
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
    public RespBean manageLoginByPassword(@RequestBody LoginDto loginDto, HttpServletRequest request){
        return userService.manageLoginByPassword(loginDto.getUsernameOrPhone(), loginDto.getPassword(),request);
    }

    @ApiOperation("管理系统通过phone+code登录，登录之后返回token")
    @PostMapping("/manage/loginByCode")
    public RespBean manageLoginByCode(@RequestBody LoginDto loginDto, HttpServletRequest request){
        return userService.manageLoginByCode(loginDto.getUsernameOrPhone(), loginDto.getCode(),request);
    }

    @ApiOperation("移动端通过username/phone + password登录，登录之后返回token")
    @PostMapping("/mobile/loginByPassword")
    public RespBean mobileLoginByPassword(@RequestBody LoginDto loginDto, HttpServletRequest request){
        return userService.mobileLoginByPassword(loginDto.getUsernameOrPhone(), loginDto.getPassword(),request);
    }

    @ApiOperation("移动端通过phone+code登录，登录之后返回token")
    @PostMapping("/mobile/loginByCode")
    public RespBean mobileLoginByCode(@RequestBody LoginDto loginDto, HttpServletRequest request){
        return userService.mobileLoginByCode(loginDto.getUsernameOrPhone(), loginDto.getCode(),request);
    }
    @ApiOperation(value = "用户注销功能")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功");
    }

    @ApiOperation("获取登录验证码")
    @GetMapping("/loginCaptcha")
    public RespBean getLoginCaptcha(@RequestParam("phone")String phone){
        if (!StringUtils.hasText(phone.trim())){
            RespBean.error("请先输入手机号");
        }
        return userService.getLoginCaptcha(phone);
    }

    @ApiOperation("获取注册验证码")
    @GetMapping("/registerCaptcha")
    public RespBean getRegisterCaptcha(@RequestParam("phone")String phone){
        if (!StringUtils.hasText(phone.trim())){
            RespBean.error("请先输入手机号");
        }
        return userService.getRegisterCaptcha(phone);
    }

    @ApiOperation("用户注册功能")
    @PostMapping("/register")
    public RespBean register(@RequestBody RegisterDto registerDto){
        if (StringUtils.hasText(registerDto.getPhone())){
            return RespBean.error("手机号不能为空");
        }
        if (StringUtils.hasText(registerDto.getUsername())){
            return RespBean.error("用户名不能为空");
        }
        if (StringUtils.hasText(registerDto.getPassword())){
            return RespBean.error("密码不能为空");
        }
        if (StringUtils.hasText(registerDto.getCheckPassword())){
            return RespBean.error("请输入确认密码");
        }
        if (StringUtils.hasText(registerDto.getUsername())){
            return RespBean.error("请输入确认密码");
        }
        if (StringUtils.hasText(registerDto.getCode())){
            return RespBean.error("请输入验证码");
        }
        if(!registerDto.getCheckPassword().equals(registerDto.getPassword())){
            return RespBean.error("两次密码输入不一致");
        }
        if (registerDto.getRoleId() == null){
            return RespBean.error("请选择注册的角色");
        }
        return userService.register(registerDto);



    }







}
