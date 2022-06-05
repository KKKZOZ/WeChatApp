package com.kkkzoz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kkkzoz.mapper.QuestionDTOMapper;
import com.kkkzoz.match.MatchHandler;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeChatAppApplicationTests {


//    private final QuestionDTOMapper questionDTOMapper;

//    @Test
//    public void testMapper(){
//        System.out.println(questionDTOMapper.getQuestionDTOById(1));
//    }


    @Test
    void contextLoads() {
    }

    @Test
    void testMatchHandler(){
        MatchHandler matchHandler = new MatchHandler("1","2",1,3);
        System.out.println(matchHandler.getQuestionIdList().toString());
    }

    @Test
    void testJson(){
        String msg="{\"data\":\"{opponentId:100}\",\"msg\":\"start\"}";
        JSONObject jsonObject = JSON.parseObject(msg);
        System.out.println(jsonObject);
    }

}
