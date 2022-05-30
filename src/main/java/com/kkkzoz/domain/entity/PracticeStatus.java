package com.kkkzoz.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeStatus {
    
    private Long id;
    
    private Long userId;
    
    private Integer category;
    
    private Long questionId;


    public PracticeStatus(Long userId, Integer category, Long questionId) {
        this.userId = userId;
        this.category = category;
        this.questionId = questionId;
    }
}

