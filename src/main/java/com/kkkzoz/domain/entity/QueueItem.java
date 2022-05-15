package com.kkkzoz.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class QueueItem {

    private int category;

    private int userId;

}
