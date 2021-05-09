package com.example.dcloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "班级成员vo", description = "")
public class CourseMemberVo {

    @ApiModelProperty(value = "cs表id")
    private Integer id;

    @ApiModelProperty(value = "班课id")
    private Integer cid;

    @ApiModelProperty(value = "学生id")
    private Integer uid;

    @ApiModelProperty(value = "学生学号")
    private String unumber;

    @ApiModelProperty(value = "学生姓名")
    private String uname;

    @ApiModelProperty(value = "学生在该班课的经验值")
    private Integer exp;

    @ApiModelProperty(value = "该学生在班课的经验值排名")
    private Integer rank;
    @ApiModelProperty(value = "学生头像")
    private String uface;




}
