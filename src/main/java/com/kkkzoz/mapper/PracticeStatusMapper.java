package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.entity.PracticeStatus;

public interface PracticeStatusMapper extends BaseMapper<PracticeStatus> {
    //TODO:测试这里是int还是long有没有影响
    int getCountByUserId(int userId);
}
