package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository
        extends MongoRepository<Test, String> {
    //复杂的可以使用MongoTemplate + Query解决


    Test findByUserIdAndTestIdAndCategory(int userId, int testId, int category);
}
