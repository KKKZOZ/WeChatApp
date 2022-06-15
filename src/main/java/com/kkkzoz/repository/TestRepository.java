package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestRepository
        extends MongoRepository<Test, String> {
    //复杂的可以使用MongoTemplate + Query解决


    Test findByUserIdAndCategory(String userId, int category);

    List<Test> findAllByUserId(String userId);
}
