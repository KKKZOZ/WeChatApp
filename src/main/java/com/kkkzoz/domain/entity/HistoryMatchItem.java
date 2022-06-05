package com.kkkzoz.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class HistoryMatchItem {

    @Id
    private String id;

    private String userId;

    private int userScore;

    private String opponentId;

    private int opponentScore;

    private String matchTime;

//    "2020-12-08 17:30:50"
    private List<Integer> questionIdList;

}
