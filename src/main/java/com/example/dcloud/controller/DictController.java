package com.example.dcloud.controller;


import com.example.dcloud.pojo.*;
import com.example.dcloud.service.IDictService;
import com.example.dcloud.dto.DictDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "DictController")
@RequestMapping("/dict/")
public class DictController {

    @Resource
    private IDictService dictService;

    @ApiOperation("列出字典列表，传参就是按条件查询，不传就是查全部")
    @GetMapping("/manage/")
    public RespPageBean listDict(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10")Integer size,
                                 @ApiParam("可按中文标识或英文标识搜索") String search){
        return dictService.listDict(currentPage,size,search);
    }

    @ApiOperation("新增字典及其字典信息")
    @PostMapping("/manage/")
    public RespBean addDict(@RequestBody DictDto dictDto){
        return dictService.addDict(dictDto);
    }

    @ApiOperation("修改某个字典及其字典项信息")
    @PutMapping("/manage/")
    public RespBean updateDict(@RequestBody DictDto dictDto){
        return dictService.updateDict(dictDto);
    }

    @ApiOperation("删除某个字典及其字典项")
    @DeleteMapping("/manage/{id}")
    public RespBean deleteDict(@ApiParam("字典id")@PathVariable Integer id){
        return dictService.deleteDict(id);
    }


}
