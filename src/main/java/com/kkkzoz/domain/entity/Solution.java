package com.kkkzoz.domain.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document
public class Solution {
    @Id
    private String solutionId;

    private int teacherId;

    private int category;

    private String localDate;

    private int weekday;

    //TODO:对weekOfYear进行相应调整，并且加上TimeUtil
    private int weekOfYear;

    private List<Segment> segments;
}
