package com.example.dcloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.example.dcloud.config.CustomAuthorityDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
@TableName("t_user")
@ApiModel(value="User对象", description="")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "登录用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "性别编号")
    private Integer sexCode;

    @ApiModelProperty(value = "性别")
    @TableField(exist = false)
    private String sex;


    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "用户生日")
    private String birthday;

    @ApiModelProperty(value = "学号or教工号or管理员工号")
    private String number;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "学历编号")
    private Integer educationCode;

    @ApiModelProperty(value = "学历")
    @TableField(exist = false)
    private String education;

    @ApiModelProperty(value = "经验值")
    private Integer exp;

    @ApiModelProperty(value = "头像")
    private String userFace;

    @ApiModelProperty(value = "是否启用")
    @Getter(AccessLevel.NONE)
    private Boolean enabled;


    @ApiModelProperty(value = "学校id")
    private Integer schoolId;

    @ApiModelProperty(value = "学院id")
    private Integer departmentId;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "学校")
    @TableField(exist = false)
    private School school;

    @ApiModelProperty(value = "学院")
    @TableField(exist = false)
    private School department;

    @ApiModelProperty(value = "专业")
    @TableField(exist = false)
    private Major major;

    @ApiModelProperty(value = "用户角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色")
    @TableField(exist = false)
    private Role role;

    /**
     * 由于这一部分不是对象的字段，因此这个authorities不会被解析成json 因此需要一个自定义的CustomAuthorityDeserializer来自定义一下反序列化
     * @return
     */

    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
        List<SimpleGrantedAuthority> collect = Arrays.asList(simpleGrantedAuthority);
        return collect;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
