package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.Mistake;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.global.ResponseVO;
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


    //根据questionIds
    //  获取相应题目列表
    @GetMapping("/list")
    public List<QuestionDTO> getQuestionList(@RequestBody List<Map<String, String>> params) {
        return questionService.getQuestionList(params);
    }

    @GetMapping("/batch")
    public List<QuestionDTO> getBatchQuestions(@RequestBody Map<String, String> params) {
        int questionId = Integer.parseInt(params.get("questionId"));
        int category = Integer.parseInt(params.get("category"));
        int count = Integer.parseInt(params.get("count"));

        return questionService.getQuestionBatch(questionId, category, count);
    }

    @PostMapping("/mistake")
    public ResponseVO addMistakes(@RequestBody List<Mistake> mistakes) {
         return questionService.addMistakes(mistakes);

    }


}

