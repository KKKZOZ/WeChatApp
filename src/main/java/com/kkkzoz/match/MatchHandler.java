package com.kkkzoz.match;


import com.kkkzoz.controller.WebSocketServer;
import com.kkkzoz.domain.entity.Match;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.mapper.QuestionDTOMapper;
import com.kkkzoz.message.MessageVO;
import com.kkkzoz.service.QuestionService;
import com.kkkzoz.utils.SpringUtilsAuTo;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
public class MatchHandler {

    private final MatchGenerator matchGenerator= SpringUtilsAuTo.getBean(MatchGenerator.class);

    private final WebSocketServer webSocketServer = SpringUtilsAuTo.getBean(WebSocketServer.class);

    private final QuestionDTOMapper questionDTOMapper = SpringUtilsAuTo.getBean(QuestionDTOMapper.class);

    private Match match;

    private int category;

    private int size;

    private int userAId;
    private int userBId;

    private boolean userAStatus=false;
    private boolean userBStatus=false;

    private int progress;

    private List<Integer> questionIdList;


    public MatchHandler(int userAId, int userBId,int category,int size) {
        this.userAId = userAId;
        this.userBId = userBId;
        this.category = category;
        this.size = size;
        this.questionIdList= matchGenerator.generateQuestionList(category,size);
        this.progress=0;

        //互相发送userId
        this.initialize();
    }

    //互相发送userId,并发送第一道题
    private void initialize() {
        webSocketServer
                .send(userAId,new MessageVO<>(MessageVO.START,"{opponentId:"+userBId+"}"));
        webSocketServer
                .send(userBId,new MessageVO<>(MessageVO.START,"{opponentId:"+userAId+"}"));

        //发送第一道题
        this.sendQuestion(userAId);
        this.sendQuestion(userBId);
    }


    private QuestionDTO getQuestion(int questionId,int category){
        if (category==1){
            return questionDTOMapper.getQuestionOneDTOById(questionId);
        }
        if (category==4){
            return questionDTOMapper.getQuestionFourDTOById(questionId);
        }
        return null;
    }

    public void checkProgress() {
        //如果两个人都答完了，则进入下一题
        if (userAStatus && userBStatus){
            this.progress++;
            if (this.progress==this.size){
                //比赛结束
                webSocketServer.send(userAId,new MessageVO(MessageVO.OVER,questionIdList));
                webSocketServer.send(userBId,new MessageVO(MessageVO.OVER,questionIdList));
            }

            this.sendQuestion(userAId);
            this.sendQuestion(userBId);

            this.userAStatus=false;
            this.userBStatus=false;
        }
    }



    public void sendQuestion(int userId){
        webSocketServer
                .send(userId, new MessageVO(
                        MessageVO.MATCH_CONTENT,
                        getQuestion(questionIdList.get(progress),category)));
    }




    public void changeStatus(int userId,boolean status){
        if (userId==userAId){
            this.setUserAStatus(status);
        }
        if (userId==userBId){
            this.setUserBStatus(status);
        }
    }


    public void setUserAStatus(boolean userAStatus) {
        this.userAStatus = userAStatus;
        this.checkProgress();
    }

    public void setUserBStatus(boolean userBStatus) {
        this.userBStatus = userBStatus;
        this.checkProgress();
    }
}
