package com.example.dcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.example.dcloud.mapper")

public class DcloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(DcloudApplication.class, args);
    }

}
