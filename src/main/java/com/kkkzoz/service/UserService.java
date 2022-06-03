package com.kkkzoz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kkkzoz.domain.entity.Group;
import com.kkkzoz.domain.entity.Test;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.dto.UserDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.GroupMapper;
import com.kkkzoz.mapper.PracticeStatusMapper;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.repository.TestRepository;
import com.kkkzoz.utils.JwtUtil;
import com.kkkzoz.utils.RedisCache;
import com.kkkzoz.vo.OpenIdVO;
import com.kkkzoz.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.kkkzoz.global.ResultCode.*;

@Slf4j
@Service
@AllArgsConstructor

//Spring Security默认优先使用自己实现的UserDetailsService
public class UserService extends ServiceImpl<UserMapper, User> implements UserDetailsService {


    private static final String APPID = "wx6bb8dfbe50ce48b5";

    //TODO:加密
    private static final String APP_SECRET = "c47575178717fd88676203e13c3f6bd3";

    private static final String AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session";


    //https://api.weixin.qq.com/sns/jscode2session
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final AuthenticationManager authenticationManager;

    private final PracticeStatusMapper practiceStatusMapper;

    private final TestRepository testRepository;
    private final RedisCache redisCache;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.findByUsername(username);
    }

    private String fetchFromServer(String code) throws IOException {
//        log.info("fetchFromServer code:{}",code);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = AUTH_URL + "?appid=" + APPID + "&secret="
                + APP_SECRET + "&js_code=" + code + "&grant_type=authorization_code";
//        log.info("url: {}", url);
        HttpGet get = new HttpGet(url);
        HttpResponse httpresponse = null;
        try {
            httpresponse = httpclient.execute(get);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream stream = httpresponse.getEntity().getContent();
        String result = new String(stream.readAllBytes());
        JSONObject object = (JSONObject) JSON.parse(result);
        log.info("openid:{}", object.get("openid"));

        return (String) object.get("openid");
    }

    //整个流程(第一次登录，JWT没有启动)
    //1. 获取code
    //2. 获取openid
    //3. 查看是否存在此用户
    //4. 如果存在，就返回一个JWT
    //5. 如果不存在，就新建一个用户，返回一个JWT



    public ResponseVO login(String code) {
        boolean isNewUser = false;
        String openId = null;
        try {
           openId = fetchFromServer(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = userMapper.selectById(openId);

        if (Objects.isNull(user)) {
            //如果为空，则新建一个用户,其他信息为空，等待前端调用其他接口来填写
            isNewUser = true;
            user = new User(openId);
            userMapper.insert(user);
        }else {
            //如果不为空，则直接改变SecurityContextHolder中的用户
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, null,null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        //使用openId生成一个jwt,jwt 存入ResponseResult中并返回
        ;
        String jwt = JwtUtil.createJWT(openId);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        map.put("isNewUser", String.valueOf(isNewUser));

        //把完整的用户信息存入redis, userid作为key
        redisCache.setCacheObject("login:" + openId, user);
        log.info("login userid:{}", openId);
        return new ResponseVO(LOGIN_SUCCESS, map);
    }


    public ResponseVO logout() {
        //TODO
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        User loginUser = (User) authentication.getPrincipal();
        String userid = loginUser.getId().toString();

        //删除redis中的值
        redisCache.deleteObject("login:" + userid);
        log.info("logout userid:{}", userid);
        return new ResponseVO(LOGOUT_SUCCESS, null);
    }

    public int getCategory(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("category")
                .eq("id", userId);
        return userMapper.selectOne(queryWrapper).getCategory();
    }

    public String getTeacherIdByUserId(String userId) {
        //先判断这个Id是不是老师
        //如果是的话，就直接返回
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        User user = userMapper.selectOne(wrapper);
        if (Objects.equals(user.getRole(), "teacher")) {
            log.info("userId:{} is teacher", userId);
            return userId;
        } else {
            log.info("userId:{} is student", userId);
            QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .select("teacher_id")
                    .eq("student_id", userId);
            return groupMapper.selectOne(queryWrapper).getTeacherId();
        }
    }


    public String getUserRole(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("role")
                .eq("id", userId);
        return userMapper.selectOne(queryWrapper).getRole();
    }

    public ResponseVO setUserInfo(String userId, UserVO userVO) {
        User user = userMapper.selectById(userId);

        user.setUsername(userVO.getUsername());
        user.setName(userVO.getName());
        user.setRole(userVO.getRole());
        user.setCategory(userVO.getCategory());
        user.setAvatarUrl(userVO.getAvatarUrl());
        user.setYearsOfTeaching(userVO.getYearsOfTeaching());
        user.setSchoolName(userVO.getSchoolName());
        user.setPhoneNumber(userVO.getPhoneNumber());

        //添加注册日期
        user.setRegisterDate(LocalDate.now());

        userMapper.updateById(user);
        return new ResponseVO(SUCCESS);
    }

    public UserDTO getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        int doneCount = practiceStatusMapper.getCountByUserId(userId);
        //calculate number of days between registerDate and now
        int numberOfDays = (int) (LocalDate.now().toEpochDay() - user.getRegisterDate().toEpochDay());
        List<Test> testList = testRepository.findAllByUserId(userId);
        List<Integer> scoreList = new ArrayList<>();
        for(Test test: testList) {
            scoreList.add(test.getScore());
        }
        //TODO: check?
        int averageScore = (int) scoreList.stream().mapToInt(Integer::intValue).average().orElse(0);

        UserDTO userDTO = new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getRole(),
                user.getCategory(),
                user.getAvatarUrl(),
                user.getYearsOfTeaching(),
                user.getSchoolName(),
                user.getPhoneNumber(),
                doneCount,
                numberOfDays,
                averageScore
        );

        return userDTO;
    }

    public ResponseVO setUserBasicInfo(String userId,String userName, String avatarUrl) {
        User user = userMapper.selectById(userId);
        user.setUsername(userName);
        user.setAvatarUrl(avatarUrl);
        userMapper.updateById(user);
        return new ResponseVO(SUCCESS);
    }
}
