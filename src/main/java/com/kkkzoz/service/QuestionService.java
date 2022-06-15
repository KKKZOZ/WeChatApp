package com.kkkzoz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkkzoz.domain.entity.*;
import com.kkkzoz.dto.MistakeDTO;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.dto.StatusDTO;
import com.kkkzoz.global.APIException;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.*;
import com.kkkzoz.match.MatchGenerator;
import com.kkkzoz.repository.TestRepository;
import com.kkkzoz.vo.QuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service

public class QuestionService extends ServiceImpl<MistakeMapper, Mistake> {

    private final QuestionDTOMapper questionDTOMapper;


    private final MistakeMapper mistakeMapper;

    private final PracticeStatusMapper practiceStatusMapper;

    private final UserService userService;

    private final FavoriteMapper favoriteMapper;

    private final TestRepository testRepository;

    private final QuestionOneMapper questionOneMapper;

    private final QuestionFourMapper questionFourMapper;

    private final ChoiceMapper choiceMapper;

    private int questionOneCount = 0;

    private int questionFourCount = 0;

    private final int questionOneRange;
    private final int questionFourRange;

    //TODO:解决这里报错但实际可用的情况
    public QuestionService(QuestionDTOMapper questionDTOMapper,  MistakeMapper mistakeMapper,
                           PracticeStatusMapper practiceStatusMapper,
                           UserService userService,
                           FavoriteMapper favoriteMapper,
                           TestRepository testRepository,
                           QuestionOneMapper questionOneMapper,
                            QuestionFourMapper questionFourMapper,
                           ChoiceMapper choiceMapper) {
        this.questionDTOMapper = questionDTOMapper;
        this.mistakeMapper = mistakeMapper;
        this.practiceStatusMapper = practiceStatusMapper;
        this.userService = userService;
        this.favoriteMapper = favoriteMapper;
        this.testRepository = testRepository;
        this.questionOneRange = this.getQuestionCount(1) + 1;
        this.questionFourRange = this.getQuestionCount(4) + 1;
        this.questionOneMapper = questionOneMapper;
        this.questionFourMapper = questionFourMapper;
        this.choiceMapper = choiceMapper;
    }

    //    public List<QuestionDTO> getBatchQuestions(int id, String category) {
//        List<QuestionDTO> batchQuestions = questionDTOMapper.getBatchQuestions(id, category);
//        if (batchQuestions.size() == 0) {
//            throw new APIException(ResultCode.QUESTIONS_GET_FAILED);
//        } else {
//            return batchQuestions;
//        }
//    }

    @PostConstruct
    public void init() {
        //调用相应的mapper，查询出题库中题目的数量
        questionOneCount = questionDTOMapper.getQuestionOneCount();
        questionFourCount = questionDTOMapper.getQuestionFourCount();

    }


    public ResponseVO addMistakes(String userId,List<Mistake> mistakes) {
        for (Mistake mistake : mistakes) {
            try {
                mistake.setUserId(userId);
                this.save(mistake);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }

        return new ResponseVO<>(ResultCode.SUCCESS);
    }

    public List<QuestionDTO> getQuestionList(List<Map<String, String>> params) {
        List<QuestionDTO> questionOneDTOList = new ArrayList<>();
        List<QuestionDTO> questionFourDTOList = new ArrayList<>();
        List<QuestionDTO> totalList = new ArrayList<>();
        for (Map<String, String> param : params) {
            int questionId = Integer.parseInt(param.get("questionId"));
            int category = Integer.parseInt(param.get("category"));
            if (category == 1) {
                questionOneDTOList.add(questionDTOMapper.getQuestionOneDTOById(questionId));
            }
            if (category == 4) {
                questionFourDTOList.add(questionDTOMapper.getQuestionFourDTOById(questionId));
            }
        }
        totalList.addAll(questionOneDTOList);
        totalList.addAll(questionFourDTOList);
        return totalList;
    }

    public List<QuestionDTO> getQuestionBatch(int questionId, int category, int count) {
        log.info("getQuestionBatch: questionId={}, category={}, count={}", questionId, category, count);
        if (category == 1) {
            return questionDTOMapper.getQuestionOneDTOBatch(questionId, count);
        }
        if (category == 4) {
            return questionDTOMapper.getQuestionFourDTOBatch(questionId, count);
        }
        return null;
    }

    public List<MistakeDTO> getMistakes(String userId, int category) {
        log.info("userId:{},category:{}", userId, category);
        QueryWrapper<Mistake> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .eq("category", category);

        List<Mistake> mistakeList = mistakeMapper.selectList(queryWrapper);
        log.info("mistakeList: {}", mistakeList);
        List<MistakeDTO> mistakeDTOList = new ArrayList<>();
        for (Mistake mistake : mistakeList) {
            mistakeDTOList
                    .add(new MistakeDTO(mistake.getQuestionId(), mistake.getWrongChoice()));
        }
        return mistakeDTOList;
    }

    public ResponseVO deleteMistake(String userId, int questionId, int category) {
        this.remove(new QueryWrapper<Mistake>()
                .eq("user_id", userId)
                .eq("question_id", questionId)
                .eq("category", category));
        return new ResponseVO<>(ResultCode.SUCCESS);
    }


    public ResponseVO addFavorite(Favorite favorite) {
        try {
            favoriteMapper.insert(favorite);
        } catch (Exception e) {
            throw new APIException(ResultCode.FAVORITE_POST_FAILED);
        }
        return new ResponseVO<>(ResultCode.SUCCESS);
    }

    public ResponseVO addTestResult(Test test) {
        testRepository.insert(test);
        return new ResponseVO<>(ResultCode.SUCCESS);
    }

    public Test getTestResult(String userId, int testId, int category) {
//        return testRepository.findByUserIdAndTestIdAndCategory(userId, testId, category);
        //TODO:记得修改
        return null;
    }

    public ResponseVO addPracticeStatus(String userId, int questionId, int category) {
        //先判断是否存在，如果存在就不用管了
        //TODO:错题和收藏可能也用同样的问题，记得改
        PracticeStatus practiceStatus = practiceStatusMapper
                .findByUserIdAndQuestionIdAndCategory(userId, questionId, category);
        if (practiceStatus == null) {
            practiceStatusMapper.insert(new PracticeStatus(userId, category, (long) questionId));
        }
        return new ResponseVO<>(ResultCode.SUCCESS);
    }

    public List<Long> getFavorites(String userId, int category) {
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("question_id")
                .eq("user_id", userId)
                .eq("category", category);

        List<Long> questionIds = favoriteMapper.selectList(queryWrapper).stream().map(Favorite::getQuestionId).collect(Collectors.toList());
        return questionIds;
    }

    public ResponseVO deleteFavorite(String userId, int questionId, int category) {
        favoriteMapper.delete(new QueryWrapper<Favorite>()
                .eq("user_id", userId)
                .eq("question_id", questionId)
                .eq("category", category));
        return new ResponseVO<>(ResultCode.SUCCESS);
    }

    public int getQuestionCount(int category) {
        if (category == 1) {
            return questionDTOMapper.getQuestionOneCount();
        }
        if (category == 4) {
            return questionDTOMapper.getQuestionFourCount();
        }
        return 0;
    }


    public StatusDTO getPracticeStatus(String userId) {
//        log.info("questionOneCount: " + questionOneCount);
//        log.info("questionFourCount: " + questionFourCount);

        //先获取用户的category
        int category = userService.getCategory(userId);

        String role = userService.getUserRole(userId);
        if (role.equals("teacher")){
            category=1;
        }





//        log.info("category: " + category);
        //获取题库总数
        int questionCount = 0;
        if (category == 1) {
            questionCount = this.questionOneCount;
        } else {
            questionCount = this.questionFourCount;
        }
        //获取用户已经做过的题目数
        int doneCount = practiceStatusMapper.getCountByUserId(userId);

        return new StatusDTO(category, doneCount, questionCount);

    }

    public List<QuestionDTO> generateTest(int category, int count) {
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
        List<Integer> list = new ArrayList<>(questionIdSet);
        log.info("list: " + list);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        if (category == 1) {
            for (int i : list) {
                questionDTOList.add(questionDTOMapper.getQuestionOneDTOById(i));
            }
        }
        if (category == 4) {
            for (int i : list) {
                questionDTOList.add(questionDTOMapper.getQuestionFourDTOById(i));
            }
        }
        log.info("questionDTOList: " + questionDTOList);
        return questionDTOList;

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

    public ResponseVO importQuestion(int category, List<QuestionVO> questions) {
        //先处理题目
        Long id;
        for (QuestionVO questionVO : questions) {
            if (category == 1) {

                QuestionOne question = new QuestionOne();
                question.setImgContent(questionVO.getImgContent());
                question.setTextContent(questionVO.getTextContent());
                question.setChoiceNumber(questionVO.getChoiceNumber());
                question.setAnswer(questionVO.getAnswer());
                question.setExplanation(questionVO.getExplanation());
                questionOneMapper.insert(question);
                id=question.getId();
            }else{
                QuestionFour question = new QuestionFour();
                question.setImgContent(questionVO.getImgContent());
                question.setTextContent(questionVO.getTextContent());
                question.setChoiceNumber(questionVO.getChoiceNumber());
                question.setAnswer(questionVO.getAnswer());
                question.setExplanation(questionVO.getExplanation());
                questionFourMapper.insert(question);
                id=question.getId();
            }
            List<String> choices = questionVO.getChoices();

            //再处理选项
            if (questionVO.getChoiceNumber()==2){
                Choice choice1 = new Choice();
                choice1.setContent("正确");
                choice1.setQuestionId(id);
                choice1.setCategory(category);
                choice1.setOrderOfChoice(1);
                choiceMapper.insert(choice1);
                Choice choice2 = new Choice();
                choice2.setContent("错误");
                choice2.setQuestionId(id);
                choice2.setCategory(category);
                choice2.setOrderOfChoice(2);
                choiceMapper.insert(choice2);
            }
            else {
                int i=1;
                for (String content : choices) {
                    Choice realChoice = new Choice();
                    realChoice.setContent(content);
                    realChoice.setQuestionId(id);
                    realChoice.setCategory(category);
                    realChoice.setOrderOfChoice(i);
                    i++;
                    //TODO:对图像的处理？
                    choiceMapper.insert(realChoice);
            }


            }




        }




        return new ResponseVO<>(ResultCode.SUCCESS);
    }
}//End of the class

