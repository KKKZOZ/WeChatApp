package com.kkkzoz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForwardingVO {

    private int senderId;
    private int receiverId;
    private boolean correct;
    private int score;

}
