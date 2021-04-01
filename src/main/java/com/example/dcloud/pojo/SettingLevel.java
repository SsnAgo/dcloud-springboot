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
@TableName("t_setting_level")
@ApiModel(value="SettingLevel对象", description="")
public class SettingLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统设置id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "出勤等级")
    private Integer lv;

    @ApiModelProperty(value = "出勤率左边界")
    private Integer left;

    @ApiModelProperty(value = "出勤率右边界")
    private Integer right;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;


}
