package com.example.dcloud.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ApiModelProperty("学生学号")
    private String number;
    @ApiModelProperty("签到状态")
    private Integer status;
    @ApiModelProperty("增加的经验值")
    private Integer addExp;
    @ApiModelProperty("签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private LocalDateTime signTime;


    @ApiModelProperty("学生头像")
    private String userFace;
    @ApiModelProperty("签到距离")
    private Double distance;

}
