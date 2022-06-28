package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.QueueItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QueueRepository
        extends MongoRepository<QueueItem, String> {


    List<QueueItem> findByCategory(int category);

    Long deleteByUserId(String userId);

}
