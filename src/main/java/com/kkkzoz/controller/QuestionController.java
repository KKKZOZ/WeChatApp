package com.kkkzoz.controller;

import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


//    获取指定题目
    @GetMapping("/single")
    public List<QuestionDTO> getSingleQuestion(@RequestBody Map<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        return questionService.getSingleQuestion(id);
    }


}

