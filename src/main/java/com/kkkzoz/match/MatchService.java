package com.kkkzoz.match;


import com.kkkzoz.domain.entity.HistoryMatchItem;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.repository.HistoryMatchRepository;
import com.kkkzoz.repository.QueueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class MatchService {

    private final HistoryMatchRepository historyMatchRepository;


    public ResponseVO saveMatchItem(HistoryMatchItem historyMatchItem) {
        historyMatchRepository
                .save(historyMatchItem);

        return new ResponseVO(ResultCode.SUCCESS);
    }


    public List<HistoryMatchItem> getHistoryMatches(int userId) {
        return historyMatchRepository.findByUserId(userId);
    }
}
