package com.kkkzoz.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题库(Question)表实体类
 *
 * @author makejava
 * @since 2022-04-25 13:56:21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    
    private Long id;
    
    private String category;
    
    private String textContent;
    
    private String imgContent;
    
    private Integer choiceNumber;
    
    private String answers;
    
    private String explanation;
}

