package com.kkkzoz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkkzoz.domain.entity.Group;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.mapper.GroupMapper;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.utils.JwtUtil;
import com.kkkzoz.utils.RedisCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kkkzoz.global.ResultCode.*;
@Slf4j
@Service
@AllArgsConstructor

//Spring Security默认优先使用自己实现的UserDetailsService
public class UserService extends ServiceImpl<UserMapper, User> implements UserDetailsService {

    private final UserMapper userMapper;

    private final GroupMapper groupMapper;
    private final AuthenticationManager authenticationManager;
    private final RedisCache redisCache;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.findByUsername(username);
    }


    public ResponseVO login(User user) {
        //获取AuthenticationManager authenticate 进行认证

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);

        if (!authenticate.isAuthenticated()) {
            return new ResponseVO(LOGIN_FAILED,null);
        }


        //如果认证通过，使用userid生成一个jwt,jwt 存入ResponseResult中并返回
        User loginUser = (User)authenticate.getPrincipal();
        String userid = loginUser.getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        //把完整的用户信息存入redis, userid作为key
        redisCache.setCacheObject("login:"+userid, loginUser);
        log.info("login userid:{}",userid);
        return new ResponseVO(LOGIN_SUCCESS, map);
    }


    public ResponseVO logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        User loginUser = (User)authentication.getPrincipal();
        String userid = loginUser.getId().toString();

        //删除redis中的值
        redisCache.deleteObject("login:"+userid);
        log.info("logout userid:{}",userid);
        return new ResponseVO(LOGOUT_SUCCESS,null);
    }

    public int getCategory(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("category")
                .eq("id", userId);
        return userMapper.selectOne(queryWrapper).getCategory();
    }

    public Long getTeacherId(Long userId) {
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("teacher_id")
                .eq("student_id", userId);
        return groupMapper.selectOne(queryWrapper).getTeacherId();
    }

    public String getUserRole(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("role")
                .eq("id", userId);
        return userMapper.selectOne(queryWrapper).getRole();
    }
}