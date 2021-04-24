package com.example.dcloud.utils;

public class SignUtils {
    /**
     * 创建签到的种类
     */
    public static final Integer NO_LIMIT = 0;
    public static final Integer TIME_LIMIT = 1;
    public static final Integer HAND = 2;
    public static final Integer LOCATION = 3;
    /**
     * 签到状态
     */
    public static final Integer SIGNED = 1;
    public static final Integer NO_SIGNED = 0;
    public static final Integer DAY_OFF = 2;
    public static final Integer LATE_IN = 3;
    public static final Integer EARLY_LEAVE = 4;


    /**
     * 放弃签到 和 关闭签到
     */
    public static final Integer GIVE_UP = 0;
    public static final Integer CLOSE = 1;

}
