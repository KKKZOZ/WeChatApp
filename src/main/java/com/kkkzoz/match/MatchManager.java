package com.kkkzoz.match;


import com.kkkzoz.controller.WebSocketServer;
import com.kkkzoz.domain.entity.QueueItem;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.repository.QueueRepository;
import com.kkkzoz.vo.ForwardingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MatchManager {

    private final QueueRepository queueRepository;

    private final WebSocketServer webSocketServer;



    private static final int MAX_MATCH_SIZE = 5;

    private int count = 0;

//    Key:userId, Value:相应handler在list中的index
    private Map<String,Integer> correspondingMap;

    private List<MatchHandler> handlers;

    public MatchManager(QueueRepository queueRepository,WebSocketServer webSocketServer) {
        this.queueRepository = queueRepository;
        this.webSocketServer = webSocketServer;
        this.correspondingMap = new HashMap<>();
        this.handlers = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        log.info("删除queueItem中所有数据");
        queueRepository.deleteAll();
    }


    public void addUserToQueue(QueueItem queueItem) {
        log.info("addUserToQueue :{}",queueItem);
        queueRepository.save(queueItem);
        log.info("checkQueue");
        checkQueue(queueItem.getCategory());
    }


    public void checkQueue(int category) {
        //TODO:考虑同步的问题
        synchronized (this){
            List<QueueItem> queueItems = queueRepository.findByCategory(category);
            while(queueItems.size()>=2){
                log.info("正在匹配");
                QueueItem queueItem1 = queueItems.get(0);
                QueueItem queueItem2 = queueItems.get(1);
                //创建新的handler
                log.info("UserAId:{}   UserBId:{}",queueItem1.getUserId(),queueItem2.getUserId());
                log.info("创建新的handler");
                handlers.add(
                        new MatchHandler(
                                queueItem1.getUserId(),
                                queueItem2.getUserId(),
                                queueItem1.getCategory(),
                                MAX_MATCH_SIZE)
                );
                //把相应关系放入map中
                correspondingMap.put(queueItem1.getUserId(),count);
                correspondingMap.put(queueItem2.getUserId(),count);
                count++;
                queueItems.remove(0);
                queueItems.remove(0);
                //从数据库中删除相应队列项

                queueRepository.delete(queueItem1);
                queueRepository.delete(queueItem2);
        }


        }

    }


    public ResponseVO forwardMessage(ForwardingVO forwarding) {
        log.info("forwardMessage");
        log.info("forwarding:{}",forwarding);
        String  senderId = forwarding.getSenderId();
        String  receiverId = forwarding.getReceiverId();
        //TODO:考虑保存错题
        //转发消息
        try {
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }

        webSocketServer
                .send(receiverId,new MessageVO(MessageVO.FORWARDING,forwarding));

        //更新handler
        MatchHandler handler = handlers.get(correspondingMap.get(senderId));
        handler.changeStatus(senderId,true);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO releaseResource(int userId) {
        //TODO: 给每个handler一个ID，每次查找时用for循环，更新和删除时尝试使用异步函数
        log.info("releaseResource");
        //删除对应handler

        //删除correspondingMap中的对应数据


        return new ResponseVO(ResultCode.SUCCESS);
    }
}
