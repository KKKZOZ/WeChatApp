package com.kkkzoz;

import com.kkkzoz.mapper.QuestionDTOMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
class WeChatAppApplicationTests {

    private final QuestionDTOMapper questionDTOMapper;

    @Test
    public void testMapper(){
        System.out.println(questionDTOMapper.getQuestionDTOById(1));
    }


    @Test
    void contextLoads() {
    }

}
