package com.example.dcloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class DcloudApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //String encoded = bCryptPasswordEncoder.encode("123");
        System.out.println(bCryptPasswordEncoder.matches("123","$2a$10$wkxorWfoniND9P1qEem5nOjvkD0mIfx7oHeM2uyLffKuf5XcZvP.G"));
    }

}
