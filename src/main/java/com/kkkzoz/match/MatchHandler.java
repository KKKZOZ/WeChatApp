package com.kkkzoz.match;


import com.alibaba.fastjson.JSON;
import com.kkkzoz.controller.WebSocketServer;
import com.kkkzoz.domain.entity.Match;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.mapper.QuestionDTOMapper;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.utils.SpringUtilsAuTo;
import io.goeasy.GoEasy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Slf4j
public class MatchHandler {

    private final MatchGenerator matchGenerator = SpringUtilsAuTo.getBean(MatchGenerator.class);

    private final WebSocketServer webSocketServer = SpringUtilsAuTo.getBean(WebSocketServer.class);

    private final QuestionDTOMapper questionDTOMapper = SpringUtilsAuTo.getBean(QuestionDTOMapper.class);

    private final UserMapper userMapper = SpringUtilsAuTo.getBean(UserMapper.class);

    private Match match;

    private int category;

    private int size;

    private String userAId;
    private String userBId;

    private String userAUserName;

    private String userBUserName;

    private boolean userAStatus = false;
    private boolean userBStatus = false;

    private int progress;

    private List<Integer> questionIdList;

    private GoEasy goEasy;


    public MatchHandler(String userAId, String userBId, int category, int size) {
        this.userAId = userAId;
        this.userBId = userBId;
        this.category = category;
        this.size = size;
        this.questionIdList = matchGenerator.generateQuestionList(category, size);
        this.progress = 0;


        this.userAUserName = userMapper.selectById(userAId).getUsername();
        this.userBUserName = userMapper.selectById(userBId).getUsername();

        this.goEasy= new GoEasy
                ("https://rest-hangzhou.goeasy.io", "BC-69353f4a5e76404d8a2c5dd26600654e");

//        //互相发送userId
//        this.initialize();

        //互相发送userId

    }

    private void sendMessage(String sender, String receiver, MessageVO message) {
        String content = JSON.toJSONString(message);
        goEasy.publish(receiver, content);
    }

    //互相发送userId,并发送第一道题
    public void initialize() {
        //username,url
        log.info("initialize");
        log.info("userAId:{},userBId:{}", userAId, userBId);
        log.info("userAUserName:{},userBUserName:{}", userAUserName, userBUserName);


        Map<String, String> userAInfo = new HashMap<>();
        Map<String, String> userBInfo = new HashMap<>();


        User userA = userMapper.selectById(userAId);
        User userB = userMapper.selectById(userBId);

        userAInfo.put("userId", userAId);
        userAInfo.put("username", userA.getUsername());
        userAInfo.put("url", userA.getAvatarUrl());

        userBInfo.put("userId", userBId);
        userBInfo.put("username", userB.getUsername());
        userBInfo.put("url", userB.getAvatarUrl());

        sendMessage(userBUserName,userAUserName, new MessageVO<>(MessageVO.START, userBInfo));

        sendMessage(userAUserName,userBUserName, new MessageVO<>(MessageVO.START, userAInfo));


        //发送第一道题
        log.info("发送第一道题");
        this.sendQuestion(userAUserName);
        this.sendQuestion(userBUserName);
    }


    private QuestionDTO getQuestion(int questionId, int category) {
        //TODO: 在questionIdList确定后，就把对应的questionDTO全部查到并保存下来，减少数据库访问
        if (category == 1) {
            return questionDTOMapper.getQuestionOneDTOById(questionId);
        }
        if (category == 4) {
            return questionDTOMapper.getQuestionFourDTOById(questionId);
        }
        return null;
    }

    public void checkProgress() {
        //如果两个人都答完了，则进入下一题
        if (userAStatus && userBStatus) {
            this.progress++;
            if (this.progress == this.size) {
                log.info("比赛结束");
                //比赛结束


                sendMessage(userAUserName,userBUserName, new MessageVO(MessageVO.OVER, questionIdList));
                sendMessage(userBUserName,userAUserName, new MessageVO(MessageVO.OVER, questionIdList));
                return;
            }

            this.sendQuestion(userAUserName);
            this.sendQuestion(userBUserName);

            this.userAStatus = false;
            this.userBStatus = false;
        }
    }


    public void sendQuestion(String receiver) {
        //TODO:如果出错，就给对应方发送Terminate信号
        log.info("发送题目: user:{}, progress:{}", receiver, this.progress);
        String content = JSON.toJSONString(new MessageVO(
                MessageVO.MATCH_CONTENT,
                getQuestion(questionIdList.get(progress), category)));
        goEasy.publish(receiver,content);

    }


    public void changeStatus(String userId, boolean status) {
        if (Objects.equals(userId, userAId)) {
            this.setUserAStatus(status);
        }
        if (Objects.equals(userId, userBId)) {
            this.setUserBStatus(status);
        }
    }


    public void setUserAStatus(boolean userAStatus) {
        log.info("userAStatus:{}", userAStatus);
        this.userAStatus = userAStatus;
        this.checkProgress();
    }

    public void setUserBStatus(boolean userBStatus) {
        log.info("userBStatus:{}", userBStatus);
        this.userBStatus = userBStatus;
        this.checkProgress();
    }

    public String getOpponentId(String userId){
        if (Objects.equals(userId, userAId)) {
            return userBId;
        }else {
            return userAId;
        }
    }
}
