package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SolutionRepository
        extends MongoRepository<Solution, String> {
    List<Solution> findByCategoryAndTeacherIdAndWeekOfYear(int category, String teacherId,int weekOfYear);

    List<Solution> findByTeacherIdAndWeekOfYear(String teacherId,int weekOfYear);
}
