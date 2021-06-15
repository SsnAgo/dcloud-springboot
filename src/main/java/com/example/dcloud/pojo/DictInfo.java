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

import javax.validation.constraints.NotEmpty;

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
@TableName("t_dict_info")
@ApiModel(value="DictInfo对象", description="")
public class DictInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内容代码")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类英文标识")
    private String tag;

    @ApiModelProperty(value = "具体内容")
    private String content;

    @ApiModelProperty(value = "值")
    private Integer value;

    @ApiModelProperty(value = "前后顺序")
    private Integer sequence;

    @ApiModelProperty(value = "是否默认显示")
    private Boolean isDefault;

    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;



}
