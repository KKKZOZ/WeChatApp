package com.kkkzoz.controller;

import com.kkkzoz.global.NotResponseBody;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.message.MessageVO;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
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
        GoEasy goEasy = new GoEasy("https://rest-hangzhou.goeasy.io", "BC-69353f4a5e76404d8a2c5dd26600654e");
        goEasy.publish("kkkzoz", "Hello KKKZOZ",new PublishListener(){
            @Override
            public void onSuccess() {
                System.out.println("Publish success.");
            }

            @Override
            public void onFailed(GoEasyError error) {
                System.out.println("Failed to Publish message, error:" + error.getCode() + " , " + error.getContent());
            }
        });

        return "OK";
    }


    @PostMapping("/3")
    public ResponseVO testSendingMessage(@RequestBody Map<String,String> map){
//        int receiverId = Integer.parseInt(map.get("receiverId"));
//        String message = map.get("message");
//        webSocketServer.send(receiverId,new MessageVO(message,message));

        return new ResponseVO(ResultCode.SUCCESS);
    }
}
