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

import java.util.ArrayList;
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
    private Map<Integer,Integer> correspondingMap;

    private List<MatchHandler> handlers;

    public MatchManager(QueueRepository queueRepository,WebSocketServer webSocketServer) {
        this.queueRepository = queueRepository;
        this.webSocketServer = webSocketServer;
        this.handlers = new ArrayList<>();
    }


    public void addUserToQueue(QueueItem queueItem) {
        queueRepository.save(queueItem);
        checkQueue(queueItem.getCategory());
    }


    public void checkQueue(int category) {
        //TODO:考虑同步的问题
        List<QueueItem> queueItems = queueRepository.findByCategory(category);
        while(queueItems.size()>=2){
            QueueItem queueItem1 = queueItems.get(0);
            QueueItem queueItem2 = queueItems.get(1);
            //创建新的handler
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
            //从数据库中删除相应队列项
            queueRepository.delete(queueItem1);
            queueRepository.delete(queueItem2);
        }
    }


    public ResponseVO forwardMessage(ForwardingVO forwarding) {
        int senderId = forwarding.getSenderId();
        int receiverId = forwarding.getReceiverId();
        //转发消息
        webSocketServer
                .send(receiverId,new MessageVO(MessageVO.FORWARDING,forwarding));

        //更新handler
        MatchHandler handler = handlers.get(correspondingMap.get(senderId));
        handler.changeStatus(senderId,true);
        return new ResponseVO(ResultCode.SUCCESS);
    }
}
