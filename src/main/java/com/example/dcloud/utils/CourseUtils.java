package com.example.dcloud.utils;

import java.util.Date;

public class CourseUtils {

    public static String generatorCourseCode(){
        Date date = new Date();
        return String.valueOf((long)(date.getTime()/1000));
    }
}
