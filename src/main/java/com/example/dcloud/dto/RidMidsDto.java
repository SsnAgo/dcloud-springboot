package com.example.dcloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "修改角色和其权限",description = "")
public class RidMidsDto {
    @ApiModelProperty(value = "角色id")
    private Integer rid;
    @ApiModelProperty(value = "菜单id列表")
    private List<Integer> ids;
}
