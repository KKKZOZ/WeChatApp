package com.kkkzoz.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Segment {

    private int segmentId;

    private int startTime;

    private int endTime;

    private int mode;

    private int occupy;

    private List<Long> members;

    private int practice;
}
