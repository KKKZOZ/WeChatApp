package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.Favorite;
import com.kkkzoz.domain.entity.Mistake;
import com.kkkzoz.domain.entity.Test;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.Tested;
import com.kkkzoz.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "题库接口")
@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @Tested
    @GetMapping("/list")
    @ApiOperation(value = "获取指定题目列表",notes = "参数是一个数组，其中每个元素包含题目的id和类别")
    public List<QuestionDTO> getQuestionList(@RequestBody List<Map<String, String>> params) {
        return questionService.getQuestionList(params);
    }

    @Tested
    @GetMapping("/batch")
    @ApiOperation(value = "批量获取题目")
    public List<QuestionDTO> getQuestionBatch(@RequestBody Map<String, String> params) {
        int questionId = Integer.parseInt(params.get("questionId"));
        int category = Integer.parseInt(params.get("category"));
        int count = Integer.parseInt(params.get("count"));

        return questionService.getQuestionBatch(questionId, category, count);
    }



    @Tested
    @PostMapping("/mistake")
    @ApiOperation(value = "添加错题")
    public ResponseVO addMistakes(@RequestBody List<Mistake> mistakes) {
         return questionService.addMistakes(mistakes);
    }

    @Tested
    @GetMapping("/mistake")
    @ApiOperation(value = "获取错题列表")
    public List<Long> getMistakes(@RequestBody Map<String, String> params) {
        int userId = Integer.parseInt(params.get("userId"));
        int category = Integer.parseInt(params.get("category"));
        return questionService.getMistakes(userId, category);
    }

    @Tested
    @DeleteMapping("/mistake")
    @ApiOperation(value = "删除错题")
    public ResponseVO deleteMistake(@RequestBody Map<String, String> params) {
        int userId = Integer.parseInt(params.get("userId"));
        int questionId = Integer.parseInt(params.get("questionId"));
        int category = Integer.parseInt(params.get("category"));
        return questionService.deleteMistake(userId,questionId,category);
    }

    @PostMapping("/favorite")
    @ApiOperation(value = "添加收藏")
    public ResponseVO addFavorite(@RequestBody Favorite favorite) {
        return questionService.addFavorite(favorite);
    }

    @GetMapping("/favorite")
    @ApiOperation(value = "获取收藏列表")
    public List<Long> getFavorites(@RequestBody Map<String, String> params) {
        int userId = Integer.parseInt(params.get("userId"));
        int category = Integer.parseInt(params.get("category"));
        return questionService.getFavorites(userId, category);
    }


    @PostMapping("/test")
    @ApiOperation(value = "保存试卷结果")
    public ResponseVO addTestResult(@RequestBody Test test) {
        return questionService.addTestResult(test);
    }

    @GetMapping("/test")
    @ApiOperation(value = "获取试卷结果")
    public Test getTestResult(@RequestBody Map<String, String> params) {
        int userId = Integer.parseInt(params.get("userId"));
        int testId = Integer.parseInt(params.get("testId"));
        int category = Integer.parseInt(params.get("category"));
        return questionService.getTestResult(userId,testId, category);
    }

    @PostMapping("/practice/status")
    @ApiOperation(value = "保存练习状态")
    public ResponseVO addPracticeStatus(@RequestBody Map<String, String> params){
        int userId = Integer.parseInt(params.get("userId"));
        int questionId = Integer.parseInt(params.get("questionId"));
        int category = Integer.parseInt(params.get("category"));
        return questionService.addPracticeStatus(userId,questionId,category);
    }






}

