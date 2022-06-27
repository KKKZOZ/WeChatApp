package com.kkkzoz.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageVO<T> {



    public static final String Init = "Init";

    public static final String START = "start";


    public static final String MATCH_CONTENT = "match_content";

    public static final String FORWARDING ="forwarding";

    public static final String TERMINATE = "terminate";

    public static final String OVER = "over";



    private final String msg;

    private final T data;

    public MessageVO(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }


}

