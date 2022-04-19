package com.kkkzoz.global;

import lombok.Getter;

@Getter
public class ResponseVO<T> {

    private final int code;
    private final String message;
    private final T data;


    public ResponseVO(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public ResponseVO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

}
