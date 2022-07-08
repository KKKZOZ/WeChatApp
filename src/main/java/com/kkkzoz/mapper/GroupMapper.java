package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.entity.Group;
import org.springframework.stereotype.Component;

@Component("groupMapper")
public interface GroupMapper extends BaseMapper<Group> {
    Group findByStudentId(String userId);
}
