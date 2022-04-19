package com.kkkzoz.controller;

import com.kkkzoz.global.NotResponseBody;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {


    @GetMapping("/1")
    public String getTest1() {
        log.info("getTest1");
        return "test1";
    }

    @GetMapping("/2")
    @NotResponseBody
    public String getTest2() {
        log.info("getTest2");
        return "test2";
    }
}
