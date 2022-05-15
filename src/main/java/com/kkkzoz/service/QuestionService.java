package com.kkkzoz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkkzoz.domain.entity.Favorite;
import com.kkkzoz.domain.entity.Mistake;
import com.kkkzoz.domain.entity.Test;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.global.APIException;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.FavoriteMapper;
import com.kkkzoz.mapper.MistakeMapper;
import com.kkkzoz.mapper.QuestionDTOMapper;
import com.kkkzoz.repository.TestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService extends ServiceImpl<MistakeMapper, Mistake> {

    private final QuestionDTOMapper questionDTOMapper;
    private final MistakeMapper mistakeMapper;

    private final FavoriteMapper favoriteMapper;

    private final TestRepository testRepository;


//    public List<QuestionDTO> getBatchQuestions(int id, String category) {
//        List<QuestionDTO> batchQuestions = questionDTOMapper.getBatchQuestions(id, category);
//        if (batchQuestions.size() == 0) {
//            throw new APIException(ResultCode.QUESTIONS_GET_FAILED);
//        } else {
//            return batchQuestions;
//        }
//    }

    public void addMistake(Mistake mistake) {

        mistakeMapper.insert(mistake);

    }

    public ResponseVO addMistakes(List<Mistake> mistakes) {
        for (Mistake mistake : mistakes) {
            try {
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
        if (category == 1) {
            return questionDTOMapper.getQuestionOneDTOBatch(questionId, count);
        }
        if (category == 4) {
            return questionDTOMapper.getQuestionFourDTOBatch(questionId, count);
        }
        return null;
    }

    public List<Long> getMistakes(int userId, int category) {
        QueryWrapper<Mistake> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("question_id")
                .eq("user_id", userId)
                .eq("category", category);

        List<Long> questionIds = this.list(queryWrapper).stream().map(Mistake::getQuestionId).collect(Collectors.toList());
        return questionIds;
    }

    public ResponseVO deleteMistake(int userId, int questionId, int category) {
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

    public Test getTestResult(int userId, int testId, int category) {
        return testRepository.findByUserIdAndTestIdAndCategory(userId, testId, category);
    }

    public ResponseVO addPracticeStatus(int userId, int questionId, int category) {
        //TODO: 细节待讨论
        return null;
    }

    public List<Long> getFavorites(int userId, int category) {
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("question_id")
                .eq("user_id", userId)
                .eq("category", category);

        List<Long> questionIds = favoriteMapper.selectList(queryWrapper).stream().map(Favorite::getQuestionId).collect(Collectors.toList());
        return questionIds;
    }

    public ResponseVO deleteFavorite(int userId, int questionId, int category) {
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


}//End of the class

