package com.kkkzoz.controller;

import com.kkkzoz.service.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/util")
@AllArgsConstructor
public class UtilController {


    private final UtilService utilService;

    @GetMapping("/getWeekOfYear")
    public int getWeekOfYear() {
        return utilService.getWeekOfYear();
    }
}