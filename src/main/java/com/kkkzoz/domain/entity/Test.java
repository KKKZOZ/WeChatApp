package com.kkkzoz.domain.entity;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Test {
    @Id
    private String id;
    private int userId;
    private int testId;
    private int category;
    private int score;
    private List<QA> questions;
}
