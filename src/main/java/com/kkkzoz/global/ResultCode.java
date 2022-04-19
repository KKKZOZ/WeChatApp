package com.kkkzoz.global;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(999, "响应失败"),
    LOGIN_FAILED(1000,"用户名或者密码错误"),
    LOGIN_SUCCESS(1001,"登录成功"),
    LOGOUT_SUCCESS(1002,"退出成功"),
    ERROR(5000, "未知错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
