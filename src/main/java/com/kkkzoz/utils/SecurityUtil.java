package com.kkkzoz.utils;

import com.kkkzoz.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SecurityUtil {

    public static String getUserId() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.valueOf(currentUser.getId());
    }
}
