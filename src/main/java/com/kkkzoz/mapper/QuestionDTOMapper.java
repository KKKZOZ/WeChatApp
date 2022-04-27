package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.dto.QuestionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface QuestionDTOMapper extends BaseMapper<QuestionDTO> {

    public QuestionDTO getQuestionOneDTOById(int id);

    public QuestionDTO getQuestionFourDTOById(int id);

    public List<QuestionDTO> getQuestionOneDTOBatch(int questionId, int count);

    public List<QuestionDTO> getQuestionFourDTOBatch(int questionId, int count);
}
