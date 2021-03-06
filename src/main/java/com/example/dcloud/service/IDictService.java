package com.example.dcloud.service;

import com.example.dcloud.pojo.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.dto.DictDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface IDictService extends IService<Dict> {

    /**
     * 获取字典列表，传参就是按条件查询，不传就是
     *
     * @param currentPage
     * @param size
     * @param search
     * @return
     */
    RespPageBean listDict(Integer currentPage, Integer size, String search);


    /**
     * 新增一个字典及其字典项
     * @param dictDto
     * @return
     */
    RespBean addDict(DictDto dictDto);

    /**
     * 修改一个字典及其字典项
     * @param dictDto
     * @return
     */
    RespBean updateDict(DictDto dictDto);


    /**
     * 删除一个字典及其字典项
     * @param id
     * @return
     */
    RespBean deleteDict(Integer id);
}
