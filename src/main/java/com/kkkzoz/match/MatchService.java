package com.kkkzoz.match;


import com.kkkzoz.domain.entity.HistoryMatchItem;
import com.kkkzoz.domain.entity.QueueItem;
import com.kkkzoz.dto.QuestionDTO;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.repository.HistoryMatchRepository;
import com.kkkzoz.service.QuestionService;
import com.kkkzoz.utils.SecurityUtil;
import com.kkkzoz.vo.RobotStatusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
@AllArgsConstructor
public class MatchService {

    private final HistoryMatchRepository historyMatchRepository;

    private static final int DEFAULT_COUNT = 5;

    private static final int MIN_TIME = 0;

    private static final int MAX_TIME = 15;

    private static final int POSSIBILITY = 70;

    private final QuestionService questionService;


    public ResponseVO saveMatchItem(HistoryMatchItem historyMatchItem) {
        String userId = SecurityUtil.getUserId();
        historyMatchItem.setUserId(userId);
        log.info("saveMatchItem: {}", historyMatchItem);
        historyMatchRepository
                .save(historyMatchItem);

        return new ResponseVO(ResultCode.SUCCESS);
    }


    public List<HistoryMatchItem> getHistoryMatches(int userId) {
        return historyMatchRepository.findByUserId(userId);
    }

    public Map<String, Object> startRobotMatch(QueueItem queueItem) {
        //开始人机匹配
        //先确定题目列表
        List<QuestionDTO> questionList = questionService.generateTest(queueItem.getCategory(), DEFAULT_COUNT);
        //开始随机生成用时和答案
        List<RobotStatusVO> statusList = new ArrayList<>();
        for (int i = 0; i < DEFAULT_COUNT; i++) {
            RobotStatusVO statusVO = new RobotStatusVO();
            Random random = new Random();
            int target = random.nextInt(1,101);
            statusVO.setAnswer(target <= POSSIBILITY);
            statusVO.setTime(random.nextInt(MIN_TIME, MAX_TIME));
            statusList.add(statusVO);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("questionList", questionList);
        map.put("statusList", statusList);
        return map;


    }
}
