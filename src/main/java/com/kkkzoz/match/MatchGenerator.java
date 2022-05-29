package com.kkkzoz.match;

import com.kkkzoz.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MatchGenerator {

    private final QuestionService questionService;

    private final int questionOneRange;
    private final int questionFourRange;

    public MatchGenerator(QuestionService questionService) {
        this.questionService = questionService;
        this.questionOneRange = questionService.getQuestionCount(1) + 1;
        this.questionFourRange = questionService.getQuestionCount(4) + 1;
        log.info("questionOneRange: " + questionOneRange);
        log.info("questionFourRange: " + questionFourRange);
    }

    public  List<Integer> generateQuestionList(int category, int count) {
        log.info("generateQuestionList: " + category + " " + count);
        Set<Integer> questionIdSet = new HashSet<>();
        int range = this.getRange(category);
        Random random = new Random();
        while (questionIdSet.size() < count) {
            int randomNumber = 0;
            while (randomNumber == 0) {
                randomNumber = random.nextInt(range);
            }
            questionIdSet.add(randomNumber);
        }
        log.info("questionIdSet: " + questionIdSet);
        return new ArrayList<Integer>(questionIdSet);
    }


    private int getRange(int category) {
        if (category == 1) {
            return questionOneRange;
        }
        if (category == 4) {
            return questionFourRange;
        }
        return 0;
    }
}
