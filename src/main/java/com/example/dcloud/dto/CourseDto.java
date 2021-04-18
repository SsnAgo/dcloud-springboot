package com.example.dcloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "课程搜索对象",description = "")

public class CourseDto {

    @ApiModelProperty("班课名")
    private String name;
    @ApiModelProperty("班课号")
    private String courseCode;
    @ApiModelProperty("班课属于的学校id")
    private String schoolId;



}
