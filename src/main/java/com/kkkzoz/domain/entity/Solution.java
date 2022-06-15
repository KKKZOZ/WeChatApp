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

    private String teacherId;

    private String teacherName;

    private int category;

    private LocalDate localDate;

    private String licenseNumber;


    private int weekday;

    private int weekOfYear;

    private List<Segment> segments;
}
