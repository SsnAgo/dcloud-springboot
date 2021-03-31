package com.example.dcloud.controller;


import com.example.dcloud.service.IDictInfoService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/dict-info")
public class DictInfoController {
    @Resource
    private IDictInfoService dictInfoService;



}
