package com.example.dcloud.controller;


import com.example.dcloud.anotation.ManageAllow;
import com.example.dcloud.anotation.StudentAllow;
import com.example.dcloud.anotation.TeacherAllow;
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

    @ManageAllow
    @GetMapping("/teacher")
    public String teacher(){
        return "teacher";
    }

    @StudentAllow
    @GetMapping("/student")
    public String student(){
        return "student";
    }

    @GetMapping("/oauth")
    public String oauth(){
        return "oauth";
    }
}
