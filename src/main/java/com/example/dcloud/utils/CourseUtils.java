package com.example.dcloud.utils;

import com.example.dcloud.pojo.Course;

import java.util.Date;
import java.util.Random;

public class CourseUtils {
    public static final String PR_PREFIX = "https://api.pwmqr.com/qrcode/create/?url=";

    private static final String[] images = {
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByilyAWJbEAAAJFcHyt_U366.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByipmAaqhEAAAJis5Vxq4365.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByiqeAClfKAAAJZkWQxpg333.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByiraAdXv6AAAJ_flf46E823.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByisGAQ1_hAAAI6bnBfQ0485.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByjg-AUitqAAAJ8hPR4V4250.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByjhqASVCVAAAJfITf8ew227.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByjiOAEHauAAAJzAFJ6x4167.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByjjKARKK-AAAJIXl7rWI986.png",
            "http://116.62.152.144:7777/group1/M00/00/00/dD6YkGByjjuAEZ4iAAAKQRbMkFI204.png"};

    public static String generatorCourseCode() {
        Date date = new Date();
        return String.valueOf((long) (date.getTime() / 1000));
    }

    public static String generatorCourseImage() {
        Random random = new Random();
        return images[random.nextInt(10)];
    }

}
