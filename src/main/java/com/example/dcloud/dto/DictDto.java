package com.example.dcloud.dto;

import com.example.dcloud.pojo.Dict;
import com.example.dcloud.pojo.DictInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Dict和DictInfo对象",description = "")
public class DictDto {
    @ApiModelProperty("字典对象")
    private Dict dict;
    @ApiModelProperty("字典信息对象数组")
    private List<DictInfo> dictInfoList;
}
