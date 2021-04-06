package com.example.dcloud.controller;


import com.example.dcloud.mapper.DictMapper;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.IDictService;
import com.example.dcloud.vo.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "DictController")
@RequestMapping("/dict/")
public class DictController {

    @Resource
    private IDictService dictService;


    @ApiOperation("列出字典列表，传参就是按条件查询，不传就是查全部")
    @GetMapping("/manage")
    public RespPageBean listDict(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10")Integer size,
                                 Dict dict){
        return dictService.listDict(currentPage,size,dict);
    }

    @ApiOperation("新增字典及其字典信息")
    @PostMapping("/manage")
    public RespBean addDict(@RequestBody DictVo dictVo){
        return dictService.addDict(dictVo);
    }

    @ApiOperation("修改某个字典及其字典项信息")
    @PutMapping("/manage")
    public RespBean updateDict(@RequestBody DictVo dictVo){
        return dictService.updateDict(dictVo);
    }


}
