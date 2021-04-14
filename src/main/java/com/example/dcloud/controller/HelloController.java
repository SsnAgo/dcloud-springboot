package com.example.dcloud.controller;


import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/teacher")
    public String teacher(){
        return "teacher";
    }

    @GetMapping("/student")
    public String student(){
        return "student";
    }
}
