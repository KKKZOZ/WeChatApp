package com.kkkzoz.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long questionId;

    private int category;
    
    private String content;
    
    private Integer orderOfChoice;
    
    private Boolean isImage;
}

