package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
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
@TableName("t_sign")
@ApiModel(value="Sign对象", description="")
public class Sign implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签到id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "签到开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "签到结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "签到位置")
    private String local;

    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "签到码")
    private String code;

    @ApiModelProperty(value = "签到发起人Id")
    private Integer teacherId;

    @ApiModelProperty(value = "0 无限制签到 1 一分钟限时 2 手势签到  3位置签到")
    private Integer type;


}
