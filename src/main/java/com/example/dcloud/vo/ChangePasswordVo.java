package com.example.dcloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "修改密码对象",description = "")
public class ChangePasswordVo {

    @ApiModelProperty("用户id")
    private Integer id;
    @ApiModelProperty("当前密码")
    private String oldPassword;
    @ApiModelProperty("要修改的新密码")
    private String newPassword;
}
