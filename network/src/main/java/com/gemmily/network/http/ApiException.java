package com.gemmily.network.http;

/**
 * Created by Gemmily on 2017/3/7.
 */
public class ApiException extends RuntimeException {
    private String msg;

    public ApiException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
