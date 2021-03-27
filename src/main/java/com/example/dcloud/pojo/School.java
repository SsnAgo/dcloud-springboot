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
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_school")
@ApiModel(value="School对象", description="")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "学校名称")
    private String name;

    @ApiModelProperty(value = "父学校id")
    private Integer parentId;

    @ApiModelProperty(value = "路径")
    private String schoolPath;

    @ApiModelProperty(value = "是否父学校")
    private Boolean isParent;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "学校编号")
    private String schoolCode;


}
