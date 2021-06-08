package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.mapper.SettingMapper;
import com.example.dcloud.pojo.Setting;
import com.example.dcloud.service.ISettingService;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {

}
