package com.example.dcloud.mapper;

import com.example.dcloud.vo.SettingVo;
import org.apache.ibatis.annotations.Param;

public interface SettingMapper {

    /**
     * 更新设置
     * @param settingVo
     * @return
     */
    boolean updateSetting(@Param("settingVo")SettingVo settingVo);
}
