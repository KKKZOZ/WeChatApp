package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.Favorite;
import com.kkkzoz.domain.entity.Mistake;
import com.kkkzoz.domain.entity.Test;
import com.kkkzoz.dto.MistakeDTO;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.dto.StatusDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.Tested;
import com.kkkzoz.service.QuestionService;
import com.kkkzoz.utils.SecurityUtil;
import com.kkkzoz.vo.QuestionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Api(tags = "题库接口")
@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @Tested
    @PostMapping("/list")
    @ApiOperation(value = "获取指定题目列表", notes = "参数是一个数组，其中每个元素包含题目的id和类别")
    public List<QuestionDTO> getQuestionList(@RequestBody List<Map<String, String>> params) {
        return questionService.getQuestionList(params);
    }

    @Tested
    @PostMapping("/batch")
    @ApiOperation(value = "批量获取题目")
    public List<QuestionDTO> getQuestionBatch(@RequestBody Map<String, String> params) {
        int questionId= Integer.parseInt(params.get("questionId"));
        int category= categoryUtil(Integer.parseInt(params.get("category")));
        int count= Integer.parseInt(params.get("count"));
            return questionService.getQuestionBatch(questionId,category,count);

    }


    @Tested
    @PostMapping("/mistake")
    @ApiOperation(value = "添加错题")
    public ResponseVO addMistakes(@RequestBody List<Mistake> mistakes) {
        String userId = SecurityUtil.getUserId();
        return questionService.addMistakes(userId,mistakes);
    }

    @Tested
    @GetMapping("/mistake")
    @ApiOperation(value = "获取错题列表")
    public List<MistakeDTO> getMistakes(
            @RequestParam("category") int category) {
        String userId = SecurityUtil.getUserId();
        return questionService.getMistakes(userId, category);
    }

    @Tested
    @DeleteMapping("/mistake")
    @ApiOperation(value = "删除错题")
    public ResponseVO deleteMistake(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        int questionId = Integer.parseInt(params.get("questionId"));
        int category= categoryUtil(Integer.parseInt(params.get("category")));
        return questionService.deleteMistake(userId, questionId, category);
    }

    @PostMapping("/favorite")
    @ApiOperation(value = "添加收藏")
    public ResponseVO addFavorite(@RequestBody Favorite favorite) {
        String userId = SecurityUtil.getUserId();
        favorite.setUserId(userId);
        return questionService.addFavorite(favorite);
    }

    @GetMapping("/favorite")
    @ApiOperation(value = "获取收藏列表")
    public List<Long> getFavorites(
            @RequestParam("category") int category) {
        String userId = SecurityUtil.getUserId();
        return questionService.getFavorites(userId, category);
    }

    @DeleteMapping("/favorite")
    @ApiOperation(value = "删除收藏")
    public ResponseVO deleteFavorite(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        int questionId = Integer.parseInt(params.get("questionId"));
        int category= categoryUtil(Integer.parseInt(params.get("category")));
        return questionService.deleteFavorite(userId, questionId, category);
    }

    @GetMapping("/test/generate")
    @ApiOperation(value = "生成试卷")
    public List<QuestionDTO> generateTest(@RequestParam("category") int category,
                                          @RequestParam("count") int count) {
        return questionService.generateTest(category, count);
    }


    @PostMapping("/test")
    @ApiOperation(value = "保存试卷结果")
    public ResponseVO addTestResult(@RequestBody Test test) {
        String userId = SecurityUtil.getUserId();
        test.setUserId(userId);
        return questionService.addTestResult(test);
    }

    @GetMapping("/test")
    @ApiOperation(value = "获取试卷结果")
    public Test getTestResult(
            @RequestParam("testId") int testId,
            @RequestParam("category") int category) {
        String userId = SecurityUtil.getUserId();
        return questionService.getTestResult(userId, testId, category);
    }

    @PostMapping("/practice/status")
    @ApiOperation(value = "保存练习状态")
    public ResponseVO addPracticeStatus(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        int questionId = Integer.parseInt(params.get("questionId"));
        int category= categoryUtil(Integer.parseInt(params.get("category")));
        return questionService.addPracticeStatus(userId, questionId, category);
    }

    @GetMapping("/practice/status")
    @ApiOperation(value = "获取练习状态")
    public StatusDTO getPracticeStatus() {
        String userId = SecurityUtil.getUserId();
        return questionService.getPracticeStatus(userId);
    }

    @PostMapping("/import/{category}")
    @ApiOperation(value = "导入题目")
    public ResponseVO importQuestion(@PathVariable("category") int category, @RequestBody List<QuestionVO> questions) {
        return questionService.importQuestion(category, questions);
    }

//    @GetMapping("/modify")
//    public ResponseVO modifyQuestions() {
//        return questionService.modifyQuestions();
//    }


    private int categoryUtil(int old){
        //如果科目为2或者3，默认抽取科目一的题库
        if (old==2 || old==3){
            return 1;
        }else {
            return old;
        }
    }

}

