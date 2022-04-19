package com.kkkzoz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.global.ResponseVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService ,IService<User> {
    ResponseVO login(User user);

    ResponseVO logout();
}
