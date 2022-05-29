package com.kkkzoz.service;

import com.kkkzoz.utils.TimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UtilService {

    private final TimeUtil timeUtil;

    public int getWeekOfYear() {
        timeUtil.updateToCurrent();
        return timeUtil.getWeekOfYear();
    }
}