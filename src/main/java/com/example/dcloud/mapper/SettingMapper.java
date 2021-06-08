package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud.pojo.Setting;
import com.example.dcloud.vo.SettingVo;
import org.apache.ibatis.annotations.Param;

public interface SettingMapper extends BaseMapper<Setting> {

    /**
     * 更新设置
     * @param settingVo
     * @return
     */
    boolean updateSetting(@Param("settingVo")SettingVo settingVo);
}
