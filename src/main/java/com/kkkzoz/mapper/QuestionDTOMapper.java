package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.dto.QuestionDTO;

import java.util.List;


public interface QuestionDTOMapper extends BaseMapper<QuestionDTO> {

    QuestionDTO getQuestionOneDTOById(int id);

    QuestionDTO getQuestionFourDTOById(int id);

    List<QuestionDTO> getQuestionOneDTOBatch(int questionId, int count);

    List<QuestionDTO> getQuestionFourDTOBatch(int questionId, int count);

    int getQuestionOneCount();

    int getQuestionFourCount();
}
