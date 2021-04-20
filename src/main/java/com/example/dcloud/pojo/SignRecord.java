package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sign_record")
@ApiModel(value="SignRecord对象", description="")
public class SignRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签到记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "课程Id")
    private Integer courseId;

    @ApiModelProperty(value = "学生id")
    private Integer studentId;

    @ApiModelProperty(value = "签到id")
    private Integer signId;

    @ApiModelProperty(value = "本次签到获得的经验值")
    private Integer addExp;

    @ApiModelProperty(value = "签到开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT")
    private LocalDateTime signTime;


    @ApiModelProperty(value = "签到状态")
    private Integer status;

    @ApiModelProperty(value = "离签到位置的距离")
    private Double distance;

    @ApiModelProperty("星期几")
    @TableField(exist = false)
    private String dayOfWeek;


}
