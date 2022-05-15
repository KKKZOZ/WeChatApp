package com.kkkzoz.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class HistoryMatchItem {

    private int userId;
    private int userScore;
    private int opponentId;
    private int opponentScore;
    private LocalDateTime matchTime;
    private List<Integer> questionIdList;
}
