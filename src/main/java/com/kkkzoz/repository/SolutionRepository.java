package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SolutionRepository
        extends MongoRepository<Solution, String> {
    List<Solution> findByCategoryAndTeacherId(int category, int teacherId);

    List<Solution> findByTeacherId(int teacherId);
}
