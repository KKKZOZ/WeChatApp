package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.dto.QuestionDTO;

public interface QuestionDTOMapper extends BaseMapper<QuestionDTO> {

    public QuestionDTO getQuestionDTOById(int id);
}
