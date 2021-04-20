package com.example.dcloud.vo;


import com.example.dcloud.dto.SignStudentDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "签到情况vo", description = "")
public class SignHistoryVo {


    @ApiModelProperty("签到id")
    private Integer signId;
    @ApiModelProperty("发起时间")
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss",timezone = "GMT")
    private LocalDateTime startTime;
    @ApiModelProperty("星期几")
    private String dayOfWeek;
    @ApiModelProperty("签到类型")
    private Integer type;
    @ApiModelProperty("班课总人数")
    private Integer total;
    @ApiModelProperty("已签到人数")
    private Integer signedCount;



}
