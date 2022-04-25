package com.kkkzoz.controller;


import com.kkkzoz.domain.User;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseVO login(@RequestParam User user){
        return userService.login(user);
    }

    @GetMapping("/logout")
    public ResponseVO logout(){
        return userService.logout();
    }
}
