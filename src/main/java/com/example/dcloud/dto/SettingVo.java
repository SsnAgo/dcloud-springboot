package com.example.dcloud.dto;


import com.example.dcloud.pojo.SettingLevel;
import com.example.dcloud.pojo.SettingSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "系统设置Setting的聚合对象",description = "")
public class SettingVo {

    @ApiModelProperty(value = "签到设置对象")
    private SettingSign settingSign;

    @ApiModelProperty(value = "出勤等级设置列表")
    private List<SettingLevel> settingLevelList;


}
