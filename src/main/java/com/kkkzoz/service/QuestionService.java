package com.kkkzoz.service;

import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.mapper.QuestionDTOMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionDTOMapper questionDTOMapper;
    public List<QuestionDTO> getSingleQuestion(int id) {
        List<QuestionDTO> list = new ArrayList<>();
        list.add(questionDTOMapper.getQuestionDTOById(id));
        return list;
    }
}
