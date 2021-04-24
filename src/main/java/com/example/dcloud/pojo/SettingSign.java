package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_setting_sign")
@ApiModel(value="SettingSign对象", description="")
public class SettingSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单项设置id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "签到经验值")
    private Integer signExp;
    @ApiModelProperty(value = "请假经验值")
    private Integer dayOffExp;
    @ApiModelProperty(value = "迟到经验值")
    private Integer lateExp;
    @ApiModelProperty(value = "早退经验值")
    private Integer leaveEarlyExp;

    @ApiModelProperty(value = "签到范围，支持小数3位")
    private Double signDistance;


}
