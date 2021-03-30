package com.example.dcloud.utils;

import com.example.dcloud.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class UserUtils {
    public static User getCurrentUser(){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user;
    }
}
