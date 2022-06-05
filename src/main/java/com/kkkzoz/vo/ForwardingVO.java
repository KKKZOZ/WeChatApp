package com.kkkzoz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForwardingVO {

    private String senderId;
    private String receiverId;
    private boolean correct;
    private int score;

}
