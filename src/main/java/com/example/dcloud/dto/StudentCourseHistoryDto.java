package com.example.dcloud.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "StudentCourseHistoryDto对象，用于某学生某班课历史签到记录的简单显示",description = "")
public class StudentCourseHistoryDto {
    @ApiModelProperty("签到id")
    private Integer signId;
    @ApiModelProperty("签到开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private LocalDateTime startTime;
    @ApiModelProperty("签到结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private LocalDateTime endTime;

    @ApiModelProperty("签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private LocalDateTime signTime;
    @ApiModelProperty("签到状态")
    private Integer status;
    @ApiModelProperty("增加经验值")
    private Integer addExp;
    @ApiModelProperty("签到距离")
    private Double distance;
    @ApiModelProperty("签到类型")
    private Double type;

    @ApiModelProperty("星期几")
    @TableField(exist = false)
    private String dayOfWeek;


}
