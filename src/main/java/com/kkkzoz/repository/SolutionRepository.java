package com.kkkzoz.repository;

import com.kkkzoz.domain.entity.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SolutionRepository
        extends MongoRepository<Solution, String> {
    List<Solution> findByCategoryAndTeacherIdAndWeekOfYear(int category, int teacherId,int weekOfYear);

    List<Solution> findByTeacherIdAndWeekOfYear(int teacherId,int weekOfYear);
}
