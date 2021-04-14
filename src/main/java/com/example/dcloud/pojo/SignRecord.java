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
@TableName("t_sign_record")
@ApiModel(value="SignRecord对象", description="")
public class SignRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "历史记录id")
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

    @ApiModelProperty(value = "签到时间")
    private LocalDateTime signTime;


}
