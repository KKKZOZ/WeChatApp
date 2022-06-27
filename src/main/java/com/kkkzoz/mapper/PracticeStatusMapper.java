package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.entity.PracticeStatus;
import org.springframework.stereotype.Component;

@Component("practiceStatusMapper")
public interface PracticeStatusMapper extends BaseMapper<PracticeStatus> {

    int getCountByUserId(String userId);

    PracticeStatus findByUserIdAndQuestionIdAndCategory(String userId, int questionId, int category);
}
