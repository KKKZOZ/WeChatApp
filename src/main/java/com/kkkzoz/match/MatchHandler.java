package com.kkkzoz.match;


import com.kkkzoz.controller.WebSocketServer;
import com.kkkzoz.domain.entity.Match;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.mapper.QuestionDTOMapper;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.utils.SpringUtilsAuTo;
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

    private boolean userAStatus = false;
    private boolean userBStatus = false;

    private int progress;

    private List<Integer> questionIdList;


    public MatchHandler(String userAId, String userBId, int category, int size) {
        this.userAId = userAId;
        this.userBId = userBId;
        this.category = category;
        this.size = size;
        this.questionIdList = matchGenerator.generateQuestionList(category, size);
        this.progress = 0;

        //互相发送userId
        this.initialize();
    }

    private void sendMessage(String sender, String receiver, MessageVO message) {
        boolean isOK = webSocketServer
                .send(receiver, message);
        //如果报错，则中断这次匹配
        //1. 向幸存方发送中断消息
        //2. 中断这次匹配
        if (!isOK) {
            webSocketServer
                    .send(sender, new MessageVO<>(MessageVO.TERMINATE, null));
        }
    }

    //互相发送userId,并发送第一道题
    private void initialize() {
        //username,url


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

        sendMessage(userBId,userAId, new MessageVO<>(MessageVO.START, userBInfo));

        sendMessage(userAId,userBId, new MessageVO<>(MessageVO.START, userAInfo));


        //发送第一道题

        this.sendQuestion(userAId, userBId);
        this.sendQuestion(userBId, userAId);
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
                webSocketServer.send(userAId, new MessageVO(MessageVO.OVER, questionIdList));
                webSocketServer.send(userBId, new MessageVO(MessageVO.OVER, questionIdList));
                return;
            }

            this.sendQuestion(userAId, userBId);
            this.sendQuestion(userBId, userAId);

            this.userAStatus = false;
            this.userBStatus = false;
        }
    }


    public void sendQuestion(String receiverId,String opponentId) {
        //如果出错，就给对应方发送Terminate信号
        log.info("发送题目: userId:{}, progress:{}", receiverId, this.progress);
        boolean isOK=webSocketServer
                .send(receiverId, new MessageVO(
                        MessageVO.MATCH_CONTENT,
                        getQuestion(questionIdList.get(progress), category)));
        if(!isOK){
            webSocketServer
                    .send(opponentId, new MessageVO<>(MessageVO.TERMINATE, null));
        }
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
}
