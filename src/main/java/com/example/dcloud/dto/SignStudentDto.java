package com.example.dcloud.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SignStudentDto对象，用于签到界面的简单显示",description = "")
public class SignStudentDto {

    @ApiModelProperty("学生id")
    private Integer id;
    @ApiModelProperty("学生姓名")
    private String name;
    @ApiModelProperty("签到状态")
    private Integer status;
    @ApiModelProperty("签到时间")
    private LocalDateTime signTime;
    @ApiModelProperty("签到距离")
    private Double distance;

}
