package com.kkkzoz.dto;

import com.kkkzoz.domain.entity.Choice;
import com.kkkzoz.domain.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO extends Question {

    private List<Choice> choices;
}
