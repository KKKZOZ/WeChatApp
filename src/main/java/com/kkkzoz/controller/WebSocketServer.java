package com.kkkzoz.controller;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.utils.JwtUtil;
import com.kkkzoz.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket的处理类。
 * 作用相当于HTTP请求
 * 中的controller
 */
@Component
@Slf4j
@ServerEndpoint("/api/v2/match/{token}")
public class WebSocketServer {



    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private static ConcurrentHashMap<String ,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId;


    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        log.info("onOpen");
        String openId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            openId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        this.session = session;
        this.userId=openId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //加入set中
            webSocketMap.put(userId,this);
        }else{
            //加入set中
            webSocketMap.put(userId,this);
            //在线数加1
            addOnlineCount();
        }
        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
        this.send(userId,new MessageVO(MessageVO.Init,""));
    }

    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {

    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean send(String receiverId, MessageVO msg) {
        log.info("receiverId:{}",receiverId);
        synchronized (this){
            log.info("ConcurrentHashMap:{}", webSocketMap);
            //TODO: 如果receiverServer不存在，向sender方发送异常，强制结束这次比赛

            WebSocketServer receiverServer = webSocketMap.get(receiverId);
            if (receiverServer == null) {
                log.info("receiverServer不存在");
                return false;
            }
            try {

                String content = JSON.toJSONString(msg);

                receiverServer.session.getBasicRemote().sendText(content);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }





    public static synchronized int getOnlineCount() {
        return onlineCount;
    }


    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

