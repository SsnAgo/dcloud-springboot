package com.example.dcloud.utils;

public class WeekDayUtils {
    public static String num2word(Integer val){
        String day;
        switch (val){
            case 1:
                day = "星期一";break;
            case 2:
                day = "星期二";break;
            case 3:
                day = "星期三";break;
            case 4:
                day = "星期四";break;
            case 5:
                day = "星期五";break;
            case 6:
                day = "星期六";break;
            case 7:
                day = "星期日";break;
            default:
                day = "未知";break;
        }
        return day;
    }
}
