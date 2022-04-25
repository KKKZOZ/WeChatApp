package com.kkkzoz.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    
    private Long id;
    
    private Long questionId;
    
    private String content;
    
    private Integer orderOfChoice;
    
    private Boolean isImage;
}

