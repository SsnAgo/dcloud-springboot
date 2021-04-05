package com.example.dcloud.utils;

import java.util.Date;

public class CourseUtils {

    public static String generatorCourseNumber(){
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
}
