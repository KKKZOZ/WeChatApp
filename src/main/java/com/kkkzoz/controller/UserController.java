package com.kkkzoz.controller;


import com.kkkzoz.domain.entity.User;
import com.kkkzoz.dto.UserDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.service.UserService;
import com.kkkzoz.utils.SecurityUtil;
import com.kkkzoz.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseVO login(@RequestParam String code){
        return userService.login(code);
    }

    @GetMapping("/logout")
    public ResponseVO logout(){
        return userService.logout();
    }

    @PostMapping("/info")
    public ResponseVO setUserInfo(@RequestBody UserVO userVO){
        String userId = SecurityUtil.getUserId();
        return userService.setUserInfo(userId,userVO);
    }

    @PostMapping("/basicInfo")
    public ResponseVO setUserBasicInfo(@RequestBody Map<String,String> params){
        String userId = SecurityUtil.getUserId();
        String userName = params.get("userName");
        String avatarUrl = params.get("avatarUrl");
        return userService.setUserBasicInfo(userId,userName,avatarUrl);

    }


    @GetMapping("/info")
    public UserDTO getUserInfo(){
        String userId = SecurityUtil.getUserId();
        return userService.getUserInfo(userId);
    }

    @GetMapping("/invitationCode")
    public int getInvitationCode(){
        String userId = SecurityUtil.getUserId();
        return userService.getInvitationCode(userId);
    }

    @GetMapping("/join")
    public ResponseVO joinGroup(@RequestParam("code") int code){

        String userId = SecurityUtil.getUserId();
        return userService.joinGroup(userId,code);
    }

    @GetMapping("/teacherInfo")
    @ApiOperation(value="获取老师信息")
    public Map<String,String> getTeacherInfo(){
        String userId = SecurityUtil.getUserId();
        return userService.getTeacherInfo(userId);
    }

}
