package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.HistoryMatchItem;
import com.kkkzoz.domain.entity.QueueItem;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.match.MatchManager;
import com.kkkzoz.match.MatchService;
import com.kkkzoz.repository.HistoryMatchRepository;
import com.kkkzoz.utils.SecurityUtil;
import com.kkkzoz.vo.ForwardingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/match")
@AllArgsConstructor
public class MatchController {

    private final MatchManager matchManager;

    private final MatchService matchService;




    @PostMapping("/forwarding")
    @ApiOperation(value="对手之间转发消息")
    public ResponseVO forwarding(@RequestBody ForwardingVO forwarding){
        log.info("MatchController forwarding");
        String userId = SecurityUtil.getUserId();
        forwarding.setSenderId(userId);
      return  matchManager.forwardMessage(forwarding);
    }

    @PostMapping("/start")
    @ApiOperation(value="开始匹配")
    public ResponseVO startMatch(@RequestBody QueueItem queueItem){
        log.info("MatchController startMatch userId:{}", SecurityUtil.getUserId());
        String userId = SecurityUtil.getUserId();
        queueItem.setUserId(userId);
        matchManager.addUserToQueue(queueItem);
        return new ResponseVO(ResultCode.SUCCESS);
    }



    @PostMapping("/start/robot")
    @ApiOperation(value="开始人机匹配")
    public Map<String, Object> startRobotMatch(@RequestBody QueueItem queueItem){
        String userId = SecurityUtil.getUserId();
        queueItem.setUserId(userId);
        return matchService.startRobotMatch(queueItem);
    }
    @GetMapping("/stop/queue")
    @ApiOperation(value="停止匹配")
    public ResponseVO stopQueuing(){
        log.info("userId:{} 停止匹配", SecurityUtil.getUserId());
        String userId = SecurityUtil.getUserId();
        return matchManager.removeUserFromQueue(userId);

    }

    @GetMapping("/stop/match")
    @ApiOperation(value="停止比赛")
    public ResponseVO stopMatch(){
        log.info("userId:{} 停止比赛", SecurityUtil.getUserId());
        String userId = SecurityUtil.getUserId();
        return matchManager.removeUserFromMatch(userId);

    }

    @GetMapping("/over")
    @ApiOperation(value="结束比赛时调用，方便后端整理资源")
    public ResponseVO overMatch(){
        String userId = SecurityUtil.getUserId();
       return matchManager.releaseResource(userId);
    }

    @PostMapping()
    @ApiOperation(value="保存历史比赛信息")
    public ResponseVO saveMatch(@RequestBody HistoryMatchItem historyMatchItem){
       return  matchService.saveMatchItem(historyMatchItem);
    }

    @GetMapping()
    @ApiOperation(value="获取历史比赛信息")
    public List<HistoryMatchItem> getHistoryMatches(@RequestParam("userId") int userId){
        return matchService.getHistoryMatches(userId);
    }



}
