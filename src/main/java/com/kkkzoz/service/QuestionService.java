package com.kkkzoz.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkkzoz.domain.entity.Mistake;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.global.APIException;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.MistakeMapper;
import com.kkkzoz.mapper.QuestionDTOMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService extends ServiceImpl<MistakeMapper, Mistake> {

    private final QuestionDTOMapper questionDTOMapper;
    private final MistakeMapper mistakeMapper;


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
}
