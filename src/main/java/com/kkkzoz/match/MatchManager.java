package com.kkkzoz.match;


import com.alibaba.fastjson.JSON;
import com.kkkzoz.controller.WebSocketServer;
import com.kkkzoz.domain.entity.QueueItem;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.repository.QueueRepository;
import com.kkkzoz.vo.ForwardingVO;
import io.goeasy.GoEasy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MatchManager {

    private final QueueRepository queueRepository;

    private final WebSocketServer webSocketServer;

    private final UserMapper userMapper;


    private static final int MAX_MATCH_SIZE = 5;


    private int count = 0;

    //    Key:userId, Value:相应handler在list中的index
//    private Map<String, Integer> correspondingMap;
//
//    private Map<Integer, MatchHandler> handlers;

    private Map<String, MatchHandler> correspondingHandlers;


    private GoEasy goEasy;



    public MatchManager(QueueRepository queueRepository, WebSocketServer webSocketServer,UserMapper userMapper) {

        this.queueRepository = queueRepository;
        this.webSocketServer = webSocketServer;
//        this.correspondingMap = new HashMap<>();
//        this.handlers = new HashMap<>();
        this.correspondingHandlers = new HashMap<>();

        this.userMapper = userMapper;
        this.goEasy = new GoEasy
                ("https://rest-hangzhou.goeasy.io", "BC-69353f4a5e76404d8a2c5dd26600654e");

    }

    @PostConstruct
    public void init() {
        log.info("删除queueItem中所有数据");
        queueRepository.deleteAll();
    }


    public void addUserToQueue(QueueItem queueItem) {
        log.info("addUserToQueue :{}", queueItem);
        queueRepository.save(queueItem);
        checkQueue(queueItem.getCategory());
        log.info("checkQueue over");
    }


    public void checkQueue(int category) {
        //TODO:考虑同步的问题
        synchronized (this) {
            List<QueueItem> queueItems = queueRepository.findByCategory(category);
            while (queueItems.size() >= 2) {
                log.info("正在匹配");
                QueueItem queueItem1 = queueItems.get(0);
                QueueItem queueItem2 = queueItems.get(1);
                //创建新的handler
                log.info("UserAId:{}   UserBId:{}", queueItem1.getUserId(), queueItem2.getUserId());
                log.info("创建新的handler");
                MatchHandler handler = new MatchHandler(
                        queueItem1.getUserId(),
                        queueItem2.getUserId(),
                        queueItem1.getCategory(),
                        MAX_MATCH_SIZE);
//                handlers.put(count, handler);
//                //把相应关系放入map中
//                correspondingMap.put(queueItem1.getUserId(), count);
//                correspondingMap.put(queueItem2.getUserId(), count);

                correspondingHandlers.put(queueItem1.getUserId(), handler);
                correspondingHandlers.put(queueItem2.getUserId(), handler);

                count++;
                queueItems.remove(0);
                queueItems.remove(0);
                //从数据库中删除相应队列项
                queueRepository.delete(queueItem1);
                queueRepository.delete(queueItem2);
                handler.initialize();
            }


        }

    }


    public ResponseVO forwardMessage(ForwardingVO forwarding) {
        log.info("forwardMessage");
        log.info("forwarding:{}", forwarding);
        String senderId = forwarding.getSenderId();
        String receiverId = forwarding.getReceiverId();

        String receiverUsername = userMapper.selectById(receiverId).getUsername();

        //TODO:考虑保存错题
        //转发消息
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String content = JSON.toJSONString(new MessageVO(MessageVO.FORWARDING, forwarding));

        goEasy.publish(receiverUsername,content);

//        webSocketServer
//                .send(receiverId, new MessageVO(MessageVO.FORWARDING, forwarding));



        //更新handler
//        MatchHandler handler = handlers.get(correspondingMap.get(senderId));
        MatchHandler handler = correspondingHandlers.get(senderId);
        handler.changeStatus(senderId, true);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    //    public ResponseVO releaseResource(String userId) {
//        log.info("releaseResource");
//        //删除对应handler
//        Integer index = correspondingMap.get(userId);
//        if (index == null) {
//            correspondingMap.remove(userId);
//            return new ResponseVO(ResultCode.SUCCESS);
//        }
//        handlers.remove(index);
//        //删除correspondingMap中的对应数据
//        correspondingMap.remove(userId);
//        return new ResponseVO(ResultCode.SUCCESS);
//    }
    public ResponseVO releaseResource(String userId) {
        log.info("releaseResource");
        correspondingHandlers.remove(userId);

        return new ResponseVO(ResultCode.SUCCESS);
    }

    public void notifyOnClose(String userId) {
        //userId为主动关闭webSocket的那方
        String opponentId = correspondingHandlers.get(userId).getOpponentId(userId);
        log.info("notifyOnClose  userId:{}", userId);
        webSocketServer
                .send(opponentId, new MessageVO(MessageVO.TERMINATE, null));
    }

    public ResponseVO removeUserFromQueue(String userId) {
        log.info("removeUserFromQueue");
        queueRepository.deleteByUserId(userId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO removeUserFromMatch(String userId) {
        log.info("removeUserFromMatch");
        MatchHandler handler = correspondingHandlers.get(userId);
        String opponentId = handler.getOpponentId(userId);
        //先删除自己的记录
        correspondingHandlers.remove(userId);
        if (correspondingHandlers.get(opponentId) != null) {
            //如果自己先退出，则通知对方进行terminate
            String opponentUsername = userMapper.selectById(opponentId).getUsername();
            goEasy.publish(opponentUsername, new MessageVO(MessageVO.TERMINATE, null).toString());

        }
        return new ResponseVO(ResultCode.SUCCESS);
    }
}

        return new ResponseVO(ResultCode.SUCCESS);
    }

    public void notifyOnClose(String userId) {
        //userId为主动关闭webSocket的那方
        String opponentId = correspondingHandlers.get(userId).getOpponentId(userId);
        log.info("notifyOnClose  userId:{}", userId);
        webSocketServer
                .send(opponentId, new MessageVO(MessageVO.TERMINATE, null));
    }
}

