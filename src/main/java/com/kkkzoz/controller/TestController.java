package com.kkkzoz.controller;

import com.kkkzoz.global.NotResponseBody;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.message.MessageVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final WebSocketServer webSocketServer;


    @GetMapping("/1")
    public String getTest1() {
        log.info("getTest1");
        for (int j=1;j<=10;j++){
            for (int i=1;i<=Integer.MAX_VALUE-1;i++){

            }
        }

        return this.toString();
    }

    @GetMapping("/2")
    @NotResponseBody
    public String getTest2() {
        log.info("getTest2");
        return "test2";
    }


    @PostMapping("/3")
    public ResponseVO testSendingMessage(@RequestBody Map<String,String> map){
//        int receiverId = Integer.parseInt(map.get("receiverId"));
//        String message = map.get("message");
//        webSocketServer.send(receiverId,new MessageVO(message,message));

        return new ResponseVO(ResultCode.SUCCESS);
    }
}
