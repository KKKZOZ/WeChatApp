package com.kkkzoz.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    private Long id;

    private String userId;

    private Long questionId;

    private Integer category;
}

