package com.kkkzoz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kkkzoz.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserMapper extends BaseMapper<User> {
    public default UserDetails findByUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        return this.selectOne(queryWrapper);
    }
}
