package com.kkkzoz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@MapperScan("com.kkkzoz.mapper")
@SpringBootApplication
@EnableAsync
public class WeChatAppApplication {

    public static void main(String[] args) {
        //TODO: 给Redis加密
        SpringApplication.run(WeChatAppApplication.class, args);
//        Map<String, String> map = new HashMap<>();
//        map.put("asd", "asd");
//        map.put("dsa", "dsa");
//        String value = map.get("asd");
//        System.out.println(value);
//        map.put("asd", "KKKZOZ");
//        String v2 = map.get("asd");
//        System.out.println(v2);

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

