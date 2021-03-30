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
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_system")
@ApiModel(value="System对象", description="")
public class System implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统设置id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "签到经验")
    private Integer signExp;

    @ApiModelProperty(value = "签到范围")
    private Integer signDistance;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "出勤等级")
    private Integer lv;

    @ApiModelProperty(value = "出勤率左边界")
    private Integer left;

    @ApiModelProperty(value = "出勤率右边界")
    private Integer right;


}
