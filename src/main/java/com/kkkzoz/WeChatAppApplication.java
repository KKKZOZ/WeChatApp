package com.kkkzoz;

import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@MapperScan("com.kkkzoz.mapper")
@SpringBootApplication
@EnableAsync
public class WeChatAppApplication {

    public static void main(String[] args) {
        //TODO: 给Redis加密
        SpringApplication.run(WeChatAppApplication.class, args);


        }




//        List<String> array = new ArrayList<>();
//        array.add("2020-12-08 17:30:50");
//        array.add("2020-12-08 17:30:51");
//        array.add("2020-12-08 18:30:50");
//        array.add("2023-12-08 18:30:50");
//        array.add("2022-11-08 18:30:50");
//        array.add("2022-6-01 18:30:50");
//        array.add("2019-9-08 18:30:50");
//        array.sort(Comparator.naturalOrder());
//        System.out.println(array);

}

