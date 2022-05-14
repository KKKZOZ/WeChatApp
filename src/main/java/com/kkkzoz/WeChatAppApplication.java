package com.kkkzoz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MapperScan("com.kkkzoz.mapper")
@SpringBootApplication
public class WeChatAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatAppApplication.class, args);
        //2022-05-06
//        System.out.println(LocalDateTime.now());
    }

}
