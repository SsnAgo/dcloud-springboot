package com.example.dcloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SignRecordChangeDto对象，用于更改签到状态传参",description = "")
public class SignRecordChangeDto {
    @ApiModelProperty(value = "签到id")
    private Integer signId;
    @ApiModelProperty(value = "要修改的状态")
    private Integer status;
    @ApiModelProperty(value = "要修改的学生")
    private List<Integer> studentIds;
}
