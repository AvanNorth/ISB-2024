package com.example.demo.models;

public class BaseResponse {
    private final String[] response;
    private final Integer code;

    public BaseResponse(String[] response, Integer code) {
        this.response = response;
        this.code = code;
    }

    public String[] getResponse() {
        return response;
    }

    public Integer getCode() {
        return code;
    }
}