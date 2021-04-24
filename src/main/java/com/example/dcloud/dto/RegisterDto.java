package com.example.dcloud.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Register对象",description = "")
public class RegisterDto {

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;
    @ApiModelProperty(value = "验证码")
    private String code;
    @ApiModelProperty(value = "角色")
    private Integer roleId;
}
