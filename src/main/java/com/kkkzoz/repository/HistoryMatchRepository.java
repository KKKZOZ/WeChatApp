package com.kkkzoz.repository;


import com.kkkzoz.domain.entity.HistoryMatchItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryMatchRepository
        extends MongoRepository<HistoryMatchItem, String> {

    List<HistoryMatchItem> findByUserId(String userId);
}
