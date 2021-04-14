package com.example.dcloud.service;

import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Sign;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ISignService extends IService<Sign> {

    /**
     * 学生进行无限制签到
     * @param cid
     * @param id
     * @return
     */
    RespBean noLimitSign(Integer cid, Integer id);

    /**
     * 学生进行一分钟签到
     * @param cid
     * @param id
     * @return
     */
    RespBean oneMinuteSign(Integer cid, Integer id);

    /**
     * 学生进行手势签到
     * @param cid
     * @param sid
     * @param sequence
     * @return
     */
    RespBean handSign(Integer cid, Integer sid, String sequence);

    /**
     * 教师关闭签到
     * @param cid
     * @return
     */
    RespBean closeSign(Integer cid);
}
