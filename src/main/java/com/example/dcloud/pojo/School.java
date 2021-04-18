package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

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
@TableName("t_school")
@ApiModel(value = "School对象", description = "")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "学校名称")
    @NotEmpty(message = "学校名称不能为空")
    private String name;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "学校编号")
    private String schoolCode;

    @ApiModelProperty("学校下面的学院")
    @TableField(exist = false)
    private List<School> departments;


    @ApiModelProperty("父节点id")
    private Integer parentId;


}
