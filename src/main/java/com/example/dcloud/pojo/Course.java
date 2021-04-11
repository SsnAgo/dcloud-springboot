package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_course")
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班课id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "班课名称")
    private String name;

    @ApiModelProperty(value = "类别编号，对应字典，必修选修")
    private Integer typeCode;
    @ApiModelProperty(value = "类别名")
    @TableField(exist = false)
    private String type;

    @ApiModelProperty(value = "班课号")
    private String courseCode;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班课描述")
    private String description;

    @ApiModelProperty(value = "班课封面")
    private String picture;

    @ApiModelProperty
    private String prcode;

    @ApiModelProperty(value = "开课学期")
    private String term;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者id")
    private Integer createrId;

    @ApiModelProperty(value = "创建者信息")
    @TableField(exist = false)
    private User creater;

    @ApiModelProperty(value = "学习要求")
    private String learnRequire;

    @ApiModelProperty(value = "教学安排")
    private String teachProgress;

    @ApiModelProperty(value = "考试安排")
    private String examSchedule;

    @ApiModelProperty(value = "学校id")
    private Integer schoolId;

    @ApiModelProperty(value = "学校名")
    @TableField(exist = false)
    private String schoolName;

    @ApiModelProperty(value = "是否学校课表班课")
    private Integer flag;

    @ApiModelProperty("教师姓名")
    @TableField(exist = false)
    private String teacherName;


    @ApiModelProperty(value = "是否可用")
    private Boolean enabled;




}
