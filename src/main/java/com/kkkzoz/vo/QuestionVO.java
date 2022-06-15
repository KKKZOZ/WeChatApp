package com.kkkzoz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionVO {

    private String textContent;

    private String imgContent;

    private Integer choiceNumber;

    private int answer;

    private String explanation;

    private String tags;

    private List<String> choices;
}
