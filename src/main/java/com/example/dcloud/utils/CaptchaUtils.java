package com.example.dcloud.utils;

import java.util.Random;

public class CaptchaUtils {
    public static String generatorCaptcha(){
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        return code;
    }
}
